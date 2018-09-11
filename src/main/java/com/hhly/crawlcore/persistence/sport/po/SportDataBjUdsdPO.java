package com.hhly.crawlcore.persistence.sport.po;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 上下盘单双数
 */
public class SportDataBjUdsdPO extends SportDataBjBasePO {
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
     * SP上单
     */
    @JsonProperty("sp_up_single")
    private Float spUpSingle;

    /**
     * SP上双
     */
    @JsonProperty("sp_up_double")
    private Float spUpDouble;

    /**
     * SP下单
     */
    @JsonProperty("sp_down_single")
    private Float spDownSingle;

    /**
     * SP下双
     */

    @JsonProperty("sp_down_double")
    private Float spDownDouble;



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

    public Float getSpUpSingle() {
        return spUpSingle;
    }

    public void setSpUpSingle(Float spUpSingle) {
        this.spUpSingle = spUpSingle;
    }

    public Float getSpUpDouble() {
        return spUpDouble;
    }

    public void setSpUpDouble(Float spUpDouble) {
        this.spUpDouble = spUpDouble;
    }

    public Float getSpDownSingle() {
        return spDownSingle;
    }

    public void setSpDownSingle(Float spDownSingle) {
        this.spDownSingle = spDownSingle;
    }

    public Float getSpDownDouble() {
        return spDownDouble;
    }

    public void setSpDownDouble(Float spDownDouble) {
        this.spDownDouble = spDownDouble;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("SportDataBjUdsdPO{").append("spUpSingle=").append(spUpSingle).append(", spUpDouble=").append(spUpDouble).append(", spDownSingle=").append(spDownSingle).append(", spDownDouble=").append(spDownDouble).append(", issueCode='").append(this.getIssueCode()).append('\'').append(", lotteryCode=").append(this.getLotteryCode()).append(", bjNum=").append(this.getBjNum()).append('}').toString();
    }
}