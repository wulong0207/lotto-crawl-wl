package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportDataBbSpPO {
    private Long id;

    private Long sportAgainstInfoId;

    private Float initialLetScore;

    private Float initialLetSpWin;

    private Float initialLetSpFail;

    private Float initialSpWin;

    private Float initialSpFail;

    private Float newestLetScore;

    private Float newestLetSpWin;

    private Float newestLetSpFail;

    private Float newestSpWin;

    private Float newestSpFail;

    private Float initialPresetScore;

    private Float initialSpBig;

    private Float newestSpSmall;

    private Float initialSpSmall;

    private Float newestPresetScore;

    private Float newestSpBig;

    private Float initialSpFail15;

    private Float initialSpFail610;

    private Float initialSpFail1115;

    private Float initialSpFail1620;

    private Float initialSpFail2125;

    private Float initialSpFail26;

    private Float initialSpWin15;

    private Float initialSpWin610;

    private Float initialSpWin1115;

    private Float initialSpWin1620;

    private Float initialSpWin2125;

    private Float initialSpWin26;

    private Float newestSpFail15;

    private Float newestSpFail610;

    private Float newestSpFail1115;

    private Float newestSpFail1620;

    private Float newestSpFail2125;

    private Float newestSpFail26;

    private Float newestSpWin15;

    private Float newestSpWin610;

    private Float newestSpWin1115;

    private Float newestSpWin1620;

    private Float newestSpWin2125;

    private Float newestSpWin26;

    private Date updateTime;

    private Date createTime;

    public SportDataBbSpPO() {
    }

    /**
     * V2.0 添加, 用于保存竞篮最新sp值
     * @param officialId
     * @param newestLetScore
     * @param newestLetSpWin
     * @param newestLetSpFail
     * @param newestSpWin
     * @param newestSpFail
     * @param newestPresetScore
     * @param newestSpBig
     * @param newestSpSmall
     * @param newestSpFail15
     * @param newestSpFail610
     * @param newestSpFail1115
     * @param newestSpFail1620
     * @param newestSpFail2125
     * @param newestSpFail26
     * @param newestSpWin15
     * @param newestSpWin610
     * @param newestSpWin1115
     * @param newestSpWin1620
     * @param newestSpWin2125
     * @param newestSpWin26
     */
    public SportDataBbSpPO(Long sportAgainstInfoId, Float newestLetScore, Float newestLetSpWin, Float newestLetSpFail, Float newestSpWin, Float newestSpFail, Float newestPresetScore, Float newestSpBig, Float newestSpSmall, Float newestSpFail15, Float newestSpFail610, Float newestSpFail1115, Float newestSpFail1620, Float newestSpFail2125, Float newestSpFail26, Float newestSpWin15, Float newestSpWin610, Float newestSpWin1115, Float newestSpWin1620, Float newestSpWin2125, Float newestSpWin26) {
        this.sportAgainstInfoId = sportAgainstInfoId;
        this.newestLetScore = newestLetScore;
        this.newestLetSpWin = newestLetSpWin;
        this.newestLetSpFail = newestLetSpFail;
        this.newestSpWin = newestSpWin;
        this.newestSpFail = newestSpFail;
        this.newestPresetScore = newestPresetScore;
        this.newestSpBig = newestSpBig;
        this.newestSpSmall = newestSpSmall;
        this.newestSpFail15 = newestSpFail15;
        this.newestSpFail610 = newestSpFail610;
        this.newestSpFail1115 = newestSpFail1115;
        this.newestSpFail1620 = newestSpFail1620;
        this.newestSpFail2125 = newestSpFail2125;
        this.newestSpFail26 = newestSpFail26;
        this.newestSpWin15 = newestSpWin15;
        this.newestSpWin610 = newestSpWin610;
        this.newestSpWin1115 = newestSpWin1115;
        this.newestSpWin1620 = newestSpWin1620;
        this.newestSpWin2125 = newestSpWin2125;
        this.newestSpWin26 = newestSpWin26;
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

    public Float getInitialLetScore() {
        return initialLetScore;
    }

    public void setInitialLetScore(Float initialLetScore) {
        this.initialLetScore = initialLetScore;
    }

    public Float getInitialLetSpWin() {
        return initialLetSpWin;
    }

    public void setInitialLetSpWin(Float initialLetSpWin) {
        this.initialLetSpWin = initialLetSpWin;
    }

    public Float getInitialLetSpFail() {
        return initialLetSpFail;
    }

    public void setInitialLetSpFail(Float initialLetSpFail) {
        this.initialLetSpFail = initialLetSpFail;
    }

    public Float getInitialSpWin() {
        return initialSpWin;
    }

    public void setInitialSpWin(Float initialSpWin) {
        this.initialSpWin = initialSpWin;
    }

    public Float getInitialSpFail() {
        return initialSpFail;
    }

    public void setInitialSpFail(Float initialSpFail) {
        this.initialSpFail = initialSpFail;
    }

    public Float getNewestLetScore() {
        return newestLetScore;
    }

    public void setNewestLetScore(Float newestLetScore) {
        this.newestLetScore = newestLetScore;
    }

    public Float getNewestLetSpWin() {
        return newestLetSpWin;
    }

    public void setNewestLetSpWin(Float newestLetSpWin) {
        this.newestLetSpWin = newestLetSpWin;
    }

    public Float getNewestLetSpFail() {
        return newestLetSpFail;
    }

    public void setNewestLetSpFail(Float newestLetSpFail) {
        this.newestLetSpFail = newestLetSpFail;
    }

    public Float getNewestSpWin() {
        return newestSpWin;
    }

    public void setNewestSpWin(Float newestSpWin) {
        this.newestSpWin = newestSpWin;
    }

    public Float getNewestSpSmall() {
        return newestSpSmall;
    }

    public void setNewestSpSmall(Float newestSpSmall) {
        this.newestSpSmall = newestSpSmall;
    }

    public Float getNewestSpFail() {
        return newestSpFail;
    }

    public void setNewestSpFail(Float newestSpFail) {
        this.newestSpFail = newestSpFail;
    }

    public Float getInitialPresetScore() {
        return initialPresetScore;
    }

    public void setInitialPresetScore(Float initialPresetScore) {
        this.initialPresetScore = initialPresetScore;
    }

    public Float getInitialSpBig() {
        return initialSpBig;
    }

    public void setInitialSpBig(Float initialSpBig) {
        this.initialSpBig = initialSpBig;
    }

    public Float getInitialSpSmall() {
        return initialSpSmall;
    }

    public void setInitialSpSmall(Float initialSpSmall) {
        this.initialSpSmall = initialSpSmall;
    }

    public Float getNewestPresetScore() {
        return newestPresetScore;
    }

    public void setNewestPresetScore(Float newestPresetScore) {
        this.newestPresetScore = newestPresetScore;
    }

    public Float getNewestSpBig() {
        return newestSpBig;
    }

    public void setNewestSpBig(Float newestSpBig) {
        this.newestSpBig = newestSpBig;
    }

    public Float getInitialSpFail15() {
        return initialSpFail15;
    }

    public void setInitialSpFail15(Float initialSpFail15) {
        this.initialSpFail15 = initialSpFail15;
    }

    public Float getInitialSpFail610() {
        return initialSpFail610;
    }

    public void setInitialSpFail610(Float initialSpFail610) {
        this.initialSpFail610 = initialSpFail610;
    }

    public Float getInitialSpFail1115() {
        return initialSpFail1115;
    }

    public void setInitialSpFail1115(Float initialSpFail1115) {
        this.initialSpFail1115 = initialSpFail1115;
    }

    public Float getInitialSpFail1620() {
        return initialSpFail1620;
    }

    public void setInitialSpFail1620(Float initialSpFail1620) {
        this.initialSpFail1620 = initialSpFail1620;
    }

    public Float getInitialSpFail2125() {
        return initialSpFail2125;
    }

    public void setInitialSpFail2125(Float initialSpFail2125) {
        this.initialSpFail2125 = initialSpFail2125;
    }

    public Float getInitialSpFail26() {
        return initialSpFail26;
    }

    public void setInitialSpFail26(Float initialSpFail26) {
        this.initialSpFail26 = initialSpFail26;
    }

    public Float getNewestSpWin15() {
        return newestSpWin15;
    }

    public void setNewestSpWin15(Float newestSpWin15) {
        this.newestSpWin15 = newestSpWin15;
    }

    public Float getNewestSpWin610() {
        return newestSpWin610;
    }

    public void setNewestSpWin610(Float newestSpWin610) {
        this.newestSpWin610 = newestSpWin610;
    }

    public Float getNewestSpWin1115() {
        return newestSpWin1115;
    }

    public void setNewestSpWin1115(Float newestSpWin1115) {
        this.newestSpWin1115 = newestSpWin1115;
    }

    public Float getNewestSpWin1620() {
        return newestSpWin1620;
    }

    public void setNewestSpWin1620(Float newestSpWin1620) {
        this.newestSpWin1620 = newestSpWin1620;
    }

    public Float getNewestSpWin2125() {
        return newestSpWin2125;
    }

    public void setNewestSpWin2125(Float newestSpWin2125) {
        this.newestSpWin2125 = newestSpWin2125;
    }

    public Float getNewestSpWin26() {
        return newestSpWin26;
    }

    public void setNewestSpWin26(Float newestSpWin26) {
        this.newestSpWin26 = newestSpWin26;
    }

    public Float getInitialSpWin15() {
        return initialSpWin15;
    }

    public void setInitialSpWin15(Float initialSpWin15) {
        this.initialSpWin15 = initialSpWin15;
    }

    public Float getInitialSpWin610() {
        return initialSpWin610;
    }

    public void setInitialSpWin610(Float initialSpWin610) {
        this.initialSpWin610 = initialSpWin610;
    }

    public Float getInitialSpWin1115() {
        return initialSpWin1115;
    }

    public void setInitialSpWin1115(Float initialSpWin1115) {
        this.initialSpWin1115 = initialSpWin1115;
    }

    public Float getInitialSpWin1620() {
        return initialSpWin1620;
    }

    public void setInitialSpWin1620(Float initialSpWin1620) {
        this.initialSpWin1620 = initialSpWin1620;
    }

    public Float getInitialSpWin2125() {
        return initialSpWin2125;
    }

    public void setInitialSpWin2125(Float initialSpWin2125) {
        this.initialSpWin2125 = initialSpWin2125;
    }

    public Float getInitialSpWin26() {
        return initialSpWin26;
    }

    public void setInitialSpWin26(Float initialSpWin26) {
        this.initialSpWin26 = initialSpWin26;
    }

    public Float getNewestSpFail15() {
        return newestSpFail15;
    }

    public void setNewestSpFail15(Float newestSpFail15) {
        this.newestSpFail15 = newestSpFail15;
    }

    public Float getNewestSpFail610() {
        return newestSpFail610;
    }

    public void setNewestSpFail610(Float newestSpFail610) {
        this.newestSpFail610 = newestSpFail610;
    }

    public Float getNewestSpFail1115() {
        return newestSpFail1115;
    }

    public void setNewestSpFail1115(Float newestSpFail1115) {
        this.newestSpFail1115 = newestSpFail1115;
    }

    public Float getNewestSpFail1620() {
        return newestSpFail1620;
    }

    public void setNewestSpFail1620(Float newestSpFail1620) {
        this.newestSpFail1620 = newestSpFail1620;
    }

    public Float getNewestSpFail2125() {
        return newestSpFail2125;
    }

    public void setNewestSpFail2125(Float newestSpFail2125) {
        this.newestSpFail2125 = newestSpFail2125;
    }

    public Float getNewestSpFail26() {
        return newestSpFail26;
    }

    public void setNewestSpFail26(Float newestSpFail26) {
        this.newestSpFail26 = newestSpFail26;
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

    @Override
    public String toString() {
        return "SportDataBBSpPO{" +
                "sportAgainstInfoId=" + sportAgainstInfoId +
                ", initialLetScore=" + initialLetScore +
                ", initialLetSpWin=" + initialLetSpWin +
                ", initialLetSpFail=" + initialLetSpFail +
                ", initialSpWin=" + initialSpWin +
                ", initialSpFail=" + initialSpFail +
                ", newestLetScore=" + newestLetScore +
                ", newestLetSpWin=" + newestLetSpWin +
                ", newestLetSpFail=" + newestLetSpFail +
                ", newestSpWin=" + newestSpWin +
                ", newestSpFail=" + newestSpFail +
                ", initialPresetScore=" + initialPresetScore +
                ", initialSpBig=" + initialSpBig +
                ", newestSpSmall=" + newestSpSmall +
                ", initialSpSmall=" + initialSpSmall +
                ", newestPresetScore=" + newestPresetScore +
                ", newestSpBig=" + newestSpBig +
                ", initialSpFail15=" + initialSpFail15 +
                ", initialSpFail610=" + initialSpFail610 +
                ", initialSpFail1115=" + initialSpFail1115 +
                ", initialSpFail1620=" + initialSpFail1620 +
                ", initialSpFail2125=" + initialSpFail2125 +
                ", initialSpFail26=" + initialSpFail26 +
                ", initialSpWin15=" + initialSpWin15 +
                ", initialSpWin610=" + initialSpWin610 +
                ", initialSpWin1115=" + initialSpWin1115 +
                ", initialSpWin1620=" + initialSpWin1620 +
                ", initialSpWin2125=" + initialSpWin2125 +
                ", initialSpWin26=" + initialSpWin26 +
                ", newestSpFail15=" + newestSpFail15 +
                ", newestSpFail610=" + newestSpFail610 +
                ", newestSpFail1115=" + newestSpFail1115 +
                ", newestSpFail1620=" + newestSpFail1620 +
                ", newestSpFail2125=" + newestSpFail2125 +
                ", newestSpFail26=" + newestSpFail26 +
                ", newestSpWin15=" + newestSpWin15 +
                ", newestSpWin610=" + newestSpWin610 +
                ", newestSpWin1115=" + newestSpWin1115 +
                ", newestSpWin1620=" + newestSpWin1620 +
                ", newestSpWin2125=" + newestSpWin2125 +
                ", newestSpWin26=" + newestSpWin26 +
                '}';
    }
}