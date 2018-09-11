package com.hhly.crawlcore.sport.service;


import com.hhly.crawlcore.persistence.sport.po.SportDataBjHfWdfPO;

/**
 * @Description:北京单彩半场胜平负
 * @author: ttw
 * @Date:   2017/6/5
 * @Version:
 */
public interface SportDataBjHfWdfService {

    /**
     * 保存北京单场半全场历史sp值
     *
     * @param record
     * @return
     */
    void save(SportDataBjHfWdfPO record);
}
