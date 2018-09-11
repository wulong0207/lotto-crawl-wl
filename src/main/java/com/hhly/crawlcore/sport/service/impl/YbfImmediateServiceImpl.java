package com.hhly.crawlcore.sport.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDrawFbMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDrawFbPO;
import com.hhly.crawlcore.sport.common.Constants;
import com.hhly.crawlcore.sport.service.ResetSportCacheService;
import com.hhly.crawlcore.sport.service.YbfImmediateService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.MQConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.mq.msg.JlSpMessageData;
import com.hhly.skeleton.base.mq.msg.JzSpMessageData;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.sport.bo.BasketBallScoreBO;
import com.hhly.skeleton.lotto.base.sport.bo.JclqTrendSpBO;
import com.hhly.skeleton.lotto.base.sport.bo.JczqTrendSpBO;

/**
 * Created by chenkangning on 2017/9/7.
 */
@Service
public class YbfImmediateServiceImpl implements YbfImmediateService {

    private static final Logger logger = LoggerFactory.getLogger(YbfImmediateServiceImpl.class);
    @Value("${ybf_refreshState_data_url}")
    String refreshUrl;
    @Value("${ybf_bk_score_url}")
    String ybf_bk_score_url;
    @Value("${ybf_immediate_score_url}")
    private String url;
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SportDrawFbMapper drawFbMapper;

    @Resource
    private SportAgainstInfoDaoMapper againstInfoDaoMapper;

    @Resource
    private ResetSportCacheService resetSportCacheService;

    @Resource
    private MQUtils mqUtils;


