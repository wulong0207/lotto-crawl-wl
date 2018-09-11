
package com.hhly.crawlcore.base.enums;

/**
 * @desc
 * @author cheng chen
 * @date 2017年12月21日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class BbLotteryEnum {

	public enum FhBbLotteryEnum {
		/**
		 * @desc
		 * @author cheng chen
		 * @date 2017年12月21日
		 * @company 益彩网络科技公司
		 * @version 1.0
		 */

		WF("sf", "胜负", (short) 1), LET_WF("rfsf", "让分胜负", (short) 2), BSS("dxf", "大小分", (short) 4), WS("sfc", "胜分差", (short) 3);

		private String code;

		private String name;

		private Short playId;

	private FhBbLotteryEnum(String code, String name, Short playId) {
		this.code = code;
		this.name = name;
		this.playId = playId;
	}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public Short getPlayId() {
			return playId;
		}
	}
}
