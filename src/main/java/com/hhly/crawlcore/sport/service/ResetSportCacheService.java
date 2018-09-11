package com.hhly.crawlcore.sport.service;

import java.util.List;

/**
 * @author lgs on
 * @version 1.0
 * @desc 重置竞技彩抓取数据缓存
 * @date 2017/5/21.
 * @company 益彩网络科技有限公司
 */
public interface ResetSportCacheService {

    /**
     * 重置竞彩足球受注赛程缓存
     */
    void resetFbMatchList();

    /**
     * 重置竞彩足球销售截止缓存
     */
    void resetFbMatchSaleEndList();

    /**
     * 根据系统编号重置竞彩足球对阵缓存
     *
     * @param systemCode 系统编号
     */
    void resetFbMatchListBySystemCode(String systemCode);

    /**
     * 批量根据系统编号重置竞彩足球对阵缓存
     *
     * @param systemCodes 系统编号集合
     */
    void resetFbMatchListBySystemCodes(List<String> systemCodes);

    /**
     * 根据彩期重置竞彩足球对阵缓存
     *
     * @param issueCode
     */
    void resetFbMatchListByIssueCode(String issueCode);

    /**
     * 批量根据彩期重置竞彩足球对阵缓存
     *
     * @param issueCodes 彩期集合
     */
    void resetFbMatchListByIssueCodes(List<String> issueCodes);

    /**
     * 重置竞彩足球胜平负历史SP值缓存
     *
     * @param ids 对阵id集合
     */
    void resetFbMatchWdfHistorySpByIds(List<Long> ids);

    /**
     * 重置竞彩足球胜平负历史SP值缓存
     *
     * @param id 对阵id
     */
    void resetFbMatchWdfHistorySpById(Long id, Short type);


    /**
     * 重置竞彩篮球受注赛程缓存
     */
    void resetBBMatchList();

    /**
     * 根据系统编号重置竞彩篮球对阵缓存
     *
     * @param systemCode 系统编号
     */
    void resetBBMatchListBySystemCode(String systemCode);

    /**
     * 批量根据系统编号重置竞彩篮球对阵缓存
     *
     * @param systemCodes 系统编号集合
     */
    void resetBBMatchListBySystemCodes(List<String> systemCodes);

    /**
     * 根据彩期重置竞彩篮球对阵缓存
     *
     * @param issueCode
     */
    void resetBBMatchListByIssueCode(String issueCode);

    /**
     * 批量根据彩期重置竞彩篮球对阵缓存
     *
     * @param issueCodes 彩期集合
     */
    void resetBBMatchListByIssueCodes(List<String> issueCodes);

    /**
     * 重置竞彩篮球胜平负历史SP值缓存
     *
     * @param ids 对阵id集合
     */
    void resetBBMatchWfHistorySpByIds(List<Long> ids);

    /**
     * 重置竞彩篮球胜平负历史SP值缓存
     *
     * @param id 对阵id
     */
    void resetBBMatchWfHistorySpByIds(Long id);

    /**
     * 重置竞彩篮球分差历史SP值缓存
     *
     * @param ids 对阵id集合
     */
    void resetBBMatchWsHistorySpByIds(List<Long> ids);

    /**
     * 重置竞彩篮球分差历史SP值缓存
     *
     * @param id 对阵id
     */
    void resetBBMatchWsHistorySpById(Long id);

    /**
     * 重置竞彩篮球大小分历史SP值缓存
     *
     * @param ids 对阵id集合
     */
    void resetBBMatchBsHistorySpByIds(List<Long> ids);

    /**
     * 重置竞彩篮球大小分历史SP值缓存
     *
     * @param id 对阵id
     */
    void resetBBMatchBsHistorySpById(Long id);

    /**
     * 重置老足彩彩期缓存
     *
     * @param lotteryCode 彩种编号
     * @param issueCode   彩期
     */
    void resetOldMatch(Integer lotteryCode, String issueCode);

    /**
     * 批量重置老足彩彩期缓存
     *
     * @param lotteryCode 彩种编号
     * @param issueCodes  彩期集合
     */
    void resetOldMatch(Integer lotteryCode, List<String> issueCodes);
}
