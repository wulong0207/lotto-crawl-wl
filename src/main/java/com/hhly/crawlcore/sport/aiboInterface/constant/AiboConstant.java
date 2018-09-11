package com.hhly.crawlcore.sport.aiboInterface.constant;

/**
 * 爱波对接常量类
 * Created by chenkangning on 2017/8/1.
 */
public class AiboConstant {


    public enum ScoreEnum {
        s10("1:0", "10"),
        s20("2:0", "20"),
        s21("2:1", "21"),
        s30("3:0", "30"),
        s31("3:1", "31"),
        s32("3:2", "32"),
        s40("4:0", "40"),
        s41("4:1", "41"),
        s42("4:2", "42"),
        s90("胜其他", "90"),
        s00("0:0", "00"),
        s11("1:1", "11"),
        s22("2:2", "22"),
        s33("3:3", "33"),
        s99("平其他", "99"),
        s01("0:1", "01"),
        s02("0:2", "02"),
        s12("1:2", "12"),
        s03("0:3", "03"),
        s13("1:3", "13"),
        s23("2:3", "23"),
        s04("0:4", "04"),
        s14("1:4", "14"),
        s24("2:4", "24"),
        s09("负其他", "09"),
        OTHER("延", "*"),;
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        ScoreEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static ScoreEnum getValueByKey(String key) {
            for (ScoreEnum scoreEnum : ScoreEnum.values()) {
                if (key.equals(scoreEnum.getKey())) {
                    return scoreEnum;
                }
            }
            return null;
        }
    }

    public enum HfwdfDrawEnum {
        //33;31;30;13;11;10;03;01;00;
        WW("胜-胜", "33"),
        WD("胜-平", "31"),
        WL("胜-负", "30"),
        DW("平-胜", "13"),
        DD("平-平", "11"),
        DL("平-负", "10"),
        LW("负-胜", "03"),
        LD("负-平", "01"),
        LL("负-负", "00"),
        OTHER("延", "*"),;
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        HfwdfDrawEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static HfwdfDrawEnum getValueByKey(String key) {
            for (HfwdfDrawEnum udsd : HfwdfDrawEnum.values()) {
                if (key.equals(udsd.getKey())) {
                    return udsd;
                }
            }
            return null;
        }
    }

    public enum UdsdDrawEnum {
        US("上+单", "1"), UD("上+双", "2"), DS("下+单", "3"), DD("下+双", "4"), OTHER("延", "*");
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        UdsdDrawEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static UdsdDrawEnum getValueByKey(String key) {
            for (UdsdDrawEnum udsd : UdsdDrawEnum.values()) {
                if (key.equals(udsd.getKey())) {
                    return udsd;
                }
            }
            return null;
        }
    }

    /**
     * 总进球彩果
     */
    public enum GoalDrawEnum {
        S0("0", "0"),
        S1("1", "1"),
        S2("2", "2"),
        S3("3", "3"),
        S4("4", "4"),
        S5("5", "5"),
        S6("6", "6"),
        S7("7+", "7"),
        OTHER("延", "*");
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        GoalDrawEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static GoalDrawEnum getValueByKey(String key) {
            for (GoalDrawEnum drawEnum : GoalDrawEnum.values()) {
                if (key.equals(drawEnum.getKey())) {
                    return drawEnum;
                }
            }
            return null;
        }
    }


    /**
     * 彩果
     */
    public enum DrawEnum {

        WIN("胜", "3"), FLAT("平", "1"), LOSS("负", "0"), OTHER("延", "*");

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        DrawEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static DrawEnum getValueByKey(String key) {
            for (DrawEnum drawEnum : DrawEnum.values()) {
                if (key.equals(drawEnum.getKey())) {
                    return drawEnum;
                }
            }
            return null;
        }
    }

    /**
     * 爱波对应彩种枚举
     */
    public enum HotoPlayEnum {
        //胜平负
        WIN_FLAT_LOSS("44", 306),
        //总进球数
        TOTAL_GOALS("45", 306),
        //上下盘单双数
        ON_SINGLE_NUMBER("46", 306),
        //单场比分
        SINGLE_GAME_SCORE("47", 306),
        //半全场胜平负
        HF_WDF_("48", 306),
        //下半场比分
        SECOND_HALF_SCORE("71", 306)
        //胜负过关
        , WIN_OR_LOSE("76", 307);

        private String key;
        private Integer value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        HotoPlayEnum(String key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public static HotoPlayEnum getValueByKey(String key) {

            for (HotoPlayEnum hotoPlayEnum : HotoPlayEnum.values()) {
                if (key.equals(hotoPlayEnum.getKey())) {
                    return hotoPlayEnum;
                }
            }
            return null;
        }
    }

    public enum MatchStatusEnum {
        //未开售
        NOT_ON_SALE(2, 7),
        //销售中
        IN_SALES(1, 9);
        private Integer key;
        private Integer value;

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        MatchStatusEnum(Integer key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public static MatchStatusEnum getValueByKey(Integer key) {
            for (MatchStatusEnum matchStatusEnum : MatchStatusEnum.values()) {
                if (key.equals(matchStatusEnum.getKey())) {
                    return matchStatusEnum;
                }
            }
            return null;
        }
    }

    public enum SellingEnum {
        //胜平负
        WDF("44", "w_"),
        //总进球数
        GOAL("45", "g_"),
        //上下盘单双数
        SINGLE_DOUBLE("46", "o_"),
        //单场比分
        SCORE("47", "s_"),
        //半全场胜平负
        HF_WDF("48", "b_"),
        //下半场比分
        DOWN_SCORE("71", "h_"),
        //胜负过关
        WF("76", "d_");
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        SellingEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static SellingEnum getValueByKey(String key) {
            for (SellingEnum sellingEnum : SellingEnum.values()) {
                if (key.equals(sellingEnum.getKey())) {
                    return sellingEnum;
                }
            }
            return null;
        }
    }

    /**
     * 爱波玩法对应常量
     */
    public interface HowToPlayInterface {

        /**
         * 胜平负
         */
        Integer WIN_FLAT_LOSS = 44;
        /**
         * 总进球数
         */
        Integer TOTAL_GOALS = 45;
        /**
         * 上下盘单双数
         */
        Integer ON_SINGLE_NUMBER = 46;
        /**
         * 单场比分
         */
        Integer SINGLE_GAME_SCORE = 47;
        /**
         * 半全场胜平负
         */
        Integer HF_WDF_ = 48;
        /**
         * 下半场比分
         */
        Integer SECOND_HALF_SCORE = 71;
        /**
         * 胜负过关
         */
        Integer WIN_OR_LOSE = 76;

    }

    public enum HowToPlayEnum {

    }

}
