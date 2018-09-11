
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response;

import com.hhly.crawlcore.base.util.XmlUtil;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.AbstractMsg;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity.BdMatchBody;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @desc    北单赛事查询
 * @author  cheng chen
 * @date    2018年6月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@XStreamAlias("message")
public class BdMatchResponseMsg extends AbstractMsg {
	
	private static XStream XS = XmlUtil.createXStream(BdMatchResponseMsg.class);

	@SuppressWarnings("unchecked")
	@Override
	public <T> T fromXML(String strXml) {
		return (T) XS.fromXML(strXml);
	}

	private BdMatchBody body;

	public BdMatchBody getBody() {
		return body;
	}

	public void setBody(BdMatchBody body) {
		this.body = body;
	}	
	
}
