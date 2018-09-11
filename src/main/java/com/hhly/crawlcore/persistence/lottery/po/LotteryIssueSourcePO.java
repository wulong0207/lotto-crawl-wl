package com.hhly.crawlcore.persistence.lottery.po;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chengkangning
 * @version 1.0
 * @desc
 * @date 2017/11/16
 * @company 益彩网络科技公司
 */
public class LotteryIssueSourcePO implements Serializable {


	private static final long serialVersionUID = 1038873343947317109L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 固定彩种code:低频:100开始;高频:200;竞技彩:300;地方彩:1100开始
     */
    private Integer lotteryCode;

    /**
     * 彩期期号
     */
    private String issueCode;

    /**
     * 彩种名称
     */
    private String lotteryName;

    /**
     * 官方开奖时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date lotteryTime;

    /**
     * 开奖号码, 全部红球 以,分隔, 红球+篮球以|分隔
     */
    private String drawCode;

    /**
     * 彩票当期销售总金额
     */
    private Integer salesAmount;

    /**
     * 彩票滚存金额
     */
    private Long jackpotAmount;

    /**
     * 数据来源1:爱彩乐;2:非凡彩票
     */
    private Short source;

    /**
     * 试机号码:福3D专用
     */
    private String drawCodeTest;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 0:未开始;1:销售中;2:销售截止;3:已开奖
     */
    private Short issueStatus;

    /**
     * 格式例如:一等奖|2|10000000 ,二等奖|5|200000,用 | 隔开；代表 奖项，注数，每注中奖金额 ,奖项与奖项之间用,号隔开.
     */
    private String drawDetail;


    /**
     * 彩种类型
     */
    private Integer lotteryType;

    private Integer issueId;

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public Integer getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(Integer lotteryType) {
        this.lotteryType = lotteryType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public String getIssueCode() {
        return issueCode;
    }

    public void setIssueCode(String issueCode) {
        this.issueCode = issueCode == null ? null : issueCode.trim();
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName == null ? null : lotteryName.trim();
    }

    public Date getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Date lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public String getDrawCode() {
        return drawCode;
    }

    public void setDrawCode(String drawCode) {
        this.drawCode = drawCode == null ? null : drawCode.trim();
    }

    public Integer getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Integer salesAmount) {
        this.salesAmount = salesAmount;
    }

    public Long getJackpotAmount() {
        return jackpotAmount;
    }

    public void setJackpotAmount(Long jackpotAmount) {
        this.jackpotAmount = jackpotAmount;
    }

    public Short getSource() {
        return source;
    }

    public void setSource(Short source) {
        this.source = source;
    }

    public String getDrawCodeTest() {
        return drawCodeTest;
    }

    public void setDrawCodeTest(String drawCodeTest) {
        this.drawCodeTest = drawCodeTest == null ? null : drawCodeTest.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(Short issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getDrawDetail() {
        return drawDetail;
    }

    public void setDrawDetail(String drawDetail) {
        this.drawDetail = drawDetail == null ? null : drawDetail.trim();
    }

}
