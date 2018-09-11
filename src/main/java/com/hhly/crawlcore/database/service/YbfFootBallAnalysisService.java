package com.hhly.crawlcore.database.service;

import com.hhly.skeleton.lotto.base.ybf.bo.AnalysisEntity;
import com.hhly.skeleton.lotto.base.ybf.bo.FootBallFutureMatchBO;
import com.hhly.skeleton.lotto.base.ybf.bo.MatchAns;
import com.hhly.skeleton.lotto.base.ybf.bo.OddsInfo;
import com.hhly.skeleton.lotto.base.ybf.vo.AnalysisVO;

import java.util.List;
import java.util.Map;

/**
 * @author lgs on
 * @version 1.0
 * @desc 一比分足球分析
 * @date 2017/11/1.
 * @company 益彩网络科技有限公司
 */
public interface YbfFootBallAnalysisService {

    /**
     * 根据对阵id获取分析数据
     *
     * @param vo
     */
    AnalysisEntity footBallAnalysis(AnalysisVO vo);

    /**
     * 主客队对阵历史
     *
     * @param vo
     * @return
     */
    List<MatchAns> footBallMatchHistory(AnalysisVO vo);

    /**
     * 主客队交战历史
     *
     * @param vo
     * @return
     */
    List<List<OddsInfo>> footBallMatchWarHistory(AnalysisVO vo);

    /**
     * 查询主客队未来赛事
     *
     * @param vo
     * @return
     */
    Map<String, List<FootBallFutureMatchBO>> footBallFutureMatch(AnalysisVO vo);


}
