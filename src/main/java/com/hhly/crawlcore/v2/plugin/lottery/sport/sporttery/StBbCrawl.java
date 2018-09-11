
package com.hhly.crawlcore.v2.plugin.lottery.sport.sporttery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBbBssDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBbSpDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBbWfDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportDataBbWsDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportStatusBbDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportDataBbBssPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBbSpPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBbWfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBbWsPO;
import com.hhly.crawlcore.persistence.sport.po.SportStatusBbPO;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.crawlcore.v2.plugin.lottery.sport.AbstractSportCrawl;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.common.SportEnum.WfTypeEnum;
import com.hhly.skeleton.base.constants.MQConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.hhly.skeleton.base.mq.msg.JlSpMessageData;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.NumberFormatUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.lotto.base.sport.bo.JclqTrendSpBO;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */

@Service
public class StBbCrawl extends AbstractSportCrawl {
	
	private static final Logger log  = LoggerFactory.getLogger(StBbCrawl.class);
	
    @Autowired
    private SportDataBbBssDaoMapper sportDataBbBssDaoMapper;

    @Autowired
    private SportDataBbWfDaoMapper sportDataBbWfDaoMapper;

    @Autowired
    private SportDataBbWsDaoMapper sportDataBbWsDaoMapper;

    @Autowired
    private SportDataBbSpDaoMapper sportDataBbSpDaoMapper;
    
    @Autowired
    private SportStatusBbDaoMapper sportStatusBbDaoMapper;
    
	//竞彩官网微信获取赛事集合url
    @Value("${st_bb_get_all_match}")
	private String st_bb_get_all_match;
	
	//竞彩官网获取暂定赛事集合
    @Value("${st_bb_interim_match}")
	private String st_bb_interim_match; 
	
    //竞彩官网赛事状态和最新sp值url
    @Value("${st_bb_get_match_info}")
    public String st_bb_get_match_info; 
    
    //竞彩官网赛事历史sp值
    @Value("${st_bb_get_match_history_sp}")
    public String st_bb_get_match_history_sp;
    
    @Value("${st_bb_get_html_match_history_sp}")
    public String st_bb_get_html_match_history_sp;
	
	public StBbCrawl() {
	}

    @Autowired
	public StBbCrawl(LotteryIssueDaoMapper lotteryIssueMapper, SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper,
			SportTeamInfoService sportTeamInfoService, SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}

    
    
	public void dataHandle(String... params) throws Exception {
		getHistorySp();
	}

