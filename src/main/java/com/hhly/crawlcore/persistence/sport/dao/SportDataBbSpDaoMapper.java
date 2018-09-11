package com.hhly.crawlcore.persistence.sport.dao;


import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportDataBbSpPO;

public interface SportDataBbSpDaoMapper {

    //v2.0版本,数据抓取--------start
    
    int merge(SportDataBbSpPO pos);
    
    SportDataBbSpPO findByMatchId(@Param("sportAgainstInfoId") Long sportAgainstInfoId);
    
    //v2.0版本,数据抓取--------end
}