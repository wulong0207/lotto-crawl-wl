
package com.hhly.crawlcore.v2.plugin.lottery.sport.fivehundred;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.enums.FbLotteryEnum.FhFbLotteryEnum;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbGoalDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbHfWdfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbScoreDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbSpDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataFbWdfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportMatchSourceInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportStatusFbDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbGoalPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbHfWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbScorePO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataFbWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportStatusFbPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;
import com.hhly.crawlcore.v2.sport.entity.FhCacheInfo;
import com.hhly.crawlcore.v2.sport.entity.fb.FhFbMatchInfo;
import com.hhly.crawlcore.v2.sport.entity.fb.FhFbPlayStatus;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.JcMatchStatusEnum;
import com.hhly.skeleton.base.common.SportEnum.WfTypeEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class FhFbCrawl extends AbstractCrawl {
	
	private static final Logger log = LoggerFactory.getLogger(FhFbCrawl.class);

    private static final String ft_fb_queue = "wubai:spider:fbmixture:list";
    
    /**
     * 联赛信息地址
     */
    @Value("${league_match_500_url}")
    private String fh_get_league_info;
    
    @Value("${fh_fb_get_match_history_sp}")
    private String fh_fb_get_match_history_sp;
    
    @Autowired
    RedisUtil redisUtil;
    
    @Autowired
    private SportMatchSourceInfoDaoMapper sportMatchSourceInfoDaoMapper;
    
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
    
	public FhFbCrawl() {
	}
    
    
    @Autowired
	public FhFbCrawl(LotteryIssueDaoMapper lotteryIssueDaoMapper, SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper,
			SportTeamInfoService sportTeamInfoService, SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueDaoMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}

	@Override
	public void crawlHandle() throws Exception {
		getSaleMatchList();
//		getHistorySp();
	}

	@Override
	public void crawlMatch() throws Exception {
	}

	@Override
	public void dataHandle(String... params) throws Exception {
		getHistorySp();
	}
	
    private void getSaleMatchList() throws Exception {
    	long startTime = System.currentTimeMillis();
    	log.info("500网从python获取竞足数据开始  start : " + startTime);
    	
        Long size = redisUtil.length(ft_fb_queue);
        List<String> datalist = redisUtil.range(ft_fb_queue, 0, size.intValue());
        redisUtil.delObj(ft_fb_queue);
        
        if(ObjectUtil.isBlank(datalist))
        	return;
        //处理对阵数据
        Set<String> systemCodeSet = new HashSet<String>();
        
        Integer lotteryCode = getLotteryCode();
        
        Short type = getType();
        
		String createBy = SportEnum.SportDataSource.FIVEHUNDRED_OFFICIAL.getName();
		
        List<LotteryIssuePO> getIssueInfo = lotteryIssueDaoMapper.getIssueInfo(lotteryCode);
        
        log.info("500网获取的赛事集合 数量 : " + size);
        for (int i = (size.intValue() - 1); i >= 0; i--) {
            String value = datalist.get(i);
            if (!StringUtil.isBlank(value)) {
            	FhFbMatchInfo fbMatchInfo = JSONObject.parseObject(value, FhFbMatchInfo.class);
            	if(ObjectUtil.isBlank(fbMatchInfo) || DateUtil.compare(fbMatchInfo.getStartTime(), DateUtil.getNowDate()) == -1)
            		continue;
                
            	String issueCode = fbMatchInfo.getIssueCode();
            	
            	String systemCode = SportLotteryUtil.getSystemCode(fbMatchInfo.getOfficialMatchCode(), issueCode);
            	
            	String officialMatchCode = fbMatchInfo.getOfficialMatchCode();
            	
            	Date saleEndTime = SportLotteryUtil.getSaleEndTime(fbMatchInfo.getStartTime(), issueCode, getIssueInfo); // 销售截止时间
            	
                if(systemCodeSet.contains(systemCode)) {
                	continue;
                }
                
        		SportAgainstInfoPO po = sportAgainstInfoDaoMapper.findByCode(lotteryCode, issueCode, systemCode);
                
        		if(!ObjectUtil.isBlank(po)){
        			if(JcMatchStatusEnum.isUpdate(po.getMatchStatus()))
        				continue;
        			po.setStartTime(fbMatchInfo.getStartTime());
        			po.setSaleEndTime(saleEndTime);
        			po.setMatchStatus(fbMatchInfo.getStatus());
        			po.setModifyBy(createBy);
        		}else{
            		SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(fbMatchInfo.getHomeName(), fbMatchInfo.getHomeAbbrName(), type, fbMatchInfo.getHomeId(), fivehundredSource);

            		SportTeamSourceInfoPO guestSourcePO = new SportTeamSourceInfoPO(fbMatchInfo.getAwayName(), fbMatchInfo.getAwayAbbrName(), type, fbMatchInfo.getAwayId(),
            				fivehundredSource);

            		SportMatchSourceInfoPO matchSourcePO = new SportMatchSourceInfoPO(fbMatchInfo.getLeagueName(), fbMatchInfo.getLeagueAbbrName(), type, fbMatchInfo.getLeagueId(),
            				fivehundredSource);
            		
                    po = new SportAgainstInfoPO(null, lotteryCode, issueCode, null, null, null, null, 
                    		null, null, officialMatchCode, systemCode, fbMatchInfo.getStatus(), null, fbMatchInfo.getStartTime(), 
                    		saleEndTime, createBy, createBy, fbMatchInfo.getSaleDate());
                    
            		SportTeamInfoPO homeTeamPO = getTeamInfo(homeSourcePO);
            		if(!ObjectUtil.isBlank(homeTeamPO)){
            			po.setHomeTeamId(homeTeamPO.getId());
            			po.setHomeName(homeTeamPO.getTeamFullName());
            		}else{
            			po.setHomeName(fbMatchInfo.getHomeAbbrName());
            		}
            		SportTeamInfoPO awayTeamPO = getTeamInfo(guestSourcePO);
            		if(!ObjectUtil.isBlank(awayTeamPO)){
            			po.setVisitiTeamId(awayTeamPO.getId());
            			po.setVisitiName(awayTeamPO.getTeamFullName());
            		}else{
            			po.setVisitiName(fbMatchInfo.getAwayAbbrName());
            		}
            		SportMatchInfoPO leaguePO = getMatchInfo(matchSourcePO);
            		if(!ObjectUtil.isBlank(leaguePO)){
            			po.setSportMatchInfoId(leaguePO.getId());
            			po.setMatchName(leaguePO.getMatchFullName());
            		}else{
            			po.setMatchName(fbMatchInfo.getLeagueAbbrName());
            		}        			
        		}

                sportAgainstInfoDaoMapper.merge(po);
                Long id = po.getId();
                
                //处理最新的SP值
                SportDataFbSpPO sportDataFbSpPO = JSONObject.parseObject(JSONObject.toJSONString(fbMatchInfo.getNewSp()), SportDataFbSpPO.class);
                sportDataFbSpPO.setSportAgainstInfoId(id);
                
                sportDataFbSpDaoMapper.merge(sportDataFbSpPO);

                //处理子玩法状态
                //处理销售状态
                FhFbPlayStatus fhFbPlayStatus = fbMatchInfo.getPlayStatus();
                SportStatusFbPO sportStatusFbPO = new SportStatusFbPO(id, SportLotteryUtil.getSaleType(fhFbPlayStatus.getStatusWdf()), 
                		SportLotteryUtil.getSaleType(fhFbPlayStatus.getStatusLetWdf()), SportLotteryUtil.getSaleType(fhFbPlayStatus.getStatusGoal()), 
                		SportLotteryUtil.getSaleType(fhFbPlayStatus.getStatusHfWdf()), SportLotteryUtil.getSaleType(fhFbPlayStatus.getStatusScore()), 
                		createBy);
                
                int num = sportStatusFBDaoMapper.selectCount(sportStatusFbPO);
                if (num == 0) {
                	sportStatusFBDaoMapper.insert(sportStatusFbPO);
                }
                systemCodeSet.add(systemCode);
                
                try {
                    //500网缓存的key
                    FhCacheInfo cacheInfo = new FhCacheInfo();
                    cacheInfo.setLotteryCode(lotteryCode);
                    cacheInfo.setIssueCode(issueCode);
                    cacheInfo.setSportAgainstInfoId(po.getId());
                    cacheInfo.setOfficialId(fbMatchInfo.getNewsId());
                    cacheInfo.setLetNum(sportDataFbSpPO.getNewestLetNum());
                    addMatchCacheByIssue(cacheInfo);
				} catch (Exception e) {
					log.error("500网的数据彩期数据添加到缓存异常 : " + e.getMessage());
				}
            }
        }
        log.info("共处理竞足赛事集合数据  : " + systemCodeSet.size());
        long endTime = System.currentTimeMillis();
    	log.info("500网从python获取竞足数据结束  end : " + (endTime - startTime));
    }
	
	/**
	 * 获取竞彩官网历史sp值
	 * 
	 * @date 2017年11月14日下午4:17:11
	 * @author cheng.chen
	 */
    private void getHistorySp() throws Exception {
    	long startTime = System.currentTimeMillis();
    	log.info("500网获取历史sp值  start : " + startTime);
        List<String> list = sportAgainstInfoDaoMapper.findSaleMatchIssueList(getLotteryCode());

        if (ObjectUtil.isBlank(list)) {
            log.info("通过彩种编码查询彩期集合数据为null");
            return;
        }
        Map<Long, FhCacheInfo> cacheInfoMap = new HashMap<Long, FhCacheInfo>();
        for (String issueCode : list) {
			String key = CacheConstants.C_CRAWL_500_FB_ISSUE + DateUtil.convertDateToStr(DateUtil.convertStrToDate(issueCode, DateUtil.DATE_FORMAT), DateUtil.FORMAT_YYMMDD);
			Map<Long, FhCacheInfo> map = redisUtil.getObj(key, new HashMap<Long,FhCacheInfo>());
			if(ObjectUtil.isBlank(map))
				continue;
			cacheInfoMap.putAll(map);
			
        }
        log.info("500网竞足查询历史数据集合  : " + cacheInfoMap.size());
		for (final FhCacheInfo cacheInfo : cacheInfoMap.values()) {
			
			//执行500网竞足历史sp值数据抓取
            ThreadPoolManager.execute("执行获取id : " + cacheInfo.getSportAgainstInfoId() + "历史sp值", new Runnable() {
				@Override
				public void run() {
					try {
						for (FhFbLotteryEnum fhFbLotteryEnum : FhFbLotteryEnum.values()) {
							getSingleHistorySp(cacheInfo, fhFbLotteryEnum);
							Thread.sleep(2000);
						}
					} catch (Exception e) {
						log.error("抓取对阵历史sp值 异常 id : " + cacheInfo.getSportAgainstInfoId() + ", message : " + e.getMessage());
					}
				}
			});		
		}
        long endTime = System.currentTimeMillis();
    	log.info("500网获取历史sp值   end : " + (endTime - startTime));
	}
	
	private void getSingleHistorySp(FhCacheInfo cacheInfo, FhFbLotteryEnum fhFbLotteryEnum) throws Exception {
        long startTime = System.currentTimeMillis();
    	log.info("500网获取单个赛事历史sp值   start : " + startTime);
		String url = fh_fb_get_match_history_sp.replace("{0}", cacheInfo.getOfficialId()).replace("{1}", fhFbLotteryEnum.getCode()) + System.currentTimeMillis();
		
	    String json = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
	       if(ObjectUtil.isBlank(json)){
	        log.info("获取单个对阵胜平负历史sp异常 sportAgainstInfoId : " + cacheInfo.getSportAgainstInfoId()  +  "没有返回值, json : " + json);
	        return;
	    }
		Long id = cacheInfo.getSportAgainstInfoId();
		String officialId = cacheInfo.getOfficialId();
		switch (fhFbLotteryEnum) {
		case WDF:
	        //通过赛事id查询历史sp值最后入库时间
			SportDataFbWdfPO wdfLastPO = sportDataFbWdfDaoMapper.findLast(id, WfTypeEnum.NOT_LET.getValue());
			wdfHistorySp(json, id, null, WfTypeEnum.NOT_LET.getValue(), wdfLastPO);
			break;
		case LET_WDF:
	        //通过赛事id查询历史sp值最后入库时间
			SportDataFbWdfPO letWdfLastPO = sportDataFbWdfDaoMapper.findLast(id, WfTypeEnum.LET.getValue());
			wdfHistorySp(json, id, cacheInfo.getLetNum(), WfTypeEnum.LET.getValue(), letWdfLastPO);
			break;			
		case GOAL:
			SportDataFbGoalPO goalLastPO = sportDataFbGoalDaoMapper.findLast(id);
			goalHistorySp(json, id, officialId, goalLastPO);
			break;				
		case HF_WDF:
			SportDataFbHfWdfPO hfWdfLastPO = sportDataFbHfWdfDaoMapper.findLast(id);
			hfWdfHistorySp(json, id, officialId, hfWdfLastPO);
			break;				
		case SCORE:
			SportDataFbScorePO scoreLastPO = sportDataFbScoreDaoMapper.findLast(id);
			scoreHistorySp(json, id, officialId, scoreLastPO);
			break;				
		}
        long endTime = System.currentTimeMillis();
    	log.info("500网获取单个赛事历史sp值   end : " + (endTime - startTime));        
	}
	
	private void wdfHistorySp(String json, Long sportAgainstInfoId, Short letNum, Short spType,  SportDataFbWdfPO lastPO) throws Exception {
		
        List<SportDataFbWdfPO> wdfPOs = new ArrayList<>();
        //字符串转换成 json数组
        JSONArray wdfArr = JSONArray.parseArray(json);
        for (int i = wdfArr.size() - 1; i >= 0; i--) {
        	JSONObject wdfJson = wdfArr.getJSONObject(i);
        	String time = wdfJson.getString("time");
            if (ObjectUtil.isBlank(time))
                continue;
        	Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATETIME_FORMAT_NO_SEC);
            if(!ObjectUtil.isBlank(lastPO) && !ObjectUtil.isBlank(lastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
            	if(DateUtil.compare(lastPO.getReleaseTime(), releaseTime) != -1)
            		continue;
        	Float spWin = wdfJson.getFloat("win");
    		Float spDraw = wdfJson.getFloat("draw");
    		Float spFail = wdfJson.getFloat("lost");
    		SportDataFbWdfPO wdfPO = new SportDataFbWdfPO(sportAgainstInfoId, letNum, spWin, spDraw, spFail, 
    				spType, releaseTime);
    		if(!ObjectUtil.isBlank(lastPO) && wdfPO.isEquals(lastPO))
    			continue;
    		wdfPOs.add(wdfPO);
    		lastPO = wdfPO;
		}
        if(!ObjectUtil.isBlank(wdfPOs))
        	sportDataFbWdfDaoMapper.insertBatch(wdfPOs);
	}
	
	private void goalHistorySp(String json, Long sportAgainstInfoId, String officialId, SportDataFbGoalPO lastPO){
		
        JSONObject spObj = JSONObject.parseObject(json);
        JSONObject goalSp = spObj.getJSONObject(officialId).getJSONObject("@attributes");
        
        if(ObjectUtil.isBlank(goalSp)){
        	log.info("获取单个对阵胜平负历史sp异常 officialId : " + officialId  +  "没有返回值, goalSp : " + goalSp.toJSONString());
        	return;
        }
    	String time = goalSp.getString("time");
        if (ObjectUtil.isBlank(time))
            return;
        Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATETIME_FORMAT_NO_SEC);
        if(!ObjectUtil.isBlank(lastPO) && !ObjectUtil.isBlank(lastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
        	if(DateUtil.compare(lastPO.getReleaseTime(), releaseTime) != -1)
        		return;
        
        Float sp0Goal = goalSp.getFloat("s0");
        Float sp1Goal = goalSp.getFloat("s1");
        Float sp2Goal = goalSp.getFloat("s2");
        Float sp3Goal = goalSp.getFloat("s3");
        Float sp4Goal = goalSp.getFloat("s4");
        Float sp5Goal = goalSp.getFloat("s5");
        Float sp6Goal = goalSp.getFloat("s6");
        Float sp7Goal = goalSp.getFloat("s7");
        
        SportDataFbGoalPO goalPO = new SportDataFbGoalPO(sportAgainstInfoId, sp0Goal, sp1Goal, sp2Goal, sp3Goal, sp4Goal, 
        			 sp5Goal, sp6Goal, sp7Goal, releaseTime);
		if(!ObjectUtil.isBlank(lastPO) && goalPO.isEquals(lastPO))
			return;
        sportDataFbGoalDaoMapper.insert(goalPO);
        	 
	}
	
	private void hfWdfHistorySp(String json, Long sportAgainstInfoId,  String officialId, SportDataFbHfWdfPO lastPO){
		
        JSONObject spObj = JSONObject.parseObject(json);
        JSONObject hfWdfSp = spObj.getJSONObject(officialId).getJSONObject("@attributes");
        
        if(ObjectUtil.isBlank(hfWdfSp)){
        	log.info("获取单个对阵胜平负历史sp异常 officialId : " + officialId  +  "没有返回值, goalSp : " + hfWdfSp.toJSONString());
        	return;
        }
    	String time = hfWdfSp.getString("time");
        if (ObjectUtil.isBlank(time))
            return;
        Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATETIME_FORMAT_NO_SEC);
        if(!ObjectUtil.isBlank(lastPO) && !ObjectUtil.isBlank(lastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
        	if(DateUtil.compare(lastPO.getReleaseTime(), releaseTime) != -1)
        		return;
        
        Float spWW = hfWdfSp.getFloat("aa");
        Float spWD = hfWdfSp.getFloat("ac");
        Float spWF = hfWdfSp.getFloat("ab");
        Float spDW = hfWdfSp.getFloat("ca");
        Float spDD = hfWdfSp.getFloat("cc");
        Float spDF = hfWdfSp.getFloat("cb");
        Float spFW = hfWdfSp.getFloat("ba");
        Float spFD = hfWdfSp.getFloat("bc");
        Float spFF = hfWdfSp.getFloat("bb");
        
        SportDataFbHfWdfPO hfWdfPO = new SportDataFbHfWdfPO(sportAgainstInfoId, spWW, spWD, spWF, spDW, spDD, spDF, spFW, spFD, spFF, releaseTime);
		if(!ObjectUtil.isBlank(lastPO) && hfWdfPO.isEquals(lastPO))
			return;       
        sportDataFbHfWdfDaoMapper.insert(hfWdfPO);
	}
	
	private void scoreHistorySp(String json, Long sportAgainstInfoId,  String officialId, SportDataFbScorePO lastPO){
		
        if(ObjectUtil.isBlank(json)){
        	log.info("获取单个对阵半全场历史sp异常 sportAgainstInfoId : " + sportAgainstInfoId  +  "没有返回值, json : " + json);
        	return;
        }
        JSONObject spObj = JSONObject.parseObject(json);
        JSONObject scoreSp = spObj.getJSONObject(officialId).getJSONObject("@attributes");
        
        if(ObjectUtil.isBlank(scoreSp)){
        	log.info("获取单个对阵胜平负历史sp异常 officialId : " + officialId  +  "没有返回值, goalSp : " + scoreSp.toJSONString());
        	return;
        }
    	String time = scoreSp.getString("time");
        if (ObjectUtil.isBlank(time))
            return;
        Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATETIME_FORMAT_NO_SEC);
        if(!ObjectUtil.isBlank(lastPO) && !ObjectUtil.isBlank(lastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
        	if(DateUtil.compare(lastPO.getReleaseTime(), releaseTime) != -1)
        		return;
        
        Float sp10 = scoreSp.getFloat("a10");
        Float sp20 = scoreSp.getFloat("a20");
        Float sp21 = scoreSp.getFloat("a21");
        Float sp30 = scoreSp.getFloat("a30");
        Float sp31 = scoreSp.getFloat("a31");
        Float sp32 = scoreSp.getFloat("a32");
        Float sp40 = scoreSp.getFloat("a40");
        Float sp41 = scoreSp.getFloat("a41");
        Float sp42 = scoreSp.getFloat("a42");
        Float sp50 = scoreSp.getFloat("a50");
        Float sp51 = scoreSp.getFloat("a51");
        Float sp52 = scoreSp.getFloat("a52");
        Float spWOther = scoreSp.getFloat("aother");
        Float sp00 = scoreSp.getFloat("c00");
        Float sp11 = scoreSp.getFloat("c11");
        Float sp22 = scoreSp.getFloat("c22");
        Float sp33 = scoreSp.getFloat("c33");
        Float spDOther = scoreSp.getFloat("cother");
        Float sp01 = scoreSp.getFloat("b10");
        Float sp02 = scoreSp.getFloat("b20");
        Float sp12 = scoreSp.getFloat("b21");
        Float sp03 = scoreSp.getFloat("b30");
        Float sp13 = scoreSp.getFloat("b31");
        Float sp23 = scoreSp.getFloat("b32");
        Float sp04 = scoreSp.getFloat("b40");
        Float sp14 = scoreSp.getFloat("b41");
        Float sp24 = scoreSp.getFloat("b42");
        Float sp05 = scoreSp.getFloat("b50");
        Float sp15 = scoreSp.getFloat("b51");
        Float sp25 = scoreSp.getFloat("b52");
        Float spFOther = scoreSp.getFloat("bother");
        
        SportDataFbScorePO scorePO = new SportDataFbScorePO(sportAgainstInfoId, sp10, sp20, sp21, sp30, sp31, sp32, sp40, sp41, sp42, sp50, sp51, sp52, spWOther, 
        		sp00, sp11, sp22, sp33, spDOther, sp01, sp02, sp12, sp03, sp13, sp23, sp04, sp14, sp24, sp05, sp15, sp25, spFOther, releaseTime);
		if(!ObjectUtil.isBlank(lastPO) && scorePO.isEquals(lastPO))
			return;           
        sportDataFbScoreDaoMapper.insert(scorePO);
	}	
	
	public void leagueCrawl() throws Exception {
		
		long startTime = System.currentTimeMillis();
        log.info("获取500网足球联赛信息  start");
        
		List<SportMatchSourceInfoPO> pos = new ArrayList<>();
        Document doc = ParseHtmlUtil.getDocument(fh_get_league_info);
        Elements leagueElements = ParseHtmlUtil.getSelect(doc, ".lallrace_main_wrap .lallrace_main .lallrace_main_list li div a");
        leagueElements.addAll(ParseHtmlUtil.getSelect(doc, ".lallrace_main_wrap .lallrace_main .lallrace_main_list li a"));
        leagueElements.addAll(ParseHtmlUtil.getSelect(doc, ".lrace_bei tr td"));
        
        log.info("500网足球联赛基础资料抓取: " + leagueElements.size());
        
        for (Element element : leagueElements) {
        	SportMatchSourceInfoPO paramPO = new SportMatchSourceInfoPO();
        	String url = element.attr("href");
        	String leagueId = getString(url, "[team|zuqiu-](\\d+)");
        	Long sourceId = leagueId == null ? null : Long.valueOf(leagueId);
            String fullName = element.attr("title") == null ? null : element.attr("title");
            String shortName = element.text();
            
            paramPO.setMatchName(fullName);
            paramPO.setMatchAbbrName(shortName);
            paramPO.setSourceId(sourceId);
            paramPO.setSource(fivehundredSource);
            SportMatchSourceInfoPO dataPO = sportMatchSourceInfoDaoMapper.find(paramPO);
            if(ObjectUtil.isBlank(dataPO))
            	pos.add(paramPO);
        }
        //批量插入
        if(!ObjectUtil.isBlank(pos))
        	sportMatchSourceInfoDaoMapper.insertBatch(pos);
        log.info("获取500网足球联赛信息  end" + (System.currentTimeMillis() - startTime));
	}

	private void addMatchCacheByIssue(FhCacheInfo fhCacheInfo){
		if(!ObjectUtil.isBlank(fhCacheInfo)){
			String key = CacheConstants.C_CRAWL_500_FB_ISSUE + fhCacheInfo.getIssueCode();
			 Map<Long, FhCacheInfo> map = redisUtil.getObj(key, new HashMap<Long,FhCacheInfo>());
			 if(ObjectUtil.isNull(map))
				 map = new HashMap<Long,FhCacheInfo>();
			 if(!map.containsKey(fhCacheInfo.getSportAgainstInfoId())){
				 map.put(fhCacheInfo.getSportAgainstInfoId(), fhCacheInfo);
				 redisUtil.addObj(key, map, CacheConstants.FOUR_HOURS);				 
			 }
		}
	}
	
	private String getString(String str, String regx) {
		String result = null;
		// 1.将正在表达式封装成对象Patten 类来实现
		Pattern pattern = Pattern.compile(regx);
		// 2.将字符串和正则表达式相关联
		Matcher matcher = pattern.matcher(str);
		// 3.String 对象中的matches 是否包含
		System.out.println(matcher.matches());
		// 查找符合规则的子串
		while (matcher.find()) {
			// 获取 字符串
			result = matcher.group(1);
		}
		return result;
	}
	
	
	/**
	 * 获取抓取彩种
	 * @return
	 * @throws Exception
	 * @date 2017年11月23日下午2:28:47
	 * @author cheng.chen
	 */
	protected Integer getLotteryCode() throws Exception {
		return fbLotteryCode;
	}
	
	/**
	 * 获取抓取类型
	 * @return
	 * @throws Exception
	 * @date 2017年11月23日下午2:28:36
	 * @author cheng.chen
	 */
	protected Short getType() throws Exception {
		return (short)SportEnum.MatchTypeEnum.FOOTBALL.getCode();
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.convertDateToStr(DateUtil.convertStrToDate("2017-12-12", DateUtil.DATE_FORMAT), DateUtil.FORMAT_YYMMDD));
		Short f = null;
		Short c = null;
		System.out.println(f.equals(c));
	}
}
