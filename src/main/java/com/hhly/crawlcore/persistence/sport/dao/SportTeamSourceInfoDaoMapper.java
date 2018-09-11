package com.hhly.crawlcore.persistence.sport.dao;

import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;

public interface SportTeamSourceInfoDaoMapper {
	
	SportTeamSourceInfoPO find(SportTeamSourceInfoPO po);

    int deleteById(Integer id);
    
    int insert(SportTeamSourceInfoPO po);

    int updateById(SportTeamSourceInfoPO po);
}