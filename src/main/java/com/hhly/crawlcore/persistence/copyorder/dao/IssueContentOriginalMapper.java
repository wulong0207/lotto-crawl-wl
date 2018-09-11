package com.hhly.crawlcore.persistence.copyorder.dao;


import com.hhly.crawlcore.persistence.copyorder.po.IssueContentOriginal;

public interface IssueContentOriginalMapper {


    Integer insert(IssueContentOriginal record);

    /**
     * 查询最近一条原始数据的推荐ID集合
     * @return
     */
    String queryNewestRecord();


}