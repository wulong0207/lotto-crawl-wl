package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportDataFbGoalPO;

public interface SportDataFbGoalDaoMapper {

    //v2.0版本,数据抓取--------start
    
    int insertBatch(List<SportDataFbGoalPO> list);
    
    int insert(SportDataFbGoalPO record);

    int selectCount(SportDataFbGoalPO po);
    
    SportDataFbGoalPO findLast(@Param("sportAgainstInfoId") Long id);
    
    //v2.0版本,数据抓取--------end

}