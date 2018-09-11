package com.hhly.crawlcore.base.zeroc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hhly.ybf.football.ServicePrxHelper;
import com.hhly.zeroc.basket.external.ExternalLotteryPrxHelper;


@Component
public class WebClientManager {

    // 代理名称
    private static final String DYBF_NAME = "ice-ybf-web";
    private static final String BASKEt_DYBF_NAME = "externalLottery";

    private static Logger logger = LoggerFactory.getLogger(WebClientManager.class);
    private static IceClient iceClient = new IceClient();
    /**
     * 一比分service
     */
    private static ServicePrxHelper fbService;

    /**
     * 竞彩篮球service
     */
    private static ExternalLotteryPrxHelper bbService;

    private static boolean fbInit = false;

    private static boolean bbInit = false;

    @Autowired
    @Qualifier("fbConnectionParam")
    private ConnectionParam fbConnectionParam;

    /**
     * 竞彩篮球对阵连接
     */
    @Autowired
    @Qualifier("bbConnectionParam")
    private ConnectionParam bbConnectionParam;

    /**
     * 查询赛事列表
     *
     * @param date
     * @param type
     * @return
     */
    public String getJcFbMatchByDate(String date) {
        String result = null;
        if (initFbProxy()) {
            try {
                result = fbService.getJcMatchByDate(date, "1");
            } catch (Exception e) {
                logger.error("WebClientManager.getJcFbMatchByDate is Error,date:" + date + ",Exception:" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 获取一比分竞彩篮球对阵数据
     *
     * @param date
     * @return
     */
    public String getJcBbMatchByDate(String date) {
        String result = null;
        if (initBbProxy()) {
            try {
                result = bbService.yicaiBasketData(date);
            } catch (Exception e) {
                logger.error("WebClientManager.getJcBbMatchByDate is Error,date:" + date + ",Exception:" + e.getMessage());
            }
        }
        return result;
    }

    /**
     * 初始化代理
     *
     * @return
     */
    private boolean initFbProxy() {
        if (fbInit) {
            return true;
        }
        try {
            iceClient.initClientPrxs(fbConnectionParam);
            fbService = (ServicePrxHelper) iceClient.getPrxByKey(DYBF_NAME);
            fbInit = true;
            if (fbService == null) {
                fbInit = false;
                logger.info("WebClientManager init proxy " + DYBF_NAME + " return null!");
            }
        } catch (Exception e) {
            fbInit = false;
            logger.error("WebClientManager ice param init error:" + e.getClass().getName());
        }
        return fbInit;
    }

    /**
     * 初始化代理
     *
     * @return
     */
    private boolean initBbProxy() {
        if (bbInit) {
            return true;
        }
        try {
            iceClient.initClientPrxs(bbConnectionParam);
            bbService = (ExternalLotteryPrxHelper) iceClient.getPrxByKey(BASKEt_DYBF_NAME);
            bbInit = true;
            if (bbService == null) {
                bbInit = false;
                logger.info("WebClientManager init proxy " + BASKEt_DYBF_NAME + " return null!");
            }
        } catch (Exception e) {
            bbInit = false;
            logger.error("WebClientManager ice param init error:" + e.getClass().getName());
        }
        return bbInit;
    }

}
