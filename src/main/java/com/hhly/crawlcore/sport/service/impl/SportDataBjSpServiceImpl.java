package com.hhly.crawlcore.sport.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBjSpDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjSpPO;
import com.hhly.crawlcore.sport.service.SportDataBjSpService;

/**
 * @Description:
 * @author: 
 * @Date:   
 * @Version: 
 */

@Service
public class SportDataBjSpServiceImpl implements SportDataBjSpService {

    @Autowired
    private SportDataBjSpDaoMapper sportDataBjSpDaoMapper;

    @Resource
    private LotteryIssueDaoMapper lotteryIssueMapper;

    @Override
    public int save(SportDataBjSpPO record) {
        int flag = sportDataBjSpDaoMapper.updateByPrimaryKeySelective(record);
        if (flag == 0) {
            flag = sportDataBjSpDaoMapper.insertSelective(record);
        }
        return flag;
    }
}
