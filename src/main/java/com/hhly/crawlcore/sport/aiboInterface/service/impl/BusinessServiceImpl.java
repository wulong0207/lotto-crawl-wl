package com.hhly.crawlcore.sport.aiboInterface.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDrawBjDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDrawWfDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjBasePO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjGoalPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjHfWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjScorePO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjUdsdPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjWfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDrawBjPO;
import com.hhly.crawlcore.persistence.sport.po.SportDrawWfPO;
import com.hhly.crawlcore.persistence.sport.po.SportStatusBjPO;
import com.hhly.crawlcore.sport.aiboInterface.constant.AiboConstant;
import com.hhly.crawlcore.sport.aiboInterface.service.BusinessService;
import com.hhly.crawlcore.sport.common.Constants;
import com.hhly.crawlcore.sport.service.SportDataBjGoalService;
import com.hhly.crawlcore.sport.service.SportDataBjHfWdfService;
import com.hhly.crawlcore.sport.service.SportDataBjScoreService;
import com.hhly.crawlcore.sport.service.SportDataBjUdsService;
import com.hhly.crawlcore.sport.service.SportDataBjWdfService;
import com.hhly.crawlcore.sport.service.SportDataBjWfService;
import com.hhly.crawlcore.sport.service.SportStatusBjService;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.NumberUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.task.issue.bo.IssueBO;

import us.codecraft.xsoup.Xsoup;

/**
 * Created by chenkangning on 2017/8/2.
 */
@Service
public class BusinessServiceImpl implements BusinessService {


