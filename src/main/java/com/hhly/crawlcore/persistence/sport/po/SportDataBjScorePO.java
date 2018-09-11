package com.hhly.crawlcore.persistence.sport.po;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 单场比分
 */
public class SportDataBjScorePO extends SportDataBjBasePO {
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
     * SP1:0
     */
    @JsonProperty("sp_1_0")
    private Float sp10;

    /**
     * SP2:0
     */
    @JsonProperty("sp_2_0")
    private Float sp20;

    /**
     * SP2:1
     */
    @JsonProperty("sp_2_1")
    private Float sp21;

    /**
     * SP3:0
     */
    @JsonProperty("sp_3_0")
    private Float sp30;

    /**
     * SP3:1
     */
    @JsonProperty("sp_3_1")
    private Float sp31;

    /**
     * SP3:2
     */
    @JsonProperty("sp_3_2")
    private Float sp32;

    /**
     * SP4:0
     */
    @JsonProperty("sp_4_0")
    private Float sp40;

    /**
     * SP4:1
     */
    @JsonProperty("sp_4_1")
    private Float sp41;

    /**
     * SP4:2
     */
    @JsonProperty("sp_4_2")
    private Float sp42;

    /**
     * SP胜其它
     */
    @JsonProperty("sp_w_other")
    private Float spWOther;

    /**
     * SP0:0
     */
    @JsonProperty("sp_0_0")
    private Float sp00;

    /**
     * SP1:1
     */
    @JsonProperty("sp_1_1")
    private Float sp11;

    /**
     * SP2:2
     */
    @JsonProperty("sp_2_2")
    private Float sp22;

    /**
     * SP3:3
     */
    @JsonProperty("sp_3_3")
    private Float sp33;

    /**
     * SP平其它
     */

    @JsonProperty("sp_d_other")
    private Float spDOther;

    /**
     * SP0:1
     */
    @JsonProperty("sp_0_1")
    private Float sp01;

    /**
     * SP0:2
     */
    @JsonProperty("sp_0_2")
    private Float sp02;

    /**
     * SP1:2
     */
    @JsonProperty("sp_1_2")
    private Float sp12;

    /**
     * SP0:3
     */
    @JsonProperty("sp_0_3")
    private Float sp03;

    /**
     * SP1:3
     */
    @JsonProperty("sp_1_3")
    private Float sp13;

    /**
     * SP2:3
     */
    @JsonProperty("sp_2_3")
    private Float sp23;

    /**
     * SP0:4
     */
    @JsonProperty("sp_0_4")
    private Float sp04;

    /**
     * SP1:4
     */
    @JsonProperty("sp_1_4")
    private Float sp14;

    /**
     * SP2:4
     */
    @JsonProperty("sp_2_4")
    private Float sp24;

    /**
     * 负其它
     */
    @JsonProperty("sp_f_other")
    private Float spFOther;



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

    public Float getSp10() {
        return sp10;
    }

    public void setSp10(Float sp10) {
        this.sp10 = sp10;
    }

    public Float getSp20() {
        return sp20;
    }

    public void setSp20(Float sp20) {
        this.sp20 = sp20;
    }

    public Float getSp21() {
        return sp21;
    }

    public void setSp21(Float sp21) {
        this.sp21 = sp21;
    }

    public Float getSp30() {
        return sp30;
    }

    public void setSp30(Float sp30) {
        this.sp30 = sp30;
    }

    public Float getSp31() {
        return sp31;
    }

    public void setSp31(Float sp31) {
        this.sp31 = sp31;
    }

    public Float getSp32() {
        return sp32;
    }

    public void setSp32(Float sp32) {
        this.sp32 = sp32;
    }

    public Float getSp40() {
        return sp40;
    }

    public void setSp40(Float sp40) {
        this.sp40 = sp40;
    }

    public Float getSp41() {
        return sp41;
    }

    public void setSp41(Float sp41) {
        this.sp41 = sp41;
    }

    public Float getSp42() {
        return sp42;
    }

    public void setSp42(Float sp42) {
        this.sp42 = sp42;
    }

    public Float getSpWOther() {
        return spWOther;
    }

    public void setSpWOther(Float spWOther) {
        this.spWOther = spWOther;
    }

    public Float getSp00() {
        return sp00;
    }

    public void setSp00(Float sp00) {
        this.sp00 = sp00;
    }

    public Float getSp11() {
        return sp11;
    }

    public void setSp11(Float sp11) {
        this.sp11 = sp11;
    }

    public Float getSp22() {
        return sp22;
    }

    public void setSp22(Float sp22) {
        this.sp22 = sp22;
    }

    public Float getSp33() {
        return sp33;
    }

    public void setSp33(Float sp33) {
        this.sp33 = sp33;
    }

    public Float getSpDOther() {
        return spDOther;
    }

    public void setSpDOther(Float spDOther) {
        this.spDOther = spDOther;
    }

    public Float getSp01() {
        return sp01;
    }

    public void setSp01(Float sp01) {
        this.sp01 = sp01;
    }

    public Float getSp02() {
        return sp02;
    }

    public void setSp02(Float sp02) {
        this.sp02 = sp02;
    }

    public Float getSp12() {
        return sp12;
    }

    public void setSp12(Float sp12) {
        this.sp12 = sp12;
    }

    public Float getSp03() {
        return sp03;
    }

    public void setSp03(Float sp03) {
        this.sp03 = sp03;
    }

    public Float getSp13() {
        return sp13;
    }

    public void setSp13(Float sp13) {
        this.sp13 = sp13;
    }

    public Float getSp23() {
        return sp23;
    }

    public void setSp23(Float sp23) {
        this.sp23 = sp23;
    }

    public Float getSp04() {
        return sp04;
    }

    public void setSp04(Float sp04) {
        this.sp04 = sp04;
    }

    public Float getSp14() {
        return sp14;
    }

    public void setSp14(Float sp14) {
        this.sp14 = sp14;
    }

    public Float getSp24() {
        return sp24;
    }

    public void setSp24(Float sp24) {
        this.sp24 = sp24;
    }

    public Float getSpFOther() {
        return spFOther;
    }

    public void setSpFOther(Float spFOther) {
        this.spFOther = spFOther;
    }

    @Override
    public String toString() {
        return "SportDataBjScorePO{" +
                "sp10=" + sp10 +
                ", sp20=" + sp20 +
                ", sp21=" + sp21 +
                ", sp30=" + sp30 +
                ", sp31=" + sp31 +
                ", sp32=" + sp32 +
                ", sp40=" + sp40 +
                ", sp41=" + sp41 +
                ", sp42=" + sp42 +
                ", spWOther=" + spWOther +
                ", sp00=" + sp00 +
                ", sp11=" + sp11 +
                ", sp22=" + sp22 +
                ", sp33=" + sp33 +
                ", spDOther=" + spDOther +
                ", sp01=" + sp01 +
                ", sp02=" + sp02 +
                ", sp12=" + sp12 +
                ", sp03=" + sp03 +
                ", sp13=" + sp13 +
                ", sp23=" + sp23 +
                ", sp04=" + sp04 +
                ", sp14=" + sp14 +
                ", sp24=" + sp24 +
                ", spFOther=" + spFOther +
                ", issueCode='" + this.getIssueCode() + '\'' +
                ", lotteryCode=" + this.getLotteryCode() +
                ", bjNum=" + this.getBjNum() +
                '}';
    }
}