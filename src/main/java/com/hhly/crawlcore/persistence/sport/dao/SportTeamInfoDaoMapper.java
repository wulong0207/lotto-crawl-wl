package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;

public interface SportTeamInfoDaoMapper {


    
    //v2.0版本,数据抓取--------start
    
    int insert(SportTeamInfoPO po);
    
    int updateBatch(List<SportTeamInfoPO> pos);
    
    List<SportTeamInfoPO> findTeam(SportTeamInfoPO po);
    
    int update(SportTeamInfoPO po);
    
    SportTeamInfoPO findById(@Param("id") Long id);
    //v2.0版本,数据抓取--------end
}