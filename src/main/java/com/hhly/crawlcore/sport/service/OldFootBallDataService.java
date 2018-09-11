package com.hhly.crawlcore.sport.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.sport.entity.OldLotteryIssue;

/**
 * @auth lgs on
 * @date 2017/2/7.
 * @desc 抓取老足彩数据service
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public interface OldFootBallDataService {

    /**
     * 根据彩期获取老足彩对阵
     * @param lotteryIssue 彩期
     * @return 抓取是否成功
     */
    void saveMatchByLotteryIssue(String lotteryIssue, String lotteryType, Integer lottery);

    /**
     * 根据彩期集合保存所有老足彩的彩期最新对阵
     * @param lotteryIssues 彩期集合
     * @return 抓取是否成功
     */
    void saveMatchByLotteryIssueList(List<String> lotteryIssues, String lotteryType, Integer lottery);

    /**
     * 获取彩期的销售时间 开售时间 停售时间 计奖时间
     * @param lotteryIssue
     * @return
     */
    JSONObject getIssueTime(String lotteryIssue, String type);

    /**
     * 获取胜负彩包括14场和9场胜负彩对阵
     *
     * @return
     */
    void saveSfcMatch(String lotteryIssue, Integer lotteryCode);

    /**
     * 获取6场半全场对阵
     *
     * @return
     */
    void saveSixHFMatch(String lotteryIssue, Integer lotteryCode);


    /**
     * 获取4场进球对阵
     *
     * @return
     */
    void saveGoalMatch(String lotteryIssue, Integer lotteryCode);

    /**
     * 更新老足彩赛事数据
     */
    void updateOldMatchInfo();

    /**
     * 查询下一期彩期信息
     *
     * @param lotteryIssue 彩期
     * @param lotteryCode  彩种编号
     * @return
     */
    OldLotteryIssue getNextIssueInfo(String lotteryIssue, Integer lotteryCode);
}
