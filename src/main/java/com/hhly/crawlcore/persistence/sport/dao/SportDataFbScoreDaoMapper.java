package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportDataFbScorePO;

public interface SportDataFbScoreDaoMapper {
	
	//v2.0版本,数据抓取--------start
	
    int insert(SportDataFbScorePO record);

    int insertBatch(List<SportDataFbScorePO> list);

    int selectCount(SportDataFbScorePO po);
    
    SportDataFbScorePO findLast(@Param("sportAgainstInfoId") Long id);
  //v2.0版本,数据抓取--------end
}