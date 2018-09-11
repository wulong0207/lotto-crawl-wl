package com.hhly.crawlcore.database.service;

import com.hhly.crawlcore.persistence.database.po.FbSchedulePO;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author lgs on
 * @version 1.0
 * @desc 足球赛事对阵service
 * @date 2017/11/18.
 * @company 益彩网络科技有限公司
 */
public interface FbScheduleService {

    /**
     * 初始化赛事
     *
     * @return
     */
    int initFbSchedule() throws IOException, URISyntaxException;

    /**
     * 获取赛事
     *
     * @param po
     * @return
     */
    FbSchedulePO findFbSchedule(FbSchedulePO po);

    /**
     * 保存赛事
     *
     * @param po
     * @return
     */
    int saveFbSchedule(FbSchedulePO po);


}
