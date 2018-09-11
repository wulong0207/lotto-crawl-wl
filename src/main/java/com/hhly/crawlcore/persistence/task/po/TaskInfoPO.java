package com.hhly.crawlcore.persistence.task.po;

import java.util.Date;

/**
 * @author huangb
 *
 * @Date 2017年1月10日
 *
 * @Desc 任务执行信息
 */
public class TaskInfoPO {

	/**
	 * 编号
	 */
	private Long id;
	/**
	 * 定时任务ID
	 */
	private Integer timedTaskId;
	/**
	 * 任务触发时间
	 */
	private Date runTime;
	/**
	 * 1：手动执行；2：自动运行
	 */
	private Integer runWay;
	/**
	 * 0：失败；1：成功
	 */
	private Integer status;
	/**
	 * 任务开始时间
	 */
	private Date startTime;
	/**
	 * 任务结束时间
	 */
	private Date endTime;
	/**
	 * 任务耗时；单位：秒
	 */
	private Integer spendTime;
	/**
	 * 0：失败；1：成功
	 */
	private Integer result;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 备注
	 */
	private String remark;

	public TaskInfoPO() {
	}

	public TaskInfoPO(Integer timedTaskId, Date runTime, Integer runWay, Integer status, Date startTime, Date endTime,
			Integer spendTime, Integer result, String remark,Date createTime) {
		super();
		this.timedTaskId = timedTaskId;
		this.runTime = runTime;
		this.runWay = runWay;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.spendTime = spendTime;
		this.result = result;
		this.remark = remark;
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTimedTaskId() {
		return timedTaskId;
	}

	public void setTimedTaskId(Integer timedTaskId) {
		this.timedTaskId = timedTaskId;
	}

	public Date getRunTime() {
		return runTime;
	}

	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}

	public Integer getRunWay() {
		return runWay;
	}

	public void setRunWay(Integer runWay) {
		this.runWay = runWay;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(Integer spendTime) {
		this.spendTime = spendTime;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "TaskInfoPO [id=" + id + ", timedTaskId=" + timedTaskId + ", runTime=" + runTime + ", runWay=" + runWay
				+ ", status=" + status + ", startTime=" + startTime + ", endTime=" + endTime + ", spendTime="
				+ spendTime + ", result=" + result + ", updateTime=" + updateTime + ", createTime=" + createTime
				+ ", remark=" + remark + "]";
	}
}
