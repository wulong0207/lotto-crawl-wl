package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

/**
 * 北京单场子玩法状态
 */
public class SportStatusBjPO extends SportDataBjBasePO {

    public SportStatusBjPO() {

    }

    public SportStatusBjPO(SportDataBjBasePO po) {
        setLotteryCode(po.getLotteryCode());
        setIssueCode(po.getIssueCode());
        setBjNum(po.getBjNum());

    }

    /**
     * 自增长主键ID
     */
    private Integer id;

    /**
     * 竞技彩对阵详情表ID
     */
    private Long sportAgainstInfoId;

    /**
     * 1：正常销售；2：暂停销售 让球胜平负玩法
     */
    private Short statusLetWdf;

    /**
     * 1：正常销售；2：暂停销售 总进球数玩法
     */
    private Short statusGoal;

    /**
     * 1：正常销售；2：暂停销售 半全场胜平负玩法
     */
    private Short statusHfWdf;

    /**
     * 1：正常销售；2：暂停销售 上下盘单双玩法
     */
    private Short statusUdSd;

    /**
     * 1：正常销售；2：暂停销售 单场比分固定玩法
     */
    private Short statusScore;

    /**
     *
     */
    private String modifyBy;

    /**
     *
     */
    private Date modifyTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 1：正常销售；2：暂停销售  胜负过关状态玩法
     */
    private Short statusLetWf;

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

    public Short getStatusLetWdf() {
        return statusLetWdf;
    }

    public void setStatusLetWdf(Short statusLetWdf) {
        this.statusLetWdf = statusLetWdf;
    }

    public Short getStatusGoal() {
        return statusGoal;
    }

    public void setStatusGoal(Short statusGoal) {
        this.statusGoal = statusGoal;
    }

    public Short getStatusHfWdf() {
        return statusHfWdf;
    }

    public void setStatusHfWdf(Short statusHfWdf) {
        this.statusHfWdf = statusHfWdf;
    }

    public Short getStatusUdSd() {
        return statusUdSd;
    }

    public void setStatusUdSd(Short statusUdSd) {
        this.statusUdSd = statusUdSd;
    }

    public Short getStatusScore() {
        return statusScore;
    }

    public void setStatusScore(Short statusScore) {
        this.statusScore = statusScore;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getStatusLetWf() {
        return statusLetWf;
    }

    public void setStatusLetWf(Short statusLetWf) {
        this.statusLetWf = statusLetWf;
    }
}