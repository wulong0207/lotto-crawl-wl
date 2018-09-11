package com.hhly.crawlcore.database.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.database.service.FbScheduleService;
import com.hhly.crawlcore.database.service.YbfFootBallOddsService;
import com.hhly.crawlcore.persistence.database.dao.OddsFbEuropeOddsDetailMapper;
import com.hhly.crawlcore.persistence.database.dao.OddsFbEuropeOddsMeanMapper;
import com.hhly.crawlcore.persistence.database.dao.OddsFbEuropeOddsPOMapper;
import com.hhly.crawlcore.persistence.database.po.FbSchedulePO;
import com.hhly.crawlcore.persistence.database.po.OddsFbEuropeOddsDetailPO;
import com.hhly.crawlcore.persistence.database.po.OddsFbEuropeOddsMeanPO;
import com.hhly.crawlcore.persistence.database.po.OddsFbEuropeOddsPO;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.HttpUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.NumberUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.OddsUtil;

@Service
public class YbfFootBallOddsServiceImpl implements YbfFootBallOddsService {


    private static Logger logger = LoggerFactory.getLogger(YbfFootBallOddsServiceImpl.class);

    @Autowired
    private FbScheduleService fbScheduleService;


    @Autowired
    private OddsFbEuropeOddsPOMapper oddsFbEuropeOddsPOMapper;

    @Autowired
    private OddsFbEuropeOddsDetailMapper oddsFbEuropeOddsDetailMapper;

    @Autowired
    private OddsFbEuropeOddsMeanMapper oddsFbEuropeOddsMeanMapper;

    @Value("${ybf_odds_url}")
    private String ybfOddsUrl;

    /**
     * 根据一比分id获取一比分欧赔
     *
     * @param matchId
     * @return
     */
    @Override
    public Map<String, List<OddsFbEuropeOddsPO>> getYbfOdds(String matchId) throws IOException, URISyntaxException {
        JSONObject jsonObject = null;
        String oddsResult = null;
        try {
            oddsResult = HttpUtil.doPost(ybfOddsUrl.replace("{0}", matchId));
            jsonObject = JsonUtil.jsonToObject(oddsResult, JSONObject.class);
        } catch (Exception e) {
            logger.info("查询一比分欧赔结果" + oddsResult);
        }

        List<OddsFbEuropeOddsPO> resultList = new ArrayList<>();
        Map<String, List<OddsFbEuropeOddsPO>> resultMap = new HashMap<>();
        if (ObjectUtil.isBlank(jsonObject)) {
            return resultMap;
        }

        List<String> odds = null;
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {

            odds = (List<String>) entry.getValue();
            for (String odd : odds) {
                String[] temp = odd.split(",");
                OddsFbEuropeOddsPO po = new OddsFbEuropeOddsPO();
                try {
                    //初盘赔率
                    po.setDrawF(Double.valueOf(temp[0]));
                    po.setHomewinF(Double.valueOf(temp[1]));
                    po.setGuestwinF(Double.valueOf(temp[2]));
                    //实盘赔率
                    po.setDrawN(Double.valueOf(temp[3]));
                    po.setHomewinN(Double.valueOf(temp[4]));
                    po.setGuestwinN(Double.valueOf(temp[5]));
                    po.setCreateTime(new Date());
                    //欧赔更新时间
                    po.setUpdateTime(DateUtil.convertStrToDate(temp[6], DateUtil.DEFAULT_FORMAT));
                    po.setEuropeId(Integer.valueOf(temp[7]));
                    po.setSourceId(entry.getKey());
                    po.setType(1);
                    po.setSourceType(1);
                    //获取返回率
                    po.setProbabilityTN(OddsUtil.getRateOfReturn(po.getHomewinN(), po.getDrawN(), po.getGuestwinN()));
                    po.setProbabilityTF(OddsUtil.getRateOfReturn(po.getHomewinF(), po.getDrawF(), po.getGuestwinF()));
                    //获取初盘赔率出现概率
                    po.setProbabilityHF(OddsUtil.getOneOfProbability(po.getHomewinF(), po.getProbabilityTF()));
                    po.setProbabilityDF(OddsUtil.getOneOfProbability(po.getDrawF(), po.getProbabilityTF()));
                    po.setProbabilityGF(OddsUtil.getOneOfProbability(po.getGuestwinF(), po.getProbabilityTF()));
                    //获取实盘赔率出现概率
                    po.setProbabilityHN(OddsUtil.getOneOfProbability(po.getHomewinN(), po.getProbabilityTN()));
                    po.setProbabilityDN(OddsUtil.getOneOfProbability(po.getDrawN(), po.getProbabilityTN()));
                    po.setProbabilityGN(OddsUtil.getOneOfProbability(po.getGuestwinN(), po.getProbabilityTN()));

                    resultList.add(po);
                } catch (Exception e) {
                    logger.info("getYbfOdds error po is" + JsonUtil.objectToJson(po) + "message is ====" + e.getMessage());
                    e.printStackTrace();
                }

            }

            resultMap.put(entry.getKey(), resultList);
        }

//        if (odds != null) {
//            double winAvgN = NumberUtil.div(winNSum, odds.length, 2);
//            double drawAvgN = NumberUtil.div(drawNSum, odds.length, 2);
//            double lostAvgN = NumberUtil.div(lostNSum, odds.length, 2);
//
//            double winAvgF = NumberUtil.div(winFSum, odds.length, 2);
//            double drawAvgF = NumberUtil.div(drawFSum, odds.length, 2);
//            double lostAvgF = NumberUtil.div(lostFSum, odds.length, 2);
//        }


        return resultMap;
    }


