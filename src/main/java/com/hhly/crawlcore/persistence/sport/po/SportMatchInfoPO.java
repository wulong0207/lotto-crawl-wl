package com.hhly.crawlcore.persistence.sport.po;

import java.io.Serializable;
import java.util.Date;

public class SportMatchInfoPO implements Serializable {

    private static final long serialVersionUID = -7677248897750491987L;

    private Long id;

    private Long matchId;

    private String matchDataUrl;

    private String matchFullName;

    private String matchShortName;

    private String logoUrl;

    private Short matchType;
    
    private String matchColor;

    private String modifyBy;

    private Date modifyTime;

    private Date updateTime;

    private Date createTime;

    private String remark;

    private String md5Value;

    public SportMatchInfoPO() {
    }

    /**
     * 一比分同步用
     * @param id
     * @param matchDataUrl
     */
    public SportMatchInfoPO(Long id, String matchDataUrl) {
        this.id = id;
        this.matchDataUrl = matchDataUrl;
    }

    /**
     * 500W抓取入库用
     * @param matchFullName
     * @param matchType
     * @param md5Value
     * @param modifyBy
     */
    public SportMatchInfoPO(String matchFullName, Short matchType, String md5Value, String modifyBy) {
        this.matchFullName = matchFullName;
        this.matchType = matchType;
        this.md5Value = md5Value;
        this.modifyBy = modifyBy;
        this.matchId = 1L;
    }

    public SportMatchInfoPO(String matchFullName, String matchShortName, Short matchType, String md5Value, String modifyBy) {
        this.matchFullName = matchFullName;
        this.matchShortName = matchShortName;
        this.matchType = matchType;
        this.md5Value = md5Value;
        this.modifyBy = modifyBy;
        this.matchId = 1L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getMatchDataUrl() {
        return matchDataUrl;
    }

    public void setMatchDataUrl(String matchDataUrl) {
        this.matchDataUrl = matchDataUrl;
    }

    public String getMatchFullName() {
        return matchFullName;
    }

    public void setMatchFullName(String matchFullName) {
        this.matchFullName = matchFullName;
    }

    public String getMatchShortName() {
        return matchShortName;
    }

    public void setMatchShortName(String matchShortName) {
        this.matchShortName = matchShortName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Short getMatchType() {
        return matchType;
    }

    public void setMatchType(Short matchType) {
        this.matchType = matchType;
    }
    
    public String getMatchColor() {
		return matchColor;
	}

	public void setMatchColor(String matchColor) {
		this.matchColor = matchColor;
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