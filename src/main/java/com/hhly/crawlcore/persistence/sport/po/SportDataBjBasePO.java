package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lgs on
 * @version 1.0
 * @desc 北京单场公共类
 * @date 2017/6/15.
 * @company 益彩网络科技有限公司
 */
public class SportDataBjBasePO {

    /**
     * MD5类 toString 转换MD5值保证唯一 用于比较
     */
    private String md5Value;

    /**
     * 彩期号
     */
    @JsonProperty("issue_code")
    private String issueCode;

    /**
     * 彩种号
     */
    @JsonProperty("lottery_code")
    private Integer lotteryCode;

    /**
     * 北京单场号
     */
    @JsonProperty("bj_num")
    private Integer bjNum;

    @JsonProperty("home_name")
    private String homeName;
    
    /**
     * 主队名称简称
     */
    private String homeAbbrName;


    @JsonProperty("visit_name")
    private String visitiName;

    /**
     * 客队名称简称
     */
    private String visitiAbbrName;
    
    @JsonProperty("match_name")
    private String matchName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("start_time")
    private Date startTime;

    /**
     * 发布时间
     */
    @JsonProperty("release_time")
    private Date releaseTime;

    /**
     * 更新时间
     */
    @JsonProperty("update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @JsonProperty("createTime")
    private Date createTime;

    @JsonProperty("match_type")
    private String matchType;

    @JsonProperty("sale_date")
    private Date saleDate;

    @JsonProperty("match_status")
    private String matchStatus;

    @JsonProperty("match_color_label")
    private String matchColorLabel;

    @JsonProperty("create_by")
    private String createBy;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getMatchColorLabel() {
        return matchColorLabel;
    }

    public void setMatchColorLabel(String matchColorLabel) {
        this.matchColorLabel = matchColorLabel;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    public String getIssueCode() {
        return issueCode;
    }

    public void setIssueCode(String issueCode) {
        this.issueCode = issueCode;
    }

    public Integer getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
    }


    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public Integer getBjNum() {
        return bjNum;
    }

    public void setBjNum(Integer bjNum) {
        this.bjNum = bjNum;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeAbbrName() {
		return homeAbbrName;
	}

	public void setHomeAbbrName(String homeAbbrName) {
		this.homeAbbrName = homeAbbrName;
	}

	public String getVisitiName() {
        return visitiName;
    }

    public void setVisitiName(String visitiName) {
        this.visitiName = visitiName;
    }

    public String getVisitiAbbrName() {
		return visitiAbbrName;
	}

	public void setVisitiAbbrName(String visitiAbbrName) {
		this.visitiAbbrName = visitiAbbrName;
	}

	public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
}
