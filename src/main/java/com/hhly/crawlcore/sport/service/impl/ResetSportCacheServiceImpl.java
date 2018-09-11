package com.hhly.crawlcore.sport.service.impl;

import static com.hhly.skeleton.base.constants.CacheConstants.S_COMM_SPORT_BB_MATCH_LIST;
import static com.hhly.skeleton.base.constants.CacheConstants.S_COMM_SPORT_FB_MATCH_LIST;
import static com.hhly.skeleton.base.constants.CacheConstants.S_COMM_SPORT_FB_MATCH_LIST_SALE_END;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportBBBSWsHistorySpCacheKey;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportBbIssueCodeMatchListCacheKey;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportBbSystemCodeMatchListCacheKey;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportBbWfHistorySpCacheKey;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportBbWsHistorySpCacheKey;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportFbMatchHistorySpCacheKey;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportFbMatchListByIssueCodeCacheKey;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportFbSystemCodeMatchListCacheKey;
import static com.hhly.skeleton.base.constants.CacheConstants.getSportOldMatchListCacheKey;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.sport.service.ResetSportCacheService;
import com.hhly.skeleton.base.constants.CacheConstants;

/**
 * @author lgs on
 * @version 1.0
 * @desc 重置竞技彩抓取数据缓存实现类
 * @date 2017/5/21.
 * @company 益彩网络科技有限公司
 */
@Service
public class ResetSportCacheServiceImpl implements ResetSportCacheService {

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 重置竞彩足球受注赛程缓存
     */
    @Override
    public void resetFbMatchList() {
        redisUtil.delAllObj(S_COMM_SPORT_FB_MATCH_LIST);
    }

    /**
     * 重置竞彩足球销售截止缓存
     */
    @Deprecated
    @Override
    public void resetFbMatchSaleEndList() {
        redisUtil.delObj(S_COMM_SPORT_FB_MATCH_LIST_SALE_END);
    }

    /**
     * 根据系统编号重置竞彩足球对阵缓存
     *
     * @param systemCode 系统编号
     */
    @Override
    public void resetFbMatchListBySystemCode(String systemCode) {
        String cacheKey = getSportFbSystemCodeMatchListCacheKey(systemCode);
        redisUtil.delObj(cacheKey);
        resetFbMatchList();
    }

    /**
     * 批量根据系统编号重置竞彩足球对阵缓存
     *
     * @param systemCodes 系统编号集合
     */
    @Override
    public void resetFbMatchListBySystemCodes(List<String> systemCodes) {
        for (String systemCode : systemCodes) {
            resetFbMatchListBySystemCode(systemCode);
        }
    }

    /**
     * 根据彩期重置竞彩足球对阵缓存
     *
     * @param issueCode
     */
    @Override
    public void resetFbMatchListByIssueCode(String issueCode) {
        String cacheKey = getSportFbMatchListByIssueCodeCacheKey(issueCode);
        redisUtil.delObj(cacheKey);
    }

    /**
     * 批量根据彩期重置竞彩足球对阵缓存
     *
     * @param issueCodes 彩期集合
     */
    @Override
    public void resetFbMatchListByIssueCodes(List<String> issueCodes) {
        for (String issueCode : issueCodes) {
            resetFbMatchListByIssueCode(issueCode);
        }
    }

    /**
     * 重置竞彩足球胜平负历史SP值缓存
     *
     * @param ids 对阵id集合
     */
    @Override
    public void resetFbMatchWdfHistorySpByIds(List<Long> ids) {
        for (Long id : ids) {
            resetFbMatchWdfHistorySpById(id, (short) 1); //非让球
            resetFbMatchWdfHistorySpById(id, (short) 2); //让球
        }
    }

    /**
     * 重置竞彩足球胜平负历史SP值缓存
     *
     * @param id 对阵id
     */
    @Override
    public void resetFbMatchWdfHistorySpById(Long id, Short type) {
        String cacheKey = getSportFbMatchHistorySpCacheKey(id, type);
        redisUtil.delObj(cacheKey);
    }

