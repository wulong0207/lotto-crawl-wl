
package com.hhly.crawlcore.persistence.sport.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.hhly.crawlcore.persistence.sport.po.SportDataFbSpPO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年5月15日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
//defaultRollback = true说明: true ? 测试数据不会污染数据库 : 会真正添加到数据库当中
@TransactionConfiguration(transactionManager = "transactionManager" , defaultRollback = false) 
public class SportDataFbSpDaoMapperTest {
	
	@Autowired
	SportDataFbSpDaoMapper sportDataFbSpDaoMapper;

	@Test
	public void testMerge() {
		SportDataFbSpPO po = new SportDataFbSpPO();
		po.setSportAgainstInfoId(58364l);
		po.setNewestLetSpWin(null);
		sportDataFbSpDaoMapper.merge(po);
	}

}
