package com.hhly.crawlcore.jms.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/3/13.
 * @company 益彩网络科技有限公司
 */
@Component("jcOddsMessageReceiver")
public class JcOddsMessageReceiver{
    protected Logger logger = LoggerFactory.getLogger(JcOddsMessageReceiver.class.getName());

    /**
     * 竞彩足球Sp值变化
     *
     * @param message
     */
    public void jczqOddsHandler(String message) {
        logger.debug("jczqOddsHandler=======================" + message);
    }
}