package com.hhly.crawlcore.log.controller;

import com.hhly.crawlcore.log.service.ILogService;
import com.hhly.skeleton.base.bo.ResultBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @desc 解析任务日志
 * @author jiangwei
 * @date 2017年3月2日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/log")
public class LogController {
	@Autowired
    private ILogService logService;
	
	/**
	 * 查询线程当前运行情况
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月17日 下午2:13:59
	 * @return
	 */
	@RequestMapping(value = "/thread/running")
	public  String run(){
		 return "log/thread_running";
	}
	
	/**
	 * 解析过滤线程任务日志
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月2日 下午4:25:36
	 * @param request
	 * @param fileUrl
	 * @return
	 */
	@RequestMapping(value = "/runnable")
	@ResponseBody
	public  Object log(HttpServletRequest request,String fileUrl){
		 String file = getFileUrl(request, fileUrl);
		 return ResultBO.ok(logService.addTaskInfo(file));
	}
	/**
	 * 解析过滤线程池状态日志
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月2日 下午4:26:01
	 * @param request
	 * @param fileUrl
	 * @return
	 */
	@RequestMapping(value = "/thread")
	@ResponseBody
	public  Object thead(HttpServletRequest request,String fileUrl){
		 String file = getFileUrl(request, fileUrl);
		 return ResultBO.ok(logService.addThreadLog(file));
	}
	/**
	 * 获取所有唯一定时任务
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月7日 下午3:15:34
	 * @param request
	 * @param fileUrl
	 * @return
	 */
	@RequestMapping(value = "/runnable/only")
	@ResponseBody
	public  Object onlyRunnable(HttpServletRequest request){
		 return ResultBO.ok(logService.getOnlyFutureTask());
	}
	
	/**
	 * 获取tomcat的路径
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月2日 下午3:06:38
	 * @param request
	 * @return
	 */
	private String getTomcatUrl(HttpServletRequest request){
		return  request.getSession().getServletContext().getRealPath("");
	}
	/**
	 * 获取文件路径
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年4月5日 上午10:00:42
	 * @param request
	 * @param fileUrl
	 * @return
	 */
	private String getFileUrl(HttpServletRequest request, String fileUrl) {
		String file = getTomcatUrl(request);
		 Path path = Paths.get(file);
		 Assert.notNull(path,"路径不存在");
		 file  = path.getParent().getParent().toString()+File.separator+fileUrl;
		return file;
	}
}
