package com.hhly.crawlcore.sport.common;

/**
 * @auth lgs on
 * @date 2017/2/8.
 * @desc 老足彩赛事状态枚举
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public enum OldMatchStatusEnum {

    WAIT_MATCH((short)1, "等待比赛"),
    IN_GAME_MATCH((short)2, "比赛中"),
    FINISH((short)3, "已完场"),
    DEFER_MATCH((short)4, "延期"),
    CANCEL((short)5, "取消");

    private Short code;
    private String name;

    private OldMatchStatusEnum(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    public Short getCode() {
        return code;
    }


    public String getName() {
        return name;
    }

}
