package com.hhly.crawlcore.sport.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhly.skeleton.base.bo.BaseBO;
import com.hhly.skeleton.base.util.DateUtil;

/**
 * @author lgs on
 * @version 1.0
 * @desc 老足彩彩期信息
 * @date 2017/4/13.
 * @company 益彩网络科技有限公司
 */
public class OldLotteryIssue extends BaseBO {
    /**
     * 彩期编号
     */
    private String num;
    /**
     * 开奖时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date prize;
    /**
     * 开始销售时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start;
    /**
     * 截止销售时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end;
    /**
     * 上一期号码
     */
    private String last;
    /**
     * 下一期号码
     */
    private String next;


    public OldLotteryIssue() {
    }

    public OldLotteryIssue(JSONObject jsonObject) {
        this.num = jsonObject.getString("num");
        this.prize = DateUtil.convertStrToDate(jsonObject.getString("prize"),"yyyy/MM/dd HH:mm");
        this.start = DateUtil.convertStrToDate(jsonObject.getString("start"),"yyyy/MM/dd HH:mm");
        this.end = DateUtil.convertStrToDate(jsonObject.getString("end"),"yyyy/MM/dd HH:mm");
        this.last = jsonObject.getString("last");
        this.next = jsonObject.getString("next");
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Date getPrize() {
        return prize;
    }

    public void setPrize(Date prize) {
        this.prize = prize;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
