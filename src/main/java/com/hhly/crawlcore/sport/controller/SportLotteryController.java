package com.hhly.crawlcore.sport.controller;


import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.sport.service.ClimbTeamInfoService;
import com.hhly.crawlcore.sport.service.OldFootBallDataService;
import com.hhly.crawlcore.sport.service.ResetSportCacheService;
import com.hhly.crawlcore.sport.service.SportBaskBallService;
import com.hhly.crawlcore.sport.service.SportFootballService;
import com.hhly.crawlcore.sport.service.YbfImmediateService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.LotteryEnum;

/**
 * Created by lgs on 2017/1/11.
 * 竞彩足球抓取
 */
@RestController
@RequestMapping(value = "/sport/lottery")
public class SportLotteryController extends BaseController {

    public static final Logger LOGGER = LoggerFactory.getLogger(SportLotteryController.class);

    @Autowired
    private SportFootballService footballService;

    @Autowired
    private SportBaskBallService baskBallService;

    @Autowired
    private OldFootBallDataService oldFootBallDataService;

    @Autowired
    private ResetSportCacheService resetSportCacheService;

    @Resource
    private ClimbTeamInfoService climbTeamInfoService;

    @Resource
    private YbfImmediateService ybfImmediateService;


    /**
     * 获取500网足球队信息
     */
    @RequestMapping(value = "/climbTeamInfo")
    public Object climbTeamInfo() throws Exception {
        climbTeamInfoService.climbTeamInfo();
        return ResultBO.ok();
    }

    /**
     * 获取500网篮球球队信息
     */
    @RequestMapping(value = "/climbTeamInfoBB")
    public Object climbTeamInfoBB() throws Exception {
        climbTeamInfoService.climbTeamInfoBB();
        return ResultBO.ok();
    }


    /**
     * 一比分竞彩足球即时比分
     * @throws Exception 异常
     */
    @RequestMapping(value = "/processImmediateScore")
    public Object processImmediateScore() throws Exception {
        ybfImmediateService.processImmediateScore();
        return ResultBO.ok();
        
    }

    /**
     * 一比分竞篮即时比分数据，未入库
     *
     * @throws Exception 异常
     */
    @RequestMapping(value = "/processBasketScore")
    public Object processBasketScore() throws Exception {
        ybfImmediateService.processBasketScore();
        return ResultBO.ok();
    }

    /**
     * http://live.13322.com/score.refreshState.do
     * 刷新比赛数据
     */
    @RequestMapping(value = "/refreshState")
    public Object refreshState() throws Exception {
        ybfImmediateService.refreshState();
        return ResultBO.ok();
    }
    /**
     * 抓取暂定赛程
     *
     * @return 是否成功
     */
    @RequestMapping(value = "/footBallMatchInterim")
    public Object footBallMatchInterim() {
        LOGGER.info("更新暂定赛程开始");
        ThreadPoolManager.execute("更新暂定赛程开始", new Runnable() {
            @Override
            public void run() {
                footballService.saveMatchInterim();
            }
        },true);
        LOGGER.info("更新暂定赛程结束");
        return ResultBO.ok();
    }

