package com.hhly.crawlcore.base.zeroc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.crawlcore.copyorder.service.CopyOrderService;

/**
 * @author yuanshangbing
 * @version 1.0
 * @desc
 * @date 2018/1/10 9:52
 * @company 益彩网络科技公司
 */
@RunWith(SpringJUnit4ClassRunner.class)    
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CopyOrderServiceTest {

    @Autowired
    private CopyOrderService copyOrderService;

    @Test
    public void testAddOrderIssueAndUser(){
        copyOrderService.addIssueMatchAndUserInfo();

    }

    @Test
    public void testUpdateUser(){
        copyOrderService.updateUserIssueInfo();

    }
}
