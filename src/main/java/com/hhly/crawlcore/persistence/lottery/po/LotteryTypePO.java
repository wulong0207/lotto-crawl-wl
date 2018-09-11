package com.hhly.crawlcore.persistence.lottery.po;

import java.io.Serializable;
import java.util.Date;

public class LotteryTypePO implements Serializable {
	
	
	private static final long serialVersionUID = -2153186590241918446L;

	/**
     * 低频：100开始；高频：200；竞技彩：300
     */
    private Integer id;

    /**
     * 固定彩种code。
     */
    private Integer lotteryCode;

    /**
     * 彩种名称
     */
    private String lotteryName;

    /**
     * 自动生成彩期数量，此数值为：开奖期号大于当前期的彩期总和。
     */
    private Short conIssueNum;

    /**
     * 0：停止生成彩期；1：正常生成彩期
     */
    private Short stopAddIssue;

    /**
     * 彩种运营logo
     */
    private String lotteryLogoUrl;

    /**
     * 购彩页logo
     */
    private String lotteryLogoBuy;

    /**
     * 移动端logo
     */
    private String lotteryLogoMobile;

    /**
     * 1：福彩；2：体彩
     */
    private Short adminCategory;

    /**
     * 1：数字彩；2：高频彩；3：竞技彩
     */
    private Short lotteryCategory;

    /**
     * 0：暂停销售；1：正常销售；
     */
    private Short saleStatus;

    /**
     * 0：请选择；1：全国；2：华东六省；3：广东
     */
    private Short area;

    /**
     * 1：手动 ；2：自动
     */
    private Short autoType;

    /**
     * 0：否；1：是；若0，按照我们规则去生成彩期。若1，同步第三方彩期数据，包括彩期编号，官方截止时间。
     */
    private Short synIssue;

    /**
     * 官方开售时间距离的秒数。
     */
    private Integer saleTime;

    /**
     * 官方截止时间距离的秒数。
     */
    private Integer buyEndTime;

    /**
     * 彩票最大倍数
     */
    private Integer splitMaxNum;

    /**
     * 官方检票时间距离的秒数。
     */
    private Integer endCheckTime;

    /**
     * 彩票最大金额
     */
    private Integer splitMaxAmount;

    /**
     * 官方开售时间(即官方可送票时间)；若为数字彩,高频彩：1|8:00,2|8:00,3|8:00,4|8:00,5|8:00,6|8:00,7|8:00;代表星期几的第一期的官方开售时间若为竞彩彩：格式为 1|09:00,2|09:00,3|07:30,4|07:30,5|09:002|09:00,6|09:00,7|09:00  ；代表周一，周二，周五每天9点，周三到周四每天7点30开售。(不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取)
     */
    private String startSailTime;

    /**
     * 官方截止销售时间；1）数字彩：例如 ：2|20:00|0,4|20:00|0,7|20:00|02）高频彩：此值为第一期截止销售时间。例如：1|09:10|0,2|09:10|0,3|09:10|0,4|09:10|0,5|09:10|0,6|09:10|0,7|09:10|0;  9点10分第一期截止销售；。3）竞技彩；格式为 1|24:00|0,2|24:00|0,3|24:00|0,4|24:00|0,5|24:00|0,6|01:00|1,7|01:00|1；表示周一到周五24点当天截止。周六到周日第二日的凌晨1点前截止销售。4）不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取
     */
    private String endSailTime;

    /**
     * 销售日销售周期；只针对高频彩；例如：1-30|300,31-60|600, 1到30期，时间间隔为300秒，31到60期时间间隔为600秒。其它彩种为空1-30为每5分钟一期，31-60为每10分钟一期；
     */
    private String sailDayCycle;

    /**
     * 真实姓名
     */
    private String createBy;

    /**
     * 真实姓名
     */
    private String modifyBy;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private String remark;

    /**
     * 官方开奖时间:格式为：2|21:15,4|21:15,7|21:15   代表周二，周四，周日的21:15分开奖,目前仅低频彩用，其它类型彩种为空
     */
    private String drawTime;

    /**
     * 彩期生成格式。
     */
    private String format;

    /**
     * 出票时间段 例如：1|09:00-20:00,2|08:00-22:00 代表周一9点到20点为出票时间，周二8点到22点为出票时间
     */
    private String comeOutTime;

    /**
     * 排序
     */
    private Integer orderId;

    /**
     * 暂停销售限制平台1:Web;2:Wap;3:Android;4:IOS;5其它平台
     */
    private String platform;

