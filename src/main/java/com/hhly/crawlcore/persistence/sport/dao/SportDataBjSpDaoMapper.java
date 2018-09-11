package com.hhly.crawlcore.persistence.sport.dao;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjSpPO;

public interface SportDataBjSpDaoMapper {

    int insertSelective(SportDataBjSpPO record);

    int updateByPrimaryKeySelective(SportDataBjSpPO record);
    
    /**
     * 2.0版本
     */
    int merge(SportDataBjSpPO po);
    
    SportDataBjSpPO findByMatchId(Long sportAgainstInfoId);
}