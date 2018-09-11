package com.hhly.crawlcore.sport.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbGoalDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbHfWdfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbScoreDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbSpDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbWdfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportMatchOddDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportStatusFbDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbGoalPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbHfWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbScorePO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchOddPO;
import com.hhly.crawlcore.persistence.sport.po.SportStatusFbPO;
import com.hhly.crawlcore.rabbitmq.provider.MessageProvider;
import com.hhly.crawlcore.sport.service.CrawlerService;
import com.hhly.crawlcore.sport.service.ResetSportCacheService;
import com.hhly.crawlcore.sport.service.SportFootballService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.JcMatchStatusEnum;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MQConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.mq.msg.JzSpMessageData;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.NumberFormatUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.sport.bo.JcAvgOddsBO;
import com.hhly.skeleton.lotto.base.sport.bo.JczqTrendSpBO;

/**
 * Created by lgs on 2017/1/5.
 * 抓取竞彩足球数据
 */
@Service
public class SportFootballServiceImpl implements SportFootballService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportFootballServiceImpl.class);
    /**
     * 让分胜平负历史sp值key
     */
    private final String LET_WDF_HISTORY_SP = "let_wdf_history_sp";
    /**
     * 胜平负历史sp值key
     */
    private final String WDF_HISTORY_SP = "wdf_history_sp";
    /**
     * 半全场胜负历史sp值key
     */
    private final String HF_HISTORY_SP = "hf_history_sp";
    /**
     * 比分历史sp值key
     */
    private final String SCORE_HISTORY_SP = "score_history_sp";
    /**
     * 进球历史sp值key
     */
    private final String GOAL_HISTORY_SP = "goal_history_sp";
    /**
     * 让分胜平负历史sp值key
     */
    private final String LET_WDF_HISTORY_SP_INIT = "let_wdf_history_sp_init";
    /**
     * 胜平负历史sp值key
     */
    private final String WDF_HISTORY_SP_INIT = "wdf_history_sp_init";
    /**
     * 半全场胜负历史sp值key
     */
    private final String HF_HISTORY_SP_INIT = "hf_history_sp_init";
    /**
     * 比分历史sp值key
     */
    private final String SCORE_HISTORY_SP_INIT = "score_history_sp_init";
    /**
     * 进球历史sp值key
     */
    private final String GOAL_HISTORY_SP_INIT = "goal_history_sp_init";

    /**
     * 让分胜平负SP值变化key
     */
    private final String LET_WDF_TREND_SP = "let_wdf_trend_sp";

    /**
     * 胜平负SP值变化key
     */
    private final String WDF_TREND_SP = "wdf_trend_sp";
//    @Value("${football_match_list}")
//    public String FOOTBALL_MATCH_LIST;
    @Value("${football_match_interim}")
    public String FOOTBALL_MATCH_INTERIM;
    @Value("${football_pool_result}")
    public String FOOTBALL_POOL_RESULT;
    @Value("${football_hhad_list}")
    public String FOOTBALL_HHAD_LIST;
    @Value("${football_wx_match_list}")
    public String FOOTBALL_WX_MATCH_LIST;
    @Value("${football_wx_mid_match_odd}")
    public String FOOTBALL_WX_MID_MATCH_ODD;
