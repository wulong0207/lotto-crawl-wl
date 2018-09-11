package com.hhly.crawlcore.copyorder.service;

import com.hhly.skeleton.base.bo.ResultBO;

/**
 * @author yuanshangbing
 * @version 1.0
 * @desc 抄单接一比分赛事定时任务业务类
 * @date 2018/1/3 14:47
 * @company 益彩网络科技公司
 */
public interface CopyOrderService {

    /**
     * 接一比分赛事，专家入库。组建抄单信息
     * @return
     */
    ResultBO addIssueMatchAndUserInfo();

    /**
     * 更新专家，用户信息
     * @return
     */
    ResultBO updateUserIssueInfo();

}
