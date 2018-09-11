package com.hhly.crawlcore.persistence.copyorder.po;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.Date;

/**
 * 专家级别表
 */
public class MUserIssueLevel {
    /**
     * 自动增长id
     */
    private Integer id;

    /**
     * 发单用户id
     */
    private Integer userIssueInfoId;

    /**
     * 彩种编号(大)
     */
    private Integer lotteryCode;

    /**
     * 用户等级,0：普通用户,1:专家
     */
    private Integer level;

    /**
     * 是否自动:0否;1是
     */
    private Integer isAutomatic;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改用户
     */
    private String modifyBy;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserIssueInfoId() {
        return userIssueInfoId;
    }

    public void setUserIssueInfoId(Integer userIssueInfoId) {
        this.userIssueInfoId = userIssueInfoId;
    }

    public Integer getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIsAutomatic() {
        return isAutomatic;
    }

    public void setIsAutomatic(Integer isAutomatic) {
        this.isAutomatic = isAutomatic;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}