package com.hhly.crawlcore.v2.draw.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.base.common.CrawlConstants;
import com.hhly.crawlcore.base.common.LotteryCrawlConstants;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueSourceDaoMapper;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssueSourcePO;
import com.hhly.crawlcore.persistence.lottery.po.LotteryTypePO;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.v2.draw.service.DrawMergeService;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.LotterySourceEnum;
import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.ObjectUtil;

@Service
public class DrawMergeServiceImpl implements DrawMergeService {

	private static final Logger logger = LoggerFactory.getLogger(DrawMergeServiceImpl.class);
	private static final Set<Integer> synchronizedSet = new HashSet<Integer>();

	@Autowired
	private LotteryTypeDaoMapper lotteryTypeDaoMapper;
	@Autowired
	private LotteryIssueSourceDaoMapper lotteryIssueSourceDaoMapper;
	@Autowired
	private LotteryIssueDaoMapper lotteryIssueDaoMapper;
	@Autowired
	private MQUtils mqUtils;

	@Override
	public void merge(Integer lotteryCategory) {
		doWarning(lotteryCategory);
		doMerge(lotteryCategory);
	}

	public void doMerge(Integer lotteryCode, List<LotteryIssueSourcePO> mergeIssueList) {
		synchronized (synchronizedSet) {
			if (synchronizedSet.contains(lotteryCode)) {
				logger.error("该彩种正在进行开奖号码合并:{}", lotteryCode);
				return;
			}
			synchronizedSet.add(lotteryCode);
		}
		try {
			for (LotteryIssueSourcePO mergeIssue : mergeIssueList) {
				doMerge(mergeIssue);
			}
			doIssueLastest(lotteryCode);
		} finally {
			synchronizedSet.remove(lotteryCode);
		}
	}

	/**
	 * 报警
	 */
	public void doWarning(Integer lotteryCategory) {
		// 查找待报警的
		List<LotteryIssueSourcePO> warnIssueList = lotteryIssueSourceDaoMapper.findWarningIssueList(lotteryCategory,
				CrawlConstants.ISSUE_DRAW_CODE_MERGE_DIC);
		if (ObjectUtil.isBlank(warnIssueList)) {
			logger.info("开奖号码合并，没有查到需要报警的数据！");
			return;
		}
		for (LotteryIssueSourcePO warnIssue : warnIssueList) {
			// 发送报警
			StringBuilder content = new StringBuilder();
			content.append(warnIssue.getLotteryName());
			content.append("(").append(warnIssue.getLotteryCode()).append(")");
			content.append(warnIssue.getIssueCode()).append("期开奖号码不一致");
			mqUtils.sendWarnMessage(content.toString(), CrawlConstants.WARN_DRAW_CODE_MERGE);
			// 更新为已报警状态
			warnIssue.setIssueStatus(LotterySourceEnum.IssueStatus.WARN.getValue());
			lotteryIssueSourceDaoMapper.updateIssueSourceStatus(warnIssue);
		}
		logger.info("开奖号码合并，报警完成:{}", warnIssueList.size());
	}

	/**
	 * 执行合并开奖号码操作
	 */
	private void doMerge(Integer lotteryCategory) {
		List<LotteryIssueSourcePO> mergeIssueList = lotteryIssueSourceDaoMapper.findMergeIssueList(lotteryCategory,
				CrawlConstants.ISSUE_DRAW_CODE_MERGE_DIC);
		if (ObjectUtil.isBlank(mergeIssueList)) {
			logger.info("开奖号码合并，没有查到需要合并的数据！");
			return;
		}
		Map<Integer, List<LotteryIssueSourcePO>> mergeIssueMap = new HashMap<>();
		for (LotteryIssueSourcePO mergeIssue : mergeIssueList) {
			List<LotteryIssueSourcePO> list = mergeIssueMap.get(mergeIssue.getLotteryCode());
			if (list == null) {
				list = new ArrayList<>();
				mergeIssueMap.put(mergeIssue.getLotteryCode(), list);
			}
			list.add(mergeIssue);
		}
		for (Map.Entry<Integer, List<LotteryIssueSourcePO>> entry : mergeIssueMap.entrySet()) {
			final Integer lotteryCode = entry.getKey();
			final List<LotteryIssueSourcePO> list = entry.getValue();
			ThreadPoolManager.execute("彩果合并入库:" + lotteryCode, new Runnable() {
				@Override
				public void run() {
					DrawMergeServiceImpl.this.doMerge(lotteryCode, list);
				}
			});
		}
	}

