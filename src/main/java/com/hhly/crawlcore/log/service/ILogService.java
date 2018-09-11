package com.hhly.crawlcore.log.service;

import com.hhly.skeleton.base.bo.FutureTaskBO;
import com.hhly.skeleton.base.log.entity.RunnableLog;
import com.hhly.skeleton.base.log.entity.ThreadPoolLog;

import java.util.List;

/**
 * 
 * @desc 调度任务日志
 * @author jiangwei
 * @date 2017年3月2日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ILogService {
	/**
	 * 解析任务日志
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月2日 上午11:06:31
	 */
	List<RunnableLog> addTaskInfo(String fileUrl);
	
	/**
	 * 获取线程池运行情况
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月2日 下午3:09:47
	 * @param fileUrl
	 * @return
	 */
	List<ThreadPoolLog> addThreadLog(String fileUrl);
	
	/**
	 * 获取唯一定时任务
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月7日 下午3:24:06
	 * @return
	 */
	List<FutureTaskBO> getOnlyFutureTask();
	/**
	 * 添加日志
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月7日 下午4:54:16
	 */
	void addRunnable(List<RunnableLog> logs);
}
