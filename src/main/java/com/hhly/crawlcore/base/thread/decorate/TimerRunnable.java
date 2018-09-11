package com.hhly.crawlcore.base.thread.decorate;

import java.util.Queue;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhly.crawlcore.base.thread.ITask;

/**
 * @desc 定时任务类 实现不是固定间隔的定时任务，和能主动结束任务
 * @author jiangwei
 * @date 2017-2-10
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class TimerRunnable implements Runnable {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(TimerRunnable.class);
    // 保存线程池执行任务返回对象
    private ScheduledFuture<?> future;
    // 定时任务执行间隔状态
    private Queue<Boolean> queue;
    // 具体之定义任务
    private ITask task;
    private long createTime;

    private TimerRunnable(Queue<Boolean> queue, ITask task) {
        this.queue = queue;
        this.task = task;
        createTime = System.currentTimeMillis();
    }

    /**
     * 实例化
     *
     * @param queue
     * @param task
     * @return
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年2月27日 下午2:20:34
     */
    public static TimerRunnable getInstance(Queue<Boolean> queue, ITask task) {
        return new TimerRunnable(queue, task);
    }

    @Override
    public void run() {
        // 根据queue 数据判断任务是否需要执行
        Boolean b = queue.poll();
        if (b == null) {
            cancel();
        } else if (b) {
            LOGGER.debug("定时器执行。。。");
            if (!task.execute()) {
                cancel();
            }
        }
    }
    
    /**
     * 线程任务主动结束
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年2月27日 下午2:14:24
     * @return
     */
    public boolean cancel() {
        if (future == null) {
            return false;
        }
        boolean isSuccess = future.cancel(false);
        task.destroy();
        LOGGER.info("taskTotalTime:"+getTaskTotalTime()+"|task.toString():"+task.toString());
        return isSuccess;
    }
    
    private long getTaskTotalTime(){
    	return  System.currentTimeMillis() -  createTime;
    }

	public ScheduledFuture<?> getFuture() {
		return future;
	}

	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}

	public Queue<Boolean> getQueue() {
		return queue;
	}

	public void setQueue(Queue<Boolean> queue) {
		this.queue = queue;
	}

	public ITask getTask() {
		return task;
	}

    public void setTask(ITask task) {
		this.task = task;
	}

}