package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportDataFbWdfPO;

public interface SportDataFbWdfDaoMapper {

    //v2.0版本,数据抓取--------start
	
    int insert(SportDataFbWdfPO record);

    int insertBatch(List<SportDataFbWdfPO> list);

    /**
     * 查询数据库数据有没有编号
     *
     * @param record
     * @return
     */
    int selectCount(SportDataFbWdfPO record);
    
    SportDataFbWdfPO findLast(@Param("sportAgainstInfoId") Long id, @Param("spType") Short spType);
    
    //v2.0版本,数据抓取--------end
}