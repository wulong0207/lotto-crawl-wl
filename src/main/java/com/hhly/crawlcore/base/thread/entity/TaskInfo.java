package com.hhly.crawlcore.base.thread.entity;

import java.util.concurrent.ScheduledFuture;

/**
 * @desc 定时任务信息
 * @author jiangwei
 * @date 2017年2月25日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class TaskInfo {
	//定时任务执行时间（毫秒）
    private long time;
    /**
     * 备注
     */
    private String remark;
    //定时任务
    private ScheduledFuture<?> future;

    public TaskInfo(){
    	
    }
    
	public TaskInfo(long time,String remark) {
		super();
		this.time = time;
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public ScheduledFuture<?> getFuture() {
		return future;
	}

	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}
     
     
}
