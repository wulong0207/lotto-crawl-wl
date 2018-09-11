
package com.hhly.crawlcore.persistence.ticket.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.crawlcore.base.enums.TicketChannelEnum.TicketChanel;
import com.hhly.crawlcore.persistence.ticket.po.TicketChannelPO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年6月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketChannelDaoMapperTest {
	
	@Autowired
	TicketChannelDaoMapper ticketChannelDaoMapper;

	@Test
	public void testFindByChannelId() {
		try {
			TicketChannelPO po = ticketChannelDaoMapper.findByChannelId(TicketChanel.Jimi.getChannelId());
			System.out.println(po.getDrawerName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
