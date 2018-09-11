
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response;

import com.hhly.crawlcore.base.util.XmlUtil;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.AbstractMsg;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity.BdResultBody;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @desc    北单彩果查询
 * @author  cheng chen
 * @date    2018年6月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@XStreamAlias("message")
public class BdResultResponseMsg extends AbstractMsg {

	private static XStream XS = XmlUtil.createXStream(BdResultResponseMsg.class);

	@SuppressWarnings("unchecked")
	@Override
	public <T> T fromXML(String strXml) {
		return (T) XS.fromXML(strXml);
	}
	
	private BdResultBody body;

	public BdResultBody getBody() {
		return body;
	}

	public void setBody(BdResultBody body) {
		this.body = body;
	}
}
