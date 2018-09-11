
package com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbGoalDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbHfWdfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbScoreDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbSpDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbWdfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportStatusFbDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbGoalPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbHfWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbScorePO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportStatusFbPO;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.crawlcore.v2.plugin.lottery.sport.AbstractSportCrawl;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.common.SportEnum.WfTypeEnum;
import com.hhly.skeleton.base.constants.MQConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.hhly.skeleton.base.mq.msg.JzSpMessageData;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.NumberFormatUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.sport.bo.JczqTrendSpBO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@Service
public class StFbCrawl extends AbstractSportCrawl {
	
	private static final Logger log  = LoggerFactory.getLogger(StFbCrawl.class);
	
    @Autowired
    private SportDataFbWdfDaoMapper sportDataFbWdfDaoMapper;

    @Autowired
    private SportDataFbGoalDaoMapper sportDataFbGoalDaoMapper;

    @Autowired
    private SportDataFbHfWdfDaoMapper sportDataFbHfWdfDaoMapper;

    @Autowired
    private SportDataFbScoreDaoMapper sportDataFbScoreDaoMapper;

    @Autowired
    private SportDataFbSpDaoMapper sportDataFbSpDaoMapper;
    
    @Autowired
    private SportStatusFbDaoMapper sportStatusFBDaoMapper;
	
	//竞彩官网微信获取赛事集合url
    @Value("${st_fb_get_all_match}")
	private String st_fb_get_all_match;
    
    //竞彩官网PC暂定赛事集合url
    @Value("${st_fb_interim_match}")
    public String st_fb_interim_match;
    
    //竞彩官网赛事状态和最新sp值url
    @Value("${st_fb_get_match_info}")
    public String st_fb_get_match_info;
    
    //竞彩官网赛事历史sp值集合url
    @Value("${st_fb_get_match_history_sp}")
    public String st_fb_get_match_history_sp;
    
	public StFbCrawl() {
	}
    
    @Autowired
	public StFbCrawl(LotteryIssueDaoMapper lotteryIssueMapper, SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper,
			SportTeamInfoService sportTeamInfoService, SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}
    
    
	
    @Override
	public void dataHandle(String... params) throws Exception {
    	getHistorySp();
	}