    private static final Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);

    @Resource
    private SportDataBjWdfService sportDataBjWdfService;

    @Resource
    private SportDataBjGoalService sportDataBjGoalService;

    @Resource
    private SportDataBjUdsService sportDataBjUdsService;

    @Resource
    private SportDataBjWfService sportDataBjWfService;

    @Resource
    private SportDataBjHfWdfService sportDataBjHfWdfService;

    @Resource
    private SportDataBjScoreService sportDataBjScoreService;

    @Resource
    private SportDrawBjDaoMapper drawBjDaoMapper;

    @Resource
    private SportDrawWfDaoMapper drawWfDaoMapper;


    @Resource
    private LotteryIssueDaoMapper lotteryIssueMapper;

    @Resource
    private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;


    @Resource
    private SportStatusBjService sportStatusBjService;

    @Resource
    private MQUtils mqUtils;
    @Resource
    private RedisUtil redisUtil;

    private static final String START_TIME_DIFF_KEY = "START_TIME_DIFF_KEY";

    @Value("${aibo_selling_url}")
    String sellingUrl;

    @Value("${aibo_howto_play_url}")
    String sp_url;


    @Override
    public void processPreviousIssueData() {
        IssueBO issueBO;
        try {
            Document document = ParseHtmlUtil.getDocumentGBK(sellingUrl);
            List<String> list = Xsoup.select(document, "//selling/w").list();
            for (String str : list) {
                String key = Xsoup.select(str, "//w/@li").get();
                Integer lotteryCode;
                AiboConstant.HotoPlayEnum hotoPlayEnum = AiboConstant.HotoPlayEnum.getValueByKey(key);
                if (!ObjectUtil.isBlank(hotoPlayEnum)) {
                    lotteryCode = hotoPlayEnum.getValue();
                    AiboConstant.SellingEnum sellingEnum = AiboConstant.SellingEnum.getValueByKey(key);
                    //查询上一期
                    LotteryIssuePO paramPO = new LotteryIssuePO();
                    paramPO.setLotteryCode(Integer.valueOf(lotteryCode));
                    paramPO.setCurrentIssue((short) 2);
                    LotteryIssuePO issuePO = lotteryIssueMapper.findSingle(paramPO);
                    String issue = issuePO.getIssueCode().substring(1, issuePO.getIssueCode().length());
                    if (null != sellingEnum) {
                        processData(issue, sellingEnum);
                    }
                }


            }
            redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_BJ_MATCH);
        } catch (Exception e) {
            logger.error("北单抓取上一期数据异常" + e.getMessage());
        }
    }

    @Override
    public void into() {
        try {
            Document document = ParseHtmlUtil.getDocumentGBK(sellingUrl);
            List<String> list = Xsoup.select(document, "//selling/w").list();
            for (String str : list) {
                String key = Xsoup.select(str, "//w/@li").get();
                String issue = Xsoup.select(str, "//w/@is").get();
                AiboConstant.SellingEnum sellingEnum = AiboConstant.SellingEnum.getValueByKey(key);
                if (null != sellingEnum) {
                    processData(issue, sellingEnum);
                }

            }
            redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_BJ_MATCH);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("北单爱波对接异常：" + e.getMessage());
        }
    }

    private void processData(String issue, AiboConstant.SellingEnum sellingEnum) {
        Document document = connect(sellingEnum.getValue(), issue);
        List<String> list = Xsoup.select(document, "//w/*").list();
        switch (sellingEnum) {
            case WDF:
                logger.info("北单爱波对接处理【胜平负】数据start>>>>>>>>>>>>>" + list.size());
                wfl(document, list, issue);
                break;
            case GOAL:
                logger.info("北单爱波对接处理【总进球数】数据start>>>>>>>>>>>>>" + list.size());
                goal(document, list, issue);
                break;
            case SINGLE_DOUBLE:
                logger.info("北单爱波对接处理【上下盘单双数】数据start>>>>>>>>>>>>>" + list.size());
                sxdf(document, list, issue);
                break;
            case SCORE:
                logger.info("北单爱波对接处理【单场比分】数据start>>>>>>>>>>>>>" + list.size());
                dcbf(document, list, issue);
                break;
            case HF_WDF:
                logger.info("北单爱波对接处理【半全场胜平负】数据start>>>>>>>>>>>>>" + list.size());
                bqcf(document, list, issue);
                break;
            case DOWN_SCORE:
                logger.info("北单爱波对接处理【下半场比分】数据start>>>>>>>>>>>>>" + list.size());
                break;
            case WF:
                logger.info("北单爱波对接处理【胜负过关】数据start>>>>>>>>>>>>>" + list.size());
                sfgg(document, list, issue);
                break;
        }

        //处理比赛时间变动发送消息
        Map<String, Integer> map = redisUtil.getObj(START_TIME_DIFF_KEY, new HashMap<String, Integer>());
        if (!ObjectUtil.isBlank(map)) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                mqUtils.sendMessage(entry.getKey(), entry.getValue());
                logger.info("北单比赛时间变动，发送MQ消息：彩种【" + entry.getKey() + "】彩期【" + entry.getValue().toString() + "】");
            }
        }


    }

    /**
     * 请求最新SP值
     *
     * @param key   爱波网彩种代码
     * @param issue 从selling.xml拿到的期号
     * @return Document 请求结果
     */
    private Document connect(String key, String issue) {
        try {
            String url = MessageFormat.format(sp_url, key + issue);
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(Boolean.FALSE);
            return Jsoup.parse(connection.getInputStream(), "GBK", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单场比分
     *
     * @param document 表示selling xml
     * @param list     赛事及SP等信息
     */
    private void dcbf(Document document, List<String> list, String issueCode) {
        List<SportDataBjBasePO> bjWdfPOList = new ArrayList<>();
        List<SportDataBjScorePO> inStockList = new ArrayList<>();
        SportDataBjScorePO sportDataBjScorePO;
        SportDrawBjPO drawBjPO;
        int i = 1;
        for (String str : list) {
            Integer lotteryCode = AiboConstant.HotoPlayEnum.getValueByKey(Xsoup.select(document, "//w/@li").get()).getValue();
            String homeName = Xsoup.select(str, "//w" + i + "/@h").get();
            String visitiName = Xsoup.select(str, "//w" + i + "/@a").get();
            String matchName = Xsoup.select(document, "//w" + i + "/@gn").get();
            String homeAbbrName = Xsoup.select(str, "//w" + i + "/@hf").get();
            String visitiAbbrName = Xsoup.select(str, "//w" + i + "/@af").get();               
            String startTimeStr = Xsoup.select(str, "//w" + i + "/@gt").get();
            String matchTypeName = Xsoup.select(str, "//w" + i + "/@gn").get();

            String sp10 = Xsoup.select(str, "//w" + i + "/@c2").get();
            String sp20 = Xsoup.select(str, "//w" + i + "/@c3").get();
            String sp21 = Xsoup.select(str, "//w" + i + "/@c4").get();
            String sp30 = Xsoup.select(str, "//w" + i + "/@c5").get();
            String sp31 = Xsoup.select(str, "//w" + i + "/@c6").get();
            String sp32 = Xsoup.select(str, "//w" + i + "/@c7").get();
            String sp40 = Xsoup.select(str, "//w" + i + "/@c8").get();
            String sp41 = Xsoup.select(str, "//w" + i + "/@c9").get();
            String sp42 = Xsoup.select(str, "//w" + i + "/@c10").get();
            String spWOther = Xsoup.select(str, "//w" + i + "/@c1").get();
            String sp00 = Xsoup.select(str, "//w" + i + "/@c12").get();
            String sp11 = Xsoup.select(str, "//w" + i + "/@c13").get();
            String sp22 = Xsoup.select(str, "//w" + i + "/@c14").get();
            String sp33 = Xsoup.select(str, "//w" + i + "/@c15").get();
            String spDOther = Xsoup.select(str, "//w" + i + "/@c11").get();
            String sp01 = Xsoup.select(str, "//w" + i + "/@c17").get();
            String sp02 = Xsoup.select(str, "//w" + i + "/@c18").get();
            String sp12 = Xsoup.select(str, "//w" + i + "/@c19").get();
            String sp03 = Xsoup.select(str, "//w" + i + "/@c20").get();
            String sp13 = Xsoup.select(str, "//w" + i + "/@c21").get();
            String sp23 = Xsoup.select(str, "//w" + i + "/@c22").get();
            String sp04 = Xsoup.select(str, "//w" + i + "/@c23").get();
            String sp14 = Xsoup.select(str, "//w" + i + "/@c24").get();
            String sp24 = Xsoup.select(str, "//w" + i + "/@c25").get();
            String spFOther = Xsoup.select(str, "//w" + i + "/@c16").get();


            String rt = Xsoup.select(str, "//w" + i + "/@rt").get();//开奖结果
            String sp = Xsoup.select(str, "//w" + i + "/@sp").get();//开奖sp
            String sr = Xsoup.select(str, "//w" + i + "/@sr").get();//比分
            String[] srArray = sr.split("\\$");

            String matchStatus = Xsoup.select(str, "//w" + i + "/@st").get();

            if (matchStatus.equals("1") || matchStatus.equals("2")) {
                sportDataBjScorePO = new SportDataBjScorePO();

                sportDataBjScorePO.setLotteryCode(lotteryCode);
                sportDataBjScorePO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                sportDataBjScorePO.setBjNum(i);
                sportDataBjScorePO.setMatchName(matchName);
                sportDataBjScorePO.setHomeName(homeName);
                sportDataBjScorePO.setHomeAbbrName(homeAbbrName);
                sportDataBjScorePO.setVisitiName(visitiName);
                sportDataBjScorePO.setVisitiAbbrName(visitiAbbrName);
                Date startTime = DateUtil.convertStrToDate(startTimeStr, DateUtil.DATETIME_FORMAT_NO_SEC);
                sportDataBjScorePO.setStartTime(startTime);
                sportDataBjScorePO.setSaleDate(SportLotteryUtil.getBjSaleDate(startTime));
                sportDataBjScorePO.setMatchStatus(AiboConstant.MatchStatusEnum.getValueByKey(Integer.valueOf(matchStatus)).getValue().toString());
                sportDataBjScorePO.setCreateBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                sportDataBjScorePO.setMatchType(matchTypeName);

                sportDataBjScorePO.setSp10(NumberUtil.parseString(sp10));
                sportDataBjScorePO.setSp20(NumberUtil.parseString(sp20));
                sportDataBjScorePO.setSp21(NumberUtil.parseString(sp21));
                sportDataBjScorePO.setSp30(NumberUtil.parseString(sp30));
                sportDataBjScorePO.setSp31(NumberUtil.parseString(sp31));
                sportDataBjScorePO.setSp32(NumberUtil.parseString(sp32));
                sportDataBjScorePO.setSp40(NumberUtil.parseString(sp40));
                sportDataBjScorePO.setSp41(NumberUtil.parseString(sp41));
                sportDataBjScorePO.setSp42(NumberUtil.parseString(sp42));
                sportDataBjScorePO.setSpWOther(NumberUtil.parseString(spWOther));
                sportDataBjScorePO.setSp00(NumberUtil.parseString(sp00));
                sportDataBjScorePO.setSp11(NumberUtil.parseString(sp11));
                sportDataBjScorePO.setSp22(NumberUtil.parseString(sp22));
                sportDataBjScorePO.setSp33(NumberUtil.parseString(sp33));
                sportDataBjScorePO.setSpDOther(NumberUtil.parseString(spDOther));
                sportDataBjScorePO.setSp01(NumberUtil.parseString(sp01));
                sportDataBjScorePO.setSp02(NumberUtil.parseString(sp02));
                sportDataBjScorePO.setSp12(NumberUtil.parseString(sp12));
                sportDataBjScorePO.setSp03(NumberUtil.parseString(sp03));
                sportDataBjScorePO.setSp13(NumberUtil.parseString(sp13));
                sportDataBjScorePO.setSp23(NumberUtil.parseString(sp23));
                sportDataBjScorePO.setSp04(NumberUtil.parseString(sp04));
                sportDataBjScorePO.setSp14(NumberUtil.parseString(sp14));
                sportDataBjScorePO.setSp24(NumberUtil.parseString(sp24));
                sportDataBjScorePO.setSpFOther(NumberUtil.parseString(spFOther));

                try {
                    String union = DigestUtils.md5DigestAsHex(sportDataBjScorePO.toString().getBytes("UTF-8"));
                    sportDataBjScorePO.setMd5Value(union);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                inStockList.add(sportDataBjScorePO);
                bjWdfPOList.add(sportDataBjScorePO);
            } else {
                //如果赛事状态为-2已开奖，3已计算，4已返奖，保存彩果

                drawBjPO = new SportDrawBjPO();
                drawBjPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                drawBjPO.setLotteryCode(lotteryCode);
                drawBjPO.setBjNum(i);
                if (!StringUtil.isBlank(sp)) {
                    drawBjPO.setSpScore(Double.valueOf(sp));
                }
                if (!StringUtil.isBlank(rt) && !AiboConstant.DrawEnum.OTHER.getKey().equals(sr)) {
                    drawBjPO.setScore(AiboConstant.ScoreEnum.getValueByKey(rt).getValue());
                    //如果取到彩果不是延，才设置值
                    drawBjPO.setHalfScore(srArray[0]);
                    drawBjPO.setFullScore(srArray[1]);
                }
                drawbj(drawBjPO);

            }

            i++;
        }
        handlBjdcMq(bjWdfPOList);
        for (SportDataBjScorePO dataBjScorePO : inStockList) {
            sportDataBjScoreService.save(dataBjScorePO);

            SportStatusBjPO po = sportStatusBjService.select(JsonUtil.json2Map(JSON.toJSONString(dataBjScorePO)));
            if (ObjectUtil.isBlank(po)) {
                //如果数据库没有就insert
                po = new SportStatusBjPO(dataBjScorePO);
                po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                po.setStatusScore(Constants.bjdcStatus.REGULAR_SALE.getKey());
                sportStatusBjService.insert(po);
            } else {
            	if(dataBjScorePO.getMatchStatus().equals("9")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusScore(Constants.bjdcStatus.REGULAR_SALE.getKey());
            	}else if(dataBjScorePO.getMatchStatus().equals("7")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusScore(Constants.bjdcStatus.SUSPENDED_SALES.getKey());
            	}               	
                sportStatusBjService.updateByPrimaryKey(po);
            }

        }
    }


    /**
     * 半全场胜平负
     *
     * @param document 表示selling xml
     * @param list     赛事及SP等信息
     */
    private void bqcf(Document document, List<String> list, String issueCode) {
        List<SportDataBjBasePO> bjWdfPOList = new ArrayList<>();
        List<SportDataBjHfWdfPO> inStockList = new ArrayList<>();
        SportDataBjHfWdfPO sportDataBjHfWdfPO;
        SportDrawBjPO drawBjPO;
        int i = 1;
        for (String str : list) {
            Integer lotteryCode = AiboConstant.HotoPlayEnum.getValueByKey(Xsoup.select(document, "//w/@li").get()).getValue();
            String homeName = Xsoup.select(str, "//w" + i + "/@h").get();
            String visitiName = Xsoup.select(str, "//w" + i + "/@a").get();
            String homeAbbrName = Xsoup.select(str, "//w" + i + "/@hf").get();
            String visitiAbbrName = Xsoup.select(str, "//w" + i + "/@af").get();            
            String matchName = Xsoup.select(document, "//w" + i + "/@gn").get();
            String startTimeStr = Xsoup.select(str, "//w" + i + "/@gt").get();
            String matchTypeName = Xsoup.select(str, "//w" + i + "/@gn").get();

            String spww = Xsoup.select(str, "//w" + i + "/@c1").get();
            String spwd = Xsoup.select(str, "//w" + i + "/@c3").get();
            String spwf = Xsoup.select(str, "//w" + i + "/@c5").get();
            String spdw = Xsoup.select(str, "//w" + i + "/@c7").get();
            String spdd = Xsoup.select(str, "//w" + i + "/@c9").get();
            String spdf = Xsoup.select(str, "//w" + i + "/@c11").get();
            String spfw = Xsoup.select(str, "//w" + i + "/@c13").get();
            String spfd = Xsoup.select(str, "//w" + i + "/@c15").get();
            String spff = Xsoup.select(str, "//w" + i + "/@c17").get();


            String rt = Xsoup.select(str, "//w" + i + "/@rt").get();//开奖结果
            String[] rtArray = rt.split("-");

            String sp = Xsoup.select(str, "//w" + i + "/@sp").get();//开奖sp
            String sr = Xsoup.select(str, "//w" + i + "/@sr").get();//比分
            String[] srArray = sr.split("\\$");

            String matchStatus = Xsoup.select(str, "//w" + i + "/@st").get();
            if (matchStatus.equals("1") || matchStatus.equals("2")) {
                sportDataBjHfWdfPO = new SportDataBjHfWdfPO();
                sportDataBjHfWdfPO.setLotteryCode(lotteryCode);
                sportDataBjHfWdfPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                sportDataBjHfWdfPO.setBjNum(i);
                sportDataBjHfWdfPO.setMatchName(matchName);
                sportDataBjHfWdfPO.setHomeName(homeName);
                sportDataBjHfWdfPO.setHomeAbbrName(homeAbbrName);
                sportDataBjHfWdfPO.setVisitiName(visitiName);
                sportDataBjHfWdfPO.setVisitiAbbrName(visitiAbbrName);
                Date startTime = DateUtil.convertStrToDate(startTimeStr, DateUtil.DATETIME_FORMAT_NO_SEC);
                sportDataBjHfWdfPO.setStartTime(startTime);
                sportDataBjHfWdfPO.setSaleDate(SportLotteryUtil.getBjSaleDate(startTime));
                sportDataBjHfWdfPO.setMatchStatus(AiboConstant.MatchStatusEnum.getValueByKey(Integer.valueOf(matchStatus)).getValue().toString());
                sportDataBjHfWdfPO.setCreateBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                sportDataBjHfWdfPO.setMatchType(matchTypeName);

                sportDataBjHfWdfPO.setSpWW(NumberUtil.parseString(spww));
                sportDataBjHfWdfPO.setSpWD(NumberUtil.parseString(spwd));
                sportDataBjHfWdfPO.setSpWF(NumberUtil.parseString(spwf));
                sportDataBjHfWdfPO.setSpDW(NumberUtil.parseString(spdw));
                sportDataBjHfWdfPO.setSpDD(NumberUtil.parseString(spdd));
                sportDataBjHfWdfPO.setSpDF(NumberUtil.parseString(spdf));
                sportDataBjHfWdfPO.setSpFW(NumberUtil.parseString(spfw));
                sportDataBjHfWdfPO.setSpFD(NumberUtil.parseString(spfd));
                sportDataBjHfWdfPO.setSpFF(NumberUtil.parseString(spff));

                try {
                    String union = DigestUtils.md5DigestAsHex(sportDataBjHfWdfPO.toString().getBytes("UTF-8"));
                    sportDataBjHfWdfPO.setMd5Value(union);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                inStockList.add(sportDataBjHfWdfPO);
                bjWdfPOList.add(sportDataBjHfWdfPO);
            } else {
                //如果赛事状态为-2已开奖，3已计算，4已返奖，保存彩果

                drawBjPO = new SportDrawBjPO();
                drawBjPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                drawBjPO.setLotteryCode(lotteryCode);
                drawBjPO.setBjNum(i);
                if (!StringUtil.isBlank(sp)) {
                    drawBjPO.setSpHfWdf(Double.valueOf(sp));
                }
                if (!StringUtil.isBlank(rt) && !AiboConstant.DrawEnum.OTHER.getKey().equals(sr)) {
                    drawBjPO.setHfWdf(AiboConstant.HfwdfDrawEnum.getValueByKey(rt).getValue());
                    //如果取到彩果不是延，才设置值
                    drawBjPO.setHalfScore(srArray[0]);
                    drawBjPO.setFullScore(srArray[1]);
                }
                drawbj(drawBjPO);

            }
            i++;
            System.out.println("BusinessServiceImpl.bqcf" + i);
        }
        handlBjdcMq(bjWdfPOList);
        for (SportDataBjHfWdfPO dataBjHfWdfPO : inStockList) {
            sportDataBjHfWdfService.save(dataBjHfWdfPO);

            //处理子玩法状态
            SportStatusBjPO po = sportStatusBjService.select(JsonUtil.json2Map(JSON.toJSONString(dataBjHfWdfPO)));
            if (ObjectUtil.isBlank(po)) {
                //如果数据库没有就insert
                po = new SportStatusBjPO(dataBjHfWdfPO);
                po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                po.setStatusHfWdf(Constants.bjdcStatus.REGULAR_SALE.getKey());
                sportStatusBjService.insert(po);
            } else {
            	if(dataBjHfWdfPO.getMatchStatus().equals("9")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusHfWdf(Constants.bjdcStatus.REGULAR_SALE.getKey());
            	}else if(dataBjHfWdfPO.getMatchStatus().equals("7")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusHfWdf(Constants.bjdcStatus.SUSPENDED_SALES.getKey());
            	}
            	 sportStatusBjService.updateByPrimaryKey(po);
            }
        }
    }


    /**
     * 胜负过关
     *
     * @param document 表示selling xml
     * @param list     赛事及SP等信息
     */
    private void sfgg(Document document, List<String> list, String issueCode) {
        List<SportDataBjBasePO> bjWdfPOList = new ArrayList<>();
        List<SportDataBjWfPO> inStockList = new ArrayList<>();
        SportDataBjWfPO sportDataBjWfPO;
        SportDrawWfPO drawWfPO;
        int i = 1;
        for (String str : list) {
            Integer lotteryCode = AiboConstant.HotoPlayEnum.getValueByKey(Xsoup.select(document, "//w/@li").get()).getValue();
            String homeName = Xsoup.select(str, "//w" + i + "/@h").get();
            String visitiName = Xsoup.select(str, "//w" + i + "/@a").get();
            String homeAbbrName = Xsoup.select(str, "//w" + i + "/@hf").get();
            String visitiAbbrName = Xsoup.select(str, "//w" + i + "/@af").get();              
            String matchName = Xsoup.select(document, "//w" + i + "/@gn_").get();
            String startTimeStr = Xsoup.select(str, "//w" + i + "/@gt").get();
            String matchTypeName = Xsoup.select(str, "//w" + i + "/@gn").get();

            String spWin = Xsoup.select(str, "//w" + i + "/@c1").get();
            String spFail = Xsoup.select(str, "//w" + i + "/@c2").get();
            String letScore = Xsoup.select(str, "//w" + i + "/@r").get();

            String rt = Xsoup.select(str, "//w" + i + "/@rt").get();//开奖结果
            String sp = Xsoup.select(str, "//w" + i + "/@sp").get();//开奖sp
            String sr = Xsoup.select(str, "//w" + i + "/@sr").get();//比分


            String matchStatus = Xsoup.select(str, "//w" + i + "/@st").get();

            if (matchStatus.equals("1") || matchStatus.equals("2")) {
                sportDataBjWfPO = new SportDataBjWfPO();

                sportDataBjWfPO.setLotteryCode(lotteryCode);
                sportDataBjWfPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                sportDataBjWfPO.setBjNum(i);
                sportDataBjWfPO.setMatchName(matchName);
                sportDataBjWfPO.setHomeName(homeName);
                sportDataBjWfPO.setHomeAbbrName(homeAbbrName);
                sportDataBjWfPO.setVisitiName(visitiName);
                sportDataBjWfPO.setVisitiAbbrName(visitiAbbrName);
                Date startTime = DateUtil.convertStrToDate(startTimeStr, DateUtil.DATETIME_FORMAT_NO_SEC);
                sportDataBjWfPO.setStartTime(startTime);
                sportDataBjWfPO.setSaleDate(SportLotteryUtil.getBjSaleDate(startTime));
                sportDataBjWfPO.setMatchStatus(AiboConstant.MatchStatusEnum.getValueByKey(Integer.valueOf(matchStatus)).getValue().toString());
                sportDataBjWfPO.setCreateBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                sportDataBjWfPO.setMatchType(matchTypeName);

                sportDataBjWfPO.setSpWin(NumberUtil.parseString(spWin));
                sportDataBjWfPO.setSpFail(NumberUtil.parseString(spFail));
                sportDataBjWfPO.setLetScore(NumberUtil.parseString(letScore));

                try {
                    String union = DigestUtils.md5DigestAsHex(sportDataBjWfPO.toString().getBytes("UTF-8"));
                    sportDataBjWfPO.setMd5Value(union);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                inStockList.add(sportDataBjWfPO);
                bjWdfPOList.add(sportDataBjWfPO);
            } else {
                //如果赛事状态为-2已开奖，3已计算，4已返奖，保存彩果
            	if(!matchStatus.equals("-1")){
            		
                    drawWfPO = new SportDrawWfPO();
                    drawWfPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                    drawWfPO.setLotteryCode(lotteryCode);
                    drawWfPO.setBjNum(i);

                    drawWfPO.setFullScore(sr);
                    drawWfPO.setLetNum(NumberUtil.parseString(letScore));
                    if (!StringUtil.isBlank(sp)) {
                        drawWfPO.setSpLetWf(Double.valueOf(sp));
                    }
                    if (!StringUtil.isBlank(rt) && !AiboConstant.DrawEnum.OTHER.getKey().equals(sr)) {
                        drawWfPO.setLetSf(AiboConstant.DrawEnum.getValueByKey(rt).getValue());
                    }
                    drawbjsfgg(drawWfPO);            		
            	}
            }
            i++;
            System.out.println("BusinessServiceImpl.sfgg" + i);
        }
        handlBjdcMq(bjWdfPOList);
        for (SportDataBjWfPO dataBjWfPO : inStockList) {
            sportDataBjWfService.save(dataBjWfPO);

            //处理子玩法状态
            SportStatusBjPO po = sportStatusBjService.select(JsonUtil.json2Map(JSON.toJSONString(dataBjWfPO)));
            if (ObjectUtil.isBlank(po)) {
                //如果数据库没有就insert
                po = new SportStatusBjPO(dataBjWfPO);
                po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                po.setStatusLetWf(Constants.bjdcStatus.REGULAR_SALE.getKey());
                sportStatusBjService.insert(po);
            } else {
            	if(dataBjWfPO.getMatchStatus().equals("9")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusLetWf(Constants.bjdcStatus.REGULAR_SALE.getKey());
            	}else if(dataBjWfPO.getMatchStatus().equals("7")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusLetWf(Constants.bjdcStatus.SUSPENDED_SALES.getKey());
            	}            	
                sportStatusBjService.updateByPrimaryKey(po);
            }
        }
    }


    /**
     * 上下盘单双数
     *
     * @param document 表示selling xml
     * @param list     赛事及SP等信息
     */
    private void sxdf(Document document, List<String> list, String issueCode) {
        List<SportDataBjBasePO> bjWdfPOList = new ArrayList<>();
        List<SportDataBjUdsdPO> inStockList = new ArrayList<>();
        SportDataBjUdsdPO sportDataBjUdsdPO;
        SportDrawBjPO drawBjPO;
        int i = 1;
        for (String str : list) {
            Integer lotteryCode = AiboConstant.HotoPlayEnum.getValueByKey(Xsoup.select(document, "//w/@li").get()).getValue();
            String homeName = Xsoup.select(str, "//w" + i + "/@h").get();
            String visitiName = Xsoup.select(str, "//w" + i + "/@a").get();
            String homeAbbrName = Xsoup.select(str, "//w" + i + "/@hf").get();
            String visitiAbbrName = Xsoup.select(str, "//w" + i + "/@af").get();              
            String matchName = Xsoup.select(document, "//w" + i + "/@gn").get();
            String startTimeStr = Xsoup.select(str, "//w" + i + "/@gt").get();
            String matchTypeName = Xsoup.select(str, "//w" + i + "/@gn").get();

            String spUpSingle = Xsoup.select(str, "//w" + i + "/@c1").get();
            String spUpDouble = Xsoup.select(str, "//w" + i + "/@c3").get();
            String spDownSingle = Xsoup.select(str, "//w" + i + "/@c5").get();
            String spDownDouble = Xsoup.select(str, "//w" + i + "/@c7").get();


            String rt = Xsoup.select(str, "//w" + i + "/@rt").get();//开奖结果
            String sp = Xsoup.select(str, "//w" + i + "/@sp").get();//开奖sp
            String sr = Xsoup.select(str, "//w" + i + "/@sr").get();//比分
            String[] srArray = sr.split("\\$");

            String matchStatus = Xsoup.select(str, "//w" + i + "/@st").get();

            if (matchStatus.equals("1") || matchStatus.equals("2")) {
                sportDataBjUdsdPO = new SportDataBjUdsdPO();

                sportDataBjUdsdPO.setLotteryCode(lotteryCode);
                sportDataBjUdsdPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                sportDataBjUdsdPO.setBjNum(i);
                sportDataBjUdsdPO.setMatchName(matchName);
                sportDataBjUdsdPO.setHomeName(homeName);
                sportDataBjUdsdPO.setHomeAbbrName(homeAbbrName);
                sportDataBjUdsdPO.setVisitiName(visitiName);
                sportDataBjUdsdPO.setVisitiAbbrName(visitiAbbrName);
                Date startTime = DateUtil.convertStrToDate(startTimeStr, DateUtil.DATETIME_FORMAT_NO_SEC);
                sportDataBjUdsdPO.setStartTime(startTime);
                sportDataBjUdsdPO.setSaleDate(SportLotteryUtil.getBjSaleDate(startTime));
                sportDataBjUdsdPO.setMatchStatus(AiboConstant.MatchStatusEnum.getValueByKey(Integer.valueOf(matchStatus)).getValue().toString());
                sportDataBjUdsdPO.setCreateBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                sportDataBjUdsdPO.setMatchType(matchTypeName);

                sportDataBjUdsdPO.setSpUpSingle(NumberUtil.parseString(spUpSingle));
                sportDataBjUdsdPO.setSpUpDouble(NumberUtil.parseString(spUpDouble));
                sportDataBjUdsdPO.setSpDownSingle(NumberUtil.parseString(spDownSingle));
                sportDataBjUdsdPO.setSpDownDouble(NumberUtil.parseString(spDownDouble));

                try {
                    String union = DigestUtils.md5DigestAsHex(sportDataBjUdsdPO.toString().getBytes("UTF-8"));
                    sportDataBjUdsdPO.setMd5Value(union);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                inStockList.add(sportDataBjUdsdPO);
                bjWdfPOList.add(sportDataBjUdsdPO);
            } else {
                //如果赛事状态为-2已开奖，3已计算，4已返奖，保存彩果

                drawBjPO = new SportDrawBjPO();
                drawBjPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                drawBjPO.setLotteryCode(lotteryCode);
                drawBjPO.setBjNum(i);
                if (!StringUtil.isBlank(sp)) {
                    drawBjPO.setSpUdSd(Double.valueOf(sp));
                }
                if (!StringUtil.isBlank(rt) && !AiboConstant.DrawEnum.OTHER.getKey().equals(sr)) {
                    drawBjPO.setUdSd(AiboConstant.UdsdDrawEnum.getValueByKey(rt).getValue());
                    //如果取到彩果不是延，才设置值
                    drawBjPO.setHalfScore(srArray[0]);
                    drawBjPO.setFullScore(srArray[1]);
                }
                drawbj(drawBjPO);
            }
            i++;
        }
        handlBjdcMq(bjWdfPOList);
        for (SportDataBjUdsdPO dataBjUdsdPO : inStockList) {
            sportDataBjUdsService.save(dataBjUdsdPO);

            //处理子玩法状态
            SportStatusBjPO po = sportStatusBjService.select(JsonUtil.json2Map(JSON.toJSONString(dataBjUdsdPO)));
            if (ObjectUtil.isBlank(po)) {
                //如果数据库没有就insert
                po = new SportStatusBjPO(dataBjUdsdPO);
                po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                po.setStatusUdSd(Constants.bjdcStatus.REGULAR_SALE.getKey());
                sportStatusBjService.insert(po);
            } else {
            	if(dataBjUdsdPO.getMatchStatus().equals("9")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusUdSd(Constants.bjdcStatus.REGULAR_SALE.getKey());
            	}else if(dataBjUdsdPO.getMatchStatus().equals("7")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusUdSd(Constants.bjdcStatus.SUSPENDED_SALES.getKey());
            	}   
                sportStatusBjService.updateByPrimaryKey(po);
            }

        }
    }

    /**
     * 处理总进球
     *
     * @param document 表示selling xml
     * @param list     赛事及SP等信息
     */
    private void goal(Document document, List<String> list, String issueCode) {
        List<SportDataBjBasePO> bjWdfPOList = new ArrayList<>();
        List<SportDataBjGoalPO> inStockList = new ArrayList<>();
        SportDataBjGoalPO sportDataBjGoalPO;
        SportDrawBjPO drawBjPO;
        int i = 1;
        for (String str : list) {
            Integer lotteryCode = AiboConstant.HotoPlayEnum.getValueByKey(Xsoup.select(document, "//w/@li").get()).getValue();
            String homeName = Xsoup.select(str, "//w" + i + "/@h").get();
            String visitiName = Xsoup.select(str, "//w" + i + "/@a").get();
            String homeAbbrName = Xsoup.select(str, "//w" + i + "/@hf").get();
            String visitiAbbrName = Xsoup.select(str, "//w" + i + "/@af").get();                
            String matchName = Xsoup.select(document, "//w" + i + "/@gn").get();
            String startTimeStr = Xsoup.select(str, "//w" + i + "/@gt").get();
            String matchTypeName = Xsoup.select(str, "//w" + i + "/@gn").get();

            String sp0 = Xsoup.select(str, "//w" + i + "/@c1").get();
            String sp1 = Xsoup.select(str, "//w" + i + "/@c3").get();
            String sp2 = Xsoup.select(str, "//w" + i + "/@c5").get();
            String sp3 = Xsoup.select(str, "//w" + i + "/@c7").get();
            String sp4 = Xsoup.select(str, "//w" + i + "/@c9").get();
            String sp5 = Xsoup.select(str, "//w" + i + "/@c11").get();
            String sp6 = Xsoup.select(str, "//w" + i + "/@c13").get();
            String sp7 = Xsoup.select(str, "//w" + i + "/@c15").get();

            String rt = Xsoup.select(str, "//w" + i + "/@rt").get();//开奖结果
            String sp = Xsoup.select(str, "//w" + i + "/@sp").get();//开奖sp
            String sr = Xsoup.select(str, "//w" + i + "/@sr").get();//比分
            String[] srArray = sr.split("\\$");

            String matchStatus = Xsoup.select(str, "//w" + i + "/@st").get();
            if (matchStatus.equals("1") || matchStatus.equals("2")) {
                sportDataBjGoalPO = new SportDataBjGoalPO();

                sportDataBjGoalPO.setLotteryCode(lotteryCode);
                sportDataBjGoalPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                sportDataBjGoalPO.setBjNum(i);
                sportDataBjGoalPO.setMatchName(matchName);
                sportDataBjGoalPO.setHomeName(homeName);
                sportDataBjGoalPO.setHomeAbbrName(homeAbbrName);
                sportDataBjGoalPO.setVisitiName(visitiName);
                sportDataBjGoalPO.setVisitiAbbrName(visitiAbbrName);
                Date startTime = DateUtil.convertStrToDate(startTimeStr, DateUtil.DATETIME_FORMAT_NO_SEC);
                sportDataBjGoalPO.setStartTime(startTime);
                sportDataBjGoalPO.setSaleDate(SportLotteryUtil.getBjSaleDate(startTime));
                sportDataBjGoalPO.setMatchStatus(AiboConstant.MatchStatusEnum.getValueByKey(Integer.valueOf(matchStatus)).getValue().toString());
                sportDataBjGoalPO.setCreateBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                sportDataBjGoalPO.setMatchType(matchTypeName);


                sportDataBjGoalPO.setSp0Goal(NumberUtil.parseString(sp0));
                sportDataBjGoalPO.setSp1Goal(NumberUtil.parseString(sp1));
                sportDataBjGoalPO.setSp2Goal(NumberUtil.parseString(sp2));
                sportDataBjGoalPO.setSp3Goal(NumberUtil.parseString(sp3));
                sportDataBjGoalPO.setSp4Goal(NumberUtil.parseString(sp4));
                sportDataBjGoalPO.setSp5Goal(NumberUtil.parseString(sp5));
                sportDataBjGoalPO.setSp6Goal(NumberUtil.parseString(sp6));
                sportDataBjGoalPO.setSp7Goal(NumberUtil.parseString(sp7));

                try {
                    String union = DigestUtils.md5DigestAsHex(sportDataBjGoalPO.toString().getBytes("UTF-8"));
                    sportDataBjGoalPO.setMd5Value(union);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                sportDataBjGoalService.save(sportDataBjGoalPO);
                inStockList.add(sportDataBjGoalPO);
                bjWdfPOList.add(sportDataBjGoalPO);
            } else {
                //如果赛事状态为-2已开奖，3已计算，4已返奖，保存彩果

                drawBjPO = new SportDrawBjPO();
                drawBjPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                drawBjPO.setLotteryCode(lotteryCode);
                drawBjPO.setBjNum(i);
                if (!StringUtil.isBlank(sp)) {
                    drawBjPO.setSpGoalNum(Double.valueOf(sp));//总进球SP值
                }
                if (!StringUtil.isBlank(rt) && !AiboConstant.DrawEnum.OTHER.getKey().equals(sr)) {
                    drawBjPO.setGoalNum(AiboConstant.GoalDrawEnum.getValueByKey(rt).getValue());//总进球数
                    //如果取到彩果不是延，才设置值
                    drawBjPO.setHalfScore(srArray[0]);
                    drawBjPO.setFullScore(srArray[1]);
                }
                drawbj(drawBjPO);

            }
            i++;
            System.out.println("BusinessServiceImpl.goal" + i);
        }
        handlBjdcMq(bjWdfPOList);
        //入库放在发送消息后面
        for (SportDataBjGoalPO dataBjGoalPO : inStockList) {
            sportDataBjGoalService.save(dataBjGoalPO);

            //处理子玩法状态
            SportStatusBjPO po = sportStatusBjService.select(JsonUtil.json2Map(JSON.toJSONString(dataBjGoalPO)));
            if (ObjectUtil.isBlank(po)) {
                //如果数据库没有就insert
                po = new SportStatusBjPO(dataBjGoalPO);
                po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                po.setStatusGoal(Constants.bjdcStatus.REGULAR_SALE.getKey());
                sportStatusBjService.insert(po);
            } else {
            	if(dataBjGoalPO.getMatchStatus().equals("9")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusGoal(Constants.bjdcStatus.REGULAR_SALE.getKey());
            	}else if(dataBjGoalPO.getMatchStatus().equals("7")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusGoal(Constants.bjdcStatus.SUSPENDED_SALES.getKey());
            	}              	
                sportStatusBjService.updateByPrimaryKey(po);
            }
        }
    }


    /**
     * 处理胜平负
     *
     * @param document  表示selling xml
     * @param list      赛事及SP等信息
     * @param issueCode 彩期
     */
    private void wfl(Document document, List<String> list, String issueCode) {
        List<SportDataBjBasePO> bjWdfPOList = new ArrayList<>();
        List<SportDataBjWdfPO> inStockList = new ArrayList<>();
        SportDataBjWdfPO sportDataBjWdfPO;
        SportDrawBjPO drawBjPO;
        int i = 1;
        for (String str : list) {

            String aiboLotteryCode = Xsoup.select(document, "//w/@li").get();
            AiboConstant.HotoPlayEnum hotoPlayEnum = AiboConstant.HotoPlayEnum.getValueByKey(aiboLotteryCode);
            if (null == hotoPlayEnum) {
                throw new RuntimeException("北单爱波接口匹配彩种错误，北单数据【" + Integer.valueOf(aiboLotteryCode) + "】");
            }
            Integer lotteryCode = hotoPlayEnum.getValue();

            String letNum = Xsoup.select(str, "//w" + i + "/@r").get();

            String homeName = Xsoup.select(str, "//w" + i + "/@h").get();
            String visitiName = Xsoup.select(str, "//w" + i + "/@a").get();
            String homeAbbrName = Xsoup.select(str, "//w" + i + "/@hf").get();
            String visitiAbbrName = Xsoup.select(str, "//w" + i + "/@af").get();             
            String matchName = Xsoup.select(document, "//w" + i + "/@gn").get();
            String startTimeStr = Xsoup.select(str, "//w" + i + "/@gt").get();
            String matchTypeName = Xsoup.select(str, "//w" + i + "/@gn").get();


            String spWin = Xsoup.select(str, "//w" + i + "/@c1").get();
            String spDraw = Xsoup.select(str, "//w" + i + "/@c3").get();
            String spFail = Xsoup.select(str, "//w" + i + "/@c5").get();


            String rt = Xsoup.select(str, "//w" + i + "/@rt").get();//开奖结果
            String sp = Xsoup.select(str, "//w" + i + "/@sp").get();//开奖sp
            String sr = Xsoup.select(str, "//w" + i + "/@sr").get();//比分

            String[] srArray = sr.split("\\$");

            String matchStatus = Xsoup.select(str, "//w" + i + "/@st").get();

            //如果是未开售或销售中的才取值
            if (matchStatus.equals("1") || matchStatus.equals("2")) {
                sportDataBjWdfPO = new SportDataBjWdfPO();
                sportDataBjWdfPO.setLotteryCode(lotteryCode);
                sportDataBjWdfPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                sportDataBjWdfPO.setBjNum(i);
                sportDataBjWdfPO.setMatchName(matchName);
                sportDataBjWdfPO.setHomeName(homeName);
                sportDataBjWdfPO.setHomeAbbrName(homeAbbrName);
                sportDataBjWdfPO.setVisitiName(visitiName);
                sportDataBjWdfPO.setVisitiAbbrName(visitiAbbrName);
                Date startTime = DateUtil.convertStrToDate(startTimeStr, DateUtil.DATETIME_FORMAT_NO_SEC);
                sportDataBjWdfPO.setStartTime(startTime);
                sportDataBjWdfPO.setSaleDate(SportLotteryUtil.getBjSaleDate(startTime));
                sportDataBjWdfPO.setMatchStatus(AiboConstant.MatchStatusEnum.getValueByKey(Integer.valueOf(matchStatus)).getValue().toString());
                sportDataBjWdfPO.setCreateBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                sportDataBjWdfPO.setMatchType(matchTypeName);


                sportDataBjWdfPO.setLetNum(NumberUtil.parseString(letNum));
                sportDataBjWdfPO.setSpWin(NumberUtil.parseString(spWin));
                sportDataBjWdfPO.setSpDraw(NumberUtil.parseString(spDraw));
                sportDataBjWdfPO.setSpFail(NumberUtil.parseString(spFail));

                try {
                    String union = DigestUtils.md5DigestAsHex(sportDataBjWdfPO.toString().getBytes("UTF-8"));
                    sportDataBjWdfPO.setMd5Value(union);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //sportDataBjWdfService.save(sportDataBjWdfPO);
                inStockList.add(sportDataBjWdfPO);
                bjWdfPOList.add(sportDataBjWdfPO);
            } else {
                //如果赛事状态为-2已开奖，3已计算，4已返奖，保存彩果

                drawBjPO = new SportDrawBjPO();
                drawBjPO.setIssueCode(SportLotteryUtil.handleBjIssueCode(issueCode));
                drawBjPO.setLotteryCode(lotteryCode);
                drawBjPO.setBjNum(i);
                if (!StringUtil.isBlank(sp)) {
                    drawBjPO.setSpLetWdf(Double.valueOf(sp));//让球胜平负SP值
                }
                drawBjPO.setLetNum(Double.valueOf(letNum));//让球值
                if (!StringUtil.isBlank(rt) && !AiboConstant.DrawEnum.OTHER.getKey().equals(sr)) {
                    drawBjPO.setLetWdf(AiboConstant.DrawEnum.getValueByKey(rt).getValue());//让球胜平负；0：负；1：平；3：胜
                    //如果取到彩果不是延，才设置值
                    drawBjPO.setHalfScore(srArray[0]);
                    drawBjPO.setFullScore(srArray[1]);
                }

                drawbj(drawBjPO);
            }

            i++;
        }
        handlBjdcMq(bjWdfPOList);
        for (SportDataBjWdfPO dataBjWdfPO : inStockList) {
            sportDataBjWdfService.save(dataBjWdfPO);

            //处理子玩法状态
            SportStatusBjPO po = sportStatusBjService.select(JsonUtil.json2Map(JSON.toJSONString(dataBjWdfPO)));
            if (ObjectUtil.isBlank(po)) {
                po = new SportStatusBjPO(dataBjWdfPO);
                po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                po.setStatusLetWdf(Constants.bjdcStatus.REGULAR_SALE.getKey());
                sportStatusBjService.insert(po);
            } else {
            	if(dataBjWdfPO.getMatchStatus().equals("9")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusLetWdf(Constants.bjdcStatus.REGULAR_SALE.getKey());
            	}else if(dataBjWdfPO.getMatchStatus().equals("7")){
            		po.setModifyBy(SportEnum.SportDataSource.AIBO_OFFICIAL.getName());
                    po.setStatusLetWdf(Constants.bjdcStatus.SUSPENDED_SALES.getKey());
            	}               	
                sportStatusBjService.updateByPrimaryKey(po);
            }

        }
    }


    /**
     * 处理北单比赛时间有变动的时候发送MQ消息
     * 当抓取的对阵赛事有新增 对阵赛事时,  数据库现有10场比赛, 当抓取有11场比赛时, 需要MQ通知
     *
     * @param bjBasePOList 参数
     */
    private void handlBjdcMq(List<SportDataBjBasePO> bjBasePOList) {

        //TODO 这里只能解决比赛时间变动，没办法解决新增赛事问题
        Map<String, Integer> map = new HashMap<>();
        for (SportDataBjBasePO po : bjBasePOList) {
            SportAgainstInfoPO queryPO = new SportAgainstInfoPO();
            queryPO.setIssueCode(po.getIssueCode());
            queryPO.setLotteryCode(po.getLotteryCode());
            queryPO.setBjNum(po.getBjNum());
            List<SportAgainstInfoPO> resultList = sportAgainstInfoDaoMapper.findCondition(queryPO);
            SportAgainstInfoPO resultPO;
            if (!ObjectUtil.isBlank(resultList)) {
                resultPO = resultList.get(0);
                if (!po.getStartTime().equals(resultPO.getStartTime())) {
                    //调用MQ发消息
                    //mqUtils.sendMessage(po.getIssueCode(), po.getLotteryCode());
                    map.put(po.getIssueCode(), po.getLotteryCode());
                    break;
                }
            } else {
                map.put(po.getIssueCode(), po.getLotteryCode());
                break;
            }
        }
        redisUtil.addObj(START_TIME_DIFF_KEY, map, CacheConstants.ONE_DAY);
    }


    private void drawbjsfgg(SportDrawWfPO sportDrawWfPO) {
        int row = drawWfDaoMapper.updateSelective(sportDrawWfPO);
        if (row == 0) {
            drawWfDaoMapper.insertSelective(sportDrawWfPO);
        }
    }

    private void drawbj(SportDrawBjPO sportDrawBjPO) {
        int row = drawBjDaoMapper.updateSelective(sportDrawBjPO);
        if (row == 0) {
            drawBjDaoMapper.insertSelective(sportDrawBjPO);
        }
    }

}