	/**
	 * 合并指定彩期开奖号码
	 * 
	 * @param mergeIssue
	 */
	private void doMerge(LotteryIssueSourcePO mergeIssue) {
		logger.info("开奖号码合并:{},{}", mergeIssue.getLotteryCode(), mergeIssue.getIssueCode());
		LotteryTypePO lotteryTypePO = lotteryTypeDaoMapper.findTypeUseAddIssue(mergeIssue.getLotteryCode());
		// 合并彩果入库
		LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
		lotteryIssuePO.setLotteryCode(mergeIssue.getLotteryCode());
		lotteryIssuePO.setIssueCode(mergeIssue.getIssueCode());
		lotteryIssuePO.setLotteryName(lotteryTypePO.getLotteryName());
		lotteryIssuePO.setRemark(CrawlConstants.ISSUE_DRAW_CODE_MERGE_REMARK);
		lotteryIssuePO.setLotteryTime(getLotteryTime(lotteryTypePO, mergeIssue));
		lotteryIssuePO.setOfficialEndTime(DateUtil.addSecond(lotteryIssuePO.getLotteryTime(), -30));
		lotteryIssuePO.setCreateBy(CrawlConstants.DEFAULT_CREATE_BY);
		lotteryIssueDaoMapper.insertOrUpdateIssue(lotteryIssuePO);
		// 更新为已处理状态
		LotteryIssueSourcePO lotteryIssueSourcePO = new LotteryIssueSourcePO();
		lotteryIssueSourcePO.setLotteryCode(mergeIssue.getLotteryCode());
		lotteryIssueSourcePO.setIssueCode(mergeIssue.getIssueCode());
		lotteryIssueSourcePO.setIssueStatus(LotterySourceEnum.IssueStatus.DONE.getValue());
		lotteryIssueSourceDaoMapper.updateIssueSourceStatus(lotteryIssueSourcePO);
	}

	/**
	 * 处理彩期最新开奖和上一期状态
	 * 
	 * @param lotteryCode
	 */
	private void doIssueLastest(Integer lotteryCode) {
		logger.info("开奖号码合并，更新彩期状态和最新开奖:{}", lotteryCode);
		lotteryIssueDaoMapper.updateOtherData(lotteryCode);
		lotteryIssueDaoMapper.updateCurrentIssue(lotteryCode);
		lotteryIssueDaoMapper.updateIssueLastest(lotteryCode);
	}

