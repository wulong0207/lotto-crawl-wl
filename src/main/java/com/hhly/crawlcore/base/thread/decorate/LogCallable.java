package com.hhly.crawlcore.base.thread.decorate;

import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.hhly.skeleton.base.log.entity.RunnableLog;

import java.util.concurrent.Callable;

/**
 * @desc 日志包装
 * @author jiangwei
 * @date 2017年3月10日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class LogCallable<T> extends AbstractCallable<T>{
	/**
	 * 日志记录
	 */
	private int taskId;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 线程模板日志
	 */
	private AbstractLog<T> abstractLog;
	/**
	 * 是否添加日志
	 */
	private boolean addLog;
	
	public LogCallable(Callable<T> callable,String remark,boolean addLog) {
		super(callable);
		this.taskId = AbstractLog.getTimedTaskId();
		this.remark = remark;
		abstractLog = this.new Log();
		this.addLog = addLog;
	}
	
    /**
     * 被包装的方法
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年3月10日 上午11:56:19
     * @return
     * @throws Exception
     */
    private T superCall() throws Exception{
    	return super.call();
    }
	
	@Override
	public T call() throws Exception {
		RunnableLog runnableLog = new RunnableLog(taskId, remark);
		//包装方法
		T t = abstractLog.packaging(runnableLog);
		//添加日志到日志列队
		if(addLog){
			AbstractLog.addLog(runnableLog);	
		}
		return t;
	}
	/**
	 * @desc 模板日志实现类
	 * @author jiangwei
	 * @date 2017年3月10日
	 * @company 益彩网络科技公司
	 * @version 1.0
	 */
	public class Log extends AbstractLog<T> {
		@Override
		public T execute() {
			try {
				return superCall();
			} catch (Exception e) {
				throw new ServiceRuntimeException("线程运行异常",e);
			}
		}

	}
}
