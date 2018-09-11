package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportDataBbWfPO{

    private Long id;

    private Long sportAgainstInfoId;

    private Float letScore;

    private Float spFail;

    private Float spWin;

    private Short spType;

    private Date releaseTime;

    private Date updateTime;

    private Date createTime;

    public SportDataBbWfPO() {
    }

    public SportDataBbWfPO(Long sportAgainstInfoId, Float letScore, Float spFail, Float spWin,
			Short spType, Date releaseTime) {
		this.sportAgainstInfoId = sportAgainstInfoId;
		this.letScore = letScore;
		this.spFail = spFail;
		this.spWin = spWin;
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

    public Float getLetScore() {
        return letScore;
    }

    public void setLetScore(Float letScore) {
        this.letScore = letScore;
    }

    public Float getSpFail() {
        return spFail;
    }

    public void setSpFail(Float spFail) {
        this.spFail = spFail;
    }

    public Float getSpWin() {
        return spWin;
    }

    public void setSpWin(Float spWin) {
        this.spWin = spWin;
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

    @Override
    public String toString() {
        return "SportDataBbWFPO{" +
                "sportAgainstInfoId=" + sportAgainstInfoId +
                ", letScore=" + letScore +
                ", spFail=" + spFail +
                ", spWin=" + spWin +
                ", spType=" + spType +
                ", releaseTime=" + releaseTime +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
    
    public Boolean isEquals(SportDataBbWfPO lastPO){
    	if(spFail.equals(lastPO.getSpFail()) && spWin.equals(lastPO.getSpWin()) 
    			&& (letScore == null ? true : letScore.equals(lastPO.getLetScore())) && spType.equals(lastPO.getSpType()))
    	  	return true;
    	return false;
    }
}