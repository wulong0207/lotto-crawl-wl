package com.hhly.crawlcore.sport.util;


import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.sport.common.Constants;
import com.hhly.crawlcore.sport.common.FootBallPlayStatusEnum;
import com.hhly.crawlcore.sport.common.MatchStatusEnum;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.NumberFormatUtil;
import com.hhly.skeleton.base.util.StringUtil;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by lgs on 2017/1/11.
 * 竞技彩一些公共处理方法工具类
 */
public class SportLotteryUtil {


    /**
     * 获取北京单场开售日期
     *
     * @param startTime 比赛时间
     * @return date 开售日期
     */
    public static Date getBjSaleDate(Date startTime) {

        Date tomorrow = DateUtil.getDateDit(startTime, 1);
        //System.out.println("tomorrow = " + DateUtil.convertDateToStr(tomorrow,DateUtil.DATE_FORMAT));
        Date today12 = get12Noon(startTime);
        Date tomorrow12 = get12Noon(tomorrow);
        //System.out.println("tomorrow12 = " + DateUtil.convertDateToStr(tomorrow12));
        int a = DateUtil.compare(startTime, today12);//1
        int b = DateUtil.compare(tomorrow12, startTime);//1
        if (a == 1 && b == 1) {
            //判断比赛时间大于今天12:00并小于明天12:00，直接返回比赛日期
            return startTime;
        } else if (a == 0 && b == 1) {
            return startTime;
        } else {
            if (a == -1) {
                return DateUtil.getDateDit(startTime, -1);
            } else {
                //否则返回明天
                return tomorrow;
            }
        }

    }


