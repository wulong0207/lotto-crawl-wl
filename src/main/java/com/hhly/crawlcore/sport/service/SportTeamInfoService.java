package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;

/**
 * @auth lgs on
 * @date 2017/2/9.
 * @desc 球队信息Service
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
public interface SportTeamInfoService {

    //v2.0版本--------start
    
    /**
     * 获取球队详情
     *
     * @param po 球队信息
     * @return 球队信息
     */
    SportTeamInfoPO findTeam(SportTeamInfoPO po);
    
    /**
     * 获取益彩球队详情
     * @return
     * @date 2017年11月16日下午12:20:16
     * @author cheng.chen
     */
    SportTeamInfoPO getTeamInfo(SportTeamSourceInfoPO po);
    
    //v2.0版本--------end
}
