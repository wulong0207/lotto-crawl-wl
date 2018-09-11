package com.hhly.crawlcore.base.thread.decorate;

import com.hhly.skeleton.base.log.entity.RunnableLog;

/**
 * @desc 对线程任务添加日志记录
 * @author jiangwei
 * @date 2017年2月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class LogRunnable extends AbstractRunnable {
	/**
	 * 日志记录
	 */
	private int taskId;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 线程日志包装类
	 */
	private AbstractLog<?> abstractLog;
	/**
	 * 是否添加日志
	 */
	private boolean addLog;

	public LogRunnable(Runnable runnable, String remark,boolean addLog) {
		super(runnable);
		this.taskId = AbstractLog.getTimedTaskId();
		this.remark = remark;
		this.addLog = addLog;
		abstractLog = this.new Log<>();
		
	}
    /**
     * 日志包装方法
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年3月10日 上午11:57:48
     */
	private void superRun() {
		super.run();
	}

	@Override
	public void run() {
		//包装日志
		RunnableLog runnableLog = new RunnableLog(taskId, remark);
		abstractLog.packaging(runnableLog);
		//添加日志到日志列队
		if(addLog){
			AbstractLog.addLog(runnableLog);	
		}
	}

	public class Log<T> extends AbstractLog<T> {
		@Override
		public T execute() {
			superRun();
			return null;
		}

	}
}
