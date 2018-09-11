package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportDataBbWfPO;

public interface SportDataBbWfDaoMapper {

    //v2.0版本,数据抓取--------start
    int selectCount(SportDataBbWfPO record);

    int insert(SportDataBbWfPO record);
    
    int insertBatch(List<SportDataBbWfPO> list);
    
    SportDataBbWfPO findLast(@Param("sportAgainstInfoId") Long id, @Param("spType") Short spType);
    
    //v2.0版本,数据抓取--------end
}