package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

/**
 * 胜负过关开奖详情
 */
public class SportDrawWfPO extends SportDataBjBasePO {

    /**
     *
     */
    private Integer id;

    /**
     * 竞技彩对阵详情表ID
     */
    private Long sportAgainstInfoId;

    /**
     * 全场比分；例如： 2:1
     */
    private String fullScore;

    /**
     * 让球值；例如：-1
     */
    private Float letNum;

    /**
     * 让球胜平负；0：负；3：胜  赛事状态为延期时，彩果为"*"
     */
    private String letSf;

    /**
     * 让球胜平负SP值
     */
    private Double spLetWf;

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

    public String getFullScore() {
        return fullScore;
    }

    public void setFullScore(String fullScore) {
        this.fullScore = fullScore == null ? null : fullScore.trim();
    }

    public Float getLetNum() {
        return letNum;
    }

    public void setLetNum(Float letNum) {
        this.letNum = letNum;
    }

    public String getLetSf() {
        return letSf;
    }

    public void setLetSf(String letSf) {
        this.letSf = letSf == null ? null : letSf.trim();
    }

    public Double getSpLetWf() {
        return spLetWf;
    }

    public void setSpLetWf(Double spLetWf) {
        this.spLetWf = spLetWf;
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