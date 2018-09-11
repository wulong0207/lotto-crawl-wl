
package com.hhly.crawlcore.v2.sport.bb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.v2.plugin.lottery.sport.fivehundred.FhBbCrawl;
import com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery.StBbCrawl;
import com.hhly.crawlcore.v2.sport.bb.service.SportBbV2Service;
import com.hhly.skeleton.base.bo.ResultBO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月20日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/sport/bb/v2.0")
public class SportBbV2Controller extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(SportBbV2Controller.class);
	
	@Autowired
	StBbCrawl stBbCrawl;
	
	@Autowired
	FhBbCrawl fhBbCrawl;
	
	@Autowired
	SportBbV2Service sportBbV2Service;
	
	@RequestMapping(value = "/st/getAllMatch")
	public Object getAllMatch(){
		try {
			stBbCrawl.crawlMatch();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩篮球V2版本 获取所有赛事, 暂定赛事 异常  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	/**
	 * 获取销售中的对阵sp值和销售状态
	 * @return
	 * @date 2017年11月22日上午10:24:10
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/st/getMatchInfo")
	public Object getStMatchInfo(){
		try {
			stBbCrawl.crawlHandle();
			sportBbV2Service.delSportBbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩篮球V2版本 销售中的对阵, 最新sp值,状态,历史sp值  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	/**
	 * 获取销售中的历史sp值
	 * @return
	 * @date 2017年11月22日上午10:24:10
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/st/getHistorySp")
	public Object getStHistorySp(){
		try {
			stBbCrawl.dataHandle();
			sportBbV2Service.delSportBbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("sporttery竞彩篮球V2版本 销售中的历史sp值  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	
	/**
	 * 获取销售中的对阵sp值和销售状态
	 * @return
	 * @date 2017年11月22日上午10:24:10
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/fh/getMatchInfo")
	public Object getFhMatchInfo(){
		try {
			fhBbCrawl.crawlHandle();
			sportBbV2Service.delSportBbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("500竞彩篮球V2版本 销售中的对阵, 最新sp值,状态,历史sp值  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	/**
	 * 获取销售中的历史sp值
	 * @return
	 * @date 2017年11月22日上午10:24:10
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/fh/getHistorySp")
	public Object getFhHistorySp(){
		try {
			fhBbCrawl.dataHandle();
			sportBbV2Service.delSportBbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("500竞彩篮球V2版本 销售中的历史sp值  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}	
	
	@RequestMapping(value = "/synMatchForYbf")
	public Object synMatchForYbf(){
		try {
			sportBbV2Service.synMatchForYbf();
			sportBbV2Service.delSportBbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩足球V2版本 销售中的历史sp值  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}	
}
