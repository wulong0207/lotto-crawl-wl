
package com.hhly.crawlcore.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhly.crawlcore.base.zeroc.ConnectionParam;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年7月31日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Configuration
public class IceConfig {
	
	@Value("${spring.ice.clientCfgClassPath}")
	private String clientCfgClassPath;
	
	@Value("${spring.ice.fbSeverUrl}")
	private String fbSeverUrl;
	
	@Value("${spring.ice.bbSeverUrl}")
	private String bbSeverUrl;
	
	@Value("${spring.ice.dataSeverUrl}")
	private String dataSeverUrl;	
	
	@Value("${spring.ice.timeOut}")
	private Integer timeOut;
	
	@Bean("fbConnectionParam")
	public ConnectionParam getFbConnectionParam(){
		ConnectionParam fbConnectionParam = new ConnectionParam();
		fbConnectionParam.setClientCfgClassPath(clientCfgClassPath);
		fbConnectionParam.setSeverUrl(fbSeverUrl);
		fbConnectionParam.setTimeOut(timeOut);
		Map<String, String> prxNames = new HashMap<String, String>();
		prxNames.put("ice-ybf-web", "com.hhly.ybf.football.ServicePrxHelper");
		prxNames.put("ice-ybf-snooker", "com.hhly.ybf.snooker.ServicePrxHelper");
		fbConnectionParam.setPrxNames(prxNames);
		return fbConnectionParam;
	}
	
	@Bean("bbConnectionParam")
	public ConnectionParam getBbConnectionParam(){
		ConnectionParam bbConnectionParam = new ConnectionParam();
		bbConnectionParam.setClientCfgClassPath(clientCfgClassPath);
		bbConnectionParam.setSeverUrl(bbSeverUrl);
		bbConnectionParam.setTimeOut(timeOut);
		Map<String, String> prxNames = new HashMap<String, String>();
		prxNames.put("externalLottery", "com.hhly.zeroc.basket.external.ExternalLotteryPrxHelper");
		bbConnectionParam.setPrxNames(prxNames);
		return bbConnectionParam;
	}
	
	@Bean("dataConnectionParam")
	public ConnectionParam getDataConnectionParam(){
		ConnectionParam dataConnectionParam = new ConnectionParam();
		dataConnectionParam.setClientCfgClassPath(clientCfgClassPath);
		dataConnectionParam.setSeverUrl(dataSeverUrl);
		dataConnectionParam.setTimeOut(timeOut);
		Map<String, String> prxNames = new HashMap<String, String>();
		prxNames.put("ice-ybf-database-mobile", "com.hhly.datacenter.zreocybf.ZlkMobileDataServicePrxHelper");
		dataConnectionParam.setPrxNames(prxNames);
		return dataConnectionParam;
	}	
}
