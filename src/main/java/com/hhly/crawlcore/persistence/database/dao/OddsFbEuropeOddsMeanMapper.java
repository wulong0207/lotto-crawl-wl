package com.hhly.crawlcore.persistence.database.dao;

import com.hhly.crawlcore.persistence.database.po.OddsFbEuropeOddsMeanPO;

public interface OddsFbEuropeOddsMeanMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OddsFbEuropeOddsMeanPO record);

    int insertSelective(OddsFbEuropeOddsMeanPO record);

    OddsFbEuropeOddsMeanPO selectByPrimaryKey(Integer id);

    OddsFbEuropeOddsMeanPO findAvgOdds(OddsFbEuropeOddsMeanPO record);

    int updateByPrimaryKeySelective(OddsFbEuropeOddsMeanPO record);

    int updateByPrimaryKey(OddsFbEuropeOddsMeanPO record);
}