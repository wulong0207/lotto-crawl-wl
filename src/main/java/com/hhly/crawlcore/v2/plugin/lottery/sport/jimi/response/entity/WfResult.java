
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年6月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@XStreamAlias("match")
public class WfResult {
	
	/**
	 * 北单编码
	 */
	private Integer matchNum;
	
	/**
	 * 半场比分
	 */
	private String banScore;
	
	/**
	 * 比分彩果
	 */	
	private String quanScore;
	
	/**
	 * 胜平负彩果
	 */	
	private String sfggResult;
	
	/**
	 * 比分奖金
	 */	
	private Double sfggAward;

	public Integer getMatchNum() {
		return matchNum;
	}

	public void setMatchNum(Integer matchNum) {
		this.matchNum = matchNum;
	}

	public String getBanScore() {
		return banScore;
	}

	public void setBanScore(String banScore) {
		this.banScore = banScore;
	}

	public String getQuanScore() {
		return quanScore;
	}

	public void setQuanScore(String quanScore) {
		this.quanScore = quanScore;
	}

	public String getSfggResult() {
		return sfggResult;
	}

	public void setSfggResult(String sfggResult) {
		this.sfggResult = sfggResult;
	}

	public Double getSfggAward() {
		return sfggAward;
	}

	public void setSfggAward(Double sfggAward) {
		this.sfggAward = sfggAward;
	}	
}
