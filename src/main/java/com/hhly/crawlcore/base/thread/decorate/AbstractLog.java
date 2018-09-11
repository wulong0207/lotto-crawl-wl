package com.hhly.crawlcore.base.thread.decorate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.util.SpringUtil;
import com.hhly.crawlcore.log.service.ILogService;
import com.hhly.crawlcore.log.service.impl.LogServiceImpl;
import com.hhly.skeleton.base.log.LogEnum;
import com.hhly.skeleton.base.log.entity.RunnableLog;
/**
 * 
 * @desc 日志记录模板类
 * @author jiangwei
 * @date 2017年3月10日
 * @company 益彩网络科技公司
 * @version 1.0
 */

public abstract class AbstractLog<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLog.class);
	/**
	 * 记录日志id
	 */
	private static final ThreadLocal<Integer> TIMED_TASK_ID= new ThreadLocal<>();
	/**
	 * 日志阻塞列队
	 */
	private static final BlockingQueue<RunnableLog> QUEUE = new LinkedBlockingQueue<>();
	/**
	 * 日志缓存条数
	 */
	private static final int LOG_CHCHE_COUNT = 30;
	/**
	 * 日志保存服务
	 */
	private static final ILogService LOG_SERVICE;
	
	static{
		LOG_SERVICE = SpringUtil.getBean(LogServiceImpl.class);
		addRunnableLog();
	}
	
	/**
	 * 包装日志
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月10日 上午11:51:44
	 * @param runnableLog 日志记录类
	 * @return
	 */
	public T packaging(RunnableLog runnableLog) {
		runnableLog.start();
		T t = null;
		try {
			t = execute();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception",e);
			runnableLog.setStatus(0);
			runnableLog.setResult(0);
			runnableLog.setRemark(runnableLog.getRemark() + e.getMessage());
			LOGGER.error("runnableLog", runnableLog);
		}
		runnableLog.end();
//		LOGGER.info(runnableLog);
		return t;
	}
	
    /**
     * 添加日志到数据库中
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年3月10日 上午11:52:44
     * @param runnableLog
     */
    public static void addLog(RunnableLog runnableLog){
    	QUEUE.add(runnableLog);
    }
    
	/**
	 * 具体执行的方法（被记录日志的方法）
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月10日 上午11:53:01
	 * @return
	 */
    public abstract T execute();
	
	/**
     * 添加日志到数据库，（通过启动一个线程获取阻塞列队的中日志，然后添加到服务中）
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年3月7日 下午4:47:36
     * @return
     */
    private static void addRunnableLog(){
    	new Thread(new Runnable() {
			@Override
			public void run() {
				List<RunnableLog> logs = new ArrayList<>();
		    	for(;;){
		    		try {
		    			RunnableLog log = QUEUE.take();
		    			logs.add(log);
		    			LOGGER.info(LogEnum.LOG_RUNNABLE.name()+JSONObject.toJSONString(log));
					} catch (InterruptedException e) {
						e.printStackTrace();
						LOGGER.error("Exception",e);
					}
		    		if(logs.size() >= LOG_CHCHE_COUNT){
		    			try {
		    				LOG_SERVICE.addRunnable(logs);
						} catch (Exception e) {
							e.printStackTrace();
							LOGGER.error("Exception",e);
						}
		    			logs.clear();
		    		}
		    	}
			}
		},"consume log to add db").start();
    }
    /**
	 * 记录日志ID
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月8日 上午9:29:39
	 * @param id
	 */
	public static void setTimedTaskId(int id){
		TIMED_TASK_ID.set(id);
	}
	/**
	 * 获取日志ID
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月8日 上午10:17:24
	 * @return
	 */
	public static int getTimedTaskId() {
		Integer id = TIMED_TASK_ID.get();
		return id == null ? 0 : id;
	}
}
