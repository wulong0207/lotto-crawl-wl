package com.hhly.crawlcore.persistence.copyorder.po;


import java.util.Date;

public class OrderIssueInfo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 发单用户表(m_user_issue_info)主键ID
     */
    private Integer userIssueId;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 最高回报率
     */
    private Double maxRoi;

    /**
     * 显示隐藏开关。默认为 1：显示， 0：隐藏
     */
    private Integer isShow;

    /**
     * 1：开奖后可见；2：全部可见；3：仅对抄单人可见；4：仅对关注人可见
     */
    private Integer orderVisibleType;

    /**
     * 提成比率 目前 4%-10%
     */
    private Double commissionRate;

    /**
     * 跟单总人数
     */
    private Integer followNum;

    /**
     * 跟单总金额
     */
    private Double followAmount;

    /**
     * 总佣金
     */
    private Double commissionAmount;

    /**
     * 置顶标签 1：置顶；0：不置顶 默认0
     */
    private Integer isTop;

    /**
     * 推荐标签 1：推荐；0：不推荐 默认0
     */
    private Integer isRecommend;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改用户id(后台操作员ID)
     */
    private String modifyBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最近战况
     */
    private String recentRecord;

    /**
     * 命中率
     */
    private Double hitRate;

    /**
     * 连红数
     */
    private Integer continueHit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 1 实单2，推荐内容
     */
    private Integer issueType;

    /**
     * 1:免费，2：收费
     */
    private Integer chargeType;

    /**
     * issue_content_info 主键
     */
    private Integer issueContentInfoId;

    /**
     * 商户ID
     */
    private Integer masterId;

    /**
     * 推荐理由
     */
    private String recommendReason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserIssueId() {
        return userIssueId;
    }

    public void setUserIssueId(Integer userIssueId) {
        this.userIssueId = userIssueId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Double getMaxRoi() {
        return maxRoi;
    }

    public void setMaxRoi(Double maxRoi) {
        this.maxRoi = maxRoi;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getOrderVisibleType() {
        return orderVisibleType;
    }

    public void setOrderVisibleType(Integer orderVisibleType) {
        this.orderVisibleType = orderVisibleType;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Double getFollowAmount() {
        return followAmount;
    }

    public void setFollowAmount(Double followAmount) {
        this.followAmount = followAmount;
    }

    public Double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public Integer getIssueType() {
        return issueType;
    }

    public void setIssueType(Integer issueType) {
        this.issueType = issueType;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public Integer getIssueContentInfoId() {
        return issueContentInfoId;
    }

    public void setIssueContentInfoId(Integer issueContentInfoId) {
        this.issueContentInfoId = issueContentInfoId;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }
}