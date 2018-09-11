
package com.hhly.crawlcore.v2.sport.fb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.v2.plugin.lottery.sport.fivehundred.FhFbCrawl;
import com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery.StFbCrawl;
import com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery.StWcFbCrawl;
import com.hhly.crawlcore.v2.sport.fb.service.SportFbV2Service;
import com.hhly.skeleton.base.bo.ResultBO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/sport/fb/v2.0")
public class SportFbV2Controller extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(SportFbV2Controller.class);
	
	@Autowired
	StFbCrawl stFbCrawl;
	
	@Autowired
	FhFbCrawl fhFbCrawl;
	
	@Autowired
	StWcFbCrawl stWcFbCrawl;
	
	@Autowired
	SportFbV2Service sportFbV2Service;
	
	/**
	 * 获取所有赛事,暂定赛事
	 * @return
	 * @throws Exception
	 * @date 2017年11月29日下午12:16:58
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/st/getAllMatch")
	public Object getAllMatch() throws Exception{
		try {
			stFbCrawl.crawlMatch();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩足球V2版本 获取所有赛事,暂定赛事 异常  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	/**
	 * 获取销售中的对阵,最新sp值,状态,历史sp值
	 * @return
	 * @date 2017年11月22日上午10:24:10
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/st/getMatchInfo")
	public Object getStMatchInfo(){
		try {
			stFbCrawl.crawlHandle();
			sportFbV2Service.delSportFbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩足球V2版本 销售中的对阵,最新sp值,状态,历史sp值  message :"+ e.getMessage());
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
			stFbCrawl.dataHandle();
			sportFbV2Service.delSportFbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩足球V2版本 销售中的历史sp值  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}	
	
	@RequestMapping(value = "/fh/getMatchInfo")
	public Object getFhMatchInfo() throws Exception{
		try {
			fhFbCrawl.crawlHandle();
			sportFbV2Service.delSportFbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩足球V2版本 销售中的历史sp值  message :"+ e.getMessage());
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
			fhFbCrawl.dataHandle();
			sportFbV2Service.delSportFbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩足球V2版本 销售中的历史sp值  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	/**
	 * 获取销售中的冠亚军对阵,最新sp值,状态
	 * @return
	 * @date 2017年11月22日上午10:24:10
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/st/getWcMatchInfo")
	public Object getStWcMatchInfo(){
		try {
			stWcFbCrawl.crawlHandle();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("获取销售中的冠亚军对阵,最新sp值,状态  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}	
	
	@RequestMapping(value = "/synMatchForYbf")
	public Object synMatchForYbf(){
		try {
			sportFbV2Service.synMatchForYbf();
			sportFbV2Service.delSportFbCache();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("竞彩足球V2版本 销售中的历史sp值  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
}
