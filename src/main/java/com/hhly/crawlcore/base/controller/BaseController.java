package com.hhly.crawlcore.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hhly.skeleton.base.bo.ResultBO;

public class BaseController {
 
	protected  HttpServletRequest request;
	
	protected  HttpServletResponse response;
	
	private static final ResultBO<?> SUCCESS;
	
	private static final ResultBO<?> FAIL;
	static{
		SUCCESS = new ResultBO<>();
		SUCCESS.setErrorCode(ResultBO.SUCCESS_CODE);
		SUCCESS.setSuccess(1);
		FAIL = new ResultBO<>();
		FAIL.setErrorCode(ResultBO.FAIL_CODE);
		FAIL.setSuccess(0);
	}

	public HttpServletRequest getRequest() {
		return request; 
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public <T> ResultBO<T> getSuccessResultBO(T data){
		ResultBO<T> bo = new ResultBO<>();
		bo.setData(data);
		bo.setErrorCode(ResultBO.SUCCESS_CODE);
		bo.setSuccess(1);
		return bo;
	}
	public  ResultBO<?> getSuccessResultBO(){
		return SUCCESS;
	}
	public <T> ResultBO<T> getFailResultBO(T data){
		ResultBO<T> bo = new ResultBO<>();
		bo.setData(data);
		bo.setErrorCode(ResultBO.FAIL_CODE);
		bo.setSuccess(0);
		return bo;
	}
	public  ResultBO<?> getFailResultBO(){
		return FAIL;
	}
	
	/**
	 * 得到IP
	 * @param request
	 * @return
	 */
	public String getIp(HttpServletRequest request) {
		// 有cnd 加速时也能取到用户ip地址
		if (request == null)
			return "";
		String ip = request.getHeader("Cdn-Src-Ip");
		if (ip == null) {
			ip = request.getHeader("X-Forwarded-For");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Real-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		}
		if (ip != null && ip.contains(",")) {
			return ip.split(",")[0].trim();
		}
		return ip;
	}	
	
}
