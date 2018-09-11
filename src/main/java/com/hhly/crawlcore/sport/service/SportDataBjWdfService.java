package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjWdfPO;

/**
 * @Description:北京单场胜平负
 * @author: ttw
 * @Date:   2017/6/5
 * @Version:
 */
public interface SportDataBjWdfService {

    /**
     * 保存北京单场胜平负历史sp值
     *
     * @param record
     * @return
     */
    void save(SportDataBjWdfPO record);
}
