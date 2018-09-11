package com.hhly.crawlcore.sport.common;

/**
 * Created by lgs on 2017/1/9.
 * 竞彩足球销售玩法
 */
public enum FootBallPlayStatusEnum {
    PASS(2,"u-cir"),//过关
    SINGLE_PASS(1, "u-dan"),//单关 后台显示正常销售
    NOT_PLAY(4,"u-kong");//未开售次玩法

    private int code;

    private String name;

    FootBallPlayStatusEnum(int code, String name) {
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

    /**
     * 竞篮玩法常量
     */
    public interface HowToPlayKey {
        /**
         * 胜负
         */
        String SUCCESS_OR_FAILURE = "wubai:spider:bbwf:list";
        /**
         * 让分胜负
         */
        String LET_SCORE_SUCCESS_OR_FAILURE = "wubai:spider:bbletwf:list";
        /**
         * 胜负差
         */
        String SUCCESS_OR_FAILURE_DIFF = "wubai:spider:bbwfdiff:list";
        /**
         * 大小分
         */
        String BIG_OR_SMALL = "wubai:spider:bbbigsmall:list";
    }


    /**
     * sport_data_bb_wf
     * 竞篮胜负或让分胜负类型
     */
    public enum wfOrLetWf {
        SUCCESS_OR_FAILURE((short) 1, "胜负"),
        LET_SCORE_SUCCESS_OR_FAILURE((short) 2, "让分胜负");

        wfOrLetWf(short key, String value) {
            this.key = key;
            this.value = value;
        }

        private short key;
        private String value;

        public short getKey() {
            return key;
        }

        public void setKey(short key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
