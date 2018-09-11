package com.hhly.crawlcore.v2.plugin.lottery.high.service;

import com.hhly.crawlcore.persistence.lottery.po.LotteryIssueSourcePO;
import com.hhly.skeleton.base.bo.ResultBO;

/**
 * @author chengkangning
 * @version 1.0
 * @desc 高频、地方彩彩果入库V2.0
 * @date 2017/11/16
 * @company 益彩网络科技公司
 */
public interface HighDrawV2Service {

    /**
     * 彩果入库
     *
     * @param lotteryIssueSourcePO 数据对象
     * @throws Exception 异常
     */
    void insert(LotteryIssueSourcePO lotteryIssueSourcePO) throws Exception;

    /**
     * 合并彩果到彩期表里面去
     *
     * @throws Exception Exception
     */
    void mergeDraw() throws Exception;

    /**
     * 处理非凡开奖时间
     *
     * @throws Exception Exception
     */
    Boolean processLotteryTime(Integer source) throws Exception;


    /**
     * lotto-task 彩期开奖号码抓取入库修改
     *
     * @param lotteryCode 彩种
     * @param issueCode   彩期
     * @return String 彩果
     */
    ResultBO<?> querySingleDrawCode(Integer lotteryCode, String issueCode) throws Exception;
}
