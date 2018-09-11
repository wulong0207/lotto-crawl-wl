package com.hhly.crawlcore.sport.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.hhly.crawlcore.persistence.sport.po.SportDataFbSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchOddPO;
import com.hhly.skeleton.lotto.base.sport.bo.JcAvgOddsBO;

/**
 * Created by lgs on 2017/1/5.
 * 抓取竞彩足球数据
 */
public interface SportFootballService {

    /**
     * 抓取竞彩足球受注赛程
     *
     * @return 是否成功 默认为false未成功
     */
    void saveMatchList();

    /**
     * 抓取竞彩足球暂定赛程
     *
     * @return 是否成功 默认为false未成功
     */
    void saveMatchInterim();

    /**
     * 抓取竞彩足球对阵胜负以及对阵各项赔率变化
     *
     * @return 是否成功 默认为false未成功
     */
    Map<String, Object> savePoolResult(String officialId, Long sportAgainstInfoId);

    /**
     * 抓取所有官方id不为null and 比赛状态为未开赛和已开售的
     * @return
     */
    void savePoolResultList() throws InterruptedException, ExecutionException;

    /**
     * 获取当天赛程天气
     * @return
     */
//    void saveMatchInfo();

    /**
     * 获取微信公开api接口对阵
     * @return
     */
    void saveWxMatchList();


    /**
     * 抓取竞彩足球受注赛程最新赔率
     * @return
     */
    void saveWxMatchOddList() throws InterruptedException;

    /**
     * 抓取竞彩足球受注赛程最新赔率
     * @return
     */
    SportDataFbSpPO saveWxMatchOdd(String officialId, Long sportAgainstInfoId);


    /**
     * 同步一比分数据
     */
    void updateYbfData();

    /**
     * 保存欧赔信息
     * @param avgs
     * @return
     */
    int insertMatchOdds(List<SportMatchOddPO> pos );

    /**
     * 更新平均赔率
     * @param avgs
     * @return
     */
    int updateAvgOdds(List<JcAvgOddsBO> avgs);

    /**
     * 清楚竞彩足球系统编号缓存
     */
    void clearJczqCacheBySystemCode();

    /**
     * 清楚竞彩足球系统编号缓存
     */
    void clearJczqCacheByIssueCode();
}