    @Override
    public void processImmediateScore() throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info("拉取一比分即时比分 start : " + startTime);
        String responseBody = HTTPUtil.transRequest(MessageFormat.format(url, DateUtil.convertDateToStr(new Date(), DateUtil.DATE_FORMAT)));
        JSONArray jsonArray = JSONArray.parseArray(responseBody);
        if (!ObjectUtil.isBlank(jsonArray)) {
            logger.info("执行即时比分存入缓存操作  --- start");
            //正在比赛的数据,放redis里面
            Map<String, Object> map = redisUtil.getObj(CacheConstants.C_COMM_SPORT_YBF_JISHI_BIFEN, new HashMap<String, Object>());
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                int status = jsonObject.getIntValue("status");
                switch (status) {
                    case 3:
                        if (ObjectUtil.isBlank(map)) {
                            map = new HashMap<>();
                        }
                        JSONObject data = new JSONObject();
                        data.put("guScore", jsonObject.get("guScore"));
                        data.put("hoScore", jsonObject.get("hoScore"));
                        data.put("min", jsonObject.get("min"));
                        data.put("status", jsonObject.get("status"));
                        map.put(jsonObject.getString("id"), data);
                        break;
                    case -1:
                        //彩果入库
                        inStock(jsonObject);
                        break;
                }
            }
            redisUtil.addObj(CacheConstants.C_COMM_SPORT_YBF_JISHI_BIFEN, map, CacheConstants.ONE_MINUTES);
            logger.info("执行即时比分存入缓存操作  --- end");
        }
        long endTime = System.currentTimeMillis();
        logger.info("拉取一比分即时比分 end : " + (endTime - startTime));
    }

    private void inStock(JSONObject jsonObject) {
        String startTimeStr = jsonObject.getString("time");
        Date startTime = DateUtil.convertStrToDate(startTimeStr, DateUtil.DATETIME_FORMAT_NO_SEC);
        String official_match_code = jsonObject.getString("serNum");
        int hoHalfScore = jsonObject.getIntValue("hoHalfScore");
        int guHalfScore = jsonObject.getIntValue("guHalfScore");
        String hfScore = hoHalfScore + ":" + guHalfScore;
        int hoScore = jsonObject.getIntValue("hoScore");
        int guScore = jsonObject.getIntValue("guScore");

        Byte letNum = jsonObject.getByte("letNum");

        SportDrawFbPO sportDrawFbPO = new SportDrawFbPO();
        sportDrawFbPO.setLotteryCode(LotteryEnum.Lottery.FB.getName());
        sportDrawFbPO.setOfficialMatchCode(official_match_code);
        sportDrawFbPO.setHalfScore(hfScore);
        sportDrawFbPO.setFullScore(hoScore + ":" + guScore);
        sportDrawFbPO.setLetNum(jsonObject.getByte("letNum"));
        sportDrawFbPO.setStartTime(startTime);

        String hfWdf = "";

        //半全场胜平负
        if (hoHalfScore > guHalfScore) {
            hfWdf += Constants.WIN;
        } else if (hoHalfScore == guHalfScore) {
            hfWdf += Constants.DRAW;
        } else {
            hfWdf += Constants.LOST;
        }

        //全场胜平负
        if (hoScore > guScore) {
            sportDrawFbPO.setFullSpf(Constants.WIN);
            hfWdf += Constants.WIN;
        } else if (hoScore == guScore) {
            sportDrawFbPO.setFullSpf(Constants.DRAW);
            hfWdf += Constants.DRAW;
        } else {
            sportDrawFbPO.setFullSpf(Constants.LOST);
            hfWdf += Constants.LOST;
        }
        sportDrawFbPO.setHfWdf(hfWdf);

        //让球胜平负
        if (hoScore + letNum > guScore) {
            sportDrawFbPO.setLetSpf(Constants.WIN);
        } else if (hoScore + letNum == guScore) {
            sportDrawFbPO.setLetSpf(Constants.DRAW);
        } else {
            sportDrawFbPO.setLetSpf(Constants.LOST);
        }

        //计算总进球数
        Integer goalNum = hoScore + guScore;
        if (goalNum > 7) {
            goalNum = 7;
        }
        sportDrawFbPO.setGoalNum(goalNum.shortValue());

        //计算总比分
        String score = "";
        if (hoScore > guScore) {
            if ((hoScore >= 4 && guScore >= 3) || hoScore > 5)
                score = "90";
        } else if (hoScore < guScore) {
            if ((hoScore >= 3 && guScore >= 4) || guScore > 5)
                score = "09";
        } else if (hoScore == guScore && hoScore > 3 && guScore > 3) {
            score = "99";
        }

        if (StringUtil.isBlank(score)) {
            score = String.valueOf(hoScore) + String.valueOf(guScore);
        }
        sportDrawFbPO.setScore(score);
        sportDrawFbPO.setModifyBy("live.13322.com");
        /*
         * 状态判断
         只有销售截止或比赛进行中的的比赛才能进行比分入库，如入库前已存在比分和彩果 则核对入库的彩果与所填彩果是否一致，如不一致则输出报警信息。
         比赛状态为取消或延期或暂停销售的不进行比分入库
         */
        SportAgainstInfoPO queryVo = new SportAgainstInfoPO();
        SportAgainstInfoPO resultPO;
        queryVo.setLotteryCode(LotteryEnum.Lottery.FB.getName());
        queryVo.setOfficialMatchCode(official_match_code);
        queryVo.setStartTime(startTime);
        List<SportAgainstInfoPO> list = againstInfoDaoMapper.findCondition(queryVo);
        if (!ObjectUtil.isBlank(list)) {
            resultPO = list.get(0);
            //只有销售截止或比赛进行中的的比赛才能进行比分入库
            if (resultPO.getMatchStatus() == 11 || resultPO.getMatchStatus() == 12) {
                SportDrawFbPO drawFbPOResult = drawFbMapper.selectCondition(sportDrawFbPO);
                if (ObjectUtil.isBlank(drawFbPOResult)) {
                    drawFbMapper.insertSelective(sportDrawFbPO);
                }
            }
        }
    }


    @Override
    public void refreshState() throws Exception {
        List<JczqTrendSpBO> trendList = new ArrayList<>();
        String responseData = HTTPUtil.transRequest(refreshUrl);
//        String responseData = "{"3498938_192":{"id":3498938,"status":1,"min":18,"num":"192"},"3498331_219":{"id":3498331,"status":1,"min":27,"num":"219"},"3498329_95":{"id":3498329,"status":1,"min":12,"hoYellow":1,"guYellow":0,"num":"95"}}";
        logger.debug("一比分即时彩果对接：" + responseData);
        JSONObject jsonObject = JSONObject.parseObject(responseData);
        for (Object object : jsonObject.values()) {
            JczqTrendSpBO jczqTrendSpBO = new JczqTrendSpBO();
            JSONObject dynObj = JSONObject.parseObject(object.toString());
            jczqTrendSpBO.setId(dynObj.getLong("id"));
            Integer hoScore = dynObj.getIntValue("hoScore");
            Integer guScore = dynObj.getIntValue("guScore");
            Integer min = dynObj.getInteger("min");
            Integer status = dynObj.getInteger("status");
            jczqTrendSpBO.setScore(hoScore + ":" + guScore);
            jczqTrendSpBO.setMin(min);
            jczqTrendSpBO.setStatus(status);
            trendList.add(jczqTrendSpBO);
        }
        if (!ObjectUtil.isBlank(trendList)) {
            JzSpMessageData jzSpMessageData = new JzSpMessageData();
            jzSpMessageData.setList(trendList);
            mqUtils.sendSpMessage(MQConstants.FOOTBALL_SP_RABBITMQ_NAME, jzSpMessageData);
            logger.debug("即时比分推送数据：" + JsonUtil.objectToJson(jzSpMessageData));
            for (JczqTrendSpBO bo : trendList) {
                resetSportCacheService.resetFbMatchWdfHistorySpById(bo.getId(), (short) 1);
                resetSportCacheService.resetFbMatchWdfHistorySpById(bo.getId(), (short) 2);
            }
            redisUtil.addString(CacheConstants.S_COMM_SPORT_FB_TREND_SP, JsonUtil.objectToJson(trendList), 3L);
        }
    }

    /**
     * 查询竞篮正在销售的数据
     *
     * @return 数据集合
     */
    private List<SportAgainstInfoPO> querySales() {
        return againstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.BB.getName());
    }

    @Override
    public void processBasketScore() throws Exception {
        List<SportAgainstInfoPO> list = querySales();
        String url = MessageFormat.format(ybf_bk_score_url, DateUtil.getNowDate().getTime(), DateUtil.getNowDate().getTime());
        Document document = ParseHtmlUtil.getDocument(url);
        Elements elements = document.body().select("m s");
        List<JclqTrendSpBO> trendList = new ArrayList<>();
        logger.debug("一比分竞篮即时比分数据对接：" + elements.toString());
        for (Element element : elements) {
            JclqTrendSpBO jclqTrendSpBO = new JclqTrendSpBO();

            String value = element.text();

            String[] string = value.split(SymbolConstants.UP_CAP_DOUBLE_SLASH,-1);


            BasketBallScoreBO bo = new BasketBallScoreBO();
            if (string.length > 0) {
                bo.setId(string[0]);
            }
            if (string.length > 1) {
                bo.setStatus(getScore(string[1]));
            }
            if (string.length > 2) {
                bo.setTime(string[2]);
            }
            if (string.length > 3) {
                bo.sethScore(getScore(string[3]));
            }
            if (string.length > 4) {
                bo.setgScore(getScore(string[4]));
            }
            if (string.length > 5) {
                bo.sethOne(getScore(string[5]));
            }
            if (string.length > 6) {
                bo.sethTwo(getScore(string[6]));
            }
            if (string.length > 7) {
                bo.sethThree(getScore(string[7]));
            }
            if (string.length > 8) {
                bo.sethFour(getScore(string[8]));
            }
            if (string.length > 9) {
                bo.setgOne(getScore(string[9]));
            }
            if (string.length > 10) {
                bo.setgTwo(getScore(string[10]));
            }
            if (string.length > 11) {
                bo.setgThree(getScore(string[11]));
            }
            if (string.length > 12) {
                bo.setgFour(getScore(string[12]));
            }

            String h_1_out = null;
            String h_2_out = null;
            String h_3_out = null;
            if (string.length > 14) {
                h_1_out = string[14];
            }
            if (string.length > 15) {
                h_2_out = string[15];
            }
            if (string.length > 16) {
                h_2_out = string[16];
            }

            String g_1_out = null;
            String g_2_out = null;
            String g_3_out = null;
            if (string.length > 17) {
                g_1_out = string[17];
            }
            if (string.length > 18) {
                g_2_out = string[18];
            }
            if (string.length > 19) {
                g_3_out = string[19];
            }

            Integer hOut = null;
            if (!StringUtil.isBlank(h_1_out)) {
                hOut = Integer.valueOf(h_1_out);
            }
            if (!StringUtil.isBlank(h_2_out)) {
                hOut = Integer.valueOf(h_2_out);
            }
            if (!StringUtil.isBlank(h_3_out)) {
                hOut = Integer.valueOf(h_3_out);
            }
            Integer gOut = null;
            if (!StringUtil.isBlank(g_1_out)) {
                hOut = Integer.valueOf(g_1_out);
            }
            if (!StringUtil.isBlank(g_2_out)) {
                hOut = Integer.valueOf(g_2_out);
            }
            if (!StringUtil.isBlank(g_3_out)) {
                hOut = Integer.valueOf(g_3_out);
            }

            if (!ObjectUtil.isBlank(hOut)) {
                bo.sethOut(hOut);
            }

            if (!ObjectUtil.isBlank(gOut)) {
                bo.sethOut(gOut);
            }

            int hTop = 0;
            int gTop = 0;
            int hDown = 0;
            int gDown = 0;
            if (!ObjectUtil.isBlank(bo.gethOne())) {
                hTop += bo.gethOne();
            }
            if (!ObjectUtil.isBlank(bo.gethTwo())) {
                hTop += bo.gethTwo();
            }
            if (!ObjectUtil.isBlank(bo.getgOne())) {
                gTop += bo.getgOne();
            }

            if (!ObjectUtil.isBlank(bo.getgTwo())) {
                gTop += bo.getgTwo();
            }

            if (!ObjectUtil.isBlank(bo.gethThree())) {
                hDown += bo.gethThree();
            }

            if (!ObjectUtil.isBlank(bo.gethFour())) {
                hDown += bo.gethFour();
            }

            if (!ObjectUtil.isBlank(bo.getgThree())) {
                gDown += bo.getgThree();
            }

            if (!ObjectUtil.isBlank(bo.getgFour())) {
                gDown += bo.getgFour();
            }


            bo.sethTop(hTop);
            bo.setgTop(gTop);
            bo.sethDown(hDown);
            bo.setgDown(gDown);

            jclqTrendSpBO.setScore(bo);

            for (SportAgainstInfoPO againstInfoPO : list) {
                //如果析ID不为空
                if (!StringUtil.isBlank(againstInfoPO.getMatchAnalysisUrl())) {
                    //并且同步一比分的析ID
                    if (bo.getId().equals(againstInfoPO.getMatchAnalysisUrl())) {
                        jclqTrendSpBO.setId(againstInfoPO.getId());
                        trendList.add(jclqTrendSpBO);
                        break;
                    }
                }
            }


        }
        if (!ObjectUtil.isBlank(trendList)) {
            JlSpMessageData jlSpMessageData = new JlSpMessageData();
            jlSpMessageData.setList(trendList);
            mqUtils.sendSpMessage(MQConstants.BASKETBALL_SP_RABBITMQ_NAME, jlSpMessageData);
            logger.debug("竞篮即时比分推送数据：" + JsonUtil.objectToJson(jlSpMessageData));
            /*
             * 篮球即时比分丢缓存
             */
            redisUtil.addString(CacheConstants.C_COMM_SPORT_YBF_BASKETBALL_JISHI_BIFEN, JsonUtil.objectToJcakJson(ResultBO.ok(trendList)), 10L);
        }
    }

    private Integer getScore(String score) {
        if (StringUtil.isBlank(score)) {
            return null;
        }
        return Integer.valueOf(score);
    }

    private String getGameStatusKey(String gameStatus) {
        String gameStatusKey;
        switch (gameStatus) {
            case "0":
                gameStatusKey = "未赛" + "_0";
                break;
            case "-1":
                gameStatusKey = "完场" + "_0";
                break;
            case "50":
                gameStatusKey = "中场" + "_0";
                break;
            case "-2":
                gameStatusKey = "待定" + "_0";
                break;
            case "-3":
                gameStatusKey = "中断" + "_0";
                break;
            case "-4":
                gameStatusKey = "取消" + "_0";
                break;
            case "-5":
                gameStatusKey = "推迟" + "_0";
                break;
            case "1":
                gameStatusKey = "1st" + "_1";
                break;
            case "2":
                gameStatusKey = "2nd" + "_1";
                break;
            case "3":
                gameStatusKey = "3rd" + "_1";
                break;
            case "4":
                gameStatusKey = "4th" + "_1";
                break;
            case "5":
                gameStatusKey = "1OT" + "_1";
                break;
            case "6":
                gameStatusKey = "2OT" + "_1";
                break;
            case "7":
                gameStatusKey = "3OT" + "_1";
                break;
            default:
                gameStatusKey = "未赛" + "_0";
                break;
        }
        return gameStatusKey;
    }
}
