package com.hhly.crawlcore.persistence.sport.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hhly.crawlcore.sport.entity.YbfJcFbMatch;

public class SportAgainstInfoPO implements Serializable {
	
	private static final long serialVersionUID = 3711721545898699513L;

	private Long id;

    private Integer lotteryCode;

    private String issueCode;
    
    private Integer fbScheduleId;

    private Long sportMatchInfoId;

    private Long homeTeamId;

    private Long visitiTeamId;

    private String matchName;

    private String homeName;

    private String visitiName;

    private String matchAnalysisUrl;

    private String matchInfoUrl;

    private Short isRecommend;

    private String matchLabelColor;

    private String officialMatchCode;

    private String systemCode;

    private Short matchStatus;

    private String officialId;

    private Date startTime;

    private Date saleEndTime;

    private Short isNeutral = 0; //默认不是中立

    private String stadium;

    private String weather;

    private Float oddsWin;

    private Float oddsDraw;

    private Float oddsFail;

    private String modifyBy;

    private Date modifyTime;

    private String createBy;

    private Date updateTime;

    private Date createTime;

    private String remark;

    private Long oldMatchIndex;

    private Date saleDate;

    private Integer bjNum;
    
    /**
     * 得奖概率
     */
    private String winProb;

    private String round;

    private String gameweek;

    private String statusLetWdf;
    private String statusHfWdf;
    private String statusScore;
    private String statusGoal;
    private String statusWdf;

    /**
     * 500竞足让分胜平负历史SP
     */
    private String letwdfSp;
    /**
     * 500竞足胜平负历史SP
     */
    private String wdfSp;


    /**
     * 比赛类型 编号
     */
    private Short matchType;
    /**
     * 比赛类型名称 例如足球，篮球。网球
     */
    private String matchTypeName;

    private List historySp;


    public List getHistorySp() {
        return historySp;
    }

    public String getStatusLetWdf() {
        return statusLetWdf;
    }

    public void setStatusLetWdf(String statusLetWdf) {
        this.statusLetWdf = statusLetWdf;
    }

    public String getStatusHfWdf() {
        return statusHfWdf;
    }

    public void setStatusHfWdf(String statusHfWdf) {
        this.statusHfWdf = statusHfWdf;
    }

    public String getStatusScore() {
        return statusScore;
    }

    public void setStatusScore(String statusScore) {
        this.statusScore = statusScore;
    }

    public String getStatusGoal() {
        return statusGoal;
    }

    public void setStatusGoal(String statusGoal) {
        this.statusGoal = statusGoal;
    }

    public String getStatusWdf() {
        return statusWdf;
    }

    public void setStatusWdf(String statusWdf) {
        this.statusWdf = statusWdf;
    }

    public void setHistorySp(List historySp) {
        this.historySp = historySp;
    }

    public String getLetwdfSp() {
        return letwdfSp;
    }

    public void setLetwdfSp(String letwdfSp) {
        this.letwdfSp = letwdfSp;
    }

    public String getWdfSp() {
        return wdfSp;
    }

    public void setWdfSp(String wdfSp) {
        this.wdfSp = wdfSp;
    }

    public SportAgainstInfoPO() {
    }

    public SportAgainstInfoPO(Integer lotteryCode, String issueCode, String officialMatchCode) {
        this.lotteryCode = lotteryCode;
        this.issueCode = issueCode;
        this.officialMatchCode = officialMatchCode;
    }

    /**
     * 暂定赛程插入数据po
     * @param id
     * @param lotteryCode
     * @param matchName
     * @param homeName
     * @param visitiName
     * @param matchStatus
     * @param startTime
     * @param modifyBy
     * @param modifyTime
     * @param createBy
     * @param updateTime
     * @param createTime
     */
    public SportAgainstInfoPO(Long id, Integer lotteryCode, String matchName, String homeName, String visitiName, Short matchStatus, Date startTime, String modifyBy, String createBy) {
        this.id = id;
        this.lotteryCode = lotteryCode;
        this.matchName = matchName;
        this.homeName = homeName;
        this.visitiName = visitiName;
        this.matchStatus = matchStatus;
        this.startTime = startTime;
        this.modifyBy = modifyBy;
        this.createBy = createBy;
    }

