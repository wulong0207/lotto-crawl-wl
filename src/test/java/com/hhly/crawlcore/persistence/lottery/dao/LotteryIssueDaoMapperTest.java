
package com.hhly.crawlcore.persistence.lottery.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年1月6日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)    
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LotteryIssueDaoMapperTest {

	@Autowired
	LotteryIssueDaoMapper lotteryIssueDaoMapper;
	
	@Test
	public void testGetIssueInfo() {
		List<LotteryIssuePO> list = lotteryIssueDaoMapper.getIssueInfo(300);
		for (LotteryIssuePO lotteryIssuePO : list) {
			System.out.println(lotteryIssuePO.getIssueCode());
		}
	}
	
	@Test
	public void testGetCurrentAndNextIssue() {
		List<String> issueCodeList = lotteryIssueDaoMapper.getCurrentAndNextIssue(300);
		for (String issueCode : issueCodeList) {
			System.out.println(issueCode);
		}
	}

}
