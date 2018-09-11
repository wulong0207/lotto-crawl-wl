package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportDataBbBssPO {
	
    private Long id;

    private Long sportAgainstInfoId;

    private Float presetScore;

    private Float spBig;

    private Float spSmall;

    private Date releaseTime;

    private Date updateTime;

    private Date createTime;
    
    public SportDataBbBssPO() {
	}

	public SportDataBbBssPO(Long sportAgainstInfoId, Float presetScore, Float spBig, Float spSmall,
			Date releaseTime) {
		this.sportAgainstInfoId = sportAgainstInfoId;
		this.presetScore = presetScore;
		this.spBig = spBig;
		this.spSmall = spSmall;
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

    public Float getPresetScore() {
        return presetScore;
    }

    public void setPresetScore(Float presetScore) {
        this.presetScore = presetScore;
    }

    public Float getSpBig() {
        return spBig;
    }

    public void setSpBig(Float spBig) {
        this.spBig = spBig;
    }

    public Float getSpSmall() {
        return spSmall;
    }

    public void setSpSmall(Float spSmall) {
        this.spSmall = spSmall;
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
    
    public Boolean isEquals(SportDataBbBssPO lastPO){
    	if(spBig.equals(lastPO.getSpBig()) && spSmall.equals(lastPO.getSpSmall()) 
    			&& presetScore.equals(lastPO.getPresetScore()))
    		return true;
    	return false;
    }    
}