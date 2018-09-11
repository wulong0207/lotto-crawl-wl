
package com.hhly.crawlcore.v2.sport.oldfb.service;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月30日
 * @company 益彩网络科技公司
 * @version 1.0
 */

public interface SportOldFbV2Service {

	void delSportOldFbCache();
	
	/**
	 * 同步竞足赛事对接数据到老足彩
	 * 
	 * @date 2017年12月22日上午11:13:19
	 * @author cheng.chen
	 */
	void synFbMatchInfo();
}
