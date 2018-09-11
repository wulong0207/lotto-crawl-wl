
package com.hhly.crawlcore.v2.sport.fb.service.impl;

import static com.hhly.skeleton.base.constants.CacheConstants.S_COMM_SPORT_FB_MATCH_LIST;

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
import com.hhly.crawlcore.sport.entity.YbfJcFbMatch;
import com.hhly.crawlcore.v2.sport.fb.service.SportFbV2Service;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.LotteryEnum.Lottery;
import com.hhly.skeleton.base.common.SportEnum.WfTypeEnum;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class SportFbV2ServiceImpl implements SportFbV2Service {

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
	public void delSportFbCache() {
    	//清除对阵的
        redisUtil.delAllObj(S_COMM_SPORT_FB_MATCH_LIST);
        
        List<SportAgainstInfoPO> pos = sportAgainstInfoDaoMapper.findSaleMatchList(LotteryEnum.Lottery.FB.getName());
        for (SportAgainstInfoPO po : pos) {
            //清除胜平负sp值
            redisUtil.delObj(CacheConstants.getSportFbMatchHistorySpCacheKey(po.getId(), WfTypeEnum.NOT_LET.getValue()));
            //清除让球胜平负sp值
            redisUtil.delObj(CacheConstants.getSportFbMatchHistorySpCacheKey(po.getId(), WfTypeEnum.LET.getValue()));
            //清除根据系统编码的缓存
            redisUtil.delObj(CacheConstants.getSportFbSystemCodeMatchListCacheKey(po.getSystemCode()));
		}
	}

	@Override
	public void synMatchForYbf() {
		Integer lotteryCode = Lottery.FB.getName();
		
		//查询需要同步赛事Map集合, key为 官网id officialId
        Map<String, SportAgainstInfoPO> poMap = sportAgainstInfoDaoMapper.findSynSaleMatch(lotteryCode);
        
        if(ObjectUtil.isBlank(poMap))
        	return;
        
        //根据比赛时间获取一比分赛事数据
        //查询需要同步赛事的比赛时间
		Set<String> times = sportAgainstInfoDaoMapper.findSynSaleMatchTimeList(lotteryCode);
        List<YbfJcFbMatch> ybfJcMatches = new ArrayList<>();
        //比赛时间添加上今天
        times.add(DateUtil.getBeforeOrAfterDate(0));
        for (String time : times) {
            String result = webClientManager.getJcFbMatchByDate(time);
            if (!StringUtils.isBlank(result)) {
                ybfJcMatches.addAll(JsonUtil.jsonToArray(result, YbfJcFbMatch.class));
            }
        }

        List<SportAgainstInfoPO> updatePos = new ArrayList<>();
        Map<Long, SportMatchInfoPO> matchMap = new HashMap<>();
        Map<Long, SportTeamInfoPO> teamMap = new HashMap<>();
        //完善插入数据的
        for (YbfJcFbMatch ybf : ybfJcMatches) {
        	SportAgainstInfoPO po = poMap.get(ybf.getGwId());
        	
        	//通过sporttery官网id取不到值, 跳过
        	if(ObjectUtil.isBlank(po))
        		continue;
            //对阵信息
        	SportAgainstInfoPO paramPO = new SportAgainstInfoPO();
        	paramPO.setId(po.getId());
        	paramPO.setMatchAnalysisUrl(ybf.getScheduleId());
        	paramPO.setMatchInfoUrl(ybf.getId());
            updatePos.add(paramPO);
            //赛事信息
            if (!ObjectUtil.isBlank(po.getSportMatchInfoId())) {
            	if(!matchMap.containsKey(po.getSportMatchInfoId()))
            		matchMap.put(po.getSportMatchInfoId(), new SportMatchInfoPO(po.getSportMatchInfoId(), ybf.getLeagueId()));
//                updateMatchPos.add(new SportMatchInfoPO(po.getSportMatchInfoId(), ybf.getLeagueId()));
            }
            //主队信息
            if (!ObjectUtil.isBlank(po.getHomeTeamId())) {
            	if(!teamMap.containsKey(po.getHomeTeamId()))
            		teamMap.put(po.getHomeTeamId(), new SportTeamInfoPO(po.getHomeTeamId(),ybf.getHomeTeamId(), ybf.getHomeOrder()));
//                updateTeamPos.add(new SportTeamInfoPO(po.getHomeTeamId(),ybf.getHomeTeamId(), ybf.getHomeOrder()));
            }
            //客队信息
            if (!ObjectUtil.isBlank(po.getVisitiTeamId())) {
            	if(!teamMap.containsKey(po.getVisitiTeamId()))
            		teamMap.put(po.getVisitiTeamId(), new SportTeamInfoPO(po.getVisitiTeamId(),ybf.getGuestTeamId(), ybf.getGuestOrder()));
//                updateTeamPos.add(new SportTeamInfoPO(po.getVisitiTeamId(), ybf.getGuestTeamId(), ybf.getGuestOrder()));
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