    /**
     * 重置竞彩篮球受注赛程缓存
     */
    @Override
    public void resetBBMatchList() {
    	//清除对阵的
        redisUtil.delAllObj(S_COMM_SPORT_BB_MATCH_LIST);
        //清除大小分sp值缓存
        redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_BB_SSS_HISTORY_SP);
        //清除胜分差sp值缓存
        redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_BB_WS_HISTORY_SP);
        //清除胜负sp值缓存
        redisUtil.delAllObj(CacheConstants.S_COMM_SPORT_BB_WF_HISTORY_SP);
    }

    /**
     * 根据系统编号重置竞彩篮球对阵缓存
     *
     * @param systemCode 系统编号
     */
    @Override
    public void resetBBMatchListBySystemCode(String systemCode) {
        String cacheKey = getSportBbSystemCodeMatchListCacheKey(systemCode);
        redisUtil.delObj(cacheKey);
        resetBBMatchList();
    }

    /**
     * 批量根据系统编号重置竞彩篮球对阵缓存
     *
     * @param systemCodes 系统编号集合
     */
    @Override
    public void resetBBMatchListBySystemCodes(List<String> systemCodes) {
        for (String systemCode : systemCodes) {
            resetBBMatchListBySystemCode(systemCode);
        }
    }

    /**
     * 根据彩期重置竞彩篮球对阵缓存
     *
     * @param issueCode
     */
    @Override
    public void resetBBMatchListByIssueCode(String issueCode) {
        String cacheKey = getSportBbIssueCodeMatchListCacheKey(issueCode);
        redisUtil.delObj(cacheKey);
    }

    /**
     * 批量根据彩期重置竞彩篮球对阵缓存
     *
     * @param issueCodes 彩期集合
     */
    @Override
    public void resetBBMatchListByIssueCodes(List<String> issueCodes) {
        for (String issueCode : issueCodes) {
            resetBBMatchListByIssueCode(issueCode);
        }
    }

    /**
     * 重置竞彩篮球胜平负历史SP值缓存
     *
     * @param ids 对阵id集合
     */
    @Override
    public void resetBBMatchWfHistorySpByIds(List<Long> ids) {
        for (Long id : ids) {
            resetBBMatchWfHistorySpByIds(id);
        }
    }

    /**
     * 重置竞彩篮球胜平负历史SP值缓存
     *
     * @param id 对阵id
     */
    @Override
    public void resetBBMatchWfHistorySpByIds(Long id) {
        String cacheKey1 = getSportBbWfHistorySpCacheKey(id, (short) 1);//删除胜负
        String cacheKey2 = getSportBbWfHistorySpCacheKey(id, (short) 2);//删除让分胜负
        redisUtil.delObj(cacheKey1);
        redisUtil.delObj(cacheKey2);
    }

    /**
     * 重置竞彩篮球分差历史SP值缓存
     *
     * @param ids 对阵id集合
     */
    @Override
    public void resetBBMatchWsHistorySpByIds(List<Long> ids) {
        for (Long id : ids) {
            resetBBMatchWsHistorySpById(id);
        }
    }

    /**
     * 重置竞彩篮球分差历史SP值缓存
     *
     * @param id 对阵id
     */
    @Override
    public void resetBBMatchWsHistorySpById(Long id) {
        String cacheKey = getSportBbWsHistorySpCacheKey(id);
        redisUtil.delObj(cacheKey);
    }

    /**
     * 重置竞彩篮球大小分历史SP值缓存
     *
     * @param ids 对阵id集合
     */
    @Override
    public void resetBBMatchBsHistorySpByIds(List<Long> ids) {
        for (Long id : ids) {
            resetBBMatchBsHistorySpById(id);
        }
    }

    /**
     * 重置竞彩篮球大小分历史SP值缓存
     *
     * @param id 对阵id
     */
    @Override
    public void resetBBMatchBsHistorySpById(Long id) {
        String cacheKey = getSportBBBSWsHistorySpCacheKey(id);
        redisUtil.delObj(cacheKey);
    }

    /**
     * 重置老足彩彩期缓存
     *
     * @param lotteryCode 彩种编号
     * @param issueCode   彩期
     */
    @Override
    public void resetOldMatch(Integer lotteryCode, String issueCode) {
        String cacheKey = getSportOldMatchListCacheKey(lotteryCode, issueCode);
        redisUtil.delObj(cacheKey);
    }

    /**
     * 批量重置老足彩彩期缓存
     *
     * @param lotteryCode 彩种编号
     * @param issueCodes  彩期集合
     */
    @Override
    public void resetOldMatch(Integer lotteryCode, List<String> issueCodes) {
        for (String issueCode : issueCodes) {
            resetOldMatch(lotteryCode, issueCode);
        }
    }
}
