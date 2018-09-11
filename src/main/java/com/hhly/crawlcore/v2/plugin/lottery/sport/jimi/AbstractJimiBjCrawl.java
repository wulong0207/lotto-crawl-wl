
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi;

import java.net.URLEncoder;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.crawlcore.base.enums.TicketChannelEnum.TicketChanel;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.ticket.dao.TicketChannelDaoMapper;
import com.hhly.crawlcore.persistence.ticket.po.TicketChannelPO;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;
import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.hhly.skeleton.base.util.DESUtil;

/**
 * @desc
 * @author cheng chen
 * @date 2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractJimiBjCrawl extends AbstractCrawl {

	private static final Logger log = LoggerFactory.getLogger(AbstractJimiBjCrawl.class);

	protected static TicketChannelPO jimiChanelPO = null;

	@Autowired
	TicketChannelDaoMapper ticketChannelDaoMapper;

	@PostConstruct
	// bean初始化之前调用的方法，等同于xml配置的init-method
	public void init() {
		try {
			jimiChanelPO = ticketChannelDaoMapper.findByChannelId(TicketChanel.Jimi.getChannelId());
			jimiChanelPO.setAccountPassword(DESUtil.decrypt(jimiChanelPO.getAccountPassword()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AbstractJimiBjCrawl() {

	}

	public AbstractJimiBjCrawl(LotteryIssueDaoMapper lotteryIssueMapper,
			SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper, SportTeamInfoService sportTeamInfoService,
			SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}

	@Override
	public void crawlMatch() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 解析出票商返回结果 验证出票商返回加密是否正确
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年9月1日 上午10:05:19
	 * @param cla
	 * @param xml
	 * @return
	 */
	protected <T extends AbstractMsg> T getResponseMsg(Class<T> cla, String xml) {
		String md5;
		T t;
		try {
			t = cla.newInstance().fromXML(xml);
			int start = xml.indexOf("<body>");
			int end = xml.indexOf("</body>") + 7;
			String body = xml.substring(start, end);
			String encrypt = URLEncoder.encode(getEncrypt(body), "UTF-8");
			md5 = DigestUtils.md5Hex(encrypt);
		} catch (Exception e) {
			throw new ServiceRuntimeException("xml格式转换错误" + xml, e);
		}
		if (Objects.equals(md5, t.getHeader().getSign())) {
			return t;
		} else {
			throw new ServiceRuntimeException("加密验证错误：" + xml);
		}
	}

	/**
	 * 发送请求
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年10月14日 下午3:08:38
	 * @param transactionType
	 * @return
	 */
	protected String responseDealer(String method, String... param) {
		StringBuilder sb = new StringBuilder();
		String[] parameters = new String[param.length + 1];
		for (int i = 0; i < param.length; i++) {
			sb.append(param[i]);
			parameters[i] = param[i];
		}
		log.info("吉米请求：" + sb.toString());
		sb.append(jimiChanelPO.getAccountPassword());
		parameters[parameters.length - 1] = DigestUtils.md5Hex(sb.toString());
		String result = "";
		try {
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();
			Integer timeout = Integer.parseInt("20000");
			Call call = (Call) service.createCall();
			call.setUseSOAPAction(true);
			call.setTargetEndpointAddress(new java.net.URL(jimiChanelPO.getSendUrl()));
			call.setEncodingStyle("http://schemas.xmlsoap.org/wsdl/soap/");
			call.setOperationName(new QName("http://service.xixi.com/", method));
			call.setTimeout(timeout);
			result = (String) call.invoke(parameters);
			log.info("吉米响应：" + result);
		} catch (Exception ex) {
			throw new ServiceRuntimeException("吉米请求出票商请求异常", ex);
		}
		return result;
	}

	/**
	 * 获取加密字符串
	 * 
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年9月1日 上午10:04:36
	 * @param body
	 * @return
	 */
	protected String getEncrypt(String body) {
		return jimiChanelPO.getDrawerAccount() + body + jimiChanelPO.getAccountPassword();
	}

	/**
	 * 获取抓取彩种
	 * 
	 * @return
	 * @throws Exception
	 * @date 2017年11月23日下午2:28:47
	 * @author cheng.chen
	 */
	protected abstract Integer getLotteryCode() throws Exception;
}
