package com.hhly.crawlcore.sport.service.impl;

import com.hhly.crawlcore.persistence.sport.dao.SportStatusBjDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportStatusBjPO;
import com.hhly.crawlcore.sport.service.SportStatusBjService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/6/7.
 * @company 益彩网络科技有限公司
 */
@Service
public class SportStatusBjServiceImpl implements SportStatusBjService {

    @Resource
    private SportStatusBjDaoMapper sportStatusBjDaoMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(SportStatusBjPO record) {
        return sportStatusBjDaoMapper.insert(record);
    }

    @Override
    public SportStatusBjPO selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public List<SportStatusBjPO> selectAll() {
        return null;
    }

    @Override
    public int updateByPrimaryKey(SportStatusBjPO record) {
        return sportStatusBjDaoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SportStatusBjPO select(Map<String, Object> map) {
        return sportStatusBjDaoMapper.select(map);
    }
}
