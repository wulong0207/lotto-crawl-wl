package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportDataFbHfWdfPO;

public interface SportDataFbHfWdfDaoMapper {

    //v2.0版本,数据抓取--------start
	
    int insert(SportDataFbHfWdfPO record);

    int insertBatch(List<SportDataFbHfWdfPO> list);

    int selectCount(SportDataFbHfWdfPO po);
    
    SportDataFbHfWdfPO findLast(@Param("sportAgainstInfoId") Long id);
    
   //v2.0版本,数据抓取--------end
}