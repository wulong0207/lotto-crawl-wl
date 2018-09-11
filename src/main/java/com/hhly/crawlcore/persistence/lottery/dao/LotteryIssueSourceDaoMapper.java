package com.hhly.crawlcore.persistence.lottery.dao;

import com.hhly.crawlcore.persistence.lottery.po.LotteryIssueSourcePO;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.task.lottery.vo.LotteryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chengkangning
 * @version 1.0
 * @desc
 * @date 2017/11/16
 * @company 益彩网络科技公司
 */
public interface LotteryIssueSourceDaoMapper {

	/**
	 * 入库
	 *
	 * @param lotteryIssueSourcePO
	 *            参数
	 * @return 影响行数
	 */
	int insertSelective(LotteryIssueSourcePO lotteryIssueSourcePO);

	/**
	 * 更新
	 *
	 * @param lotteryIssueSourcePO
	 *            参数和条件
	 * @return 影响行数
	 */
	int updateByExampleSelective(LotteryIssueSourcePO lotteryIssueSourcePO);

	/**
	 * 根据条件查询
	 *
	 * @param lotteryVO
	 *            条件对象
	 * @return 结果
	 * @throws Exception
	 *             异常
	 */
	List<LotteryIssueSourcePO> selectCondition(LotteryVO lotteryVO) throws Exception;

	List<LotteryIssueSourcePO> selectForLotteryTime(LotteryVO lotteryVO) throws Exception;

	/**
	 * 查询出彩果不同的数据
	 *
	 * @param lotteryCodes
	 *            彩种
	 * @return List
	 * @throws Exception
	 *             Exception
	 */
	List<LotteryIssueSourcePO> selectWarningData(@Param("lotteryCodes") List<Integer> lotteryCodes) throws Exception;

	/**
	 * 查询出彩果不同的数据-地方彩
	 *
	 * @param lotteryCodes
	 *            lotteryCodes
	 * @return List
	 * @throws Exception
	 *             Exception
	 */
	List<LotteryIssueSourcePO> selectWarningLocal(@Param("lotteryCodes") List<Integer> lotteryCodes) throws Exception;

	/**
	 * 需要同步的数据，已经去掉报警数据，高频彩
	 *
	 * @param lotteryCode
	 *            彩种
	 * @param issueCodes
	 *            过滤报警
	 * @return List
	 * @throws Exception
	 *             Exception
	 */
	List<LotteryIssueSourcePO> selectSynData(@Param("lotteryCode") Integer lotteryCode, @Param("issueCodes") List<String> issueCodes)
			throws Exception;

	/**
	 * 需要同步的数据，已经去掉报警数据，地方彩
	 *
	 * @param lotteryCode
	 *            彩种
	 * @param issueCodes
	 *            过滤报警
	 * @return List
	 * @throws Exception
	 *             Exception
	 */
	List<LotteryIssueSourcePO> synLocalData(@Param("lotteryCode") Integer lotteryCode, @Param("issueCodes") List<String> issueCodes)
			throws Exception;

	/**
	 * 查询需要合并的彩种
	 *
	 * @return 集合
	 * @throws Exception
	 *             Exception
	 */
	List<DicDataDetailBO> selectLotteryCode() throws Exception;

	/**
	 * 检查此彩期今天是否已经有过报警
	 *
	 * @param alarmChild
	 *            alarmChild
	 * @param alarmInfo
	 *            alarmInfo
	 * @return int
	 * @throws Exception
	 *             Exception
	 */
	int queryAlarmInfo(@Param("alarmChild") int alarmChild, @Param("alarmInfo") String alarmInfo) throws Exception;

	/**
	 * lotto-task 彩期开奖号码抓取入库修改
	 *
	 * @param lotteryCode
	 *            彩种
	 * @param issueCode
	 *            彩期
	 * @return LotteryIssueSourcePO
	 * @throws Exception
	 *             Exception
	 */
	LotteryIssueSourcePO querySingleDrawCode(@Param("lotteryCode") Integer lotteryCode, @Param("issueCode") String issueCode)
			throws Exception;

	/**
	 * 查询报警
	 * 
	 * @param lotteryType
	 * @param dicDataCode
	 * @return
	 */
	List<LotteryIssueSourcePO> findWarningIssueList(@Param("lotteryCategory") Integer lotteryCategory, @Param("dicCode") String dicCode);
	
	/**
	 * 查询需要合并的彩果
	 * @param lotteryCategory
	 * @param dicCode
	 * @return
	 */
	List<LotteryIssueSourcePO> findMergeIssueList(@Param("lotteryCategory") Integer lotteryCategory, @Param("dicCode") String dicCode);
	
	/**
	 * 更新状态
	 * @param po
	 * @return
	 */
	int updateIssueSourceStatus(LotteryIssueSourcePO po);
	
	/**
	 * 查询详情
	 * @param lotteryCode
	 * @param issueCode
	 * @return
	 */
	LotteryIssueSourcePO findMergeIssue(@Param("lotteryCode") Integer lotteryCode, @Param("issueCode") String issueCode);
}
