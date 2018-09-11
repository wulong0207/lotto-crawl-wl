package com.hhly.crawlcore.v2.draw.service;

/**
 * 开奖数据合并入库
 *
 * @author huangchengfang1219
 * @date 2018年1月17日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface DrawMergeService {

	/**
	 * 合并
	 * 
	 * @param lotteryCode
	 * @param date
	 */
	public void merge(Integer lotteryCategory);

}
