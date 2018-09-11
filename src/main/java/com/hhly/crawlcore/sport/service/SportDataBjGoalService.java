package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjGoalPO;

/**
 * @Description: 北京单彩分数
 * @author: ttw
 * @Date:   2017/6/5
 * @Version:
 */
public interface SportDataBjGoalService {

    /**
     * 保存北京单场总进球王法历史sp值
     *
     * @param record
     * @return
     */
    void save(SportDataBjGoalPO record);
}
