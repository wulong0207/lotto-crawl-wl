
package com.hhly.crawlcore.v2.sport.bj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.v2.plugin.lottery.sport.aibo.AiboBjCrawl;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.JimiBdCrawl;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.JimiWfCrawl;
import com.hhly.skeleton.base.bo.ResultBO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年1月18日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/sport/bj/v2.0")
public class SportBjV2Controller {
	
	private static final Logger log = LoggerFactory.getLogger(SportBjV2Controller.class);
	
	@Autowired
	AiboBjCrawl aiboBjCrawl;
	
	@Autowired
	JimiBdCrawl jimiBjCrawl;
	
	@Autowired
	JimiWfCrawl jimiWfCrawl;

	@RequestMapping(value = "/ab/getMatchInfo")
	public Object getAbMatchInfo(){
		try {
			aiboBjCrawl.crawlHandle();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("北京单场V2版本 获取所有赛事信息 异常  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	@RequestMapping(value = "/jm/bd/getMatchInfo")
	public Object getJmBdMatchInfo(){
		try {
			jimiBjCrawl.crawlHandle();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("北京单场V2版本 获取所有赛事信息 异常  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	@RequestMapping(value = "/jm/bd/getResultInfo")
	public Object getJmBdResultInfo(@RequestParam(value = "issueCode") String issueCode){
		try {
			jimiBjCrawl.dataHandle(issueCode);
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("北京单场V2版本 获取所有赛事信息 异常  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}

	@RequestMapping(value = "/jm/wf/getMatchInfo")
	public Object getJmWfMatchInfo(){
		try {
			jimiWfCrawl.crawlHandle();
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("北京单场V2版本 获取所有赛事信息 异常  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
	
	@RequestMapping(value = "/jm/wf/getResultInfo")
	public Object getJmWfResultInfo(@RequestParam(value = "issueCode") String issueCode){
		try {
			jimiWfCrawl.dataHandle(issueCode);
			return ResultBO.ok();
		} catch (Exception e) {
			log.error("北京单场V2版本 获取所有赛事信息 异常  message :"+ e.getMessage());
			return ResultBO.err();
		}
	}
}
