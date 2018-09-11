package com.hhly.crawlcore.database.controller;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.database.service.YbfFootBallAnalysisService;
import com.hhly.crawlcore.database.service.YbfFootBallCountAnalysisService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.NumberUtil;
import com.hhly.skeleton.lotto.base.ybf.bo.FootBallMatchAnsBO;
import com.hhly.skeleton.lotto.base.ybf.vo.AnalysisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/11/9.
 * @company 益彩网络科技有限公司
 */
@RestController
@RequestMapping("/football-analysis")
public class FootBallAnalysisController extends BaseController {

    @Autowired
    private YbfFootBallCountAnalysisService ybfFootBallCountAnalysisService;

    @Autowired
    private YbfFootBallAnalysisService ybfFootBallAnalysisService;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 双方交战战绩统计
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/war", method = RequestMethod.GET)
    public String getFootBallWarCountBO(AnalysisVO vo) {
        String result = JsonUtil.objectToJcakJson(ResultBO.ok(ybfFootBallCountAnalysisService.getFootBallWarCountBO(vo)));
        return result;
    }

    /**
     * 统计近期战绩
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/recent-record", method = RequestMethod.GET)
    public String FootBallRecentRecordCount(AnalysisVO vo) {
        Map<String, List<Integer>> listMap = ybfFootBallCountAnalysisService.FootBallRecentRecordCount(vo);
        int homeWinTotal = setRate(listMap.get("home"));
        int guestWinTotal = setRate(listMap.get("guest"));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("homeWinRate", NumberUtil.div(homeWinTotal, listMap.get("home").size(), 2) * 100);
        resultMap.put("guestWinRate", NumberUtil.div(guestWinTotal, listMap.get("guest").size(), 2) * 100);
        resultMap.put("list", listMap);
        String result = JsonUtil.objectToJcakJson(ResultBO.ok(resultMap));
        return result;
    }

    /**
     * 设置胜率负率
     *
     * @param list
     */
    private int setRate(List<Integer> list) {
        int winTotal = 0;
        for (Integer temp : list) {
            if (temp == 0) {
                winTotal++;
            }
        }
        return winTotal;
    }

    /**
     * 球队历史对阵
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/history-match", method = RequestMethod.GET)
    public String getFootBallMatchAnsBO(AnalysisVO vo) {
        Map<String, Object> result = new HashMap<>();
        List<FootBallMatchAnsBO> bos = ybfFootBallCountAnalysisService.getFootBallMatchAnsBO(vo);
        result.put("matchList", bos);
        result.put("record", ybfFootBallCountAnalysisService.getMatchLetCount(bos));
        String resultJson = JsonUtil.objectToJcakJson(ResultBO.ok(result));
        return resultJson;
    }


    /**
     * 获取球队双方排名
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/match-rank", method = RequestMethod.GET)
    public String getMatchRankCount(AnalysisVO vo) {
        String resultJson = JsonUtil.objectToJcakJson(ResultBO.ok(ybfFootBallCountAnalysisService.getMatchRankCount(vo)));
        return resultJson;
    }

    /**
     * 获取赛果统计
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/result-count", method = RequestMethod.GET)
    public String getMatchResultCount(AnalysisVO vo) {
        String resultJson = JsonUtil.objectToJcakJson(ResultBO.ok(ybfFootBallCountAnalysisService.getMatchResultCount(vo)));
        return resultJson;
    }


    /**
     * 获取未来赛事
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/future-match", method = RequestMethod.GET)
    public String getFootBallFutureMatch(AnalysisVO vo) {
        String resultJson = JsonUtil.objectToJcakJson(ResultBO.ok(ybfFootBallAnalysisService.footBallFutureMatch(vo)));
        return resultJson;
    }


    /**
     * 获取两队战绩
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/match-count", method = RequestMethod.GET)
    public String getFootBallMatchCount(AnalysisVO vo) {
        String resultJson = JsonUtil.objectToJcakJson(ResultBO.ok(ybfFootBallCountAnalysisService.getFootBallMatchCount(vo)));
        return resultJson;
    }

}
