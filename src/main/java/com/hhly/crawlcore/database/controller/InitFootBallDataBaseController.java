package com.hhly.crawlcore.database.controller;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.database.service.FbScheduleService;
import com.hhly.crawlcore.database.service.YbfFootBallOddsService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/11/20.
 * @company 益彩网络科技有限公司
 */
@RestController
@RequestMapping("/football-init")
public class InitFootBallDataBaseController extends BaseController {


    @Autowired
    private FbScheduleService fbScheduleService;

    @Autowired
    private YbfFootBallOddsService ybfFootBallOddsService;

    @RequestMapping("/schedule")
    public Object initFbSchedule() throws IOException, URISyntaxException {
        return ResultBO.ok(fbScheduleService.initFbSchedule());
    }

    @RequestMapping("/odds")
    public Object initOdds(String matchId) throws IOException, URISyntaxException {
        return ResultBO.ok(ybfFootBallOddsService.saveOddsFbEuropeOddsPOs(matchId));
    }

}
