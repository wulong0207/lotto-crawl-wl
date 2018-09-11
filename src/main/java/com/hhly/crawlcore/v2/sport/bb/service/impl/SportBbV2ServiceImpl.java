
package com.hhly.crawlcore.v2.sport.bb.service.impl;

import static com.hhly.skeleton.base.constants.CacheConstants.S_COMM_SPORT_BB_MATCH_LIST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.base.zeroc.WebClientManager;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportMatchInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportTeamInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.sport.entity.YbfJcBbMatch;
import com.hhly.crawlcore.v2.sport.bb.service.SportBbV2Service;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.LotteryEnum.Lottery;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月23日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@Service
public class SportBbV2ServiceImpl implements SportBbV2Service {
	
	@Autowired
	RedisUtil redisUtil;
	
    @Autowired
    private WebClientManager webClientManager;
	
	@Autowired
	SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;
	
	@Autowired
	SportMatchInfoDaoMapper sportMatchInfoDaoMapper;
	
	@Autowired
	SportTeamInfoDaoMapper sportTeamInfoDaoMapper;

	@Override
	public void delSportBbCache() {
    	//清除对阵的
        redisUtil.delAllObj(S_COMM_SPORT_BB_MATCH_LIST);
        //清除大小分sp值缓存
        redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_BB_SSS_HISTORY_SP);
        //清除胜分差sp值缓存
        redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_BB_WS_HISTORY_SP);
        //清除胜负sp值缓存
        redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_BB_WF_HISTORY_SP);
        
        List<SportAgainstInfoPO> pos = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.BB.getName());
        for (SportAgainstInfoPO po : pos) {
            redisUtil.delObj(CacheConstants.getSportBbSystemCodeMatchListCacheKey(po.getSystemCode()));
		}
        
	}

	@Override
	public void synMatchForYbf() {
		Integer lotteryCode = Lottery.BB.getName();
		
		//查询需要同步赛事Map集合, key为 官网id officialId
        Map<String, SportAgainstInfoPO> poMap = sportAgainstInfoDaoMapper.findSynSaleMatch(lotteryCode);
        
        if(ObjectUtil.isBlank(poMap))
        	return;
        
        //根据比赛时间获取一比分赛事数据
        //查询需要同步赛事的比赛时间
		Set<String> times = sportAgainstInfoDaoMapper.findSynSaleMatchTimeList(lotteryCode);
        List<YbfJcBbMatch> ybfJcMatches = new ArrayList<>();
        for (String time : times) {
            String result = webClientManager.getJcBbMatchByDate(time);
            if (!StringUtils.isBlank(result)) {
                ybfJcMatches.addAll(JsonUtil.jsonToArray(result, YbfJcBbMatch.class));
            }
        }

        List<SportAgainstInfoPO> updatePos = new ArrayList<>();
//        List<SportMatchInfoPO> updateMatchPos = new ArrayList<>();
//        List<SportTeamInfoPO> updateTeamPos = new ArrayList<>();
        Map<Long, SportMatchInfoPO> matchMap = new HashMap<>();
        Map<Long, SportTeamInfoPO> teamMap = new HashMap<>();
        //完善插入数据的
        for (YbfJcBbMatch ybf : ybfJcMatches) {
        	SportAgainstInfoPO po = poMap.get(ybf.getOfficialId());
        	
        	//通过sporttery官网id取不到值, 跳过
        	if(ObjectUtil.isBlank(po))
        		continue;
            //对阵信息
        	SportAgainstInfoPO paramPO = new SportAgainstInfoPO();
        	paramPO.setId(po.getId());
        	paramPO.setMatchAnalysisUrl(ybf.getAnalysisId() == null ? null : ybf.getAnalysisId().toString());
        	paramPO.setMatchInfoUrl(ybf.getMatchAdvicesId());
            updatePos.add(paramPO);
            //赛事信息
            if (!ObjectUtil.isBlank(po.getSportMatchInfoId())) {
            	if(!matchMap.containsKey(po.getSportMatchInfoId()))
            		matchMap.put(po.getSportMatchInfoId(), new SportMatchInfoPO(po.getSportMatchInfoId(), ybf.getMatchDataId()));
//                updateMatchPos.add(new SportMatchInfoPO(po.getSportMatchInfoId(), ybf.getMatchDataId()));
            }
            //主队信息
            if (!ObjectUtil.isBlank(po.getHomeTeamId())) {
            	if(!teamMap.containsKey(po.getHomeTeamId()))
            		teamMap.put(po.getHomeTeamId(), new SportTeamInfoPO(po.getHomeTeamId(),ybf.getHomeDataId(), ybf.getHomeRank()));
//                updateTeamPos.add(new SportTeamInfoPO(po.getHomeTeamId(),ybf.getHomeDataId(), ybf.getHomeRank()));
            }
            //客队信息
            if (!ObjectUtil.isBlank(po.getVisitiTeamId())) {
            	if(!teamMap.containsKey(po.getVisitiTeamId()))
            		teamMap.put(po.getVisitiTeamId(), new SportTeamInfoPO(po.getVisitiTeamId(), ybf.getGuestDataId(), ybf.getGuestRank()));
//                updateTeamPos.add(new SportTeamInfoPO(po.getVisitiTeamId(), ybf.getGuestDataId(), ybf.getGuestRank()));
            }
        }
        if (!ObjectUtil.isBlank(updatePos)) {
            sportAgainstInfoDaoMapper.updateBatch(updatePos);
        }
        if (!ObjectUtil.isBlank(matchMap)) {
            sportMatchInfoDaoMapper.updateBatch(new ArrayList<>(matchMap.values()));
        }
        if (!ObjectUtil.isBlank(teamMap)) {
        	sportTeamInfoDaoMapper.updateBatch(new ArrayList<>(teamMap.values()));
        }		
	}

}
