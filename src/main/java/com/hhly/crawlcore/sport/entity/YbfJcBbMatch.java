package com.hhly.crawlcore.sport.entity;

import com.hhly.skeleton.base.bo.BaseBO;

/**
 * 益彩网络用于竞彩篮球相关数据
 *
 * @author YUSHOULING
 * @date 2017-6-20
 */
public class YbfJcBbMatch extends BaseBO {

    private static final long serialVersionUID = 1L;

    private String officialId;// 官网对阵id
    private Long analysisId;// 分析id
    private String homeDataId;// 主队id，如27_0_1
    private String guestDataId;// 客队id，如16_0_1
    private String matchDataId;// 赛事id，如1_1_0
    private String homeRank;// 主队排名，如“东1”，“西1”，没有直接给排名
    private String guestRank;// 客队排名
    private String homeName;// 主队名称
    private String guestName;// 客队名称
    private String homeLogo;// 主队logo
    private String guestLogo;// 客队logo
    private String matchLogo;// 赛事logo
    private String matchAdvicesId;// 赛事推荐页面id
    private String lotteryDate;//官方抓取的日期，用于处理彩期
    private String lotteryNumber;//官方赛事编码：周四301

    public String getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(String lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public String getLotteryNumber() {
        return lotteryNumber;
    }

    public void setLotteryNumber(String lotteryNumber) {
        this.lotteryNumber = lotteryNumber;
    }

    public String getOfficialId() {
        return officialId;
    }

    public void setOfficialId(String officialId) {
        this.officialId = officialId;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public String getHomeDataId() {
        return homeDataId;
    }

    public void setHomeDataId(String homeDataId) {
        this.homeDataId = homeDataId;
    }

    public String getGuestDataId() {
        return guestDataId;
    }

    public void setGuestDataId(String guestDataId) {
        this.guestDataId = guestDataId;
    }

    public String getMatchDataId() {
        return matchDataId;
    }

    public void setMatchDataId(String matchDataId) {
        this.matchDataId = matchDataId;
    }

    public String getHomeRank() {
        return homeRank;
    }

    public void setHomeRank(String homeRank) {
        this.homeRank = homeRank;
    }

    public String getGuestRank() {
        return guestRank;
    }

    public void setGuestRank(String guestRank) {
        this.guestRank = guestRank;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getHomeLogo() {
        return homeLogo;
    }

    public void setHomeLogo(String homeLogo) {
        this.homeLogo = homeLogo;
    }

    public String getGuestLogo() {
        return guestLogo;
    }

    public void setGuestLogo(String guestLogo) {
        this.guestLogo = guestLogo;
    }

    public String getMatchLogo() {
        return matchLogo;
    }

    public void setMatchLogo(String matchLogo) {
        this.matchLogo = matchLogo;
    }

    public String getMatchAdvicesId() {
        return matchAdvicesId;
    }

    public void setMatchAdvicesId(String matchAdvicesId) {
        this.matchAdvicesId = matchAdvicesId;
    }

}