    /**
     * 抓取受注赛程
     *
     * @return 是否成功
     */
    @RequestMapping(value = "/footBallMatchList")
    public Object footBallMatchList() {
        ThreadPoolManager.execute("抓取受注赛程", new Runnable() {
            @Override
            public void run() {
                footballService.saveWxMatchList();
                footballService.saveMatchList();
                //更新天气, 现在不需要了
//                footballService.saveMatchInfo();
                try {
                    footballService.saveWxMatchOddList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    footballService.savePoolResultList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                footballService.updateYbfData();
                resetSportCacheService.resetFbMatchList();
                footballService.clearJczqCacheBySystemCode();
                footballService.clearJczqCacheByIssueCode();
            }
        },true);

        LOGGER.info("更新受注赛程结束");
        return ResultBO.ok();
    }

    /**
     * 抓取对阵官方id各项SP值
     *
     * @return 是否成功
     */
    @RequestMapping(value = "/poolResult/{officialId}/{sportAgainstInfoId}")
    public Object poolResult(@PathVariable("officialId") final String officialId,
                             @PathVariable("sportAgainstInfoId") final Long sportAgainstInfoId) {
        String info = "更新指定赛程SP值开始officialId:" + officialId + "sportAgainstInfoId" + sportAgainstInfoId;
        LOGGER.info(info);
        ThreadPoolManager.execute(info, new Runnable() {
            @Override
            public void run() {
                footballService.savePoolResult(officialId, sportAgainstInfoId);
            }
        },true);
        LOGGER.info("更新指定赛程SP值结束");
        return ResultBO.ok();
    }

    /**
     * 抓取所有已经开售对阵各项SP值
     *
     * @return 是否成功
     */
    @RequestMapping(value = "/poolResultList")
    public Object poolResultList() {
        LOGGER.info("更新所有赛程SP值开始");
        ThreadPoolManager.execute("更新所有赛程SP值", new Runnable() {
            @Override
            public void run() {
                try {
                    footballService.savePoolResultList();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        },true);
        LOGGER.info("更新所有赛程SP值结束");
        return ResultBO.ok();
    }

    /**
     * 抓取所有已经开售对阵各项SP值
     *
     * @return 是否成功
     */
    @RequestMapping(value = "/getWxMatchOddList")
    public Object getWxMatchOdd() {
        LOGGER.info("更新所有赛程SP值开始");
        ThreadPoolManager.execute("更新所有赛程SP", new Runnable() {
            @Override
            public void run() {
                try {
                    footballService.saveWxMatchOddList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resetSportCacheService.resetFbMatchList();
                footballService.clearJczqCacheBySystemCode();
                footballService.clearJczqCacheByIssueCode();
            }
        },true);
        LOGGER.info("更新所有赛程SP值结束");

        return ResultBO.ok();
    }

    /**
     * 更新篮球暂定赛程
     *
     * @return
     */
    @RequestMapping(value = "/getBaskBallMatchInterim")
    public Object getBaskBallMatchInterim() {
        LOGGER.info("更新篮球暂定赛程Begin");
        ThreadPoolManager.execute("更新篮球暂定赛程", new Runnable() {
            @Override
            public void run() {
                baskBallService.saveMatchInterim();
            }
        },true);
        LOGGER.info("更新篮球暂定赛程End");
        return ResultBO.ok();
    }

    /**
     * 更新篮球微信受注赛程
     *
     * @return
     */
    @RequestMapping(value = "/getBaskBallWxMatchList")
    public Object getBaskBallWxMatchList() {
        LOGGER.info("更新篮球微信对阵Begin");
        ThreadPoolManager.execute("更新篮球微信对阵", new Runnable() {
            @Override
            public void run() {
                baskBallService.saveWxMatchList();//处理竞篮对阵信息
//                baskBallService.saveMatchList();//通过受注页面更新子玩法状态、备注等值
//                baskBallService.saveBkMatchOddList();//处理所有
//                try {
                baskBallService.proceeSportBBData();
                baskBallService.saveBkMatchMnlList();//处理SP值
//                } catch (Exception e) {
//
//                }

                baskBallService.updateBasketBallForYbf();//同步一比分数据
                resetSportCacheService.resetBBMatchList();
                baskBallService.clearJclqCacheByIssueCode();
                baskBallService.clearJclqCacheBySystemCode();

            }
        },true);

        LOGGER.info("更新篮球微信对阵End");
        return ResultBO.ok();
    }


    /**
     * 更新篮球最新赔率以及赛事状态
     *
     * @return
     */
    @RequestMapping(value = "/getBaskBallMatchOddList")
    public Object getBaskBallMatchOddList() {
        LOGGER.info("更新篮球最新赔率以及赛事状态Begin");
        ThreadPoolManager.execute("更新篮球最新赔率以及赛事状态", new Runnable() {
            @Override
            public void run() {
                baskBallService.saveBkMatchOddList();
                resetSportCacheService.resetBBMatchList();
                baskBallService.clearJclqCacheByIssueCode();
                baskBallService.clearJclqCacheBySystemCode();
            }
        },true);

        LOGGER.info("更新篮球最新赔率以及赛事状态End");
        return ResultBO.ok();
    }

    /**
     * 更新篮球赛事对阵赔率历史
     *
     * @return
     */
    @RequestMapping(value = "/getBaskBallMatchMnlList")
    public Object getBaskBallMatchMnlList() {
        LOGGER.info("更新篮球赛事对阵赔率历史Begin");
        ThreadPoolManager.execute("更新篮球赛事对阵赔率历史", new Runnable() {
            @Override
            public void run() {
                baskBallService.saveBkMatchMnlList();
            }
        },true);
        LOGGER.info("更新篮球赛事对阵赔率历史阵End");
        return ResultBO.ok();
    }


    /**
     * 更新老足彩胜负彩当前期的数据
     *
     * @return
     */
    @RequestMapping(value = "/getOldSfcMatch")
    public Object getOldSfcMatch(@RequestParam(value = "issueCode") final String issueCode) {
        LOGGER.info("更新老足彩胜负彩当前期的数据Begin");
        ThreadPoolManager.execute("更新老足彩胜负彩当前期的数据", new Runnable() {
            @Override
            public void run() {
                oldFootBallDataService.saveSfcMatch(issueCode, LotteryEnum.Lottery.SFC.getName());
                oldFootBallDataService.saveSixHFMatch(issueCode, LotteryEnum.Lottery.ZC6.getName());
                oldFootBallDataService.saveGoalMatch(issueCode, LotteryEnum.Lottery.JQ4.getName());
            }
        },true);
        LOGGER.info("更新老足彩胜负彩当前期的数据End");
        return ResultBO.ok();
    }


    /**
     * 同步一比分数据
     *
     * @return
     */
    @RequestMapping(value = "/synYbfData")
    public Object synYbfData() {
        LOGGER.info("同步一比分数据Begin");
        ThreadPoolManager.execute("同步一比分数据", new Runnable() {
            @Override
            public void run() {
                footballService.updateYbfData();

                resetSportCacheService.resetFbMatchList();
                footballService.clearJczqCacheByIssueCode();
                footballService.clearJczqCacheBySystemCode();
                resetSportCacheService.resetBBMatchList();
                baskBallService.clearJclqCacheByIssueCode();
                baskBallService.clearJclqCacheBySystemCode();
                oldFootBallDataService.updateOldMatchInfo();
            }
        },true);
        LOGGER.info("同步一比分数据End");
        return ResultBO.ok();
    }

    /**
     * 获取老足彩彩期信息
     *
     * @param issueCode
     * @param lotteryCode
     * @return
     */
    @RequestMapping(value = "/getOldIssueInfo")
    private Object getOldIssueInfo(String issueCode, Integer lotteryCode) {
        return oldFootBallDataService.getNextIssueInfo(issueCode, lotteryCode);
    }
}
