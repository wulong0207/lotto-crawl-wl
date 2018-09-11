
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
public class WfResultBody {

	private String issue;
	
	@XStreamAlias("matchs")
	private List<WfResult> wfResults;

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public List<WfResult> getWfResults() {
		return wfResults;
	}

	public void setWfResults(List<WfResult> wfResults) {
		this.wfResults = wfResults;
	}
}
