package com.hhly.crawlcore.persistence.sport.po;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 半全场胜平负
 */
public class SportDataBjHfWdfPO extends SportDataBjBasePO {
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
     * SP胜胜
     */
    @JsonProperty("sp_w_w")
    private Float spWW;

    /**
     * SP胜平
     */
    @JsonProperty("sp_w_d")
    private Float spWD;

    /**
     * SP胜负
     */
    @JsonProperty("sp_w_f")
    private Float spWF;

    /**
     * SP平胜
     */
    @JsonProperty("sp_d_w")
    private Float spDW;

    /**
     * SP平平
     */
    @JsonProperty("sp_d_d")
    private Float spDD;

    /**
     * SP平负
     */
    @JsonProperty("sp_d_f")
    private Float spDF;

    /**
     * SP负胜
     */

    @JsonProperty("sp_f_w")
    private Float spFW;


    /**
     * SP负平
     */
    @JsonProperty("sp_f_d")
    private Float spFD;

    /**
     * SP负负
     */
    @JsonProperty("sp_f_f")
    private Float spFF;

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

    public Float getSpWW() {
        return spWW;
    }

    public void setSpWW(Float spWW) {
        this.spWW = spWW;
    }

    public Float getSpWD() {
        return spWD;
    }

    public void setSpWD(Float spWD) {
        this.spWD = spWD;
    }

    public Float getSpWF() {
        return spWF;
    }

    public void setSpWF(Float spWF) {
        this.spWF = spWF;
    }

    public Float getSpDW() {
        return spDW;
    }

    public void setSpDW(Float spDW) {
        this.spDW = spDW;
    }

    public Float getSpDD() {
        return spDD;
    }

    public void setSpDD(Float spDD) {
        this.spDD = spDD;
    }

    public Float getSpDF() {
        return spDF;
    }

    public void setSpDF(Float spDF) {
        this.spDF = spDF;
    }

    public Float getSpFW() {
        return spFW;
    }

    public void setSpFW(Float spFW) {
        this.spFW = spFW;
    }

    public Float getSpFD() {
        return spFD;
    }

    public void setSpFD(Float spFD) {
        this.spFD = spFD;
    }

    public Float getSpFF() {
        return spFF;
    }

    public void setSpFF(Float spFF) {
        this.spFF = spFF;
    }



    @Override
    public String toString() {
        return new StringBuilder().append("SportDataBjHfWdfPO{").
                append("spWW=").append(spWW).
                append(", spWD=").append(spWD).
                append(", spWF=").append(spWF).
                append(", spDW=").append(spDW).
                append(", spDD=").append(spDD).
                append(", spDF=").append(spDF).
                append(", spFW=").append(spFW).
                append(", spFD=").append(spFD).
                append(", spFF=").append(spFF).
                append(", issueCode='").append(this.getIssueCode()).append('\'').
                append(", lotteryCode=").append(this.getLotteryCode()).append(", bjNum=").append(this.getBjNum()).append('}').toString();
    }
}