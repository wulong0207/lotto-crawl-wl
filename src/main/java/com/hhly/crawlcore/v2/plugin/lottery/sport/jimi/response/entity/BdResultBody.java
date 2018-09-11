
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年6月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class BdResultBody {

	private String issue;
	
	@XStreamAlias("matchs")
	private List<BdResult> bjResults;

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public List<BdResult> getBjResults() {
		return bjResults;
	}

	public void setBjResults(List<BdResult> bjResults) {
		this.bjResults = bjResults;
	}
}
