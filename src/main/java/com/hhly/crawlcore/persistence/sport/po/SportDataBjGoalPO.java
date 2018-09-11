package com.hhly.crawlcore.persistence.sport.po;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 总进球
 */
public class SportDataBjGoalPO extends SportDataBjBasePO {
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
     * 总进球0SP值
     */
    @JsonProperty("sp_0_goal")
    private Float sp0Goal;

    /**
     * 总进球1SP值
     */
    @JsonProperty("sp_1_goal")
    private Float sp1Goal;

    /**
     * 总进球2SP
     */
    @JsonProperty("sp_2_goal")
    private Float sp2Goal;

    /**
     * 总进球3SP
     */
    @JsonProperty("sp_3_goal")
    private Float sp3Goal;

    /**
     * 总进球4SP
     */
    @JsonProperty("sp_4_goal")
    private Float sp4Goal;

    /**
     * 总进球5SP
     */
    @JsonProperty("sp_5_goal")
    private Float sp5Goal;

    /**
     * 总进球6SP
     */
    @JsonProperty("sp_6_goal")
    private Float sp6Goal;

    /**
     * 总进球7+SP
     */
    @JsonProperty("sp_7_goal")
    private Float sp7Goal;


    public SportDataBjGoalPO() {
    }

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

    public Float getSp0Goal() {
        return sp0Goal;
    }

    public void setSp0Goal(Float sp0Goal) {
        this.sp0Goal = sp0Goal;
    }

    public Float getSp1Goal() {
        return sp1Goal;
    }

    public void setSp1Goal(Float sp1Goal) {
        this.sp1Goal = sp1Goal;
    }

    public Float getSp2Goal() {
        return sp2Goal;
    }

    public void setSp2Goal(Float sp2Goal) {
        this.sp2Goal = sp2Goal;
    }

    public Float getSp3Goal() {
        return sp3Goal;
    }

    public void setSp3Goal(Float sp3Goal) {
        this.sp3Goal = sp3Goal;
    }

    public Float getSp4Goal() {
        return sp4Goal;
    }

    public void setSp4Goal(Float sp4Goal) {
        this.sp4Goal = sp4Goal;
    }

    public Float getSp5Goal() {
        return sp5Goal;
    }

    public void setSp5Goal(Float sp5Goal) {
        this.sp5Goal = sp5Goal;
    }

    public Float getSp6Goal() {
        return sp6Goal;
    }

    public void setSp6Goal(Float sp6Goal) {
        this.sp6Goal = sp6Goal;
    }

    public Float getSp7Goal() {
        return sp7Goal;
    }

    public void setSp7Goal(Float sp7Goal) {
        this.sp7Goal = sp7Goal;
    }


    @Override
    public String toString() {
        return new StringBuilder().append("SportDataBjGoalPO{").append("sp0Goal=").append(sp0Goal).append(", sp1Goal=").append(sp1Goal).append(", sp2Goal=").append(sp2Goal).append(", sp3Goal=").append(sp3Goal).append(", sp4Goal=").append(sp4Goal).append(", sp5Goal=").append(sp5Goal).append(", sp6Goal=").append(sp6Goal).append(", sp7Goal=").append(sp7Goal).append(", issueCode='").append(this.getIssueCode()).append('\'').append(", lotteryCode=").append(this.getLotteryCode()).append(", bjNum=").append(this.getBjNum()).append('}').toString();
    }


}