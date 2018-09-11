
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
import com.hhly.crawlcore.persistence.sport.dao.SportDrawBjDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportStatusBjDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDrawBjPO;
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
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.BdMatchResponseMsg;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.BdResultResponseMsg;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity.BdMatch;
import com.hhly.crawlcore.v2.plugin.lottery.sport.jimi.response.entity.BdResult;
import com.hhly.skeleton.base.common.LotteryEnum.ConIssue;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.JcMatchStatusEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.common.SportEnum.SportUdSdEnum;
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
public class JimiBdCrawl extends AbstractJimiBjCrawl {

	private static final Logger log = LoggerFactory.getLogger(JimiBdCrawl.class);

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
	SportDrawBjDaoMapper sportDrawBjDaoMapper;
	
	@Autowired
	SportStatusBjDaoMapper sportStatusBjDaoMapper;
	
	@Autowired
	LotteryTypeDaoMapper lotteryTypeDaoMapper;
	
	String createBy = SportDataSource.JIMI_OFFICIAL.getName();

	
    @Autowired
	public JimiBdCrawl(LotteryIssueDaoMapper lotteryIssueMapper, SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper,
			SportTeamInfoService sportTeamInfoService, SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}	

	@Override
	protected Integer getLotteryCode() throws Exception {
		return bdLotteryCode;
	}

