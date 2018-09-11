
package com.hhly.crawlcore.v2.sport.entity;

import java.io.Serializable;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年12月21日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class FhCacheInfo implements Serializable {

	private static final long serialVersionUID = -4043907054156868700L;
	
	private Integer lotteryCode;
	
	private String issueCode;

	private Long sportAgainstInfoId;
	
	private String officialId;
	
	private Short letNum;
	
	private String hashCode;
	
	public Integer getLotteryCode() {
		return lotteryCode;
	}

	public void setLotteryCode(Integer lotteryCode) {
		this.lotteryCode = lotteryCode;
	}

	public String getIssueCode() {
		return issueCode;
	}

	public void setIssueCode(String issueCode) {
		this.issueCode = issueCode;
	}

	public Long getSportAgainstInfoId() {
		return sportAgainstInfoId;
	}

	public void setSportAgainstInfoId(Long sportAgainstInfoId) {
		this.sportAgainstInfoId = sportAgainstInfoId;
	}

	public String getOfficialId() {
		return officialId;
	}

	public void setOfficialId(String officialId) {
		this.officialId = officialId;
	}

	public Short getLetNum() {
		return letNum;
	}

	public void setLetNum(Short letNum) {
		this.letNum = letNum;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
}
