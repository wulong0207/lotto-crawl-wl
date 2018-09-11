package com.hhly.crawlcore.base.controller;

import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.skeleton.base.bo.ResultBO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @desc 监控系统数据
 * @author jiangwei
 * @date 2017年2月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/monitor")
public class MonitorController{
	/**
	 * 获取线程池运行情况
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年2月27日 下午4:45:42
	 * @return
	 */
	@RequestMapping(value = "/thread/pool/status")
	@ResponseBody
	public  Object threadPool(){
		 return ResultBO.ok(ThreadPoolManager.getThreadPoolStatus());
	}
}
