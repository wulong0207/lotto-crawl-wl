
package com.hhly.crawlcore.v2.sport.oldfb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.v2.sport.oldfb.service.SportOldFbV2Service;
import com.hhly.skeleton.base.constants.CacheConstants;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月30日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@Service
public class SportOldFbV2ServiceImpl implements SportOldFbV2Service {
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

	@Override
	public void delSportOldFbCache() {
    	//清除对阵的
        redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_OLD_MATCH_LIST);
    }

	@Override
	public void synFbMatchInfo() {
		sportAgainstInfoDaoMapper.updateSynFbMatchInfo();
	}
	
}
