
package com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.common.OldMatchStatusEnum;
import com.hhly.crawlcore.sport.entity.OldLotteryIssue;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;
import com.hhly.skeleton.base.common.LotteryEnum.Lottery;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.SportOldFbLottery;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;

/**
 * @desc
 * @author cheng chen
 * @date 2017年11月30日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class StOldFbCrawl extends AbstractCrawl {

	private static final Logger log = LoggerFactory.getLogger(StOldFbCrawl.class);

	// 竞彩官网微信获取赛事集合url
	@Value("${st_oldfb_get_match_info}")
	private String st_oldfb_get_match_info;

	// 竞彩官网微信获取赛事集合url
	@Value("${st_oldfb_get_match_issue}")
	private String st_oldfb_get_match_issue;

	private static final short type = (short) SportEnum.MatchTypeEnum.FOOTBALL.getCode();

	@Autowired
	private LotteryIssueDaoMapper lotteryIssueDaoMapper;
	
	public StOldFbCrawl(){
		
	}
	
    @Autowired
	public StOldFbCrawl(LotteryIssueDaoMapper lotteryIssueMapper, SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper,
			SportTeamInfoService sportTeamInfoService, SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}

	@Override
	public void crawlHandle() throws Exception {

	}

	@Override
	public void crawlMatch() throws Exception {
		try {
			
			for (final SportOldFbLottery sportOldFbLottery : SportOldFbLottery.values()) {
				if (sportOldFbLottery.getCode() == Lottery.ZC_NINE.getName())
					continue;
				List<LotteryIssuePO> lotIssuePOs = lotteryIssueDaoMapper
						.getCurrentAndNextIssuePO(sportOldFbLottery.getCode());
				for (final LotteryIssuePO po : lotIssuePOs) {
				//测试单个彩种抓取
//				final SportOldFbLottery sportOldFbLottery = SportOldFbLottery.WILO;
//				final LotteryIssuePO po = new LotteryIssuePO();
//				po.setLotteryCode(304);
//				po.setIssueCode("18011");
//				po.setSaleEndTime(DateUtil.convertStrToDate("2018-01-26 22:40:00"));
					ThreadPoolManager.execute("获取 lotteryCode : " + po.getLotteryCode() + ", issueCode : " + po.getIssueCode() + "赛事集合", new Runnable() {
						public void run() {
							try {
								getMatchByIssue(sportOldFbLottery.getKey(), po);
							} catch (Exception e) {
								log.error("获取 lotteryCode : " + po.getLotteryCode() + ", issueCode : " + po.getIssueCode() + "赛事集合异常, message : "+ e.getMessage());
							}
						}
					});
				}
			}
		} catch (Exception e) {
			log.error("执行获取老足彩赛事集合异常 , message :" + e.getMessage());
		}
	}

	@Override
	public void dataHandle(String... params) throws Exception {

	}

	public OldLotteryIssue getNextIssue(Integer lotteryCode, String issueCode) {
		OldLotteryIssue nexOldLotteryIssue = null;
		SportOldFbLottery sportOldFbLottery = SportOldFbLottery.getSportOldFbLottery(lotteryCode);
		if (ObjectUtil.isBlank(sportOldFbLottery))
			return nexOldLotteryIssue;

		OldLotteryIssue currLotteryIssue = getIssueInfo(issueCode, sportOldFbLottery.getKey());
		if(!ObjectUtil.isBlank(currLotteryIssue)){
			String nextIssue = currLotteryIssue.getNext();
			if (!StringUtil.isBlank(nextIssue)) {
				nexOldLotteryIssue = getIssueInfo(nextIssue, sportOldFbLottery.getKey());
			}
		}
		return nexOldLotteryIssue;
	}

	private void getMatchByIssue(String name, LotteryIssuePO issuePO) throws Exception {

		Integer lotteryCode = issuePO.getLotteryCode();
		String issueCode = issuePO.getIssueCode();

		String url = st_oldfb_get_match_info.replace("{0}", name).replace("{1}", issueCode) + "&_="
				+ System.currentTimeMillis();
		log.info("获取老足彩赛事集合 url : "+ url);
		String json = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
		// 查询结果为0的时候返回
		if (json.equals("0") || ObjectUtil.isBlank(json)) {
			return;
		}		
		String initData = StringUtils.stripEnd(StringUtils.stripStart(json, SymbolConstants.PARENTHESES_LEFT),
				SymbolConstants.PARENTHESES_RIGHT);
		JSONObject initJson = JsonUtil.jsonToObject(initData, JSONObject.class);
		JSONObject resultObject = initJson.getJSONObject("result");
		Set<String> keySet = resultObject.keySet();
		List<String> keys = new ArrayList<String>(keySet);
		Map<String, Boolean> map = new HashMap<>();
//		List<SportAgainstInfoPO> pos = new ArrayList<>();
		int i = 0;
		
		Collections.sort(keys, new Comparator<String>() {
			@Override
			public int compare(String key1, String key2) {
				return key1.compareTo(key2);
			}
		});
		for (String key : keys) {
			JSONObject value = resultObject.getJSONObject(key);
			
			String league = value.getString("league");
			String homeName = value.getString("h_cn");
			String guestName = value.getString("a_cn");
			String date = value.getString("date");
			String time = value.getString("time");
			String mid = value.getString("mid");
            //暂时没有使用胜平负的SP值
//          String winSp = value.getString("a");
//          String lostSp = value.getString("h");
//          String drawSp = value.getString("d");			
			Integer result = value.getInteger("result");
			//完赛的比分, 暂时没有使用
//			String full = value.getString("full");			
			
			//判断是六场半全场时, 只取一条
			if(map.containsKey(homeName + SymbolConstants.UNDERLINE + guestName))
				continue;

			Date startTime = null;
			if (!StringUtil.isBlank(time)) {
				startTime = DateUtil.convertStrToDate(date + " " + time, DateUtil.DEFAULT_FORMAT);
			} else {
				startTime = DateUtil.convertStrToDate(date, DateUtil.DATE_FORMAT);
			}
			Short matchStatus;
			if (!ObjectUtil.isNull(result)) {
				matchStatus = OldMatchStatusEnum.FINISH.getCode();
			} else {
				matchStatus = OldMatchStatusEnum.WAIT_MATCH.getCode();
			}

			String createBy = SportEnum.SportDataSource.JC_OFFICIAL.getName();

			SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(homeName, null, type, null, sportterySource);

			SportTeamSourceInfoPO guestSourcePO = new SportTeamSourceInfoPO(guestName, null, type, null, sportterySource);

			SportMatchSourceInfoPO matchSourcePO = new SportMatchSourceInfoPO(null, league, type, null, sportterySource);

			String systemCode = SportLotteryUtil.getOldLotterySystemCode(issueCode, i + 1);
			
			Long id = sportAgainstInfoDaoMapper.findIdByCode(lotteryCode, issueCode, systemCode);

			SportAgainstInfoPO po = new SportAgainstInfoPO(id, lotteryCode, issueCode, null, null, null,
					null, null, null, systemCode,
					matchStatus, mid, startTime, issuePO.getSaleEndTime(), Long.valueOf(key), createBy, createBy);
			
			SportTeamInfoPO homeTeamPO = getTeamInfo(homeSourcePO);
			if(!ObjectUtil.isBlank(homeTeamPO)){
				po.setHomeTeamId(homeTeamPO.getId());
				po.setHomeName(homeTeamPO.getTeamFullName());
			}else
				po.setHomeName(homeName);
			SportTeamInfoPO awayTeamPO = getTeamInfo(guestSourcePO);
			if(!ObjectUtil.isBlank(awayTeamPO)){
				po.setVisitiTeamId(awayTeamPO.getId());
				po.setVisitiName(awayTeamPO.getTeamFullName());
			}else
				po.setVisitiName(guestName);
			SportMatchInfoPO leaguePO = getMatchInfo(matchSourcePO);
			if(!ObjectUtil.isBlank(leaguePO)){
				po.setSportMatchInfoId(leaguePO.getId());
				po.setMatchName(leaguePO.getMatchFullName());
			}else
				po.setMatchName(league);
			
			sportAgainstInfoDaoMapper.merge(po);
			map.put(homeName + SymbolConstants.UNDERLINE + guestName, true);
			i++;
		}
//		if(!ObjectUtil.isBlank(pos)){
//			Collections.sort(pos, new Comparator<SportAgainstInfoPO>() {
//				@Override
//				public int compare(SportAgainstInfoPO po1, SportAgainstInfoPO po2) {
//					return (int) (po1.getOldMatchIndex() - po2.getOldMatchIndex());
//				}
//			});
//			for (SportAgainstInfoPO matchInfoPO : pos) {
//				sportAgainstInfoDaoMapper.merge(matchInfoPO);
//			}			
//		}
	}

	private OldLotteryIssue getIssueInfo(String issueCode, String type) {
		try {
			// 请求sporttery官网老足彩彩期查询
			String url = st_oldfb_get_match_issue.replace("{0}", type).replace("{1}", issueCode) + "&_="
					+ System.currentTimeMillis();
			String json = HTTPUtil.transRequest(url);
			// 查询结果为0的时候返回
			if (json.equals("0") || ObjectUtil.isBlank(json)) {
				return null;
			}

			String initData = StringUtils.stripEnd(StringUtils.stripStart(json, SymbolConstants.PARENTHESES_LEFT),
					SymbolConstants.PARENTHESES_RIGHT);
			JSONObject dataJson = JsonUtil.jsonToObject(initData, JSONObject.class);
			JSONObject statusJson = dataJson.getJSONObject("status");
			int code = statusJson.getInteger("code");
			String message = statusJson.getString("message");

			if (code != 0 && !message.equals("")) {
				throw new ServiceRuntimeException("获取官网www.sporttery.cn的老足彩彩期异常!!!");
			}

			return new OldLotteryIssue(dataJson.getJSONObject("result"));
		} catch (Exception e) {
			log.error("获取sporttery官网老足彩 彩期 异常 , message :" + e.getMessage());
		}
		return null;
	}

}
