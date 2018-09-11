package com.hhly.crawlcore.persistence.sport.po;


import java.util.Date;

public class SportStatusBbPO {
	
    private Long id;

    private Long sportAgainstInfoId;

    private Short statusWf;

    private Short statusLetWf;

    private Short statusBigSmall;

    private Short statusScoreWf;

    private Object modifyBy;

    private Date modifyTime;

    private Date updateTime;

    private Date createTime;
    
    public SportStatusBbPO() {
    }

    public SportStatusBbPO(Long sportAgainstInfoId,Short statusWf, Short statusLetWf, Short statusBigSmall, Short statusScoreWf, String modifyBy) {
        this.sportAgainstInfoId = sportAgainstInfoId;
        this.statusWf = statusWf;
        this.statusLetWf = statusLetWf;
        this.statusBigSmall = statusBigSmall;
        this.statusScoreWf = statusScoreWf;
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

    public Short getStatusWf() {
        return statusWf;
    }

    public void setStatusWf(Short statusWf) {
        this.statusWf = statusWf;
    }

    public Short getStatusLetWf() {
        return statusLetWf;
    }

    public void setStatusLetWf(Short statusLetWf) {
        this.statusLetWf = statusLetWf;
    }

    public Short getStatusBigSmall() {
        return statusBigSmall;
    }

    public void setStatusBigSmall(Short statusBigSmall) {
        this.statusBigSmall = statusBigSmall;
    }

    public Short getStatusScoreWf() {
        return statusScoreWf;
    }

    public void setStatusScoreWf(Short statusScoreWf) {
        this.statusScoreWf = statusScoreWf;
    }

    public Object getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Object modifyBy) {
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