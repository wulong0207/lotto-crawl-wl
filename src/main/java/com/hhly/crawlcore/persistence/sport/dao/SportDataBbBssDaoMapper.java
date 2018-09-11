package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportDataBbBssPO;

public interface SportDataBbBssDaoMapper {


    //v2.0版本,数据抓取--------start
    int selectCount(SportDataBbBssPO record);

    int insert(SportDataBbBssPO record);
    
    int insertBatch(List<SportDataBbBssPO> list);
    
    SportDataBbBssPO findLast(@Param("sportAgainstInfoId") Long id);
    //v2.0版本,数据抓取--------end
}