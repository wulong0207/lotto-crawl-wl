package com.hhly.crawlcore.sport.common;

import java.util.HashMap;

/**
 * Created by lgs on 2017/1/5.
 * 公共常量类
 */
public class Constants {

    public static final short WIN = 3;
    public static final short DRAW = 1;
    public static final short LOST = 0;
    /**
     * 北京单场玩法销售状态
     */
    public enum bjdcStatus {
        REGULAR_SALE((short) 1, "正常销售"),
        SUSPENDED_SALES((short) 2, "暂停销售");

        bjdcStatus(short key, String value) {
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

    /**
     * 周几map
     */
    public static final HashMap<String, String> weekTxtMap = new HashMap<>(7);

    static {
        weekTxtMap.put("周一", "1");
        weekTxtMap.put("周二", "2");
        weekTxtMap.put("周三", "3");
        weekTxtMap.put("周四", "4");
        weekTxtMap.put("周五", "5");
        weekTxtMap.put("周六", "6");
        weekTxtMap.put("周日", "7");
    }
}
