/**
 * @see      对外提供API启动类
 * @author   scott
 * @version  v1.0
 * @date      2018-03-06
 * @company   益彩网络科技
 */
package com.hhly.crawlcore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.hhly.skeleton.base.util.PropertyUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;


@SpringBootApplication(scanBasePackages = { "com.hhly.crawlcore.*" })
@PropertySources(value = {@PropertySource(value = {"classpath:redis/redis-config.properties"}), 
		@PropertySource(value = {"classpath:db-config.properties"}), @PropertySource(value = {"classpath:mq/mq-config.properties"})})
@MapperScan(basePackages = { "com.hhly.crawlcore.persistence.*.dao" })
@ImportResource("classpath:transaction.xml")
@EnableEncryptableProperties
public class LottoCrawlSpringBootApplication {

    public static void main(String[] args) {
        {
        	if(args.length > 0){
        		String env = args[0];
        		PropertyUtil.env = env.split("=")[1];
        	}
        	SpringApplication.run(LottoCrawlSpringBootApplication.class, args);
        }

    }}
