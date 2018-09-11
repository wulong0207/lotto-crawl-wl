package com.hhly.crawlcore.sport.service;

import java.util.List;

import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;

/**
 * @auth lgs on
 * @date 2017/2/13.
 * @desc 抓取数据service
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public interface CrawlerService {

    /**
     * 获取竞彩足球，竞彩篮球暂定赛程
     *
     * @param url         url
     * @param lotteryCode 彩种编号
     * @return 暂定赛程集合
     */
    List<SportAgainstInfoPO> crawlerMatchInterim(String url, Integer lotteryCode, String matchType);

    /**
     * 获取微信公开对阵API
     *
     * @param url         url
     * @param lotteryCode 彩种编号
     * @param matchType   赛事类型
     * @return
     */
    List<SportAgainstInfoPO> crawlerWxMatchList(String url, Integer lotteryCode, String matchType);


    /**
     * 对接一比分竞彩足球数据
     *
     * @param lotteryCode
     * @param type
     * @return
     */
    void crawlerYbfJczqMatch();
    
    /**
     * 对接一比分竞彩篮球数据
     *
     * @param lotteryCode
     * @param type
     * @return
     */
    void crawlerYbfJclqMatch();

}