    /**
     * 老足彩插入数据PO
     * @param lotteryCode
     * @param issueCode
     * @param matchName
     * @param homeName
     * @param visitiName
     * @param homeTeamId
     * @param visitiTeamId
     * @param sportMatchInfoId
     * @param systemCode
     * @param matchStatus
     * @param officialId
     * @param startTime
     * @param saleEndTime
     * @param modifyBy
     * @param modifyTime
     * @param createBy
     * @param updateTime
     * @param createTime
     */
    public SportAgainstInfoPO(Long id, Integer lotteryCode, String issueCode, String matchName, String homeName, String visitiName, Long homeTeamId, Long visitiTeamId, Long sportMatchInfoId, String systemCode, Short matchStatus, String officialId, Date startTime, Date saleEndTime, Long oldMatchIndex, String modifyBy, String createBy) {
    	this.id = id;
    	this.lotteryCode = lotteryCode;
        this.issueCode = issueCode;
        this.matchName = matchName;
        this.homeName = homeName;
        this.visitiName = visitiName;
        this.homeTeamId = homeTeamId;
        this.visitiTeamId = visitiTeamId;
        this.sportMatchInfoId = sportMatchInfoId;
        this.systemCode = systemCode;
        this.matchStatus = matchStatus;
        this.officialId = officialId;
        this.startTime = startTime;
        this.saleEndTime = saleEndTime;
        this.oldMatchIndex = oldMatchIndex;
        this.isRecommend = 0;
        this.modifyBy = modifyBy;
        this.createBy = createBy;
    }

    /**
     * 竞足, 竞蓝插入数据
     * @param id
     * @param lotteryCode
     * @param issueCode
     * @param sportMatchInfoId
     * @param homeTeamId
     * @param visitiTeamId
     * @param matchName
     * @param homeName
     * @param visitiName
     * @param officialMatchCode
     * @param systemCode
     * @param matchStatus
     * @param officialId
     * @param startTime
     * @param saleEndTime
     * @param modifyBy
     * @param createBy
     * @param saleDate
     */
    public SportAgainstInfoPO(Long id,Integer lotteryCode, String issueCode, Long sportMatchInfoId, Long homeTeamId, Long visitiTeamId, String matchName, String homeName, String visitiName, String officialMatchCode, String systemCode, Short matchStatus, String officialId, Date startTime, Date saleEndTime, String modifyBy, String createBy,Date saleDate) {
        this.id = id;
    	this.lotteryCode = lotteryCode;
        this.issueCode = issueCode;
        this.sportMatchInfoId = sportMatchInfoId;
        this.homeTeamId = homeTeamId;
        this.visitiTeamId = visitiTeamId;
        this.matchName = matchName;
        this.homeName = homeName;
        this.visitiName = visitiName;
        this.officialMatchCode = officialMatchCode;
        this.systemCode = systemCode;
        this.matchStatus = matchStatus;
        this.officialId = officialId;
        this.startTime = startTime;
        this.saleDate = saleDate;
        this.saleEndTime = saleEndTime;
        this.isRecommend = 0;
        this.modifyBy = modifyBy;
        this.createBy = createBy;
    }


    public SportAgainstInfoPO(YbfJcFbMatch match) {
        this.matchAnalysisUrl = match.getScheduleId();
        this.matchInfoUrl = match.getId();
        this.officialId = match.getGwId();
    }


