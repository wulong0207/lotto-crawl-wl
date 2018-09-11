
package com.hhly.crawlcore.v2.sport.fb.service;
/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface SportFbV2Service {

	/**
	 * 清除竞足赛事缓存
	 * 
	 * @date 2017年12月22日上午11:13:43
	 * @author cheng.chen
	 */
	void delSportFbCache();
	
	/**
	 * 同步一比分数据到竞足赛事
	 * 
	 * @date 2017年12月22日上午11:13:33
	 * @author cheng.chen
	 */
	void synMatchForYbf();
}
