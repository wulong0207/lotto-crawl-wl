package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;

/**
 * @author chengkangning
 * @version 1.0
 * @desc 11选5，快3，快乐10分彩果入库
 * @date 2017/10/11
 * @company 益彩网络科技公司
 */
@Deprecated
public interface HighDrawService {


    /**
     * 彩果入库
     *
     * @param lotteryIssuePO 数据对象
     * @throws Exception 异常
     */
    void inStock(LotteryIssuePO lotteryIssuePO) throws Exception;
}
