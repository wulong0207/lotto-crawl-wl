
package com.hhly.crawlcore.persistence.sport.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.SportEnum.JcMatchStatusEnum;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月10日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)    
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
//defaultRollback = true说明: true ? 测试数据不会污染数据库 : 会真正添加到数据库当中
@TransactionConfiguration(transactionManager = "transactionManager" , defaultRollback = true) 
public class SportAgainstInfoDaoMapperTest {
	
	@Autowired
	SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

	@Test
	public void testFindMatchListOfficialIdIsNotNull() {
		List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findSaleMatchList(301);
		System.out.println(JSONObject.toJSONString(list));
        /*List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findMatchListOfficialIdIsNotNull(301);
		System.out.println(JSONObject.toJSONString(list));*/
	}
	
	@Test
	public void testFindIdByCode(){
		Integer lotteryCode = 300;
		String issueCode = "171121";
		String officialMatchCode = "周二001";
		String systemCode = SportLotteryUtil.getSystemCode(officialMatchCode, issueCode);
		System.out.println(sportAgainstInfoDaoMapper.findIdByCode(lotteryCode, issueCode, systemCode));
	}

	@Test
	public void testFindSaleMatchTimeList(){
		Integer lotteryCode = 300;
		Set<String> times = sportAgainstInfoDaoMapper.findSynSaleMatchTimeList(lotteryCode);
		for (String time : times) {
			System.out.println("time : " + time);
		}
	}
	
	@Test
	public void testFindSynSaleMatch(){
		Integer lotteryCode = 300;
		Map<String, SportAgainstInfoPO> dataMap = sportAgainstInfoDaoMapper.findSynSaleMatch(lotteryCode);
		System.out.println(JSONObject.toJSONString(dataMap));
	}
	
	@Test
	public void testMerge(){
		SportAgainstInfoPO po = sportAgainstInfoDaoMapper.findByCode(300, "180802", "1808024001");
		
		System.out.println("修改权限判断" + JcMatchStatusEnum.isUpdate(po.getMatchStatus()));
//		po.setRemark("测试sql merge功能");
		po.setCreateBy("modify");
		po.setModifyBy("cc");
		sportAgainstInfoDaoMapper.merge(po);
		System.out.println("新增的id : " + po.getId());
	}
}
