package com.hhly.crawlcore.database.service;

import com.hhly.skeleton.lotto.base.ybf.bo.FootBallMatchAnsBO;
import com.hhly.skeleton.lotto.base.ybf.bo.FootBallMatchCount;
import com.hhly.skeleton.lotto.base.ybf.bo.FootBallWarCountBO;
import com.hhly.skeleton.lotto.base.ybf.vo.AnalysisVO;

import java.util.List;
import java.util.Map;

/**
 * @author lgs on
 * @version 1.0
 * @desc 统计一比分分析数据
 * @date 2017/11/3.
 * @company 益彩网络科技有限公司
 */
public interface YbfFootBallCountAnalysisService {

    /**
     * 战绩统计
     *
     * @param vo
     * @return
     */
    FootBallWarCountBO getFootBallWarCountBO(AnalysisVO vo);

    /**
     * 统计近期战绩
     *
     * @param vo
     * @return
     */
    Map<String, List<Integer>> FootBallRecentRecordCount(AnalysisVO vo);

    /**
     * 获取球队近期交战战绩
     *
     * @param vo
     * @return
     */
    List<FootBallMatchAnsBO> getFootBallMatchAnsBO(AnalysisVO vo);


    /**
     * 获取盘路统计结果
     *
     * @param list
     * @return
     */
    Map<String, Integer> getMatchLetCount(List<FootBallMatchAnsBO> list);


    /**
     * 获取盘路统计结果
     *
     * @param vo
     * @return
     */
    Map<String, Object> getMatchRankCount(AnalysisVO vo);


    /**
     * 统计结果
     getFootBallMatchAnsBO
     * @param vo
     * @return
     */
    Map<String, Object> getMatchResultCount(AnalysisVO vo);

    /**
     * 足球球队最近对阵信息
     *
     * @param vo
     * @return
     */
    FootBallMatchCount getFootBallMatchCount(AnalysisVO vo);


}
