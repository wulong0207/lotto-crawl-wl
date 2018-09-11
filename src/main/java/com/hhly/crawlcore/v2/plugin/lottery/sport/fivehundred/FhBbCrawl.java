
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.enums.BbLotteryEnum.FhBbLotteryEnum;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.base.util.RedisUtil;
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
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportStatusBbPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;
import com.hhly.crawlcore.v2.sport.entity.FhCacheInfo;
import com.hhly.crawlcore.v2.sport.entity.bb.FhBbMatchInfo;
import com.hhly.crawlcore.v2.sport.entity.bb.FhBbPlayStatus;
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
public class FhBbCrawl extends AbstractCrawl {
	
	private static final Logger log = LoggerFactory.getLogger(FhBbCrawl.class);

    private static final String ft_bb_queue = "wubai:spider:bbmix:list";
	
    @Value("${fh_bb_get_match_hash}")
    private String fh_bb_get_match_hash;
    
    @Value("${fh_bb_get_match_history_sp}")
    private String fh_bb_get_match_history_sp;
    
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	SportDataBbWfDaoMapper sportDataBbWfDaoMapper;
	
	@Autowired
	SportDataBbBssDaoMapper sportDataBbBssDaoMapper;
	
	@Autowired
	SportDataBbWsDaoMapper sportDataBbWsDaoMapper;
	
	@Autowired
	SportDataBbSpDaoMapper sportDataBbSpDaoMapper;
	
	@Autowired
	SportStatusBbDaoMapper sportStatusBBDaoMapper;
	
	public FhBbCrawl() {
	}