    /**
     * 获取指定日期的12点
     *
     * @param date 指定日期
     * @return 指定日期的12点
     */
    private static Date get12Noon(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取销售截止时间
     *
     * @param matchTime 比赛时间
     * @param issueCode 彩期编号
     * @param issueInfo 彩期信息
     * @return
     */
    public static Date getSaleEndTime(Date matchTime, String issueCode, List<LotteryIssuePO> issueInfo) {
        LotteryIssuePO po = getLotteryIssuePO(issueCode, issueInfo);

        long matchTimeLong = matchTime.getTime();
        long saleEndTime = po.getSaleEndTime().getTime();
        long officialEndTime = po.getOfficialEndTime().getTime();
        long saleTime = po.getSaleTime().getTime();
        long result = 0L;
        long timePoor = officialEndTime - saleEndTime;

        LotteryIssuePO nextPo = getLotteryIssuePO(DateUtil.convertDateToStr(DateUtil.getDateDit(DateUtil.convertStrToDate(issueCode, DateUtil.FORMAT_YYMMDD), 1), DateUtil.FORMAT_YYMMDD), issueInfo);

        //如果比赛时间小于本站销售截止时间并且大于彩期销售开始时间+30分钟 销售截止时间为 比赛时间减去官方销售截止时间与本站销售截止时间时间差。或者小于下一期比赛时间大于销售时间+30分钟的比赛。比赛时间减去官方销售截止时间与本站销售截止时间时间差
        if ((matchTimeLong < saleEndTime && matchTimeLong > (saleTime + 1800000) || (matchTimeLong < nextPo.getSaleEndTime().getTime() && matchTimeLong >= (nextPo.getSaleTime().getTime() + 1800000)))) {
            result = matchTimeLong - timePoor;
        } else if (matchTimeLong > saleTime && matchTimeLong <= (saleTime + 1800000)) {//如果大于销售时间，但是小于销售时间加30分钟的比赛。会提前至彩期上一期销售截止时间
            LotteryIssuePO prevPo = getLotteryIssuePO(DateUtil.convertDateToStr(DateUtil.getDateDit(DateUtil.convertStrToDate(issueCode, DateUtil.FORMAT_YYMMDD), -1), DateUtil.FORMAT_YYMMDD), issueInfo);
            result = prevPo.getSaleEndTime().getTime();
        } else { //否则取本站销售截止时间。
            result = saleEndTime;
        }


        Date issueDate = DateUtil.convertStrToDate(issueCode, DateUtil.FORMAT_YYMMDD);
        String matchIssue = DateUtil.convertDateToStr(matchTime, DateUtil.FORMAT_YYMMDD);

        //如果比赛时间格式化后减去比赛所属彩期大于2天说明比赛时间发生了变化。
        if (matchTimeLong - issueDate.getTime() >= 172800000L) {
            po = getLotteryIssuePO(String.valueOf(Integer.valueOf(matchIssue)), issueInfo);
            LotteryIssuePO prevPo = getLotteryIssuePO(DateUtil.convertDateToStr(DateUtil.getDateDit(matchTime, -1), DateUtil.FORMAT_YYMMDD), issueInfo);
            //如果比赛时间日期小于本期销售开始时间
            if (matchTimeLong > prevPo.getSaleEndTime().getTime() && matchTimeLong < (po.getSaleTime().getTime() + 1800000)) {
                result = prevPo.getSaleEndTime().getTime();
            } else if (matchTimeLong < po.getSaleEndTime().getTime()) {
                timePoor = po.getOfficialEndTime().getTime() - po.getSaleEndTime().getTime();
                result = matchTimeLong - timePoor;
            } else {
                result = po.getSaleEndTime().getTime();
            }
        }

        return new Date(result);
    }

    /**
     * 根据彩期获取彩期信息
     *
     * @param issueCode
     * @param issueInfo
     * @return
     */
    private static LotteryIssuePO getLotteryIssuePO(String issueCode, List<LotteryIssuePO> issueInfo) {
        LotteryIssuePO po = null;
        for (int i = 0; i < issueInfo.size(); i++) {
            po = issueInfo.get(i);
            if (issueCode.equals(po.getIssueCode())) {
                return po;
            }
        }
        return po;
    }


    /**
     * 获取系统编号 ,以及彩期
     *
     * @param official  竞彩官方编号
     * @param issueCode 彩期
     * @return 系统编号
     */
    public static String getSystemCode(String official, String issueCode) {
        String week = official.substring(0, 2);
        String code = official.substring(2, official.length());
        Integer weekNum = Integer.parseInt(Constants.weekTxtMap.get(week));
        return issueCode + weekNum + code;
    }


    /**
     * 获取比赛状态
     *
     * @param matchStatusText
     * @return
     */
    public static String getMatchStatus(String matchStatusText) {
        String matchStatus = null;

        if (StringUtil.isBlank(matchStatusText)) {
            return String.valueOf(MatchStatusEnum.NOT_SALE.getCode());
        }

        for (MatchStatusEnum enums : MatchStatusEnum.values()) {
            if (matchStatusText.equals(enums.getName())) {
                return String.valueOf(enums.getCode());
            }
        }

        return matchStatus;
    }


    /**
     * 获取子玩法状态
     *
     * @param status
     * @return
     */
    public static String getChildPlayStatus(String status) {
        String childPlayStatus = null;

        if (status == null || status.equals("")) {
            return SportEnum.SportSaleStatusEnum.STOP_SALE.getValue().toString();//暂停销售
        }

        for (FootBallPlayStatusEnum enums : FootBallPlayStatusEnum.values()) {
            if (enums.getName().equals(status)) {
                return String.valueOf(enums.getCode());
            }
        }

        return childPlayStatus;
    }

    /**
     * 获取老足彩系统编号
     *
     * @param lotteryIssue 彩期
     * @param i            索引
     * @return 系统编号
     */
    public static String getOldLotterySystemCode(String lotteryIssue, int i) {
        return lotteryIssue + NumberFormatUtil.formatZeroFill(i, com.hhly.skeleton.base.constants.Constants.NUM_3);
    }


    /**
     * 获取过关方式
     *
     * @param single
     * @return
     */
    public static Short getSaleType(String single) {
        if (StringUtil.isBlank(single)) {
            return SportEnum.SportSaleStatusEnum.STOP_SALE.getValue().shortValue();//暂停销售
        }

        if (single.equals("0")) {
            return SportEnum.SportSaleStatusEnum.PASS_SALE.getValue().shortValue();//销售过关
        } else if (single.equals("1")) {
            return SportEnum.SportSaleStatusEnum.NORMAL_SALE.getValue().shortValue();//正常销售
        } else {
            return SportEnum.SportSaleStatusEnum.STOP_SALE.getValue().shortValue();//暂停销售
        }
    }

    /**
     * 获取北京单场系统编号
     *
     * @param biNum
     * @return
     */
    public static String getBjSystemCode(Integer biNum, String issueCode) {
        //int weekNum = DateUtil.dayForWeek(saleDate);
        //String code = NumberFormatUtil.formatZeroFill(biNum, com.hhly.skeleton.base.constants.Constants.NUM_3);
        //刘琼要求直接是期号+官主编码
        return issueCode + biNum;
    }


    /**
     * 北单爱波对接处理彩期
     *
     * @param code 爱波彩期号
     * @return
     */
    public static String handleBjIssueCode(String code) {
        return DateUtil.getNow("yy").substring(0, 1) + code;
    }

    /**
     * 获取北京单场系统编号
     *
     * @param startTime  比赛时间
     * @param buyEndTime 销售截止提前多少秒时间。
     * @return
     */
    public static Date getBjSaleEndTime(Date startTime, Integer buyEndTime) {
        //转换时间，这个时间段不出票
        Date sixDateTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(startTime, DateUtil.DATETIME_FORMAT_BJ_SALE_END_START_TIME), DateUtil.DATETIME_FORMAT_NO_SEC);
        Date nineDateTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(startTime, DateUtil.DATETIME_FORMAT_BJ_SALE_START_TIME), DateUtil.DATETIME_FORMAT_NO_SEC);
        long result = startTime.getTime();
        buyEndTime = buyEndTime * 1000;
        if (result <= nineDateTime.getTime()
                && result > sixDateTime.getTime()) {
            result = sixDateTime.getTime() + buyEndTime;
        } else {
            result = result + buyEndTime;
        }
        return new Date(result);
    }
    
    public static String getNameMd5(String name, Short type) throws UnsupportedEncodingException{
    	return DigestUtils.md5DigestAsHex(String.valueOf(name + type).getBytes("UTF-8"));
    }


    public static void main(String[] args) throws Exception {
//        StringBuffer issueCode = new StringBuffer();
//        StringBuffer saleEenTime = new StringBuffer();
        //String systemCode = SportLotteryUtil.getSystemCode("周日090", "2017-03-20 12:30", issueCode, saleEenTime, 600);
        //System.out.println(String.format("issueCode: %s,saleEenTime: %s,systemCode: %s", new Object[]{issueCode.toString(), saleEenTime.toString(), systemCode}));
//        Date date = getBjSaleDate(DateUtil.convertStrToDate("2017-08-07 02:20", DateUtil.DATETIME_FORMAT_NO_SEC));
//        String dateStr = DateUtil.convertDateToStr(date);
//        System.out.println(dateStr);
    	
    	System.out.println(DateUtil.convertDateToStr(getBjSaleEndTime(DateUtil.convertStrToDate("2017-10-27 09:30:00"), -1800)));
    	
//    	List<LotteryIssuePO> list = new ArrayList<>();
//    	LotteryIssuePO po = new LotteryIssuePO();
//    	po.setIssueCode("171031");
//    	po.setLotteryCode(301);
//    	po.setOfficialEndTime(DateUtil.convertStrToDate("2017-11-01 00:00:00"));
//    	po.setSaleTime(DateUtil.convertStrToDate("2017-10-31 09:00:00"));
//    	po.setSaleEndTime(DateUtil.convertStrToDate("2017-10-31 23:50:00"));
//    	list.add(po);
//    	LotteryIssuePO po1 = new LotteryIssuePO();
//    	po1.setIssueCode("171101");
//    	po1.setLotteryCode(301);
//    	po1.setOfficialEndTime(DateUtil.convertStrToDate("2017-11-02 00:00:00"));
//    	po1.setSaleTime(DateUtil.convertStrToDate("2017-11-01 07:30:00"));
//    	po1.setSaleEndTime(DateUtil.convertStrToDate("2017-11-01 23:50:00"));
//    	list.add(po1);
//    	System.out.println(DateUtil.convertDateToStr(getSaleEndTime(DateUtil.convertStrToDate("2017-11-01 8:00:00"), "171031", list)));
    }
}