	@Override
	public void getSaleMatchList() throws Exception {
        String url = st_bb_get_match_info + "&_=" + new Date().getTime();
        try {
            String json = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
            if (json.equals("0") || ObjectUtil.isBlank(json)) {
            	log.info("竞篮官网销售中赛事数据抓取, 没有返回值, json : " + json);
            	return;
            }
            String initData = StringUtils.stripEnd(StringUtils.stripStart(json, SymbolConstants.PARENTHESES_LEFT), SymbolConstants.PARENTHESES_RIGHT);
            JSONObject initJson = JsonUtil.jsonToObject(initData, JSONObject.class);
            JSONObject dataJson = initJson.getJSONObject("data");

            Integer lotteryCode = getLotteryCode();
            if (ObjectUtil.isBlank(dataJson)) {
                return;
            }
            
            List<LotteryIssuePO> getIssueInfo = lotteryIssueDaoMapper.getIssueInfo(lotteryCode);
            
            for (Object valueJson : dataJson.values()) {
                JSONObject singleJson = JSONObject.parseObject(valueJson.toString());
                
                Long sportAgainstInfoId = MatchHandle(singleJson, getIssueInfo);

                JSONObject mnl = ObjectUtil.isBlank(singleJson.getJSONObject("mnl")) ? new JSONObject() : singleJson.getJSONObject("mnl");//胜负SP值、包括子玩法状态
                JSONObject hdc = ObjectUtil.isBlank(singleJson.getJSONObject("hdc")) ? new JSONObject() : singleJson.getJSONObject("hdc");//让分胜负SP值、包括子玩法状态
                JSONObject wnm = ObjectUtil.isBlank(singleJson.getJSONObject("wnm")) ? new JSONObject() : singleJson.getJSONObject("wnm");//胜分差SP值、包括子玩法状态
                JSONObject hilo = ObjectUtil.isBlank(singleJson.getJSONObject("hilo")) ? new JSONObject() : singleJson.getJSONObject("hilo");//大小分SP值、包括子玩法状态

                //处理SP值
                Float letHomeLostSp = hdc.getFloat("a");//主负
                Float letHomeWinSp = hdc.getFloat("h");//主胜
                Float letScore = hdc.getFloat("fixedodds");//让分

                Float homeLostSp = mnl.getFloat("a");//主负
                Float homeWinSp = mnl.getFloat("h");//主胜

                Float spBig = hilo.getFloat("h");
                Float spSmall = hilo.getFloat("l");
                Float presetScore = hilo.getFloat("fixedodds");

                Float spFail15 = wnm.getFloat("l1");
                Float spFail610 = wnm.getFloat("l2");
                Float spFail1115 = wnm.getFloat("l3");
                Float spFail1620 = wnm.getFloat("l4");
                Float spFail2125 = wnm.getFloat("l5");
                Float spFail26 = wnm.getFloat("l6");
                Float spWin15 = wnm.getFloat("w1");
                Float spWin610 = wnm.getFloat("w2");
                Float spWin1115 = wnm.getFloat("w3");
                Float spWin1620 = wnm.getFloat("w4");
                Float spWin2125 = wnm.getFloat("w5");
                Float spWin26 = wnm.getFloat("w6");

				SportDataBbSpPO spPo = new SportDataBbSpPO(sportAgainstInfoId,
                        letScore,
                        letHomeWinSp,
                        letHomeLostSp,
                        homeWinSp,
                        homeLostSp,
                        presetScore,
                        spBig,
                        spSmall,
                        spFail15,
                        spFail610,
                        spFail1115,
                        spFail1620,
                        spFail2125,
                        spFail26,
                        spWin15,
                        spWin610,
                        spWin1115,
                        spWin1620,
                        spWin2125,
                        spWin26);

                sportDataBbSpDaoMapper.merge(spPo);

                //处理子玩法状态
                SportStatusBbPO statusPo = new SportStatusBbPO(sportAgainstInfoId,
                        SportLotteryUtil.getSaleType(mnl.getString("single")),
                        SportLotteryUtil.getSaleType(hdc.getString("single")),
                        SportLotteryUtil.getSaleType(hilo.getString("single")),
                        SportLotteryUtil.getSaleType(wnm.getString("single")),
                        SportDataSource.JC_OFFICIAL.getName());
                
                //新增或修改竞篮子玩法状态
                sportStatusBbDaoMapper.merge(statusPo);
                
//                int statusFlag = sportStatusBbDaoMapper.selectCount(statusPo);
//                if (statusFlag == 0) {
//                	sportStatusBbDaoMapper.insert(statusPo);
//                }
            }
        } catch (Exception e) {
            log.error("竞篮官网数据抓取异常：" + e.getMessage());
        }		
	}	
	
	@Override
	protected String getAllMatchUrl() throws Exception {
		return st_bb_get_all_match + "&_=" + System.currentTimeMillis();
	}

	@Override
	protected String getInterimMatchUrl() throws Exception {
		return st_bb_interim_match + "?_=" + System.currentTimeMillis();
	}

	@Override
	protected Integer getLotteryCode() throws Exception {
		return bbLotteryCode;
	}
	
	@Override
	protected Short getType() throws Exception {
		return (short)SportEnum.MatchTypeEnum.BASKETBALL.getCode();
	}

