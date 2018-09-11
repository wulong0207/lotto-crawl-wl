package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportDataFbOldPO {
    private Long id;

    private Long sportAgainstInfoId;

    private Float spWin;

    private Float spDraw;

    private Float spFail;

    private Short spType;

    private Date releaseTime;

    private Date updateTime;

    private Date createTime;

    public SportDataFbOldPO() {
    }

    public SportDataFbOldPO(Long sportDataFbOldId, Long sportAgainstInfoId, Float spWin, Float spDraw, Float spFail, Short spType, Date releaseTime) {
        this.id = sportDataFbOldId;
        this.sportAgainstInfoId = sportAgainstInfoId;
        this.spWin = spWin;
        this.spDraw = spDraw;
        this.spFail = spFail;
        this.spType = spType;
        this.releaseTime = releaseTime;
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

    public Float getSpWin() {
        return spWin;
    }

    public void setSpWin(Float spWin) {
        this.spWin = spWin;
    }

    public Float getSpDraw() {
        return spDraw;
    }

    public void setSpDraw(Float spDraw) {
        this.spDraw = spDraw;
    }

    public Float getSpFail() {
        return spFail;
    }

    public void setSpFail(Float spFail) {
        this.spFail = spFail;
    }

    public Short getSpType() {
        return spType;
    }

    public void setSpType(Short spType) {
        this.spType = spType;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
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