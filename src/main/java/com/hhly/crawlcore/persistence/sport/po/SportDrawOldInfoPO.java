package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportDrawOldInfoPO {
    private Long id;

    private Long sportAgainstInfoId;

    private String halfScore;

    private String fullScore;

    private String fourGoal;

    private String sixHfWdf;

    private Long fourteenWdf;

    private Date drawTime;

    private String modifyBy;

    private Date modifyTime;

    private Date updateTime;

    private Date createTime;

    public SportDrawOldInfoPO() {
    }

    public SportDrawOldInfoPO(Long id, Long sportAgainstInfoId, String halfScore, String fullScore, String fourGoal, String sixHfWdf, Long fourteenWdf, Date drawTime, String modifyBy) {
        this.id = id;
        this.sportAgainstInfoId = sportAgainstInfoId;
        this.halfScore = halfScore;
        this.fullScore = fullScore;
        this.fourGoal = fourGoal;
        this.sixHfWdf = sixHfWdf;
        this.fourteenWdf = fourteenWdf;
        this.drawTime = drawTime;
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

    public String getHalfScore() {
        return halfScore;
    }

    public void setHalfScore(String halfScore) {
        this.halfScore = halfScore;
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore;
    }

    public String getFourGoal() {
        return fourGoal;
    }

    public void setFourGoal(String fourGoal) {
        this.fourGoal = fourGoal;
    }

    public String getSixHfWdf() {
        return sixHfWdf;
    }

    public void setSixHfWdf(String sixHfWdf) {
        this.sixHfWdf = sixHfWdf;
    }

    public Long getFourteenWdf() {
        return fourteenWdf;
    }

    public void setFourteenWdf(Long fourteenWdf) {
        this.fourteenWdf = fourteenWdf;
    }

    public Date getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Date drawTime) {
        this.drawTime = drawTime;
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