	/**
	 * 
	 * @param officialId
	 * @param sportAgainstInfoId
	 * @return
	 * @date 2017年11月21日下午8:13:03
	 * @author cheng.chen
	 */
	@Override
	protected void getSingleHistorySp(String officialId, Long sportAgainstInfoId) {
        if (sportAgainstInfoId == 0 || StringUtils.isBlank(officialId)) {
            return;
        }
        //竞篮大小分历史sp值
        List<SportDataBbBssPO> bssPOs = new ArrayList<>();
        //竞篮胜负和让球历史sp值
        List<SportDataBbWfPO> wfPOs = new ArrayList<>();
        //竞篮让分胜负历史sp值
//        List<SportDataBbWfPO> letWfPos = new ArrayList<>();
        //竞篮胜分差历史sp值
        List<SportDataBbWsPO> wsPOs = new ArrayList<>();
        
        String url = st_bb_get_match_history_sp.replace("{0}", officialId) + "&_=" + new Date().getTime();
        try {

            String json = HTTPUtil.transRequest(url, connectTimeout, socketTimeout);
            if(ObjectUtil.isBlank(json)){
            	log.info("获取单个对阵历史sp异常  sportAgainstInfoId : " + sportAgainstInfoId + "没有返回值, json : " + json);
            	return;
            }
            String initData = StringUtils.stripEnd(StringUtils.stripStart(json, SymbolConstants.PARENTHESES_LEFT), SymbolConstants.PARENTHESES_RIGHT);
            JSONObject initJson = JsonUtil.jsonToObject(initData, JSONObject.class);
            JSONObject statusJson = initJson.getJSONObject("status");
    		int code = statusJson.getInteger("code");
    		String message = statusJson.getString("message");

    		if (code != 0 && !message.equals("")) {
    			throw new ServiceRuntimeException("获取官网www.sporttery.cn的竞彩篮球历史sp值异常!!!");
    		}

            JSONObject listJson = initJson.getJSONObject("result").getJSONObject("list");


            JSONArray wfArray = new JSONArray();
            JSONArray letWfArray = new JSONArray();
            JSONArray bssArray = new JSONArray();
            JSONArray wsArray = new JSONArray();

            try {
            	wfArray = listJson.getJSONObject("mnl").getJSONArray("odds_his");
            } catch (Exception e) {
                log.error("解析竞篮胜负历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
            	letWfArray = listJson.getJSONObject("hdc").getJSONArray("odds_his");
            } catch (Exception e) {
            	 log.error("解析竞篮让分胜负历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
            	bssArray = listJson.getJSONObject("hilo").getJSONArray("odds_his");
            } catch (Exception e) {
            	 log.error("解析竞篮大小分历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            try {
            	wsArray = listJson.getJSONObject("wnm").getJSONArray("odds_his");
            } catch (Exception e) {
            	 log.error("解析竞篮胜分差历史sp值异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
            }

            //解析竞篮胜负
            SportDataBbWfPO wfLastPO = sportDataBbWfDaoMapper.findLast(sportAgainstInfoId, WfTypeEnum.NOT_LET.getValue());
            for (int i = 0; i < wfArray.size(); i++) {
                JSONObject lefWdfObject = wfArray.getJSONObject(i);

                Float winSp = lefWdfObject.getFloat("h");
                Float lostSp = lefWdfObject.getFloat("a");
                String time = lefWdfObject.getString("date") + " " + lefWdfObject.getString("time");
                if (ObjectUtil.isBlank(time))
                    continue;
            	Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATE_FORMAT_YY_MM_DD_HH_MM_SS);
                if(!ObjectUtil.isBlank(wfLastPO) && !ObjectUtil.isBlank(wfLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(wfLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;           
                SportDataBbWfPO wfPO = new SportDataBbWfPO(sportAgainstInfoId, null, lostSp, 
                		winSp, WfTypeEnum.NOT_LET.getValue(), releaseTime);
        		if(!ObjectUtil.isBlank(wfLastPO) && wfPO.isEquals(wfLastPO))
        			continue;
        		wfPOs.add(wfPO);
        		wfLastPO = wfPO;                
            }

            //解析竞篮让球胜负
            SportDataBbWfPO letWfLastPO = sportDataBbWfDaoMapper.findLast(sportAgainstInfoId, WfTypeEnum.LET.getValue());
            for (int i = 0; i < letWfArray.size(); i++) {
                JSONObject letWfObject = letWfArray.getJSONObject(i);
                Float letScore = letWfObject.getFloat("goalline");
                Float winSp = letWfObject.getFloat("h");
                Float lostSp = letWfObject.getFloat("a");
                String time = letWfObject.getString("date") + " " + letWfObject.getString("time");
                if (ObjectUtil.isBlank(time))
                    continue;
            	Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATE_FORMAT_YY_MM_DD_HH_MM_SS);
                if(!ObjectUtil.isBlank(letWfLastPO) && !ObjectUtil.isBlank(letWfLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(letWfLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;                   
                SportDataBbWfPO letWfPO = new SportDataBbWfPO(sportAgainstInfoId, letScore,lostSp, winSp, WfTypeEnum.LET.getValue(), 
                		releaseTime);
        		if(!ObjectUtil.isBlank(letWfLastPO) && letWfPO.isEquals(letWfLastPO))
        			continue;
        		wfPOs.add(letWfPO);
        		letWfLastPO = letWfPO;   
            }

            //解析竞篮大小分
            SportDataBbBssPO bssLastPO = sportDataBbBssDaoMapper.findLast(sportAgainstInfoId);
            for (int i = 0; i < bssArray.size(); i++) {
                JSONObject bssObject = bssArray.getJSONObject(i);
                Float letScore = bssObject.getFloat("goalline");
                Float spBig = bssObject.getFloat("h");
                Float spSmall = bssObject.getFloat("l");
                String time = bssObject.getString("date") + " " + bssObject.getString("time");
                if (ObjectUtil.isBlank(time))
                    continue;  
                Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATE_FORMAT_YY_MM_DD_HH_MM_SS);
                if(!ObjectUtil.isBlank(bssLastPO) && !ObjectUtil.isBlank(bssLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(bssLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;                    
                SportDataBbBssPO bssPO = new SportDataBbBssPO(sportAgainstInfoId,letScore,spBig, spSmall, 
                		releaseTime);
        		if(!ObjectUtil.isBlank(bssLastPO) && bssPO.isEquals(bssLastPO))
        			continue;                
                bssPOs.add(bssPO);
                bssLastPO = bssPO;
            }

            //解析竞篮胜分差
            SportDataBbWsPO wsLastPO = sportDataBbWsDaoMapper.findLast(sportAgainstInfoId);
            for (int i = 0; i < wsArray.size(); i++) {
                JSONObject wsObject = wsArray.getJSONObject(i);
                Float spWin15 = wsObject.getFloat("w1");
                Float spWin610 = wsObject.getFloat("w2");
                Float spWin1115 = wsObject.getFloat("w3");
                Float spWin1620 = wsObject.getFloat("w4");
                Float spWin2125 = wsObject.getFloat("w5");
                Float spWin26 = wsObject.getFloat("w6");
                Float spFail15 = wsObject.getFloat("l1");
                Float spFail610 = wsObject.getFloat("l2");
                Float spFail1115 = wsObject.getFloat("l3");
                Float spFail1620 = wsObject.getFloat("l4");
                Float spFail2125 = wsObject.getFloat("l5");
                Float spFail26 = wsObject.getFloat("l6");
                String time = wsObject.getString("date") + " " + wsObject.getString("time");
                if (ObjectUtil.isBlank(time))
                    continue;  
                Date releaseTime = DateUtil.convertStrToDate(time, DateUtil.DATE_FORMAT_YY_MM_DD_HH_MM_SS);
                if(!ObjectUtil.isBlank(wsLastPO) && !ObjectUtil.isBlank(wsLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(wsLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;                 
                SportDataBbWsPO wsPO = new SportDataBbWsPO(sportAgainstInfoId, spFail15, spFail610,
                		spFail1115, spFail1620, spFail2125, spFail26, spWin15, spWin610, spWin1115, 
                		spWin1620, spWin2125, spWin26, releaseTime);
        		if(!ObjectUtil.isBlank(wsLastPO) && wsPO.isEquals(wsLastPO))
        			continue;                
                wsPOs.add(wsPO);
                wsLastPO = wsPO;
            }
            
            //批量插入胜负和让球胜负的sp对象
            if(!ObjectUtil.isBlank(wfPOs)){
              	sportDataBbWfDaoMapper.insertBatch(wfPOs);
            }else{
            	wfLastPO = null;
            	letWfLastPO = null;
            }
 
            //批量插入大小分sp对象
            if(!ObjectUtil.isBlank(bssPOs)){
             	sportDataBbBssDaoMapper.insertBatch(bssPOs);
            }else{
            	bssLastPO = null;
            }
            
 
            //批量插入胜分差sp喜爱那个
            if(!ObjectUtil.isBlank(wsPOs)){
               	sportDataBbWsDaoMapper.insertBatch(wsPOs);
            }else{
            	wsLastPO = null;
            }
            
            if(!ObjectUtil.isBlank(wfPOs) || !ObjectUtil.isBlank(bssPOs) || !ObjectUtil.isBlank(wsPOs)){
            	pushSp(sportAgainstInfoId, wfLastPO, letWfLastPO, bssLastPO, wsLastPO);
            }
            	
            
        } catch (Exception e) {
            log.error("获取单个对阵历史sp异常, officialId ：" + officialId + " sportAgainstInfoId : " + sportAgainstInfoId + "message:" + e.getMessage());
        }
    }
	
	public void getSingleHtmlHistorySp(String officialId, Long sportAgainstInfoId){
		
        if (sportAgainstInfoId == 0 || StringUtils.isBlank(officialId)) {
            return;
        }
        //竞篮大小分历史sp值
        List<SportDataBbBssPO> bssPOs = new ArrayList<>();
        //竞篮胜负和让球历史sp值
        List<SportDataBbWfPO> wfPOs = new ArrayList<>();
        //竞篮让分胜负历史sp值
//        List<SportDataBbWfPO> letWfPos = new ArrayList<>();
        //竞篮胜分差历史sp值
        List<SportDataBbWsPO> wsPOs = new ArrayList<>();
        
		try {
			String url = st_bb_get_html_match_history_sp.replace("{0}", officialId)+ "&_=" + System.currentTimeMillis();
			
            Document doc = ParseHtmlUtil.getDocument(url);
            Elements tabls = doc.select("table");
            Elements wfEl = ParseHtmlUtil.getSelect(tabls.eq(0), "tr");//胜负固定奖金
            Elements letWfEl = ParseHtmlUtil.getSelect(tabls.eq(1), "tr");//让分胜负固定奖金
            Elements bigSmallEl = ParseHtmlUtil.getSelect(tabls.eq(2), "tr");//大小分固定奖金
            Elements wsEl = ParseHtmlUtil.getSelect(tabls.eq(3), "tr");//胜分差固定奖金

            //解析胜负历史sp
            SportDataBbWfPO wfLastPO = sportDataBbWfDaoMapper.findLast(sportAgainstInfoId, WfTypeEnum.NOT_LET.getValue());
            //index从2开始 0和1是表头不需要读取
            for (int i = 2; i < wfEl.size(); i++) {
                Element el = wfEl.get(i);
                Elements tdEl = ParseHtmlUtil.getElementsByTag(el, "td");
                String time = ParseHtmlUtil.getElementsEqIndexText(tdEl, 0);
                if (StringUtils.isBlank(time))
                    continue;
            	Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(wfLastPO) && !ObjectUtil.isBlank(wfLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(wfLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;    
                
                Float homeLostSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 1, Float.class);
                Float homeWinSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 2, Float.class);
                
                SportDataBbWfPO po = new SportDataBbWfPO(sportAgainstInfoId, null, homeLostSp, homeWinSp,
                		WfTypeEnum.NOT_LET.getValue(), releaseTime);
        		if(!ObjectUtil.isBlank(wfLastPO) && po.isEquals(wfLastPO))
        			continue;
        		wfPOs.add(po);
        		wfLastPO = po;
        		
            }
    
            //解析让分胜负历史sp
            SportDataBbWfPO letWfLastPO = sportDataBbWfDaoMapper.findLast(sportAgainstInfoId, WfTypeEnum.LET.getValue());
            for (int i = 1 ; i < letWfEl.size() ; i++) {
                Element el = letWfEl.get(i);
                Elements tdEl = ParseHtmlUtil.getElementsByTag(el, "td");
                String time = ParseHtmlUtil.getElementsEqIndexText(tdEl, 0);
                if (StringUtils.isBlank(time))
                    continue;
            	Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(letWfLastPO) && !ObjectUtil.isBlank(letWfLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(letWfLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;   
            	
                Float homeLostSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 1, Float.class);
                Float letScore = ParseHtmlUtil.getElementsEqIndexText(tdEl, 2, Float.class);
                Float homeWinSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 3 , Float.class);

                SportDataBbWfPO po = new SportDataBbWfPO(sportAgainstInfoId, letScore, homeLostSp, homeWinSp, 
                		WfTypeEnum.LET.getValue(), releaseTime);
        		if(!ObjectUtil.isBlank(letWfLastPO) && po.isEquals(letWfLastPO))
        			continue;
        		wfPOs.add(po);
        		letWfLastPO = po;
        		
            }
    
            //解析大小分历史sp
            SportDataBbBssPO bssLastPO = sportDataBbBssDaoMapper.findLast(sportAgainstInfoId);
            for (int i = 1 ; i < bigSmallEl.size() ; i++) {
                Element el = bigSmallEl.get(i);
                Elements tdEl = ParseHtmlUtil.getElementsByTag(el, "td");
                String time = ParseHtmlUtil.getElementsEqIndexText(tdEl, 0);//发布时间
                if (StringUtils.isBlank(time))
                    continue;
            	Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(bssLastPO) && !ObjectUtil.isBlank(bssLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(bssLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;
            	
            	Float bigSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 1, Float.class);//大分
            	Float score = ParseHtmlUtil.getElementsEqIndexText(tdEl, 2, Float.class);//预设总分
            	Float smallSp = ParseHtmlUtil.getElementsEqIndexText(tdEl, 3, Float.class);//小分

                SportDataBbBssPO po = new SportDataBbBssPO(sportAgainstInfoId, 
                		score, bigSp, smallSp, releaseTime);
        		if(!ObjectUtil.isBlank(bssLastPO) && po.isEquals(bssLastPO))
        			continue;
        		bssPOs.add(po);
        		bssLastPO = po;
            }

            //解析胜分差历史sp
            SportDataBbWsPO wsLastPO = sportDataBbWsDaoMapper.findLast(sportAgainstInfoId);
            for (int i = 3; i < wsEl.size(); i++) {
                Element el = wsEl.get(i);
                Elements tdEl = ParseHtmlUtil.getElementsByTag(el, "td");
                String time = ParseHtmlUtil.getElementsEqIndexText(tdEl, 0);
                if (StringUtils.isBlank(time))
                    continue;
            	Date releaseTime = DateUtil.convertStrToDate(time);
                if(!ObjectUtil.isBlank(wsLastPO) && !ObjectUtil.isBlank(wsLastPO.getReleaseTime()) && !ObjectUtil.isBlank(releaseTime))
                	if(DateUtil.compare(wsLastPO.getReleaseTime(), releaseTime) != -1)
                		continue;
            	
            	Float spFail15 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 1, Float.class);
            	Float spFail610 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 2, Float.class);
                Float spFail1115 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 3, Float.class);
                Float spFail1620 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 4, Float.class);
                Float spFail2125 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 5, Float.class);
                Float spFail26 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 6, Float.class);
                Float spWin15 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 7, Float.class);
                Float spWin610 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 8, Float.class);
                Float spWin1115 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 9, Float.class);
                Float spWin1620 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 10, Float.class);
                Float spWin2125 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 11, Float.class);
                Float spWin26 = ParseHtmlUtil.getElementsEqIndexText(tdEl, 12, Float.class);
    
                SportDataBbWsPO po = new SportDataBbWsPO(sportAgainstInfoId, spFail15, spFail610, 
                		spFail1115, spFail1620, spFail2125, spFail26,spWin15, spWin610, spWin1115, 
                		spWin1620, spWin2125, spWin26, releaseTime);
        		if(!ObjectUtil.isBlank(wsLastPO) && po.isEquals(wsLastPO))
        			continue;         
                wsPOs.add(po);
                wsLastPO = po;
            }
            
            if(wfPOs.size() > 0)
            	sportDataBbWfDaoMapper.insertBatch(wfPOs);
            if(bssPOs.size() > 0)
            	sportDataBbBssDaoMapper.insertBatch(bssPOs);
            if(wsPOs.size() > 0)
            	sportDataBbWsDaoMapper.insertBatch(wsPOs);
            
		} catch (Exception e) {
			log.error("获取单个对阵 html 历史sp异常, officialId : " + officialId + " sportAgainstInfoId " + sportAgainstInfoId + "message:" + e.getMessage());
		}
	}
	
	private void pushSp(Long sportAgainstInfoId, SportDataBbWfPO wfLastPO, SportDataBbWfPO letWfLastPO, SportDataBbBssPO bssLastPO, SportDataBbWsPO wsLastPO){
        //胜负/让分胜负变化
        String[] wf = new String[7];
        
        if(!ObjectUtil.isBlank(wfLastPO)){
        	wf[0] = NumberFormatUtil.format(wfLastPO.getSpWin());
        	wf[1] = NumberFormatUtil.format(wfLastPO.getSpFail());
        	wf[2] = DateUtil.convertDateToStr(wfLastPO.getReleaseTime());
        }else{
        	wf[0] = "";
        	wf[1] = "";
        	wf[2] = "";
        }
        if(!ObjectUtil.isBlank(letWfLastPO)){
        	wf[3] = NumberFormatUtil.format(letWfLastPO.getLetScore(), NumberFormatUtil.ONE_DECIMAL_POINT);
        	wf[4] = NumberFormatUtil.format(letWfLastPO.getSpWin());
        	wf[5] = NumberFormatUtil.format(letWfLastPO.getSpFail());
        	wf[6] = DateUtil.convertDateToStr(letWfLastPO.getReleaseTime());
        }else{
        	wf[3] = "";
        	wf[4] = "";
        	wf[5] = "";
         	wf[6] = "";
        }        

        
        //大小分变化
        String[] bs = new String[4];
        if(!ObjectUtil.isBlank(bssLastPO)){
        	bs[0] = NumberFormatUtil.format(bssLastPO.getPresetScore(), NumberFormatUtil.ONE_DECIMAL_POINT);
        	bs[1] = NumberFormatUtil.format(bssLastPO.getSpBig());
        	bs[2] = NumberFormatUtil.format(bssLastPO.getSpSmall());
        	bs[3] = DateUtil.convertDateToStr(bssLastPO.getReleaseTime());
        }else{
        	bs[0] = "";
        	bs[1] = "";
        	bs[2] = "";
        	bs[3] = "";        	
        }

        //让分差分变化
        String[] winArray = new String[7];
        String[] failArray = new String[7];
        if(!ObjectUtil.isBlank(wsLastPO)){
        	winArray[0] = NumberFormatUtil.format(wsLastPO.getSpWin15());
        	winArray[1] = NumberFormatUtil.format(wsLastPO.getSpWin610());
        	winArray[2] = NumberFormatUtil.format(wsLastPO.getSpWin1115());
        	winArray[3] =NumberFormatUtil.format(wsLastPO.getSpWin1620());
        	winArray[4] = NumberFormatUtil.format(wsLastPO.getSpWin2125());
        	winArray[5] = NumberFormatUtil.format(wsLastPO.getSpWin26());
        	winArray[6] = DateUtil.convertDateToStr(wsLastPO.getReleaseTime());
        	
        	failArray[0] = NumberFormatUtil.format(wsLastPO.getSpFail15());
        	failArray[1] = NumberFormatUtil.format(wsLastPO.getSpFail610());
        	failArray[2] = NumberFormatUtil.format(wsLastPO.getSpFail1115());
        	failArray[3] = NumberFormatUtil.format(wsLastPO.getSpFail1620());
        	failArray[4] = NumberFormatUtil.format(wsLastPO.getSpFail2125());
        	failArray[5] = NumberFormatUtil.format(wsLastPO.getSpFail26());
        	failArray[6] = DateUtil.convertDateToStr(wsLastPO.getReleaseTime());
        	
        }else{
        	winArray[0] = "";
        	winArray[1] = "";
        	winArray[2] = "";
        	winArray[3] = "";
        	winArray[4] = "";
        	winArray[5] = "";
        	winArray[6] = "";  
        	
        	failArray[0] = "";
        	failArray[1] = "";
        	failArray[2] = "";
        	failArray[3] = "";
        	failArray[4] = "";
        	failArray[5] = "";
        	failArray[6] = "";        	
        }

        Map<String, Object> ws = new HashMap<>();
        ws.put("win", winArray);
        ws.put("lost", failArray);
        //竞篮即时比分推送
        JclqTrendSpBO spBO = new JclqTrendSpBO(sportAgainstInfoId, wf, ws, bs);
        // 竞篮SP值变化推送
        JlSpMessageData jlSpMessageData = new JlSpMessageData();
        List<JclqTrendSpBO> list = new ArrayList<JclqTrendSpBO>();
        list.add(spBO);
        jlSpMessageData.setList(list);
        mqUtils.sendSpMessage(MQConstants.BASKETBALL_SP_RABBITMQ_NAME, jlSpMessageData);  
        log.info("竞篮sp值变化推送  " +MQConstants.BASKETBALL_SP_RABBITMQ_NAME + "jlSpMessageData : " + JSONObject.toJSONString(jlSpMessageData));		
	}
	
	public static void main(String[] args) {
		try {
			String json = "{\"data\":\"[]\"}";
			JSONObject jsonObj = JSONObject.parseObject(json);
			JSONObject data = jsonObj.getJSONObject("data");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
