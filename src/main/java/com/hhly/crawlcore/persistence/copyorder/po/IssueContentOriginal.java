package com.hhly.crawlcore.persistence.copyorder.po;

import java.util.Date;

/**
 * 一比分赛事内容原始表
 */
public class IssueContentOriginal {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 一比分推荐过来时的id集合
     */
    private String originalIdList;

    /**
     * 商户id
     */
    private Integer masterId;

    /**
     * 是否处理1：未处理；2：已处理3：处理中
     */
    private Integer status;

    /**
     * 
     */
    private Date createTime;

    /**
     * 一比分接口返回赛事内容，JSON串
     */
    private String content;

    /**
     * 一比分userId
     */
    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalIdList() {
        return originalIdList;
    }

    public void setOriginalIdList(String originalIdList) {
        this.originalIdList = originalIdList == null ? null : originalIdList.trim();
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}