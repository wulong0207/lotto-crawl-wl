
package com.hhly.crawlcore.base.enums;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年6月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class TicketChannelEnum {

	public enum TicketChanel{
		Ruilang("2", "睿朗"),
		Jimi("8","吉米");
		
		private String channelId;
		
		private String name;

		private TicketChanel(String channelId, String name) {
			this.channelId = channelId;
			this.name = name;
		}

		public String getChannelId() {
			return channelId;
		}

		public String getName() {
			return name;
		}
	}
	
}