	@Autowired
	public FhBbCrawl(LotteryIssueDaoMapper lotteryIssueDaoMapper, SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper,
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
    	
        Long size = redisUtil.length(ft_bb_queue);
        List<String> datalist = redisUtil.range(ft_bb_queue, 0, size.intValue());
        redisUtil.delObj(ft_bb_queue);
        
        if(ObjectUtil.isBlank(datalist))
        	return;
        //处理对阵数据
        Set<String> systemCodeSet = new HashSet<String>();
        
        Integer lotteryCode = getLotteryCode();
        
        Short type = getType();
        
		String createBy = SportEnum.SportDataSource.FIVEHUNDRED_OFFICIAL.getName();
		
        List<LotteryIssuePO> getIssueInfo = lotteryIssueDaoMapper.getIssueInfo(lotteryCode);
        
        for (int i = datalist.size() - 1; i >= 0; i--) {
            String value = datalist.get(i);
            if (!StringUtil.isBlank(value)) {
            	FhBbMatchInfo bbMatchInfo = JSONObject.parseObject(value, FhBbMatchInfo.class);
            	if(ObjectUtil.isBlank(bbMatchInfo) || DateUtil.compare(bbMatchInfo.getStartTime(), DateUtil.getNowDate()) == -1)
            		continue;
                
            	String issueCode = bbMatchInfo.getIssueCode();
            	
            	String systemCode = SportLotteryUtil.getSystemCode(bbMatchInfo.getOfficialMatchCode(), issueCode);
            	
            	String officialMatchCode = bbMatchInfo.getOfficialMatchCode();
            	
            	Date saleEndTime = SportLotteryUtil.getSaleEndTime(bbMatchInfo.getStartTime(), issueCode, getIssueInfo); // 销售截止时间
            	
                if(systemCodeSet.contains(systemCode)) {
                	continue;
                }
                
        		SportAgainstInfoPO po = sportAgainstInfoDaoMapper.findByCode(lotteryCode, issueCode, systemCode);
        		
        		if(!ObjectUtil.isBlank(po)){
        			if(JcMatchStatusEnum.isUpdate(po.getMatchStatus()))
        				continue;
        			po.setModifyBy(createBy);
        			po.setStartTime(bbMatchInfo.getStartTime());
        			po.setSaleEndTime(saleEndTime);
        			po.setMatchStatus(bbMatchInfo.getStatus());
        		}else{
            		SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(bbMatchInfo.getHomeName(), bbMatchInfo.getHomeAbbrName(), type, bbMatchInfo.getHomeId(), fivehundredSource);

            		SportTeamSourceInfoPO guestSourcePO = new SportTeamSourceInfoPO(bbMatchInfo.getAwayName(), bbMatchInfo.getAwayAbbrName(), type, bbMatchInfo.getAwayId(),
            				fivehundredSource);

            		SportMatchSourceInfoPO matchSourcePO = new SportMatchSourceInfoPO(bbMatchInfo.getLeagueName(), bbMatchInfo.getLeagueAbbrName(), type, bbMatchInfo.getLeagueId(),
            				fivehundredSource);
            		
                    po = new SportAgainstInfoPO(null, lotteryCode, issueCode, null, null, null, null, 
                    		null, null, officialMatchCode, systemCode, bbMatchInfo.getStatus(), null, bbMatchInfo.getStartTime(), 
                    		saleEndTime, createBy, createBy, bbMatchInfo.getSaleDate());
                    
            		SportTeamInfoPO homeTeamPO = getTeamInfo(homeSourcePO);
            		if(!ObjectUtil.isBlank(homeTeamPO)){
            			po.setHomeTeamId(homeTeamPO.getId());
            			po.setHomeName(homeTeamPO.getTeamFullName());
            		}else{
            			po.setHomeName(bbMatchInfo.getHomeAbbrName());
            		}
            		SportTeamInfoPO awayTeamPO = getTeamInfo(guestSourcePO);
            		if(!ObjectUtil.isBlank(awayTeamPO)){
            			po.setVisitiTeamId(awayTeamPO.getId());
            			po.setVisitiName(awayTeamPO.getTeamFullName());
            		}else{
            			po.setHomeName(bbMatchInfo.getAwayAbbrName());
            		}
            		SportMatchInfoPO leaguePO = getMatchInfo(matchSourcePO);
            		if(!ObjectUtil.isBlank(leaguePO)){
            			po.setSportMatchInfoId(leaguePO.getId());
            			po.setMatchName(leaguePO.getMatchFullName());
            		}else{
            			po.setMatchName(bbMatchInfo.getLeagueAbbrName());       			
            		}
        		}

                sportAgainstInfoDaoMapper.merge(po);
                Long id = po.getId();
                
                //处理最新的SP值
                SportDataBbSpPO sportDataBbSpPO = JSONObject.parseObject(JSONObject.toJSONString(bbMatchInfo.getNewSp()), SportDataBbSpPO.class);
                sportDataBbSpPO.setSportAgainstInfoId(id);
                
                sportDataBbSpDaoMapper.merge(sportDataBbSpPO);

                // 处理子玩法状态
                //处理销售状态
                FhBbPlayStatus fhBbPlayStatus = bbMatchInfo.getPlayStatus();
                SportStatusBbPO sportStatusBbPO = new SportStatusBbPO(id, SportLotteryUtil.getSaleType(fhBbPlayStatus.getStatusWf()), 
                		SportLotteryUtil.getSaleType(fhBbPlayStatus.getStatusLetWf()), SportLotteryUtil.getSaleType(fhBbPlayStatus.getStatusBss()), 
                		SportLotteryUtil.getSaleType(fhBbPlayStatus.getStatusWs()), createBy);
                
                sportStatusBBDaoMapper.merge(sportStatusBbPO);
//                int num = sportStatusBBDaoMapper.selectCount(sportStatusBbPO);
//                if (num == 0) {
//                	sportStatusBBDaoMapper.insert(sportStatusBbPO);
//                }
                
                systemCodeSet.add(systemCode);
                
                try {
                    //500网缓存的key
                    FhCacheInfo cacheInfo = new FhCacheInfo();
                    cacheInfo.setLotteryCode(lotteryCode);
                    cacheInfo.setIssueCode(issueCode);
                    cacheInfo.setSportAgainstInfoId(po.getId());
                    cacheInfo.setOfficialId(bbMatchInfo.getNewsId());
                    addMatchCacheByIssue(cacheInfo);
				} catch (Exception e) {
					log.error("500网的数据彩期数据添加到缓存异常" + e.getMessage());
				}
            }
        }  	    	
    }
	
