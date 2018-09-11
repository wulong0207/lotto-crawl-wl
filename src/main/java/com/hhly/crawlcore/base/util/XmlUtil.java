package com.hhly.crawlcore.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * 
 * @desc XStream 帮助类
 * @author jiangwei
 * @date 2017年5月11日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class XmlUtil {
	/**
	 * 创建 XStream转换器
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年5月11日 下午3:09:31
	 * @param processAnnotation
	 * @return
	 */
	public static XStream createXStream(Class<?>... processAnnotation) {
		XStream xs = new XStream(new StaxDriver());
		for (Class<?> cla : processAnnotation) {
			xs.processAnnotations(cla);
		}
		xs.autodetectAnnotations(true);
		return xs;
	}
    /**
     * 格式化xml
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月11日 下午5:49:44
     * @param xml
     * @return
     */
	public static String formatXml(String xml) {
		try {
			Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
			StreamResult res = new StreamResult(new ByteArrayOutputStream());
			serializer.transform(xmlSource, res);
			return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
		} catch (Exception e) {
			return xml;
		}
	}
}
