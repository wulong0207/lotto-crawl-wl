
package com.hhly.crawlcore.v2.plugin.lottery.sport;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.common.MatchStatusEnum;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.JcMatchStatusEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @desc    
 * @author  cheng chen
 * @date    2017年11月14日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractSportCrawl extends AbstractCrawl {
	
	
	private static final Logger log  = LoggerFactory.getLogger(AbstractSportCrawl.class);
	
    @Autowired
    protected RedisUtil redisUtil;
    
    @Autowired
    protected MQUtils mqUtils;
    
    public AbstractSportCrawl(){
    	
    }
    
	public AbstractSportCrawl(LotteryIssueDaoMapper lotteryIssueMapper,
			SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper, SportTeamInfoService sportTeamInfoService,
			SportMatchInfoService sportMatchInfoService) {
		super(lotteryIssueMapper, sportAgainstInfoDaoMapper, sportTeamInfoService, sportMatchInfoService);
	}

	public void crawlHandle() throws Exception {
		getSaleMatchList();
		getHistorySp();
	}
	
	
	public void crawlMatch() throws Exception {
		getMatchList();
		getInterimMatchList();
		
	}
    
	/**
	 * 获取竞彩官网销售中的对阵
	 * 
	 * @date 2017年11月15日上午11:39:43
	 * @author cheng.chen
	 */
	protected abstract void getSaleMatchList() throws Exception;
	
	/**
	 * 处理单个对阵的历史sp
	 * @throws Exception
	 * @date 2017年11月24日下午3:15:33
	 * @author cheng.chen
	 */
	protected abstract void getSingleHistorySp(String officialId, Long sportAgainstInfoId) throws Exception;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 * @date 2017年11月23日下午2:27:07
	 * @author cheng.chen
	 */
	protected abstract String getAllMatchUrl() throws Exception;
	
	/**
	 * 获取竞彩官网暂定赛程
	 * @throws Exception
	 * @date 2017年11月22日上午11:49:04
	 * @author cheng.chen
	 */
	protected abstract String getInterimMatchUrl() throws Exception;
	
	
	/**
	 * 获取抓取彩种
	 * @return
	 * @throws Exception
	 * @date 2017年11月23日下午2:28:47
	 * @author cheng.chen
	 */
	protected abstract Integer getLotteryCode() throws Exception;
	
	/**
	 * 获取抓取类型
	 * @return
	 * @throws Exception
	 * @date 2017年11月23日下午2:28:36
	 * @author cheng.chen
	 */
	protected abstract Short getType() throws Exception;
	
	
	protected Long MatchHandle(JSONObject data, List<LotteryIssuePO> getIssueInfo) throws Exception {
		SportAgainstInfoPO po = null;
		//彩种编码
		Integer lotteryCode = getLotteryCode();
		//比赛类型
		Short type = getType();
		
		String officialId = data.getString("id"); // 官方id
		String saleDate = data.getString("b_date"); // 销售时间
		String officialMatchCode = data.getString("num");// 官方编号
		String date = data.getString("date");
		String time = data.getString("time");
		String matchTimeStr = date + " " + time;// 比赛时间
		Date matchTime = DateUtil.convertStrToDate(matchTimeStr);// 比赛时间
		Long matchId = data.getLong("l_id");// 赛事id
		String matchName = data.getString("l_cn");// 赛事全称
		String matchNameAbbr = data.getString("l_cn_abbr");// 赛事简称
		Long homeId = data.getLong("h_id");// 主队id
		String homeName = data.getString("h_cn");// 主队全称
		Long guestId = data.getLong("a_id");// 客队队id
		String guestName = data.getString("a_cn");// 客队全称
		String status = data.getString("status");// 销售状态
		String homeNameAbbr = data.getString("h_cn_abbr");  // 主队简称
		String guestNameAbbr = data.getString("a_cn_abbr"); // 客队简称
		String color = data.getString("l_background_color"); //对阵颜色

		String createBy = SportEnum.SportDataSource.JC_OFFICIAL.getName();
		
		String issueCode = DateUtil.convertDateToStr(DateUtil.convertStrToDate(saleDate, DateUtil.DATE_FORMAT),
				DateUtil.FORMAT_YYMMDD);
		Date saleEndTime = SportLotteryUtil.getSaleEndTime(matchTime, issueCode, getIssueInfo); // 销售截止时间

		String systemCode = SportLotteryUtil.getSystemCode(officialMatchCode, issueCode);
		
		po = sportAgainstInfoDaoMapper.findByCode(lotteryCode, issueCode, systemCode);
		
		//赛事已存在数据库, 只修改赛事状态和比赛时间
		if(!ObjectUtil.isBlank(po)){
			if(JcMatchStatusEnum.isUpdate(po.getMatchStatus()))
				return po.getId();
			po.setOfficialId(officialId);
			po.setStartTime(matchTime);
			po.setSaleEndTime(saleEndTime);
			po.setMatchStatus(Short.valueOf(SportLotteryUtil.getMatchStatus(status)));
			po.setModifyBy(createBy);
		}else{
			//赛事不存在, 新增赛事信息
			SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(homeName, homeNameAbbr, type, homeId, sportterySource);

			SportTeamSourceInfoPO guestSourcePO = new SportTeamSourceInfoPO(guestName, guestNameAbbr, type, guestId,
					sportterySource);

			SportMatchSourceInfoPO matchSourcePO = new SportMatchSourceInfoPO(matchName, matchNameAbbr, type, matchId,
					sportterySource);

			// 获取主队 客队 赛事id
			po = new SportAgainstInfoPO(null, lotteryCode, issueCode, null, null,
					null, null, null, null, officialMatchCode, systemCode,
					Short.valueOf(SportLotteryUtil.getMatchStatus(status)), officialId, matchTime, saleEndTime,
					createBy, createBy, DateUtil.convertStrToDate(saleDate, DateUtil.DATE_FORMAT));
			
			SportTeamInfoPO homeTeamPO = getTeamInfo(homeSourcePO);
			if(!ObjectUtil.isBlank(homeTeamPO)){
				po.setHomeTeamId(homeTeamPO.getId());
				po.setHomeName(homeTeamPO.getTeamFullName());
			}else{
				po.setHomeName(homeName);
			}
			SportTeamInfoPO awayTeamPO = getTeamInfo(guestSourcePO);
			if(!ObjectUtil.isBlank(awayTeamPO)){
				po.setVisitiTeamId(awayTeamPO.getId());
				po.setVisitiName(awayTeamPO.getTeamFullName());
			}else{
				po.setVisitiName(guestName);
			}
			SportMatchInfoPO leaguePO = getMatchInfo(matchSourcePO);
			if(!ObjectUtil.isBlank(leaguePO)){
				po.setSportMatchInfoId(leaguePO.getId());
				po.setMatchName(leaguePO.getMatchFullName());
			}else{
				po.setMatchName(matchName);
			}
		}
		
		sportAgainstInfoDaoMapper.merge(po);
		
//		String key = lotteryCode + SymbolConstants.UNDERLINE + issueCode + SymbolConstants.UNDERLINE + systemCode + SymbolConstants.UNDERLINE + matchTime.getTime();
//		id = (Long) redisUtil.getObj(key);
//		if(ObjectUtil.isBlank(id)){
//			id = sportAgainstInfoDaoMapper.findIdByCode(lotteryCode, issueCode, officialMatchCode);
//		}else{
//			return id;
//		}

		
		return po.getId();
	}

	/**
	 * 获取所有竞彩官网的对阵
	 * 
	 * @date 2017年11月14日下午4:18:43
	 * @author cheng.chen
	 * @throws Exception 
	 */
	private void getMatchList() throws Exception {
		
		String url = getAllMatchUrl();
		
		log.info("抓取sporttery对阵赛事信息  ---------------- start : " + url);

		String json = HTTPUtil.transRequest(url , connectTimeout, socketTimeout);
		
        if (json.equals("0")) {
            return;
        }

		String initData = StringUtils.stripEnd(StringUtils.stripStart(json, SymbolConstants.PARENTHESES_LEFT),
				SymbolConstants.PARENTHESES_RIGHT);
		JSONObject initJson = JsonUtil.jsonToObject(initData, JSONObject.class);
		JSONObject statusJson = initJson.getJSONObject("status");
		int code = statusJson.getInteger("code");
		String message = statusJson.getString("message");

		if (code != 8000 && !message.equals("OK")) {
			throw new ServiceRuntimeException("获取官网www.sporttery.cn的赛事集合异常!!!");
		}

		//通过彩种获取彩期信息
        List<LotteryIssuePO> getIssueInfo = lotteryIssueDaoMapper.getIssueInfo(getLotteryCode());
        
		JSONObject dataJson = initJson.getJSONObject("data");
		JSONArray listArr = dataJson.getJSONArray("list");
		
//		JSONObject leagueJson = dataJson.getJSONObject("league");
//		JSONObject dateJson = dataJson.getJSONObject("date");
		
		try {
			for (int i = 0; i < listArr.size(); i++) {
				JSONObject obj = listArr.getJSONObject(i);
				MatchHandle(obj, getIssueInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("抓取赛事结果信息 ---------------- end");
	};
	
	/**
	 * 获取竞彩官网历史sp值
	 * 
	 * @date 2017年11月14日下午4:17:11
	 * @author cheng.chen
	 */
	protected void getHistorySp() throws Exception{
		
        List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findSaleMatchList(getLotteryCode());

        if (ObjectUtil.isBlank(list)) {
            log.info("通过彩种编码查询竞彩官网id集合数据为null");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            final SportAgainstInfoPO po = list.get(i);
            
            ThreadPoolManager.execute("执行获取id : " + po.getId() + "历史sp值", new Runnable() {
				
				@Override
				public void run() {
					try {
						getSingleHistorySp(po.getOfficialId(), po.getId());
					} catch (Exception e) {
						log.error("抓取对阵历史sp值 异常 id : " + po.getId() + ", message : " + e.getMessage());
					}
				}
			});
        }
	}
	
	/**
	 * 获取竞彩官网暂定赛事集合
	 * 
	 * @date 2017年11月20日下午4:17:24
	 * @author cheng.chen
	 */
	private void getInterimMatchList() throws Exception{
		log.info("抓取暂定赛程信息  ------- start ");
		String url = getInterimMatchUrl();
		Integer lotteryCode = getLotteryCode();
        Document doc = null;
        try {
            doc = ParseHtmlUtil.getDocumentGBK(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements els = ParseHtmlUtil.getSelect(doc, ".match_list li");
        for (Element el : els) {
            Elements divEl = ParseHtmlUtil.getElementsByTag(el, "div");
            Elements matchNameEl = divEl.eq(0);
            Elements teamNameEl = divEl.eq(1);
            Elements matchTime = divEl.eq(2);

            String matchNameText = StringUtils.strip(matchNameEl.text()); //获取联赛名称

            if (StringUtils.isBlank(matchNameText)) {
                continue;
            }
            String matchTimeText = StringUtils.strip(matchTime.text());   //获取比赛时间
            Elements home = null;
            Elements guest = null;
            //竞足 主在前。竞蓝客在前
            if (lotteryCode == LotteryEnum.Lottery.FB.getName()) {
                home = ParseHtmlUtil.getSelect(teamNameEl, ".zhu"); //获取主队名称
                guest = ParseHtmlUtil.getSelect(teamNameEl, ".ke"); //获取客队名称
            } else {
                guest = ParseHtmlUtil.getSelect(teamNameEl, ".zhu"); //获取主队名称
                home = ParseHtmlUtil.getSelect(teamNameEl, ".ke"); //获取客队名称
            }

            String homeTeamText = StringUtils.strip(home.text());
            String guestTeamText = StringUtils.strip(guest.text());
            
            log.info("暂定赛程  主队名称: " + homeTeamText + ", 客队名称: " + guestTeamText);

            SportAgainstInfoPO po = new SportAgainstInfoPO(null, lotteryCode,
                    matchNameText,
                    homeTeamText,
                    guestTeamText,
                    Short.valueOf(String.valueOf(MatchStatusEnum.TENTATIVE.getCode())),
                    DateUtil.convertStrToDate(matchTimeText, DateUtil.DATETIME_FORMAT_NO_SEC),
                    SportDataSource.JC_OFFICIAL.getName(),
                    SportDataSource.JC_OFFICIAL.getName()
            );
            int flag = sportAgainstInfoDaoMapper.updateInterimMatch(po);
            if (flag == 0) {
                sportAgainstInfoDaoMapper.merge(po);
            }            
        }
        log.info("抓取暂定赛程信息  ------- start ");
	}
	
}
