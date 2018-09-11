package com.hhly.crawlcore.persistence.sport.po;

import java.io.Serializable;
import java.util.Date;

public class SportTeamInfoPO implements Serializable {
    private static final long serialVersionUID = -7688763835023805547L;
    
    private Long id;

    private Long teamId;

    private Short teamType;

    private String teamDataUrl;

    private String teamFullName;

    private String teamShortName;

    private String teamOrder;

    private String logoUrl;

    private Date modifyTime;

    private String modifyBy;

    private String createBy;

    private Date updateTime;

    private Date createTime;

    private String remark;

    private String md5Value;

    public SportTeamInfoPO() {
    }

    public SportTeamInfoPO(Long sportTeamInfoId, String teamDataUrl, String teamFullName, String teamShortName, String teamOrder, String logoUrl, String modifyBy, String createBy, String remark) {
        this.id = sportTeamInfoId;
        this.teamDataUrl = teamDataUrl;
        this.teamFullName = teamFullName;
        this.teamShortName = teamShortName;
        this.teamOrder = teamOrder;
        this.logoUrl = null;
        this.modifyBy = modifyBy;
        this.createBy = createBy;
        this.remark = remark;
    }

    public SportTeamInfoPO(Long id, Long teamId, Short teamType, String teamFullName, String teamShortName, String md5Value, String modifyBy, String createBy) {
        this.id = id;
        this.teamId = teamId;
        this.teamType = teamType;
        this.teamFullName = teamFullName;
        this.teamShortName = teamShortName;
        this.md5Value = md5Value;
        this.modifyBy = modifyBy;
        this.createBy = createBy;
    }

    /**
     * 球队对象
     *
     * @param teamType
     * @param teamFullName
     * @param md5Value
     * @param modifyBy
     * @param createBy
     */
    public SportTeamInfoPO(Short teamType, String teamFullName, String md5Value, String modifyBy, String createBy) {
        this.teamType = teamType;
        this.teamFullName = teamFullName;
        this.md5Value = md5Value;
        this.modifyBy = modifyBy;
        this.createBy = createBy;
        this.teamId = 1L;
    }

    /**
     * 球队对象
     *
     * @param teamType
     * @param teamFullName
     * @param md5Value
     * @param modifyBy
     * @param createBy
     */
    public SportTeamInfoPO(Short teamType, String teamFullName, String teamShortName, String md5Value, String modifyBy, String createBy) {
        this.teamType = teamType;
        this.teamFullName = teamFullName;
        this.teamShortName = teamShortName;
        this.md5Value = md5Value;
        this.modifyBy = modifyBy;
        this.createBy = createBy;
        this.teamId = 1L;
    }


    public SportTeamInfoPO(Long id, String teamDataUrl, String teamOrder) {
        this.id = id;
        this.teamDataUrl = teamDataUrl;
        this.teamOrder = teamOrder;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Short getTeamType() {
        return teamType;
    }

    public void setTeamType(Short teamType) {
        this.teamType = teamType;
    }

    public String getTeamDataUrl() {
        return teamDataUrl;
    }

    public void setTeamDataUrl(String teamDataUrl) {
        this.teamDataUrl = teamDataUrl;
    }

    public String getTeamFullName() {
        return teamFullName;
    }

    public void setTeamFullName(String teamFullName) {
        this.teamFullName = teamFullName;
    }

    public String getTeamShortName() {
        return teamShortName;
    }

    public void setTeamShortName(String teamShortName) {
        this.teamShortName = teamShortName;
    }

    public String getTeamOrder() {
        return teamOrder;
    }

    public void setTeamOrder(String teamOrder) {
        this.teamOrder = teamOrder;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }
}