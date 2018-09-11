package com.hhly.crawlcore.sport.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.persistence.sport.dao.SportDataSfWfDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjWfPO;
import com.hhly.crawlcore.sport.service.SportBjMatchService;
import com.hhly.crawlcore.sport.service.SportDataBjSpService;
import com.hhly.crawlcore.sport.service.SportDataBjWfService;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/6/14.
 * @company 益彩网络科技有限公司
 */
@Service
public class SportDataBjWfServiceImpl implements SportDataBjWfService {

    @Autowired
    private SportDataSfWfDaoMapper sportDataSfWfDaoMapper;

    @Autowired
    private SportBjMatchService sportBjMatchService;

    @Autowired
    private SportDataBjSpService sportDataBjSpService;

    /**
     * 保存北京单场胜平负历史sp值
     *
     * @param record
     * @return
     */
    @Override
    public void save(SportDataBjWfPO record) {
        try {
            sportBjMatchService.save(record);
            sportDataBjSpService.save(new SportDataBjSpPO(record));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
