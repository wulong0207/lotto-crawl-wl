package com.hhly.crawlcore.persistence.sport.dao;

import java.util.List;

import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;

public interface SportMatchSourceInfoDaoMapper {

    int deleteById(Integer id);

    int insert(SportMatchSourceInfoPO po);
    
    void insertBatch(List<SportMatchSourceInfoPO> pos);

    SportMatchSourceInfoPO find(SportMatchSourceInfoPO po);

    int updateById(SportMatchSourceInfoPO po);
}