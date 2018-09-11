
package com.hhly.crawlcore.base.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.util.HttpUtil;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年12月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/proxy")
public class ProxyController {
	
	private static final Logger log = LoggerFactory.getLogger(ProxyController.class);
	
	@Value("${crawl_get_proxy_ip}")
	private String get_proxy_ip;
	
	@Autowired
	RedisUtil redisUtil;
	
	
	@RequestMapping("/getIp")
	public Object getIp(){
		ThreadPoolManager.execute("lotto-crawl项目获取代理ip集合", new Runnable() {
			@Override
			public void run() {
				try {
					log.info("从lotto-plugin获取代理ip集合 start");
					String ipStrs = HttpUtil.doGet(get_proxy_ip, 10000, 20000);
					if(!ObjectUtil.isBlank(ipStrs)){
			            List<String> list = JSONArray.parseArray(ipStrs, String.class);
			            redisUtil.addObj(CacheConstants.C_CRAWL_PROXY_IP, list, CacheConstants.TWO_MINUTES);
					}
					log.info("从lotto-plugin获取代理ip集合  end  ipStrs :" + ipStrs);
				} catch (Exception e) {
					log.error("通过lotto-proxy项目获取代理ip异常, message :" + e.getMessage());
				}
			}
		});
		return ResultBO.ok();
	}
}
