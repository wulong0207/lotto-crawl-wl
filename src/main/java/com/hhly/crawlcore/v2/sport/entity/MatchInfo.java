
package com.hhly.crawlcore.v2.sport.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年12月15日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class MatchInfo {

	/**
	 * 赛程id
	 */
	@JSONField(name = "match_id")
	private Long id;
	
	/**
	 * sp值,资讯id
	 */
	@JSONField(name = "official_id")
	private String newsId;
	
	/**
	 * 分析id
	 */
	private Long analysisId;
	
	/**
	 * 彩种编码
	 */
	private Integer lotteryCode;
	
	/**
	 * 彩期编码
	 */
	private String issueCode;
	
	/**
	 * 官网赛事编码
	 */
	private String officialMatchCode;
	
	/**
	 * 联赛id
	 */
	private Long leagueId;
	
	/**
	 * 联赛名称
	 */
	@JSONField(name = "match_name")
	private String leagueName;
	
	/**
	 * 联赛简称
	 */
	private String leagueAbbrName;
	
	/**
	 * 主队id
	 */
	private Long homeId;
	
	/**
	 * 主队全称
	 */
	private String homeName;
	
	/**
	 * 主队简称
	 */
	private String homeAbbrName;
	
	/**
	 * 客队id
	 */
	private Long awayId;
	
	/**
	 * 客队全称
	 */
	private String awayName;
	
	/**
	 * 客队简称
	 */
	private String awayAbbrName;	
	
	/**
	 * 赛程状态
	 */
	@JSONField(name = "match_status")
	private Short status;
	
	/**
	 * 比赛时间
	 */
	private Date startTime;
	
	/**
	 * 销售时间
	 */
	private Date saleDate;
	
	/**
	 * 销售截止时间
	 */
	private Date saleEndTime;
	
	/**
	 * 赛事排序
	 */
	private Integer matchIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public Long getAnalysisId() {
		return analysisId;
	}

	public void setAnalysisId(Long analysisId) {
		this.analysisId = analysisId;
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
		this.issueCode = issueCode;
	}

	public String getOfficialMatchCode() {
		return officialMatchCode;
	}

	public void setOfficialMatchCode(String officialMatchCode) {
		this.officialMatchCode = officialMatchCode;
	}

	public Long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getLeagueAbbrName() {
		return leagueAbbrName;
	}

	public void setLeagueAbbrName(String leagueAbbrName) {
		this.leagueAbbrName = leagueAbbrName;
	}

	public Long getHomeId() {
		return homeId;
	}

	public void setHomeId(Long homeId) {
		this.homeId = homeId;
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

	public Long getAwayId() {
		return awayId;
	}

	public void setAwayId(Long awayId) {
		this.awayId = awayId;
	}

	public String getAwayName() {
		return awayName;
	}

	public void setAwayName(String awayName) {
		this.awayName = awayName;
	}

	public String getAwayAbbrName() {
		return awayAbbrName;
	}

	public void setAwayAbbrName(String awayAbbrName) {
		this.awayAbbrName = awayAbbrName;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Date getSaleEndTime() {
		return saleEndTime;
	}

	public void setSaleEndTime(Date saleEndTime) {
		this.saleEndTime = saleEndTime;
	}

	public Integer getMatchIndex() {
		return matchIndex;
	}

	public void setMatchIndex(Integer matchIndex) {
		this.matchIndex = matchIndex;
	}
}
