package com.hhly.crawlcore.database.service.impl;

import com.hhly.crawlcore.database.service.YbfFootBallAnalysisService;
import com.hhly.crawlcore.database.service.YbfFootBallCountAnalysisService;
import com.hhly.skeleton.base.util.NumberUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.ybf.bo.*;
import com.hhly.skeleton.lotto.base.ybf.vo.AnalysisVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/11/3.
 * @company 益彩网络科技有限公司
 */
@Service
public class YbfFootBallCountAnalysisServiceImpl implements YbfFootBallCountAnalysisService {

    @Autowired
    private YbfFootBallAnalysisService ybfFootBallAnalysisService;

    /**
     * 战绩统计
     *
     * @param vo
     * @return
     */
    @Override
    public FootBallWarCountBO getFootBallWarCountBO(AnalysisVO vo) {
        List<List<OddsInfo>> list = ybfFootBallAnalysisService.footBallMatchWarHistory(vo);
        return getFootBallWarCountBO(list, vo);
    }

    private FootBallWarCountBO getFootBallWarCountBO(List<List<OddsInfo>> list, AnalysisVO vo) {
        FootBallWarCountBO footBallWarCountBO = new FootBallWarCountBO();
        int winNum = 0;
        int drawNum = 0;
        int lostNum = 0;
        //计数
        int i = 0;

        if (!CollectionUtils.isEmpty(list)) {
            if (vo.getSclassFlag() == 1) {
                for (List<OddsInfo> oddsInfos : list) {
                    OddsInfo oddsInfo = oddsInfos.get(0);
                    if (oddsInfo == null) {
                        continue;
                    }
                    if (!oddsInfo.getSclassId().equals(vo.getLeagueId())) {
                        continue;
                    }

                    if (vo.getTeamFlag() == 1) {
                        if (oddsInfo.getGuestTeamId().equals(vo.getHomeId())) {
                            continue;
                        }
                    } else if (vo.getTeamFlag() == 2) {
                        if (oddsInfo.getHomeTeamId().equals(vo.getGuestId())) {
                            continue;
                        }
                    }

                    switch (oddsInfo.getMatchResult()) {
                        case 0:
                            winNum++;
                            break;
                        case 1:
                            drawNum++;
                            break;
                        case 2:
                            lostNum++;
                            break;
                    }
                    i++;

                    if (i == vo.getCountNum()) {
                        break;
                    }


                    if (i == vo.getCountNum()) {
                        break;
                    }
                }
            } else {
                for (List<OddsInfo> oddsInfos : list) {
                    OddsInfo oddsInfo = oddsInfos.get(0);
                    if (vo.getTeamFlag() == 1) {
                        if (oddsInfo.getGuestTeamId().equals(vo.getHomeId())) {
                            continue;
                        }
                    } else if (vo.getTeamFlag() == 2) {
                        if (oddsInfo.getHomeTeamId().equals(vo.getGuestId())) {
                            continue;
                        }
                    }


                    switch (oddsInfo.getMatchResult()) {
                        case 0:
                            winNum++;
                            break;
                        case 1:
                            drawNum++;
                            break;
                        case 2:
                            lostNum++;
                            break;
                    }
                    i++;

                    if (i == vo.getCountNum()) {
                        break;
                    }


                    if (i == vo.getCountNum()) {
                        break;
                    }

                }

            }

        }
        footBallWarCountBO.setWinNum(winNum);
        footBallWarCountBO.setDrawNum(drawNum);
        footBallWarCountBO.setLostNum(lostNum);
        return footBallWarCountBO;
    }

    /**
     * 统计近期战绩
     *
     * @param vo
     * @return
     */
    @Override
    public Map<String, List<Integer>> FootBallRecentRecordCount(AnalysisVO vo) {
        Map<String, List<Integer>> resultMap = new HashMap<>();
        List<FootBallMatchAnsBO> homeMatchAns = getFootBallMatchAnsBO(vo);
        vo.setHomeId(null);
        List<FootBallMatchAnsBO> guestMatchAns = getFootBallMatchAnsBO(vo);

        List<Integer> home = getMatchRecordCount(homeMatchAns, vo);
        List<Integer> guest = getMatchRecordCount(guestMatchAns, vo);
        resultMap.put("home", home);
        resultMap.put("guest", guest);
        return resultMap;
    }

