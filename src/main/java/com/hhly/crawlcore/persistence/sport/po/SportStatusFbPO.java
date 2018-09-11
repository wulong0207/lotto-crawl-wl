package com.hhly.crawlcore.persistence.sport.po;


import java.util.Date;

public class SportStatusFbPO {
	
    private Long id;
    
    private Long sportAgainstInfoId;

    private Short statusWdf;

    private Short statusLetWdf;

    private Short statusGoal;

    private Short statusHfWdf;

    private Short statusScore;

    private String modifyBy;

    private Date modifyTime;

    private Date updateTime;

    private Date createTime;

    public SportStatusFbPO() {
    }

    public SportStatusFbPO(Long sportAgainstInfoId, Short statusWdf, Short statusLetWdf, Short statusGoal, Short statusHfWdf, Short statusScore, String modifyBy) {
    	this.sportAgainstInfoId = sportAgainstInfoId;
        this.statusWdf = statusWdf;
        this.statusLetWdf = statusLetWdf;
        this.statusGoal = statusGoal;
        this.statusHfWdf = statusHfWdf;
        this.statusScore = statusScore;
        this.modifyBy = modifyBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSportAgainstInfoId() {
		return sportAgainstInfoId;
	}

	public void setSportAgainstInfoId(Long sportAgainstInfoId) {
		this.sportAgainstInfoId = sportAgainstInfoId;
	}

    public Short getStatusWdf() {
        return statusWdf;
    }

    public void setStatusWdf(Short statusWdf) {
        this.statusWdf = statusWdf;
    }

    public Short getStatusLetWdf() {
        return statusLetWdf;
    }

    public void setStatusLetWdf(Short statusLetWdf) {
        this.statusLetWdf = statusLetWdf;
    }

    public Short getStatusGoal() {
        return statusGoal;
    }

    public void setStatusGoal(Short statusGoal) {
        this.statusGoal = statusGoal;
    }

    public Short getStatusHfWdf() {
        return statusHfWdf;
    }

    public void setStatusHfWdf(Short statusHfWdf) {
        this.statusHfWdf = statusHfWdf;
    }

    public Short getStatusScore() {
        return statusScore;
    }

    public void setStatusScore(Short statusScore) {
        this.statusScore = statusScore;
    }
    
	public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }    

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
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
}