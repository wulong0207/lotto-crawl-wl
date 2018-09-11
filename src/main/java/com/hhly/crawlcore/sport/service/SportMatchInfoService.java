package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;

/**
 * @auth lgs on
 * @date 2017/2/9.
 * @desc 赛事信息Service
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public interface SportMatchInfoService {

    //v2.0版本的方法  start -- 请不要覆盖
    
    /**
     * 查询赛事
     *
     * @param po
     * @return
     */
    SportMatchInfoPO findMatch(SportMatchInfoPO po);
    
    /**
     * 获取益彩球队id
     * @return
     * @date 2017年11月16日下午12:20:16
     * @author cheng.chen
     */
    SportMatchInfoPO getMatchInfo(SportMatchSourceInfoPO po);
    
    //v2.0版本的方法 end -- 请不要覆盖

}