    /**
     * 获取球队近期交战战绩
     *
     * @param vo
     * @return
     */
    @Override
    public List<FootBallMatchAnsBO> getFootBallMatchAnsBO(AnalysisVO vo) {
        List<MatchAns> list = ybfFootBallAnalysisService.footBallMatchHistory(vo);
        List<FootBallMatchAnsBO> result = new ArrayList<>();
        Integer teamId = null;
        if (!ObjectUtil.isBlank(vo.getHomeId())) {
            teamId = vo.getHomeId();
        } else {
            teamId = vo.getGuestId();
        }
        if (list != null && !list.isEmpty()) {

            if (vo.getSclassFlag() == 1) {
                for (MatchAns matchAns : list) {
                    FootBallMatchAnsBO bo = new FootBallMatchAnsBO();
                    if (!matchAns.getSclassId().equals(vo.getLeagueId())) {
                        continue;
                    }
                    switch (vo.getTeamFlag()) {
                        case 1:
                            if (matchAns.getHomeTeamId().equals(teamId)) {
                                BeanUtils.copyProperties(matchAns, bo);
                                result.add(bo);
                            }
                            break;
                        case 2:
                            if (matchAns.getGuestTeamId().equals(teamId)) {
                                BeanUtils.copyProperties(matchAns, bo);
                                result.add(bo);
                            }
                            break;
                        case 0:
                        default:
                            BeanUtils.copyProperties(matchAns, bo);
                            result.add(bo);
                            break;
                    }

                    if (result.size() == vo.getCountNum()) {
                        break;
                    }
                }
            } else {
                for (MatchAns matchAns : list) {
                    FootBallMatchAnsBO bo = new FootBallMatchAnsBO();
                    switch (vo.getTeamFlag()) {
                        case 1:
                            if (matchAns.getHomeTeamId().equals(teamId)) {
                                BeanUtils.copyProperties(matchAns, bo);
                                result.add(bo);
                            }
                            break;
                        case 2:
                            if (matchAns.getGuestTeamId().equals(teamId)) {
                                BeanUtils.copyProperties(matchAns, bo);
                                result.add(bo);
                            }
                            break;
                        case 0:
                        default:
                            BeanUtils.copyProperties(matchAns, bo);
                            result.add(bo);
                            break;
                    }

                    if (result.size() == vo.getCountNum()) {
                        break;
                    }
                }


            }
        }
        return result;
    }

    /**
     * 获取盘路统计结果
     *
     * @param list
     * @return
     */
    @Override
    public Map<String, Integer> getMatchLetCount(List<FootBallMatchAnsBO> list) {
        Map<String, Integer> resultMap = new HashMap<>();
        //分别为赢盘，走盘，输盘次数
        int winNum = 0;
        int drawNum = 0;
        int lostNum = 0;

        if (!list.isEmpty()) {
            for (FootBallMatchAnsBO bo : list) {
                if (StringUtil.isBlank(bo.getLetGoResult())) {
                    continue;
                }
                switch (bo.getLetGoResult()) {
                    case "0":
                        winNum++;
                        break;
                    case "1":
                        drawNum++;
                        break;
                    case "2":
                        lostNum++;
                        break;
                }
            }
        }

        resultMap.put("w", winNum);
        resultMap.put("d", drawNum);
        resultMap.put("l", lostNum);
        return resultMap;
    }


    /**
     * 获取球队近期战绩走势
     *
     * @param list
     * @param vo
     * @return
     */
    private List<Integer> getMatchRecordCount(List<FootBallMatchAnsBO> list, AnalysisVO vo) {
        List<Integer> record = new ArrayList<>();

        if (!list.isEmpty()) {
            for (FootBallMatchAnsBO temp : list) {
                if (record.size() == vo.getCountNum()) {
                    break;
                }
                record.add(Integer.valueOf(temp.getScoreResult()));
            }
        }

        return record;
    }


