package com.hhly.crawlcore.persistence.lottery.dao;

import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LotteryIssueDaoMapper {

	/**
	 * 获取当前期以后期的彩期信息
	 *
	 * @param lotteryCode
	 * @return
	 */
	List<LotteryIssuePO> getIssueInfo(@Param("lotteryCode") Integer lotteryCode);

	/**
	 * @desc 查询单个彩期信息
	 * @author huangb
	 * @date 2017年3月6日
	 * @param lotteryVO
	 *            参数对象
	 * @return 查询单个彩期信息
	 */
	LotteryIssuePO findSingle(LotteryIssuePO po);

	/**
	 * 根据彩种, 彩期编码数据查询彩期详情对象
	 * 
	 * @return
	 * @date 2017年12月23日下午3:27:20
	 * @author cheng.chen
	 */
	LotteryIssuePO findByCode(@Param("lotteryCode") Integer lotteryCode, @Param("issueCode") String issueCode);

	/**
	 * 获取当前期以及下几期彩种编号
	 *
	 * @param lotteryCode
	 * @return
	 */
	List<String> getCurrentAndNextIssue(@Param("lotteryCode") Integer lotteryCode);

	/**
	 * 获取当前期以及下几期彩种编号
	 *
	 * @param lotteryCode
	 * @return
	 */
	List<LotteryIssuePO> getCurrentAndNextIssuePO(@Param("lotteryCode") Integer lotteryCode);

	/**
	 * 彩期入库
	 * 
	 * @param lotteryIssuePO
	 * @date 2017年11月9日下午6:14:57
	 * @author cheng.chen
	 */
	void insert(LotteryIssuePO lotteryIssuePO);

	void highDrawUpdate(LotteryIssuePO lotteryIssuePO);

	/**
	 * 处理最大一期开奖时间的状态
	 *
	 * @param lotteryCode
	 */
	void updateLastStatus(@Param("lotteryCode") Integer lotteryCode);

	/**
	 * 更新其它数据为已完成，不是最新开奖
	 *
	 * @param lotteryCode
	 *            lotteryCode
	 */
	void updateOtherData(@Param("lotteryCode") Integer lotteryCode);

	/**
	 * 合并彩期入库
	 * 
	 * @param lotteryIssuePO
	 */
	void insertOrUpdateIssue(LotteryIssuePO lotteryIssuePO);

	/**
	 * 更新上一期彩期，开奖小于当前时间的最大一期
	 * 
	 * @param lotteryCode
	 */
	void updateCurrentIssue(@Param("lotteryCode") Integer lotteryCode);

	/**
	 * 更新最新开奖，开奖时间小于当前时间且开奖号码不为空的最大一期
	 * 
	 * @param lotteryCode
	 */
	void updateIssueLastest(@Param("lotteryCode") Integer lotteryCode);

}
