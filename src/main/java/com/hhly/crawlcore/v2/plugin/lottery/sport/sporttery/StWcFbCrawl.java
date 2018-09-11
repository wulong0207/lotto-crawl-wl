
package com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.enums.FbLotteryEnum.StFbWcEnum;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;
import com.hhly.skeleton.base.common.LotteryEnum.ConIssue;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @desc    
 * @author  cheng chen
 * @date    2018年3月15日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class StWcFbCrawl extends AbstractCrawl {
	
	private static final Logger log = LoggerFactory.getLogger(StWcFbCrawl.class);
	
	//竞彩官网获取世界杯冠亚军赛事集合url
    @Value("${st_fb_wc_get_match_info}")
	private String st_fb_wc_get_match_info;
    
	public StWcFbCrawl() {
	}

	@Autowired
	public StWcFbCrawl(LotteryIssueDaoMapper lotteryIssueDaoMapper, SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper,
			SportTeamInfoService sportTeamInfoService, SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueDaoMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}

	@Override
	public void crawlHandle() throws Exception {
		
		for (StFbWcEnum stFbWcEnum : StFbWcEnum.values()) {
			switch (stFbWcEnum) {
			case CHP:
				getChampionMatch(stFbWcEnum);
				break;
			case FNL:
				getFinalMatch(stFbWcEnum);
				break;
			}
		}
	}

	@Override
	public void crawlMatch() throws Exception {
		
	}

	@Override
	public void dataHandle(String... params) throws Exception {
		
	}
    
	private void getChampionMatch(StFbWcEnum stFbWcEnum) throws Exception {
		String url = MessageFormat.format(st_fb_wc_get_match_info, stFbWcEnum.getId().toString(), stFbWcEnum.getType(), System.currentTimeMillis() + "");

		String json = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
        
        if (json.equals("0") || ObjectUtil.isBlank(json)) {
          	log.info("竞足官网冠军赛事数据抓取, 没有返回值, json : " + json);
            return;
        }
        JSONObject initJson = JSONObject.parseObject(json);
        JSONArray dataArrJson = initJson.getJSONArray("data");
        JSONObject dataJson = dataArrJson.getJSONObject(0);
        
        if (ObjectUtil.isBlank(dataJson)) {
        	log.info("冠军赛事数据, 没有值 dataJson :" + dataJson);
            return;
        }
        
        String byName = SportEnum.SportDataSource.JC_OFFICIAL.getName();
        
        Integer lotteryCode = stFbWcEnum.getLotteryCode();
        
        LotteryIssuePO paramPO = new LotteryIssuePO();
        paramPO.setLotteryCode(lotteryCode);
        paramPO.setCurrentIssue(ConIssue.CURRENT.getValue());
        LotteryIssuePO currIssueInfo = lotteryIssueDaoMapper.findSingle(paramPO);
        
        String value = dataJson.getString("data");
        
        String matchName = dataJson.getString("name");
        
        String[] matchStrs = value.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
        
        List<SportAgainstInfoPO> list = new ArrayList<SportAgainstInfoPO>();
        for (String matchStr : matchStrs) {
			String[] matchInfo = matchStr.split(SymbolConstants.DOUBLE_SLASH_TRAVERSE_SLASH);
			//球队编号
			String num = matchInfo[0];
			String systemCode = String.format("%02d", Integer.valueOf(num));
			//球队名称
			String homeName = matchInfo[1];
			//sp值
			String oddsWin = matchInfo[3];
			//最低概率
			String lowProb = matchInfo[4];
			//最高概率
			String highProb = matchInfo[5];
			//官网子玩法id + 编码
			String officialMatchCode = matchInfo[6];
			//equal [7], 无用
			//子玩法id
			String pid = matchInfo[8];
			//球队编号 [9], 无用
			//图片id
			String imgId = matchInfo[10];
			//图片url
			String imgUrl = matchInfo[11];
			
			SportAgainstInfoPO po = sportAgainstInfoDaoMapper.findByCode(lotteryCode, null, systemCode);
			if(!ObjectUtil.isBlank(po)){
				po.setOddsWin(Float.valueOf(oddsWin));
				po.setWinProb(highProb);
				po.setModifyBy(byName);
			}else{
				po = new SportAgainstInfoPO();
				po.setLotteryCode(lotteryCode);
				po.setIssueCode(currIssueInfo.getIssueCode());
				po.setMatchName(matchName);
				
				//赛事不存在, 新增赛事信息
				SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(homeName, null, SportEnum.MatchTypeEnum.FOOTBALL.getCode(), null, sportterySource);
				
				SportTeamInfoPO homeTeamPO = getTeamInfo(homeSourcePO);
				if(!ObjectUtil.isBlank(homeTeamPO)){
					po.setHomeTeamId(homeTeamPO.getId());
					po.setHomeName(homeTeamPO.getTeamFullName());
				}else{
					po.setHomeName(homeName);
				}
				po.setSystemCode(systemCode);
				po.setOfficialMatchCode(officialMatchCode);
				po.setMatchStatus((short)9);
				po.setOddsWin(Float.valueOf(oddsWin));
				po.setWinProb(highProb);
				po.setCreateBy(byName);
			}
			sportAgainstInfoDaoMapper.merge(po);
		}
	}
	
	private void getFinalMatch(StFbWcEnum stFbWcEnum) throws Exception{
		String url = MessageFormat.format(st_fb_wc_get_match_info, stFbWcEnum.getId().toString(), stFbWcEnum.getType(), System.currentTimeMillis() + "");

		String json = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
        
        if (json.equals("0") || ObjectUtil.isBlank(json)) {
          	log.info("竞足官网冠亚军赛事数据抓取, 没有返回值, json : " + json);
            return;
        }
        JSONObject initJson = JSONObject.parseObject(json);
        JSONArray dataArrJson = initJson.getJSONArray("data");
        JSONObject dataJson = dataArrJson.getJSONObject(0);
        
        if (ObjectUtil.isBlank(dataJson)) {
        	log.info("冠亚军赛事数据, 没有值 dataJson :" + dataJson);
            return;
        }
        
        String byName = SportEnum.SportDataSource.JC_OFFICIAL.getName();
        
        Integer lotteryCode = stFbWcEnum.getLotteryCode();
        
        LotteryIssuePO paramPO = new LotteryIssuePO();
        paramPO.setLotteryCode(lotteryCode);
        paramPO.setCurrentIssue(ConIssue.CURRENT.getValue());
        LotteryIssuePO currIssueInfo = lotteryIssueDaoMapper.findSingle(paramPO);
        
        String value = dataJson.getString("data");
        
        String matchName = dataJson.getString("name");
        
        String[] matchStrs = value.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
        
//        List<SportAgainstInfoPO> list = new ArrayList<SportAgainstInfoPO>();
        for (String matchStr : matchStrs) {
        	
			String[] matchInfo = matchStr.split(SymbolConstants.DOUBLE_SLASH_TRAVERSE_SLASH);
			//编号
			String num = matchInfo[0];
			String systemCode = String.format("%02d", Integer.valueOf(num));
			//主球队名称
			String teamName = matchInfo[1];
			String[] teamStrs = teamName.split(SymbolConstants.DOUBLE_SLASH_CHINA_TRAVERSE_SLASH);
	
			//售卖状态[2], 无用
			//sp值
			String oddsWin = matchInfo[3];
			//最低概率
			String lowProb = matchInfo[4];
			//最高概率
			String highProb = matchInfo[5];
			//官网子玩法id + 编码
			String officialMatchCode = matchInfo[6];
			//equal [7], 无用
			//子玩法id
			String pid = matchInfo[8];
			//编号 [9], 无用
			//图片id
			String ImgId = matchInfo[10];
			//图片url
			String imgUrl = matchInfo[11];
			
			SportAgainstInfoPO po = sportAgainstInfoDaoMapper.findByCode(lotteryCode, null, systemCode);
			if(!ObjectUtil.isBlank(po)){
				po.setWinProb(highProb);
				po.setOddsWin(Float.valueOf(oddsWin));
				po.setModifyBy(byName);
			}else{
				po = new SportAgainstInfoPO();
				po.setLotteryCode(lotteryCode);
				po.setIssueCode(currIssueInfo.getIssueCode());
				po.setMatchName(matchName);
				
				//赛事不存在, 新增赛事信息
				String homeName = "";
				String awayName = "";
				if(num.equals("50")){
					homeName = teamStrs[0];
					po.setHomeName(homeName);
				}else{
					homeName = teamStrs[0];
					awayName = teamStrs[1];
					
					SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(homeName, null, SportEnum.MatchTypeEnum.FOOTBALL.getCode(), null, sportterySource);
					SportTeamSourceInfoPO awaySourcePO = new SportTeamSourceInfoPO(awayName, null, SportEnum.MatchTypeEnum.FOOTBALL.getCode(), null, sportterySource);
					
					SportTeamInfoPO homeTeamPO = getTeamInfo(homeSourcePO);
					if(!ObjectUtil.isBlank(homeTeamPO)){
						po.setHomeTeamId(homeTeamPO.getId());
						po.setHomeName(homeTeamPO.getTeamFullName());
					}else{
						po.setHomeName(homeName);
					}
					SportTeamInfoPO awayTeamPO = getTeamInfo(awaySourcePO);
					if(!ObjectUtil.isBlank(awayTeamPO)){
						po.setVisitiTeamId(awayTeamPO.getId());
						po.setVisitiName(awayTeamPO.getTeamFullName());
					}else{
						po.setVisitiName(awayName);
					}
				}
				
				po.setSystemCode(systemCode);
				po.setOfficialMatchCode(officialMatchCode);
				po.setMatchStatus((short)9);
				po.setOddsWin(Float.valueOf(oddsWin));
				po.setWinProb(highProb);
				po.setCreateBy(byName);
			}
			sportAgainstInfoDaoMapper.merge(po);
		}		
	}
	
	public static void main(String[] args) {
		try {
//			System.out.println(String.format("%02d", 1));
//			System.out.println(NumberFormatUtil.format("22"));
			String[] str = "巴西—德国".split("\\—");
			for (int i = 0; i < str.length; i++) {
				System.out.println(str[i]);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
