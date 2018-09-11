package com.hhly.crawlcore.persistence.sport.po;


import java.util.Date;

public class SportDataFbScorePO {
	
    private Long id;

    private Long sportAgainstInfoId;

    private Float sp10;

    private Float sp20;

    private Float sp21;

    private Float sp30;

    private Float sp31;

    private Float sp32;

    private Float sp40;

    private Float sp41;

    private Float sp42;

    private Float sp50;

    private Float sp51;

    private Float sp52;

    private Float spWOther;

    private Float sp00;

    private Float sp11;

    private Float sp22;

    private Float sp33;

    private Float spDOther;

    private Float sp01;

    private Float sp02;

    private Float sp12;

    private Float sp03;

    private Float sp13;

    private Float sp23;

    private Float sp04;

    private Float sp14;

    private Float sp24;

    private Float sp05;

    private Float sp15;

    private Float sp25;

    private Float spFOther;

    private Date releaseTime;

    private Date updateTime;

    private Date createTime;

    public SportDataFbScorePO() {
    }

    public SportDataFbScorePO(Long sportAgainstInfoId, Float sp10, Float sp20, Float sp21, Float sp30, Float sp31, Float sp32, Float sp40, Float sp41, Float sp42, Float sp50, Float sp51, Float sp52, Float spWOther, Float sp00, Float sp11, Float sp22, Float sp33, Float spDOther, Float sp01, Float sp02, Float sp12, Float sp03, Float sp13, Float sp23, Float sp04, Float sp14, Float sp24, Float sp05, Float sp15, Float sp25, Float spFOther, Date releaseTime) {
        this.sportAgainstInfoId = sportAgainstInfoId;
        this.sp10 = sp10;
        this.sp20 = sp20;
        this.sp21 = sp21;
        this.sp30 = sp30;
        this.sp31 = sp31;
        this.sp32 = sp32;
        this.sp40 = sp40;
        this.sp41 = sp41;
        this.sp42 = sp42;
        this.sp50 = sp50;
        this.sp51 = sp51;
        this.sp52 = sp52;
        this.spWOther = spWOther;
        this.sp00 = sp00;
        this.sp11 = sp11;
        this.sp22 = sp22;
        this.sp33 = sp33;
        this.spDOther = spDOther;
        this.sp01 = sp01;
        this.sp02 = sp02;
        this.sp12 = sp12;
        this.sp03 = sp03;
        this.sp13 = sp13;
        this.sp23 = sp23;
        this.sp04 = sp04;
        this.sp14 = sp14;
        this.sp24 = sp24;
        this.sp05 = sp05;
        this.sp15 = sp15;
        this.sp25 = sp25;
        this.spFOther = spFOther;
        this.releaseTime = releaseTime;
    }

    public SportDataFbScorePO(SportDataFbSpPO sportDataFbSpPO) {

        this.sp10 = sportDataFbSpPO.getNewestSp10();
        this.sp20 = sportDataFbSpPO.getNewestSp20();
        this.sp21 = sportDataFbSpPO.getNewestSp21();
        this.sp30 = sportDataFbSpPO.getNewestSp30();
        this.sp31 = sportDataFbSpPO.getNewestSp31();
        this.sp32 = sportDataFbSpPO.getNewestSp32();
        this.sp40 = sportDataFbSpPO.getNewestSp40();
        this.sp41 = sportDataFbSpPO.getNewestSp41();
        this.sp42 = sportDataFbSpPO.getNewestSp42();
        this.sp50 = sportDataFbSpPO.getNewestSp50();
        this.sp51 = sportDataFbSpPO.getNewestSp51();
        this.sp52 = sportDataFbSpPO.getNewestSp52();
        this.spWOther = sportDataFbSpPO.getNewestSpWOther();

        this.sp00 = sportDataFbSpPO.getNewestSp00();
        this.sp11 = sportDataFbSpPO.getNewestSp11();
        this.sp22 = sportDataFbSpPO.getNewestSp22();
        this.sp33 = sportDataFbSpPO.getNewestSp33();
        this.spDOther = sportDataFbSpPO.getNewestSpDOther();

        this.sp01 = sportDataFbSpPO.getNewestSp01();
        this.sp02 = sportDataFbSpPO.getNewestSp02();
        this.sp12 = sportDataFbSpPO.getNewestSp12();
        this.sp03 = sportDataFbSpPO.getNewestSp03();
        this.sp13 = sportDataFbSpPO.getNewestSp13();
        this.sp23 = sportDataFbSpPO.getNewestSp23();
        this.sp04 = sportDataFbSpPO.getNewestSp04();
        this.sp14 = sportDataFbSpPO.getNewestSp14();
        this.sp24 = sportDataFbSpPO.getNewestSp24();
        this.sp05 = sportDataFbSpPO.getNewestSp05();
        this.sp15 = sportDataFbSpPO.getNewestSp15();
        this.sp25 = sportDataFbSpPO.getNewestSp25();
        this.spFOther = sportDataFbSpPO.getNewestSpFOther();

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

    public Float getSp50() {
        return sp50;
    }

    public void setSp50(Float sp50) {
        this.sp50 = sp50;
    }

    public Float getSp51() {
        return sp51;
    }

    public void setSp51(Float sp51) {
        this.sp51 = sp51;
    }

    public Float getSp52() {
        return sp52;
    }

    public void setSp52(Float sp52) {
        this.sp52 = sp52;
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

    public Float getSp05() {
        return sp05;
    }

    public void setSp05(Float sp05) {
        this.sp05 = sp05;
    }

    public Float getSp15() {
        return sp15;
    }

    public void setSp15(Float sp15) {
        this.sp15 = sp15;
    }

    public Float getSp25() {
        return sp25;
    }

    public void setSp25(Float sp25) {
        this.sp25 = sp25;
    }

    public Float getSpFOther() {
        return spFOther;
    }

    public void setSpFOther(Float spFOther) {
        this.spFOther = spFOther;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
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
    
    public Boolean isEquals(SportDataFbScorePO lastPO){
   	 if(sp10.equals(lastPO.getSp10()) && sp20.equals(lastPO.getSp20()) && sp21.equals(lastPO.getSp21())
   			 && sp30.equals(lastPO.getSp30()) && sp31.equals(lastPO.getSp31()) && sp32.equals(lastPO.getSp32())
   			 && sp40.equals(lastPO.getSp40()) && sp41.equals(lastPO.getSp41()) && sp42.equals(lastPO.getSp42())
   			 && sp50.equals(lastPO.getSp50()) && sp51.equals(lastPO.getSp51()) && sp52.equals(lastPO.getSp52())
   			 && spWOther.equals(lastPO.getSpWOther()) && sp00.equals(lastPO.getSp00()) && sp11.equals(lastPO.getSp11())
   			 && sp22.equals(lastPO.getSp22()) && sp33.equals(lastPO.getSp33()) && spDOther.equals(lastPO.getSpDOther())   			 
   			 && sp01.equals(lastPO.getSp01()) && sp02.equals(lastPO.getSp02()) && sp12.equals(lastPO.getSp12())
   			 && sp03.equals(lastPO.getSp03()) && sp13.equals(lastPO.getSp13()) && sp23.equals(lastPO.getSp23())
   			 && sp04.equals(lastPO.getSp04()) && sp14.equals(lastPO.getSp14()) && sp24.equals(lastPO.getSp24())
   			 && sp05.equals(lastPO.getSp05()) && sp15.equals(lastPO.getSp15()) && sp25.equals(lastPO.getSp25()) 
   			 && spFOther.equals(lastPO.getSpFOther()))
   		 return true;
   	 return false;
   }    
}