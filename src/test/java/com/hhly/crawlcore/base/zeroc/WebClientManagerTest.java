
package com.hhly.crawlcore.base.zeroc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.skeleton.base.util.DateUtil;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年12月8日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)    
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebClientManagerTest {

	@Autowired
	WebClientManager webClientManager;
	
	
	@Test
	public void testGetJcFbMatchByDate() {
		String date = DateUtil.getBeforeOrAfterDate(0);
		System.err.println(date);
		String result = webClientManager.getJcFbMatchByDate(date);
		System.err.println("fb result : " + result);
	}

	@Test
	public void testGetJcBbMatchByDate() {
		String date = DateUtil.getBeforeOrAfterDate(0);
		System.err.println(date);
		String result = webClientManager.getJcBbMatchByDate(date);
		System.err.println("bb result : " + result);
	}

}
