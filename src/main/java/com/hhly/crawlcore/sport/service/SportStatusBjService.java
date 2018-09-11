package com.hhly.crawlcore.sport.service;

import com.hhly.crawlcore.persistence.sport.po.SportStatusBjPO;

import java.util.List;
import java.util.Map;

/**
 * @Description:北京单场胜平负
 * @author: ttw
 * @Date:   2017/6/5
 * @Version:
 */
public interface SportStatusBjService {

    int deleteByPrimaryKey(Long id);

    int insert(SportStatusBjPO record);

    SportStatusBjPO selectByPrimaryKey(Long id);

    List<SportStatusBjPO> selectAll();

    int updateByPrimaryKey(SportStatusBjPO record);

    SportStatusBjPO select(Map<String, Object> map);
}
