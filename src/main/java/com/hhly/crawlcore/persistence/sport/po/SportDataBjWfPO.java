package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

/**
 * 胜负过关
 */
public class SportDataBjWfPO extends SportDataBjBasePO {
    /**
     *
     */
    private Integer id;

    /**
     * 竞技彩对阵详情表ID
     */
    private Long sportAgainstInfoId;

    /**
     * 让分值
     */
    private Float letScore;

    /**
     * SP主胜
     */
    private Float spWin;

    /**
     * SP主负
     */
    private Float spFail;

    /**
     * 发布时间
     */
    private Date releaseTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * sp值变化比较
     */
    private String md5Value;

    public SportDataBjWfPO() {
    }

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

    public Float getLetScore() {
        return letScore;
    }

    public void setLetScore(Float letScore) {
        this.letScore = letScore;
    }

    public Float getSpWin() {
        return spWin;
    }

    public void setSpWin(Float spWin) {
        this.spWin = spWin;
    }

    public Float getSpFail() {
        return spFail;
    }

    public void setSpFail(Float spFail) {
        this.spFail = spFail;
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

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value == null ? null : md5Value.trim();
    }

    @Override
    public String toString() {
        return new StringBuilder().append("SportDataBjWfPO{").
                append("letScore=").append(letScore).
                append(", spWin=").append(spWin).
                append(", spFail=").append(spFail).
                append(", issueCode='").append(this.getIssueCode()).append('\'').
                append(", lotteryCode=").append(this.getLotteryCode()).
                append(", bjNum=").append(this.getBjNum()).append('}').toString();
    }
}