    public SportAgainstInfoPO(SportDataBjBasePO po) {
        this.lotteryCode = po.getLotteryCode();
        this.issueCode = po.getIssueCode();
        this.matchName = po.getMatchName();
        this.homeName = po.getHomeName();
        this.visitiName = po.getVisitiName();
        this.startTime = po.getStartTime();
        this.saleDate = po.getSaleDate();
        this.bjNum = po.getBjNum();
        this.matchTypeName = po.getMatchType();
        this.matchStatus = Integer.valueOf(po.getMatchStatus()).shortValue();
        this.createBy = po.getCreateBy();
    }

    public String getMatchTypeName() {
        return matchTypeName;
    }

    public void setMatchTypeName(String matchTypeName) {
        this.matchTypeName = matchTypeName;
    }

    public Short getMatchType() {
        return matchType;
    }

    public void setMatchType(Short matchType) {
        this.matchType = matchType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    
    public Integer getFbScheduleId() {
		return fbScheduleId;
	}

	public void setFbScheduleId(Integer fbScheduleId) {
		this.fbScheduleId = fbScheduleId;
	}

	public Long getSportMatchInfoId() {
        return sportMatchInfoId;
    }

    public void setSportMatchInfoId(Long sportMatchInfoId) {
        this.sportMatchInfoId = sportMatchInfoId;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Long getVisitiTeamId() {
        return visitiTeamId;
    }

    public void setVisitiTeamId(Long visitiTeamId) {
        this.visitiTeamId = visitiTeamId;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getVisitiName() {
        return visitiName;
    }

    public void setVisitiName(String visitiName) {
        this.visitiName = visitiName;
    }

    public String getMatchAnalysisUrl() {
        return matchAnalysisUrl;
    }

    public void setMatchAnalysisUrl(String matchAnalysisUrl) {
        this.matchAnalysisUrl = matchAnalysisUrl == null ? null : matchAnalysisUrl.trim();
    }

    public String getMatchInfoUrl() {
        return matchInfoUrl;
    }

    public void setMatchInfoUrl(String matchInfoUrl) {
        this.matchInfoUrl = matchInfoUrl == null ? null : matchInfoUrl.trim();
    }

    public Short getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Short isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getMatchLabelColor() {
        return matchLabelColor;
    }

    public void setMatchLabelColor(String matchLabelColor) {
        this.matchLabelColor = matchLabelColor == null ? null : matchLabelColor.trim();
    }

    public String getOfficialMatchCode() {
        return officialMatchCode;
    }

    public void setOfficialMatchCode(String officialMatchCode) {
        this.officialMatchCode = officialMatchCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }

    public Short getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Short matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getOfficialId() {
        return officialId;
    }

    public void setOfficialId(String officialId) {
        this.officialId = officialId == null ? null : officialId.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(Date saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    public Short getIsNeutral() {
        return isNeutral;
    }

    public void setIsNeutral(Short isNeutral) {
        this.isNeutral = isNeutral;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOldMatchIndex() {
        return oldMatchIndex;
    }

    public void setOldMatchIndex(Long oldMatchIndex) {
        this.oldMatchIndex = oldMatchIndex;
    }

    public Float getOddsWin() {
        return oddsWin;
    }

    public void setOddsWin(Float oddsWin) {
        this.oddsWin = oddsWin;
    }

    public Float getOddsDraw() {
        return oddsDraw;
    }

    public void setOddsDraw(Float oddsDraw) {
        this.oddsDraw = oddsDraw;
    }

    public Float getOddsFail() {
        return oddsFail;
    }

    public void setOddsFail(Float oddsFail) {
        this.oddsFail = oddsFail;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Integer getBjNum() {
        return bjNum;
    }

    public void setBjNum(Integer bjNum) {
        this.bjNum = bjNum;
    }
    
    public String getWinProb() {
		return winProb;
	}

	public void setWinProb(String winProb) {
		this.winProb = winProb;
	}

	public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getGameweek() {
        return gameweek;
    }

    public void setGameweek(String gameweek) {
        this.gameweek = gameweek;
    }
}