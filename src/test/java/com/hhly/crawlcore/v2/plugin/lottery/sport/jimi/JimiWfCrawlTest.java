
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年6月28日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JimiWfCrawlTest {
	
	@Autowired
	JimiWfCrawl jimiWfCrawl;

	@Test
	public void testCrawlHandle() {
		try {
			jimiWfCrawl.crawlHandle();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	public void testDataHandle() {
		try {
			jimiWfCrawl.dataHandle("180701");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
