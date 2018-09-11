package com.hhly.crawlcore.rabbitmq.consumer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.config.RabbitMqConfig;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssueSourcePO;
import com.hhly.crawlcore.sport.service.HighDrawService;
import com.hhly.crawlcore.v2.plugin.lottery.high.service.HighDrawV2Service;

/**
 * @author chengkangning
 * @version 1.0
 * @desc 11选5，快3，快乐10分彩果入库
 * @date 2017/10/11
 * @company 益彩网络科技公司
 */
@Configuration
@AutoConfigureAfter(RabbitMqConfig.class)
public class HighDrawListenter {

	private Logger logger = LoggerFactory.getLogger(HighDrawListenter.class);

	@Value("${high_draw_queue}")
	private String highDrawQueue;

	@Resource
	private HighDrawService highDrawService;

	@Resource
	private HighDrawV2Service highDrawV2Service;

	@Bean("highDrawQueueContainer")
	public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setQueueNames(highDrawQueue);
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(exampleListener());
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return container;
	}

	public MessageListener exampleListener() {
		return new MessageListener() {
			@Override
			public void onMessage(final Message message) {
				 ThreadPoolManager.execute("彩果rabbitMq", new Runnable() {
					 @Override
					public void run() {
							try {
					            String msg = new String(message.getBody(), "UTF-8");
					            logger.debug("11选5、快3、快乐10分等彩果对接：" + msg);
//					            LotteryIssuePO lotteryIssuePO = JSONObject.parseObject(msg, LotteryIssuePO.class);
					            LotteryIssueSourcePO lotteryIssueSourcePO = JSONObject.parseObject(msg, LotteryIssueSourcePO.class);
					            highDrawV2Service.insert(lotteryIssueSourcePO);
							} catch (Exception e) {
								e.printStackTrace();
							}
						
					}
				 });
			}
		};
	}
}
