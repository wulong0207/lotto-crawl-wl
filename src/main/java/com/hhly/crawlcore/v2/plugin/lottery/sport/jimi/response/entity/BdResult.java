
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
public class BdResult {
	
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
	private String bfResult;
	
	/**
	 * 比分奖金
	 */	
	private Double bfAward;
	
	/**
	 * 胜平负彩果
	 */	
	private String spfResult;
	
	/**
	 * 胜平负奖金
	 */	
	private Double spfAward;
	
	/**
	 * 上下单双彩果
	 */	
	private String sxdsResult;
	
	/**
	 * 上下单双奖金
	 */	
	private Double sxdsAward;
	
	/**
	 * 总进球彩果
	 */	
	private String zjqResult;
	
	/**
	 * 总进球奖金
	 */	
	private Double zjqAward;
	
	/**
	 * 半全场彩果
	 */	
	private String bqcResult;
	
	/**
	 * 半全场奖金
	 */	
	private Double bqcAward;

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

	public String getBfResult() {
		return bfResult;
	}

	public void setBfResult(String bfResult) {
		this.bfResult = bfResult;
	}

	public Double getBfAward() {
		return bfAward;
	}

	public void setBfAward(Double bfAward) {
		this.bfAward = bfAward;
	}

	public String getSpfResult() {
		return spfResult;
	}

	public void setSpfResult(String spfResult) {
		this.spfResult = spfResult;
	}

	public Double getSpfAward() {
		return spfAward;
	}

	public void setSpfAward(Double spfAward) {
		this.spfAward = spfAward;
	}

	public String getSxdsResult() {
		return sxdsResult;
	}

	public void setSxdsResult(String sxdsResult) {
		this.sxdsResult = sxdsResult;
	}

	public Double getSxdsAward() {
		return sxdsAward;
	}

	public void setSxdsAward(Double sxdsAward) {
		this.sxdsAward = sxdsAward;
	}

	public String getZjqResult() {
		return zjqResult;
	}

	public void setZjqResult(String zjqResult) {
		this.zjqResult = zjqResult;
	}

	public Double getZjqAward() {
		return zjqAward;
	}

	public void setZjqAward(Double zjqAward) {
		this.zjqAward = zjqAward;
	}

	public String getBqcResult() {
		return bqcResult;
	}

	public void setBqcResult(String bqcResult) {
		this.bqcResult = bqcResult;
	}

	public Double getBqcAward() {
		return bqcAward;
	}

	public void setBqcAward(Double bqcAward) {
		this.bqcAward = bqcAward;
	}
}
