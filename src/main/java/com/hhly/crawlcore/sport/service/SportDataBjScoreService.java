package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjScorePO;

/**
 * @Description: 北京单彩比分
 * @author: ttw
 * @Date:   2017/6/5
 * @Version:
 */
public interface SportDataBjScoreService {

    /**
     * 保存北京单场比分历史sp值
     *
     * @param record
     * @return
     */
    void save(SportDataBjScorePO record);
}
