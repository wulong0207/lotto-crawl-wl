package com.hhly.crawlcore.v2.draw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.v2.draw.service.DrawMergeService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.LotteryEnum;

@RestController
@RequestMapping(value = "/draw/merge/v2.0")
public class DrawMergeController {

	@Autowired
	private DrawMergeService drawMergeService;

	/**
	 * 合并高频彩
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/high")
	public Object mergeHigh() throws Exception {
		drawMergeService.merge(LotteryEnum.LotteryCategory.HIGH.getValue());
		return ResultBO.ok();
	}

	/**
	 * 合并地方彩
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/local")
	public Object mergeLocal() throws Exception {
		drawMergeService.merge(LotteryEnum.LotteryCategory.LOCAL.getValue());
		return ResultBO.ok();
	}
}
