package com.hhly.crawlcore.sport.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.sport.aiboInterface.service.BusinessService;
import com.hhly.crawlcore.sport.quartz.SportBjdcQuartz;
import com.hhly.skeleton.base.bo.ResultBO;

/**
 * Created by chenkangning on 2017/7/17.
 * 北京单场
 */
@RestController
@RequestMapping(value = "/sport/bjdc")
public class SportBjdcController extends BaseController {


    @Resource
    private SportBjdcQuartz sportBjdcQuartz;

    @Resource
    private BusinessService businessService;


    /**
     * python北单对接入口
     */
    @RequestMapping(value = "/insert")
    public Object insert() {
        sportBjdcQuartz.bjdcSpTask();
        return ResultBO.ok();
    }

    /**
     * 爱波网北单对接入口
     */
    @RequestMapping(value = "/aiboDockInsert")
    public Object aiboDockInsert() {
        businessService.into();
        return ResultBO.ok();
    }

    /**
     * 北单获取上一期的数据
     */
    @RequestMapping(value = "/processPreviousIssueData")
    public Object processPreviousIssueData() {
        businessService.processPreviousIssueData();
        return ResultBO.ok();
    }
}
