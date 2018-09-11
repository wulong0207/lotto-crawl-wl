package com.hhly.crawlcore.persistence.sport.dao;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjWfPO;

public interface SportDataSfWfDaoMapper {

    int insert(SportDataBjWfPO record);

    int selectByUnionValueCount(SportDataBjWfPO record);
}