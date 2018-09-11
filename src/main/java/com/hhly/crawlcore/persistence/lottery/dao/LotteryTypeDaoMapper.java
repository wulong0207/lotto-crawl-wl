package com.hhly.crawlcore.persistence.lottery.dao;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.lottery.po.LotteryTypePO;

public interface LotteryTypeDaoMapper {
	/**
	 * 查询彩种信息用于添加彩期
	 * 
	 * @param lotteryCode
	 * @return
	 */
	LotteryTypePO findTypeUseAddIssue(@Param("lotteryCode") Integer lotteryCode);
}