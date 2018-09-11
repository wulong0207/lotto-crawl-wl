
package com.hhly.crawlcore.persistence.sport.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.crawlcore.persistence.sport.po.SportStatusBjPO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年6月29日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SportStatusBjDaoMapperTest {

	@Autowired
	SportStatusBjDaoMapper sportStatusBjDaoMapper;
	
	@Test
	public void testFindByMatchId() {
		Long sportAgainstInfoId = 62021l;
		SportStatusBjPO po = sportStatusBjDaoMapper.findByMatchId(sportAgainstInfoId);
		System.err.println(po.getModifyBy());
	}

}