	/**
	 * 获取500网竞蓝历史sp值
	 * 
	 * @date 2017年11月14日下午4:17:11
	 * @author cheng.chen
	 */
    private void getHistorySp() throws Exception {
		
        List<String> list = sportAgainstInfoDaoMapper.findSaleMatchIssueList(getLotteryCode());

        if (ObjectUtil.isBlank(list)) {
            log.info("通过彩种编码查询彩期集合数据为null");
            return;
        }
        Map<Long, FhCacheInfo> cacheInfoMap = new HashMap<Long, FhCacheInfo>();
        for (String issueCode : list) {
			String key = CacheConstants.C_CRAWL_500_BB_ISSUE + DateUtil.convertDateToStr(DateUtil.convertStrToDate(issueCode, DateUtil.DATE_FORMAT), DateUtil.FORMAT_YYMMDD);
			Map<Long, FhCacheInfo> map = redisUtil.getObj(key, new HashMap<Long, FhCacheInfo>());
			if(ObjectUtil.isBlank(map))
				continue;
			cacheInfoMap.putAll(map);
        }
        
        Map<String, String> hashCodeMap = null;
        try {
            Object obj = redisUtil.getObj(CacheConstants.C_CRAWL_500_BB_HASH_FLAG);
            if(ObjectUtil.isBlank(obj)){
            	hashCodeMap = getMatchHash();
            	if(ObjectUtil.isBlank(hashCodeMap)){
            		log.info("抓取竞蓝历史sp值  hash值 页面数据抓取失败  !!!");
            		return;
            	}else{
            		redisUtil.addObj(CacheConstants.C_CRAWL_500_BB_HASH_FLAG, hashCodeMap, CacheConstants.FOUR_HOURS);
            	}
            }else{
            	hashCodeMap = (Map<String, String>) obj;
            }
		} catch (Exception e) {
			log.error("处理抓取500网竞蓝历史sp值, 抓取lhash值 异常 : " + e.getMessage());
			return;
		}
        
		for (final FhCacheInfo cacheInfo : cacheInfoMap.values()) {
			final String hashCode = hashCodeMap.get(cacheInfo.getOfficialId());
			if(ObjectUtil.isBlank(hashCode))
				continue;
			//执行500网竞足历史sp值数据抓取
            ThreadPoolManager.execute("执行获取id : " + cacheInfo.getSportAgainstInfoId() + "历史sp值", new Runnable() {
				@Override
				public void run() {
					try {
						for (FhBbLotteryEnum fhBbLotteryEnum : FhBbLotteryEnum.values()) {
							getSingleHistorySp(cacheInfo, fhBbLotteryEnum, hashCode);
							Thread.sleep(2000);
						}
					} catch (Exception e) {
						log.error("抓取对阵历史sp值 异常 id : " + cacheInfo.getSportAgainstInfoId() + ", message : " + e.getMessage());
					}
				}
			});		
		}
	}

