package com.hhly.crawlcore.sport.service;

/**
 * 一比分接口
 * Created by chenkangning on 2017/9/7.
 */
public interface YbfImmediateService {

    /**
     * 一比分即时比分接口
     * 获取竞彩比分列表数据
     */
    void processImmediateScore() throws Exception;

    /**
     * http://live.13322.com/score.refreshState.do
     * 刷新比赛数据
     */
    void refreshState() throws Exception;

    /**
     * 一分比竞篮即时比分
     *
     * @throws Exception 异常
     */
    void processBasketScore() throws Exception;
}