//    @Value("${football_odds_calculator}")
//    public String FOOTBALL_ODDS_CALCULATOR;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

    @Autowired
    private SportDataFbWdfDaoMapper sportDataFbWDFDaoMapper;

    @Autowired
    private SportDataFbGoalDaoMapper sportDataFbGoalDaoMapper;

    @Autowired
    private SportDataFbHfWdfDaoMapper sportDataFbHfWDFDaoMapper;

    @Autowired
    private SportDataFbScoreDaoMapper sportDataFbScoreDaoMapper;

    @Autowired
    private SportDataFbSpDaoMapper sportDataFbSpDaoMapper;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private SportMatchOddDaoMapper sportMatchOddDaoMapper;

    @Autowired
    private ResetSportCacheService resetSportCacheService;

    @Resource
    private MQUtils mqUtils;

    @Resource
    private MessageProvider messageProvider;

    @Resource
    private SportStatusFbDaoMapper statusFBDaoMapper;

    /**
     * 是否执行完标识
     */
    private boolean exeFlag = false;

    /**
     * 抓取微信接口sp值是否执行中标识
     */
    private boolean wxFlag = false;


    public String getMatchListJson(String url) throws Exception {
        String json = HTTPUtil.transRequest(url);
        if (json.equals("0") || json.contains("error")) {
            json = getMatchListJson(url);
        }
        return json;
    }

    @Override
    public void saveMatchList() {
        String url = FOOTBALL_HHAD_LIST + new Date().getTime();
        String json = null;
        try {
            json = getMatchListJson(url);
        } catch (Exception e) {
            LOGGER.error("抓取官网子玩法状态异常：" + e.getMessage());
        }
        SportStatusFbPO statusFBPO;
        String initData = StringUtils.stripEnd(StringUtils.stripStart(json, "getData("), SymbolConstants.PARENTHESES_RIGHT + SymbolConstants.SEMICOLON);
        JSONObject initJson = JSONObject.parseObject(initData);
        JSONObject data = initJson.getJSONObject("data");
        for (Object object : data.values()) {
            statusFBPO = new SportStatusFbPO();
            JSONObject jsonObject = JSON.parseObject(object.toString());
            Integer lotteryCode = LotteryEnum.Lottery.FB.getName();
            String issueCode = DateUtil.convertStrToTarget(jsonObject.getString("b_date"), DateUtil.DATE_FORMAT, DateUtil.FORMAT_YYMMDD);
            String officialMatchCode = jsonObject.getString("num");
    		String systemCode = SportLotteryUtil.getSystemCode(officialMatchCode, issueCode);
            Long id = sportAgainstInfoDaoMapper.findIdByCode(lotteryCode, issueCode, systemCode);
            
            JSONObject hhad = jsonObject.getJSONObject("hhad");//让分胜平负单关状态
            JSONObject had = jsonObject.getJSONObject("had");//胜平负单关状态
            String hhad_status;
            String had_status;
            if (!ObjectUtil.isBlank(hhad)) {
                hhad_status = hhad.getString("single");
                if (hhad_status.equals("0")) {
                    statusFBPO.setStatusLetWdf(SportEnum.SportSaleStatusEnum.PASS_SALE.getValue().shortValue());
                } else if (hhad_status.equals("1")) {
                    statusFBPO.setStatusLetWdf(SportEnum.SportSaleStatusEnum.NORMAL_SALE.getValue().shortValue());
                }
            } else {
                statusFBPO.setStatusLetWdf(SportEnum.SportSaleStatusEnum.STOP_SALE.getValue().shortValue());
            }
            if (!ObjectUtil.isBlank(had)) {
                had_status = had.getString("single");
                if (had_status.equals("0")) {
                    statusFBPO.setStatusWdf(SportEnum.SportSaleStatusEnum.PASS_SALE.getValue().shortValue());
                } else if (had_status.equals("1")) {
                    statusFBPO.setStatusWdf(SportEnum.SportSaleStatusEnum.NORMAL_SALE.getValue().shortValue());
                }
            } else {
                statusFBPO.setStatusWdf(SportEnum.SportSaleStatusEnum.STOP_SALE.getValue().shortValue());
            }
            statusFBPO.setStatusGoal(SportEnum.SportSaleStatusEnum.NORMAL_SALE.getValue().shortValue());
            statusFBPO.setStatusHfWdf(SportEnum.SportSaleStatusEnum.NORMAL_SALE.getValue().shortValue());
            statusFBPO.setStatusScore(SportEnum.SportSaleStatusEnum.NORMAL_SALE.getValue().shortValue());
            statusFBPO.setModifyBy(SportEnum.SportDataSource.JC_OFFICIAL.getName());
            statusFBPO.setSportAgainstInfoId(id);
            int row = statusFBDaoMapper.selectCount(statusFBPO);
            if (row < 1) {
                statusFBDaoMapper.insert(statusFBPO);
            }
        }
    }

    @Override
    public void saveMatchInterim() {
        try {
            List<SportAgainstInfoPO> pos = crawlerService.crawlerMatchInterim(FOOTBALL_MATCH_INTERIM, LotteryEnum.Lottery.FB.getName(), String.valueOf(SportEnum.MatchTypeEnum.FOOTBALL.getCode()));
            for (SportAgainstInfoPO po : pos) {
                int flag = sportAgainstInfoDaoMapper.updateInterimMatch(po);
                if (flag == 0) {
                    sportAgainstInfoDaoMapper.insert(po);
                }
            }
            LOGGER.debug("crawler football match interim Size is" + pos.size() + " Data is " + JsonUtil.objectToJson(pos));
        } catch (Exception e) {
            LOGGER.error("crawler football match interim Data error " + e.getMessage());
        }
    }


    public String getPoolResultElements(String url) throws Exception {
        String result = HTTPUtil.transRequest(url);
        if (StringUtil.isBlank(result) || result.equals("0")) {
            result = getPoolResultElements(url);
        }
        return result;
    }

    @Override
    public Map<String, Object> savePoolResult(String officialId, Long sportAgainstInfoId) {
        if (sportAgainstInfoId == 0 || StringUtils.isBlank(officialId)) {
            return null;
        }
        List<SportDataFbWdfPO> letWdfPos = new ArrayList<>();//保存让球胜平负SP
        List<SportDataFbWdfPO> wdfPos = new ArrayList<>();//保存胜平负SP
        List<SportDataFbGoalPO> goalPos = new ArrayList<>();//保存进球数SP
        List<SportDataFbScorePO> scorePos = new ArrayList<>();//保存比分SP
        List<SportDataFbHfWdfPO> hfWdfPos = new ArrayList<>();//保存半全场胜平负SP
        String url = FOOTBALL_POOL_RESULT.replace("{0}", officialId).replace("{1}", String.valueOf(new Date().getTime()));
        try {

            String json = getPoolResultElements(url);
            json = StringUtils.stripEnd(StringUtils.stripEnd(StringUtils.stripStart(json, "pool_prcess("), ";"), ")");


            JSONObject jsonObject = JsonUtil.jsonToObject(json, JSONObject.class);

            JSONObject statusObject = jsonObject.getJSONObject("status");

            if (statusObject.getString("code").equals("0")) {

            }

            JSONObject resultObject = jsonObject.getJSONObject("result").getJSONObject("odds_list");


            JSONArray wdfArray = new JSONArray();
            JSONArray letWdfArray = new JSONArray();
            JSONArray hfWdfArray = new JSONArray();
            JSONArray goalArray = new JSONArray();
            JSONArray scoreArray = new JSONArray();

            try {
                wdfArray = resultObject.getJSONObject("had").getJSONArray("odds");
            } catch (Exception e) {
                LOGGER.error("crawler pool result error officialId is" + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
                letWdfArray = resultObject.getJSONObject("hhad").getJSONArray("odds");
            } catch (Exception e) {
                LOGGER.error("crawler pool result error officialId is" + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
                hfWdfArray = resultObject.getJSONObject("hafu").getJSONArray("odds");
            } catch (Exception e) {
                LOGGER.error("crawler pool result error officialId is" + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
                goalArray = resultObject.getJSONObject("ttg").getJSONArray("odds");
            } catch (Exception e) {
                LOGGER.error("crawler pool result error officialId is" + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
                scoreArray = resultObject.getJSONObject("crs").getJSONArray("odds");
            } catch (Exception e) {
                LOGGER.error("crawler pool result error officialId is" + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            //解析让球胜平负
            //index从2开始 0和1是表头不需要读取
            Short letNum = resultObject.getJSONObject("hhad").getShort("goalline");
            for (int i = 0; i < letWdfArray.size(); i++) {
                JSONObject lefWdfObject = letWdfArray.getJSONObject(i);


                Float winSp = lefWdfObject.getFloat("h");
                Float drawSp = lefWdfObject.getFloat("d");
                Float lostSp = lefWdfObject.getFloat("a");
                String releaseTime = lefWdfObject.getString("date") + " " + lefWdfObject.getString("time");
                SportDataFbWdfPO wdfPo = new SportDataFbWdfPO(sportAgainstInfoId, letNum, winSp,
                        drawSp, lostSp, (short) Constants.NUM_2, DateUtil.convertStrToDate(releaseTime));
                letWdfPos.add(wdfPo);
            }

            //解析胜平负
            //index从2开始 0和1是表头不需要读取
            for (int i = 0; i < wdfArray.size(); i++) {
                JSONObject wdfObject = wdfArray.getJSONObject(i);
                Float winSp = wdfObject.getFloat("h");
                Float drawSp = wdfObject.getFloat("d");
                Float lostSp = wdfObject.getFloat("a");
                String releaseTime = wdfObject.getString("date") + " " + wdfObject.getString("time");
                SportDataFbWdfPO wdfPo = new SportDataFbWdfPO(sportAgainstInfoId, null, winSp, drawSp, lostSp, (short) Constants.NUM_1, DateUtil.convertStrToDate(releaseTime));
                wdfPos.add(wdfPo);
            }

            //解析进球数
            //index从2开始 0和1是表头不需要读取
            for (int i = 0; i < goalArray.size(); i++) {
                JSONObject goalObject = goalArray.getJSONObject(i);

                Float sp0Goal = goalObject.getFloat("s0");
                Float sp1Goal = goalObject.getFloat("s1");
                Float sp2Goal = goalObject.getFloat("s2");
                Float sp3Goal = goalObject.getFloat("s3");
                Float sp4Goal = goalObject.getFloat("s4");
                Float sp5Goal = goalObject.getFloat("s5");
                Float sp6Goal = goalObject.getFloat("s6");
                Float sp7Goal = goalObject.getFloat("s7");
                String releaseTime = goalObject.getString("date") + " " + goalObject.getString("time");
                SportDataFbGoalPO goalPo = new SportDataFbGoalPO(sportAgainstInfoId, sp0Goal, sp1Goal, sp2Goal, sp3Goal, sp4Goal, sp5Goal, sp6Goal, sp7Goal, DateUtil.convertStrToDate(releaseTime));
                goalPos.add(goalPo);
            }

            //解析半全场胜平负
            //index从2开始 0和1是表头不需要读取
            for (int i = 0; i < hfWdfArray.size(); i++) {
                JSONObject hfWdfObject = hfWdfArray.getJSONObject(i);

                String releaseTime = hfWdfObject.getString("date") + " " + hfWdfObject.getString("time");

                Float spWW = hfWdfObject.getFloat("hh");
                Float spWD = hfWdfObject.getFloat("hd");
                Float spWF = hfWdfObject.getFloat("ha");
                Float spDW = hfWdfObject.getFloat("dh");
                Float spDD = hfWdfObject.getFloat("dd");
                Float spDF = hfWdfObject.getFloat("da");
                Float spFW = hfWdfObject.getFloat("ah");
                Float spFD = hfWdfObject.getFloat("ad");
                Float spFF = hfWdfObject.getFloat("aa");

                SportDataFbHfWdfPO hfWdf = new SportDataFbHfWdfPO(sportAgainstInfoId, spWW, spWD, spWF, spDW, spDD, spDF, spFW, spFD, spFF, DateUtil.convertStrToDate(releaseTime));
                hfWdfPos.add(hfWdf);
            }


            //解析比分SP
            //index从1开始 0是表头不需要读取
            for (int i = 0; i < scoreArray.size(); i++) {
                JSONObject scoreObject = scoreArray.getJSONObject(i);

                String releaseTime = scoreObject.getString("date") + " " + scoreObject.getString("time");

                Float sp10 = scoreObject.getFloat("0100");
                Float sp20 = scoreObject.getFloat("0200");
                Float sp21 = scoreObject.getFloat("0201");
                Float sp30 = scoreObject.getFloat("0300");
                Float sp31 = scoreObject.getFloat("0301");
                Float sp32 = scoreObject.getFloat("0302");
                Float sp40 = scoreObject.getFloat("0400");
                Float sp41 = scoreObject.getFloat("0401");
                Float sp42 = scoreObject.getFloat("0402");
                Float sp50 = scoreObject.getFloat("0500");
                Float sp51 = scoreObject.getFloat("0501");
                Float sp52 = scoreObject.getFloat("0502");
                Float spWOther = scoreObject.getFloat("-1-h");


                Float sp00 = scoreObject.getFloat("0000");
                Float sp11 = scoreObject.getFloat("0101");
                Float sp22 = scoreObject.getFloat("0202");
                Float sp33 = scoreObject.getFloat("0303");
                Float spDOther = scoreObject.getFloat("-1-d");


                Float sp01 = scoreObject.getFloat("0001");
                Float sp02 = scoreObject.getFloat("0002");
                Float sp12 = scoreObject.getFloat("0102");
                Float sp03 = scoreObject.getFloat("0003");
                Float sp13 = scoreObject.getFloat("0103");
                Float sp23 = scoreObject.getFloat("0203");
                Float sp04 = scoreObject.getFloat("0004");
                Float sp14 = scoreObject.getFloat("0104");
                Float sp24 = scoreObject.getFloat("0204");
                Float sp05 = scoreObject.getFloat("0005");
                Float sp15 = scoreObject.getFloat("0105");
                Float sp25 = scoreObject.getFloat("0205");
                Float spFOther = scoreObject.getFloat("-1-a");

                SportDataFbScorePO score = new SportDataFbScorePO(sportAgainstInfoId, sp10, sp20, sp21, sp30, sp31, sp32, sp40, sp41, sp42, sp50, sp51, sp52, spWOther,
                        sp00, sp11, sp22, sp33, spDOther,
                        sp01, sp02, sp12, sp03, sp13, sp23, sp04, sp14, sp24, sp05, sp15, sp25, spFOther,
                        DateUtil.convertStrToDate(releaseTime));
                scorePos.add(score);
                i = i + 7;
            }

            Map<String, Object> spMap = new HashMap<>();
            if (letWdfPos.size() > 0) {
                spMap.put(LET_WDF_HISTORY_SP, letWdfPos);
                spMap.put(LET_WDF_HISTORY_SP_INIT, letWdfPos.get(0));
                SportDataFbWdfPO po = letWdfPos.get(letWdfPos.size() - 1);//获取最后一个sp值进行对比
                int total = sportDataFbWDFDaoMapper.selectCount(po);
                //查看SP值是否有变化
                if (total < 1) {
                    spMap.put(LET_WDF_TREND_SP, po);
                    //sportDataFbWDFDaoMapper.insert(po);
                }
            }

            if (wdfPos.size() > 0) {
                spMap.put(WDF_HISTORY_SP, wdfPos);
                spMap.put(WDF_HISTORY_SP_INIT, wdfPos.get(0));
                SportDataFbWdfPO po = wdfPos.get(wdfPos.size() - 1);//获取最后一个sp值进行对比
                int total = sportDataFbWDFDaoMapper.selectCount(po);
                //查看SP值是否有变化
                if (total < 1) {
                    spMap.put(WDF_TREND_SP, po);
                    //sportDataFbWDFDaoMapper.insert(po);
                }
            }

            if (goalPos.size() > 0) {
                spMap.put(GOAL_HISTORY_SP, goalPos);
                spMap.put(GOAL_HISTORY_SP_INIT, goalPos.get(goalPos.size() - 1));
            }

            if (scorePos.size() > 0) {
                spMap.put(SCORE_HISTORY_SP, scorePos);
                spMap.put(SCORE_HISTORY_SP_INIT, scorePos.get(scorePos.size() - 1));
            }

            if (hfWdfPos.size() > 0) {
                spMap.put(HF_HISTORY_SP, hfWdfPos);
                spMap.put(HF_HISTORY_SP_INIT, hfWdfPos.get(hfWdfPos.size() - 1));
            }

            LOGGER.debug("crawler pool result officialId is" + officialId + " sportAgainstInfoId " + sportAgainstInfoId);
            LOGGER.debug("crawler pool result let wdf Size is" + letWdfPos.size() + " Data is " + JsonUtil.objectToJson(letWdfPos));
            LOGGER.debug("crawler pool result wdf Size is" + wdfPos.size() + " Data is " + JsonUtil.objectToJson(wdfPos));
            LOGGER.debug("crawler pool result goal Size is" + goalPos.size() + " Data is " + JsonUtil.objectToJson(goalPos));
            LOGGER.debug("crawler pool result let score Size is" + scorePos.size() + " Data is " + JsonUtil.objectToJson(scorePos));
            LOGGER.debug("crawler pool result hf wdf Size is" + hfWdfPos.size() + " Data is " + JsonUtil.objectToJson(hfWdfPos));
            return spMap;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("crawler pool result error officialId is" + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
        }
        return null;
    }

    @Override
    public void savePoolResultList() throws InterruptedException, ExecutionException {
        if (exeFlag) {
            LOGGER.info("正在执行抓取，请勿重复执行");
            return;
        }
        exeFlag = true;
        try {
            long beginTime = System.currentTimeMillis();
            List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.FB.getName());

            if (list == null || list.size() < 1) {
                LOGGER.error("crawler pool result list not SportAgainstInfoPO");
                return;
            }
            List<Map<String, Object>> resultList = new ArrayList<>();

            List<Callable<Map<String, Object>>> callList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i % 15 == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                final SportAgainstInfoPO po = list.get(i);
                try {
                    Callable<Map<String, Object>> callable = new Callable<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> call() throws Exception {
                            return savePoolResult(po.getOfficialId(), po.getId());
                        }
                    };
                    callList.add(callable);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("crawler pool result error officialId is" + po.getOfficialId() + " sportAgainstInfoId " + po.getId() + "message:" + e.getMessage());
                }
            }

            List<Future<Map<String, Object>>> feature = ThreadPoolManager.invokeAll("更新赛程历史SP值", callList);

            for (Future<Map<String, Object>> featureMap : feature) {
                resultList.add(featureMap.get());
            }
            //入库
            List<SportDataFbWdfPO> letWdfPos = new ArrayList<>();//保存让球胜平负SP
            List<SportDataFbWdfPO> wdfPos = new ArrayList<>();//保存胜平负SP
            List<SportDataFbGoalPO> goalPos = new ArrayList<>();//保存进球数SP
            List<SportDataFbScorePO> scorePos = new ArrayList<>();//保存比分SP
            List<SportDataFbHfWdfPO> hfWdfPos = new ArrayList<>();//保存半全场胜平负SP

            List<JczqTrendSpBO> trendList = new ArrayList<>();


            for (Map<String, Object> map : resultList) {

                if (ObjectUtil.isBlank(map)) {
                    continue;
                }

                if (!ObjectUtil.isBlank(map.get(WDF_HISTORY_SP))) {
                    wdfPos.addAll((List<SportDataFbWdfPO>) map.get(WDF_HISTORY_SP));
                }
                if (!ObjectUtil.isBlank(map.get(LET_WDF_HISTORY_SP))) {
                    letWdfPos.addAll((List<SportDataFbWdfPO>) map.get(LET_WDF_HISTORY_SP));
                }
                if (!ObjectUtil.isBlank(map.get(GOAL_HISTORY_SP))) {
                    goalPos.addAll((List<SportDataFbGoalPO>) map.get(GOAL_HISTORY_SP));
                }
                if (!ObjectUtil.isBlank(map.get(SCORE_HISTORY_SP))) {
                    scorePos.addAll((List<SportDataFbScorePO>) map.get(SCORE_HISTORY_SP));
                }
                if (!ObjectUtil.isBlank(map.get(HF_HISTORY_SP))) {
                    hfWdfPos.addAll((List<SportDataFbHfWdfPO>) map.get(HF_HISTORY_SP));
                }

                //SP值变化 存数组格式用于推送给前端 格式为 [w,d,f,let_w,let_d,let_f]
                if (!ObjectUtil.isBlank(map.get(WDF_TREND_SP)) || !ObjectUtil.isBlank(map.get(LET_WDF_TREND_SP))) {
                    SportDataFbWdfPO wdfPo = new SportDataFbWdfPO();
                    SportDataFbWdfPO letPo = new SportDataFbWdfPO();
                    if (!ObjectUtil.isBlank(map.get(WDF_TREND_SP))) {
                        wdfPo = (SportDataFbWdfPO) map.get(WDF_TREND_SP);
                    }
                    if (!ObjectUtil.isBlank(map.get(LET_WDF_TREND_SP))) {
                        letPo = (SportDataFbWdfPO) map.get(LET_WDF_TREND_SP);
                    }

                    //[w,d,f,let_w,let_d,let_f]
                    String[] spTrend = new String[]{
                            NumberFormatUtil.format(wdfPo.getSpWin()),
                            NumberFormatUtil.format(wdfPo.getSpDraw()),
                            NumberFormatUtil.format(wdfPo.getSpFail()),
                            NumberFormatUtil.format(letPo.getLetNum(), NumberFormatUtil.DEFAULT),
                            NumberFormatUtil.format(letPo.getSpWin()),
                            NumberFormatUtil.format(letPo.getSpDraw()),
                            NumberFormatUtil.format(letPo.getSpFail())
                    };
                    Long againstId = null;
                    if (!ObjectUtil.isBlank(wdfPo.getSportAgainstInfoId())) {
                        againstId = wdfPo.getSportAgainstInfoId();
                    }
                    if (!ObjectUtil.isBlank(letPo.getSportAgainstInfoId())) {
                        againstId = letPo.getSportAgainstInfoId();
                    }
                    JczqTrendSpBO spTrendBO = new JczqTrendSpBO(againstId, spTrend);
                    trendList.add(spTrendBO);
                }

            }

            //sportDataFbWDFDaoMapper.insert(letWdfPos);

            for (SportDataFbWdfPO po : letWdfPos) {
                int flag = sportDataFbWDFDaoMapper.selectCount(po);
                if (flag == 0) {
                    sportDataFbWDFDaoMapper.insert(po);
                }
            }

            for (SportDataFbWdfPO po : wdfPos) {
                int flag = sportDataFbWDFDaoMapper.selectCount(po);
                if (flag == 0) {
                    sportDataFbWDFDaoMapper.insert(po);
                }
            }

            for (SportDataFbScorePO po : scorePos) {
                int flag = sportDataFbScoreDaoMapper.selectCount(po);
                if (flag == 0) {
                    sportDataFbScoreDaoMapper.insert(po);
                }
            }

            for (SportDataFbGoalPO po : goalPos) {
                int flag = sportDataFbGoalDaoMapper.selectCount(po);
                if (flag == 0) {
                    sportDataFbGoalDaoMapper.insert(po);
                }
            }

            for (SportDataFbHfWdfPO po : hfWdfPos) {
                int flag = sportDataFbHfWDFDaoMapper.selectCount(po);
                if (flag == 0) {
                    sportDataFbHfWDFDaoMapper.insert(po);
                }
            }
//        sportDataFbSpDaoMapper.saveInitLetGoalSp(letWdfPos.get(letWdfPos.size() - 1));
//            sportDataFbWDFDaoMapper.insert(wdfPos);
//        sportDataFbSpDaoMapper.saveInitWDFSp(wdfPos.get(wdfPos.size() - 1));
//            sportDataFbGoalDaoMapper.insert(goalPos);
//        sportDataFbSpDaoMapper.saveInitGoalSp(goalPos.get(goalPos.size() - 1));
//            sportDataFbScoreDaoMapper.insert(scorePos);
//        sportDataFbSpDaoMapper.saveInitScoreSp(scorePos.get(scorePos.size() - 1));
//            sportDataFbHfWDFDaoMapper.insert(hfWdfPos);
//        sportDataFbSpDaoMapper.saveInitHFWDFSp(hfWdfPos.get(hfWdfPos.size() - 1));
            long endTime = System.currentTimeMillis();
            LOGGER.debug("crawler pool resultList All list data is:" + JsonUtil.objectToJson(resultList) + "S");
            LOGGER.debug("crawler pool result All list necessity for time:" + ((endTime - beginTime) / 1000) + "S");

            //如果有数据变化就推送前端
            if (trendList.size() > 0) {
                //竞足SP值变化推送
                JzSpMessageData jzSpMessageData = new JzSpMessageData();
                jzSpMessageData.setList(trendList);
                mqUtils.sendSpMessage(MQConstants.FOOTBALL_SP_RABBITMQ_NAME, jzSpMessageData);

                for (JczqTrendSpBO bo : trendList) {
                    resetSportCacheService.resetFbMatchWdfHistorySpById(bo.getId(), (short) 1);
                    resetSportCacheService.resetFbMatchWdfHistorySpById(bo.getId(), (short) 2);
                }
            }
            System.out.println("crawler pool result All list necessity for time:" + ((endTime - beginTime) / 1000) + "S");
        } catch (Exception e) {
            e.printStackTrace();
            exeFlag = false;
        }
        exeFlag = false;
    }

//    @Override
//    public void saveMatchInfo() {
//        try {
//            String json = HTTPUtil.transRequest(FOOTBALL_ODDS_CALCULATOR + "&_=" + new Date().getTime());
//            if (StringUtil.isBlank(json)) {
//                throw new ServiceRuntimeException("抓取无数据");
//            }
//            json = StringUtils.stripEnd(StringUtils.stripStart(json, "getData("), ");");//去除无用字符
//            Map maps = JsonUtil.jsonToObject(json, Map.class);
//            Map<String, JSONObject> data = (Map<String, JSONObject>) maps.get("data");
//            List<SportStatusFBPO> statusFBPOs = new ArrayList<>();
//            List<SportAgainstInfoPO> pos = new ArrayList<>();
//
//            for (Map.Entry<String, JSONObject> entryMap : data.entrySet()) {
//                JSONObject obj = entryMap.getValue();
//                String officialId = obj.getString("id");
//                String weather = obj.getString("weather");
//                String temperature = StringUtils.stripEnd(obj.getString("temperature"), "&amp;deg;");
//                Long matchId = obj.getLong("l_id");
//                Long homeId = obj.getLong("h_id");
//                Long guestId = obj.getLong("a_id");
//                //String color = obj.getString("l_background_color");
//                JSONArray matchArr = obj.getJSONArray("match_info");
//                String remark = null;
//                if (matchArr.size() > 0) {
//                    JSONObject matchInfoObj = (JSONObject) matchArr.get(0);
//                    remark = matchInfoObj.getString("prompt");
//                }
//                Short isNeutral = 0;//是否中立
//                if (remark != null) {
//                    isNeutral = 1;//是否中立
//                }
//                SportAgainstInfoPO po = new SportAgainstInfoPO();
//                po.setSportMatchInfoId(matchId);
//                po.setHomeTeamId(homeId);
//                po.setVisitiTeamId(guestId);
//                po.setOfficialId(officialId);
//
//                if (!StringUtils.isBlank(temperature)) {
//                    temperature = temperature + "° ";
//                }
//
//                if (StringUtil.isBlank(weather)) {
//                    weather = "未知";
//                } else {
//                    weather = temperature + "," + weather;
//                }
//
//                po.setWeather(weather);
//                po.setIsNeutral(isNeutral);
//                po.setRemark(remark);
//                pos.add(po);
//
////                JSONObject hadObj = obj.getJSONObject("had");//胜平负
////                JSONObject hhadObj = obj.getJSONObject("hhad");//让球胜平负
////                JSONObject crsObj = obj.getJSONObject("crs");//比分
////                JSONObject ttgObj = obj.getJSONObject("ttg");//进球
////                JSONObject hafuObj = obj.getJSONObject("hafu");//半全场
//
//
////                SportStatusFBPO statusFBPO = new SportStatusFBPO(officialId, String.valueOf(LotteryEnum.Lottery.FB.getName()), getStatus(hadObj), getStatus(hhadObj),
////                        getStatus(ttgObj), getStatus(hafuObj), getStatus(crsObj), com.hhly.crawlcore.sport.common.Constants.SPORTTERY);
////                statusFBPOs.add(statusFBPO);
//            }
//
//            sportAgainstInfoDaoMapper.updateMatchInfo(pos);
//            //更新天气
//            LOGGER.debug("update match weather Size is" + pos.size() + " Data is " + JsonUtil.objectToJson(pos));
//            //保存赛事状态
////            for (SportStatusFBPO po : statusFBPOs) {
////                int flag = sportStatusFBDaoMapper.selectCount(po);
////                if (flag == 0) {
////                    sportStatusFBDaoMapper.insert(po);
////                }
////            }
//            LOGGER.debug("update football match status Size is" + statusFBPOs.size() + " Data is " + JsonUtil.objectToJson(statusFBPOs));
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOGGER.error("update match weather And match status error:" + e.getMessage());
//        }
//    }


    @Override
    public void saveWxMatchList() {
    	boolean msgFlag = false;
        String url = FOOTBALL_WX_MATCH_LIST + "&_=" + new Date().getTime();
        try {
        	LOGGER.info("抓取竞技足球对阵赛事信息  ----------- start" + FOOTBALL_WX_MATCH_LIST);
            List<SportAgainstInfoPO> sportAgainstInfoPOS = crawlerService.crawlerWxMatchList(url, LotteryEnum.Lottery.FB.getName(), String.valueOf(SportEnum.MatchTypeEnum.FOOTBALL.getCode()));
            LOGGER.info("抓取竞技足球的赛事结果信息 ---------------- size : " + sportAgainstInfoPOS.size());
            for (SportAgainstInfoPO po : sportAgainstInfoPOS) {
//            	if(DateUtil.compare(po.getSaleEndTime(), DateUtil.getNowDate()) == -1)
//            		continue;
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
//                if (!ObjectUtil.isBlank(resultPO)) {
//                    //禅道任务ID1481，当竞彩比赛场次为暂停销售或延期时，重新抓取时不覆盖这两个状态，需要做变更时，人工进行维护
//                    if (resultPO.getMatchStatus() != Constants.NUM_10 || resultPO.getMatchStatus() != Constants.NUM_13) {
//                    	po.setId(resultPO.getId());
//                        sportAgainstInfoDaoMapper.update(po);
//                    }
//                } else {
//                    sportAgainstInfoDaoMapper.insert(po);
//                }
            }
            LOGGER.info("抓取竞技足球的赛事结果信息 ---------------- end");            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveWxMatchOddList() {
        if (wxFlag) {
            LOGGER.info("正在执行抓取，请勿重复执行");
            return;
        }
        wxFlag = true;
        long beginTime = System.currentTimeMillis();
        List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.FB.getName());

        List<SportDataFbSpPO> pos = new ArrayList<>();
        List<Callable<SportDataFbSpPO>> callList = new ArrayList<>();
        for (int i = 0; list != null && i < list.size(); i++) {

            if (i % 20 == 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            final SportAgainstInfoPO po = list.get(i);
            try {
                Callable<SportDataFbSpPO> callable = new Callable<SportDataFbSpPO>() {
                    @Override
                    public SportDataFbSpPO call() throws Exception {
                        return saveWxMatchOdd(po.getOfficialId(), po.getId());
                    }
                };
                callList.add(callable);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("crawler pool result error officialId is" + po.getOfficialId() + " sportAgainstInfoId " + po.getId() + "message:" + e.getMessage());
            }

        }
        List<Future<SportDataFbSpPO>> feature = null;
        try {
            feature = ThreadPoolManager.invokeAll("更新赛程历史SP值", callList);
            for (Future<SportDataFbSpPO> featurePO : feature) {
                try {
                    SportDataFbSpPO po = featurePO.get();
                    if (ObjectUtil.isBlank(po)) {
                        continue;
                    }
                    pos.add(po);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            wxFlag = false;
            e.printStackTrace();
        }


        //批量更新最新SP值
        //sportDataFbSpDaoMapper.saveNewestSpList(pos);

        for (SportDataFbSpPO po : pos) {
            sportDataFbSpDaoMapper.merge(po);
        }
        wxFlag = false;
        LOGGER.debug("update wx odds list Data is " + JsonUtil.objectToJson(pos));
        long endTime = System.currentTimeMillis();
        LOGGER.debug("update wx odds list success necessity for time ：" + ((endTime - beginTime) / 1000) + "S");
    }

    @Override
    public SportDataFbSpPO saveWxMatchOdd(String officialId, Long sportAgainstInfoId) {
        try {

            SportDataFbSpPO po = new SportDataFbSpPO();
            if (StringUtils.isBlank(officialId)) {
                return po;
            }

            String url = FOOTBALL_WX_MID_MATCH_ODD.replace("{0}", officialId) + "&_=" + new Date().getTime();
            String json = getMatchListJson(url);

            String data = StringUtils.stripEnd(StringUtils.stripStart(json, "initData("), SymbolConstants.PARENTHESES_RIGHT);

            if (data.equals("0")) {
                return null;
            }
            Map maps = JsonUtil.jsonToObject(data, Map.class);

            JSONObject dataObj = (JSONObject) maps.get("data");
            String id = dataObj.getString("id");
            if (StringUtils.isBlank(id)) {
                LOGGER.error("not data wx odds officialId is" + officialId);
                return po;
            }


            JSONObject hadObj = dataObj.getJSONObject("had");//获取胜平负的SP值
            if (ObjectUtil.isBlank(hadObj)) {
                hadObj = new JSONObject();
            }
            String goalline = dataObj.getString("goalline");//让球数
            if (!StringUtils.isBlank(goalline) && goalline.contains(".")) {
                goalline = StringUtils.stripEnd(goalline, ".00");
            }
            Short newestLetNum = NumberUtils.parseNumber(goalline, Short.class);
            JSONObject hhadObj = dataObj.getJSONObject("hhad");//获取让球胜平负的SP值
            JSONObject ttgObj = dataObj.getJSONObject("ttg");//获取进球数的SP值
            JSONObject hafuObj = dataObj.getJSONObject("hafu");//获取半全场胜负的SP值
            JSONObject crsObj = dataObj.getJSONObject("crs");//获取比分的SP值
            if (ObjectUtil.isBlank(hhadObj)) {
                hhadObj = new JSONObject();
            }
            if (ObjectUtil.isBlank(ttgObj)) {
                ttgObj = new JSONObject();
            }
            if (ObjectUtil.isBlank(hafuObj)) {
                hafuObj = new JSONObject();
            }
            if (ObjectUtil.isBlank(crsObj)) {
                crsObj = new JSONObject();
            }
            Float newestSpWin = hadObj.getFloat("h");
            Float newestSpDraw = hadObj.getFloat("d");
            Float newestSpFail = hadObj.getFloat("a");

            Float newestLetSpWin = hhadObj.getFloat("h");
            Float newestLetSpDraw = hhadObj.getFloat("d");
            Float newestLetSpFail = hhadObj.getFloat("a");

            Float newestSp0Goal = ttgObj.getFloat("s0");
            Float newestSp1Goal = ttgObj.getFloat("s1");
            Float newestSp2Goal = ttgObj.getFloat("s2");
            Float newestSp3Goal = ttgObj.getFloat("s3");
            Float newestSp4Goal = ttgObj.getFloat("s4");
            Float newestSp5Goal = ttgObj.getFloat("s5");
            Float newestSp6Goal = ttgObj.getFloat("s6");
            Float newestSp7Goal = ttgObj.getFloat("s7");

            Float newestSpWW = hafuObj.getFloat("hh");
            Float newestSpWD = hafuObj.getFloat("hd");
            Float newestSpWF = hafuObj.getFloat("ha");
            Float newestSpDW = hafuObj.getFloat("dh");
            Float newestSpDD = hafuObj.getFloat("dd");
            Float newestSpDF = hafuObj.getFloat("da");
            Float newestSpFW = hafuObj.getFloat("ah");
            Float newestSpFD = hafuObj.getFloat("ad");
            Float newestSpFF = hafuObj.getFloat("aa");

            Float newestSp10 = crsObj.getFloat("0100");
            Float newestSp20 = crsObj.getFloat("0200");
            Float newestSp21 = crsObj.getFloat("0201");
            Float newestSp30 = crsObj.getFloat("0300");
            Float newestSp31 = crsObj.getFloat("0301");
            Float newestSp32 = crsObj.getFloat("0302");
            Float newestSp40 = crsObj.getFloat("0400");
            Float newestSp41 = crsObj.getFloat("0401");
            Float newestSp42 = crsObj.getFloat("0402");
            Float newestSp50 = crsObj.getFloat("0500");
            Float newestSp51 = crsObj.getFloat("0501");
            Float newestSp52 = crsObj.getFloat("0502");
            Float newestSpWOther = crsObj.getFloat("-1-h");
            Float newestSp00 = crsObj.getFloat("0000");
            Float newestSp11 = crsObj.getFloat("0101");
            Float newestSp22 = crsObj.getFloat("0202");
            Float newestSp33 = crsObj.getFloat("0303");
            Float newestSpDOther = crsObj.getFloat("-1-d");
            Float newestSp01 = crsObj.getFloat("0001");
            Float newestSp02 = crsObj.getFloat("0002");
            Float newestSp12 = crsObj.getFloat("0102");
            Float newestSp03 = crsObj.getFloat("0003");
            Float newestSp13 = crsObj.getFloat("0103");
            Float newestSp23 = crsObj.getFloat("0203");
            Float newestSp04 = crsObj.getFloat("0004");
            Float newestSp14 = crsObj.getFloat("0104");
            Float newestSp24 = crsObj.getFloat("0204");
            Float newestSp05 = crsObj.getFloat("0005");
            Float newestSp15 = crsObj.getFloat("0105");
            Float newestSp25 = crsObj.getFloat("0205");
            Float newestSpFOther = crsObj.getFloat("-1-a");

            po.setSportAgainstInfoId(sportAgainstInfoId);

            po.setNewestLetNum(newestLetNum);
            po.setNewestLetSpWin(newestLetSpWin);
            po.setNewestLetSpDraw(newestLetSpDraw);
            po.setNewestLetSpFail(newestLetSpFail);

            po.setNewestSpWin(newestSpWin);
            po.setNewestSpDraw(newestSpDraw);
            po.setNewestSpFail(newestSpFail);

            po.setNewestSp0Goal(newestSp0Goal);
            po.setNewestSp1Goal(newestSp1Goal);
            po.setNewestSp2Goal(newestSp2Goal);
            po.setNewestSp3Goal(newestSp3Goal);
            po.setNewestSp4Goal(newestSp4Goal);
            po.setNewestSp5Goal(newestSp5Goal);
            po.setNewestSp6Goal(newestSp6Goal);
            po.setNewestSp7Goal(newestSp7Goal);

            po.setNewestSpWW(newestSpWW);
            po.setNewestSpWD(newestSpWD);
            po.setNewestSpWF(newestSpWF);
            po.setNewestSpDW(newestSpDW);
            po.setNewestSpDD(newestSpDD);
            po.setNewestSpDF(newestSpDF);
            po.setNewestSpFW(newestSpFW);
            po.setNewestSpFD(newestSpFD);
            po.setNewestSpFF(newestSpFF);

            po.setNewestSp10(newestSp10);
            po.setNewestSp20(newestSp20);
            po.setNewestSp21(newestSp21);
            po.setNewestSp30(newestSp30);
            po.setNewestSp31(newestSp31);
            po.setNewestSp32(newestSp32);
            po.setNewestSp40(newestSp40);
            po.setNewestSp41(newestSp41);
            po.setNewestSp42(newestSp42);
            po.setNewestSp50(newestSp50);
            po.setNewestSp51(newestSp51);
            po.setNewestSp52(newestSp52);
            po.setNewestSpWOther(newestSpWOther);
            po.setNewestSp00(newestSp00);
            po.setNewestSp11(newestSp11);
            po.setNewestSp22(newestSp22);
            po.setNewestSp33(newestSp33);
            po.setNewestSpDOther(newestSpDOther);
            po.setNewestSp01(newestSp01);
            po.setNewestSp02(newestSp02);
            po.setNewestSp12(newestSp12);
            po.setNewestSp03(newestSp03);
            po.setNewestSp13(newestSp13);
            po.setNewestSp23(newestSp23);
            po.setNewestSp04(newestSp04);
            po.setNewestSp14(newestSp14);
            po.setNewestSp24(newestSp24);
            po.setNewestSp05(newestSp05);
            po.setNewestSp15(newestSp15);
            po.setNewestSp25(newestSp25);
            po.setNewestSpFOther(newestSpFOther);
            return po;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("not data wx odds officialId is" + officialId + " error message " + e.getMessage());
        }
        return null;
    }

    /**
     * 同步一比分数据
     */
    @Override
    public void updateYbfData() {
        crawlerService.crawlerYbfJczqMatch();
    }

    /**
     * 获取过关方式
     *
     * @param obj
     * @return
     */
    private Short getStatus(JSONObject obj) {
        if (!ObjectUtil.isNull(obj)) {
            return SportLotteryUtil.getSaleType(obj.getString("single"));
        }
        return Constants.NUM_4;//未开售此玩法
    }

    @Override
    public int insertMatchOdds(List<SportMatchOddPO> pos) {
        //批量保存赔率变化
        return sportMatchOddDaoMapper.insert(pos);
    }

    @Override
    public int updateAvgOdds(List<JcAvgOddsBO> avgs) {
        return sportAgainstInfoDaoMapper.updateAvgOdds(avgs);
    }

    /**
     * 清除竞彩足球系统编号缓存
     */
    @Override
    public void clearJczqCacheBySystemCode() {
        List<SportAgainstInfoPO> pos = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.FB.getName());
        for (SportAgainstInfoPO po : pos) {
            resetSportCacheService.resetFbMatchListBySystemCode(po.getSystemCode());
        }
    }

    /**
     * 清除竞彩足球系统编号缓存
     */
    @Override
    public void clearJczqCacheByIssueCode() {
        List<SportAgainstInfoPO> pos = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.FB.getName());
        Set<String> set = new HashSet<>();
        for (SportAgainstInfoPO po : pos) {
            set.add(po.getIssueCode());
        }
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            resetSportCacheService.resetFbMatchListByIssueCode(iter.next());
        }
    }
}
