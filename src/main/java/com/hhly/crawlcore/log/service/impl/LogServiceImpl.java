package com.hhly.crawlcore.log.service.impl;

import com.hhly.crawlcore.base.thread.decorate.OnlyRunnable;
import com.hhly.crawlcore.log.service.ILogService;
import com.hhly.crawlcore.persistence.task.dao.TimedTaskInfoDaoMapper;
import com.hhly.skeleton.base.bo.FutureTaskBO;
import com.hhly.skeleton.base.log.LogEnum;
import com.hhly.skeleton.base.log.LogHandler;
import com.hhly.skeleton.base.log.entity.RunnableLog;
import com.hhly.skeleton.base.log.entity.ThreadPoolLog;
import com.hhly.skeleton.base.log.visit.IVisitor;
import com.hhly.skeleton.base.log.visit.LogLimitVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements ILogService{
	
	private IVisitor visitor = new LogLimitVisitor();
	
	@Autowired
	private TimedTaskInfoDaoMapper taskInfoDaoMapper;
	
	@Override
	public List<RunnableLog> addTaskInfo(String fileUrl) {		
		return LogHandler.analysisLog(LogEnum.LOG_RUNNABLE, fileUrl, RunnableLog.class,visitor);
	}
	
	@Override
	public List<ThreadPoolLog> addThreadLog(String fileUrl) {
		return LogHandler.analysisLog(LogEnum.LOG_THREAD_POOL, fileUrl, ThreadPoolLog.class,visitor);
	}

	@Override
	public List<FutureTaskBO> getOnlyFutureTask() {
		return OnlyRunnable.getAllFutureTask();
	}
	
	@Override
	public void addRunnable(List<RunnableLog> logs){
		taskInfoDaoMapper.addListLogs(logs);
	}
	
}
