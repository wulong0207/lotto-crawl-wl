package com.hhly.crawlcore.sport.service;

import com.hhly.skeleton.lotto.base.sport.bo.JclqTrendSpBO;

/**
 * @auth lgs on
 * @date 2017/2/13.
 * @desc 抓取竞彩篮球数据service
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public interface SportBaskBallService {

    /**
     * 抓取竞彩篮球暂定赛程
     *
     * @return 是否成功 默认为false未成功
     */
    void saveMatchInterim();

    /**
     * 抓取竞彩篮球受注赛程
     *
     * @return 是否成功 默认为false未成功
     */
    void saveMatchList();

    /**
     * 获取微信公开api接口对阵
     *
     * @return
     */
    void saveWxMatchList();

    /**
     * 获取对阵赔率历史
     *
     * @return 返回历史SP值变化
     */
    JclqTrendSpBO saveBkMatchMnl(String officialId, Long sportAgainstInfoId);

    /**
     * 获取所有销售对阵赔率历史
     *
     * @return
     */
    void saveBkMatchMnlList();

    /**
     * 获取对阵列表SP
     *
     * @return
     */
    void saveBkMatchOddList();

    void proceeSportBBData();

    /**
     * 同步一比分数据
     */
    void updateBasketBallForYbf();

    /**
     * 清楚竞彩足球系统编号缓存
     */
    void clearJclqCacheBySystemCode();

    /**
     * 清楚竞彩足球系统编号缓存
     */
    void clearJclqCacheByIssueCode();
}
