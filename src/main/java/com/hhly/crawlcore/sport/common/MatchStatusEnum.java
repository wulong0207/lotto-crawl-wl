package com.hhly.crawlcore.sport.common;

/**
 * Created by lgs on 2017/1/9.
 * 比赛状态枚举
 */
public enum MatchStatusEnum {

    TENTATIVE(6,"暂定赛程"),
    NOT_SALE(7,"未开售"),
    SALE(9,"已开售"),
    SALE_EN(9, "Selling"),
    GOING(12,"进行中");

    private int code;

    private String name;

    private MatchStatusEnum(int code,String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
