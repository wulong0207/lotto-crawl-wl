package com.hhly.crawlcore.persistence.sport.dao;


import com.hhly.crawlcore.persistence.sport.po.SportStatusFbPO;

public interface SportStatusFbDaoMapper {

    //v2.0版本,数据抓取--------start
	
    int selectCount(SportStatusFbPO record);

    int insert(SportStatusFbPO record);
    
    int update(SportStatusFbPO record);
    
    //v2.0版本,数据抓取--------end
}