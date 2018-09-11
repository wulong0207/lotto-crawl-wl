package com.hhly.crawlcore.copyorder.controller;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.copyorder.service.CopyOrderService;
import com.hhly.skeleton.base.bo.ResultBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanshangbing
 * @version 1.0
 * @desc 抄单接一比分赛事定时任务入口
 * @date 2018/1/3 12:21
 * @company 益彩网络科技公司
 */
@RestController
@RequestMapping("/copyOrder-init")
public class CopyOrderController extends BaseController {

    @Autowired
    private CopyOrderService copyOrderService;


    @RequestMapping("/addCopyOrderInfo")
    public ResultBO addMatchAndUserInfo(){
         return copyOrderService.addIssueMatchAndUserInfo();
    }




}
