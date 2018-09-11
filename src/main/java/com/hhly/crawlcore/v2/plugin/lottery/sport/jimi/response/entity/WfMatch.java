
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
public class WfMatch {

	/**
	 * 场次编码
	 */
	private Integer matchNum;
	
	/**
	 * 赛事类型
	 */
	private String leagueName;
	
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
	private String concedeNum;
	
	/**
	 * 胜负赔率值
	 */
	private String sfggOdds;

	public Integer getMatchNum() {
		return matchNum;
	}

	public void setMatchNum(Integer matchNum) {
		this.matchNum = matchNum;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
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

	public String getConcedeNum() {
		return concedeNum;
	}

	public void setConcedeNum(String concedeNum) {
		this.concedeNum = concedeNum;
	}

	public String getSfggOdds() {
		return sfggOdds;
	}

	public void setSfggOdds(String sfggOdds) {
		this.sfggOdds = sfggOdds;
	}
}
