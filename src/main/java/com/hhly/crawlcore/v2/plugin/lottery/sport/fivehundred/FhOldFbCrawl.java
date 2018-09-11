
package com.hhly.crawlcore.v2.plugin.lottery.sport.fivehundred;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.common.OldMatchStatusEnum;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;
import com.hhly.crawlcore.v2.sport.entity.MatchInfo;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年12月23日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class FhOldFbCrawl extends AbstractCrawl {
	
	private static final Logger log = LoggerFactory.getLogger(FhOldFbCrawl.class);
	
	private static final String ft_old_fb_queue = "lotter:spider:matchinfo:list";
	
	@Autowired
	RedisUtil redisUtil;
	
	public FhOldFbCrawl() {
	}

	@Autowired
	public FhOldFbCrawl(LotteryIssueDaoMapper lotteryIssueDaoMapper,
			SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper, SportTeamInfoService sportTeamInfoService,
			SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueDaoMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}

	@Override
	public void crawlHandle() throws Exception {

	}

	@Override
	public void crawlMatch() throws Exception {
    	long startTime = System.currentTimeMillis();
    	log.info("500网从python获取数据开始  start : " + startTime);
    	
        Long size = redisUtil.length(ft_old_fb_queue);
        List<String> datalist = redisUtil.range(ft_old_fb_queue, 0, size.intValue());
        redisUtil.delObj(ft_old_fb_queue);
        
        if(ObjectUtil.isBlank(datalist))
        	return;
        
        Short type = getType();
        
        //处理对阵数据
        Set<String> codeSet = new HashSet<String>();
        
        Map<String, LotteryIssuePO> issueMap = new HashMap<>();
        
		String createBy = SportEnum.SportDataSource.FIVEHUNDRED_OFFICIAL.getName();
		
        log.info("500网获取的赛事集合 数量 : " + size);
        for (int i = 0; i < size; i++) {
            String value = datalist.get(i);
            if (!StringUtil.isBlank(value)) {
            	MatchInfo fhMatchInfo = JSONObject.parseObject(value, MatchInfo.class);
            	
            	if(ObjectUtil.isBlank(fhMatchInfo) || DateUtil.compare(fhMatchInfo.getStartTime(), DateUtil.getNowDate()) == -1)
            		continue;
            	
            	//彩种编码
            	Integer lotteryCode = fhMatchInfo.getLotteryCode();
            	//彩期编码
            	String issueCode = fhMatchInfo.getIssueCode();
            	//系统编码
            	String systemCode = SportLotteryUtil.getOldLotterySystemCode(issueCode, fhMatchInfo.getMatchIndex());
            	
                if(codeSet.contains(lotteryCode + SymbolConstants.UNDERLINE +  systemCode)) {
                	continue;
                }
                
                String issueKey = lotteryCode + SymbolConstants.UNDERLINE + issueCode;
                LotteryIssuePO issuePo = issueMap.get(issueKey);
                if(ObjectUtil.isBlank(issuePo)){
                    issuePo = lotteryIssueDaoMapper.findByCode(lotteryCode, issueCode);
                }else{
                	if(!issueMap.containsKey(issueKey))
                		issueMap.put(issueKey, issuePo);
                }
                
                Long id = sportAgainstInfoDaoMapper.findIdByCode(lotteryCode, issueCode, systemCode);
                
                //获取主队信息
                SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(fhMatchInfo.getHomeName(), fhMatchInfo.getHomeAbbrName(), type, fhMatchInfo.getHomeId(), fivehundredSource);
                //获取客队信息
                SportTeamSourceInfoPO awaySourcePO = new SportTeamSourceInfoPO(fhMatchInfo.getAwayName(), fhMatchInfo.getAwayAbbrName(), type, fhMatchInfo.getAwayId(), fivehundredSource);
                //获取赛事信息
                SportMatchSourceInfoPO matchSourcePO = new SportMatchSourceInfoPO(fhMatchInfo.getLeagueName(), fhMatchInfo.getLeagueAbbrName(), type, fhMatchInfo.getLeagueId(), fivehundredSource);

                SportAgainstInfoPO po = new SportAgainstInfoPO(id, lotteryCode, issueCode, null, null, null, null, null, 
                		null, systemCode, OldMatchStatusEnum.WAIT_MATCH.getCode(), null, fhMatchInfo.getStartTime(), issuePo.getSaleEndTime(), fhMatchInfo.getMatchIndex().longValue(), createBy, createBy);
                SportTeamInfoPO homeTeamPO = getTeamInfo(homeSourcePO);
                SportTeamInfoPO awayTeamPO = getTeamInfo(awaySourcePO);
                SportMatchInfoPO matchInfoPO = getMatchInfo(matchSourcePO);

                if (!ObjectUtil.isBlank(homeTeamPO)) {
                    po.setHomeTeamId(homeTeamPO.getId());
                    po.setHomeName(homeTeamPO.getTeamFullName());
                }else{
                	po.setHomeName(fhMatchInfo.getHomeAbbrName());
                }
                if (!ObjectUtil.isBlank(awayTeamPO)) {
                    po.setVisitiTeamId(awayTeamPO.getId());
                    po.setVisitiName(awayTeamPO.getTeamFullName());
                }else{
                	po.setVisitiName(fhMatchInfo.getAwayAbbrName());
                }
                if(!ObjectUtil.isBlank(matchInfoPO)){
                	po.setSportMatchInfoId(matchInfoPO.getId());
                	po.setMatchName(matchInfoPO.getMatchFullName());
                }else{
                	po.setMatchName(fhMatchInfo.getLeagueAbbrName());
                }
                
                sportAgainstInfoDaoMapper.merge(po);
                
                codeSet.add(lotteryCode + SymbolConstants.UNDERLINE +  systemCode);
            }
        }
        log.info("共处理赛事集合数据  : " + codeSet.size());
        long endTime = System.currentTimeMillis();
    	log.info("500网从python获取数据结束  end : " + (endTime - startTime));        
	}

	@Override
	public void dataHandle(String... params) throws Exception {
		// TODO Auto-generated method stub
		
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

}
