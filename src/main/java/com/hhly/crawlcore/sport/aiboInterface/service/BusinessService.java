package com.hhly.crawlcore.sport.aiboInterface.service;

/**
 * Created by chenkangning on 2017/8/1.
 */
public interface BusinessService {

    /**
     * 接口入口
     */
    void into();

    /**
     * 每周2中午12点以前
     * 处理北单上一期的数据
     */
    void processPreviousIssueData();
}
