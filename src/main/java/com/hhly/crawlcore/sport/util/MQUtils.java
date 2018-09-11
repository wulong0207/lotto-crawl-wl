package com.hhly.crawlcore.sport.util;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.rabbitmq.provider.MessageProvider;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.MQConstants;
import com.hhly.skeleton.base.mq.OrderOverdueMsgModel;
import com.hhly.skeleton.base.util.DateUtil;

/**
 * Created by chenkangning on 2017/8/8.
 */
@Component
public class MQUtils {

    @Resource
    private MessageProvider messageProvider;

    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * 发送MQ消息
     *
     * @param issue       彩期
     * @param lotteryCode 彩种
     */
    public void sendMessage(String issue, Integer lotteryCode) {
        OrderOverdueMsgModel model = new OrderOverdueMsgModel();
        model.setIssueCode(issue);
        model.setLotteryCode(lotteryCode);
        messageProvider.sendMessage(MQConstants.ORDER_OVERDUE_TIME_QUEUENAME, model);
    }

    /**
     * 推送SP消息
     *
     * @param queueKey 队列名称
     * @param object   参数对象
     */
    public void sendSpMessage(String queueKey, Object object) {
        amqpTemplate.convertAndSend(queueKey, JSON.toJSON(object));
    }


    /**
     * 500网球队信息抓取异常报警
     *
     * @param content 报警内容
     */
    public void sendWarnMessage(String content) {
        sendWarnMessage(content, Constants.NUM_36);
    }

    /**
     * 500网球队信息抓取异常报警
     *
     * @param content    报警内容
     * @param alarmChild alarmChild
     */
    public void sendWarnMessage(String content, int alarmChild) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("alarmChild", alarmChild);
        jsonObject.put("alarmInfo", content);
        jsonObject.put("alarmLevel", 1);
        jsonObject.put("alarmType", 1);
        jsonObject.put("remark", content);
        jsonObject.put("alarmTime", DateUtil.getNow());
        sendSpMessage(Constants.QUEUE_NAME_ALERM_INFO, jsonObject);
    }

}
