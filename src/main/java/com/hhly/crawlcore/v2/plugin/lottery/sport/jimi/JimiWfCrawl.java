
package com.hhly.crawlcore.v2.plugin.lottery.sport.jimi;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.lottery.po.LotteryTypePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBjSpDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDrawWfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportStatusBjDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDrawWfPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportStatusBjPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.common.Constants.bjdcStatus;
import com.hhly.crawlcore.sport.common.MatchStatusEnum;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.WfMatchResponseMsg;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.WfResultResponseMsg;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity.WfMatch;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity.WfResult;
import com.hhly.skeleton.base.common.LotteryEnum.ConIssue;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.JcMatchStatusEnum;
import com.hhly.skeleton.base.common.SportEnum.MatchTypeEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.common.SportEnum.SportWDFEnum;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @desc
 * @author cheng chen
 * @date 2018年6月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class JimiWfCrawl extends AbstractJimiBjCrawl {

	private static final Logger log = LoggerFactory.getLogger(JimiWfCrawl.class);

//	@Autowired
//	SportDataBjWdfDaoMapper sportDataBjWdfDaoMapper;
//	
//	@Autowired
//	SportDataBjHfWdfDaoMapper sportDataBjHfWdfDaoMapper;
//	
//	@Autowired
//	SportDataBjScoreDaoMapper sportDataBjScoreDaoMapper;
//	
//	@Autowired
//	SportDataBjGoalDaoMapper sportDataBjGoalDaoMapper;
//	
//	@Autowired
//	SportDataBjUdsdDaoMapper sportDataBjUdsdDaoMapper;
	
	@Autowired
	SportDataBjSpDaoMapper sportDataBjSpDaoMapper;
	
	@Autowired
	SportDrawWfDaoMapper SportDrawWfDaoMapper;
	
	@Autowired
	SportStatusBjDaoMapper sportStatusBjDaoMapper;	
	
	@Autowired
	LotteryTypeDaoMapper lotteryTypeDaoMapper;
	
	String createBy = SportDataSource.JIMI_OFFICIAL.getName();

	
    @Autowired
	public JimiWfCrawl(LotteryIssueDaoMapper lotteryIssueMapper, SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper,
			SportTeamInfoService sportTeamInfoService, SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}	

	@Override
	protected Integer getLotteryCode() throws Exception {
		return wfLotteryCode;
	}

	@Override
	public void crawlHandle() throws Exception {
		String xml = responseDealer("QueryBdsfggMatch", jimiChanelPO.getDrawerAccount());
		WfMatchResponseMsg response = getResponseMsg(WfMatchResponseMsg.class, xml);
		List<WfMatch> list = response.getBody().getMatchs();		
		
		if(!ObjectUtil.isBlank(list)){
			//初始化公共数据
			Integer lotteryCode = getLotteryCode();
			
			String issueCode = response.getBody().getIssue();
			
			issueCode = SportLotteryUtil.handleBjIssueCode(issueCode);
			
			LotteryTypePO lotTypePO = lotteryTypeDaoMapper.findTypeUseAddIssue(getLotteryCode());
	
			for (WfMatch wfMatch : list) {
				
				Integer num = wfMatch.getMatchNum();
				
				String systemCode = SportLotteryUtil.getBjSystemCode(num, issueCode);
				
		        SportAgainstInfoPO po = sportAgainstInfoDaoMapper.findByCode(lotteryCode, issueCode, systemCode);
		        
                Date startTime = DateUtil.convertStrToDate(wfMatch.getGameTime(), DateUtil.DATE_FORMAT_NUM);
                
				if(!ObjectUtil.isBlank(po)){
					if(JcMatchStatusEnum.isUpdate(po.getMatchStatus()))
						continue;
					po.setStartTime(startTime);
	                po.setSaleDate(SportLotteryUtil.getBjSaleDate(startTime));
	                po.setSaleEndTime(SportLotteryUtil.getBjSaleEndTime(po.getStartTime(), lotTypePO.getBuyEndTime()));
					po.setModifyBy(createBy);					
				}else{
					po = new SportAgainstInfoPO();
					po.setLotteryCode(getLotteryCode());
					po.setIssueCode(issueCode);
					po.setBjNum(num);
							
					Short type = MatchTypeEnum.getMatchTypeEnumCode(wfMatch.getLeagueName());
							 
					String matchName = wfMatch.getMatchName();
					String homeName = wfMatch.getHostTeam();
					String guestName = wfMatch.getGuestTeam();
					
					//赛事不存在, 新增赛事信息
					SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(homeName, null, type, null, jimiSource);

					SportTeamSourceInfoPO guestSourcePO = new SportTeamSourceInfoPO(guestName, null, type, null, jimiSource);

					//对吉米赛事名称进行处理, 对中文后的任意字符去除
					matchName = matchName.replaceAll("([\u4E00-\u9FA5])([^\u4E00-\u9FA5]*)", "$1");
					SportMatchSourceInfoPO matchSourcePO = new SportMatchSourceInfoPO(matchName, null, type, null, jimiSource);
					
					SportTeamInfoPO homeTeamPO = getTeamInfo(homeSourcePO);
					if(!ObjectUtil.isBlank(homeTeamPO)){
						po.setHomeTeamId(homeTeamPO.getId());
						po.setHomeName(homeTeamPO.getTeamFullName());
					}else{
						po.setHomeName(homeName);
					}
					SportTeamInfoPO awayTeamPO = getTeamInfo(guestSourcePO);
					if(!ObjectUtil.isBlank(awayTeamPO)){
						po.setVisitiTeamId(awayTeamPO.getId());
						po.setVisitiName(awayTeamPO.getTeamFullName());
					}else{
						po.setVisitiName(guestName);
					}
					SportMatchInfoPO leaguePO = getMatchInfo(matchSourcePO);
					if(!ObjectUtil.isBlank(leaguePO)){
						po.setSportMatchInfoId(leaguePO.getId());
						po.setMatchName(leaguePO.getMatchFullName());
					}else{
						po.setMatchName(matchName);
					}				
					
	                po.setStartTime(startTime);
	                po.setSaleDate(SportLotteryUtil.getBjSaleDate(startTime));
	                po.setSaleEndTime(SportLotteryUtil.getBjSaleEndTime(po.getStartTime(), lotTypePO.getBuyEndTime()));
	                
	                po.setMatchStatus((short) MatchStatusEnum.SALE.getCode());
	                po.setCreateBy(SportDataSource.JIMI_OFFICIAL.getName());
	                po.setMatchType(SportEnum.MatchTypeEnum.FOOTBALL.getCode());
	                po.setSystemCode(systemCode);           
	                //设置推荐赛事默认为0
	                po.setIsRecommend((short)0);					
				}					
                
				sportAgainstInfoDaoMapper.merge(po);
				
				SportStatusBjPO statusPO = sportStatusBjDaoMapper.findByMatchId(po.getId());
				if(ObjectUtil.isBlank(statusPO)){
					statusPO = new SportStatusBjPO();
					statusPO.setSportAgainstInfoId(po.getId());
					statusPO.setStatusLetWf(bjdcStatus.REGULAR_SALE.getKey());
					statusPO.setModifyBy(createBy);	
					sportStatusBjDaoMapper.merge(statusPO);
				}				
				
				SportDataBjSpPO spPO = new SportDataBjSpPO();
				spPO.setSportAgainstInfoId(po.getId());
				spPO.setLetScore(Float.valueOf(wfMatch.getConcedeNum()));
				//设置胜负过关sp值;
				String[] sfggOdd = wfMatch.getSfggOdds().split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				
				spPO.setSpWinWf(Float.valueOf(sfggOdd[0]));
				spPO.setSpFailWf(Float.valueOf(sfggOdd[1]));
				
				sportDataBjSpDaoMapper.merge(spPO);
			}
		}
	}
	
	@Override
	public void dataHandle(String... params) throws Exception {
		String issueCode = "";
		if(!ObjectUtil.isBlank(params[0])){
			issueCode = params[0];
		}else{
			LotteryIssuePO paramPO = new LotteryIssuePO();
			paramPO.setLotteryCode(getLotteryCode());
			paramPO.setCurrentIssue(ConIssue.CURRENT.getValue());
			LotteryIssuePO dataPO = lotteryIssueDaoMapper.findSingle(paramPO);	
			issueCode = dataPO.getIssueCode();
		}
		String xml = responseDealer("QueryBdsfggMatchResult", jimiChanelPO.getDrawerAccount(),issueCode.substring(1, issueCode.length()));
		WfResultResponseMsg response = getResponseMsg(WfResultResponseMsg.class, xml);	
		
		List<WfResult> wfResults = response.getBody().getWfResults();
		if(!ObjectUtil.isBlank(wfResults)){
			
			Integer lotteryCode = getLotteryCode();
			
			for (WfResult wfResult : wfResults) {
				if(ObjectUtil.isBlank(wfResult) ||ObjectUtil.isBlank(wfResult.getQuanScore()) ||wfResult.getQuanScore().equals("*")){
					continue;
				}
				Integer num = wfResult.getMatchNum();
				
				String systemCode = SportLotteryUtil.getBjSystemCode(num, issueCode);
				
				Long id = sportAgainstInfoDaoMapper.findIdByCode(lotteryCode, issueCode, systemCode);
				
				if(ObjectUtil.isBlank(id))
					continue;
				
				SportDataBjSpPO spPO = sportDataBjSpDaoMapper.findByMatchId(id);
				
				SportDrawWfPO po = new SportDrawWfPO();
				po.setSportAgainstInfoId(id);
				po.setModifyBy(createBy);
				po.setLetNum(spPO.getLetScore());
				po.setFullScore(wfResult.getQuanScore().replace(SymbolConstants.TRAVERSE_SLASH, SymbolConstants.COLON));
				po.setLetSf(SportWDFEnum.getWdfValue(wfResult.getSfggResult()).toString());
				po.setSpLetWf(wfResult.getSfggAward());
				
				SportDrawWfDaoMapper.merge(po);
			}
		}
	}	
}
