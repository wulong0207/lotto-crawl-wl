package com.hhly.crawlcore.database.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.base.zeroc.ConnectionParam;
import com.hhly.crawlcore.base.zeroc.IceClient;
import com.hhly.crawlcore.database.service.YbfFootBallAnalysisService;
import com.hhly.datacenter.zreocybf.ZlkMobileDataServicePrxHelper;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.ybf.bo.AnalysisEntity;
import com.hhly.skeleton.lotto.base.ybf.bo.FootBallFutureMatchBO;
import com.hhly.skeleton.lotto.base.ybf.bo.MatchAns;
import com.hhly.skeleton.lotto.base.ybf.bo.OddsInfo;
import com.hhly.skeleton.lotto.base.ybf.vo.AnalysisVO;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/11/1.
 * @company 益彩网络科技有限公司
 */
@Service
public class YbfFootBallAnalysisServiceImpl implements YbfFootBallAnalysisService {

    private static final String key = "ice-ybf-database-mobile";
    private static Logger logger = LoggerFactory.getLogger(YbfFootBallAnalysisService.class);
    private static IceClient iceClient = new IceClient();
    private static ZlkMobileDataServicePrxHelper zlkMobileDataService;

    private static boolean init = false;

    @Autowired
    @Qualifier("dataConnectionParam")
    private ConnectionParam connectionParam;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据对阵id获取分析数据
     *
     * @param vo
     */
    @Override
    public AnalysisEntity footBallAnalysis(AnalysisVO vo) {
        String result = null;
        AnalysisEntity analysisEntity = new AnalysisEntity();
        if (ObjectUtil.isBlank(vo.getMatchId()) || ObjectUtil.isBlank(vo.getLeagueId())) {
            return analysisEntity;
        }
        if (initProxy()) {
            result = zlkMobileDataService.getZlkAnalysis(vo.getLang(), vo.getLeagueId(), vo.getMatchId());
            if (!StringUtil.isBlank(result)) {
                analysisEntity = JsonUtil.jsonToObject(result, AnalysisEntity.class);
                //redisUtil.addObj(CacheConstants.SPORT_YBF_MATCH_ANALYSIS,analysisEntity,CacheConstants.ONE_DAY);
            }
        }
        return analysisEntity;
    }

    /**
     * 主客队对阵历史
     *
     * @return
     */
    @Override
    public List<MatchAns> footBallMatchHistory(AnalysisVO vo) {
        AnalysisEntity analysisEntity = footBallAnalysis(vo);
        if (!ObjectUtil.isBlank(vo.getHomeId())) {
            return analysisEntity.getHomeNear();
        }
        return analysisEntity.getGuestNear();
    }


    /**
     * 主客队交战历史
     *
     * @return
     */
    @Override
    public List<List<OddsInfo>> footBallMatchWarHistory(AnalysisVO vo) {
        AnalysisEntity analysisEntity = footBallAnalysis(vo);
        return analysisEntity.getHisMatch();
    }

    /**
     * 查询主客队未来赛事
     *
     * @param vo
     * @return
     */
    @Override
    public Map<String, List<FootBallFutureMatchBO>> footBallFutureMatch(AnalysisVO vo) {
        AnalysisEntity analysisEntity = footBallAnalysis(vo);
        Map<String, List<FootBallFutureMatchBO>> resultMap = new HashMap<>();
        resultMap.put("home", getFootBallFutureMatchCount(analysisEntity.getHomeFuture()));
        resultMap.put("guest", getFootBallFutureMatchCount(analysisEntity.getGuestFuture()));
        return resultMap;
    }

    /**
     * 比赛时间按日期分组
     *
     * @param list
     * @return
     */
    private List<FootBallFutureMatchBO> getFootBallFutureMatchCount(List<MatchAns> list) {
        Map<String, List<MatchAns>> tempMap = new LinkedHashMap<>();
        List<FootBallFutureMatchBO> resultList = new ArrayList<>();
        for (MatchAns matchAns : list) {

            String date = DateUtil.convertDateToStr(DateUtil.convertStrToDate(matchAns.getMatchTime(), DateUtil.DATE_FORMAT), DateUtil.FORMAT_CHINESE_YYYYMM);
            if (tempMap.containsKey(date)) {
                tempMap.get(date).add(matchAns);
            } else {
                List<MatchAns> temp = new ArrayList<>();
                temp.add(matchAns);
                tempMap.put(date, temp);
            }
        }

        for (Map.Entry<String, List<MatchAns>> entry : tempMap.entrySet()) {
            FootBallFutureMatchBO bo = new FootBallFutureMatchBO();
            bo.setDate(entry.getKey());
            bo.setList(entry.getValue());
            resultList.add(bo);
        }

        return resultList;
    }

    /**
     * 初始化代理
     *
     * @return
     */
    public boolean initProxy() {
        if (init) {
            return true;
        }
        try {
            iceClient.initClientPrxs(connectionParam);
            zlkMobileDataService = (ZlkMobileDataServicePrxHelper) iceClient.getPrxByKey(key);
            init = true;
            if (zlkMobileDataService == null) {
                init = false;
                logger.info("init proxy " + key + " return null!");
            }
        } catch (Exception e) {
            init = false;
            logger.error("ice param init error:" + e.getClass().getName());
        }
        return init;
    }
}
