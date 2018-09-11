package com.hhly.crawlcore.persistence.sport.dao;


import com.hhly.crawlcore.persistence.sport.po.SportStatusBbPO;

public interface SportStatusBbDaoMapper {
    
    //v2.0版本,数据抓取--------start
	
    int insert(SportStatusBbPO po);
    
    int update(SportStatusBbPO record);
    
    int selectCount(SportStatusBbPO record);
    
    int merge(SportStatusBbPO po);
    //v2.0版本,数据抓取--------end

}