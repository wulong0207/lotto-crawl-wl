
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年6月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@XStreamAlias("match")
public class BdMatch {
	
	/**
	 * 场次
	 */
	private Integer matchNum;
	
	/**
	 * 联赛名称
	 */
	private String matchName;
	
	/**
	 * 主队
	 */
	private String hostTeam;
	
	/**
	 * 客队
	 */
	private String guestTeam;
	
	/**
	 * 比赛时间
	 */
	private String gameTime;
	
	/**
	 * 让球数
	 */
	private Integer concedeNum;
	
	/**
	 * 胜平负赔率值	胜|平|负
	 */
	private String spfOdds;
	
	/**
	 * 上下单双赔率值	上单|上双|下单|下双
	 */
	private String sxdsOdds;
	
	/**
	 * 总进球赔率值	0|1|2|3|4|5|6|7
	 */
	private String zjqOdds;
	
	/**
	 * 比分赔率值	胜其他|1:0|2:0|2:1|3:0|3:1|3:2|4:0|4:1|4:2|平其他|0:0|1:1|2:2|3:3|负其他|0:1|0:2|1:2|0:3|1:3|2:3|0:4|1:4|2:4
	 */
	private String bfOdds;
	
	/**
	 * 胜胜|胜平|胜负|平胜|平平|平负|负胜|负平|负负
	 */
	private String bqcOdds;

	public Integer getMatchNum() {
		return matchNum;
	}

	public void setMatchNum(Integer matchNum) {
		this.matchNum = matchNum;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public String getHostTeam() {
		return hostTeam;
	}

	public void setHostTeam(String hostTeam) {
		this.hostTeam = hostTeam;
	}

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}

	public String getGameTime() {
		return gameTime;
	}

	public void setGameTime(String gameTime) {
		this.gameTime = gameTime;
	}

	public Integer getConcedeNum() {
		return concedeNum;
	}

	public void setConcedeNum(Integer concedeNum) {
		this.concedeNum = concedeNum;
	}

	public String getSpfOdds() {
		return spfOdds;
	}

	public void setSpfOdds(String spfOdds) {
		this.spfOdds = spfOdds;
	}

	public String getSxdsOdds() {
		return sxdsOdds;
	}

	public void setSxdsOdds(String sxdsOdds) {
		this.sxdsOdds = sxdsOdds;
	}

	public String getZjqOdds() {
		return zjqOdds;
	}

	public void setZjqOdds(String zjqOdds) {
		this.zjqOdds = zjqOdds;
	}

	public String getBfOdds() {
		return bfOdds;
	}

	public void setBfOdds(String bfOdds) {
		this.bfOdds = bfOdds;
	}

	public String getBqcOdds() {
		return bqcOdds;
	}

	public void setBqcOdds(String bqcOdds) {
		this.bqcOdds = bqcOdds;
	}	
}
