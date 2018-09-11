package com.hhly.crawlcore.sport.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.crawlcore.base.controller.BaseController;
import com.hhly.crawlcore.sport.service.ResetSportCacheService;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.util.JsonUtil;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/5/24.
 * @company 益彩网络科技有限公司
 */
@Deprecated
@RestController
@RequestMapping("/resetCache")
public class ResetCacheController extends BaseController {

    @Autowired
    private ResetSportCacheService resetSportCacheService;


    /**
     * 重置足球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/football")
    public Object football() {
        resetSportCacheService.resetFbMatchList();
        return ResultBO.ok();
    }

    /**
     * 根据系统编号重置足球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/football/{systemCode}")
    public Object footballBySystemCode(@PathVariable("systemCode") String systemCode) {
        resetSportCacheService.resetFbMatchList();
        resetSportCacheService.resetFbMatchListBySystemCode(systemCode);
        return ResultBO.ok();
    }

    /**
     * 根据系统编号重置足球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/football/systemCodes")
    public Object footballBySystemCodes(String systemCodes) {

        resetSportCacheService.resetFbMatchList();
        resetSportCacheService.resetFbMatchListBySystemCodes(JsonUtil.jsonToArray(systemCodes, String.class));
        return ResultBO.ok();
    }

    /**
     * 根据彩期重置足球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/football/issueCode/{issueCode}")
    public Object footballByIssueCode(@PathVariable("issueCode") String issueCode) {
        resetSportCacheService.resetFbMatchListByIssueCode(issueCode);
        return ResultBO.ok();
    }

    /**
     * 根据彩期集合编号重置足球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/football/issueCodes")
    public Object footballByIssueCodes(String issueCodes) {
        resetSportCacheService.resetFbMatchListByIssueCodes(JsonUtil.jsonToArray(issueCodes, String.class));
        return ResultBO.ok();
    }

    /**
     * 根据对阵ID重置足球历史sp值
     *
     * @return
     */
    @RequestMapping(value = "/football/history/{id}")
    public Object footballHistorySpById(@PathVariable("id") Long id) {
        resetSportCacheService.resetFbMatchWdfHistorySpById(id, (short) 1);
        resetSportCacheService.resetFbMatchWdfHistorySpById(id, (short) 2);
        return ResultBO.ok();
    }

    /**
     * 根据对阵IDs重置足球历史sp值
     *
     * @return
     */
    @RequestMapping(value = "/football/history/ids")
    public Object footballHistorySpById(String ids) {
        resetSportCacheService.resetFbMatchWdfHistorySpByIds(JsonUtil.jsonToArray(ids, Long.class));
        return ResultBO.ok();
    }

    /**
     * 重置篮球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/basketball")
    public Object basketball() {
        resetSportCacheService.resetBBMatchList();
        return ResultBO.ok();
    }

    /**
     * 根据系统编号重置篮球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/basketball/{systemCode}")
    public Object basketballBySystemCode(@PathVariable("systemCode") String systemCode) {
        resetSportCacheService.resetBBMatchList();
        resetSportCacheService.resetBBMatchListBySystemCode(systemCode);
        return ResultBO.ok();
    }

    /**
     * 根据系统编号重置足球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/basketball/systemCodes")
    public Object basketballBySystemCodes(String systemCodes) {
        resetSportCacheService.resetBBMatchList();
        resetSportCacheService.resetBBMatchListBySystemCodes(JsonUtil.jsonToArray(systemCodes, String.class));
        return ResultBO.ok();
    }


    /**
     * 根据彩期重置足球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/basketball/issueCode/{issueCode}")
    public Object basketballByIssueCode(@PathVariable("issueCode") String issueCode) {
        resetSportCacheService.resetBBMatchList();
        resetSportCacheService.resetBBMatchListByIssueCode(issueCode);
        return ResultBO.ok();
    }

    /**
     * 根据彩期集合编号重置足球比赛缓存
     *
     * @return
     */
    @RequestMapping(value = "/basketball/issueCodes")
    public Object basketballByIssueCodes(String issueCodes) {
        resetSportCacheService.resetBBMatchList();
        resetSportCacheService.resetBBMatchListByIssueCodes(JsonUtil.jsonToArray(issueCodes, String.class));
        return ResultBO.ok();
    }

    /**
     * 根据对阵ID重置竞彩篮球历史sp值
     *
     * @return
     */
    @RequestMapping(value = "/basketball/history/{id}")
    public Object basketballHistorySpById(@PathVariable("id") Long id) {
        resetSportCacheService.resetBBMatchWfHistorySpByIds(id);
        resetSportCacheService.resetBBMatchBsHistorySpById(id);
        resetSportCacheService.resetBBMatchWsHistorySpById(id);
        return ResultBO.ok();
    }

    /**
     * 根据对阵ID重置竞彩篮球历史sp值
     *
     * @return
     */
    @RequestMapping(value = "/basketball/history/ids")
    public Object basketballHistorySpById(String ids) {
        resetSportCacheService.resetBBMatchWfHistorySpByIds(JsonUtil.jsonToArray(ids, Long.class));
        resetSportCacheService.resetBBMatchBsHistorySpByIds(JsonUtil.jsonToArray(ids, Long.class));
        resetSportCacheService.resetBBMatchWsHistorySpByIds(JsonUtil.jsonToArray(ids, Long.class));
        return ResultBO.ok();
    }


    /**
     * 删除老足彩的对阵缓存
     *
     * @return
     */
    @RequestMapping(value = "/old")
    public Object old(@RequestParam Integer lotteryCode,
                      @RequestParam String issueCode) {
        resetSportCacheService.resetOldMatch(lotteryCode, issueCode);
        return ResultBO.ok();
    }

    /**
     * 批量删除老足彩缓存
     *
     * @return
     */
    @RequestMapping(value = "/old/issueCodes")
    public Object old(@RequestParam Integer lotteryCode,
                      @RequestBody List<String> issueCodes) {
        resetSportCacheService.resetOldMatch(lotteryCode, issueCodes);
        return ResultBO.ok();
    }
}
