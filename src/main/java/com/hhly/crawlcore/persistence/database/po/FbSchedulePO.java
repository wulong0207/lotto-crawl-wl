package com.hhly.crawlcore.persistence.database.po;

import java.util.Date;

public class FbSchedulePO {
    /**
     * 赛程(对阵)id
     */
    private Integer id;

    /**
     * 赛事类型id标识 如:   英超id
     */
    private Integer sclassId;

    /**
     * 循环类型id
     */
    private Integer roundId;

    /**
     * 是否中立球场:1:是,0:否,默认:0
     */
    private Integer isneutral;

    /**
     * 主队编号id
     */
    private Integer homeTeamId;

    /**
     * 客队编号id
     */
    private Integer guestTeamId;

    /**
     * 比赛的时间
     */
    private Date matchTime;

    /**
     * 是否推迟比赛1:是,0:否,默认:0
     */
    private Integer isdelayed;

    /**
     * 最终比赛时间,isdelayed为1时,使用
     */
    private Date finalTime;

    /**
     * 比赛的地点
     */
    private String location;

    /**
     * 主队上半场得分
     */
    private Short homeFHalf;

    /**
     * 主队下半场得分
     */
    private Short homeSHalf;

    /**
     * 客队上半场得分
     */
    private Short guestFHalf;

    /**
     * 客队下半场得分
     */
    private Short guestSHalf;

    /**
     * 主队上半场加时得分
     */
    private Short homeFAdd;

    /**
     * 主队下半场加时得分
     */
    private Short homeSAdd;

    /**
     * 客队上半场加时得分
     */
    private Short guestFAdd;

    /**
     * 客队下半场加时得分
     */
    private Short guestSAdd;

    /**
     * 主队总得分
     */
    private Short homeScore;

    /**
     * 客队总得分
     */
    private Short guestScore;

    /**
     * 比赛状态0:未;1:上半场:2:下半场;3:上半场加时;4:下半场加时;5:点球决胜;50:中场休息;99:终场(增加状态类别后面补充)
     */
    private Short matchStatus;

    /**
     * 主队红牌数
     */
    private Byte homeRd;

    /**
     * 客队红牌数
     */
    private Byte guestRd;

    /**
     * 主队黄牌数
     */
    private Byte homeYl;

    /**
     * 客队黄牌数
     */
    private Byte guestYl;

    /**
     * 直播电视台
     */
    private String tv;

    /**
     * 当值裁判员
     */
    private String umpire;

    /**
     * 参观人数
     */
    private Integer visitor;

    /**
     * 数据来源方id
     */
    private String sourceId;

    /**
     * 数据来源方类型:1:一比分
     */
    private Integer sourceType;

    /**
     * 所属赛季
     */
    private String season;

    /**
     * 当前轮次
     */
    private Short rounds;

    /**
     * 主队排名(当前赛事)
     */
    private Integer homeRanking;

    /**
     * 客队排名(当前赛事)
     */
    private Integer guestRanking;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 后台操作员ID
     */
    private String modifyBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSclassId() {
        return sclassId;
    }

    public void setSclassId(Integer sclassId) {
        this.sclassId = sclassId;
    }

    public Integer getRoundId() {
        return roundId;
    }

    public void setRoundId(Integer roundId) {
        this.roundId = roundId;
    }

    public Integer getIsneutral() {
        return isneutral;
    }

    public void setIsneutral(Integer isneutral) {
        this.isneutral = isneutral;
    }

    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Integer homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Integer getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(Integer guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public Integer getIsdelayed() {
        return isdelayed;
    }

    public void setIsdelayed(Integer isdelayed) {
        this.isdelayed = isdelayed;
    }

    public Date getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(Date finalTime) {
        this.finalTime = finalTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public Short getHomeFHalf() {
        return homeFHalf;
    }

    public void setHomeFHalf(Short homeFHalf) {
        this.homeFHalf = homeFHalf;
    }

    public Short getHomeSHalf() {
        return homeSHalf;
    }

    public void setHomeSHalf(Short homeSHalf) {
        this.homeSHalf = homeSHalf;
    }

    public Short getGuestFHalf() {
        return guestFHalf;
    }

    public void setGuestFHalf(Short guestFHalf) {
        this.guestFHalf = guestFHalf;
    }

    public Short getGuestSHalf() {
        return guestSHalf;
    }

    public void setGuestSHalf(Short guestSHalf) {
        this.guestSHalf = guestSHalf;
    }

    public Short getHomeFAdd() {
        return homeFAdd;
    }

    public void setHomeFAdd(Short homeFAdd) {
        this.homeFAdd = homeFAdd;
    }

    public Short getHomeSAdd() {
        return homeSAdd;
    }

    public void setHomeSAdd(Short homeSAdd) {
        this.homeSAdd = homeSAdd;
    }

    public Short getGuestFAdd() {
        return guestFAdd;
    }

    public void setGuestFAdd(Short guestFAdd) {
        this.guestFAdd = guestFAdd;
    }

    public Short getGuestSAdd() {
        return guestSAdd;
    }

    public void setGuestSAdd(Short guestSAdd) {
        this.guestSAdd = guestSAdd;
    }

    public Short getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Short homeScore) {
        this.homeScore = homeScore;
    }

    public Short getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(Short guestScore) {
        this.guestScore = guestScore;
    }

    public Short getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Short matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Byte getHomeRd() {
        return homeRd;
    }

    public void setHomeRd(Byte homeRd) {
        this.homeRd = homeRd;
    }

    public Byte getGuestRd() {
        return guestRd;
    }

    public void setGuestRd(Byte guestRd) {
        this.guestRd = guestRd;
    }

    public Byte getHomeYl() {
        return homeYl;
    }

    public void setHomeYl(Byte homeYl) {
        this.homeYl = homeYl;
    }

    public Byte getGuestYl() {
        return guestYl;
    }

    public void setGuestYl(Byte guestYl) {
        this.guestYl = guestYl;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv == null ? null : tv.trim();
    }

    public String getUmpire() {
        return umpire;
    }

    public void setUmpire(String umpire) {
        this.umpire = umpire == null ? null : umpire.trim();
    }

    public Integer getVisitor() {
        return visitor;
    }

    public void setVisitor(Integer visitor) {
        this.visitor = visitor;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId == null ? null : sourceId.trim();
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season == null ? null : season.trim();
    }

    public Short getRounds() {
        return rounds;
    }

    public void setRounds(Short rounds) {
        this.rounds = rounds;
    }

    public Integer getHomeRanking() {
        return homeRanking;
    }

    public void setHomeRanking(Integer homeRanking) {
        this.homeRanking = homeRanking;
    }

    public Integer getGuestRanking() {
        return guestRanking;
    }

    public void setGuestRanking(Integer guestRanking) {
        this.guestRanking = guestRanking;
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
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}