	@Override
    public void getSaleMatchList() throws Exception {
        String url = st_fb_get_match_info + "&_=" + System.currentTimeMillis();
        
        String json = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
        
        if (json.equals("0") || ObjectUtil.isBlank(json)) {
          	log.info("竞足官网销售中赛事数据抓取, 没有返回值, json : " + json);
            return;
        }
        
        String initData = StringUtils.stripEnd(StringUtils.stripStart(json, SymbolConstants.PARENTHESES_LEFT), SymbolConstants.PARENTHESES_RIGHT);
        JSONObject initJson = JSONObject.parseObject(initData);
        JSONObject dataJson = initJson.getJSONObject("data");
        
        Integer lotteryCode = getLotteryCode();
        if (ObjectUtil.isBlank(dataJson)) {
            return;
        }
        
        List<LotteryIssuePO> getIssueInfo = lotteryIssueDaoMapper.getIssueInfo(lotteryCode);
        
        for (Object valueJson : dataJson.values()) {
            JSONObject singleJson = JSON.parseObject(valueJson.toString());
            
            Long sportAgainstInfoId = MatchHandle(singleJson, getIssueInfo);
            
            //处理最新sp值
            JSONObject had = ObjectUtil.isBlank(singleJson.getJSONObject("had")) ? new JSONObject() : singleJson.getJSONObject("had");//胜平负SP值、包括子玩法状态
            JSONObject hhad = ObjectUtil.isBlank(singleJson.getJSONObject("hhad")) ? new JSONObject() : singleJson.getJSONObject("hhad");//让球胜平负SP值、包括子玩法状态
            JSONObject ttg = ObjectUtil.isBlank(singleJson.getJSONObject("ttg")) ? new JSONObject() : singleJson.getJSONObject("ttg");//总进球SP值、包括子玩法状态
            JSONObject hafu = ObjectUtil.isBlank(singleJson.getJSONObject("hafu")) ? new JSONObject() : singleJson.getJSONObject("hafu");//半全场SP值、包括子玩法状态
            JSONObject crs = ObjectUtil.isBlank(singleJson.getJSONObject("crs")) ? new JSONObject() : singleJson.getJSONObject("crs");//比分SP值、包括子玩法状态
            
            SportDataFbSpPO spPo = new SportDataFbSpPO();
            
            Float newestSpWin = had.getFloat("h");
            Float newestSpDraw = had.getFloat("d");
            Float newestSpFail = had.getFloat("a");

            Short newestLetNum = hhad.getShort("fixedodds");
            Float newestLetSpWin = hhad.getFloat("h");
            Float newestLetSpDraw = hhad.getFloat("d");
            Float newestLetSpFail = hhad.getFloat("a");

            Float newestSp0Goal = ttg.getFloat("s0");
            Float newestSp1Goal = ttg.getFloat("s1");
            Float newestSp2Goal = ttg.getFloat("s2");
            Float newestSp3Goal = ttg.getFloat("s3");
            Float newestSp4Goal = ttg.getFloat("s4");
            Float newestSp5Goal = ttg.getFloat("s5");
            Float newestSp6Goal = ttg.getFloat("s6");
            Float newestSp7Goal = ttg.getFloat("s7");

            Float newestSpWW = hafu.getFloat("hh");
            Float newestSpWD = hafu.getFloat("hd");
            Float newestSpWF = hafu.getFloat("ha");
            Float newestSpDW = hafu.getFloat("dh");
            Float newestSpDD = hafu.getFloat("dd");
            Float newestSpDF = hafu.getFloat("da");
            Float newestSpFW = hafu.getFloat("ah");
            Float newestSpFD = hafu.getFloat("ad");
            Float newestSpFF = hafu.getFloat("aa");

            Float newestSp10 = crs.getFloat("0100");
            Float newestSp20 = crs.getFloat("0200");
            Float newestSp21 = crs.getFloat("0201");
            Float newestSp30 = crs.getFloat("0300");
            Float newestSp31 = crs.getFloat("0301");
            Float newestSp32 = crs.getFloat("0302");
            Float newestSp40 = crs.getFloat("0400");
            Float newestSp41 = crs.getFloat("0401");
            Float newestSp42 = crs.getFloat("0402");
            Float newestSp50 = crs.getFloat("0500");
            Float newestSp51 = crs.getFloat("0501");
            Float newestSp52 = crs.getFloat("0502");
            Float newestSpWOther = crs.getFloat("-1-h");
            Float newestSp00 = crs.getFloat("0000");
            Float newestSp11 = crs.getFloat("0101");
            Float newestSp22 = crs.getFloat("0202");
            Float newestSp33 = crs.getFloat("0303");
            Float newestSpDOther = crs.getFloat("-1-d");
            Float newestSp01 = crs.getFloat("0001");
            Float newestSp02 = crs.getFloat("0002");
            Float newestSp12 = crs.getFloat("0102");
            Float newestSp03 = crs.getFloat("0003");
            Float newestSp13 = crs.getFloat("0103");
            Float newestSp23 = crs.getFloat("0203");
            Float newestSp04 = crs.getFloat("0004");
            Float newestSp14 = crs.getFloat("0104");
            Float newestSp24 = crs.getFloat("0204");
            Float newestSp05 = crs.getFloat("0005");
            Float newestSp15 = crs.getFloat("0105");
            Float newestSp25 = crs.getFloat("0205");
            Float newestSpFOther = crs.getFloat("-1-a");

            spPo.setSportAgainstInfoId(sportAgainstInfoId);

            spPo.setNewestLetNum(newestLetNum);
            spPo.setNewestLetSpWin(newestLetSpWin);
            spPo.setNewestLetSpDraw(newestLetSpDraw);
            spPo.setNewestLetSpFail(newestLetSpFail);

            spPo.setNewestSpWin(newestSpWin);
            spPo.setNewestSpDraw(newestSpDraw);
            spPo.setNewestSpFail(newestSpFail);

            spPo.setNewestSp0Goal(newestSp0Goal);
            spPo.setNewestSp1Goal(newestSp1Goal);
            spPo.setNewestSp2Goal(newestSp2Goal);
            spPo.setNewestSp3Goal(newestSp3Goal);
            spPo.setNewestSp4Goal(newestSp4Goal);
            spPo.setNewestSp5Goal(newestSp5Goal);
            spPo.setNewestSp6Goal(newestSp6Goal);
            spPo.setNewestSp7Goal(newestSp7Goal);

            spPo.setNewestSpWW(newestSpWW);
            spPo.setNewestSpWD(newestSpWD);
            spPo.setNewestSpWF(newestSpWF);
            spPo.setNewestSpDW(newestSpDW);
            spPo.setNewestSpDD(newestSpDD);
            spPo.setNewestSpDF(newestSpDF);
            spPo.setNewestSpFW(newestSpFW);
            spPo.setNewestSpFD(newestSpFD);
            spPo.setNewestSpFF(newestSpFF);

            spPo.setNewestSp10(newestSp10);
            spPo.setNewestSp20(newestSp20);
            spPo.setNewestSp21(newestSp21);
            spPo.setNewestSp30(newestSp30);
            spPo.setNewestSp31(newestSp31);
            spPo.setNewestSp32(newestSp32);
            spPo.setNewestSp40(newestSp40);
            spPo.setNewestSp41(newestSp41);
            spPo.setNewestSp42(newestSp42);
            spPo.setNewestSp50(newestSp50);
            spPo.setNewestSp51(newestSp51);
            spPo.setNewestSp52(newestSp52);
            spPo.setNewestSpWOther(newestSpWOther);
            spPo.setNewestSp00(newestSp00);
            spPo.setNewestSp11(newestSp11);
            spPo.setNewestSp22(newestSp22);
            spPo.setNewestSp33(newestSp33);
            spPo.setNewestSpDOther(newestSpDOther);
            spPo.setNewestSp01(newestSp01);
            spPo.setNewestSp02(newestSp02);
            spPo.setNewestSp12(newestSp12);
            spPo.setNewestSp03(newestSp03);
            spPo.setNewestSp13(newestSp13);
            spPo.setNewestSp23(newestSp23);
            spPo.setNewestSp04(newestSp04);
            spPo.setNewestSp14(newestSp14);
            spPo.setNewestSp24(newestSp24);
            spPo.setNewestSp05(newestSp05);
            spPo.setNewestSp15(newestSp15);
            spPo.setNewestSp25(newestSp25);
            spPo.setNewestSpFOther(newestSpFOther);

            sportDataFbSpDaoMapper.merge(spPo);
            
            //处理销售状态
            SportStatusFbPO sportStatusFbPO = new SportStatusFbPO(sportAgainstInfoId, SportLotteryUtil.getSaleType(had.getString("single")), 
            		SportLotteryUtil.getSaleType(hhad.getString("single")), SportLotteryUtil.getSaleType(ttg.getString("single")), 
            		SportLotteryUtil.getSaleType(hafu.getString("single")), SportLotteryUtil.getSaleType(crs.getString("single")), 
            		SportDataSource.JC_OFFICIAL.getName());
            
            int row = sportStatusFBDaoMapper.selectCount(sportStatusFbPO);
            if (row < 1) {
            	sportStatusFBDaoMapper.insert(sportStatusFbPO);
            }
        }

    }	
	
