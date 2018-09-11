
package com.hhly.crawlcore.v2.sport.oldfb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.sport.entity.OldLotteryIssue;
import com.hhly.crawlcore.v2.plugin.lottery.sport.fivehundred.FhOldFbCrawl;
import com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery.StOldFbCrawl;
import com.hhly.crawlcore.v2.sport.oldfb.service.SportOldFbV2Service;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @desc
 * @author cheng chen
 * @date 2017年11月30日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/sport/oldfb/v2.0")
public class SportOldFbV2Controller extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(SportOldFbV2Controller.class);

	@Autowired
	StOldFbCrawl stOldFbCrawl;

	@Autowired
	FhOldFbCrawl fhOldFbCrawl;

	@Autowired
	SportOldFbV2Service sportOldFbV2Service;

	@Autowired
	RedisUtil redisUtil;

	/**
	 * 获取所有赛事,暂定赛事
	 * 
	 * @return
	 * @throws Exception
	 * @date 2017年11月29日下午12:16:58
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/st/getAllMatch")
	public Object getStMatch() throws Exception {
		ThreadPoolManager.execute("st竞彩足球V2版本 获取所有赛事,暂定赛事", new Runnable() {

			@Override
			public void run() {
				try {
					stOldFbCrawl.crawlMatch();
					sportOldFbV2Service.delSportOldFbCache();
				} catch (Exception e) {
					log.error("st竞彩足球V2版本 获取所有赛事,暂定赛事 异常  message :" + e.getMessage());
				}
			}
		});
		return ResultBO.ok();
	}

	/**
	 * 获取老足彩彩期信息
	 *
	 * @param issueCode
	 * @param lotteryCode
	 * @return
	 */
	@RequestMapping(value = "/st/getNextIssue", method = { RequestMethod.GET, RequestMethod.POST })
	public Object getNextIssue(@RequestParam(value = "lotteryCode") Integer lotteryCode,
			@RequestParam(value = "issueCode") String issueCode) {
		OldLotteryIssue result = null;
		try {
			String key = CacheConstants.C_CRAWL_OLDFB_NEXT_ISSUE + lotteryCode + SymbolConstants.UNDERLINE + issueCode;
			result = redisUtil.getObj(key, new OldLotteryIssue());
			if (ObjectUtil.isBlank(result)) {
				result = stOldFbCrawl.getNextIssue(lotteryCode, issueCode);
				redisUtil.addObj(key, result, CacheConstants.ONE_DAY);
			}
		} catch (Exception e) {
			log.error("st获取老足彩下一期彩期明细异常, message : " + e.getMessage());
		}
		return result;
	}

	/**
	 * 获取所有赛事,暂定赛事
	 * 
	 * @return
	 * @throws Exception
	 * @date 2017年11月29日下午12:16:58
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/fh/getAllMatch")
	public Object getFhMatch() throws Exception {
		ThreadPoolManager.execute("fh竞彩足球V2版本 获取所有赛事,暂定赛事", new Runnable() {

			@Override
			public void run() {
				try {
					fhOldFbCrawl.crawlMatch();
					sportOldFbV2Service.delSportOldFbCache();
				} catch (Exception e) {
					log.error("fh竞彩足球V2版本 获取所有赛事,暂定赛事 异常  message :" + e.getMessage());
				}
			}
		});	
		return ResultBO.ok();		
	}

	/**
	 * 同步竞足的一比分信息和欧赔到老足彩
	 * 
	 * @return
	 * @date 2017年12月22日上午11:21:22
	 * @author cheng.chen
	 */
	@RequestMapping(value = "/synFbMatchInfo")
	public Object synFbMatchInfo() {
		ThreadPoolManager.execute("竞彩足球V2版本 销售中的历史sp值  ", new Runnable() {

			@Override
			public void run() {
				try {
					sportOldFbV2Service.synFbMatchInfo();
					sportOldFbV2Service.delSportOldFbCache();
				} catch (Exception e) {
					log.error("竞彩足球V2版本 销售中的历史sp值  message :" + e.getMessage());
				}
			}
		});	
		return ResultBO.ok();		
	}
}
