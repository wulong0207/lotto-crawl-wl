package com.hhly.crawlcore.base.thread.common;

/**
 * @desc 唯一定时任务主键
 * @author jiangwei
 * @date 2017年3月7日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public  enum OnlyEnum {
	/**
	 * 彩期定时任务
	 */
	LOTTERY_ISSUE,
	
	/**
	 * 发送通知信息
	 */
	SEND_MSG,
	/**
	 * 未支付订单过期
	 */
	NO_PAY_ORDER_OVERDUE;
}
