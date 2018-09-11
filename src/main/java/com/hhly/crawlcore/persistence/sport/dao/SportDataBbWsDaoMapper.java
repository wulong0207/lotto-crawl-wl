package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportDataBbWsPO;

public interface SportDataBbWsDaoMapper {

    
    //v2.0版本,数据抓取--------start
    int selectCount(SportDataBbWsPO record);
    
    int insert(SportDataBbWsPO record);
    
    int insertBatch(List<SportDataBbWsPO> list);
    
    SportDataBbWsPO findLast(@Param("sportAgainstInfoId") Long id);
    //v2.0版本,数据抓取--------end
}