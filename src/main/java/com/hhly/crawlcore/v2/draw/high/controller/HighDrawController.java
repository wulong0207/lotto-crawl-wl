package com.hhly.crawlcore.v2.draw.high.controller;

import com.hhly.crawlcore.v2.plugin.lottery.high.service.HighDrawV2Service;
import com.hhly.skeleton.base.bo.ResultBO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chengkangning
 * @version 1.0
 * @desc 高频彩controller
 * @date 2017/11/21
 * @company 益彩网络科技公司
 */
@RestController
@RequestMapping(value = "/high/v2.0")
public class HighDrawController {

    @Resource
    private HighDrawV2Service highDrawV2Service;

    /**
     * 合并到彩期表里
     */
    @RequestMapping(value = "/mergeData")
    public Object mergeData() throws Exception {
        highDrawV2Service.mergeDraw();
        return ResultBO.ok();
    }


    /**
     * 提供给开奖的接口
     *
     * @param lotteryCode 彩种
     * @param issueCode   彩期
     * @return 对应腰果
     * @throws Exception Exception
     */
    @RequestMapping(value = "/findSingleDrawCode")
    public Object findSingleDrawCode(Integer lotteryCode, String issueCode) throws Exception {
        return highDrawV2Service.querySingleDrawCode(lotteryCode, issueCode);
    }
}
