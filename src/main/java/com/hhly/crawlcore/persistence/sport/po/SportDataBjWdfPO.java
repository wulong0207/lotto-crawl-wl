package com.hhly.crawlcore.persistence.sport.po;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 让球胜平负
 */
public class SportDataBjWdfPO extends SportDataBjBasePO {
    /**
     * null
     */
    @JsonProperty("id")
    private Long id;

    /**
     * 竞技彩对阵详情表ID
     */
    @JsonProperty("sport_against_info_id")
    private Long sportAgainstInfoId;

    /**
     * 让球值；例如：-1
     */
    @JsonProperty("let_num")
    private Float letNum;

    /**
     * 胜的赔率；例如：2.21
     */
    @JsonProperty("sp_win")
    private Float spWin;

    /**
     * 平的赔率；例如：2.21
     */
    @JsonProperty("sp_draw")
    private Float spDraw;

    /**
     * 负的赔率；例如：2.21
     */
    @JsonProperty("sp_fail")
    private Float spFail;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSportAgainstInfoId() {
        return sportAgainstInfoId;
    }

    public void setSportAgainstInfoId(Long sportAgainstInfoId) {
        this.sportAgainstInfoId = sportAgainstInfoId;
    }

    public Float getLetNum() {
        return letNum;
    }

    public void setLetNum(Float letNum) {
        this.letNum = letNum;
    }

    public Float getSpWin() {
        return spWin;
    }

    public void setSpWin(Float spWin) {
        this.spWin = spWin;
    }

    public Float getSpDraw() {
        return spDraw;
    }

    public void setSpDraw(Float spDraw) {
        this.spDraw = spDraw;
    }

    public Float getSpFail() {
        return spFail;
    }

    public void setSpFail(Float spFail) {
        this.spFail = spFail;
    }


    @Override
    public String toString() {
        return new StringBuilder().append("SportDataBjWdfPO{").append("letNum=").append(letNum).append(", spWin=").append(spWin).append(", spDraw=").append(spDraw).append(", spFail=").append(spFail).append(", issueCode='").append(this.getIssueCode()).append('\'').append(", lotteryCode=").append(this.getLotteryCode()).append(", bjNum=").append(this.getBjNum()).append('}').toString();
    }
}