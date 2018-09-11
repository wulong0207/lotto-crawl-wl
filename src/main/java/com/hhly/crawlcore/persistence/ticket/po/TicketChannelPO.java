package com.hhly.crawlcore.persistence.ticket.po;

import java.math.BigDecimal;
import java.util.Date;

public class TicketChannelPO {
    /**
     * 自增长ID
     */
    private Integer id;

    /**
     * 自定义渠道ID
     */
    private String ticketChannelId;

    /**
     * 出票商名称
     */
    private String drawerName;

    /**
     * 出票账户
     */
    private String drawerAccount;

    /**
     * 账户密码
     */
    private String accountPassword;

    /**
     * 认证码
     */
    private String authCode;

    /**
     * 送票地址
     */
    private String sendUrl;

    /**
     * 查询地址
     */
    private String searchUrl;

    /**
     * 查询地址备用
     */
    private String searchUrlSpare;

    /**
     * 通知地址
     */
    private String noticeUrl;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    /**
     * 操作人员
     */
    private String modifyBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 
     */
    private String createBy;

    /**
     * 渠道状态:0禁用;1启用
     */
    private Boolean channelStatus;

    /**
     * 开奖状态(只针对高频彩):0禁用;1启用
     */
    private Boolean lotteryStatus;

    /**
     * 出票状态:0禁用;1启用
     */
    private Boolean ticketStatus;

    /**
     * 自增长ID
     * @return id 自增长ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 自增长ID
     * @param id 自增长ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 自定义渠道ID
     * @return ticket_channel_id 自定义渠道ID
     */
    public String getTicketChannelId() {
        return ticketChannelId;
    }

    /**
     * 自定义渠道ID
     * @param ticketChannelId 自定义渠道ID
     */
    public void setTicketChannelId(String ticketChannelId) {
        this.ticketChannelId = ticketChannelId == null ? null : ticketChannelId.trim();
    }

    /**
     * 出票商名称
     * @return drawer_name 出票商名称
     */
    public String getDrawerName() {
        return drawerName;
    }

    /**
     * 出票商名称
     * @param drawerName 出票商名称
     */
    public void setDrawerName(String drawerName) {
        this.drawerName = drawerName == null ? null : drawerName.trim();
    }

    /**
     * 出票账户
     * @return drawer_account 出票账户
     */
    public String getDrawerAccount() {
        return drawerAccount;
    }

    /**
     * 出票账户
     * @param drawerAccount 出票账户
     */
    public void setDrawerAccount(String drawerAccount) {
        this.drawerAccount = drawerAccount == null ? null : drawerAccount.trim();
    }

    /**
     * 账户密码
     * @return account_password 账户密码
     */
    public String getAccountPassword() {
        return accountPassword;
    }

    /**
     * 账户密码
     * @param accountPassword 账户密码
     */
    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword == null ? null : accountPassword.trim();
    }

    /**
     * 认证码
     * @return auth_code 认证码
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * 认证码
     * @param authCode 认证码
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    /**
     * 送票地址
     * @return send_url 送票地址
     */
    public String getSendUrl() {
        return sendUrl;
    }

    /**
     * 送票地址
     * @param sendUrl 送票地址
     */
    public void setSendUrl(String sendUrl) {
        this.sendUrl = sendUrl == null ? null : sendUrl.trim();
    }

    /**
     * 查询地址
     * @return search_url 查询地址
     */
    public String getSearchUrl() {
        return searchUrl;
    }

    /**
     * 查询地址
     * @param searchUrl 查询地址
     */
    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl == null ? null : searchUrl.trim();
    }

    /**
     * 查询地址备用
     * @return search_url_spare 查询地址备用
     */
    public String getSearchUrlSpare() {
        return searchUrlSpare;
    }

    /**
     * 查询地址备用
     * @param searchUrlSpare 查询地址备用
     */
    public void setSearchUrlSpare(String searchUrlSpare) {
        this.searchUrlSpare = searchUrlSpare == null ? null : searchUrlSpare.trim();
    }

    /**
     * 通知地址
     * @return notice_url 通知地址
     */
    public String getNoticeUrl() {
        return noticeUrl;
    }

    /**
     * 通知地址
     * @param noticeUrl 通知地址
     */
    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl == null ? null : noticeUrl.trim();
    }

    /**
     * 账户余额
     * @return account_balance 账户余额
     */
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    /**
     * 账户余额
     * @param accountBalance 账户余额
     */
    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    /**
     * 操作人员
     * @return modify_by 操作人员
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 操作人员
     * @param modifyBy 操作人员
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    /**
     * 更新时间
     * @return update_time 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 
     * @return create_by 
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 
     * @param createBy 
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 渠道状态:0禁用;1启用
     * @return channel_status 渠道状态:0禁用;1启用
     */
    public Boolean getChannelStatus() {
        return channelStatus;
    }

    /**
     * 渠道状态:0禁用;1启用
     * @param channelStatus 渠道状态:0禁用;1启用
     */
    public void setChannelStatus(Boolean channelStatus) {
        this.channelStatus = channelStatus;
    }

    /**
     * 开奖状态(只针对高频彩):0禁用;1启用
     * @return lottery_status 开奖状态(只针对高频彩):0禁用;1启用
     */
    public Boolean getLotteryStatus() {
        return lotteryStatus;
    }

    /**
     * 开奖状态(只针对高频彩):0禁用;1启用
     * @param lotteryStatus 开奖状态(只针对高频彩):0禁用;1启用
     */
    public void setLotteryStatus(Boolean lotteryStatus) {
        this.lotteryStatus = lotteryStatus;
    }

    /**
     * 出票状态:0禁用;1启用
     * @return ticket_status 出票状态:0禁用;1启用
     */
    public Boolean getTicketStatus() {
        return ticketStatus;
    }

    /**
     * 出票状态:0禁用;1启用
     * @param ticketStatus 出票状态:0禁用;1启用
     */
    public void setTicketStatus(Boolean ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}