    /**
     * 保存欧赔数据
     *
     * @param po
     * @return
     */
    @Override
    public int saveOddsFbEuropeOddsPO(OddsFbEuropeOddsPO po) {
        OddsFbEuropeOddsPO oddsFbEuropeOddsPO = oddsFbEuropeOddsPOMapper.findOddsBySourceId(po);

        int i = 0;
        try {
            if (ObjectUtil.isBlank(oddsFbEuropeOddsPO)) {
                i = oddsFbEuropeOddsPOMapper.insert(po);
            } else {
                i = oddsFbEuropeOddsPOMapper.updateByPrimaryKeySelective(po);
            }
            OddsFbEuropeOddsDetailPO oddsFbEuropeOddsDetailPO = new OddsFbEuropeOddsDetailPO(po);
            saveOddsDetail(oddsFbEuropeOddsDetailPO);
        } catch (Exception e) {
            logger.info("save OddsFbEuropeOdds error po is " + JsonUtil.objectToJson(po) + "message ====" + e.getMessage());
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 保存一个对阵所有欧赔初赔和实盘赔率
     *
     * @param matchId
     * @return
     */
    @Override
    public int saveOddsFbEuropeOddsPOs(String matchId) throws IOException, URISyntaxException {
        Map<String, List<OddsFbEuropeOddsPO>> map = getYbfOdds(matchId);
        double winNSum = 0;
        double drawNSum = 0;
        double lostNSum = 0;

        double winFSum = 0;
        double drawFSum = 0;
        double lostFSum = 0;


        for (Map.Entry<String, List<OddsFbEuropeOddsPO>> entry : map.entrySet()) {
            FbSchedulePO fbSchedulePO = new FbSchedulePO();
            fbSchedulePO.setSourceId(entry.getKey());
            fbSchedulePO.setSourceType(1);
            Integer scheduleId = fbScheduleService.findFbSchedule(fbSchedulePO).getId();

            for (OddsFbEuropeOddsPO temp : entry.getValue()) {
                temp.setScheduleId(scheduleId);
                //基础数据入库
                saveOddsFbEuropeOddsPO(temp);

                //计算百家欧赔
                //实盘总赔率
                winNSum = NumberUtil.sum(temp.getHomewinN(), winNSum);
                drawNSum = NumberUtil.sum(temp.getDrawN(), drawNSum);
                lostNSum = NumberUtil.sum(temp.getGuestwinN(), lostNSum);
                //初盘总赔率
                winFSum = NumberUtil.sum(temp.getHomewinF(), winFSum);
                drawFSum = NumberUtil.sum(temp.getDrawF(), drawFSum);
                lostFSum = NumberUtil.sum(temp.getGuestwinF(), lostFSum);
            }

            OddsFbEuropeOddsMeanPO oddsFbEuropeOddsMeanPO = new OddsFbEuropeOddsMeanPO();
            oddsFbEuropeOddsMeanPO.setScheduleId(scheduleId);
            oddsFbEuropeOddsMeanPO.setHomewinF(NumberUtil.div(winFSum, entry.getValue().size(), 2));
            oddsFbEuropeOddsMeanPO.setDrawF(NumberUtil.div(drawFSum, entry.getValue().size(), 2));
            oddsFbEuropeOddsMeanPO.setGuestwinF(NumberUtil.div(lostFSum, entry.getValue().size(), 2));

            oddsFbEuropeOddsMeanPO.setHomewinN(NumberUtil.div(winNSum, entry.getValue().size(), 2));
            oddsFbEuropeOddsMeanPO.setDrawN(NumberUtil.div(drawNSum, entry.getValue().size(), 2));
            oddsFbEuropeOddsMeanPO.setGuestwinN(NumberUtil.div(lostNSum, entry.getValue().size(), 2));

            //获取返回率
            oddsFbEuropeOddsMeanPO.setProbabilityTN(OddsUtil.getRateOfReturn(oddsFbEuropeOddsMeanPO.getHomewinN(), oddsFbEuropeOddsMeanPO.getDrawN(), oddsFbEuropeOddsMeanPO.getGuestwinN()));
            oddsFbEuropeOddsMeanPO.setProbabilityTF(OddsUtil.getRateOfReturn(oddsFbEuropeOddsMeanPO.getHomewinF(), oddsFbEuropeOddsMeanPO.getDrawF(), oddsFbEuropeOddsMeanPO.getGuestwinF()));
            //获取初盘赔率出现概率
            oddsFbEuropeOddsMeanPO.setProbabilityHF(OddsUtil.getOneOfProbability(oddsFbEuropeOddsMeanPO.getHomewinF(), oddsFbEuropeOddsMeanPO.getProbabilityTF()));
            oddsFbEuropeOddsMeanPO.setProbabilityDF(OddsUtil.getOneOfProbability(oddsFbEuropeOddsMeanPO.getDrawF(), oddsFbEuropeOddsMeanPO.getProbabilityTF()));
            oddsFbEuropeOddsMeanPO.setProbabilityGF(OddsUtil.getOneOfProbability(oddsFbEuropeOddsMeanPO.getGuestwinF(), oddsFbEuropeOddsMeanPO.getProbabilityTF()));
            //获取实盘赔率出现概率
            oddsFbEuropeOddsMeanPO.setProbabilityHN(OddsUtil.getOneOfProbability(oddsFbEuropeOddsMeanPO.getHomewinN(), oddsFbEuropeOddsMeanPO.getProbabilityTN()));
            oddsFbEuropeOddsMeanPO.setProbabilityDN(OddsUtil.getOneOfProbability(oddsFbEuropeOddsMeanPO.getDrawN(), oddsFbEuropeOddsMeanPO.getProbabilityTN()));
            oddsFbEuropeOddsMeanPO.setProbabilityGN(OddsUtil.getOneOfProbability(oddsFbEuropeOddsMeanPO.getGuestwinN(), oddsFbEuropeOddsMeanPO.getProbabilityTN()));
            oddsFbEuropeOddsMeanPO.setSourceType(1);
            oddsFbEuropeOddsMeanPO.setSourceId(entry.getKey());
            oddsFbEuropeOddsMeanPO.setType(1);
            OddsFbEuropeOddsMeanPO resultBO = oddsFbEuropeOddsMeanMapper.findAvgOdds(oddsFbEuropeOddsMeanPO);
            if (ObjectUtil.isBlank(resultBO)) {
                oddsFbEuropeOddsMeanMapper.insert(oddsFbEuropeOddsMeanPO);
            }
        }

        return 1;
    }

    /**
     * 保存欧赔明细
     *
     * @param po
     * @return
     */
    @Override
    public int saveOddsDetail(OddsFbEuropeOddsDetailPO po) {
        OddsFbEuropeOddsDetailPO oddsFbEuropeOddsDetailPO = oddsFbEuropeOddsDetailMapper.findOddsDetail(po);
        int i = 0;
        if (ObjectUtil.isBlank(oddsFbEuropeOddsDetailPO)) {
            oddsFbEuropeOddsDetailMapper.insert(po);
        }
        return i;
    }


}