    /**
     * 休市时间，用‘,’分隔；例如：2017-01-27,2017-01-28
     */
    private String vacations;

    /**
     * 低频：100开始；高频：200；竞技彩：300
     * @return id 低频：100开始；高频：200；竞技彩：300
     */
    public Integer getId() {
        return id;
    }

    /**
     * 低频：100开始；高频：200；竞技彩：300
     * @param id 低频：100开始；高频：200；竞技彩：300
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 固定彩种code。
     * @return lottery_code 固定彩种code。
     */
    public Integer getLotteryCode() {
        return lotteryCode;
    }

    /**
     * 固定彩种code。
     * @param lotteryCode 固定彩种code。
     */
    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
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
     * 自动生成彩期数量，此数值为：开奖期号大于当前期的彩期总和。
     * @return con_issue_num 自动生成彩期数量，此数值为：开奖期号大于当前期的彩期总和。
     */
    public Short getConIssueNum() {
        return conIssueNum;
    }

    /**
     * 自动生成彩期数量，此数值为：开奖期号大于当前期的彩期总和。
     * @param conIssueNum 自动生成彩期数量，此数值为：开奖期号大于当前期的彩期总和。
     */
    public void setConIssueNum(Short conIssueNum) {
        this.conIssueNum = conIssueNum;
    }

    /**
     * 0：停止生成彩期；1：正常生成彩期
     * @return stop_add_issue 0：停止生成彩期；1：正常生成彩期
     */
    public Short getStopAddIssue() {
        return stopAddIssue;
    }

    /**
     * 0：停止生成彩期；1：正常生成彩期
     * @param stopAddIssue 0：停止生成彩期；1：正常生成彩期
     */
    public void setStopAddIssue(Short stopAddIssue) {
        this.stopAddIssue = stopAddIssue;
    }

    /**
     * 彩种运营logo
     * @return lottery_logo_url 彩种运营logo
     */
    public String getLotteryLogoUrl() {
        return lotteryLogoUrl;
    }

    /**
     * 彩种运营logo
     * @param lotteryLogoUrl 彩种运营logo
     */
    public void setLotteryLogoUrl(String lotteryLogoUrl) {
        this.lotteryLogoUrl = lotteryLogoUrl == null ? null : lotteryLogoUrl.trim();
    }

    /**
     * 购彩页logo
     * @return lottery_logo_buy 购彩页logo
     */
    public String getLotteryLogoBuy() {
        return lotteryLogoBuy;
    }

    /**
     * 购彩页logo
     * @param lotteryLogoBuy 购彩页logo
     */
    public void setLotteryLogoBuy(String lotteryLogoBuy) {
        this.lotteryLogoBuy = lotteryLogoBuy == null ? null : lotteryLogoBuy.trim();
    }

    /**
     * 移动端logo
     * @return lottery_logo_mobile 移动端logo
     */
    public String getLotteryLogoMobile() {
        return lotteryLogoMobile;
    }

    /**
     * 移动端logo
     * @param lotteryLogoMobile 移动端logo
     */
    public void setLotteryLogoMobile(String lotteryLogoMobile) {
        this.lotteryLogoMobile = lotteryLogoMobile == null ? null : lotteryLogoMobile.trim();
    }

    /**
     * 1：福彩；2：体彩
     * @return admin_category 1：福彩；2：体彩
     */
    public Short getAdminCategory() {
        return adminCategory;
    }

    /**
     * 1：福彩；2：体彩
     * @param adminCategory 1：福彩；2：体彩
     */
    public void setAdminCategory(Short adminCategory) {
        this.adminCategory = adminCategory;
    }

    /**
     * 1：数字彩；2：高频彩；3：竞技彩
     * @return lottery_category 1：数字彩；2：高频彩；3：竞技彩
     */
    public Short getLotteryCategory() {
        return lotteryCategory;
    }

    /**
     * 1：数字彩；2：高频彩；3：竞技彩
     * @param lotteryCategory 1：数字彩；2：高频彩；3：竞技彩
     */
    public void setLotteryCategory(Short lotteryCategory) {
        this.lotteryCategory = lotteryCategory;
    }

    /**
     * 0：暂停销售；1：正常销售；
     * @return sale_status 0：暂停销售；1：正常销售；
     */
    public Short getSaleStatus() {
        return saleStatus;
    }

    /**
     * 0：暂停销售；1：正常销售；
     * @param saleStatus 0：暂停销售；1：正常销售；
     */
    public void setSaleStatus(Short saleStatus) {
        this.saleStatus = saleStatus;
    }

