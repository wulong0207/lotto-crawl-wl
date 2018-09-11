package com.hhly.crawlcore.database.entity;

import java.util.List;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/10/25.
 * @company 益彩网络科技有限公司
 */
public class RecommendDetailEntity {
    /**
     * 流水号
     */
    private String id;

    private String enable;
    /**
     *
     */
    private String remark;
    private String createBy;
    private long createTime;
    private String updateBy;
    private String updateTime;
    /**
     * 标题
     */
    private String title;
    /**
     * 推荐理由
     */
    private String context;
    /**
     * 推荐结果1
     */
    private String choose;
    /**
     * 推荐结果2
     */
    private String choose1;
    /**
     * 推荐价格
     */
    private int price;
    /**
     * 用户id
     */
    private String userid;

    private int matchid;
    /**
     * 联赛id
     */
    private String leagueid;
    /**
     * 类型 0：竞彩单关
     */
    private int type;
    private int status;
    /**
     * 创建时间
     */
    private String createtime;

    private String oddsid;
    private int releaseType;
    private String tjMutilDetail;
    private int hideContent;
    /**
     * 串关数
     */
    private int multiType;
    /**
     * 查看人数
     */
    private int lookCount;
    /**
     *
     */
    private int sportType;
    /**
     * 联赛的名称
     */
    private String leagueName;
    /**
     * 主队名称
     */
    private String homeName;
    /**
     * 主队图片
     */
    private String homeImg;
    /**
     * 客队名称
     */
    private String guestName;
    /**
     * 客队图片
     */
    private String guestImg;
    /**
     * 赛事期号（如周三001）
     */
    private String serNum;

    /**
     * 彩果
     */
    private String res;

    private int matchStatus;
    private long matchDateTime;
    private String handicap;
    /**
     * 主胜赔率
     */
    private String leftOdds;
    /**
     * 平赔率
     */
    private String minOdds;
    /**
     * 客胜赔率
     */
    private String rightOdds;
    private int countOrder;
    private double winsRate;
    private double earningsRate;
    private int winPoint;
    private List<MutilDetailListBean> mutilDetailList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String getChoose1() {
        return choose1;
    }

