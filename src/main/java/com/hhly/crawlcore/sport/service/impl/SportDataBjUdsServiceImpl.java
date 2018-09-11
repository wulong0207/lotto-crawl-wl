package com.hhly.crawlcore.sport.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjUdsdPO;
import com.hhly.crawlcore.sport.service.SportBjMatchService;
import com.hhly.crawlcore.sport.service.SportDataBjSpService;
import com.hhly.crawlcore.sport.service.SportDataBjUdsService;

/**
 * @Description:
 * @author:
 * @Date:
 * @Version:
 */

@Service
public class SportDataBjUdsServiceImpl implements SportDataBjUdsService {

    @Autowired
    private SportDataBjSpService sportDataBjSpService;

    @Autowired
    private SportBjMatchService sportBjMatchService;


    /**
     * 保存北京单场上下单双历史sp值
     *
     * @param record
     * @return
     */
    @Override
    public void save(SportDataBjUdsdPO record) {
        try {
            sportBjMatchService.save(record);
            sportDataBjSpService.save(new SportDataBjSpPO(record));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
