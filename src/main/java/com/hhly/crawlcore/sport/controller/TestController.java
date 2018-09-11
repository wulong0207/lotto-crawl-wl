package com.hhly.crawlcore.sport.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.database.service.YbfFootBallAnalysisService;
import com.hhly.crawlcore.database.service.YbfFootBallCountAnalysisService;
import com.hhly.crawlcore.rabbitmq.provider.impl.OrderOverdueTimeProvider;
import com.hhly.crawlcore.sport.service.SportBaskBallService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.MQConstants;
import com.hhly.skeleton.base.mq.OrderOverdueMsgModel;
import com.hhly.skeleton.lotto.base.ybf.bo.FootBallMatchAnsBO;
import com.hhly.skeleton.lotto.base.ybf.vo.AnalysisVO;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/6/14.
 * @company 益彩网络科技有限公司
 */
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    private OrderOverdueTimeProvider orderOverdueTimeProvider;

    @Autowired
    private SportBaskBallService baskBallService;

    @Autowired
    private YbfFootBallAnalysisService ybfFootBallAnalysisService;

    @Autowired
    private YbfFootBallCountAnalysisService ybfFootBallCountAnalysisService;

    @Resource
    private RedisUtil redisUtil;

    @RequestMapping("/1")
    public Object test1() {
        OrderOverdueMsgModel model = new OrderOverdueMsgModel();
        model.setIssueCode("170606");
        model.setLotteryCode(300);
        orderOverdueTimeProvider.sendMessage(MQConstants.ORDER_OVERDUE_TIME_QUEUENAME, model);
        return ResultBO.ok();
    }


    @RequestMapping("/3")
    public Object test3() {
        AnalysisVO vo = new AnalysisVO();
        vo.setMatchId(848886324);
        vo.setLeagueId(36);
        vo.setLang("zh");
        ybfFootBallAnalysisService.footBallAnalysis(vo);
        return ResultBO.ok();
    }


    @RequestMapping("/4")
    public Object test4() {
        AnalysisVO vo = new AnalysisVO();
        vo.setMatchId(848886324);
        vo.setLeagueId(36);
        vo.setLang("zh");
        vo.setTeamFlag(1);
        vo.setHomeId(58);
        Map<String, Object> result = new HashMap<>();
        List<FootBallMatchAnsBO> bos = ybfFootBallCountAnalysisService.getFootBallMatchAnsBO(vo);
        result.put("matchList", bos);
        result.put("record", ybfFootBallCountAnalysisService.getMatchLetCount(bos));
        return ResultBO.ok(result);
    }

    @RequestMapping("/5")
    public Object test5() {
        AnalysisVO vo = new AnalysisVO();
        vo.setMatchId(848886324);
        vo.setLeagueId(36);
        vo.setLang("zh");
        vo.setTeamFlag(1);
        vo.setHomeId(58);


        return ResultBO.ok(ybfFootBallCountAnalysisService.getMatchRankCount(vo));
    }

    @RequestMapping("/6")
    public Object test6() {
        AnalysisVO vo = new AnalysisVO();
        vo.setMatchId(848887682);
        vo.setLeagueId(4);
        vo.setLang("zh");
        vo.setTeamFlag(2);
        vo.setHomeId(4176);
        vo.setGuestId(467);
        return ResultBO.ok(ybfFootBallCountAnalysisService.getMatchResultCount(vo));
    }

    @RequestMapping("/7")
    public Object test7() {
        AnalysisVO vo = new AnalysisVO();
        vo.setMatchId(848887682);
        vo.setLeagueId(4);
        vo.setLang("zh");
        vo.setTeamFlag(2);
        vo.setHomeId(4176);
        vo.setGuestId(467);
        return ResultBO.ok(ybfFootBallCountAnalysisService.FootBallRecentRecordCount(vo));
    }

    @RequestMapping("/8")
    public Object test8() {
        AnalysisVO vo = new AnalysisVO();
        vo.setMatchId(848887682);
        vo.setLeagueId(4);
        vo.setLang("zh");
        vo.setTeamFlag(2);
        vo.setHomeId(4176);
        vo.setGuestId(467);
        return ResultBO.ok(ybfFootBallAnalysisService.footBallFutureMatch(vo));
    }

}
