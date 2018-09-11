package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjWfPO;

/**
 * @Description:北京单场胜负过关
 * @author: ttw
 * @Date: 2017/6/5
 * @Version:
 */
public interface SportDataBjWfService {

    /**
     * 保存北京单场胜平负历史sp值
     *
     * @param record
     * @return
     */
    void save(SportDataBjWfPO record);
}
