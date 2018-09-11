package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

/**
 * 北单除胜负过关以外彩果
 */
public class SportDrawBjPO extends SportDataBjBasePO {

    /**
     * 自增长ID
     */
    private Integer id;

    /**
     * 竞技彩对阵详情表ID
     */
    private Long sportAgainstInfoId;

    /**
     * 半场比分；例如： 1:1
     */
    private String halfScore;

    /**
     * 全场比分；例如： 2:1
     */
    private String fullScore;

    /**
     * 让球值；例如：-1
     */
    private Double letNum;

    /**
     * 让球胜平负；0：负；1：平；3：胜  赛事状态为延期时，彩果为"*"
     */
    private String letWdf;

    /**
     * 大于等于7 时为  7 赛事状态为延期时，彩果为"*"
     */
    private String goalNum;

    /**
     * 可能值为33;31;30;13;11;10;03;01;00;3：胜；1：平；0：负
     */
    private String hfWdf;

    /**
     * 1：上单；2：上双；3：下单；4：下双；  例如： 4 解释：下盘：进球数小于等于2；上盘：进球数大于2单双：进球总数的单双； 赛事状态为延期时，彩果为"*"
     */
    private String udSd;

    /**
     * 10：1:0 ；20：2:0； 21：2:1； 30：3:0； 31：3:1； 32：3:2； 40：4:0； 41：4:1 ；42：4:2 ；90：胜其它；00： 0:0； 11：1:1； 22：2:2； 33：3:3；99：平其它；01：0:1 ；02：0:2； 12：1:2 ；03：0:3； 13：1:3； 23：2:3； 04：0:4； 14：1:4 ；24：2:4 ；09：负其它；
     */
    private String score;

    /**
     * 让球胜平负SP值
     */
    private Double spLetWdf;

    /**
     * 总进球数SP值
     */
    private Double spGoalNum;

    /**
     * 半全场胜平负SP值
     */
    private Double spHfWdf;

    /**
     * 上下盘单双SP值
     */
    private Double spUdSd;

    /**
     * 单场比分SP值
     */
    private Double spScore;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        this.halfScore = halfScore == null ? null : halfScore.trim();
    }

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore == null ? null : fullScore.trim();
    }

    public Double getLetNum() {
        return letNum;
    }

    public void setLetNum(Double letNum) {
        this.letNum = letNum;
    }

    public String getLetWdf() {
        return letWdf;
    }

    public void setLetWdf(String letWdf) {
        this.letWdf = letWdf == null ? null : letWdf.trim();
    }

    public String getGoalNum() {
        return goalNum;
    }

    public void setGoalNum(String goalNum) {
        this.goalNum = goalNum == null ? null : goalNum.trim();
    }

    public String getHfWdf() {
        return hfWdf;
    }

    public void setHfWdf(String hfWdf) {
        this.hfWdf = hfWdf == null ? null : hfWdf.trim();
    }

    public String getUdSd() {
        return udSd;
    }

    public void setUdSd(String udSd) {
        this.udSd = udSd == null ? null : udSd.trim();
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    public Double getSpLetWdf() {
        return spLetWdf;
    }

    public void setSpLetWdf(Double spLetWdf) {
        this.spLetWdf = spLetWdf;
    }

    public Double getSpGoalNum() {
        return spGoalNum;
    }

    public void setSpGoalNum(Double spGoalNum) {
        this.spGoalNum = spGoalNum;
    }

    public Double getSpHfWdf() {
        return spHfWdf;
    }

    public void setSpHfWdf(Double spHfWdf) {
        this.spHfWdf = spHfWdf;
    }

    public Double getSpUdSd() {
        return spUdSd;
    }

    public void setSpUdSd(Double spUdSd) {
        this.spUdSd = spUdSd;
    }

    public Double getSpScore() {
        return spScore;
    }

    public void setSpScore(Double spScore) {
        this.spScore = spScore;
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