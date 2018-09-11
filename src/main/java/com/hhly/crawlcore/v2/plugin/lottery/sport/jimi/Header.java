package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi;
/**
 * @desc
 * @author jiangwei
 * @date 2017年12月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class Header {
	// 代理商ID
	private String agentID;
	// 错误码
	private String errorCode;
	// 错误信息
	private String errorMsg;
	// 加密
	private String sign;

	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
