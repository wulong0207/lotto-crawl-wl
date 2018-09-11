package com.hhly.crawlcore.rabbitmq.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.crawlcore.rabbitmq.provider.MessageProvider;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.mq.OrderOverdueMsgModel;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/6/14.
 * @company 益彩网络科技有限公司
 */
@Component
public class OrderOverdueTimeProvider implements MessageProvider {

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(OrderOverdueTimeProvider.class);

    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 发送消息
     *
     * @param queueKey 队列名
     * @param message  消息
     */
    @Override
    public void sendMessage(String queueKey, Object message) {
        if (ObjectUtil.isBlank(message) || !(message instanceof OrderOverdueMsgModel)) {
            logger.error("[messageModel is null not send message]");
            return;
        }
        try {
            amqpTemplate.convertAndSend(queueKey, message, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setPriority(Constants.NUM_1);// 消息优先级
                    return message;
                }
            });
            logger.info("[orderOverdueTime is change] model is " + message.toString());
        } catch (Exception e) {
            logger.error("[message send error. error info is] " + e.getMessage() + " model is" + message.toString());
        }
    }
}