    /**
     * 单场赛事历史sp值抓取
     * @param cacheInfo
     * @param fhBbLotteryEnum
     * @param hashCode
     * @throws Exception
     * @date 2017年12月24日下午5:03:28
     * @author cheng.chen
     */
	private void getSingleHistorySp(FhCacheInfo cacheInfo, FhBbLotteryEnum fhBbLotteryEnum, String hashCode) throws Exception {
		String url = "";
		String html = "";
		if(!fhBbLotteryEnum.equals(FhBbLotteryEnum.WS)){
			url = fh_bb_get_match_history_sp.replace("{0}", cacheInfo.getOfficialId()).replace("{1}", fhBbLotteryEnum.getPlayId().toString()).replace("{2}", hashCode)  + "&_=" + System.currentTimeMillis();
		    html = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
		       if(ObjectUtil.isBlank(html)){
		        log.info("获取单个对阵胜平负历史sp异常 sportAgainstInfoId : " + cacheInfo.getSportAgainstInfoId()  +  "没有返回值, json : " + html);
		        return;
		    }			
		}
		Long id = cacheInfo.getSportAgainstInfoId();
		String officialId = cacheInfo.getOfficialId();
		switch (fhBbLotteryEnum) {
		case WF:
	        //通过赛事id查询历史sp值最后入库时间
			SportDataBbWfPO lastPO = sportDataBbWfDaoMapper.findLast(id, WfTypeEnum.NOT_LET.getValue());
			wfHistorySp(html, id, WfTypeEnum.NOT_LET.getValue(), lastPO);
			break;
		case LET_WF:
	        //通过赛事id查询历史sp值最后入库时间
			SportDataBbWfPO letLastPO = sportDataBbWfDaoMapper.findLast(id, WfTypeEnum.LET.getValue());
			letWfHistorySp(html, id, WfTypeEnum.LET.getValue(), letLastPO);
			break;			
		case BSS:
			SportDataBbBssPO bssLastPO = sportDataBbBssDaoMapper.findLast(id);
			bssHistorySp(html, id, officialId, bssLastPO);
			break;
		case WS:
			SportDataBbWsPO wsLastPO = sportDataBbWsDaoMapper.findLast(id);
			wsHistorySp(id, wsLastPO);
			break;
		}
        
	}

