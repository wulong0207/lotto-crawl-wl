package com.hhly.crawlcore.persistence.database.dao;

import com.hhly.crawlcore.persistence.database.po.OddsEuropePO;

public interface OddsEuropePOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OddsEuropePO record);

    int insertSelective(OddsEuropePO record);


    OddsEuropePO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OddsEuropePO record);

    int updateByPrimaryKey(OddsEuropePO record);
}