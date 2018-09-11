package com.hhly.crawlcore.persistence.sport.dao;

import com.hhly.crawlcore.persistence.sport.po.SportDrawBjPO;

/**
 * 北单除胜负过关外彩果dao
 * Created by dell on 2017/8/11.
 */
public interface SportDrawBjDaoMapper {

    int insertSelective(SportDrawBjPO sportDrawBjPO);

    int updateSelective(SportDrawBjPO sportDrawBjPO);
    
    int merge(SportDrawBjPO po);

}