	/**
	 * 胜负历史sp值处理
	 * @param html
	 * @param sportAgainstInfoId
	 * @param spType
	 * @param lastTime
	 * @date 2017年12月24日下午5:03:16
	 * @author cheng.chen
	 */
	private void wfHistorySp(String html, Long sportAgainstInfoId, Short spType, SportDataBbWfPO lastPO){
		
		List<SportDataBbWfPO> wfPOs = new ArrayList<>();
		Document document = Jsoup.parse(html);
		Elements trEl = ParseHtmlUtil.getSelect(document, ".jjzs_table tr");
		for (int i = trEl.size() -1; i >= 1; i--) {
			Elements td = ParseHtmlUtil.getSelect(trEl.get(i), "td");
			String time = ParseHtmlUtil.getElementsEqIndexText(td, 2);
            if (ObjectUtil.isBlank(time))
                continue;			
	      	Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATETIME_FORMAT_NO_SEC);
            if(!ObjectUtil.isBlank(lastPO) && !ObjectUtil.isBlank(lastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
            	if(DateUtil.compare(lastPO.getReleaseTime(), releaseTime) != -1)
            		continue;
        		Float spFail = ParseHtmlUtil.getElementsEqIndexText(td, 0, Float.class);
        		Float spWin = ParseHtmlUtil.getElementsEqIndexText(td, 1, Float.class);
        		SportDataBbWfPO wfPO = new SportDataBbWfPO(sportAgainstInfoId, null, spFail, spWin, spType, releaseTime);
        		if(!ObjectUtil.isBlank(lastPO) && wfPO.isEquals(lastPO))
        			continue;
        		wfPOs.add(wfPO);
        		lastPO = wfPO;
		}
		
		if(!ObjectUtil.isBlank(wfPOs))
			sportDataBbWfDaoMapper.insertBatch(wfPOs);
	}

	/**
	 * 让分胜负历史sp值处理
	 * @param html
	 * @param sportAgainstInfoId
	 * @param spType
	 * @param lastTime
	 * @date 2017年12月24日下午5:03:01
	 * @author cheng.chen
	 */
	private void letWfHistorySp(String html, Long sportAgainstInfoId, Short spType,  SportDataBbWfPO lastPO){
		List<SportDataBbWfPO> wfPOs = new ArrayList<>();
		Document document = Jsoup.parse(html);
		Elements trEl = ParseHtmlUtil.getSelect(document, ".jjzs_table tr");
		for (int i = trEl.size() -1; i >= 1; i--) {
			Elements td = ParseHtmlUtil.getSelect(trEl.get(i), "td");
			String time = ParseHtmlUtil.getElementsEqIndexText(td, 3);
            if (ObjectUtil.isBlank(time))
                continue;			
	      	Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATETIME_FORMAT_NO_SEC);
            if(!ObjectUtil.isBlank(lastPO) && !ObjectUtil.isBlank(lastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
            	if(DateUtil.compare(lastPO.getReleaseTime(), releaseTime) != -1)
            		continue;
        		Float spFail = ParseHtmlUtil.getElementsEqIndexText(td, 0, Float.class);
        		Float letScore = ParseHtmlUtil.getElementsEqIndexText(td, 1, Float.class);
        		Float spWin = ParseHtmlUtil.getElementsEqIndexText(td, 2, Float.class);
        		SportDataBbWfPO wfPO = new SportDataBbWfPO(sportAgainstInfoId, letScore, spFail, spWin, spType, releaseTime);
        		if(!ObjectUtil.isBlank(lastPO) && wfPO.isEquals(lastPO))
        			continue;
        		wfPOs.add(wfPO);
        		lastPO = wfPO;
        		
		}
		
		if(!ObjectUtil.isBlank(wfPOs))
			sportDataBbWfDaoMapper.insertBatch(wfPOs);
	}	

	/**
	 * 大小分历史sp处理
	 * @param html
	 * @param sportAgainstInfoId
	 * @param officialId
	 * @param lastTime
	 * @date 2017年12月24日下午5:02:27
	 * @author cheng.chen
	 */
	private void bssHistorySp(String html, Long sportAgainstInfoId, String officialId, SportDataBbBssPO lastPO){
		List<SportDataBbBssPO> bssPOs = new ArrayList<>();
		Document document = Jsoup.parse(html);
		Elements trEl = ParseHtmlUtil.getSelect(document, ".jjzs_table tr");
		for (int i = trEl.size() -1; i >= 1; i--) {
			Elements td = ParseHtmlUtil.getSelect(trEl.get(i), "td");
			String time = ParseHtmlUtil.getElementsEqIndexText(td, 3);
            if (ObjectUtil.isBlank(time))
                continue;			
	      	Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATETIME_FORMAT_NO_SEC);
            if(!ObjectUtil.isBlank(lastPO) && !ObjectUtil.isBlank(lastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
            	if(DateUtil.compare(lastPO.getReleaseTime(), releaseTime) != -1)
            		continue;
        		Float spBig = ParseHtmlUtil.getElementsEqIndexText(td, 0, Float.class);
        		Float presetScore = ParseHtmlUtil.getElementsEqIndexText(td, 1, Float.class);
        		Float spSmall = ParseHtmlUtil.getElementsEqIndexText(td, 2, Float.class);
        		SportDataBbBssPO bssPO = new SportDataBbBssPO(sportAgainstInfoId, presetScore, spBig, spSmall, releaseTime);
        		if(!ObjectUtil.isBlank(lastPO) && bssPO.isEquals(lastPO))
        			continue;
        		bssPOs.add(bssPO);
        		lastPO = bssPO;
		}
		
		if(!ObjectUtil.isBlank(bssPOs))
			sportDataBbBssDaoMapper.insertBatch(bssPOs);		
	}
	
	private void wsHistorySp(Long sportAgainstInfoId, SportDataBbWsPO lastPO){
//        胜分差sp值处理
      	//和官网一起跑, 胜分差历史sp值 可能存在两条, 需沟通是否要这么处理
          SportDataBbWsPO wsPO = new SportDataBbWsPO();
          wsPO.setSportAgainstInfoId(sportAgainstInfoId);
          int num = sportDataBbWsDaoMapper.selectCount(wsPO);
          if(num == 0){
        	SportDataBbSpPO newSp =  sportDataBbSpDaoMapper.findByMatchId(sportAgainstInfoId);
        	if(ObjectUtil.isBlank(newSp) || ObjectUtil.isBlank(newSp.getNewestSpFail15()))
        		return;
          	wsPO.setSpFail15(newSp.getNewestSpFail15());
          	wsPO.setSpFail610(newSp.getNewestSpFail610());
          	wsPO.setSpFail1115(newSp.getNewestSpFail1115());
          	wsPO.setSpFail1620(newSp.getNewestSpFail1620());
          	wsPO.setSpFail2125(newSp.getNewestSpFail2125());
          	wsPO.setSpFail26(newSp.getNewestSpFail26());
          	wsPO.setSpWin15(newSp.getNewestSpWin15());
          	wsPO.setSpWin610(newSp.getNewestSpWin610());
          	wsPO.setSpWin1115(newSp.getNewestSpWin1115());
          	wsPO.setSpWin1620(newSp.getNewestSpWin1620());
          	wsPO.setSpWin2125(newSp.getNewestSpWin2125());
          	wsPO.setSpWin26(newSp.getNewestSpWin26());
          	wsPO.setReleaseTime(new Date());
    		if(!ObjectUtil.isBlank(lastPO) && wsPO.isEquals(lastPO))
    			return;
          	sportDataBbWsDaoMapper.insert(wsPO);
          }					
	}

	/**
	 * 从500网的竞篮奖金集合获取hashcode值,方便访问历史sp
	 * @return
	 * @throws Exception
	 * @date 2017年12月24日下午5:01:57
	 * @author cheng.chen
	 */
	private Map<String, String> getMatchHash() throws Exception{
		Map<String, String> dataMap = null;
		Document document = Jsoup.parse(HTTPUtil.getRemotePage(fh_bb_get_match_hash));
		if(ObjectUtil.isBlank(document))
			return dataMap;
		Elements aElements = ParseHtmlUtil.getSelect(document, ".ld_table tbody .icon_jjzs");
		dataMap =  new HashMap<>();
		for (Element aEl : aElements) {
			String href = aEl.attr("href");
			String officalId = getString(href, "(id=)([0-9]+)", 2);
			String hashCode = getString(href, "(lhash=)([A-Za-z0-9]+)", 2);
			dataMap.put(officalId, hashCode);			
		}
		return dataMap;
	}
	
	/**
	 * 添加缓存数据到redis
	 * @param FhCacheInfo
	 * @date 2017年12月23日下午3:41:56
	 * @author cheng.chen
	 */
	private void addMatchCacheByIssue(FhCacheInfo fhCacheInfo){
		if(!ObjectUtil.isBlank(fhCacheInfo)){
			String key = CacheConstants.C_CRAWL_500_BB_ISSUE + fhCacheInfo.getIssueCode();
			 Map<Long, FhCacheInfo> map = redisUtil.getObj(key, new HashMap<Long,FhCacheInfo>());
			 if(ObjectUtil.isNull(map))
				 map = new HashMap<Long,FhCacheInfo>();
			 if(!map.containsKey(fhCacheInfo.getSportAgainstInfoId())){
				 map.put(fhCacheInfo.getSportAgainstInfoId(), fhCacheInfo);
				 redisUtil.addObj(key, map, CacheConstants.FOUR_HOURS);				 
			 }
		}
	}
	
	private String getString(String str, String regx, int index) {
		String result = null;
		// 1.将正在表达式封装成对象Patten 类来实现
		Pattern pattern = Pattern.compile(regx);
		// 2.将字符串和正则表达式相关联
		Matcher matcher = pattern.matcher(str);
		// 3.String 对象中的matches 是否包含
		// 查找符合规则的子串
		while (matcher.find()) {
			// 获取 字符串
			result = matcher.group(index);
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
		return bbLotteryCode;
	}
	
	/**
	 * 获取抓取类型
	 * @return
	 * @throws Exception
	 * @date 2017年11月23日下午2:28:36
	 * @author cheng.chen
	 */
	protected Short getType() throws Exception {
		return (short)SportEnum.MatchTypeEnum.BASKETBALL.getCode();
	}	

	public static void main(String[] args) throws Exception {
		SportDataBbWfPO po = new SportDataBbWfPO();
		po.setSpFail(1f);
		po.setSpWin(2f);
		po.setLetScore(null);
		
		SportDataBbWfPO po2 = new SportDataBbWfPO();
		System.out.println(po.isEquals(po2));
	}
	
}
