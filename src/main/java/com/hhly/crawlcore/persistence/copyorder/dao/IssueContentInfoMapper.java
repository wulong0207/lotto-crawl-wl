package com.hhly.crawlcore.persistence.copyorder.dao;


import com.hhly.crawlcore.persistence.copyorder.po.IssueContentInfo;

public interface IssueContentInfoMapper {


    int insert(IssueContentInfo record);

    int queryIssueContentByOriginalIssueId(Integer sourceIssueId);


}