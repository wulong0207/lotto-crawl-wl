
package com.hhly.crawlcore.v2.plugin.lottery.sport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractBjCrawl extends AbstractCrawl {
	
	
	private static final Logger log = LoggerFactory.getLogger(AbstractBjCrawl.class);
	
    public AbstractBjCrawl(){
    	
    }
    
	public AbstractBjCrawl(LotteryIssueDaoMapper lotteryIssueMapper,
			SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper, SportTeamInfoService sportTeamInfoService,
			SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}
    
	@Override
	public void dataHandle(String... params) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void crawlHandle() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 获取抓取彩种
	 * @return
	 * @throws Exception
	 * @date 2017年11月23日下午2:28:47
	 * @author cheng.chen
	 */
	protected abstract Integer getLotteryCode() throws Exception;	
}
