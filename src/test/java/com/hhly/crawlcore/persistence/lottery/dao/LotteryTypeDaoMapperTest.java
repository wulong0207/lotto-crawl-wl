
package com.hhly.crawlcore.persistence.lottery.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年12月2日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)    
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LotteryTypeDaoMapperTest {
	
	@Autowired
	LotteryTypeDaoMapper lotteryTypeDaoMapper;

	@Test
	public void testFindTypeUseAddIssue() {
		System.out.println(JSONObject.toJSONString(lotteryTypeDaoMapper.findTypeUseAddIssue(300)));
	}

}
