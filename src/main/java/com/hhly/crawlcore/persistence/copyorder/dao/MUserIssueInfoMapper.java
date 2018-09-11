package com.hhly.crawlcore.persistence.copyorder.dao;


import com.hhly.crawlcore.persistence.copyorder.po.MUserIssueInfo;

import java.util.List;

public interface MUserIssueInfoMapper {


    int insert(MUserIssueInfo record);

    int updateByPrimaryKey(MUserIssueInfo record);

    /**
     * 查询一比分用户是否存在,返回推单主键ID
     * @param yibifenUserId
     * @return
     */
    Integer queryUserByYiBiFenUserId(String yibifenUserId);

    /**
     * 查询所有的一比分userId
     * @return
     */
    List<MUserIssueInfo> queryAllYiBiFenUserIds();

}