    /**
     * 0：请选择；1：全国；2：华东六省；3：广东
     * @return area 0：请选择；1：全国；2：华东六省；3：广东
     */
    public Short getArea() {
        return area;
    }

    /**
     * 0：请选择；1：全国；2：华东六省；3：广东
     * @param area 0：请选择；1：全国；2：华东六省；3：广东
     */
    public void setArea(Short area) {
        this.area = area;
    }

    /**
     * 1：手动 ；2：自动
     * @return auto_type 1：手动 ；2：自动
     */
    public Short getAutoType() {
        return autoType;
    }

    /**
     * 1：手动 ；2：自动
     * @param autoType 1：手动 ；2：自动
     */
    public void setAutoType(Short autoType) {
        this.autoType = autoType;
    }

    /**
     * 0：否；1：是；若0，按照我们规则去生成彩期。若1，同步第三方彩期数据，包括彩期编号，官方截止时间。
     * @return syn_issue 0：否；1：是；若0，按照我们规则去生成彩期。若1，同步第三方彩期数据，包括彩期编号，官方截止时间。
     */
    public Short getSynIssue() {
        return synIssue;
    }

    /**
     * 0：否；1：是；若0，按照我们规则去生成彩期。若1，同步第三方彩期数据，包括彩期编号，官方截止时间。
     * @param synIssue 0：否；1：是；若0，按照我们规则去生成彩期。若1，同步第三方彩期数据，包括彩期编号，官方截止时间。
     */
    public void setSynIssue(Short synIssue) {
        this.synIssue = synIssue;
    }

    /**
     * 官方开售时间距离的秒数。
     * @return sale_time 官方开售时间距离的秒数。
     */
    public Integer getSaleTime() {
        return saleTime;
    }

    /**
     * 官方开售时间距离的秒数。
     * @param saleTime 官方开售时间距离的秒数。
     */
    public void setSaleTime(Integer saleTime) {
        this.saleTime = saleTime;
    }

    /**
     * 官方截止时间距离的秒数。
     * @return buy_end_time 官方截止时间距离的秒数。
     */
    public Integer getBuyEndTime() {
        return buyEndTime;
    }

    /**
     * 官方截止时间距离的秒数。
     * @param buyEndTime 官方截止时间距离的秒数。
     */
    public void setBuyEndTime(Integer buyEndTime) {
        this.buyEndTime = buyEndTime;
    }

    /**
     * 彩票最大倍数
     * @return split_max_num 彩票最大倍数
     */
    public Integer getSplitMaxNum() {
        return splitMaxNum;
    }

    /**
     * 彩票最大倍数
     * @param splitMaxNum 彩票最大倍数
     */
    public void setSplitMaxNum(Integer splitMaxNum) {
        this.splitMaxNum = splitMaxNum;
    }

    /**
     * 官方检票时间距离的秒数。
     * @return end_check_time 官方检票时间距离的秒数。
     */
    public Integer getEndCheckTime() {
        return endCheckTime;
    }

    /**
     * 官方检票时间距离的秒数。
     * @param endCheckTime 官方检票时间距离的秒数。
     */
    public void setEndCheckTime(Integer endCheckTime) {
        this.endCheckTime = endCheckTime;
    }

    /**
     * 彩票最大金额
     * @return split_max_amount 彩票最大金额
     */
    public Integer getSplitMaxAmount() {
        return splitMaxAmount;
    }

    /**
     * 彩票最大金额
     * @param splitMaxAmount 彩票最大金额
     */
    public void setSplitMaxAmount(Integer splitMaxAmount) {
        this.splitMaxAmount = splitMaxAmount;
    }

    /**
     * 官方开售时间(即官方可送票时间)；若为数字彩,高频彩：1|8:00,2|8:00,3|8:00,4|8:00,5|8:00,6|8:00,7|8:00;代表星期几的第一期的官方开售时间若为竞彩彩：格式为 1|09:00,2|09:00,3|07:30,4|07:30,5|09:002|09:00,6|09:00,7|09:00  ；代表周一，周二，周五每天9点，周三到周四每天7点30开售。(不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取)
     * @return start_sail_time 官方开售时间(即官方可送票时间)；若为数字彩,高频彩：1|8:00,2|8:00,3|8:00,4|8:00,5|8:00,6|8:00,7|8:00;代表星期几的第一期的官方开售时间若为竞彩彩：格式为 1|09:00,2|09:00,3|07:30,4|07:30,5|09:002|09:00,6|09:00,7|09:00  ；代表周一，周二，周五每天9点，周三到周四每天7点30开售。(不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取)
     */
    public String getStartSailTime() {
        return startSailTime;
    }

