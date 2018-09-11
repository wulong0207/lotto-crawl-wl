package com.hhly.crawlcore.persistence.sport.dao;


import com.hhly.crawlcore.persistence.sport.po.SportDataFbSpPO;

public interface SportDataFbSpDaoMapper {

    //v2.0版本,数据抓取--------start
    int merge(SportDataFbSpPO po);

    //v2.0版本,数据抓取--------end
}