package com.hhly.crawlcore.sport.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.persistence.sport.po.SportDataBjHfWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjSpPO;
import com.hhly.crawlcore.sport.service.SportBjMatchService;
import com.hhly.crawlcore.sport.service.SportDataBjHfWdfService;
import com.hhly.crawlcore.sport.service.SportDataBjSpService;

/**
 * @Description:
 * @author:
 * @Date:
 * @Version:
 */

@Service
public class SportDataBjHfWdfServiceImpl implements SportDataBjHfWdfService {

    @Autowired
    private SportDataBjSpService sportDataBjSpService;

    @Autowired
    private SportBjMatchService sportBjMatchService;


    /**
     * 保存北京单场半全场历史sp值
     *
     * @param record
     * @return
     */
    @Override
    public void save(SportDataBjHfWdfPO record) {
        try {
            sportBjMatchService.save(record);
            sportDataBjSpService.save(new SportDataBjSpPO(record));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
