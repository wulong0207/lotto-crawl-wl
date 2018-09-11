package com.hhly.crawlcore.persistence.sport.dao;

import com.hhly.crawlcore.persistence.sport.po.SportDrawWfPO;

public interface SportDrawWfDaoMapper {

    int insertSelective(SportDrawWfPO sportDrawWfPO);

    int updateSelective(SportDrawWfPO sportDrawWfPO);
    
    //抓取2.0版本
    int merge(SportDrawWfPO po);
}