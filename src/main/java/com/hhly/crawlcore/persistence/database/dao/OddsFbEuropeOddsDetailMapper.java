package com.hhly.crawlcore.persistence.database.dao;

import com.hhly.crawlcore.persistence.database.po.OddsFbEuropeOddsDetailPO;

public interface OddsFbEuropeOddsDetailMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OddsFbEuropeOddsDetailPO record);

    int insertSelective(OddsFbEuropeOddsDetailPO record);

    OddsFbEuropeOddsDetailPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OddsFbEuropeOddsDetailPO record);

    int updateByPrimaryKey(OddsFbEuropeOddsDetailPO record);

    /**
     * 查询欧赔明细
     *
     * @param record
     * @return
     */
    OddsFbEuropeOddsDetailPO findOddsDetail(OddsFbEuropeOddsDetailPO record);
}