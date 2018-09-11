package com.hhly.crawlcore.persistence.lottery.po;

import java.io.Serializable;
import java.util.Date;

public class LotteryIssuePO implements Serializable {
	
	private static final long serialVersionUID = -5850352794282056232L;

	/**
     * 
     */
    private Integer id;

    /**
     * 固定彩种code:低频：100开始；高频：200；竞技彩：300
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
     * 0：未开始；1：当前期；2：上一期；3：已完成
     */
    private Short currentIssue;

    /**
     * 0：暂停销售；1：未开售；2：预售中；3：销售中；4：销售截止；5：已开奖；6：已派奖 ; 7 : 已审核
     */
    private Short saleStatus;

    /**
     * 官方截止销售时间
     */
    private Date officialEndTime;

    /**
     * 官方开奖时间
     */
    private Date lotteryTime;

    /**
     * 本站开始送票时间
     */
    private Date saleTime;

    /**
     * 本站截止销售时间
     */
    private Date saleEndTime;

    /**
     * 彩票销售总金额
     */
    private Long salesAmount;

    /**
     * 奖池金额
     */
    private Long jackpotAmount;

    /**
     * 开奖号码
     */
    private String drawCode;

    /**
     * 创建人真实姓名
     */
    private String createBy;

    /**
     * 修改人真实姓名
     */
    private String modifyBy;

    /**
     * cms修改时间
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

    /**
     * 备注
     */
    private String remark;

    /**
     * 官方开始销售时间
     */
    private Date officialStartTime;

    /**
     * 最新开奖 ，审核开奖时切换此状态；0：否；1：是
     */
    private Short issueLastest;

    /**
     * 试机号码
     */
    private String drawCodeTest;

    /**
     * 格式例如:一等奖|2|10000000 ,二等奖|5|200000,用 | 隔开；代表 奖项，注数，每注中奖金额 ，奖项与奖项之间用,号隔开。
     */
    private String drawDetail;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 固定彩种code:低频：100开始；高频：200；竞技彩：300
     * @return lottery_code 固定彩种code:低频：100开始；高频：200；竞技彩：300
     */
    public Integer getLotteryCode() {
        return lotteryCode;
    }

    /**
     * 固定彩种code:低频：100开始；高频：200；竞技彩：300
     * @param lotteryCode 固定彩种code:低频：100开始；高频：200；竞技彩：300
     */
    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    /**
     * 彩期期号
     * @return issue_code 彩期期号
     */
    public String getIssueCode() {
        return issueCode;
    }

    /**
     * 彩期期号
     * @param issueCode 彩期期号
     */
    public void setIssueCode(String issueCode) {
        this.issueCode = issueCode == null ? null : issueCode.trim();
    }

    /**
     * 彩种名称
     * @return lottery_name 彩种名称
     */
    public String getLotteryName() {
        return lotteryName;
    }

