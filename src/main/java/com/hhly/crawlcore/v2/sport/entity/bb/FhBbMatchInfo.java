
package com.hhly.crawlcore.v2.sport.entity.bb;

import com.alibaba.fastjson.annotation.JSONField;
import com.hhly.crawlcore.v2.sport.entity.MatchInfo;

/**
 * @desc    500网竞足对象
 * @author  cheng chen
 * @date    2017年12月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class FhBbMatchInfo extends MatchInfo {

	/**
	 * 最新sp
	 */
	@JSONField(name = "new_sp")
	private FhBbNewSp newSp;
	
	/**
	 * 子玩法状态
	 */
	@JSONField(name = "play_status")
	private FhBbPlayStatus playStatus;


	public FhBbNewSp getNewSp() {
		return newSp;
	}

	public void setNewSp(FhBbNewSp newSp) {
		this.newSp = newSp;
	}

	public FhBbPlayStatus getPlayStatus() {
		return playStatus;
	}

	public void setPlayStatus(FhBbPlayStatus playStatus) {
		this.playStatus = playStatus;
	}
}
