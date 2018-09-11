package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjUdsdPO;

/**
 * @Description: 竞技彩对阵详情
 * @author: ttw
 * @Date:   2017/6/5
 * @Version:
 */
public interface SportDataBjUdsService {

    /**
     * 保存北京单场上下单双历史sp值
     *
     * @param record
     * @return
     */
    void save(SportDataBjUdsdPO record);
}
