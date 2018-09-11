package com.hhly.crawlcore.persistence.database.dao;

import com.hhly.crawlcore.persistence.database.po.FbSchedulePO;

import java.util.List;
import java.util.Map;

public interface FbSchedulePOMapper {


    int deleteByPrimaryKey(Integer id);

    FbSchedulePO findScheduleBySourceId(FbSchedulePO record);

    int insert(FbSchedulePO record);

    int insertSelective(FbSchedulePO record);

    List<Map<String, String>> findInitSchedule();

    int updateSportAgainstInfoByMatchInfoUrl(Map<String, String> map);

    FbSchedulePO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FbSchedulePO record);

    int updateByPrimaryKey(FbSchedulePO record);
}