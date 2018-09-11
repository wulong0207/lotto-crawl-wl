package com.hhly.crawlcore.persistence.sport.po;


import java.util.Date;

public class SportDataFbHfWdfPO {
	
    private Long id;

    private Long sportAgainstInfoId;

    private Float spWW;

    private Float spWD;

    private Float spWF;

    private Float spDW;

    private Float spDD;

    private Float spDF;

    private Float spFW;

    private Float spFD;

    private Float spFF;

    private Date releaseTime;

    private Date updateTime;

    private Date createTime;

    public SportDataFbHfWdfPO() {
    }

    public SportDataFbHfWdfPO(SportDataFbSpPO sportFbMatchSpPO) {
        this.spWW = sportFbMatchSpPO.getNewestSpWW().floatValue();
        this.spWD = sportFbMatchSpPO.getNewestSpWD().floatValue();
        this.spWF = sportFbMatchSpPO.getNewestSpWF().floatValue();
        this.spDW = sportFbMatchSpPO.getNewestSpDW().floatValue();
        this.spDD = sportFbMatchSpPO.getNewestSpDD().floatValue();
        this.spDF = sportFbMatchSpPO.getNewestSpDF().floatValue();
        this.spFW = sportFbMatchSpPO.getNewestSpFW().floatValue();
        this.spFD = sportFbMatchSpPO.getNewestSpFD().floatValue();
        this.spFF = sportFbMatchSpPO.getNewestSpFF().floatValue();
    }

    public SportDataFbHfWdfPO(Long sportAgainstInfoId, Float spWW, Float spWD, Float spWF, Float spDW, Float spDD, Float spDF, Float spFW, Float spFD, Float spFF, Date releaseTime) {
        this.sportAgainstInfoId = sportAgainstInfoId;
        this.spWW = spWW;
        this.spWD = spWD;
        this.spWF = spWF;
        this.spDW = spDW;
        this.spDD = spDD;
        this.spDF = spDF;
        this.spFW = spFW;
        this.spFD = spFD;
        this.spFF = spFF;
        this.releaseTime = releaseTime;
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
    
    public Boolean isEquals(SportDataFbHfWdfPO lastPO){
    	 if(spWW.equals(lastPO.getSpWW()) && spWD.equals(lastPO.getSpWD())
    			 && spWF.equals(lastPO.getSpWF()) && spDW.equals(lastPO.getSpDW())
    			 && spDD.equals(lastPO.getSpDD()) && spDF.equals(lastPO.getSpDF())
    			 && spFW.equals(lastPO.getSpFW()) && spFD.equals(lastPO.getSpFD())
    			 && spFF.equals(lastPO.getSpFF()))
    		 return true;
    	 return false;
    }
}