package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportDrawFbPO extends SportAgainstInfoPO {
    /**
     * 自增长ID
     */
    private Long id;

    /**
     * 竞技彩对阵详情表ID
     */
    private Integer sportAgainstInfoId;

    /**
     * 半场比分；例如： 1:1
     */
    private String halfScore;

    /**
     * 全场比分；例如： 2:1
     */
    private String fullScore;

    /**
     * 全场胜平负:0：负；1：平；3：胜
     */
    private Short fullSpf;

    /**
     * 让球胜平负；0：负；1：平；3：胜
     */
    private Short letSpf;

    /**
     * 让球值；例如：-1
     */
    private Byte letNum;

    /**
     * 10：1:0 ；20：2:0； 21：2:1； 30：3:0； 31：3:1； 32：3:2； 40：4:0； 41：4:1 ；42：4:2 ；50：5:0； 51：5:1； 52：5:2；90：胜其它；00： 0:0； 11：1:1； 22：2:2； 33：3:3；99：平其它；01：0:1 ；02：0:2； 12：1:2 ；03：0:3； 13：1:3； 23：2:3； 04：0:4； 14：1:4 ；24：2:4 ；05：0:5； 15：1:5； 25：2:5；09：负其它；
     */
    private String score;

    /**
     * 大于等于7 时为  7
     */
    private Short goalNum;

    /**
     * 例如： 33；31；30；13；11；10；03；01；00 3：胜；1：平；0：负
     */
    private String hfWdf;

    /**
     *
     */
    private Date drawTime;

    /**
     *
     */
    private String modifyBy;

    /**
     *
     */
    private Date modifyTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSportAgainstInfoId() {
        return sportAgainstInfoId;
    }

    public void setSportAgainstInfoId(Integer sportAgainstInfoId) {
        this.sportAgainstInfoId = sportAgainstInfoId;
    }

    public String getHalfScore() {
        return halfScore;
    }

    public void setHalfScore(String halfScore) {
        this.halfScore = halfScore == null ? null : halfScore.trim();
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore == null ? null : fullScore.trim();
    }

    public Short getFullSpf() {
        return fullSpf;
    }

    public void setFullSpf(Short fullSpf) {
        this.fullSpf = fullSpf;
    }

    public Short getLetSpf() {
        return letSpf;
    }

    public void setLetSpf(Short letSpf) {
        this.letSpf = letSpf;
    }

    public Byte getLetNum() {
        return letNum;
    }

    public void setLetNum(Byte letNum) {
        this.letNum = letNum;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public Short getGoalNum() {
        return goalNum;
    }

    public void setGoalNum(Short goalNum) {
        this.goalNum = goalNum;
    }

    public String getHfWdf() {
        return hfWdf;
    }

    public void setHfWdf(String hfWdf) {
        this.hfWdf = hfWdf == null ? null : hfWdf.trim();
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
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
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