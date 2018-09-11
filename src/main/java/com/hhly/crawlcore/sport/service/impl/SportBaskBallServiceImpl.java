package com.hhly.crawlcore.sport.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.NumberUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.base.zeroc.WebClientManager;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBbBssDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBbSpDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBbWfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBbWsDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportStatusBbDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBbBssPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBbSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBbWfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBbWsPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportStatusBbPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.rabbitmq.provider.MessageProvider;
import com.hhly.crawlcore.sport.service.CrawlerService;
import com.hhly.crawlcore.sport.service.ResetSportCacheService;
import com.hhly.crawlcore.sport.service.SportBaskBallService;
import com.hhly.crawlcore.sport.service.SportBjMatchService;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.JcMatchStatusEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.common.SportEnum.WfTypeEnum;
import com.hhly.skeleton.base.constants.MQConstants;
import com.hhly.skeleton.base.mq.msg.JlSpMessageData;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.NumberFormatUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.sport.bo.JclqTrendSpBO;

/**
 * @auth lgs on
 * @date 2017/2/13.
 * @desc 抓取竞彩篮球数据service实现类
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
@Service
public class SportBaskBallServiceImpl implements SportBaskBallService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportBaskBallServiceImpl.class);

    @Value("${baskball_match_interim}")
    private String BASKBALL_MATCH_INTERIM;

    @Value("${baskball_match_list}")
    private String BASKBALL_MATCH_LIST;

    @Value("${baskball_wx_match_list}")
    private String BASKBALL_WX_MATCH_LIST;

    @Value("${bk_match_mnl}")
    private String BK_MATCH_MNL;

    @Value("${bk_match_odds_calculator}")
    private String BK_MATCH_ODDS_CALCULATOR;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

    @Autowired
    private SportStatusBbDaoMapper sportStatusBBDaoMapper;

    @Autowired
    private SportTeamInfoService sportTeamInfoService;

    @Autowired
    private SportMatchInfoService sportMatchInfoService;

    @Autowired
    private SportDataBbBssDaoMapper sportDataBbSSSDaoMapper;

    @Autowired
    private SportDataBbWfDaoMapper sportDataBbWFDaoMapper;

    @Autowired
    private SportDataBbWsDaoMapper sportDataBbWSDaoMapper;

    @Autowired
    private SportDataBbSpDaoMapper sportDataBBSpDaoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ResetSportCacheService resetSportCacheService;

    @Autowired
    private WebClientManager webClientManager;

    /**
     * 队阵信息
     */
    @Resource
    private SportBjMatchService sportBjMatchService;

    @Resource
    private MessageProvider messageProvider;

    @Resource
    private MQUtils mqUtils;


    @Autowired
    private LotteryIssueDaoMapper lotteryIssueMapper;

    /**
     * 抓取数据标准
     */
    private boolean flag = false;

    @Override
    public void saveMatchInterim() {

        try {
        	LOGGER.info("抓取竞技篮球对阵赛事信息  ----------- start" + BASKBALL_MATCH_INTERIM);
            List<SportAgainstInfoPO> pos = crawlerService.crawlerMatchInterim(BASKBALL_MATCH_INTERIM, LotteryEnum.Lottery.BB.getName(), String.valueOf(SportEnum.MatchTypeEnum.BASKETBALL.getCode()));
            LOGGER.info("抓取竞技篮球的赛事结果信息 ---------------- size : " + pos.size());
            if (pos.size() > 0) {
                for (SportAgainstInfoPO po : pos) {
                    int flag = sportAgainstInfoDaoMapper.updateInterimMatch(po);
                    if (flag == 0) {
                        sportAgainstInfoDaoMapper.insert(po);
                    }
                }
                //sportAgainstInfoDaoMapper.saveMatchInterim(pos);
                LOGGER.info("crawler bask ball match interim Size is:" + pos.size() + " Data is:" + JsonUtil.objectToJson(pos));
                LOGGER.info("抓取竞技篮球的赛事结果信息 ---------------- end"); 
            }
        } catch (Exception e) {
            LOGGER.error("crawler bask ball match interim Error message: " + e.getMessage());
        }
    }

    @Override
    public void saveMatchList() {

        Document doc = null;
        try {
            doc = ParseHtmlUtil.getDocument(BASKBALL_MATCH_LIST + "?=" + new Date().getTime());
        } catch (Exception e) {
            //
        }
        Elements els = ParseHtmlUtil.getSelect(doc, ".match_list tr");
        Map<String, String> map = ParseHtmlUtil.getJavaScriptData(doc, 5);
        String objMatchInfo = map.get("obj_match_info"); //提示信息
        Map<String, String> jsonMap = new HashMap<>();
        try {
            jsonMap = JsonUtil.jsonToObject(objMatchInfo, Map.class);
        } catch (Exception e) {
        }
        List<SportAgainstInfoPO> pos = new ArrayList<>();
        List<SportStatusBbPO> statusPos = new ArrayList<>();
        for (Element el : els) {
            try {
                Elements tds = ParseHtmlUtil.getElementsByTag(el, "td");

                String official = tds.eq(0).text(); //官方编号
                if (StringUtils.isBlank(official)) {
                    continue;
                }
                
                Elements td2 = tds.eq(2); //球队信息
                Elements ael = ParseHtmlUtil.getSelect(td2, "a");
                String href = ael.attr("href");
                String officialId = StringUtils.stripStart(href, "http://info.sporttery.cn/basketball/info/bk_match_mnl.php?m=");//获取官方id
                
                SportAgainstInfoPO paramPO = new SportAgainstInfoPO();
                paramPO.setLotteryCode(LotteryEnum.Lottery.BB.getName());
                paramPO.setOfficialId(officialId);
                Long sportAgainstInfoId = sportAgainstInfoDaoMapper.findId(paramPO);
                if(ObjectUtil.isBlank(sportAgainstInfoId))
                	continue;

                String wfStatus = ParseHtmlUtil.getSelect(tds.eq(6), "div").attr("class");//获取胜负状态
                String letWfStatus = ParseHtmlUtil.getSelect(tds.eq(7), "div").attr("class");//获取让球胜负状态
                String bigSmallStatus = ParseHtmlUtil.getSelect(tds.eq(8), "div").attr("class");//获取大小分状态
                String scoreStatus = ParseHtmlUtil.getSelect(tds.eq(9), "div").attr("class");//获取胜分差状态
                if (StringUtil.isBlank(wfStatus) && StringUtil.isBlank(letWfStatus)) {
                    continue;
                }

                SportStatusBbPO statusPo = new SportStatusBbPO(sportAgainstInfoId,
                        Short.valueOf(SportLotteryUtil.getChildPlayStatus(wfStatus)),
                        Short.valueOf(SportLotteryUtil.getChildPlayStatus(letWfStatus)),
                        Short.valueOf(SportLotteryUtil.getChildPlayStatus(bigSmallStatus)),
                        Short.valueOf(SportLotteryUtil.getChildPlayStatus(scoreStatus)),
                        SportDataSource.JC_OFFICIAL.getName());

                
                int flag = sportStatusBBDaoMapper.selectCount(statusPo);
                if (flag == 0) {
                    sportStatusBBDaoMapper.insert(statusPo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("crawler bask ball match list Error message: " + e.getMessage());
            }
        }
        LOGGER.debug("crawler bask ball match list Size is:" + pos.size() + " Data is:" + JsonUtil.objectToJson(pos));
    }


    @Override
    public void saveWxMatchList() {
    	boolean msgFlag = false;
        String url = BASKBALL_WX_MATCH_LIST + "&_=" + new Date().getTime();
        List<SportAgainstInfoPO> sportAgainstInfoPOS = crawlerService.crawlerWxMatchList(url, LotteryEnum.Lottery.BB.getName(), String.valueOf(SportEnum.MatchTypeEnum.BASKETBALL.getCode()));
        for (SportAgainstInfoPO po : sportAgainstInfoPOS) {
            //禅道任务ID1481，当竞彩比赛场次为暂停销售或延期时，重新抓取时不覆盖这两个状态，需要做变更时，人工进行维护
           /* if (resultPO.getMatchStatus() != Constants.NUM_10 || resultPO.getMatchStatus() != Constants.NUM_13) {
                sportAgainstInfoDaoMapper.updateFootBall(againstInfoPO);
            }*/
        	SportAgainstInfoPO resultPO = sportAgainstInfoDaoMapper.findByCode(po.getLotteryCode(), po.getIssueCode(), po.getSystemCode());
        	
    		if(!ObjectUtil.isBlank(resultPO)){
    			if(JcMatchStatusEnum.isUpdate(resultPO.getMatchStatus()))
    				continue;
    			po.setId(resultPO.getId());
    			//通过状态判断是否要再次进去判断时间
    			if(!msgFlag)
    	         if (po.getStartTime().compareTo(resultPO.getStartTime()) != 0) {
    	             mqUtils.sendMessage(po.getIssueCode(), po.getLotteryCode());
    	         	 msgFlag = true;
    	         }
    		}
    		sportAgainstInfoDaoMapper.merge(po);
    		
//            if (!ObjectUtil.isBlank(resultPO)) {
//                if (resultPO.getMatchStatus() != Constants.NUM_10 || resultPO.getMatchStatus() != Constants.NUM_13) {
//                	po.setId(resultPO.getId());
//                    sportAgainstInfoDaoMapper.update(po);
//                }
//            } else {
//                sportAgainstInfoDaoMapper.insert(po);
//            }
        }

        //如果对阵有多条比赛时间有变化，也只发一条MQ消息
//        for (SportAgainstInfoPO sportAgainstInfoPO : sportAgainstInfoPOS) {
//            SportAgainstInfoPO queryPO = new SportAgainstInfoPO();
//            queryPO.setIssueCode(sportAgainstInfoPO.getIssueCode());
//            queryPO.setLotteryCode(sportAgainstInfoPO.getLotteryCode());
//            queryPO.setOfficialMatchCode(sportAgainstInfoPO.getOfficialMatchCode());
//            List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findCondition(queryPO);
//            SportAgainstInfoPO resultPO;
//            if (!ObjectUtil.isBlank(list)) {
//                resultPO = list.get(0);
//                if (sportAgainstInfoPO.getStartTime().compareTo(resultPO.getStartTime()) != 0) {
//                    //调用MQ发消息
//                    mqUtils.sendMessage(sportAgainstInfoPO.getIssueCode(), sportAgainstInfoPO.getLotteryCode());
//                    break;
//                }
//            }
//        }
    }


    @Override
    public JclqTrendSpBO saveBkMatchMnl(String officialId, Long sportAgainstInfoId) {
        String url = BK_MATCH_MNL.replace("{0}", officialId) + "&time=" + new Date().getTime();

        List<SportDataBbBssPO> sssPos = new ArrayList<>();
        List<SportDataBbWfPO> wfPos = new ArrayList<>();
        List<SportDataBbWfPO> letWfPos = new ArrayList<>();
        List<SportDataBbWsPO> wsPos = new ArrayList<>();
        JclqTrendSpBO spTrendBO = null;//保存篮球SP值变化
        try {
            Document doc = ParseHtmlUtil.getDocument(url);
            Elements tabls = doc.select("table");
            Elements wfEl = ParseHtmlUtil.getSelect(tabls.eq(0), "tr");//胜负固定奖金
            Elements letWfEl = ParseHtmlUtil.getSelect(tabls.eq(1), "tr");//让分胜负固定奖金
            Elements bigSmallEl = ParseHtmlUtil.getSelect(tabls.eq(2), "tr");//大小分固定奖金
            Elements wsEl = ParseHtmlUtil.getSelect(tabls.eq(3), "tr");//胜分差固定奖金

            for (int i = 2; i < wfEl.size(); i++) {
                Element el = wfEl.get(i);
                Elements tdEl = ParseHtmlUtil.getElementsByTag(el, "td");
                String homeLostSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 1);
                if (StringUtils.isBlank(homeLostSp)) {
                    continue;
                }
    
                String homeWinSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 2);
                String dateTime = ParseHtmlUtil.getElementsEqIndexText(tdEl, 0);
                
                SportDataBbWfPO po = new SportDataBbWfPO();
                po.setSpWin(NumberUtils.parseNumber(homeWinSp, Float.class));
                po.setSpFail(NumberUtils.parseNumber(homeLostSp, Float.class));
                po.setSportAgainstInfoId(sportAgainstInfoId);
                po.setReleaseTime(DateUtil.convertStrToDate(dateTime));
                po.setSpType(WfTypeEnum.NOT_LET.getValue());//非让球SP值
                wfPos.add(po);
            }
    
            for (int i = 1 ; i < letWfEl.size() ; i++) {
                Element el = letWfEl.get(i);
                Elements tdEl = ParseHtmlUtil.getElementsByTag(el, "td");
                String homeLostSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 1);
                if (StringUtils.isBlank(homeLostSp)) {
                    continue;
                }
                String letScore = ParseHtmlUtil.getElementsEqIndexText(tdEl, 2);
                String homeWinSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 3);
                String dateTime = ParseHtmlUtil.getElementsEqIndexText(tdEl, 0);

                SportDataBbWfPO po = new SportDataBbWfPO();
                po.setSpWin(NumberUtils.parseNumber(homeWinSp, Float.class));
                po.setSpFail(NumberUtils.parseNumber(homeLostSp, Float.class));
                po.setSportAgainstInfoId(sportAgainstInfoId);
                po.setReleaseTime(DateUtil.convertStrToDate(dateTime));
                po.setLetScore(NumberUtils.parseNumber(letScore, Float.class));
                po.setSpType(WfTypeEnum.LET.getValue());//让球SP值
                letWfPos.add(po);
            }
    
            for (int i = 1 ; i < bigSmallEl.size() ; i++) {
                Element el = bigSmallEl.get(i);
                Elements tdEl = ParseHtmlUtil.getElementsByTag(el, "td");
                String bigSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 1);//大分
                if (StringUtils.isBlank(bigSp)) {
                    continue;
                }
                String score = ParseHtmlUtil.getElementsEqIndexText(tdEl, 2);//预设总分
                String smallSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 3);//小分
                String dateTime = ParseHtmlUtil.getElementsEqIndexText(tdEl, 0);//发布时间

                SportDataBbBssPO po = new SportDataBbBssPO();
                po.setSpBig(NumberUtils.parseNumber(bigSp, Float.class));
                po.setSpSmall(NumberUtils.parseNumber(smallSp, Float.class));
                po.setPresetScore(NumberUtils.parseNumber(score, Float.class));
                po.setSportAgainstInfoId(sportAgainstInfoId);
                po.setReleaseTime(DateUtil.convertStrToDate(dateTime));
                sssPos.add(po);
            }

            for (int i = 3; i < wsEl.size(); i++) {
                Element el = wsEl.get(i);
                Elements tdEl = ParseHtmlUtil.getElementsByTag(el, "td");
                String spFail15 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 1);

                if (StringUtils.isBlank(spFail15)) {
                    continue;
                }
                String spFail610 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 2);
                String spFail1115 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 3);
                String spFail1620 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 4);
                String spFail2125 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 5);
                String spFail26 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 6);
                String spWin15 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 7);
                String spWin610 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 8);
                String spWin1115 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 9);
                String spWin1620 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 10);
                String spWin2125 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 11);
                String spWin26 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 12);
    
                String dateTime = ParseHtmlUtil.getElementsEqIndexText(tdEl, 0);

                SportDataBbWsPO po = new SportDataBbWsPO();
                po.setSportAgainstInfoId(sportAgainstInfoId);
                po.setSpFail15(NumberUtils.parseNumber(spFail15, Float.class));
                po.setSpFail610(NumberUtils.parseNumber(spFail610, Float.class));
                po.setSpFail1115(NumberUtils.parseNumber(spFail1115, Float.class));
                po.setSpFail1620(NumberUtils.parseNumber(spFail1620, Float.class));
                po.setSpFail2125(NumberUtils.parseNumber(spFail2125, Float.class));
                po.setSpFail26(NumberUtils.parseNumber(spFail26, Float.class));
                po.setSpWin15(NumberUtils.parseNumber(spWin15, Float.class));
                po.setSpWin610(NumberUtils.parseNumber(spWin610, Float.class));
                po.setSpWin1115(NumberUtils.parseNumber(spWin1115, Float.class));
                po.setSpWin1620(NumberUtils.parseNumber(spWin1620, Float.class));
                po.setSpWin2125(NumberUtils.parseNumber(spWin2125, Float.class));
                po.setSpWin26(NumberUtils.parseNumber(spWin26, Float.class));
                po.setReleaseTime(DateUtil.convertStrToDate(dateTime));
                wsPos.add(po);
            }

            //查看篮球SP值变化
            SportDataBbWfPO wfTrendPo = new SportDataBbWfPO();
            SportDataBbWfPO letWfTrendPo = new SportDataBbWfPO();
            SportDataBbBssPO bbSSSPO = new SportDataBbBssPO();
            SportDataBbWsPO wSPo = new SportDataBbWsPO();
            if (sssPos.size() > 0) {
                //sportDataBbSSSDaoMapper.saveBBS(sssPos);
                SportDataBbBssPO po = sssPos.get(sssPos.size() - 1);
                int count = sportDataBbSSSDaoMapper.selectCount(po);
                if (count <= 1) {
                    bbSSSPO = po;
                }

                for (SportDataBbBssPO ssspo : sssPos) {
                    int flag = sportDataBbSSSDaoMapper.selectCount(ssspo);
                    if (flag == 0) {
                        sportDataBbSSSDaoMapper.insert(ssspo);
                    }
                }
            }
            if (wfPos.size() > 0) {
                SportDataBbWfPO po = wfPos.get(wfPos.size() - 1);
                int count = sportDataBbWFDaoMapper.selectCount(po);
                if (count <= 1) {
                    wfTrendPo = po;
                }

                for (SportDataBbWfPO wfPo : wfPos) {
                    int flag = sportDataBbWFDaoMapper.selectCount(wfPo);
                    if (flag == 0) {
                        sportDataBbWFDaoMapper.insert(wfPo);
                    }
                }
            }

            if (letWfPos.size() > 0) {
                SportDataBbWfPO po = letWfPos.get(letWfPos.size() - 1);
                int count = sportDataBbWFDaoMapper.selectCount(po);
                if (count <= 1) {
                    letWfTrendPo = po;
                }

                for (SportDataBbWfPO wfPo : letWfPos) {
                    int flag = sportDataBbWFDaoMapper.selectCount(wfPo);
                    if (flag == 0) {
                        sportDataBbWFDaoMapper.insert(wfPo);
                    }
                }
            }

            if (wsPos.size() > 0) {
                SportDataBbWsPO po = wsPos.get(wsPos.size() - 1);
                int count = sportDataBbWSDaoMapper.selectCount(po);
                if (count <= 1) {
                    wSPo = po;
                }

                for (SportDataBbWsPO ws : wsPos) {
                    int flag = sportDataBbWSDaoMapper.selectCount(ws);
                    if (flag == 0) {
                        sportDataBbWSDaoMapper.insert(ws);
                    }
                }
            }

            //只要有一个有变化就满足，封装指定格式存入缓存
            if (!ObjectUtil.isBlank(wfTrendPo.getSportAgainstInfoId()) || !ObjectUtil.isBlank(letWfTrendPo.getSportAgainstInfoId()) || !ObjectUtil.isBlank(bbSSSPO.getSportAgainstInfoId()) || !ObjectUtil.isBlank(wSPo.getSportAgainstInfoId())) {
                //用于保存sp值变化 key=对阵id value=[w,f,let_score,let_w,let_f]

                //胜负/让分胜负变化
                String[] wf = new String[]{
                        NumberFormatUtil.format(wfTrendPo.getSpWin()),
                        NumberFormatUtil.format(wfTrendPo.getSpFail()),
                        DateUtil.convertDateToStr(wfTrendPo.getReleaseTime()),
                        NumberFormatUtil.format(letWfTrendPo.getLetScore(), NumberFormatUtil.ONE_DECIMAL_POINT),
                        NumberFormatUtil.format(letWfTrendPo.getSpWin()),
                        NumberFormatUtil.format(letWfTrendPo.getSpFail()),
                        DateUtil.convertDateToStr(letWfTrendPo.getReleaseTime())
                };
                //大小分变化
                String[] bs = new String[]{
                        NumberFormatUtil.format(bbSSSPO.getPresetScore(), NumberFormatUtil.ONE_DECIMAL_POINT),
                        NumberFormatUtil.format(bbSSSPO.getSpBig()),
                        NumberFormatUtil.format(bbSSSPO.getSpSmall()),
                        DateUtil.convertDateToStr(bbSSSPO.getReleaseTime())
                };

                //让分差分变化
                String[] winArray = new String[]{
                        NumberFormatUtil.format(wSPo.getSpWin15()),
                        NumberFormatUtil.format(wSPo.getSpWin610()),
                        NumberFormatUtil.format(wSPo.getSpWin1115()),
                        NumberFormatUtil.format(wSPo.getSpWin1620()),
                        NumberFormatUtil.format(wSPo.getSpWin2125()),
                        NumberFormatUtil.format(wSPo.getSpWin26()),
                        DateUtil.convertDateToStr(wSPo.getReleaseTime())};

                String[] failArray = new String[]{
                        NumberFormatUtil.format(wSPo.getSpFail15()),
                        NumberFormatUtil.format(wSPo.getSpFail610()),
                        NumberFormatUtil.format(wSPo.getSpFail1115()),
                        NumberFormatUtil.format(wSPo.getSpFail1620()),
                        NumberFormatUtil.format(wSPo.getSpFail2125()),
                        NumberFormatUtil.format(wSPo.getSpFail26()),
                        DateUtil.convertDateToStr(wSPo.getReleaseTime())};

                Map<String, Object> ws = new HashMap<>();
                ws.put("win", winArray);
                ws.put("lost", failArray);
                spTrendBO = new JclqTrendSpBO(sportAgainstInfoId, wf, ws, bs);

            }


            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("crawler bask ball odd history sss Size is:" + sssPos.size() + "Data is:" + JsonUtil.objectToJson(sssPos));
                LOGGER.debug("crawler bask ball odd history wf Size is:" + wfPos.size() + "Data is:" + JsonUtil.objectToJson(wfPos));
                LOGGER.debug("crawler bask ball odd history ws Size is:" + wsPos.size() + "Data is:" + JsonUtil.objectToJson(wsPos));
            }
        } catch (Exception e) {
            LOGGER.error("crawler bask ball odd history Error message:" + e.getMessage());
            //e.printStackTrace();
        }

        return spTrendBO;
    }


    @Override
    public synchronized void saveBkMatchMnlList() {
        if (flag) {
            LOGGER.info("crawler bask ball odd history Error message 正在执行抓取");
        }
        flag = true;
        try {
            long beginTime = System.currentTimeMillis();
            List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.BB.getName());
            List<JclqTrendSpBO> results = new ArrayList<>();
            for (SportAgainstInfoPO po : list) {
                try {
                    JclqTrendSpBO resultBO = saveBkMatchMnl(po.getOfficialId(), po.getId());
                    if (!ObjectUtil.isBlank(resultBO)) {
                        results.add(resultBO);
                    }
                } catch (Exception e) {
                    LOGGER.error("crawler bask ball  odd official" + po.getOfficialId() + "againstId:" + po.getId() + " history list Error message:" + e.getMessage());
                }
            }
            //有变化丢缓存
            if (results.size() > 0) {
                // 竞篮SP值变化推送
                JlSpMessageData jlSpMessageData = new JlSpMessageData();
                jlSpMessageData.setList(results);
                LOGGER.debug("竞篮sp值变化推送  " +MQConstants.BASKETBALL_SP_RABBITMQ_NAME + "jlSpMessageData : " + JSONObject.toJSONString(jlSpMessageData));
                mqUtils.sendSpMessage(MQConstants.BASKETBALL_SP_RABBITMQ_NAME, jlSpMessageData);

                for (JclqTrendSpBO bo : results) {
                    resetSportCacheService.resetBBMatchWfHistorySpByIds(bo.getId());
                    resetSportCacheService.resetBBMatchWsHistorySpById(bo.getId());
                    resetSportCacheService.resetBBMatchBsHistorySpById(bo.getId());
                }
            }
            long endTime = System.currentTimeMillis();
            LOGGER.debug("crawler bask ball odds list success for time:" + ((endTime - beginTime) / 1000) + "S");
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        flag = false;
    }

    @Override
    public void proceeSportBBData() {
        String url = BK_MATCH_ODDS_CALCULATOR + "&_=" + new Date().getTime();
        try {
            String json = HTTPUtil.transRequest(url);
            if (json.equals("0")) {
                return;
            }
            String getData = StringUtils.stripEnd(StringUtils.stripStart(json, "getData("), ");");
            JSONObject getDataJson = JsonUtil.jsonToObject(getData, JSONObject.class);
            Object object = getDataJson.get("data");
            if (object instanceof JSONArray && ((JSONArray) object).size() <= 0) {
                return;
            }

            SportAgainstInfoPO againstInfoPO;
            Short matchType = SportEnum.MatchTypeEnum.BASKETBALL.getCode();
            String createBy = SportEnum.SportDataSource.JC_OFFICIAL.getName();//创建人
            Integer lotteryCode = LotteryEnum.Lottery.BB.getName();
            JSONObject dataJson = getDataJson.getJSONObject("data");
            if (ObjectUtil.isBlank(dataJson)) {
                return;
            }
            for (Map.Entry<String, Object> entry : dataJson.entrySet()) {
                JSONObject valueJson = JSONObject.parseObject(entry.getValue().toString());
                String officialId = valueJson.getString("id"); //官方id
                String matchName = valueJson.getString("l_cn");//赛事全称
                String matchNameAbbr = valueJson.getString("l_cn_abbr");//赛事简称
                String homeName = valueJson.getString("h_cn");//主队全称
                String guestName = valueJson.getString("a_cn");//客队全称
                String officialMatchCode = valueJson.getString("num");//官方编号
                String matchTimeStr = valueJson.getString("date") + " " + valueJson.getString("time");
                Date matchTime = DateUtil.convertStrToDate(matchTimeStr);//比赛时间
                String saleDate = valueJson.getString("b_date"); //销售时间
                String status = valueJson.getString("status");//销售状态
                String l_background_color = valueJson.getString("l_background_color");//赛事标签颜色
                String issueCode = DateUtil.convertDateToStr(DateUtil.convertStrToDate(saleDate, DateUtil.DATE_FORMAT), DateUtil.FORMAT_YYMMDD);
                //获取彩期信息
                List<LotteryIssuePO> getIssueInfo = lotteryIssueMapper.getIssueInfo(Integer.valueOf(lotteryCode));
                Date saleEndTime = SportLotteryUtil.getSaleEndTime(matchTime, issueCode, getIssueInfo); //销售截止时间
                String systemCode = SportLotteryUtil.getSystemCode(officialMatchCode, issueCode);

                Long sportAgainstInfoId = null;
                SportAgainstInfoPO resultPO = sportAgainstInfoDaoMapper.findByCode(lotteryCode, issueCode,
                		systemCode);
                
        		if(!ObjectUtil.isBlank(resultPO)){
        			if(JcMatchStatusEnum.isUpdate(resultPO.getMatchStatus()))
        				continue;
        			sportAgainstInfoId = resultPO.getId();
        		}

                String homeTeamUnion = null;
                String guestTeamUnion = null;
                String matchUnion = null;
                try {
                    homeTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(homeName + matchType).getBytes("UTF-8"));
                    guestTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(guestName + matchType).getBytes("UTF-8"));
                    matchUnion = DigestUtils.md5DigestAsHex(String.valueOf(matchName + matchType).getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("proceeSportBBData " + e.getMessage());
                }

                //获取球队信息
                SportTeamInfoPO homeTeam = new SportTeamInfoPO(matchType, homeName, null, homeTeamUnion, createBy, createBy);
                SportTeamInfoPO guestTeam = new SportTeamInfoPO(matchType, guestName, null, guestTeamUnion, createBy, createBy);
                SportMatchInfoPO matchInfo = new SportMatchInfoPO(matchName, matchNameAbbr, Short.valueOf(matchType), matchUnion, createBy);

                homeTeam = sportTeamInfoService.findTeam(homeTeam);
                guestTeam = sportTeamInfoService.findTeam(guestTeam);
                matchInfo = sportMatchInfoService.findMatch(matchInfo);

                againstInfoPO = new SportAgainstInfoPO();
                againstInfoPO.setId(sportAgainstInfoId);
                againstInfoPO.setLotteryCode(lotteryCode);
                againstInfoPO.setIssueCode(issueCode);
                againstInfoPO.setOfficialMatchCode(officialMatchCode);
                againstInfoPO.setSportMatchInfoId(homeTeam.getId());
                againstInfoPO.setHomeTeamId(homeTeam.getId());
                againstInfoPO.setVisitiTeamId(guestTeam.getId());
                againstInfoPO.setMatchName(matchName);
                againstInfoPO.setHomeName(homeName);
                againstInfoPO.setVisitiName(guestName);
                againstInfoPO.setSystemCode(systemCode);
                againstInfoPO.setMatchStatus(Short.valueOf(SportLotteryUtil.getMatchStatus(status)));
                againstInfoPO.setStartTime(matchTime);
                againstInfoPO.setSaleEndTime(saleEndTime);
                againstInfoPO.setModifyBy(createBy);
                againstInfoPO.setCreateBy(createBy);
                againstInfoPO.setSaleDate(DateUtil.convertStrToDate(saleDate, DateUtil.DATE_FORMAT));
                againstInfoPO.setMatchLabelColor("#" + l_background_color);
                againstInfoPO.setOfficialId(officialId);

                sportAgainstInfoDaoMapper.merge(againstInfoPO);
//                if (!ObjectUtil.isBlank(resultPO)) {
//                	sportAgainstInfoId = resultPO.getId();
//                    //禅道任务ID1481，当竞彩比赛场次为暂停销售或延期时，重新抓取时不覆盖这两个状态，需要做变更时，人工进行维护
//                    if (resultPO.getMatchStatus() != Constants.NUM_10 || resultPO.getMatchStatus() != Constants.NUM_13) {
//                    	againstInfoPO.setId(sportAgainstInfoId);
//                        sportAgainstInfoDaoMapper.update(againstInfoPO);
//                    }
//                } else {
//                    sportAgainstInfoDaoMapper.merge(againstInfoPO);
//                    sportAgainstInfoId = againstInfoPO.getId();
//                }

                JSONObject mnl = ObjectUtil.isBlank(valueJson.getJSONObject("mnl")) ? new JSONObject() : valueJson.getJSONObject("mnl");//胜负SP值、包括子玩法状态
                JSONObject hdc = ObjectUtil.isBlank(valueJson.getJSONObject("hdc")) ? new JSONObject() : valueJson.getJSONObject("hdc");//让分胜负SP值、包括子玩法状态
                JSONObject wnm = ObjectUtil.isBlank(valueJson.getJSONObject("wnm")) ? new JSONObject() : valueJson.getJSONObject("wnm");//胜分差SP值、包括子玩法状态
                JSONObject hilo = ObjectUtil.isBlank(valueJson.getJSONObject("hilo")) ? new JSONObject() : valueJson.getJSONObject("hilo");//大小分SP值、包括子玩法状态

                //处理SP值
                Float letHomeLostSp = hdc.getFloat("a");//主负
                Float letHomeWinSp = hdc.getFloat("h");//主胜
                Float letScore = hdc.getFloat("fixedodds");//让分

                Float homeLostSp = mnl.getFloat("a");//主负
                Float homeWinSp = mnl.getFloat("h");//主胜

                Float spBig = hilo.getFloat("h");
                Float spSmall = hilo.getFloat("l");
                Float presetScore = hilo.getFloat("fixedodds");

                Float spFail15 = wnm.getFloat("l1");
                Float spFail610 = wnm.getFloat("l2");
                Float spFail1115 = wnm.getFloat("l3");
                Float spFail1620 = wnm.getFloat("l4");
                Float spFail2125 = wnm.getFloat("l5");
                Float spFail26 = wnm.getFloat("l6");
                Float spWin15 = wnm.getFloat("w1");
                Float spWin610 = wnm.getFloat("w2");
                Float spWin1115 = wnm.getFloat("w3");
                Float spWin1620 = wnm.getFloat("w4");
                Float spWin2125 = wnm.getFloat("w5");
                Float spWin26 = wnm.getFloat("w6");

                SportDataBbSpPO spPo = new SportDataBbSpPO(sportAgainstInfoId,
                        letScore,
                        letHomeWinSp,
                        letHomeLostSp,
                        homeWinSp,
                        homeLostSp,
                        presetScore,
                        spBig,
                        spSmall,
                        spFail15,
                        spFail610,
                        spFail1115,
                        spFail1620,
                        spFail2125,
                        spFail26,
                        spWin15,
                        spWin610,
                        spWin1115,
                        spWin1620,
                        spWin2125,
                        spWin26);

                int spFlag = sportDataBBSpDaoMapper.merge(spPo);

                //处理子玩法状态

                SportStatusBbPO statusPo = new SportStatusBbPO(sportAgainstInfoId,
                        SportLotteryUtil.getSaleType(mnl.getString("single")),
                        SportLotteryUtil.getSaleType(hdc.getString("single")),
                        SportLotteryUtil.getSaleType(hilo.getString("single")),
                        SportLotteryUtil.getSaleType(wnm.getString("single")),
                        SportDataSource.JC_OFFICIAL.getName());
                int statusFlag = sportStatusBBDaoMapper.selectCount(statusPo);
                if (statusFlag == 0) {
                    sportStatusBBDaoMapper.insert(statusPo);
                }
                System.out.println(valueJson);
                LOGGER.debug("竞篮官网数据抓取json:" + valueJson);
            }


        } catch (Exception e) {
            LOGGER.error("竞篮官网数据抓取异常：" + e.getMessage());
        }
    }

    @Override
    public void saveBkMatchOddList() {
        String url = BK_MATCH_ODDS_CALCULATOR + "&_=" + new Date().getTime();

        try {
            String json = HTTPUtil.transRequest(url);
            if (json.equals("0")) {
                return;
            }
            String getData = StringUtils.stripEnd(StringUtils.stripStart(json, "getData("), ");");
            JSONObject getDataJson = JsonUtil.jsonToObject(getData, JSONObject.class);
            Object object = getDataJson.get("data");
            if (object instanceof JSONArray && ((JSONArray) object).size() <= 0) {
                return;
            }
            JSONObject dataJson = getDataJson.getJSONObject("data");

            if (ObjectUtil.isBlank(dataJson)) {
                return;
            }
            List<SportDataBbSpPO> spPos = new ArrayList<>();
            List<SportStatusBbPO> statusPos = new ArrayList<>();
            if (ObjectUtil.isBlank(dataJson)) {
                return;
            }
            for (Map.Entry<String, Object> entry : dataJson.entrySet()) {
                try {
                    JSONObject temp = new JSONObject((Map<String, Object>) entry.getValue());
                    String officialId = temp.getString("id"); //官方id
                    
                    SportAgainstInfoPO po = new SportAgainstInfoPO();
                    po.setOfficialId(officialId);
                    Long sportAgainstInfoId = sportAgainstInfoDaoMapper.findId(po);
                    if(ObjectUtil.isBlank(sportAgainstInfoId))
                    	continue;

                    //胜负SP
                    JSONObject mnlObj = temp.getJSONObject("mnl");//
                    if (ObjectUtil.isBlank(mnlObj)) { //避免为null
                        mnlObj = new JSONObject();
                    }
                    Float homeLostSp = mnlObj.getFloat("a");//主负
                    Float homeWinSp = mnlObj.getFloat("h");//主胜
                    String mnlSingle = mnlObj.getString("single");//过关方式
                    //让分胜负SP
                    JSONObject hdcObj = temp.getJSONObject("hdc");//
                    if (ObjectUtil.isBlank(hdcObj)) { //避免为null
                        hdcObj = new JSONObject();
                    }

                    Float letHomeLostSp = hdcObj.getFloat("a");//主负
                    Float letHomeWinSp = hdcObj.getFloat("h");//主胜
                    Float letScore = hdcObj.getFloat("fixedodds");//让分
                    String hdcSingle = hdcObj.getString("single");//过关方式
                    //胜负差SP
                    JSONObject wnmObj = temp.getJSONObject("wnm");//
                    if (ObjectUtil.isBlank(wnmObj)) {
                        wnmObj = new JSONObject();
                    }
                    Float spFail15 = wnmObj.getFloat("l1");
                    Float spFail610 = wnmObj.getFloat("l2");
                    Float spFail1115 = wnmObj.getFloat("l3");
                    Float spFail1620 = wnmObj.getFloat("l4");
                    Float spFail2125 = wnmObj.getFloat("l5");
                    Float spFail26 = wnmObj.getFloat("l6");
                    Float spWin15 = wnmObj.getFloat("w1");
                    Float spWin610 = wnmObj.getFloat("w2");
                    Float spWin1115 = wnmObj.getFloat("w3");
                    Float spWin1620 = wnmObj.getFloat("w4");
                    Float spWin2125 = wnmObj.getFloat("w5");
                    Float spWin26 = wnmObj.getFloat("w6");
                    String wnmSingle = wnmObj.getString("single");//过关方式
                    //大小分SP
                    JSONObject hiloObj = temp.getJSONObject("hilo");//
                    if (ObjectUtil.isBlank(hiloObj)) {
                        hiloObj = new JSONObject();
                    }
                    Float spBig = hiloObj.getFloat("h");
                    Float spSmall = hiloObj.getFloat("l");
                    Float presetScore = hiloObj.getFloat("fixedodds");
                    String hiloSingle = hiloObj.getString("single");//过关方式

                    //SportDataBbWFPO wf = new SportDataBbWFPO(null,officialId,null,homeLostSp,homeWinSp,SportDataBbWFPO.NOT_LET,new Date(),new Date(),new Date());
                    SportDataBbSpPO spPo = new SportDataBbSpPO(sportAgainstInfoId,
                            letScore,
                            letHomeWinSp,
                            letHomeLostSp,
                            homeWinSp,
                            homeLostSp,
                            presetScore,
                            spBig,
                            spSmall,
                            spFail15,
                            spFail610,
                            spFail1115,
                            spFail1620,
                            spFail2125,
                            spFail26,
                            spWin15,
                            spWin610,
                            spWin1115,
                            spWin1620,
                            spWin2125,
                            spWin26);

                    SportStatusBbPO statusPo = new SportStatusBbPO(sportAgainstInfoId,
                            SportLotteryUtil.getSaleType(mnlSingle),
                            SportLotteryUtil.getSaleType(hdcSingle),
                            SportLotteryUtil.getSaleType(hiloSingle),
                            SportLotteryUtil.getSaleType(wnmSingle),
                            SportDataSource.JC_OFFICIAL.getName());

                    spPos.add(spPo);
                    statusPos.add(statusPo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //sportDataBBSpDaoMapper.saveBBSp(spPos);
            //更新SP值
            for (SportDataBbSpPO po : spPos) {
                try {
                    sportDataBBSpDaoMapper.merge(po);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            LOGGER.debug("crawler bask ball new SP Size is" + spPos.size() + "Data is" + JsonUtil.objectToJson(spPos));
            //sportStatusBBDaoMapper.saveBBStauts(statusPos);
            for (SportStatusBbPO po : statusPos) {//更新数据库状态
                try {
                    //po.setLotteryCode(LotteryEnum.Lottery.BB.getName());
                    int flag = sportStatusBBDaoMapper.selectCount(po);
                    if (flag == 0) {
                        sportStatusBBDaoMapper.insert(po);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LOGGER.debug("crawler bask ball status Size is" + statusPos.size() + "Data is" + JsonUtil.objectToJson(statusPos));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("crawler bask ball new SP And status Error message:" + e.getMessage());
        }
    }

    @Override
    public void updateBasketBallForYbf() {
    	crawlerService.crawlerYbfJclqMatch();
    }


    /**
     * 清楚竞彩足球系统编号缓存
     */
    @Override
    public void clearJclqCacheBySystemCode() {
        List<SportAgainstInfoPO> pos = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.BB.getName());
        for (SportAgainstInfoPO po : pos) {
            resetSportCacheService.resetBBMatchListBySystemCode(po.getSystemCode());
        }
    }

    /**
     * 清楚竞彩足球系统编号缓存
     */
    @Override
    public void clearJclqCacheByIssueCode() {
        List<SportAgainstInfoPO> pos = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.FB.getName());
        Set<String> set = new HashSet<>();
        for (SportAgainstInfoPO po : pos) {
            set.add(po.getIssueCode());
        }
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            resetSportCacheService.resetBBMatchListByIssueCode(iter.next());
        }
    }
}
