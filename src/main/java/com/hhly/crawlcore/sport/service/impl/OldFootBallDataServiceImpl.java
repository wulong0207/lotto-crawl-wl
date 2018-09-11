package com.hhly.crawlcore.sport.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.sport.common.OldMatchStatusEnum;
import com.hhly.crawlcore.sport.entity.OldLotteryIssue;
import com.hhly.crawlcore.sport.service.OldFootBallDataService;
import com.hhly.crawlcore.sport.service.ResetSportCacheService;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;

/**
 * @auth lgs on
 * @date 2017/2/7.
 * @desc 抓取老足彩数据service实现类
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
@Service
public class OldFootBallDataServiceImpl implements OldFootBallDataService {

    public static final Logger LOGGER = LoggerFactory.getLogger(OldFootBallDataServiceImpl.class);
    /**
     * 14场胜负彩/9场胜负彩标识
     */
    private final String WILO = "wilo";

    /**
     * 6场半全场
     */
    private final String HAFU = "hafu";

    /**
     * 4场进球
     */
    private final String GOAL = "goal";

    @Value("${old_fb_lottery_match}")
    private String OLD_FB_LOTTERY_MATCH;

    @Value("${old_fb_lottery_nums}")
    private String OLD_FB_LOTTERY_NUMS;

    /**
     * 老足彩下一期信息
     */
    private String OLD_LOTTERY_NEXT_ISSUE = "OLD_LOTTERY_NEXT_ISSUE_";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

    @Autowired
    private LotteryIssueDaoMapper lotteryIssueMapper;

    @Autowired
    private ResetSportCacheService resetSportCacheService;

    @Autowired
    private SportTeamInfoService sportTeamInfoService;

    @Autowired
    private SportMatchInfoService sportMatchInfoService;

    /**
     * 老足彩抓取对阵次数
     */
    private int cralwIndex = 0;

    @Override
    public void saveMatchByLotteryIssue(String lotteryIssue, String lotteryType, Integer lottery) {
        if (lotteryIssue == null) {
            lotteryIssue = "";
        }
        //获取彩期的销售时间 开售时间 停售时间 计奖时间
        JSONObject issueMap = getIssueTime(lotteryIssue, lotteryType);
        lotteryIssue = issueMap.getString("num"); //获取彩期 防止参数lotteryIssue为null
        try {
            String url = OLD_FB_LOTTERY_MATCH.replace("{0}", lotteryType).replace("{1}", lotteryIssue).replace("{2}", String.valueOf(new Date().getTime()));
            String json = HTTPUtil.transRequest(url);
            String data = StringUtils.stripEnd(StringUtils.stripStart(json, "getDataBack("), ");");
            JSONObject jsonObject = JsonUtil.jsonToObject(data, JSONObject.class);
            JSONObject resultObject = jsonObject.getJSONObject("result");
            Set<Map.Entry<String, Object>> sets = resultObject.entrySet();
            List<Map<String, Object>> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry : sets) {
                String key = entry.getKey();
                Map<String, Object> matchMap = (Map<String, Object>) entry.getValue();
                String league = MapUtils.getString(matchMap, "league");
                String homeName = MapUtils.getString(matchMap, "h_cn"); //主队
                String guestName = MapUtils.getString(matchMap, "a_cn");//客队
                String date = MapUtils.getString(matchMap, "date");
                String time = MapUtils.getString(matchMap, "time");

                String mid = MapUtils.getString(matchMap, "mid");
                
                //暂时没有使用胜平负的SP值
//                String winSp = MapUtils.getString(matchMap, "a");
//                String lostSp = MapUtils.getString(matchMap, "h");
//                String drawSp = MapUtils.getString(matchMap, "d");
                
                Integer result = MapUtils.getInteger(matchMap, "result");
                String full = MapUtils.getString(matchMap, "full");
                Map<String, Object> paramMap = new HashMap<>();

                if (!ObjectUtil.isNull(result)) {
                    paramMap.put("matchStatus", OldMatchStatusEnum.FINISH.getCode());
                } else {
                    paramMap.put("matchStatus", OldMatchStatusEnum.WAIT_MATCH.getCode());
                }


                paramMap.put("sortBy", key); //用于显示排序。老足彩非常重要
                paramMap.put("homeName", homeName);
                paramMap.put("guestName", guestName);
                paramMap.put("matchName", league);
                String dateTime = null;
                if (!StringUtil.isBlank(time)) {
                    dateTime = date + " " + time;
                } else {
                    dateTime = date;
                }
                paramMap.put("matchTime", dateTime);
                paramMap.put("officialId", mid);
                paramMap.put("result", result);
                paramMap.put("full", full);
                params.add(paramMap);
            }

            /*对list冒泡排序下*/
            Map<String, Object> temp = null;
            for (int i = params.size() - 1; i > 0; --i) {
                for (int j = 0; j < i; ++j) {
                    Map<String, Object> iMap = params.get(j);
                    Map<String, Object> jMap = params.get(j + 1);
                    Long iMapKeyVal = MapUtils.getLong(iMap, "sortBy");
                    Long jMapKeyVal = MapUtils.getLong(jMap, "sortBy");
                    if (iMapKeyVal.compareTo(jMapKeyVal) == 1) {
                        temp = params.get(j);
                        params.set(j, params.get(j + 1));
                        params.set(j + 1, temp);
                    }
                }
            }


            List<SportAgainstInfoPO> pos = new ArrayList<>();
            // 生成系统编号 并且保存数据库
            for (int i = 0; i < params.size(); i++) {
                temp = params.get(i);
                if (lotteryType.equals(HAFU)) { //当等于6场半全场会出现重复所以i++只取一条
                    i++;
                }

                Date startTime = DateUtil.convertStrToDate(MapUtils.getString(temp, "matchTime"), DateUtil.DEFAULT_FORMAT);
                if (startTime == null) {
                    startTime = DateUtil.convertStrToDate(MapUtils.getString(temp, "matchTime"), DateUtil.DATE_FORMAT);
                }

                String homeName = MapUtils.getString(temp, "homeName");
                String guestName = MapUtils.getString(temp, "guestName");
                String matchName = MapUtils.getString(temp, "matchName");
                String createBy = SportEnum.SportDataSource.JC_OFFICIAL.getName();
                Short matchType = SportEnum.MatchTypeEnum.FOOTBALL.getCode();

                //获取球队唯一md5值
                String homeTeamUnion = null;
                String guestTeamUnion = null;
                String matchUnion = null;
                try {
                    homeTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(homeName + matchType).getBytes("UTF-8"));
                    guestTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(guestName + matchType).getBytes("UTF-8"));
                    matchUnion = DigestUtils.md5DigestAsHex(String.valueOf(matchName + matchType).getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //获取球队信息
                SportTeamInfoPO homeTeam = new SportTeamInfoPO(matchType, homeName, homeTeamUnion, createBy, createBy);
                SportTeamInfoPO guestTeam = new SportTeamInfoPO(matchType, guestName, guestTeamUnion, createBy, createBy);
                SportMatchInfoPO matchInfo = new SportMatchInfoPO(matchName, matchType, matchUnion, createBy);

                homeTeam = sportTeamInfoService.findTeam(homeTeam);
                guestTeam = sportTeamInfoService.findTeam(guestTeam);
                matchInfo = sportMatchInfoService.findMatch(matchInfo);
                String systemCode = SportLotteryUtil.getOldLotterySystemCode(lotteryIssue, i + 1);
                
                Long id = sportAgainstInfoDaoMapper.findIdByCode(lottery, lotteryIssue, systemCode);
                
                SportAgainstInfoPO po = new SportAgainstInfoPO(id, lottery,
                        lotteryIssue,
                        matchName,
                        homeName,
                        guestName,
                        homeTeam.getId(),
                        guestTeam.getId(),
                        matchInfo.getId(),
                        SportLotteryUtil.getOldLotterySystemCode(lotteryIssue, i + 1),
                        MapUtils.getShort(temp, "matchStatus"),
                        MapUtils.getString(temp, "officialId"),
                        startTime,
                        DateUtil.convertStrToDate(issueMap.getString("end"), "yyyy/MM/dd HH:mm"),
                        MapUtils.getLong(temp, "sortBy"), createBy, createBy);
                pos.add(po);
            }

            for (SportAgainstInfoPO po : pos) {
            	sportAgainstInfoDaoMapper.merge(po);
//                int flag = sportAgainstInfoDaoMapper.updateByCode(po);
//                if (flag == 0) {
//                    sportAgainstInfoDaoMapper.insert(po);
//                }
            }

            LOGGER.debug("save old foot ball match list issue is:" + lotteryIssue + "lotteryType is:" + lotteryType + "Size is: " + pos.size() + "Data is: " + JsonUtil.objectToJson(pos));
            //存储彩期信息到redis
            String nextIssue = issueMap.getString("next");
            String key = OLD_LOTTERY_NEXT_ISSUE + lottery + "_" + issueMap.getString("last");
            OldLotteryIssue result = new OldLotteryIssue(issueMap);
            redisUtil.addObj(key, result, CacheConstants.ONE_DAY);

            if (cralwIndex == 5) {
                cralwIndex = 0;
                return;
            }
            if (!StringUtil.isBlank(nextIssue) && cralwIndex < 5) {
                cralwIndex++;
                Thread.sleep(3000);//抓取一次数据后暂停3秒在抓取下一期数据
                LOGGER.info("recursion saveMatchByLotteryIssue save old foot ball match list issue is:" + lotteryIssue + "lotteryType is:" + lotteryType + "Size is: " + pos.size() + "Data is: " + JsonUtil.objectToJson(pos));
                saveMatchByLotteryIssue(nextIssue, lotteryType, lottery);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("save old foot ball match list issue is:" + lotteryIssue + "lotteryType is:" + lotteryType + "Error message: " + e.getMessage());
        }
    }

    @Override
    public void saveMatchByLotteryIssueList(List<String> lotteryIssues, String lotteryType, Integer lottery) {
        for (String lotteryIssue : lotteryIssues) {
            saveMatchByLotteryIssue(lotteryIssue, lotteryType, lottery);
        }
    }

    @Override
    public JSONObject getIssueTime(String lotteryIssue, String type) {
        String json = null;
        try {
            String url = OLD_FB_LOTTERY_NUMS.replace("{0}", type).replace("{1}", lotteryIssue).replace("{2}", String.valueOf(new Date().getTime()));
            json = HTTPUtil.transRequest(url);
            if (json.equals("0")) {
                json = HTTPUtil.transRequest(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String data = StringUtils.stripEnd(StringUtils.stripStart(json, "getNumBack("), ");");
        JSONObject jsonObject = JsonUtil.jsonToObject(data, JSONObject.class);
        return jsonObject.getJSONObject("result");
    }

    @Override
    public void saveSfcMatch(String lotteryIssue, Integer lotteryCode) {
        this.saveMatchByLotteryIssue(lotteryIssue, WILO, lotteryCode);
        updateOldMatchInfo();
        resetSportCacheService.resetOldMatch(lotteryCode, lotteryIssue);

    }

    @Override
    public void saveSixHFMatch(String lotteryIssue, Integer lotteryCode) {
        this.saveMatchByLotteryIssue(lotteryIssue, HAFU, lotteryCode);
        resetSportCacheService.resetOldMatch(lotteryCode, lotteryIssue);
    }

    @Override
    public void saveGoalMatch(String lotteryIssue, Integer lotteryCode) {
        this.saveMatchByLotteryIssue(lotteryIssue, GOAL, lotteryCode);
        resetSportCacheService.resetOldMatch(lotteryCode, lotteryIssue);
    }

    /**
     * 更新同步老足彩赛事数据
     */
    @Override
    public void updateOldMatchInfo() {
        //查询需要同步的胜负彩对阵
        List<SportAgainstInfoPO> pos = sportAgainstInfoDaoMapper.findSfcMatch();
        if(ObjectUtil.isBlank(pos)){
        	LOGGER.info("查询需要同步的胜负彩对阵-----  没有需要同步的胜负彩对阵!!  size : " + pos.size());
        	return;
        }
        sportAgainstInfoDaoMapper.updateMatchInfoByOfficialId(pos);
        List<String> issueCodes = lotteryIssueMapper.getCurrentAndNextIssue(LotteryEnum.Lottery.SFC.getName());
        resetSportCacheService.resetOldMatch(LotteryEnum.Lottery.SFC.getName(), issueCodes);
        resetSportCacheService.resetOldMatch(LotteryEnum.Lottery.JQ4.getName(), issueCodes);
        resetSportCacheService.resetOldMatch(LotteryEnum.Lottery.ZC6.getName(), issueCodes);
    }

    /**
     * 查询下一期彩期信息
     *
     * @param lotteryIssue 彩期
     * @param lotteryCode  彩种编号
     * @return
     */
    @Override
    public OldLotteryIssue getNextIssueInfo(String lotteryIssue, Integer lotteryCode) {
        String key = OLD_LOTTERY_NEXT_ISSUE + lotteryCode + "_" + lotteryIssue;
        OldLotteryIssue result = null;
        result = redisUtil.getObj(key, new OldLotteryIssue());
        if (ObjectUtil.isBlank(result)) {
            String type = WILO;
            if (LotteryEnum.Lottery.ZC6.getName() == lotteryCode) {
                type = HAFU;
            } else if (LotteryEnum.Lottery.JQ4.getName() == lotteryCode) {
                type = GOAL;
            }
            JSONObject jsonObject = getIssueTime(lotteryIssue, type);
            String nextIssue = jsonObject.getString("next");
            if (!StringUtil.isBlank(nextIssue)) {
                JSONObject nextIssueObj = getIssueTime(nextIssue, type);
                result = new OldLotteryIssue(nextIssueObj);
                redisUtil.addObj(OLD_LOTTERY_NEXT_ISSUE + lotteryCode + "_" + lotteryIssue, result, CacheConstants.ONE_DAY);
            }
        }


        return result;
    }
}