    /**
     * 球队排名
     *
     * @param vo
     * @return
     */
    @Override
    public Map<String, Object> getMatchRankCount(AnalysisVO vo) {
        AnalysisEntity analysisEntity = ybfFootBallAnalysisService.footBallAnalysis(vo);
        List<String[]> scoreRank = analysisEntity.getScoreRank();
        List<TeamRankTrendBO> homeRank = new ArrayList<>();
        List<TeamRankTrendBO> guestRank = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        if (scoreRank != null && !scoreRank.isEmpty()) {
            for (int i = 0; i < scoreRank.size(); i++) {
                TeamRankTrendBO bo = new TeamRankTrendBO();
                String[] strings = scoreRank.get(i);
                bo.setVsCount(strings[0]);
                bo.setWin(strings[1]);
                bo.setDraw(strings[2]);
                bo.setLose(strings[3]);
                bo.setGoal(strings[4]);
                bo.setMiss(strings[5]);
                bo.setGoalDiff(strings[6]);
                bo.setIntegral(strings[7]);
                bo.setRank(strings[8]);
                if (i % 2 == 0) {
                    homeRank.add(bo);
                } else {
                    guestRank.add(bo);
                }
            }
        }
        resultMap.put("home", homeRank);
        resultMap.put("guest", guestRank);
        return resultMap;
    }


    /**
     * 统计结果
     *
     * @param vo
     * @return
     */
    @Override
    public Map<String, Object> getMatchResultCount(AnalysisVO vo) {
        if (ObjectUtil.isBlank(vo.getGuestId()) || ObjectUtil.isBlank(vo.getHomeId())) {
            return null;
        }

        List<FootBallMatchAnsBO> homeMatchAns = getFootBallMatchAnsBO(vo);
        vo.setHomeId(null);
        List<FootBallMatchAnsBO> guestMatchAns = getFootBallMatchAnsBO(vo);

        FootBallResultCountBO homeBo = getFootBallResultCountBO(homeMatchAns);
        FootBallResultCountBO guestBo = getFootBallResultCountBO(guestMatchAns);
        Map<String, Object> map = new HashMap<>();
        map.put("home", homeBo);
        map.put("guest", guestBo);
        return map;
    }


    /**
     * 获取球队统计结果
     *
     * @param matchAns
     * @return
     */

    private FootBallResultCountBO getFootBallResultCountBO(List<FootBallMatchAnsBO> matchAns) {
        FootBallResultCountBO resultBO = new FootBallResultCountBO();
        Map<String, Double> full = new HashMap<>();
        Map<String, Double> hafl = new HashMap<>();
        List<String> score = new ArrayList<>();
        List<String> goalNum = new ArrayList<>();

        int winNum = 0;
        int drawNum = 0;
        int lostNum = 0;

        int haflWinNum = 0;
        int haflDrawNum = 0;
        int haflLostNum = 0;
        Map<String, Integer> scoreMap = new TreeMap<>();
        Map<String, Integer> goalMap = new TreeMap<>();
        if (!matchAns.isEmpty()) {
            for (FootBallMatchAnsBO bo : matchAns) {
                switch (bo.getScoreResult()) {
                    case "0":
                        winNum++;
                        break;
                    case "1":
                        drawNum++;
                        break;
                    case "2":
                        lostNum++;
                        break;
                }
                if (!StringUtil.isBlank(bo.getScoreHalfRs())) {
                    switch (bo.getScoreHalfRs()) {
                        case "0":
                            haflWinNum++;
                            break;
                        case "1":
                            haflDrawNum++;
                            break;
                        case "2":
                            haflLostNum++;
                            break;
                    }
                }
                StringBuilder scoreBuilder = new StringBuilder().append(bo.getHomeScore()).append(":").append(bo.getGuestScore());
                String scoreStr = scoreBuilder.toString();
                //如果存在数量加一次 不存在给1
                if (scoreMap.containsKey(scoreStr)) {
                    int num = scoreMap.get(scoreStr);
                    num = ++num;
                    scoreMap.put(scoreStr, num);
                } else {
                    scoreMap.put(scoreStr, 1);
                }

                String goal = String.valueOf(bo.getHomeScore() + bo.getGuestScore());
                //如果存在数量加一次 不存在给1
                if (goalMap.containsKey(goal)) {
                    int num = goalMap.get(goal);
                    num = ++num;
                    goalMap.put(goal, num);
                } else {
                    goalMap.put(goal, 1);
                }
            }

            full.put("win", NumberUtil.div(winNum, matchAns.size(), 2) * 100);
            full.put("draw", NumberUtil.div(drawNum, matchAns.size(), 2) * 100);
            full.put("lost", NumberUtil.div(lostNum, matchAns.size(), 2) * 100);

            hafl.put("win", NumberUtil.div(haflWinNum, matchAns.size(), 2) * 100);
            hafl.put("draw", NumberUtil.div(haflDrawNum, matchAns.size(), 2) * 100);
            hafl.put("lost", NumberUtil.div(haflLostNum, matchAns.size(), 2) * 100);
            //对map的value 进行排序
            List<Map.Entry<String, Integer>> scoreList = new ArrayList<>(scoreMap.entrySet());
            Collections.sort(scoreList, new Comparator<Map.Entry<String, Integer>>() {
                //降序排序
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }

            });

            List<Map.Entry<String, Integer>> goalList = new ArrayList<>(goalMap.entrySet());
            Collections.sort(goalList, new Comparator<Map.Entry<String, Integer>>() {
                //降序排序
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }

            });

