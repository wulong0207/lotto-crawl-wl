package com.hhly.crawlcore.persistence.database.po;

import java.util.Date;

public class OddsEuropePO {
    /**
     * 开盘公司唯一编号
     */
    private Integer id;

    /**
     * 名称(全)如：亚细亚洲
     */
    private String name;

    /**
     * 名称(缩)如：亚洲
     */
    private String nameAbb;

    /**
     * 是否显示1:是,0:否
     */
    private Boolean isshow;

    /**
     * 置顶排序值范围1-999,默认:1,1:置顶
     */
    private Short topRank;

    /**
     * 公司注册地
     */
    private String regLocation;

    /**
     * 说明，备注
     */
    private String remark;

    /**
     * 是否主流1:是,0:否
     */
    private Boolean isPrimary;

    /**
     * 是否交易所1:是,0:否
     */
    private Boolean isExchange;

    /**
     * 数据来源方id
     */
    private String sourceId;

    /**
     * 数据来源方类型1:一比分
     */
    private Boolean sourceType;

    /**
     * 类型1:足球;2:篮球
     */
    private Boolean type;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNameAbb() {
        return nameAbb;
    }

    public void setNameAbb(String nameAbb) {
        this.nameAbb = nameAbb == null ? null : nameAbb.trim();
    }

    public Boolean getIsshow() {
        return isshow;
    }

    public void setIsshow(Boolean isshow) {
        this.isshow = isshow;
    }

    public Short getTopRank() {
        return topRank;
    }

    public void setTopRank(Short topRank) {
        this.topRank = topRank;
    }

    public String getRegLocation() {
        return regLocation;
    }

    public void setRegLocation(String regLocation) {
        this.regLocation = regLocation == null ? null : regLocation.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Boolean getIsExchange() {
        return isExchange;
    }

    public void setIsExchange(Boolean isExchange) {
        this.isExchange = isExchange;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId == null ? null : sourceId.trim();
    }

    public Boolean getSourceType() {
        return sourceType;
    }

    public void setSourceType(Boolean sourceType) {
        this.sourceType = sourceType;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
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