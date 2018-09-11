
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity;

import java.util.List;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年6月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class BdMatchBody {
	
	/**
	 * 彩期
	 */
	private String issue;
	
	/**
	 * 北单赛事集合
	 */
	private List<BdMatch> matchs;

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public List<BdMatch> getMatchs() {
		return matchs;
	}

	public void setMatchs(List<BdMatch> matchs) {
		this.matchs = matchs;
	}
}
