package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SportDataBjSpPO {
    private Integer id;

    @JsonProperty("sport_against_info_id")
    private Long sportAgainstInfoId;

    @JsonProperty("sp_0_goal")
    private Float sp0Goal;

    @JsonProperty("sp_1_goal")
    private Float sp1Goal;
    @JsonProperty("sp_2_goal")
    private Float sp2Goal;

    @JsonProperty("sp_3_goal")
    private Float sp3Goal;

    @JsonProperty("sp_4_goal")
    private Float sp4Goal;

    @JsonProperty("s_p5_goal")
    private Float sp5Goal;

    @JsonProperty("sp_6_goal")
    private Float sp6Goal;

    @JsonProperty("sp_7_goal")
    private Float sp7Goal;

    @JsonProperty("sp_w_w")
    private Float spWW;

    @JsonProperty("sp_w_d")
    private Float spWD;

    @JsonProperty("sp_w_f")
    private Float spWF;

    @JsonProperty("sp_d_w")
    private Float spDW;

    @JsonProperty("sp_d_d")
    private Float spDD;

    @JsonProperty("sp_d_f")
    private Float spDF;

    @JsonProperty("sp_f_w")
    private Float spFW;

    @JsonProperty("sp_f_d")
    private Float spFD;

    @JsonProperty("sp_f_f")
    private Float spFF;

    @JsonProperty("sp_1_0")
    private Float sp10;

    @JsonProperty("sp_2_0")
    private Float sp20;

    @JsonProperty("sp_2_1")
    private Float sp21;

    @JsonProperty("sp_3_0")
    private Float sp30;

    @JsonProperty("sp_3_1")
    private Float sp31;

    @JsonProperty("sp_3_2")
    private Float sp32;

    @JsonProperty("sp_4_0")
    private Float sp40;

    @JsonProperty("sp_4_1")
    private Float sp41;

    @JsonProperty("sp_4_2")
    private Float sp42;

    @JsonProperty("sp_w_other")
    private Float spWOther;

    @JsonProperty("sp_0_0")
    private Float sp00;

    @JsonProperty("sp_1_1")
    private Float sp11;

    @JsonProperty("sp_2_2")
    private Float sp22;

    @JsonProperty("sp_3_3")
    private Float sp33;

    @JsonProperty("sp_d_other")
    private Float spDOther;

    @JsonProperty("sp_0_1")
    private Float sp01;

    @JsonProperty("sp_0_2")
    private Float sp02;

    @JsonProperty("sp_1_12")
    private Float sp12;

    @JsonProperty("sp_0_3")
    private Float sp03;

    @JsonProperty("sp_1_3")
    private Float sp13;

    @JsonProperty("sp_@-3'")
    private Float sp23;

    @JsonProperty("sp_0_4")
    private Float sp04;

    @JsonProperty("sp_1_4")
    private Float sp14;

    @JsonProperty("sp_2_4")
    private Float sp24;

    @JsonProperty("sp_f_other")
    private Float spFOther;

    @JsonProperty("sp_up_single")
    private Float spUpSingle;

    @JsonProperty("sp_up_double")
    private Float spUpDouble;

    @JsonProperty("sp_down_single")
    private Float spDownSingle;

    @JsonProperty("sp_down_double")
    private Float spDownDouble;

    @JsonProperty("let_num")
    private Float letNum;

    @JsonProperty("sp_win")
    private Float spWin;

    @JsonProperty("sp_draw")
    private Float spDraw;

    @JsonProperty("sp_fail")
    private Float spFail;

    @JsonProperty("let_scorce")
    private Float letScore;

    @JsonProperty("sp_win+_wf")
    private Float spWinWf;


    @JsonProperty("sp_fail_wf")
    private Float spFailWf;

    /**
     * 彩期号
     */
    @JsonProperty("issue_code")
    private String issueCode;

    /**
     *   彩种号
     */
    @JsonProperty("lotter_code")
    private Integer lotteryCode;


    /**
     * 北京单场号
     */
    @JsonProperty("bj_num")
    private Integer bjNum;

    @JsonProperty("update_time")
    private Date updateTime;

    @JsonProperty("create_tims")
    private Date createTime;
    

    public SportDataBjSpPO() {
	}

	/**
     * 进球SP值PO
     *
     * @param po
     */
    public SportDataBjSpPO(SportDataBjGoalPO po) {
        this.sportAgainstInfoId = po.getSportAgainstInfoId();
        this.bjNum = po.getBjNum();
        this.lotteryCode = po.getLotteryCode();
        this.issueCode = po.getIssueCode();
        this.sp0Goal = po.getSp0Goal();
        this.sp1Goal = po.getSp1Goal();
        this.sp2Goal = po.getSp2Goal();
        this.sp3Goal = po.getSp3Goal();
        this.sp4Goal = po.getSp4Goal();
        this.sp5Goal = po.getSp5Goal();
        this.sp6Goal = po.getSp6Goal();
        this.sp7Goal = po.getSp7Goal();
    }

    /**
     * 半全场po
     *
     * @param po
     */
    public SportDataBjSpPO(SportDataBjHfWdfPO po) {
        this.sportAgainstInfoId = po.getSportAgainstInfoId();
        this.bjNum = po.getBjNum();
        this.lotteryCode = po.getLotteryCode();
        this.issueCode = po.getIssueCode();
        this.spWW = po.getSpWW();
        this.spWD = po.getSpWD();
        this.spWF = po.getSpWF();
        this.spDW = po.getSpDW();
        this.spDD = po.getSpDD();
        this.spDF = po.getSpDF();
        this.spFW = po.getSpFW();
        this.spFD = po.getSpFD();
        this.spFF = po.getSpFF();
    }

    /**
     * 比分po
     *
     * @param po
     */
    public SportDataBjSpPO(SportDataBjScorePO po) {
        this.sportAgainstInfoId = po.getSportAgainstInfoId();
        this.bjNum = po.getBjNum();
        this.lotteryCode = po.getLotteryCode();
        this.issueCode = po.getIssueCode();
        this.sp10 = po.getSp10();
        this.sp20 = po.getSp20();
        this.sp21 = po.getSp21();
        this.sp30 = po.getSp30();
        this.sp31 = po.getSp31();
        this.sp32 = po.getSp32();
        this.sp40 = po.getSp40();
        this.sp41 = po.getSp41();
        this.sp42 = po.getSp42();
        this.spWOther = po.getSpWOther();
        this.sp00 = po.getSp00();
        this.sp11 = po.getSp11();
        this.sp22 = po.getSp22();
        this.sp33 = po.getSp33();
        this.spDOther = po.getSpDOther();
        this.sp01 = po.getSp01();
        this.sp02 = po.getSp02();
        this.sp12 = po.getSp12();
        this.sp03 = po.getSp03();
        this.sp13 = po.getSp13();
        this.sp23 = po.getSp23();
        this.sp04 = po.getSp04();
        this.sp14 = po.getSp14();
        this.sp24 = po.getSp24();
        this.spFOther = po.getSpFOther();
    }

    /**
     * 上单下单po
     *
     * @param po
     */
    public SportDataBjSpPO(SportDataBjUdsdPO po) {
        this.sportAgainstInfoId = po.getSportAgainstInfoId();
        this.bjNum = po.getBjNum();
        this.lotteryCode = po.getLotteryCode();
        this.issueCode = po.getIssueCode();
        this.spUpSingle = po.getSpUpSingle();
        this.spUpDouble = po.getSpUpDouble();
        this.spDownSingle = po.getSpDownSingle();
        this.spDownDouble = po.getSpDownDouble();
    }


    public SportDataBjSpPO(SportDataBjWdfPO po) {
        this.sportAgainstInfoId = po.getSportAgainstInfoId();
        this.bjNum = po.getBjNum();
        this.lotteryCode = po.getLotteryCode();
        this.issueCode = po.getIssueCode();
        this.letNum = po.getLetNum();
        this.spWin = po.getSpWin();
        this.spDraw = po.getSpDraw();
        this.spFail = po.getSpFail();
    }

    public SportDataBjSpPO(SportDataBjWfPO po) {
        this.sportAgainstInfoId = po.getSportAgainstInfoId();
        this.bjNum = po.getBjNum();
        this.lotteryCode = po.getLotteryCode();
        this.issueCode = po.getIssueCode();
        this.letScore = po.getLetScore();
        this.spWinWf = po.getSpWin();
        this.spFailWf = po.getSpFail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Float getLetScore() {
        return letScore;
    }

    public void setLetScore(Float letScore) {
        this.letScore = letScore;
    }

    public Float getSpWinWf() {
        return spWinWf;
    }

    public void setSpWinWf(Float spWinWf) {
        this.spWinWf = spWinWf;
    }

    public Float getSpFailWf() {
        return spFailWf;
    }

    public void setSpFailWf(Float spFailWf) {
        this.spFailWf = spFailWf;
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


    public String getIssueCode() {return issueCode;}

    public void setIssueCode(String issueCode) {this.issueCode = issueCode;}

    public Integer getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public Integer getBjNum() {return bjNum;}

    public void setBjNum(Integer bjNum) {this.bjNum = bjNum;}

}