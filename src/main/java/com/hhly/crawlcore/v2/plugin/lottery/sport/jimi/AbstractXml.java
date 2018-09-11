package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi;

import com.hhly.crawlcore.base.util.XmlUtil;
import com.hhly.crawlcore.v2.plugin.lottery.sport.IXml;
import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
/**
 * @desc 
 * @author jiangwei
 * @date 2017年12月12日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class AbstractXml implements IXml {

	@XStreamOmitField
	private static final XStream  XS_TO_XML;
	
	static{
		XS_TO_XML= XmlUtil.createXStream();
	}
	
	@Override
	public String toXml() {
		return XS_TO_XML.toXML(this);
	}

	@Override
	public <T> T fromXML(String xml) {
		throw new ServiceRuntimeException("请实现");
	}
}
