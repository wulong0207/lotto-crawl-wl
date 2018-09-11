package com.hhly.crawlcore.persistence.sport.dao;

import com.hhly.crawlcore.persistence.sport.po.SportDrawFbPO;

public interface SportDrawFbMapper {


    int insert(SportDrawFbPO sportDrawFbPO);

    int insertSelective(SportDrawFbPO sportDrawFbPO);


    int updateByPrimaryKey(SportDrawFbPO sportDrawFbPO);

    SportDrawFbPO selectCondition(SportDrawFbPO sportDrawFbPO);
}