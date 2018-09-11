package com.hhly.crawlcore.persistence.sport.dao;

import java.util.Map;

import com.hhly.crawlcore.persistence.sport.po.SportStatusBjPO;

/**
 * 北京单场子玩法状态dao
 * Created by chenkangning on 2017/7/13.
 */
public interface SportStatusBjDaoMapper {


    int insert(SportStatusBjPO sportStatusBjPO);

    int updateByPrimaryKeySelective(SportStatusBjPO sportStatusBjPO);

    SportStatusBjPO select(Map<String, Object> map);
    
    // 2.0 新版 抓取 sql 
    SportStatusBjPO findByMatchId(Long sportAgainstInfoId);
    
    int merge(SportStatusBjPO po);

}
