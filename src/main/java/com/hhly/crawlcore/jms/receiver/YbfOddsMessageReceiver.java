package com.hhly.crawlcore.jms.receiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportMatchOddDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportTeamInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchOddPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.sport.bo.JcAvgOddsBO;

/**
 * @author lgs on
 * @version 1.0
 * @desc 接受欧赔Jms Message类
 * @date 2017/3/8.
 * @company 益彩网络科技有限公司
 */
@Component
public class YbfOddsMessageReceiver {

	protected Logger logger = LoggerFactory.getLogger(YbfOddsMessageReceiver.class.getName());
	
	@Autowired
	private SportMatchOddDaoMapper sportMatchOddDaoMapper;

    @Resource
    private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

    @Resource
    private SportTeamInfoDaoMapper teamInfoDaoMapper;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 接受百家欧赔消息
	 * @param message
     */
	@JmsListener(destination= "match.OneHundredOdds.refresh", containerFactory="jmsListenerContainerTopic")
    public synchronized void oddsMessage(String message) throws JsonProcessingException {
        logger.info("[odds message] is " + message);
        System.out.println("[odds message] is " + message);
        if (!"{}".equals(message)) {
			List<JSONObject> jsonObjects = JsonUtil.jsonToArray(message, JSONObject.class);
			List<SportMatchOddPO> pos = new ArrayList<>();
			List<String> matchIds = new ArrayList<>();
			for (JSONObject jsonObject : jsonObjects) {
				for (Map.Entry<String, Object> scheduleMap : jsonObject.entrySet()) {
					String scheduleId = scheduleMap.getKey();
					Object scheduleOdds = scheduleMap.getValue();
					JSONObject oddsObj = (JSONObject) scheduleOdds;
					for (Map.Entry<String, Object> oddsMap : oddsObj.entrySet()) {
						Long gameId = Long.valueOf(oddsMap.getKey());
						JSONObject oddObj = (JSONObject) oddsMap.getValue();
						Float win = oddObj.getFloat("homeWin");
						Float draw = oddObj.getFloat("standOff");
						Float lost = oddObj.getFloat("guestWin");
						Date modifyTime = DateUtil.convertStrToDate(oddObj.getString("modifyTime"), DateUtil.DATE_MMM_D_YYYY_K_M_S_S);
						SportMatchOddPO po = new SportMatchOddPO(null, scheduleId, gameId, win, draw, lost, modifyTime, null);
						pos.add(po);
					}
					matchIds.add(scheduleId);
				}
			}
            try {
            	sportMatchOddDaoMapper.insert(pos);
                //查询平均欧赔
                List<JcAvgOddsBO> avgOdds = sportMatchOddDaoMapper.selectAvgOddsByMatchId(matchIds);
                ObjectMapper mapper = new ObjectMapper();
                sportAgainstInfoDaoMapper.updateAvgOdds(avgOdds);
                //更新缓存
                redisUtil.addString(CacheConstants.S_COMM_SPORT_AVG_ODDS_MATCH_LIST, mapper.writeValueAsString(avgOdds), CacheConstants.TEN_MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }

		}
	}

	@JmsListener(destination= "football.bjlot.ratio", containerFactory="jmsListenerContainerTopic")
    public synchronized void bjdcListener(String message) throws Exception {
        logger.info("[bjdcListener message] is " + message);
		System.out.println("=====" + message);
        JSONArray jsonArray = JSONArray.parseArray(message);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.getString(i));

            //处理北单
            String prefix = DateUtil.getNow("yy");

            SportAgainstInfoPO queryPo = new SportAgainstInfoPO();
            queryPo.setIssueCode(prefix.substring(0, 1) + jsonObject.getString("issue"));
            queryPo.setBjNum(jsonObject.getInteger("serNum"));
            //1.首先，根据彩期编码和官方编码，获取到对阵信息
            List<SportAgainstInfoPO> infoPOList = sportAgainstInfoDaoMapper.findCondition(queryPo);
            SportTeamInfoPO teamInfoPO;
            if (!ObjectUtil.isBlank(infoPOList)) {
                for (SportAgainstInfoPO sportAgainstInfoPO : infoPOList) {
                    sportAgainstInfoPO.setMatchAnalysisUrl(jsonObject.getString("analysisId"));//析ID
                    String[] oddsStr = jsonObject.getString("odds").split(",");
                    sportAgainstInfoPO.setOddsWin(Float.valueOf(oddsStr[0]));//主胜
                    sportAgainstInfoPO.setOddsFail(Float.valueOf(oddsStr[1]));//主负
                    sportAgainstInfoPO.setOddsDraw(Float.valueOf(oddsStr[2]));//和
                    sportAgainstInfoPO.setUpdateTime(jsonObject.getDate("updateTime"));//更新时间
                    sportAgainstInfoPO.setBjNum(jsonObject.getInteger("serNum"));
                    sportAgainstInfoDaoMapper.update(sportAgainstInfoPO);

                    //2.得到主客对ID，更新对应排名信息
                    teamInfoPO = new SportTeamInfoPO();
                    //更新主队排名
                    updateHomeTeam(jsonObject, teamInfoPO, sportAgainstInfoPO);
                    //更新客队排名
                    updateVisiterTeam(jsonObject, teamInfoPO, sportAgainstInfoPO);
                }
            }
        }

    }


    private void updateHomeTeam(JSONObject jsonObject, SportTeamInfoPO teamInfoPO, SportAgainstInfoPO sportAgainstInfoPO) {
        teamInfoPO.setId(sportAgainstInfoPO.getHomeTeamId());
        teamInfoPO.setTeamOrder(jsonObject.getString("homeRank"));
        teamInfoDaoMapper.update(teamInfoPO);
    }

    private void updateVisiterTeam(JSONObject jsonObject, SportTeamInfoPO teamInfoPO, SportAgainstInfoPO sportAgainstInfoPO) {
        teamInfoPO.setId(sportAgainstInfoPO.getVisitiTeamId());
        teamInfoPO.setTeamOrder(jsonObject.getString("guestRank"));
        teamInfoDaoMapper.update(teamInfoPO);
    }

}