    /**
     * 彩种名称
     * @param lotteryName 彩种名称
     */
    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName == null ? null : lotteryName.trim();
    }

    /**
     * 0：未开始；1：当前期；2：上一期；3：已完成
     * @return current_issue 0：未开始；1：当前期；2：上一期；3：已完成
     */
    public Short getCurrentIssue() {
        return currentIssue;
    }

    /**
     * 0：未开始；1：当前期；2：上一期；3：已完成
     * @param currentIssue 0：未开始；1：当前期；2：上一期；3：已完成
     */
    public void setCurrentIssue(Short currentIssue) {
        this.currentIssue = currentIssue;
    }

    /**
     * 0：暂停销售；1：未开售；2：预售中；3：销售中；4：销售截止；5：已开奖；6：已派奖 ; 7 : 已审核
     * @return sale_status 0：暂停销售；1：未开售；2：预售中；3：销售中；4：销售截止；5：已开奖；6：已派奖 ; 7 : 已审核
     */
    public Short getSaleStatus() {
        return saleStatus;
    }

    /**
     * 0：暂停销售；1：未开售；2：预售中；3：销售中；4：销售截止；5：已开奖；6：已派奖 ; 7 : 已审核
     * @param saleStatus 0：暂停销售；1：未开售；2：预售中；3：销售中；4：销售截止；5：已开奖；6：已派奖 ; 7 : 已审核
     */
    public void setSaleStatus(Short saleStatus) {
        this.saleStatus = saleStatus;
    }

    /**
     * 官方截止销售时间
     * @return official_end_time 官方截止销售时间
     */
    public Date getOfficialEndTime() {
        return officialEndTime;
    }

    /**
     * 官方截止销售时间
     * @param officialEndTime 官方截止销售时间
     */
    public void setOfficialEndTime(Date officialEndTime) {
        this.officialEndTime = officialEndTime;
    }

    /**
     * 官方开奖时间
     * @return lottery_time 官方开奖时间
     */
    public Date getLotteryTime() {
        return lotteryTime;
    }

    /**
     * 官方开奖时间
     * @param lotteryTime 官方开奖时间
     */
    public void setLotteryTime(Date lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    /**
     * 本站开始送票时间
     * @return sale_time 本站开始送票时间
     */
    public Date getSaleTime() {
        return saleTime;
    }

    /**
     * 本站开始送票时间
     * @param saleTime 本站开始送票时间
     */
    public void setSaleTime(Date saleTime) {
        this.saleTime = saleTime;
    }

    /**
     * 本站截止销售时间
     * @return sale_end_time 本站截止销售时间
     */
    public Date getSaleEndTime() {
        return saleEndTime;
    }

    /**
     * 本站截止销售时间
     * @param saleEndTime 本站截止销售时间
     */
    public void setSaleEndTime(Date saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    /**
     * 彩票销售总金额
     * @return sales_amount 彩票销售总金额
     */
    public Long getSalesAmount() {
        return salesAmount;
    }

    /**
     * 彩票销售总金额
     * @param salesAmount 彩票销售总金额
     */
    public void setSalesAmount(Long salesAmount) {
        this.salesAmount = salesAmount;
    }

    /**
     * 奖池金额
     * @return jackpot_amount 奖池金额
     */
    public Long getJackpotAmount() {
        return jackpotAmount;
    }

    /**
     * 奖池金额
     * @param jackpotAmount 奖池金额
     */
    public void setJackpotAmount(Long jackpotAmount) {
        this.jackpotAmount = jackpotAmount;
    }

    /**
     * 开奖号码
     * @return draw_code 开奖号码
     */
    public String getDrawCode() {
        return drawCode;
    }

    /**
     * 开奖号码
     * @param drawCode 开奖号码
     */
    public void setDrawCode(String drawCode) {
        this.drawCode = drawCode == null ? null : drawCode.trim();
    }

    /**
     * 创建人真实姓名
     * @return create_by 创建人真实姓名
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 创建人真实姓名
     * @param createBy 创建人真实姓名
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 修改人真实姓名
     * @return modify_by 修改人真实姓名
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 修改人真实姓名
     * @param modifyBy 修改人真实姓名
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    /**
     * cms修改时间
     * @return modify_time cms修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * cms修改时间
     * @param modifyTime cms修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
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
     * 官方开始销售时间
     * @return official_start_time 官方开始销售时间
     */
    public Date getOfficialStartTime() {
        return officialStartTime;
    }

    /**
     * 官方开始销售时间
     * @param officialStartTime 官方开始销售时间
     */
    public void setOfficialStartTime(Date officialStartTime) {
        this.officialStartTime = officialStartTime;
    }

    /**
     * 最新开奖 ，审核开奖时切换此状态；0：否；1：是
     * @return issue_lastest 最新开奖 ，审核开奖时切换此状态；0：否；1：是
     */
    public Short getIssueLastest() {
        return issueLastest;
    }

    /**
     * 最新开奖 ，审核开奖时切换此状态；0：否；1：是
     * @param issueLastest 最新开奖 ，审核开奖时切换此状态；0：否；1：是
     */
    public void setIssueLastest(Short issueLastest) {
        this.issueLastest = issueLastest;
    }

    /**
     * 试机号码
     * @return draw_code_test 试机号码
     */
    public String getDrawCodeTest() {
        return drawCodeTest;
    }

    /**
     * 试机号码
     * @param drawCodeTest 试机号码
     */
    public void setDrawCodeTest(String drawCodeTest) {
        this.drawCodeTest = drawCodeTest == null ? null : drawCodeTest.trim();
    }

    /**
     * 格式例如:一等奖|2|10000000 ,二等奖|5|200000,用 | 隔开；代表 奖项，注数，每注中奖金额 ，奖项与奖项之间用,号隔开。
     * @return draw_detail 格式例如:一等奖|2|10000000 ,二等奖|5|200000,用 | 隔开；代表 奖项，注数，每注中奖金额 ，奖项与奖项之间用,号隔开。
     */
    public String getDrawDetail() {
        return drawDetail;
    }

    /**
     * 格式例如:一等奖|2|10000000 ,二等奖|5|200000,用 | 隔开；代表 奖项，注数，每注中奖金额 ，奖项与奖项之间用,号隔开。
     * @param drawDetail 格式例如:一等奖|2|10000000 ,二等奖|5|200000,用 | 隔开；代表 奖项，注数，每注中奖金额 ，奖项与奖项之间用,号隔开。
     */
    public void setDrawDetail(String drawDetail) {
        this.drawDetail = drawDetail == null ? null : drawDetail.trim();
    }
	
}