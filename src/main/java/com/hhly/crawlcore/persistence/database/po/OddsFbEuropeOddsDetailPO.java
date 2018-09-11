package com.hhly.crawlcore.persistence.database.po;

import java.util.Date;

public class OddsFbEuropeOddsDetailPO {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 欧赔率id
     */
    private Integer europeoddsId;

    /**
     * 主胜水位
     */
    private Double homewin;

    /**
     * 平局水位
     */
    private Double draw;

    /**
     * 客胜水位
     */
    private Double guestwin;

    /**
     * 数据来源方id
     */
    private String sourceId;

    /**
     * 数据来源方类型1:一比分
     */
    private Integer sourceType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 后台操作员ID
     */
    private String modifyBy;

    /**
     * 更新时间
     */
    private Date updateTime;


    public OddsFbEuropeOddsDetailPO() {
    }

    public OddsFbEuropeOddsDetailPO(OddsFbEuropeOddsPO po) {
        this.europeoddsId = po.getEuropeId();
        this.homewin = po.getHomewinN();
        this.draw = po.getDrawN();
        this.guestwin = po.getGuestwinN();
        this.sourceId = po.getSourceId();
        this.sourceType = po.getSourceType();
        this.updateTime = po.getUpdateTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEuropeoddsId() {
        return europeoddsId;
    }

    public void setEuropeoddsId(Integer europeoddsId) {
        this.europeoddsId = europeoddsId;
    }

    public Double getHomewin() {
        return homewin;
    }

    public void setHomewin(Double homewin) {
        this.homewin = homewin;
    }

    public Double getDraw() {
        return draw;
    }

    public void setDraw(Double draw) {
        this.draw = draw;
    }

    public Double getGuestwin() {
        return guestwin;
    }

    public void setGuestwin(Double guestwin) {
        this.guestwin = guestwin;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId == null ? null : sourceId.trim();
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}