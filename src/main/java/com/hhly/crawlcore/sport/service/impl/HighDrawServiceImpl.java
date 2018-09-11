package com.hhly.crawlcore.sport.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.sport.service.HighDrawService;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @author chengkangning
 * @version 1.0
 * @desc 1选5，快3，快乐10分彩果入库
 * @date 2017/10/11
 * @company 益彩网络科技公司
 */
@Deprecated
@Service
public class HighDrawServiceImpl implements HighDrawService {

    @Resource
    private LotteryIssueDaoMapper issueMapper;

    /**
     * 缓存采用彩种+彩期作为key值
     *
     * @param lotteryIssuePO 数据对象
     * @throws Exception
     */
    @Override
    public void inStock(LotteryIssuePO lotteryIssuePO) throws Exception {
        LotteryIssuePO paramPO = new LotteryIssuePO();
        paramPO.setLotteryCode(lotteryIssuePO.getLotteryCode());
        paramPO.setIssueCode(lotteryIssuePO.getIssueCode());
        LotteryIssuePO issuePO = issueMapper.findSingle(paramPO);
        if (ObjectUtil.isBlank(issuePO)) {

            lotteryIssuePO.setRemark("test draw");
            issueMapper.insert(lotteryIssuePO);
            //更新开奖时间不是最新一条的所有记录
            //issueMapper.updateYesterdayData(lotteryIssuePO);

        } else {
            issueMapper.highDrawUpdate(lotteryIssuePO);
        }
    }

}