            int scoreTotal = 0;
            for (int i = 0; i < scoreList.size(); i++) {
                Map.Entry<String, Integer> entry = scoreList.get(i);
                String key = String.valueOf(entry.getKey());
                Integer value = entry.getValue();
                scoreTotal += value;
                score.add(key + "," + (NumberUtil.div(value, matchAns.size(), 2) * 100));
                if (score.size() == 4) {
                    score.add("other," + (NumberUtil.div(matchAns.size() - scoreTotal, matchAns.size(), 2) * 100));
                    break;
                }
            }

            int goalTotal = 0;
            for (int i = 0; i < goalList.size(); i++) {
                Map.Entry<String, Integer> entry = goalList.get(i);
                String key = String.valueOf(entry.getKey());
                Integer value = entry.getValue();
                goalTotal += value;
                goalNum.add(key + "," + (NumberUtil.div(value, matchAns.size(), 2) * 100));
                if (goalNum.size() == 4) {
                    goalNum.add("other," + (NumberUtil.div(matchAns.size() - goalTotal, matchAns.size(), 2) * 100));
                    break;
                }

            }
        }
        resultBO.setFull(full);
        resultBO.setHafl(hafl);
        resultBO.setScore(score);
        resultBO.setGoalNum(goalNum);
        return resultBO;
    }


    private Map<String, Integer> getMatchResultCount(List<FootBallMatchAnsBO> list) {
        Map<String, Integer> resultMap = new HashMap<>();
        //分别为赢盘，走盘，输盘次数
        int winNum = 0;
        int drawNum = 0;
        int lostNum = 0;

        if (!list.isEmpty()) {
            for (FootBallMatchAnsBO bo : list) {
                if (StringUtil.isBlank(bo.getLetGoResult())) {
                    continue;
                }
                switch (bo.getScoreResult()) {
                    case "0":
                        winNum++;
                        break;
                    case "1":
                        drawNum++;
                        break;
                    case "2":
                        lostNum++;
                        break;
                }
            }
        }

        resultMap.put("w", winNum);
        resultMap.put("d", drawNum);
        resultMap.put("l", lostNum);
        return resultMap;
    }


    /**
     * 足球球队最近对阵信息
     *
     * @param vo
     * @return
     */
    @Override
    public FootBallMatchCount getFootBallMatchCount(AnalysisVO vo) {
        AnalysisEntity analysisEntity = ybfFootBallAnalysisService.footBallAnalysis(vo);

        FootBallWarCountBO footBallWarCountBO = getFootBallWarCountBO(analysisEntity.getHisMatch(), vo);
        List<FootBallMatchAnsBO> homeMatchAns = getFootBallMatchAnsBO(vo);
        vo.setHomeId(null);
        List<FootBallMatchAnsBO> guestMatchAns = getFootBallMatchAnsBO(vo);
        Map<String, Integer> home = getMatchResultCount(homeMatchAns);
        Map<String, Integer> guest = getMatchResultCount(guestMatchAns);

        FootBallMatchCount result = new FootBallMatchCount();
        result.setWar(footBallWarCountBO);
        result.setHome(home);
        result.setGuest(guest);
        return result;
    }


}
