package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjBasePO;

/**
 * @author lgs on
 * @version 1.0
 * @desc 北京单场对阵service
 * @date 2017/6/13.
 * @company 益彩网络科技有限公司
 */
public interface SportBjMatchService {

    /**
     * 保存北京单场对阵
     *
     * @param po
     */
    void save(SportDataBjBasePO po);

    /**
     * 保存竞篮数据
     *
     * @param po
     */
    void saveForBasketBall(SportAgainstInfoPO po);
}
