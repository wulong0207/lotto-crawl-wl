package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportDataBbWsPO{
	
    private Long id;

    private Long sportAgainstInfoId;

    private Float spFail15;

    private Float spFail610;

    private Float spFail1115;

    private Float spFail1620;

    private Float spFail2125;

    private Float spFail26;

    private Float spWin15;

    private Float spWin610;

    private Float spWin1115;

    private Float spWin1620;

    private Float spWin2125;

    private Float spWin26;

    private Date releaseTime;

    private Date updateTime;

    private Date createTime;

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

    public Float getSpFail15() {
        return spFail15;
    }

    public void setSpFail15(Float spFail15) {
        this.spFail15 = spFail15;
    }

    public Float getSpFail610() {
        return spFail610;
    }

    public void setSpFail610(Float spFail610) {
        this.spFail610 = spFail610;
    }

    public Float getSpFail1115() {
        return spFail1115;
    }

    public void setSpFail1115(Float spFail1115) {
        this.spFail1115 = spFail1115;
    }

    public Float getSpFail1620() {
        return spFail1620;
    }

    public void setSpFail1620(Float spFail1620) {
        this.spFail1620 = spFail1620;
    }

    public Float getSpFail2125() {
        return spFail2125;
    }

    public void setSpFail2125(Float spFail2125) {
        this.spFail2125 = spFail2125;
    }

    public Float getSpFail26() {
        return spFail26;
    }

    public void setSpFail26(Float spFail26) {
        this.spFail26 = spFail26;
    }

    public Float getSpWin15() {
        return spWin15;
    }

    public void setSpWin15(Float spWin15) {
        this.spWin15 = spWin15;
    }

    public Float getSpWin610() {
        return spWin610;
    }

    public void setSpWin610(Float spWin610) {
        this.spWin610 = spWin610;
    }

    public Float getSpWin1115() {
        return spWin1115;
    }

    public void setSpWin1115(Float spWin1115) {
        this.spWin1115 = spWin1115;
    }

    public Float getSpWin1620() {
        return spWin1620;
    }

    public void setSpWin1620(Float spWin1620) {
        this.spWin1620 = spWin1620;
    }

    public Float getSpWin2125() {
        return spWin2125;
    }

    public void setSpWin2125(Float spWin2125) {
        this.spWin2125 = spWin2125;
    }

    public Float getSpWin26() {
        return spWin26;
    }

    public void setSpWin26(Float spWin26) {
        this.spWin26 = spWin26;
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

    public SportDataBbWsPO() {

    }

    public SportDataBbWsPO(Long sportAgainstInfoId, Float spFail15, Float spFail610,
			Float spFail1115, Float spFail1620, Float spFail2125, Float spFail26,
			Float spWin15, Float spWin610, Float spWin1115, Float spWin1620, Float spWin2125,
			Float spWin26, Date releaseTime) {
		this.sportAgainstInfoId = sportAgainstInfoId;
		this.spFail15 = spFail15;
		this.spFail610 = spFail610;
		this.spFail1115 = spFail1115;
		this.spFail1620 = spFail1620;
		this.spFail2125 = spFail2125;
		this.spFail26 = spFail26;
		this.spWin15 = spWin15;
		this.spWin610 = spWin610;
		this.spWin1115 = spWin1115;
		this.spWin1620 = spWin1620;
		this.spWin2125 = spWin2125;
		this.spWin26 = spWin26;
		this.releaseTime = releaseTime;
	}

	@Override
    public String toString() {
        return "SportDataBbWSPO{" +
                "sportAgainstInfoId=" + sportAgainstInfoId +
                ", spFail15=" + spFail15 +
                ", spFail610=" + spFail610 +
                ", spFail1115=" + spFail1115 +
                ", spFail1620=" + spFail1620 +
                ", spFail2125=" + spFail2125 +
                ", spFail26=" + spFail26 +
                ", spWin15=" + spWin15 +
                ", spWin610=" + spWin610 +
                ", spWin1115=" + spWin1115 +
                ", spWin1620=" + spWin1620 +
                ", spWin2125=" + spWin2125 +
                ", spWin26=" + spWin26 +
                '}';
    }
	
    public Boolean isEquals(SportDataBbWsPO lastPO){
    	if(spFail15.equals(lastPO.getSpFail15()) && spFail610.equals(lastPO.getSpFail610())
    			&& spFail1115.equals(lastPO.getSpFail1115()) && spFail1620.equals(lastPO.getSpFail1620()) 
    			&& spFail2125.equals(lastPO.getSpFail2125()) && spFail26.equals(lastPO.getSpFail26()) 
    			&& spWin15.equals(lastPO.getSpWin15()) && spWin610.equals(lastPO.getSpWin610()) 
    			&& spWin1115.equals(lastPO.getSpWin1115()) && spWin1620.equals(lastPO.getSpWin1620())
    			&& spWin2125.equals(lastPO.getSpWin2125()) && spWin26.equals(lastPO.getSpWin26()))
    		return true;
    	return false;
    }	
}