package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;

public interface SportMatchInfoDaoMapper {

    //v2.0版本,数据抓取--------start
    
    int insert(SportMatchInfoPO po);

    int update(SportMatchInfoPO po);
    
    int updateBatch(List<SportMatchInfoPO> pos);

    List<SportMatchInfoPO> findMatch(SportMatchInfoPO po);
    
    SportMatchInfoPO findById(@Param("id") Long id);
    //v2.0版本,数据抓取--------end
}