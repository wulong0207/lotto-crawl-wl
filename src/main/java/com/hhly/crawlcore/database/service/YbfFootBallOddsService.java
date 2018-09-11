package com.hhly.crawlcore.database.service;

import com.hhly.crawlcore.persistence.database.po.OddsFbEuropeOddsDetailPO;
import com.hhly.crawlcore.persistence.database.po.OddsFbEuropeOddsPO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @author lgs on
 * @version 1.0
 * @desc 一比分欧赔
 * @date 2017/11/15.
 * @company 益彩网络科技有限公司
 */
public interface YbfFootBallOddsService {

    /**
     * 根据一比分id获取一比分欧赔
     *
     * @param matchId
     * @return
     */
    Map<String, List<OddsFbEuropeOddsPO>> getYbfOdds(String matchId) throws IOException, URISyntaxException;

    /**
     * 保存欧赔数据
     *
     * @param po
     * @return
     */
    int saveOddsFbEuropeOddsPO(OddsFbEuropeOddsPO po);

    /**
     * 保存一个对阵所有欧赔初赔和实盘赔率
     *
     * @param matchId
     * @return
     */
    int saveOddsFbEuropeOddsPOs(String matchId) throws IOException, URISyntaxException;

    /**
     * 保存欧赔明细
     *
     * @param po
     * @return
     */
    int saveOddsDetail(OddsFbEuropeOddsDetailPO po);

}
