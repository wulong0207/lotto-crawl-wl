package com.hhly.crawlcore.base.thread.decorate;

import com.hhly.crawlcore.base.thread.entity.TaskInfo;
import com.hhly.skeleton.base.bo.FutureTaskBO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc 执行唯一定时任务装饰类
 * @author jiangwei
 * @date 2017年3月7日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class OnlyRunnable extends AbstractRunnable {
    /**
     * 保存唯一定时任务信息
     */
	private static final Map<String, TaskInfo> ONLY_MAP = new ConcurrentHashMap<>();
	/**
	 * 创建唯一定时任务加锁，避免出现重复
	 */
	private static Lock lock = new  ReentrantLock();
	/**
	 * 定时任务唯一key
	 * 必须保证在项目中唯一
	 */
	private String key;

	public OnlyRunnable(Runnable runnable, String key) {
		super(runnable);
		this.key = key;
	}
   
	/**
	 * 检查是否需要添加此定时任务
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月7日 下午2:53:10
	 * @param key 唯一表示
	 * @param time 定时间执行时间（毫秒时间戳）
	 * @return
	 */
	public static boolean isAddTask(String key, long time,String remark) {
		try {
			lock.lock();
			TaskInfo taskInfo = ONLY_MAP.get(key);
			if (taskInfo == null) {
				taskInfo = new TaskInfo(time,remark);
				ONLY_MAP.put(key, taskInfo);
			} else {
				//采用覆盖策略
				/*if (taskInfo.getTime() == time) {
					return false;
				}*/
				if (taskInfo.getFuture() != null) {
					taskInfo.getFuture().cancel(false);
					taskInfo.setFuture(null);
				}
			}
		} finally {
			lock.unlock();
		}
		return true;
	}

	/**
	 * 获取任务属性
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月7日 下午2:54:04
	 * @param key
	 * @return
	 */
	public static TaskInfo getTaskInfo(String key) {
		return ONLY_MAP.get(key);
	}
	/**
	 * 添加定时任务future
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月7日 下午2:54:28
	 * @param key
	 * @param future
	 */
	public static void setFuture(String key,ScheduledFuture<?> future){
		TaskInfo info = getTaskInfo(key);
		if(info != null){
			info.setFuture(future);
		}
	}
	/**
	 * 获取定时任务属性
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月7日 下午2:54:53
	 * @param key
	 * @return
	 */
	public static ScheduledFuture<?> getFuture(String key){
		return getTaskInfo(key).getFuture();
	}
	
	/**
	 * 获取当前所有的唯一定时任务
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月7日 下午2:59:10
	 * @return
	 */
	public static List<FutureTaskBO> getAllFutureTask(){
		List<FutureTaskBO> list = new ArrayList<>();
		for (Map.Entry<String,TaskInfo> entry : ONLY_MAP.entrySet()) {
			FutureTaskBO bo = new FutureTaskBO();
			bo.setKey(entry.getKey());
			TaskInfo info = entry.getValue();
			if(info != null){
				bo.setRemark(info.getRemark());
				bo.setDate(new Date(info.getTime()));
				ScheduledFuture<?> future = info.getFuture();
				if(future != null){
					bo.setInterval(future.getDelay(TimeUnit.SECONDS));
					bo.setStatus(future.isCancelled() ? 0 : 1);
				}
			}
			list.add(bo);
		}
		return list;
	}
	@Override 
	public void run() {
		ONLY_MAP.remove(key);
		super.run();
	}

}
