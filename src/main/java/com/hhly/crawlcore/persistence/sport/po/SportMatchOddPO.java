package com.hhly.crawlcore.persistence.sport.po;

import java.util.Date;

public class SportMatchOddPO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 一比分对阵id
     */
    private String matchId;

    /**
     * 菠菜公司id
     */
    private Long gameId;

    /**
     * 主队胜SP值
     */
    private Float homeWin;

    /**
     * 平SP值
     */
    private Float standOff;

    /**
     * 主队负的SP值
     */
    private Float guestWin;

    /**
     * null
     */
    private Date modifyTime;

    /**
     * null
     */
    private Date createTime;


    public SportMatchOddPO(Long id, String matchId, Long gameId, Float homeWin, Float standOff, Float guestWin, Date modifyTime, Date createTime) {
        this.id = id;
        this.matchId = matchId;
        this.gameId = gameId;
        this.homeWin = homeWin;
        this.standOff = standOff;
        this.guestWin = guestWin;
        this.modifyTime = modifyTime;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId == null ? null : matchId.trim();
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Float getHomeWin() {
        return homeWin;
    }

    public void setHomeWin(Float homeWin) {
        this.homeWin = homeWin;
    }

    public Float getStandOff() {
        return standOff;
    }

    public void setStandOff(Float standOff) {
        this.standOff = standOff;
    }

    public Float getGuestWin() {
        return guestWin;
    }

    public void setGuestWin(Float guestWin) {
        this.guestWin = guestWin;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}