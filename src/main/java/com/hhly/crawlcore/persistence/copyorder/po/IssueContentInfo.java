package com.hhly.crawlcore.persistence.copyorder.po;

import java.util.Date;

/**
 * 抄单-推荐内容表
 */
public class IssueContentInfo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 原始内容表主键ID
     */
    private Integer issueContentOriginalId;

    /**
     * 彩期
     */
    private String issueCode;

    /**
     * 标准投注内容
     */
    private String planContent;

    /**
     * 1：未开奖2：未中奖3：已中奖
     */
    private Integer orderStatus;

    /**
     * 大彩种
     */
    private Integer lotteryCode;

    /**
     * 子玩法
     */
    private Integer lotteryChildCode;

    /**
     * 推荐查看金额
     */
    private Double amount;

    /**
     * 
     */
    private Date createTime;

    /**
     * 购买场次编号
     */
    private String buyScreen;

    /**
     * 开始的对阵名称(最早开打)
     */
    private String beginTeamName;

    /**
     * 销售截止时间(最早开打的)
     */
    private Date saleEndTime;

    /**
     * 一比分原始推荐主键
     */
    private Integer sourceIssueId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIssueContentOriginalId() {
        return issueContentOriginalId;
    }

    public void setIssueContentOriginalId(Integer issueContentOriginalId) {
        this.issueContentOriginalId = issueContentOriginalId;
    }

    public String getIssueCode() {
        return issueCode;
    }

    public void setIssueCode(String issueCode) {
        this.issueCode = issueCode == null ? null : issueCode.trim();
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent == null ? null : planContent.trim();
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getLotteryChildCode() {
        return lotteryChildCode;
    }

    public void setLotteryChildCode(Integer lotteryChildCode) {
        this.lotteryChildCode = lotteryChildCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBuyScreen() {
        return buyScreen;
    }

    public void setBuyScreen(String buyScreen) {
        this.buyScreen = buyScreen;
    }

    public Integer getSourceIssueId() {
        return sourceIssueId;
    }

    public void setSourceIssueId(Integer sourceIssueId) {
        this.sourceIssueId = sourceIssueId;
    }

    public String getBeginTeamName() {
        return beginTeamName;
    }

    public void setBeginTeamName(String beginTeamName) {
        this.beginTeamName = beginTeamName;
    }

    public Date getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(Date saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    public Integer getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
    }
}