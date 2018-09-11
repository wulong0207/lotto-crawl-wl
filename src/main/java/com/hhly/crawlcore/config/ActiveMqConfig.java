
package com.hhly.crawlcore.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;


/**
 * @desc    
 * @author  cheng chen
 * @date    2018年7月26日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Configuration
public class ActiveMqConfig {
	
	/**
	 * ip地址
	 */
	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;
	
	/**
	 * 账号
	 */
	@Value("${spring.activemq.user-name}")
	private String userName;
	
	/**
	 * 密码
	 */
	@Value("${spring.activemq.password}")
	private String password;
	
	/**
	 * 心跳quene
	 */
	@Value("${spring.activemq.ping-queue}")
	private String pingQueue;
	
	/**
	 * 
	 */
	@Value("${spring.activemq.useAsyncSend}")
	private boolean useAsyncSend;
	
	/**
	 * 
	 */
	@Value("${spring.activemq.alwaysSessionAsync}")
	private boolean alwaysSessionAsync;	
	
	/**
	 * 
	 */
	@Value("${spring.activemq.optimizeAcknowledge}")
	private boolean optimizeAcknowledge;
	
	/**
	 * 
	 */
	@Value("${spring.activemq.producerWindowSize}")
	private int producerWindowSize;	
	
	/**
	 * 
	 */
	@Value("${spring.activemq.sessionCacheSize}")
	private int sessionCacheSize;		
	
    // 构建mq实例工厂
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
    	ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(userName, password, brokerUrl);
    	activeMQConnectionFactory.setUseAsyncSend(useAsyncSend);
    	activeMQConnectionFactory.setAlwaysSessionAsync(alwaysSessionAsync);
    	activeMQConnectionFactory.setOptimizeAcknowledge(optimizeAcknowledge);
    	activeMQConnectionFactory.setProducerWindowSize(producerWindowSize);
        return activeMQConnectionFactory;
    }
    
    @Bean("cachingConnectionFactory")
    public CachingConnectionFactory cachingConnectionFactory(){
    	CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
    	cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory());
    	cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        return cachingConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(CachingConnectionFactory cachingConnectionFactory){
    	JmsTemplate jmsTemplat = new JmsTemplate();
    	jmsTemplat.setConnectionFactory(cachingConnectionFactory);
    	ActiveMQQueue queue = new ActiveMQQueue(pingQueue);
    	jmsTemplat.setDefaultDestination(queue);
    	jmsTemplat.setPubSubDomain(true);
    	return jmsTemplat;
    }
    
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(){
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(cachingConnectionFactory());
        return bean;
    }    
}
