package com.hhly.crawlcore.persistence.database.dao;

import com.hhly.crawlcore.persistence.database.po.OddsFbEuropeOddsPO;

public interface OddsFbEuropeOddsPOMapper {


    int deleteByPrimaryKey(Integer id);

    int insert(OddsFbEuropeOddsPO record);

    int insertSelective(OddsFbEuropeOddsPO record);

    OddsFbEuropeOddsPO selectByPrimaryKey(Integer id);

    OddsFbEuropeOddsPO findOddsBySourceId(OddsFbEuropeOddsPO record);

    int updateByPrimaryKeySelective(OddsFbEuropeOddsPO record);

    int updateByPrimaryKey(OddsFbEuropeOddsPO record);
}