    public void setChoose1(String choose1) {
        this.choose1 = choose1;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getMatchid() {
        return matchid;
    }

    public void setMatchid(int matchid) {
        this.matchid = matchid;
    }

    public String getLeagueid() {
        return leagueid;
    }

    public void setLeagueid(String leagueid) {
        this.leagueid = leagueid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }


    public String getOddsid() {
        return oddsid;
    }

    public void setOddsid(String oddsid) {
        this.oddsid = oddsid;
    }

    public int getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(int releaseType) {
        this.releaseType = releaseType;
    }

    public String getTjMutilDetail() {
        return tjMutilDetail;
    }

    public void setTjMutilDetail(String tjMutilDetail) {
        this.tjMutilDetail = tjMutilDetail;
    }

    public int getHideContent() {
        return hideContent;
    }

    public void setHideContent(int hideContent) {
        this.hideContent = hideContent;
    }

    public int getMultiType() {
        return multiType;
    }

    public void setMultiType(int multiType) {
        this.multiType = multiType;
    }

    public int getLookCount() {
        return lookCount;
    }

    public void setLookCount(int lookCount) {
        this.lookCount = lookCount;
    }

    public int getSportType() {
        return sportType;
    }

    public void setSportType(int sportType) {
        this.sportType = sportType;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeImg() {
        return homeImg;
    }

    public void setHomeImg(String homeImg) {
        this.homeImg = homeImg;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestImg() {
        return guestImg;
    }

    public void setGuestImg(String guestImg) {
        this.guestImg = guestImg;
    }

    public String getSerNum() {
        return serNum;
    }

    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public long getMatchDateTime() {
        return matchDateTime;
    }

    public void setMatchDateTime(long matchDateTime) {
        this.matchDateTime = matchDateTime;
    }

    public String getHandicap() {
        return handicap;
    }

    public void setHandicap(String handicap) {
        this.handicap = handicap;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getLeftOdds() {
        return leftOdds;
    }

    public void setLeftOdds(String leftOdds) {
        this.leftOdds = leftOdds;
    }

    public String getMinOdds() {
        return minOdds;
    }

    public void setMinOdds(String minOdds) {
        this.minOdds = minOdds;
    }

    public String getRightOdds() {
        return rightOdds;
    }

    public void setRightOdds(String rightOdds) {
        this.rightOdds = rightOdds;
    }

    public int getCountOrder() {
        return countOrder;
    }

    public void setCountOrder(int countOrder) {
        this.countOrder = countOrder;
    }

    public double getWinsRate() {
        return winsRate;
    }

    public void setWinsRate(double winsRate) {
        this.winsRate = winsRate;
    }

    public double getEarningsRate() {
        return earningsRate;
    }

    public void setEarningsRate(double earningsRate) {
        this.earningsRate = earningsRate;
    }

    public int getWinPoint() {
        return winPoint;
    }

    public void setWinPoint(int winPoint) {
        this.winPoint = winPoint;
    }

    public List<MutilDetailListBean> getMutilDetailList() {
        return mutilDetailList;
    }

    public void setMutilDetailList(List<MutilDetailListBean> mutilDetailList) {
        this.mutilDetailList = mutilDetailList;
    }

    public static class MutilDetailListBean {
        /**
         * id : 2106
         * enable : null
         * remark : null
         * createBy : null
         * createTime : null
         * updateBy : null
         * updateTime : null
         * title : null
         * context :
         * choose : 1
         * choose1 : null
         * price : 0
         * userid : null
         * matchid : 848872748
         * leagueid : 1562
         * type : 0
         * status : 0
         * createtime : null
         * updatetime : null
         * oddsid : 45098
         * releaseType : 0
         * tjMutilDetail : null
         * hideContent : 0
         * multiType : 0
         * lookCount : 0
         * sportType : 0
         * leagueName : 海伊洼
         * homeName : 华奇巴托
         * homeImg : http://cn3.pp13322.com/img/pic/team/images/20130922170218.gif
         * guestName : 智利天主大学
         * guestImg : http://cn2.pp13322.com/img/pic/team/images/20130922170348.gif
         * serNum : 周三008
         * matchStatus : 1
         * matchDateTime : 1504738800000
         * handicap : 0
         * res : null
         * leftOdds : 2.63
         * minOdds : 2.2
         * rightOdds : 3.4
         * countOrder : 0
         * winsRate : 0
         * earningsRate : 0
         * winPoint : 0
         * mutilDetailList : null
         */

        private String id;
        private String enable;
        private String remark;
        private String createBy;
        private String createTime;
        private String updateBy;
        private String updateTime;
        private String title;
        private String context;
        private String choose;
        private String choose1;
        private int price;
        private String userid;
        private int matchid;
        private int leagueid;
        private int type;
        private int status;
        private String createtime;
        private String updatetime;
        private int oddsid;
        private int releaseType;
        private String tjMutilDetail;
        private int hideContent;
        private int multiType;
        private int lookCount;
        private int sportType;
        private String leagueName;
        private String homeName;
        private String homeImg;
        private String guestName;
        private String guestImg;
        private String serNum;
        private int matchStatus;
        private long matchDateTime;
        private String handicap;
        private String res;
        private double leftOdds;
        private double minOdds;
        private double rightOdds;
        private int countOrder;
        private int winsRate;
        private int earningsRate;
        private int winPoint;
        private String mutilDetailList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getChoose() {
            return choose;
        }

        public void setChoose(String choose) {
            this.choose = choose;
        }

        public String getChoose1() {
            return choose1;
        }

        public void setChoose1(String choose1) {
            this.choose1 = choose1;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public int getMatchid() {
            return matchid;
        }

        public void setMatchid(int matchid) {
            this.matchid = matchid;
        }

        public int getLeagueid() {
            return leagueid;
        }

        public void setLeagueid(int leagueid) {
            this.leagueid = leagueid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public int getOddsid() {
            return oddsid;
        }

        public void setOddsid(int oddsid) {
            this.oddsid = oddsid;
        }

        public int getReleaseType() {
            return releaseType;
        }

        public void setReleaseType(int releaseType) {
            this.releaseType = releaseType;
        }

        public String getTjMutilDetail() {
            return tjMutilDetail;
        }

        public void setTjMutilDetail(String tjMutilDetail) {
            this.tjMutilDetail = tjMutilDetail;
        }

        public int getHideContent() {
            return hideContent;
        }

        public void setHideContent(int hideContent) {
            this.hideContent = hideContent;
        }

        public int getMultiType() {
            return multiType;
        }

        public void setMultiType(int multiType) {
            this.multiType = multiType;
        }

        public int getLookCount() {
            return lookCount;
        }

        public void setLookCount(int lookCount) {
            this.lookCount = lookCount;
        }

        public int getSportType() {
            return sportType;
        }

        public void setSportType(int sportType) {
            this.sportType = sportType;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getHomeName() {
            return homeName;
        }

        public void setHomeName(String homeName) {
            this.homeName = homeName;
        }

        public String getHomeImg() {
            return homeImg;
        }

        public void setHomeImg(String homeImg) {
            this.homeImg = homeImg;
        }

        public String getGuestName() {
            return guestName;
        }

        public void setGuestName(String guestName) {
            this.guestName = guestName;
        }

        public String getGuestImg() {
            return guestImg;
        }

        public void setGuestImg(String guestImg) {
            this.guestImg = guestImg;
        }

        public String getSerNum() {
            return serNum;
        }

        public void setSerNum(String serNum) {
            this.serNum = serNum;
        }

        public int getMatchStatus() {
            return matchStatus;
        }

        public void setMatchStatus(int matchStatus) {
            this.matchStatus = matchStatus;
        }

        public long getMatchDateTime() {
            return matchDateTime;
        }

        public void setMatchDateTime(long matchDateTime) {
            this.matchDateTime = matchDateTime;
        }

        public String getHandicap() {
            return handicap;
        }

        public void setHandicap(String handicap) {
            this.handicap = handicap;
        }

        public String getRes() {
            return res;
        }

        public void setRes(String res) {
            this.res = res;
        }

        public double getLeftOdds() {
            return leftOdds;
        }

        public void setLeftOdds(double leftOdds) {
            this.leftOdds = leftOdds;
        }

        public double getMinOdds() {
            return minOdds;
        }

        public void setMinOdds(double minOdds) {
            this.minOdds = minOdds;
        }

        public double getRightOdds() {
            return rightOdds;
        }

        public void setRightOdds(double rightOdds) {
            this.rightOdds = rightOdds;
        }

        public int getCountOrder() {
            return countOrder;
        }

        public void setCountOrder(int countOrder) {
            this.countOrder = countOrder;
        }

        public int getWinsRate() {
            return winsRate;
        }

        public void setWinsRate(int winsRate) {
            this.winsRate = winsRate;
        }

        public int getEarningsRate() {
            return earningsRate;
        }

        public void setEarningsRate(int earningsRate) {
            this.earningsRate = earningsRate;
        }

        public int getWinPoint() {
            return winPoint;
        }

        public void setWinPoint(int winPoint) {
            this.winPoint = winPoint;
        }

        public String getMutilDetailList() {
            return mutilDetailList;
        }

        public void setMutilDetailList(String mutilDetailList) {
            this.mutilDetailList = mutilDetailList;
        }
    }
}