	@Override
	public void crawlHandle() throws Exception {
		String xml = responseDealer("QueryBdMatch", jimiChanelPO.getDrawerAccount());
		BdMatchResponseMsg response = getResponseMsg(BdMatchResponseMsg.class, xml);
		List<BdMatch> list = response.getBody().getMatchs();		
		
		if(!ObjectUtil.isBlank(list)){
			//初始化公共数据
			Integer lotteryCode = getLotteryCode();
			
			String issueCode = response.getBody().getIssue();
			
			issueCode = SportLotteryUtil.handleBjIssueCode(issueCode);
			
			LotteryTypePO lotTypePO = lotteryTypeDaoMapper.findTypeUseAddIssue(getLotteryCode());
	
			for (BdMatch bjMatch : list) {
				
				Integer num = bjMatch.getMatchNum();
				
				String systemCode = SportLotteryUtil.getBjSystemCode(num, issueCode);
				
		        SportAgainstInfoPO po = sportAgainstInfoDaoMapper.findByCode(lotteryCode, issueCode, systemCode);
		        
                Date startTime = DateUtil.convertStrToDate(bjMatch.getGameTime(), DateUtil.DATE_FORMAT_NUM);

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
					
					String matchName = bjMatch.getMatchName();
					String homeName = bjMatch.getHostTeam();
					String guestName = bjMatch.getGuestTeam();
					
					//赛事不存在, 新增赛事信息
					SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(homeName, null, SportEnum.MatchTypeEnum.FOOTBALL.getCode(), null, jimiSource);

					SportTeamSourceInfoPO guestSourcePO = new SportTeamSourceInfoPO(guestName, null, SportEnum.MatchTypeEnum.FOOTBALL.getCode(), null,
							jimiSource);

					//对吉米赛事名称进行处理, 对中文后的任意字符去除
					matchName = matchName.replaceAll("([\u4E00-\u9FA5])([^\u4E00-\u9FA5]*)", "$1");
					SportMatchSourceInfoPO matchSourcePO = new SportMatchSourceInfoPO(matchName, null, SportEnum.MatchTypeEnum.FOOTBALL.getCode(), null,
							jimiSource);
					
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
				
				//北单子玩法状态创建
				SportStatusBjPO statusPO = sportStatusBjDaoMapper.findByMatchId(po.getId());
				if(ObjectUtil.isBlank(statusPO)){
					statusPO = new SportStatusBjPO();
					statusPO.setSportAgainstInfoId(po.getId());
					statusPO.setStatusLetWdf(bjdcStatus.REGULAR_SALE.getKey());
					statusPO.setStatusGoal(bjdcStatus.REGULAR_SALE.getKey());
					statusPO.setStatusHfWdf(bjdcStatus.REGULAR_SALE.getKey());
					statusPO.setStatusUdSd(bjdcStatus.REGULAR_SALE.getKey());
					statusPO.setStatusScore(bjdcStatus.REGULAR_SALE.getKey());
					statusPO.setModifyBy(createBy);	
					sportStatusBjDaoMapper.merge(statusPO);
				}
				
				SportDataBjSpPO spPO = new SportDataBjSpPO();
				spPO.setSportAgainstInfoId(po.getId());
				
				//设置让球胜平负
				String spfOdds = bjMatch.getSpfOdds();
				String[] spfOdd = spfOdds.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				spPO.setLetNum(bjMatch.getConcedeNum().floatValue());
				spPO.setSpWin(Float.valueOf(spfOdd[0]));
				spPO.setSpDraw(Float.valueOf(spfOdd[1]));
				spPO.setSpFail(Float.valueOf(spfOdd[2]));
				
				//设置上下单双
				String sxdsOdds = bjMatch.getSxdsOdds();
				String[] sxdsOdd = sxdsOdds.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				spPO.setSpUpSingle(Float.valueOf(sxdsOdd[0]));
				spPO.setSpUpDouble(Float.valueOf(sxdsOdd[1]));
				spPO.setSpDownSingle(Float.valueOf(sxdsOdd[2]));
				spPO.setSpDownDouble(Float.valueOf(sxdsOdd[3]));
				
				//设置总进球
				String zjqOdds = bjMatch.getZjqOdds();
				String[] zjqOdd = zjqOdds.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				spPO.setSp0Goal(Float.valueOf(zjqOdd[0]));
				spPO.setSp1Goal(Float.valueOf(zjqOdd[1]));
				spPO.setSp2Goal(Float.valueOf(zjqOdd[2]));
				spPO.setSp3Goal(Float.valueOf(zjqOdd[3]));
				spPO.setSp4Goal(Float.valueOf(zjqOdd[4]));
				spPO.setSp5Goal(Float.valueOf(zjqOdd[5]));
				spPO.setSp6Goal(Float.valueOf(zjqOdd[6]));
				spPO.setSp7Goal(Float.valueOf(zjqOdd[7]));
				
				//设置bfOdds
				String bfOdds = bjMatch.getBfOdds();
				String[] bfOdd = bfOdds.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				spPO.setSpWOther(Float.valueOf(bfOdd[0]));
				spPO.setSp10(Float.valueOf(bfOdd[1]));
				spPO.setSp20(Float.valueOf(bfOdd[2]));
				spPO.setSp21(Float.valueOf(bfOdd[3]));
				spPO.setSp30(Float.valueOf(bfOdd[4]));
				spPO.setSp31(Float.valueOf(bfOdd[5]));
				spPO.setSp32(Float.valueOf(bfOdd[6]));
				spPO.setSp40(Float.valueOf(bfOdd[7]));
				spPO.setSp41(Float.valueOf(bfOdd[8]));
				spPO.setSp42(Float.valueOf(bfOdd[9]));
				spPO.setSpDOther(Float.valueOf(bfOdd[10]));
				spPO.setSp00(Float.valueOf(bfOdd[11]));
				spPO.setSp11(Float.valueOf(bfOdd[12]));
				spPO.setSp22(Float.valueOf(bfOdd[13]));
				spPO.setSp33(Float.valueOf(bfOdd[14]));
				spPO.setSpFOther(Float.valueOf(bfOdd[15]));
				spPO.setSp01(Float.valueOf(bfOdd[16]));
				spPO.setSp02(Float.valueOf(bfOdd[17]));
				spPO.setSp12(Float.valueOf(bfOdd[18]));
				spPO.setSp03(Float.valueOf(bfOdd[19]));
				spPO.setSp13(Float.valueOf(bfOdd[20]));
				spPO.setSp23(Float.valueOf(bfOdd[21]));
				spPO.setSp04(Float.valueOf(bfOdd[22]));
				spPO.setSp14(Float.valueOf(bfOdd[23]));
				spPO.setSp24(Float.valueOf(bfOdd[24]));	
				
				//设置半全场
				String bqcOdds = bjMatch.getBqcOdds();
				String[] bqcOdd = bqcOdds.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				spPO.setSpWW(Float.valueOf(bqcOdd[0]));
				spPO.setSpWD(Float.valueOf(bqcOdd[1]));
				spPO.setSpWF(Float.valueOf(bqcOdd[2]));
				spPO.setSpDW(Float.valueOf(bqcOdd[3]));
				spPO.setSpDD(Float.valueOf(bqcOdd[4]));
				spPO.setSpDF(Float.valueOf(bqcOdd[5]));
				spPO.setSpFW(Float.valueOf(bqcOdd[6]));
				spPO.setSpFD(Float.valueOf(bqcOdd[7]));
				spPO.setSpFF(Float.valueOf(bqcOdd[8]));
				
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
		String xml = responseDealer("QueryBdMatchResult", jimiChanelPO.getDrawerAccount(),issueCode.substring(1, issueCode.length()));
		BdResultResponseMsg response = getResponseMsg(BdResultResponseMsg.class, xml);	
		
		List<BdResult> bjResults = response.getBody().getBjResults();
		if(!ObjectUtil.isBlank(bjResults)){
			
			Integer lotteryCode = getLotteryCode();
			
			for (BdResult bjResult : bjResults) {
				if(ObjectUtil.isBlank(bjResult) ||ObjectUtil.isBlank(bjResult.getBfResult()) ||bjResult.getBfResult().equals("*")){
					continue;
				}

				Integer num = bjResult.getMatchNum();
				
				String systemCode = SportLotteryUtil.getBjSystemCode(num, issueCode);
				
				Long id = sportAgainstInfoDaoMapper.findIdByCode(lotteryCode, issueCode, systemCode);
				
				if(ObjectUtil.isBlank(id))
					continue;
				
				SportDataBjSpPO spPO = sportDataBjSpDaoMapper.findByMatchId(id);
				
				SportDrawBjPO po = new SportDrawBjPO();
				po.setSportAgainstInfoId(id);
				po.setModifyBy(createBy);
				po.setHalfScore(bjResult.getBanScore());
				String score = "";
				if(!bjResult.getBfResult().contains("其他")){
					po.setFullScore(bjResult.getBfResult());
					po.setScore(bjResult.getBfResult().replace(SymbolConstants.COLON, ""));
				}else{
					switch (bjResult.getBfResult()) {
					case "胜其他":
						score = "90";
						break;
					case "平其他":
						score = "99";
						break;
					case "负其他":
						score = "09";
						break;
					}
					po.setScore(score);
				}
				po.setSpScore(bjResult.getBfAward());
				
				po.setLetNum(spPO.getLetNum().doubleValue());
				po.setLetWdf(SportWDFEnum.getWdfValue(bjResult.getSpfResult()).toString());
				po.setSpLetWdf(bjResult.getSpfAward());
				
				po.setUdSd(SportUdSdEnum.getUdSdValue(bjResult.getSxdsResult()).toString());
				po.setSpUdSd(bjResult.getSxdsAward());
				//对总进球7+进行处理
				String zjq = bjResult.getZjqResult().replace(SymbolConstants.ADD, "");
				po.setGoalNum(zjq);
				po.setSpGoalNum(bjResult.getZjqAward());
				
				String[] hfWdfs = bjResult.getBqcResult().split(SymbolConstants.DOUBLE_SLASH_TRAVERSE_SLASH);
				String hfWdf = SportWDFEnum.getWdfValue(hfWdfs[0])+ "" + SportWDFEnum.getWdfValue(hfWdfs[1]);
				po.setHfWdf(hfWdf);
				po.setSpHfWdf(bjResult.getBqcAward());
				
				sportDrawBjDaoMapper.merge(po);
			}
		}
		
	}	


	public static void main(String[] args) throws Exception {
//		// 调用webservice地址
//		String url = "http://123.57.147.232:8088/service";
//		// 调用方法
//		String method = "QueryBdMatchResult";
//		// 使用axis service 调用 webservice
//		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
//		Call call = (Call) service.createCall();
//		// 设置服务地址
//		call.setTargetEndpointAddress(new URL(url));
//		// 设置调用方法
//		call.setOperationName(method);
//		// 不知, 应该是开启soap
//		call.setUseSOAPAction(true);
//		// inLicense是参数名，XSD_STRING是参数类型，IN代表传入
//		// call.addParameter("agentID",
//		// org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
//		// call.addParameter("sign",
//		// org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
//		// 设置返回类型
//		// call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
//
//		call.setEncodingStyle("http://schemas.xmlsoap.org/wsdl/soap/");
//		call.setOperationName(new QName("http://service.xixi.com/", method));
//		String agentId = "800123";
//		String issue = "80605";
//		String password = DESUtil.decrypt("FF40PsJt17KBn3odtf4KJA==");
//		System.out.println(password);
//		String sgin = DigestUtils.md5Hex(agentId + issue + password);
//		Object ret = null;
//		try {
//			// 使用invoke调用方法，Object数据放传入的参数值
//			ret = call.invoke(new Object[] { agentId, issue, sgin });
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// 输出SOAP请求报文
//		System.err.println("--SOAP Request: " + call.getMessageContext().getRequestMessage().getSOAPPartAsString());
//		// 输出SOAP返回报文
//		System.err.println("--SOAP Response: " + call.getResponseMessage().getSOAPPartAsString());
//		// 输出返回信息
//		System.err.println("result===" + ret.toString());
		String issueCode = "180603";
		issueCode = issueCode.substring(1, issueCode.length());
		System.out.println(issueCode);
	}

}
