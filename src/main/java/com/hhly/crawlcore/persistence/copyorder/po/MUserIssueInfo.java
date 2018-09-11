package com.hhly.crawlcore.persistence.copyorder.po;


import java.util.Date;

/**
 * 专家表
 */
public class MUserIssueInfo {
    /**
     * 自动增长id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 默认启用 0:禁用; 1:启用
     */
    private Integer status;

    /**
     * 用户近期战绩 例如近7中7
     */
    private String recentRecord;

    /**
     * 命中率
     */
    private Double hitRate;

    /**
     * 盈利率
     */
    private Double profitRate;

    /**
     * 关注总人数
     */
    private Integer focusNum;

    /**
     * 发单总次数
     */
    private Integer issueNum;

    /**
     * 发单总金额
     */
    private Double issueAmount;

    /**
     * 命中次数
     */
    private Integer hitNum;

    /**
     * 命中订单总金额
     */
    private Double hitMoney;

    /**
     * 返佣总金额
     */
    private Double commissionAmount;

    /**
     * 跟单总人数
     */
    private Integer followNum;

    /**
     * 跟单总金额
     */
    private Integer followAmount;

    /**
     * 总中奖金额(发单)
     */
    private Double winAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改用户id(后台操作员ID)
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

    /**
     * 连红数
     */
    private Integer continueHit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 一比分接口过来的主键
     */
    private String yibifenUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRecentRecord() {
        return recentRecord;
    }

    public void setRecentRecord(String recentRecord) {
        this.recentRecord = recentRecord;
    }

    public Double getHitRate() {
        return hitRate;
    }

    public void setHitRate(Double hitRate) {
        this.hitRate = hitRate;
    }

    public Double getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(Double profitRate) {
        this.profitRate = profitRate;
    }

    public Integer getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(Integer focusNum) {
        this.focusNum = focusNum;
    }

    public Integer getIssueNum() {
        return issueNum;
    }

    public void setIssueNum(Integer issueNum) {
        this.issueNum = issueNum;
    }

    public Double getIssueAmount() {
        return issueAmount;
    }

    public void setIssueAmount(Double issueAmount) {
        this.issueAmount = issueAmount;
    }

    public Integer getHitNum() {
        return hitNum;
    }

    public void setHitNum(Integer hitNum) {
        this.hitNum = hitNum;
    }

    public Double getHitMoney() {
        return hitMoney;
    }

    public void setHitMoney(Double hitMoney) {
        this.hitMoney = hitMoney;
    }

    public Double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getFollowAmount() {
        return followAmount;
    }

    public void setFollowAmount(Integer followAmount) {
        this.followAmount = followAmount;
    }

    public Double getWinAmount() {
        return winAmount;
    }

    public void setWinAmount(Double winAmount) {
        this.winAmount = winAmount;
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

    public Integer getContinueHit() {
        return continueHit;
    }

    public void setContinueHit(Integer continueHit) {
        this.continueHit = continueHit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getYibifenUserId() {
        return yibifenUserId;
    }

    public void setYibifenUserId(String yibifenUserId) {
        this.yibifenUserId = yibifenUserId;
    }
}