    /**
     * 官方开售时间(即官方可送票时间)；若为数字彩,高频彩：1|8:00,2|8:00,3|8:00,4|8:00,5|8:00,6|8:00,7|8:00;代表星期几的第一期的官方开售时间若为竞彩彩：格式为 1|09:00,2|09:00,3|07:30,4|07:30,5|09:002|09:00,6|09:00,7|09:00  ；代表周一，周二，周五每天9点，周三到周四每天7点30开售。(不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取)
     * @param startSailTime 官方开售时间(即官方可送票时间)；若为数字彩,高频彩：1|8:00,2|8:00,3|8:00,4|8:00,5|8:00,6|8:00,7|8:00;代表星期几的第一期的官方开售时间若为竞彩彩：格式为 1|09:00,2|09:00,3|07:30,4|07:30,5|09:002|09:00,6|09:00,7|09:00  ；代表周一，周二，周五每天9点，周三到周四每天7点30开售。(不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取)
     */
    public void setStartSailTime(String startSailTime) {
        this.startSailTime = startSailTime == null ? null : startSailTime.trim();
    }

    /**
     * 官方截止销售时间；1）数字彩：例如 ：2|20:00|0,4|20:00|0,7|20:00|02）高频彩：此值为第一期截止销售时间。例如：1|09:10|0,2|09:10|0,3|09:10|0,4|09:10|0,5|09:10|0,6|09:10|0,7|09:10|0;  9点10分第一期截止销售；。3）竞技彩；格式为 1|24:00|0,2|24:00|0,3|24:00|0,4|24:00|0,5|24:00|0,6|01:00|1,7|01:00|1；表示周一到周五24点当天截止。周六到周日第二日的凌晨1点前截止销售。4）不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取
     * @return end_sail_time 官方截止销售时间；1）数字彩：例如 ：2|20:00|0,4|20:00|0,7|20:00|02）高频彩：此值为第一期截止销售时间。例如：1|09:10|0,2|09:10|0,3|09:10|0,4|09:10|0,5|09:10|0,6|09:10|0,7|09:10|0;  9点10分第一期截止销售；。3）竞技彩；格式为 1|24:00|0,2|24:00|0,3|24:00|0,4|24:00|0,5|24:00|0,6|01:00|1,7|01:00|1；表示周一到周五24点当天截止。周六到周日第二日的凌晨1点前截止销售。4）不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取
     */
    public String getEndSailTime() {
        return endSailTime;
    }

    /**
     * 官方截止销售时间；1）数字彩：例如 ：2|20:00|0,4|20:00|0,7|20:00|02）高频彩：此值为第一期截止销售时间。例如：1|09:10|0,2|09:10|0,3|09:10|0,4|09:10|0,5|09:10|0,6|09:10|0,7|09:10|0;  9点10分第一期截止销售；。3）竞技彩；格式为 1|24:00|0,2|24:00|0,3|24:00|0,4|24:00|0,5|24:00|0,6|01:00|1,7|01:00|1；表示周一到周五24点当天截止。周六到周日第二日的凌晨1点前截止销售。4）不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取
     * @param endSailTime 官方截止销售时间；1）数字彩：例如 ：2|20:00|0,4|20:00|0,7|20:00|02）高频彩：此值为第一期截止销售时间。例如：1|09:10|0,2|09:10|0,3|09:10|0,4|09:10|0,5|09:10|0,6|09:10|0,7|09:10|0;  9点10分第一期截止销售；。3）竞技彩；格式为 1|24:00|0,2|24:00|0,3|24:00|0,4|24:00|0,5|24:00|0,6|01:00|1,7|01:00|1；表示周一到周五24点当天截止。周六到周日第二日的凌晨1点前截止销售。4）不包括老足彩，北京单场，胜负过关彩种，这些彩种为人工在彩期中录入或数据抓取
     */
    public void setEndSailTime(String endSailTime) {
        this.endSailTime = endSailTime == null ? null : endSailTime.trim();
    }

    /**
     * 销售日销售周期；只针对高频彩；例如：1-30|300,31-60|600, 1到30期，时间间隔为300秒，31到60期时间间隔为600秒。其它彩种为空1-30为每5分钟一期，31-60为每10分钟一期；
     * @return sail_day_cycle 销售日销售周期；只针对高频彩；例如：1-30|300,31-60|600, 1到30期，时间间隔为300秒，31到60期时间间隔为600秒。其它彩种为空1-30为每5分钟一期，31-60为每10分钟一期；
     */
    public String getSailDayCycle() {
        return sailDayCycle;
    }

