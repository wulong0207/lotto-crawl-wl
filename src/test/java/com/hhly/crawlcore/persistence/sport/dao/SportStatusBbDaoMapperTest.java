
package com.hhly.crawlcore.persistence.sport.dao;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.crawlcore.persistence.sport.po.SportStatusBbPO;
import com.hhly.skeleton.base.common.SportEnum;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年12月26日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SportStatusBbDaoMapperTest {
	
	@Autowired
	SportStatusBbDaoMapper sportStatusBbDaoMapper;

	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testMerge(){
		SportStatusBbPO po = new SportStatusBbPO();
		po.setSportAgainstInfoId(48152l);
		po.setStatusWf(SportEnum.SportSaleStatusEnum.PASS_SALE.getValue().shortValue());
		po.setStatusLetWf(SportEnum.SportSaleStatusEnum.PASS_SALE.getValue().shortValue());
		po.setStatusBigSmall(SportEnum.SportSaleStatusEnum.PASS_SALE.getValue().shortValue());
		po.setStatusScoreWf(SportEnum.SportSaleStatusEnum.NORMAL_SALE.getValue().shortValue());
		po.setModifyBy("cc123");
		sportStatusBbDaoMapper.merge(po);
	}

	@Test
	public void testSelectCount() {
		SportStatusBbPO po = new SportStatusBbPO();
		po.setSportAgainstInfoId(38045l);
		int num = sportStatusBbDaoMapper.selectCount(po);
		System.out.println(num);
	}
	

}
