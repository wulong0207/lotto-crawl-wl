package com.hhly.crawlcore.persistence.task.dao;

import com.hhly.skeleton.base.log.entity.RunnableLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * @desc 定时任务日志
 * @author jiangwei
 * @date 2017年3月7日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface TimedTaskInfoDaoMapper {
    /**
     * 添加日志
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年3月7日 下午5:10:55
     * @param logs
     */
	void addListLogs(@Param("logs") List<RunnableLog> logs);

}