    /**
     * 销售日销售周期；只针对高频彩；例如：1-30|300,31-60|600, 1到30期，时间间隔为300秒，31到60期时间间隔为600秒。其它彩种为空1-30为每5分钟一期，31-60为每10分钟一期；
     * @param sailDayCycle 销售日销售周期；只针对高频彩；例如：1-30|300,31-60|600, 1到30期，时间间隔为300秒，31到60期时间间隔为600秒。其它彩种为空1-30为每5分钟一期，31-60为每10分钟一期；
     */
    public void setSailDayCycle(String sailDayCycle) {
        this.sailDayCycle = sailDayCycle == null ? null : sailDayCycle.trim();
    }

    /**
     * 真实姓名
     * @return create_by 真实姓名
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 真实姓名
     * @param createBy 真实姓名
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 真实姓名
     * @return modify_by 真实姓名
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 真实姓名
     * @param modifyBy 真实姓名
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    /**
     * 更新时间
     * @return modify_time 更新时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 更新时间
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 
     * @return update_time 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     * @param updateTime 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 
     * @return create_time 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     * @param createTime 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     * @return remark 
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 
     * @param remark 
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 官方开奖时间:格式为：2|21:15,4|21:15,7|21:15   代表周二，周四，周日的21:15分开奖,目前仅低频彩用，其它类型彩种为空
     * @return draw_time 官方开奖时间:格式为：2|21:15,4|21:15,7|21:15   代表周二，周四，周日的21:15分开奖,目前仅低频彩用，其它类型彩种为空
     */
    public String getDrawTime() {
        return drawTime;
    }

    /**
     * 官方开奖时间:格式为：2|21:15,4|21:15,7|21:15   代表周二，周四，周日的21:15分开奖,目前仅低频彩用，其它类型彩种为空
     * @param drawTime 官方开奖时间:格式为：2|21:15,4|21:15,7|21:15   代表周二，周四，周日的21:15分开奖,目前仅低频彩用，其它类型彩种为空
     */
    public void setDrawTime(String drawTime) {
        this.drawTime = drawTime == null ? null : drawTime.trim();
    }

    /**
     * 彩期生成格式。
     * @return format 彩期生成格式。
     */
    public String getFormat() {
        return format;
    }

    /**
     * 彩期生成格式。
     * @param format 彩期生成格式。
     */
    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }

    /**
     * 出票时间段 例如：1|09:00-20:00,2|08:00-22:00 代表周一9点到20点为出票时间，周二8点到22点为出票时间
     * @return come_out_time 出票时间段 例如：1|09:00-20:00,2|08:00-22:00 代表周一9点到20点为出票时间，周二8点到22点为出票时间
     */
    public String getComeOutTime() {
        return comeOutTime;
    }

    /**
     * 出票时间段 例如：1|09:00-20:00,2|08:00-22:00 代表周一9点到20点为出票时间，周二8点到22点为出票时间
     * @param comeOutTime 出票时间段 例如：1|09:00-20:00,2|08:00-22:00 代表周一9点到20点为出票时间，周二8点到22点为出票时间
     */
    public void setComeOutTime(String comeOutTime) {
        this.comeOutTime = comeOutTime == null ? null : comeOutTime.trim();
    }

    /**
     * 排序
     * @return order_id 排序
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * 排序
     * @param orderId 排序
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * 暂停销售限制平台1:Web;2:Wap;3:Android;4:IOS;5其它平台
     * @return platform 暂停销售限制平台1:Web;2:Wap;3:Android;4:IOS;5其它平台
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 暂停销售限制平台1:Web;2:Wap;3:Android;4:IOS;5其它平台
     * @param platform 暂停销售限制平台1:Web;2:Wap;3:Android;4:IOS;5其它平台
     */
    public void setPlatform(String platform) {
        this.platform = platform == null ? null : platform.trim();
    }

    /**
     * 休市时间，用‘,’分隔；例如：2017-01-27,2017-01-28
     * @return vacations 休市时间，用‘,’分隔；例如：2017-01-27,2017-01-28
     */
    public String getVacations() {
        return vacations;
    }

    /**
     * 休市时间，用‘,’分隔；例如：2017-01-27,2017-01-28
     * @param vacations 休市时间，用‘,’分隔；例如：2017-01-27,2017-01-28
     */
    public void setVacations(String vacations) {
        this.vacations = vacations == null ? null : vacations.trim();
    }
}