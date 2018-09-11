
package com.hhly.crawlcore.v2.plugin;

import java.io.UnsupportedEncodingException;

import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.skeleton.base.common.LotteryEnum.Lottery;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractCrawl {
	
	/**
	 * 竞技彩编码
	 */
    protected static final Integer fbLotteryCode = Lottery.FB.getName();
    
    protected static final Integer bbLotteryCode = Lottery.BB.getName();
    
    protected static final Integer zc6LotteryCode = Lottery.ZC6.getName();
    
    protected static final Integer jq4LotteryCode = Lottery.JQ4.getName();
    
    protected static final Integer sfcLotteryCode = Lottery.SFC.getName();
    
    protected static final Integer nineLotteryCode = Lottery.ZC_NINE.getName();
    
    protected static final Integer bdLotteryCode = Lottery.BJDC.getName();
    
    protected static final Integer wfLotteryCode = Lottery.SFGG.getName();
    
    /**
     * 数据来源
     */
    protected static final Short sportterySource = SportDataSource.JC_OFFICIAL.getValue();
    
    protected static final Short fivehundredSource = SportDataSource.FIVEHUNDRED_OFFICIAL.getValue();
    
    protected static final Short aiboSource = SportDataSource.AIBO_OFFICIAL.getValue();
    
    protected static final Short jimiSource = SportDataSource.JIMI_OFFICIAL.getValue();

    /**
     * http请求时间参数
     */
    /**
     * 连接失效时间
     */
    protected static final int connectTimeout = 10000;
    
    /**
     * 读取数据时间
     */
    protected static final int socketTimeout = 30000;
    /**
     * 系统创建人和修改人
     */
    protected static final String systemBy = "system";
    
	protected LotteryIssueDaoMapper lotteryIssueDaoMapper;
	
	protected SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;
    
    protected SportTeamInfoService sportTeamInfoService;

    protected SportMatchInfoService sportMatchInfoService;
    
    public AbstractCrawl(){
    	
    }
    
	public AbstractCrawl(LotteryIssueDaoMapper lotteryIssueDaoMapper,
			SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper, SportTeamInfoService sportTeamInfoService,
			SportMatchInfoService sportMatchInfoService) {
		this.lotteryIssueDaoMapper = lotteryIssueDaoMapper;
		this.sportAgainstInfoDaoMapper = sportAgainstInfoDaoMapper;
		this.sportTeamInfoService = sportTeamInfoService;
		this.sportMatchInfoService = sportMatchInfoService;
	}    
    
	protected SportTeamInfoPO getTeamInfo(SportTeamSourceInfoPO reqPO) throws UnsupportedEncodingException{
		return sportTeamInfoService.getTeamInfo(reqPO);
	}
	
	protected SportMatchInfoPO getMatchInfo(SportMatchSourceInfoPO reqPO) throws UnsupportedEncodingException{
		return sportMatchInfoService.getMatchInfo(reqPO);
	}   
    
	/**
	 * 抓取功能处理类
	 * @date 2017年11月14日下午4:01:46
	 * @author cheng.chen
	 * @throws Exception 
	 */
    public abstract void crawlHandle() throws Exception;
	
	
	/**
	 * 抓取全部赛程
	 * @throws Exception
	 * @date 2017年11月23日下午12:12:23
	 * @author cheng.chen
	 */
    public abstract void crawlMatch() throws Exception;
	
	/**
	 * 解析数据处理类
	 * 
	 * @date 2017年11月14日下午4:02:19
	 * @author cheng.chen
	 * @throws Exception 
	 */
    public abstract void dataHandle(String... params) throws Exception;
}
