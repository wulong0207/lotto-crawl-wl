
package com.hhly.crawlcore.v2.sport.entity.fb;

import com.alibaba.fastjson.annotation.JSONField;
import com.hhly.crawlcore.v2.sport.entity.MatchInfo;

/**
 * @desc    500网竞足对象
 * @author  cheng chen
 * @date    2017年12月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class FhFbMatchInfo extends MatchInfo {

	/**
	 * 最新sp
	 */
	@JSONField(name = "new_sp")
	private FhFbNewSp newSp;
	
	/**
	 * 子玩法状态
	 */
	@JSONField(name = "play_status")
	private FhFbPlayStatus playStatus;


	public FhFbNewSp getNewSp() {
		return newSp;
	}

	public void setNewSp(FhFbNewSp newSp) {
		this.newSp = newSp;
	}

	public FhFbPlayStatus getPlayStatus() {
		return playStatus;
	}

	public void setPlayStatus(FhFbPlayStatus playStatus) {
		this.playStatus = playStatus;
	}
}