    @Override
	protected String getAllMatchUrl() throws Exception {
		return st_fb_get_all_match + "&_=" + System.currentTimeMillis();
	}

	@Override
	protected String getInterimMatchUrl() throws Exception {
		return st_fb_interim_match + "?_=" + System.currentTimeMillis();
	}

	@Override
	protected Integer getLotteryCode() throws Exception {
		return fbLotteryCode;
	}
	
	@Override
	protected Short getType() throws Exception {
		return (short)SportEnum.MatchTypeEnum.FOOTBALL.getCode();
	}

	@Override
    protected void getSingleHistorySp(String officialId, Long sportAgainstInfoId) {
		
        if (sportAgainstInfoId == 0 || StringUtils.isBlank(officialId)) {
            return;
        }
        //让球胜平负SP
//        List<SportDataFbWdfPO> letWdfPos = new ArrayList<>();
        //胜平负SP
        List<SportDataFbWdfPO> wdfPOs = new ArrayList<>();
        //进球数SP
        List<SportDataFbGoalPO> goalPOs = new ArrayList<>();
        //比分SP
        List<SportDataFbScorePO> scorePOs = new ArrayList<>();
        //半全场胜平负SP
        List<SportDataFbHfWdfPO> hfWdfPOs = new ArrayList<>();
        try {
        	log.info("获取单个对阵 json 历史sp start : sportAgainstInfoId : " + sportAgainstInfoId);
        	
            String url = st_fb_get_match_history_sp.replace("{0}", officialId)+ "&_=" + System.currentTimeMillis();
            
            String json = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
            
            if(ObjectUtil.isBlank(json)){
            	log.info("获取单个对阵历史sp异常  sportAgainstInfoId : " + sportAgainstInfoId + "没有返回值, json : " + json);
            	return;
            }            
            String initData = StringUtils.stripEnd(StringUtils.stripStart(json, SymbolConstants.PARENTHESES_LEFT), SymbolConstants.PARENTHESES_RIGHT);

            JSONObject initJson = JsonUtil.jsonToObject(initData, JSONObject.class);

            JSONObject statusJson = initJson.getJSONObject("status");
    		int code = statusJson.getInteger("code");
    		String message = statusJson.getString("message");
    		if (code != 0 && !message.equals("")) {
    			throw new ServiceRuntimeException("获取官网www.sporttery.cn的竞彩足球历史sp值异常!!!");
    		}

    		JSONObject listJson = initJson.getJSONObject("result").getJSONObject("odds_list");


            JSONArray wdfArray = new JSONArray();
            JSONArray letWdfArray = new JSONArray();
            JSONArray hfWdfArray = new JSONArray();
            JSONArray goalArray = new JSONArray();
            JSONArray scoreArray = new JSONArray();

            try {
                wdfArray = listJson.getJSONObject("had").getJSONArray("odds");
            } catch (Exception e) {
            	log.error("解析竞足胜平负历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
                letWdfArray = listJson.getJSONObject("hhad").getJSONArray("odds");
            } catch (Exception e) {
            	log.error("解析竞足让球胜平负历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
                hfWdfArray = listJson.getJSONObject("hafu").getJSONArray("odds");
            } catch (Exception e) {
            	log.error("解析竞足半全场胜负历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
                goalArray = listJson.getJSONObject("ttg").getJSONArray("odds");
            } catch (Exception e) {
            	log.error("解析竞足总进球胜负历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
                scoreArray = listJson.getJSONObject("crs").getJSONArray("odds");
            } catch (Exception e) {
            	log.error("解析竞足比分胜负历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            //解析胜平负
            SportDataFbWdfPO wdfLastPO = sportDataFbWdfDaoMapper.findLast(sportAgainstInfoId, WfTypeEnum.NOT_LET.getValue());
            for (int i = 0; i < wdfArray.size(); i++) {
                JSONObject wdfObject = wdfArray.getJSONObject(i);
                Float winSp = wdfObject.getFloat("h");
                Float drawSp = wdfObject.getFloat("d");
                Float lostSp = wdfObject.getFloat("a");
                String time = wdfObject.getString("date") + " " + wdfObject.getString("time");
                if (ObjectUtil.isBlank(time))
                    continue;
                Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(wdfLastPO) && !ObjectUtil.isBlank(wdfLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(wdfLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;                
                SportDataFbWdfPO wdfPO = new SportDataFbWdfPO(sportAgainstInfoId, null, winSp, drawSp, lostSp, WfTypeEnum.NOT_LET.getValue(), releaseTime);
        		if(!ObjectUtil.isBlank(wdfLastPO) && wdfPO.isEquals(wdfLastPO))
        			continue;
        		wdfPOs.add(wdfPO);
        		wdfLastPO = wdfPO;                
            }
            
            //解析让球胜平负
            SportDataFbWdfPO letWdfLastPO = sportDataFbWdfDaoMapper.findLast(sportAgainstInfoId, WfTypeEnum.LET.getValue());
            Short letNum = listJson.getJSONObject("hhad").getShort("goalline");
            for (int i = 0; i < letWdfArray.size(); i++) {
                JSONObject lefWdfObject = letWdfArray.getJSONObject(i);
                
                String time = lefWdfObject.getString("date") + " " + lefWdfObject.getString("time");
                if (ObjectUtil.isBlank(time))
                    continue;
                Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(letWdfLastPO) && !ObjectUtil.isBlank(letWdfLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(letWdfLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;  

                Float winSp = lefWdfObject.getFloat("h");
                Float drawSp = lefWdfObject.getFloat("d");
                Float lostSp = lefWdfObject.getFloat("a");
               
                SportDataFbWdfPO lefWdfPO = new SportDataFbWdfPO(sportAgainstInfoId, letNum, winSp,
                        drawSp, lostSp, WfTypeEnum.LET.getValue(), releaseTime);
        		if(!ObjectUtil.isBlank(letWdfLastPO) && lefWdfPO.isEquals(letWdfLastPO))
        			continue;                
                wdfPOs.add(lefWdfPO);
                letWdfLastPO = lefWdfPO;
            }            

            //解析进球数
            SportDataFbGoalPO goalLastPO = sportDataFbGoalDaoMapper.findLast(sportAgainstInfoId);
            for (int i = 0; i < goalArray.size(); i++) {
                JSONObject goalObject = goalArray.getJSONObject(i);
                
                String time = goalObject.getString("date") + " " + goalObject.getString("time");
                if (ObjectUtil.isBlank(time))
                    continue;
                
                Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(goalLastPO) && !ObjectUtil.isBlank(goalLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(goalLastPO.getReleaseTime(), releaseTime) != -1)
                		continue; 
                
                Float sp0Goal = goalObject.getFloat("s0");
                Float sp1Goal = goalObject.getFloat("s1");
                Float sp2Goal = goalObject.getFloat("s2");
                Float sp3Goal = goalObject.getFloat("s3");
                Float sp4Goal = goalObject.getFloat("s4");
                Float sp5Goal = goalObject.getFloat("s5");
                Float sp6Goal = goalObject.getFloat("s6");
                Float sp7Goal = goalObject.getFloat("s7");
                  
                SportDataFbGoalPO goalPO = new SportDataFbGoalPO(sportAgainstInfoId, sp0Goal, sp1Goal, sp2Goal, sp3Goal, sp4Goal, sp5Goal, sp6Goal, sp7Goal, releaseTime);
        		if(!ObjectUtil.isBlank(goalLastPO) && goalPO.isEquals(goalLastPO))
        			continue;   
                goalPOs.add(goalPO);
                goalLastPO = goalPO;
            }

            //解析半全场胜平负
            SportDataFbHfWdfPO hfWdfLastPO = sportDataFbHfWdfDaoMapper.findLast(sportAgainstInfoId);
            for (int i = 0; i < hfWdfArray.size(); i++) {
                JSONObject hfWdfObject = hfWdfArray.getJSONObject(i);

                String time = hfWdfObject.getString("date") + " " + hfWdfObject.getString("time");
                if (ObjectUtil.isBlank(time))
                    continue;   
                
                Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(hfWdfLastPO) && !ObjectUtil.isBlank(hfWdfLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(hfWdfLastPO.getReleaseTime(), releaseTime) != -1)
                		continue; 
                
                Float spWW = hfWdfObject.getFloat("hh");
                Float spWD = hfWdfObject.getFloat("hd");
                Float spWF = hfWdfObject.getFloat("ha");
                Float spDW = hfWdfObject.getFloat("dh");
                Float spDD = hfWdfObject.getFloat("dd");
                Float spDF = hfWdfObject.getFloat("da");
                Float spFW = hfWdfObject.getFloat("ah");
                Float spFD = hfWdfObject.getFloat("ad");
                Float spFF = hfWdfObject.getFloat("aa");

                SportDataFbHfWdfPO hfWdfPO = new SportDataFbHfWdfPO(sportAgainstInfoId, spWW, spWD, spWF, spDW, spDD, spDF, spFW, spFD, spFF, releaseTime);
        		if(!ObjectUtil.isBlank(hfWdfLastPO) && hfWdfPO.isEquals(hfWdfLastPO))
        			continue;
                hfWdfPOs.add(hfWdfPO);
                hfWdfLastPO = hfWdfPO;
            }


            //解析比分SP
            SportDataFbScorePO scoreLastPO = sportDataFbScoreDaoMapper.findLast(sportAgainstInfoId);
            for (int i = 0; i < scoreArray.size(); i++) {
                JSONObject scoreObject = scoreArray.getJSONObject(i);

                String time = scoreObject.getString("date") + " " + scoreObject.getString("time");

                if (ObjectUtil.isBlank(time))
                    continue;   
                
                Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(scoreLastPO) && !ObjectUtil.isBlank(scoreLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(scoreLastPO.getReleaseTime(), releaseTime) != -1)
                		continue; 
                
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

                SportDataFbScorePO scorePO = new SportDataFbScorePO(sportAgainstInfoId, sp10, sp20, sp21, sp30, sp31, sp32, sp40, sp41, sp42, sp50, sp51, sp52, spWOther,
                        sp00, sp11, sp22, sp33, spDOther,
                        sp01, sp02, sp12, sp03, sp13, sp23, sp04, sp14, sp24, sp05, sp15, sp25, spFOther,
                        releaseTime);
        		if(!ObjectUtil.isBlank(scoreLastPO) && scorePO.isEquals(scoreLastPO))
        			continue;                
                scorePOs.add(scorePO);
                scoreLastPO = scorePO;
            }
            
            if(!ObjectUtil.isBlank(wdfPOs))
            	sportDataFbWdfDaoMapper.insertBatch(wdfPOs);
            
            if(!ObjectUtil.isBlank(goalPOs))
            	sportDataFbGoalDaoMapper.insertBatch(goalPOs);
            
            if(!ObjectUtil.isBlank(hfWdfPOs))
            	sportDataFbHfWdfDaoMapper.insertBatch(hfWdfPOs);
            
            if(!ObjectUtil.isBlank(scorePOs))
            	sportDataFbScoreDaoMapper.insertBatch(scorePOs);

            if(!ObjectUtil.isBlank(wdfPOs)){
                //SP值变化 存数组格式用于推送给前端 格式为 [w,d,f,let_w,let_d,let_f]
            	pushSp(sportAgainstInfoId, wdfLastPO, letWdfLastPO);
            }
            log.info("获取单个对阵 json 历史sp end : sportAgainstInfoId : " + sportAgainstInfoId);
        } catch (Exception e) {
            log.error("获取单个对阵历史sp异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
        }
    }
	
	private void pushSp(Long sportAgainstInfoId, SportDataFbWdfPO wdfLastPO, SportDataFbWdfPO letWdfLastPO){
		try {
	        String[] spTrend = new String[7];
	        if(!ObjectUtil.isBlank(wdfLastPO)){
	        	spTrend[0] = NumberFormatUtil.format(wdfLastPO.getSpWin());
	        	spTrend[1] = NumberFormatUtil.format(wdfLastPO.getSpDraw());
	        	spTrend[2] = NumberFormatUtil.format(wdfLastPO.getSpFail());
	        }else{
	        	spTrend[0] = "";
	        	spTrend[1] = "";
	        	spTrend[2] = "";
	        }
	        
	        if(!ObjectUtil.isBlank(letWdfLastPO)){
	        	spTrend[3] = NumberFormatUtil.format(letWdfLastPO.getLetNum(), NumberFormatUtil.DEFAULT);
	            spTrend[4] = NumberFormatUtil.format(letWdfLastPO.getSpWin());
	            spTrend[5] = NumberFormatUtil.format(letWdfLastPO.getSpDraw());
	            spTrend[6] = NumberFormatUtil.format(letWdfLastPO.getSpFail());
	        }else{
	        	spTrend[3] = "";
	        	spTrend[4] = "";
	        	spTrend[5] = "";
	        	spTrend[6] = "";
	        }
	        JczqTrendSpBO spTrendBO = new JczqTrendSpBO(sportAgainstInfoId, spTrend);
	        List<JczqTrendSpBO> list = new ArrayList<>();
	        list.add(spTrendBO);
	        JzSpMessageData jzSpMessageData = new JzSpMessageData();
	        jzSpMessageData.setList(list);
	        mqUtils.sendSpMessage(MQConstants.FOOTBALL_SP_RABBITMQ_NAME, jzSpMessageData);
	        log.info("竞足sp值变化推送  " +MQConstants.FOOTBALL_SP_RABBITMQ_NAME + "jlSpMessageData : " + JSONObject.toJSONString(jzSpMessageData));			
		} catch (Exception e) {
			log.error("竞足sp值推送 异常 : " + e.getMessage());
		}
	}
}