	/**
	 * 计算开奖时间
	 * 
	 * @param lotteryTypePO
	 * @param issueCode
	 * @return
	 */
	private Date getLotteryTime(LotteryTypePO lotteryTypePO, LotteryIssueSourcePO mergeIssue) {
		if (lotteryTypePO.getLotteryCategory() == null
				|| lotteryTypePO.getLotteryCategory().intValue() != LotteryEnum.LotteryCategory.HIGH.getValue()) {
			logger.info("非高频彩不计算开奖时间:{}", lotteryTypePO.getLotteryCode());
			return null;
		}
		Integer lotteryCode = lotteryTypePO.getLotteryCode();
		if (LotteryCrawlConstants.isUnMergeLotteryTime(lotteryCode)) {
			return null;
		}
		if (ObjectUtil.isBlank(lotteryTypePO.getFormat())) {// 彩期格式
			logger.error("开奖号码合并失败，彩期格式未设置，彩种:{}", lotteryCode);
			throw new ServiceRuntimeException("开奖号码合并失败，彩期格式未设置:" + lotteryCode);
		}
		String issueCode = mergeIssue.getIssueCode();
		// 处理彩期，
		Date issueDate = null;
		String issueDateStr = null;
		Integer issueKey = null;
		if (LotteryCrawlConstants.isUseIssueDateFormSource(lotteryCode)) {
			// 原来对象没有开奖时间，需要重新查一遍，去最大开奖时间那个
			mergeIssue = lotteryIssueSourceDaoMapper.findMergeIssue(lotteryCode, issueCode);
			// 特殊彩种，直接根据抓取的开奖日期计算开奖日期，截取后2位做
			issueDateStr = DateUtil.convertDateToStr(mergeIssue.getLotteryTime(), DateUtil.DATE_FORMAT);
			issueDate = DateUtil.convertStrToDate(issueDateStr, DateUtil.DATE_FORMAT);
			issueKey = Integer.parseInt(issueCode.substring(issueCode.length() - 2, issueCode.length()));
		} else {
			// 根据彩期格式，计算彩期所属日期和第几期
			String lowerFormat = lotteryTypePO.getFormat().toLowerCase();
			Integer issueSuffix = lowerFormat.length() - lowerFormat.replaceAll("x", "").length();
			String issueDateFormat = lowerFormat.replace("x", "").replaceAll("m", "M");
			issueDate = DateUtil.convertStrToDate(issueCode.substring(0, issueCode.length() - issueSuffix), issueDateFormat);
			issueDateStr = DateUtil.convertDateToStr(issueDate, DateUtil.DATE_FORMAT);
			issueKey = Integer.parseInt(issueCode.substring(issueCode.length() - issueSuffix, issueCode.length()));
		}
		if (issueDate == null || issueKey == null) {
			logger.error("开奖号码合并失败，彩期解析失败，彩种:{},彩期:{}", lotteryCode, issueCode);
			throw new ServiceRuntimeException("开奖号码合并失败，彩期解析失败，彩种:" + lotteryCode);
		}
		// 根据彩种设置计算彩期当天所有开奖时间
		// 1-23|300,24-24|29100,25-96|600,97-120|300
		String sailDayCycle = lotteryTypePO.getSailDayCycle();
		// 1|00:05|0,2|00:05|0,3|00:05|0,4|00:05|0,5|00:05|0,6|00:05|0,7|00:05|0
		String endSailTimeAllStr = lotteryTypePO.getEndSailTime();
		if (ObjectUtil.isBlank(sailDayCycle) || ObjectUtil.isBlank(endSailTimeAllStr)) {
			logger.error("开奖号码合并失败，彩种设置缺失， 彩种:{}, 销售周期:{}, 第一期销售截止时间:{}", lotteryCode, sailDayCycle, endSailTimeAllStr);
			throw new ServiceRuntimeException("开奖号码合并失败，彩种设置缺失:" + lotteryCode);
		}
		int week = DateUtil.dayForWeek(issueDate);
		Matcher m = Pattern.compile("(\\d+)-(\\d+)\\|(\\d+)").matcher(sailDayCycle);
		// 处理销售周期
		Map<Integer, Integer> sailDayCycleMap = new HashMap<>();
		Integer totalIssue = 0;
		while (m.find()) {
			Integer start = Integer.parseInt(m.group(1));
			Integer end = Integer.parseInt(m.group(2));
			Integer frequency = Integer.parseInt(m.group(3));
			for (int i = start; i <= end; i++) {
				sailDayCycleMap.put(i, frequency);
			}
			if (end > totalIssue) {
				totalIssue = end;
			}
		}
		// 处理第一期截止销售时间
		m = Pattern.compile(week + "\\|(\\d+:\\d+)").matcher(endSailTimeAllStr);
		String endTimeStr = null;
		if (m.find()) {
			endTimeStr = m.group(1);
		}
		Date endTime = DateUtil.convertStrToDate(issueDateStr + " " + endTimeStr, DateUtil.DATETIME_FORMAT_NO_SEC);
		// 处理每期开奖时间，截止销售时间上加30秒
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		calendar.add(Calendar.SECOND, 30);
		Map<Integer, Date> lotteryTimeMap = new HashMap<Integer, Date>();
		lotteryTimeMap.put(1, calendar.getTime());
		for (int i = 2; i <= totalIssue; i++) {
			Integer frequency = sailDayCycleMap.get(i);
			calendar.add(Calendar.SECOND, frequency);
			lotteryTimeMap.put(i, calendar.getTime());
		}
		// 加入缓存
		if (!lotteryTimeMap.containsKey(issueKey)) {
			logger.error("开奖号码合并失败，获取开奖时间失败， 彩种:{}, 彩期:{}, 彩期后缀:{}", lotteryCode, issueCode, issueKey);
			throw new ServiceRuntimeException("开奖号码合并失败，获取开奖时间失败:" + lotteryCode);
		}
		return lotteryTimeMap.get(issueKey);
	}

}
