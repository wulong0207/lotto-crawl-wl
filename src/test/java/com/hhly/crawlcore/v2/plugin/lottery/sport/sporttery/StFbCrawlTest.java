
package com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @desc    
 * @author  cheng chen
 * @date    2018年8月2日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StFbCrawlTest {
	
	@Autowired
	StFbCrawl stFbCrawl;

	@Test
	public void testStFbCrawl() {
		try {
			stFbCrawl.crawlMatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
