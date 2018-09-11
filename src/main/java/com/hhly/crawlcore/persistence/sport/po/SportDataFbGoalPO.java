package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportDataFbGoalPO {
    private Long id;

    private Long sportAgainstInfoId;

    private Float sp0Goal;

    private Float sp1Goal;

    private Float sp2Goal;

    private Float sp3Goal;

    private Float sp4Goal;

    private Float sp5Goal;

    private Float sp6Goal;

    private Float sp7Goal;

    private Date releaseTime;

    private Date updateTime;

    private Date createTime;

    public SportDataFbGoalPO() {
    }

    public SportDataFbGoalPO(SportDataFbSpPO sportDataFbSpPO) {
        this.sp0Goal = sportDataFbSpPO.getNewestSp0Goal();
        this.sp1Goal = sportDataFbSpPO.getNewestSp1Goal();
        this.sp2Goal = sportDataFbSpPO.getNewestSp2Goal();
        this.sp3Goal = sportDataFbSpPO.getNewestSp3Goal();
        this.sp4Goal = sportDataFbSpPO.getNewestSp4Goal();
        this.sp5Goal = sportDataFbSpPO.getNewestSp5Goal();
        this.sp6Goal = sportDataFbSpPO.getNewestSp6Goal();
        this.sp7Goal = sportDataFbSpPO.getNewestSp7Goal();

    }

    public SportDataFbGoalPO(Long sportAgainstInfoId, Float sp0Goal, Float sp1Goal, Float sp2Goal, Float sp3Goal, Float sp4Goal, Float sp5Goal, Float sp6Goal, Float sp7Goal, Date releaseTime) {
        this.sportAgainstInfoId = sportAgainstInfoId;
        this.sp0Goal = sp0Goal;
        this.sp1Goal = sp1Goal;
        this.sp2Goal = sp2Goal;
        this.sp3Goal = sp3Goal;
        this.sp4Goal = sp4Goal;
        this.sp5Goal = sp5Goal;
        this.sp6Goal = sp6Goal;
        this.sp7Goal = sp7Goal;
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

    public Float getSp0Goal() {
        return sp0Goal;
    }

    public void setSp0Goal(Float sp0Goal) {
        this.sp0Goal = sp0Goal;
    }

    public Float getSp1Goal() {
        return sp1Goal;
    }

    public void setSp1Goal(Float sp1Goal) {
        this.sp1Goal = sp1Goal;
    }

    public Float getSp2Goal() {
        return sp2Goal;
    }

    public void setSp2Goal(Float sp2Goal) {
        this.sp2Goal = sp2Goal;
    }

    public Float getSp3Goal() {
        return sp3Goal;
    }

    public void setSp3Goal(Float sp3Goal) {
        this.sp3Goal = sp3Goal;
    }

    public Float getSp4Goal() {
        return sp4Goal;
    }

    public void setSp4Goal(Float sp4Goal) {
        this.sp4Goal = sp4Goal;
    }

    public Float getSp5Goal() {
        return sp5Goal;
    }

    public void setSp5Goal(Float sp5Goal) {
        this.sp5Goal = sp5Goal;
    }

    public Float getSp6Goal() {
        return sp6Goal;
    }

    public void setSp6Goal(Float sp6Goal) {
        this.sp6Goal = sp6Goal;
    }

    public Float getSp7Goal() {
        return sp7Goal;
    }

    public void setSp7Goal(Float sp7Goal) {
        this.sp7Goal = sp7Goal;
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
    
    public Boolean isEquals(SportDataFbGoalPO lastPO){
    	if(sp0Goal.equals(lastPO.getSp0Goal()) && sp1Goal.equals(lastPO.getSp1Goal())
    			&& sp2Goal.equals(lastPO.getSp2Goal()) && sp3Goal.equals(lastPO.getSp3Goal())
    			&& sp4Goal.equals(lastPO.getSp4Goal()) && sp5Goal.equals(lastPO.getSp5Goal())
    			&& sp6Goal.equals(lastPO.getSp6Goal()) && sp7Goal.equals(lastPO.getSp7Goal()))
    		return true;
    	return false;
    }
}