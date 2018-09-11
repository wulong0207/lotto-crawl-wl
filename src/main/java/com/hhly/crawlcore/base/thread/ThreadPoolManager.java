package com.hhly.crawlcore.base.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.common.CrawlConstants;
import com.hhly.crawlcore.base.thread.common.OnlyEnum;
import com.hhly.crawlcore.base.thread.decorate.AbstractLog;
import com.hhly.crawlcore.base.thread.decorate.LogCallable;
import com.hhly.crawlcore.base.thread.decorate.LogRunnable;
import com.hhly.crawlcore.base.thread.decorate.OnlyRunnable;
import com.hhly.crawlcore.base.thread.decorate.TimerRunnable;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.log.LogEnum;
import com.hhly.skeleton.base.log.entity.RunnableLog;
import com.hhly.skeleton.base.log.entity.ThreadPoolLog;
import com.hhly.skeleton.base.util.PropertyUtil;

/**
 * @desc 线程池管理类,对线程池统一管理监控和使用
 * @author jiangwei
 * @date 2017年2月24日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class ThreadPoolManager {

	/**
	 * 线程池
	 */
	private static final ThreadPoolExecutor THREAD_POOL;

	/**
	 * 定时任务线程池，参数corePoolSize 核心线程数（同时能执行的任务数为corePoolSize）
	 */
	private static final ScheduledThreadPoolExecutor SCHEDULED_EXECUTOR;
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolManager.class);

	static {
		// 根据配置文件初始化线程池
		int corePoolSize = Integer.parseInt(PropertyUtil.getPropertyValue(CrawlConstants.APPLICATION, "manager_core_pool_size"));
		int maxCorePoolSize = Integer.parseInt(PropertyUtil.getPropertyValue(CrawlConstants.APPLICATION, "manager_max_core_pool_size"));
		int scheduledCorePoolSize = Integer.parseInt(PropertyUtil.getPropertyValue(CrawlConstants.APPLICATION, "manager_scheduled_core_pool_size"));
		THREAD_POOL = new ThreadPoolExecutor(corePoolSize, maxCorePoolSize, 60, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(100),new RunsPolicy());
		SCHEDULED_EXECUTOR = new ScheduledThreadPoolExecutor(scheduledCorePoolSize);
		// 记录线程池的情况，每10秒记录一次线程池信息
		SCHEDULED_EXECUTOR.scheduleAtFixedRate(new Runnable() {
			public void run() {
				monitor();
			}
		}, 10, 120, TimeUnit.SECONDS);
	}

	/**
	 * 启动一个线程
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 上午9:43:32
	 * @param remark
	 *            备注
	 * @param runnable
	 *            线程
	 */
	public static void execute(String remark, Runnable runnable) {
		execute(remark, runnable, false);
	}
	/**
	 * 启动一个线程
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 上午9:43:32
	 * @param remark
	 *            备注
	 * @param runnable
	 *            线程
	 * @param addLog
	 *            是否添加到数据库
	 */
	public static void execute(String remark, Runnable runnable,boolean addLog) {
		Runnable logRunable = new LogRunnable(runnable, remark,addLog);
		THREAD_POOL.execute(logRunable);
	}
	/**
	 * 添加任务到线程池
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月10日 下午12:06:28
	 * @param remark
	 * @param call
	 * @return
	 */
	public static <T> Future<T> submit(String remark, Callable<T> call) {
		return submit(remark, call, false);
	}
	/**
	 * 添加任务到线程池
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月10日 下午12:06:28
	 * @param remark
	 * @param call
	 * @param addLog
	 *          是否添加到数据库
	 * @return
	 */
	public static <T> Future<T> submit(String remark, Callable<T> call,boolean addLog) {
		Callable<T> callable = new LogCallable<>(call, remark,addLog);
		return THREAD_POOL.submit(callable);
	}

	/**
	 * 批量添加任务到线程池
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月10日 下午12:06:28
	 * @param remark
	 * @param calls
	 * @return
	 */
	public static <T> List<Future<T>> invokeAll(String remark, Collection<? extends Callable<T>>  calls) throws InterruptedException {
		return invokeAll(remark, calls, false);
	}
	
	/**
	 * 批量添加任务到线程池
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月10日 下午12:06:28
	 * @param remark
	 * @param call
	 * @param addLog
	 *          是否添加到数据库
	 * @return
	 */
	public static <T> List<Future<T>> invokeAll(String remark, Collection<? extends Callable<T>>  calls,boolean addLog) throws InterruptedException {
		List<Callable<T>> list = new ArrayList<>();
		for (Callable<T> callable : calls) {
			list.add(new LogCallable<>(callable, remark,addLog));
		}
		return THREAD_POOL.invokeAll(list);
	}

	/**
	 * 定时任务
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午12:31:48
	 * @param remark
	 *            备注
	 * @param runnable
	 *            任务
	 * @param initialDelay
	 *            延迟时间
	 * @param period
	 *            执行周期
	 * @param unit
	 *            时间单位
	 * @return
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(String remark, Runnable runnable, long initialDelay,
			long period, TimeUnit unit) {
		return scheduleAtFixedRate(remark, runnable, initialDelay, period, unit, false);
	}
	/**
	 * 定时任务
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午12:31:48
	 * @param remark
	 *            备注
	 * @param runnable
	 *            任务
	 * @param initialDelay
	 *            延迟时间
	 * @param period
	 *            执行周期
	 * @param unit
	 *            时间单位
	 * @param addLog 
	 *            是否添加日志           
	 * @return
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(String remark, Runnable runnable, long initialDelay,
			long period, TimeUnit unit,boolean addLog) {
		Runnable logRunable =new LogRunnable(runnable, remark,addLog);
		ScheduledFuture<?> future = SCHEDULED_EXECUTOR.scheduleAtFixedRate(logRunable, initialDelay, period, unit);
		return future;
	}
	/**
	 * 定时执行任务
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午12:25:52
	 * @param remark
	 *            备注
	 * @param queue
	 *            状态
	 * @param task
	 *            任务
	 * @param initialDelay
	 *            延迟时间
	 * @param period
	 *            周期时间
	 * @param unit
	 *            周几时间单位
	 */
	public static void scheduleAtFixedRateTask(String remark, Queue<Boolean> queue, ITask task, long initialDelay,
			long period, TimeUnit unit) {
		scheduleAtFixedRateTask(remark, queue, task, initialDelay, period, unit, false);
	}
	/**
	 * 定时执行任务
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午12:25:52
	 * @param remark
	 *            备注
	 * @param queue
	 *            状态
	 * @param task
	 *            任务
	 * @param initialDelay
	 *            延迟时间
	 * @param period
	 *            周期时间
	 * @param unit
	 *            周几时间单位
	 * @param addLog 
	 *            是否添加日志   
	 */
	public static void scheduleAtFixedRateTask(String remark, Queue<Boolean> queue, ITask task, long initialDelay,
			long period, TimeUnit unit,boolean addLog) {
		TimerRunnable timerRunable = TimerRunnable.getInstance(queue, task);
		ScheduledFuture<?> future = scheduleAtFixedRate(remark, timerRunable, initialDelay, period, unit,addLog);
		timerRunable.setFuture(future);
	}

	/**
	 * 定时任务
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午12:27:11
	 * @param remark
	 *            备注
	 * @param runnable
	 *            任务
	 * @param delay
	 *            延迟
	 * @param unit
	 *            时间单位
	 * @return
	 */
	public static ScheduledFuture<?> schedule(String remark, Runnable runnable, long delay, TimeUnit unit) {
		return schedule(remark, runnable, delay, unit, false);
	}
	/**
	 * 定时任务
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午12:27:11
	 * @param remark
	 *            备注
	 * @param runnable
	 *            任务
	 * @param delay
	 *            延迟
	 * @param unit
	 *            时间单位
	 * @param addLog 
	 *            是否添加日志   
	 * @return
	 */
	public static ScheduledFuture<?> schedule(String remark, Runnable runnable, long delay, TimeUnit unit,boolean addLog) {
		Runnable logRunable =new LogRunnable(runnable, remark,addLog);
		return SCHEDULED_EXECUTOR.schedule(logRunable, delay, unit);
	}

	/**
	 * 添加唯一定时任务
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月6日 下午4:59:09
	 * @param remark
	 *            备注
	 * @param runnable
	 *            具体任务
	 * @param key
	 *            唯一key
	 * @param time
	 *            执行时间（时间戳）
	 * @return
	 */
	public static ScheduledFuture<?> scheduleOnlyTask(String remark, Runnable runnable, OnlyEnum onlyEnum, String key,
			long time) {
		return scheduleOnlyTask(remark, runnable, onlyEnum, key, time, false);
	}
	/**
	 * 添加唯一定时任务
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月6日 下午4:59:09
	 * @param remark
	 *            备注
	 * @param runnable
	 *            具体任务
	 * @param key
	 *            唯一key
	 * @param time
	 *            执行时间（时间戳）
	 * @param addLog 
	 *            是否添加日志   
	 * @return
	 */
	public static ScheduledFuture<?> scheduleOnlyTask(String remark, Runnable runnable, OnlyEnum onlyEnum, String key,
			long time,boolean addLog) {
		Assert.notNull(runnable);
		Assert.hasText(key);
		key = onlyEnum.name() + SymbolConstants.UNDERLINE + key;
		OnlyRunnable onlyRunnable = new OnlyRunnable(runnable, key);
		boolean isAdd = OnlyRunnable.isAddTask(key, time,remark);
		if (!isAdd) {
			LOGGER.info("已启动 - 定时任务：" + remark + ",key:" + key + ",time:" + time);
			return OnlyRunnable.getFuture(key);
		}
		Runnable logRunable =new LogRunnable(onlyRunnable, remark,addLog);
		ScheduledFuture<?> future = SCHEDULED_EXECUTOR.schedule(logRunable, time - System.currentTimeMillis(),
				TimeUnit.MILLISECONDS);
		OnlyRunnable.setFuture(key, future);
		LOGGER.info("启动成功 - 定时任务：" + remark + ",key:" + key + ",time:" + time);
		return future;
	}
	/**
	 * 获取线程状态
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午4:39:45
	 * @return
	 */
	public static List<ThreadPoolLog> getThreadPoolStatus() {
		List<ThreadPoolLog> list = new ArrayList<>();
		ThreadPoolLog threadPoolBO = getServiceLog(THREAD_POOL);
		threadPoolBO.setName("THREAD_POOL");
		ThreadPoolLog scheduledThreadPoolBO = getServiceLog(SCHEDULED_EXECUTOR);
		scheduledThreadPoolBO.setMaximumPoolSize(scheduledThreadPoolBO.getCorePoolSize());
		scheduledThreadPoolBO.setName("SCHEDULED_EXECUTOR");
		list.add(threadPoolBO);
		list.add(scheduledThreadPoolBO);
		return list;
	}

	/**
	 * 添加日志
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午4:21:10
	 */
	private static void monitor() {
		List<ThreadPoolLog> list = getThreadPoolStatus();
		for (ThreadPoolLog bo : list) {
			LOGGER.info(LogEnum.LOG_THREAD_POOL.name() + JSONObject.toJSONString(bo));
		}
	}

	/**
	 * 解析封装线程池参数
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午4:02:10
	 * @param executorService
	 *            线程池
	 * @return
	 */
	private static ThreadPoolLog getServiceLog(ThreadPoolExecutor executorService) {
		ThreadPoolLog bo = new ThreadPoolLog();
		bo.setCreateTime(new Date());
		bo.setCorePoolSize(executorService.getCorePoolSize());
		bo.setPoolSize(executorService.getPoolSize());
		bo.setMaximumPoolSize(executorService.getMaximumPoolSize());
		bo.setActiveCount(executorService.getActiveCount());
		bo.setQueueSize(executorService.getQueue().size());
		bo.setTaskCount(executorService.getTaskCount());
		bo.setCompletedTaskCount(executorService.getCompletedTaskCount());
		bo.setKeepAliveTime(executorService.getKeepAliveTime(TimeUnit.MILLISECONDS));
		return bo;
	}
	/**
	 * @desc 线程池满后的处理策略
	 * @author jiangwei
	 * @date 2017年4月1日
	 * @company 益彩网络科技公司
	 * @version 1.0
	 */
	public static class RunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy{
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			RunnableLog runnableLog = new RunnableLog(AbstractLog.getTimedTaskId(),"警告任务列队已满，采用同步执行策略，请分析任务或调整线程池");
			runnableLog.start();
			super.rejectedExecution(r, e);
			runnableLog.end();
			AbstractLog.addLog(runnableLog);
		}
	}
}
