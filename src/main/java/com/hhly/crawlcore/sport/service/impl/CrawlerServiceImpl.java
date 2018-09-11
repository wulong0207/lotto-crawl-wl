package com.hhly.crawlcore.sport.service.impl;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.zeroc.WebClientManager;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportMatchInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportTeamInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.sport.common.MatchStatusEnum;
import com.hhly.crawlcore.sport.entity.YbfJcBbMatch;
import com.hhly.crawlcore.sport.entity.YbfJcFbMatch;
import com.hhly.crawlcore.sport.service.CrawlerService;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.HTTPUtil;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.LotteryEnum.Lottery;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @auth lgs on
 * @date 2017/2/13.
 * @desc 爬取数据具体实现类
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {
	
	private static final Logger log = LoggerFactory.getLogger(CrawlerServiceImpl.class);

    @Autowired
    private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

    @Autowired
    private SportTeamInfoDaoMapper sportTeamInfoDaoMapper;

    @Autowired
    private SportMatchInfoDaoMapper sportMatchInfoDaoMapper;

    @Autowired
    private WebClientManager webClientManager;

    @Autowired
    private LotteryIssueDaoMapper lotteryIssueMapper;

    @Autowired
    private SportTeamInfoService sportTeamInfoService;

    @Autowired
    private SportMatchInfoService sportMatchInfoService;

    @Resource
    private MQUtils mqUtils;

    @Override
    public List<SportAgainstInfoPO> crawlerMatchInterim(String url, Integer lotteryCode, String matchType) {
        Document doc = null;
        try {
            doc = ParseHtmlUtil.getDocumentGBK(url + "?=" + new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements els = ParseHtmlUtil.getSelect(doc, ".match_list li");
        List<SportAgainstInfoPO> pos = new ArrayList<>();
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
            if (lotteryCode.equals(String.valueOf(LotteryEnum.Lottery.FB.getName()))) {
                home = ParseHtmlUtil.getSelect(teamNameEl, ".zhu"); //获取主队名称
                guest = ParseHtmlUtil.getSelect(teamNameEl, ".ke"); //获取客队名称
            } else {
                guest = ParseHtmlUtil.getSelect(teamNameEl, ".zhu"); //获取主队名称
                home = ParseHtmlUtil.getSelect(teamNameEl, ".ke"); //获取客队名称
            }

            String homeTeamText = StringUtils.strip(home.text());
            String guestTeamText = StringUtils.strip(guest.text());

            SportAgainstInfoPO po = new SportAgainstInfoPO(null, lotteryCode,
                    matchNameText,
                    homeTeamText,
                    guestTeamText,
                    Short.valueOf(String.valueOf(MatchStatusEnum.TENTATIVE.getCode())),
                    DateUtil.convertStrToDate(matchTimeText, DateUtil.DATETIME_FORMAT_NO_SEC),
                    SportDataSource.JC_OFFICIAL.getName(),
                    SportDataSource.JC_OFFICIAL.getName()
            );
            pos.add(po);
        }
        return pos;
    }

    public String getMatchListJson(String url) throws Exception {
        String json = HTTPUtil.transRequest(url);
        if (json.equals("0") || json.contains("error")) {
            json = getMatchListJson(url);
        }
        return json;
    }

    @Override
    public List<SportAgainstInfoPO> crawlerWxMatchList(String url, Integer lotteryCode, String matchType) {
        String json = null;
        try {
            json = getMatchListJson(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String initData = StringUtils.stripEnd(StringUtils.stripStart(json, "initData("), SymbolConstants.PARENTHESES_RIGHT);
        JSONObject initJson = JsonUtil.jsonToObject(initData, JSONObject.class);
        JSONObject statusJson = initJson.getJSONObject("status");
        int code = statusJson.getInteger("code");
        String message = statusJson.getString("message");

        if (code != 8000 && !message.equals("ok")) {
            return null;
        }
        //获取彩期信息
        List<LotteryIssuePO> getIssueInfo = lotteryIssueMapper.getIssueInfo(Integer.valueOf(lotteryCode));

        JSONObject dataJson = initJson.getJSONObject("data");
        JSONArray listArr = dataJson.getJSONArray("list");
        JSONObject leagueJson = dataJson.getJSONObject("league");
//        JSONObject dateJson = dataJson.getJSONObject("date");
        List<SportAgainstInfoPO> againstInfoPOs = new ArrayList<>();
        try {
            for (int i = 0; i < listArr.size(); i++) {
                JSONObject obj = listArr.getJSONObject(i);
                String officialId = obj.getString("id"); //官方id
                String officialMatchCode = obj.getString("num");//官方编号
                String date = obj.getString("date");
                String time = obj.getString("time");
                String matchTimeStr = date + " " + time;//比赛时间
                Date matchTime = DateUtil.convertStrToDate(matchTimeStr);//比赛时间
                //Long matchId = obj.getLong("l_id");//赛事id
                String matchName = obj.getString("l_cn");//赛事全称
                String matchNameAbbr = obj.getString("l_cn_abbr");//赛事简称
                //Long homeId = obj.getLong("h_id");//主队id
                String homeName = obj.getString("h_cn");//主队全称
                //Long guestId = obj.getLong("a_id");//客队队id
                String guestName = obj.getString("a_cn");//客队全称
                String status = obj.getString("status");//销售状态
                String saleDate = obj.getString("b_date"); //销售时间
                String homeNameAbbr = obj.getString("h_cn_abbr");//赛事简称
                String guestNameAbbr = obj.getString("a_cn_abbr");//赛事简称


                //获取球队唯一md5值
                String homeTeamUnion = null;
                String guestTeamUnion = null;
                String matchUnion = null;
                try {
                    homeTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(homeName + matchType).getBytes("UTF-8"));
                    guestTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(guestName + matchType).getBytes("UTF-8"));
                    matchUnion = DigestUtils.md5DigestAsHex(String.valueOf(matchName + matchType).getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String createBy = SportEnum.SportDataSource.JC_OFFICIAL.getName();
                //获取球队信息
                SportTeamInfoPO homeTeam = new SportTeamInfoPO(Short.valueOf(matchType), homeName, null, homeTeamUnion, createBy, createBy);
                SportTeamInfoPO guestTeam = new SportTeamInfoPO(Short.valueOf(matchType), guestName, null, guestTeamUnion, createBy, createBy);
                SportMatchInfoPO matchInfo = new SportMatchInfoPO(matchName, matchNameAbbr, Short.valueOf(matchType), matchUnion, createBy);


                homeTeam = sportTeamInfoService.findTeam(homeTeam);
                guestTeam = sportTeamInfoService.findTeam(guestTeam);
                matchInfo  = sportMatchInfoService.findMatch(matchInfo);

                String issueCode = DateUtil.convertDateToStr(DateUtil.convertStrToDate(saleDate, DateUtil.DATE_FORMAT), DateUtil.FORMAT_YYMMDD);
                Date saleEndTime = SportLotteryUtil.getSaleEndTime(matchTime, issueCode, getIssueInfo); //销售截止时间

                String systemCode = SportLotteryUtil.getSystemCode(officialMatchCode, issueCode);

                SportAgainstInfoPO po = new SportAgainstInfoPO(null,lotteryCode,
                        issueCode,
                        matchInfo.getId(),
                        homeTeam.getId(),
                        guestTeam.getId(),
                        matchName,
                        homeName,
                        guestName,
                        officialMatchCode,
                        systemCode,
                        Short.valueOf(SportLotteryUtil.getMatchStatus(status)),
                        officialId,
                        matchTime,
                        saleEndTime,
                        createBy,
                        createBy,
                        DateUtil.convertStrToDate(saleDate, DateUtil.DATE_FORMAT)
                );
                againstInfoPOs.add(po);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return againstInfoPOs;
    }

    /**
     * 对接一比分竞彩数据
     *
     * @param type
     * @return
     */
    @Override
    public void crawlerYbfJczqMatch() {
        List<SportAgainstInfoPO> pos = sportAgainstInfoDaoMapper.findSaleMatchList(Lottery.FB.getName());
        Map<String, String> dateMap = new HashMap<>();
        Map<String, SportAgainstInfoPO> posMap = new HashMap<>();
        //获取对阵时间
        for (int i = 0; pos != null && pos.size() > 0 && i < pos.size(); i++) {
            SportAgainstInfoPO po = pos.get(i);
            String str = DateUtil.convertDateToStr(po.getSaleEndTime(), DateUtil.DATE_FORMAT);
            dateMap.put(str, str);
            posMap.put(po.getOfficialId(), po);
        }

        /*根据比赛时间对接数据*/
        List<YbfJcFbMatch> ybfJcMatches = new ArrayList<>();
        for (Map.Entry<String, String> entry : dateMap.entrySet()) {
            String key = entry.getKey();
            String result = webClientManager.getJcFbMatchByDate(key);
            if (!StringUtils.isBlank(result)) {
                ybfJcMatches.addAll(JsonUtil.jsonToArray(result, YbfJcFbMatch.class));
            }
        }

        List<SportAgainstInfoPO> updatePos = new ArrayList<>();
        List<SportMatchInfoPO> updateMatchPos = new ArrayList<>();
        List<SportTeamInfoPO> updateTeamPos = new ArrayList<>();
        //完善插入数据的
        for (YbfJcFbMatch ybf : ybfJcMatches) {
            SportAgainstInfoPO po = posMap.get(ybf.getGwId());

            if (ObjectUtil.isBlank(po)) {
                continue;
            }

            //对阵信息
            updatePos.add(new SportAgainstInfoPO(ybf));
            //赛事信息
            if (!ObjectUtil.isBlank(po.getSportMatchInfoId())) {
                updateMatchPos.add(new SportMatchInfoPO(po.getSportMatchInfoId(), ybf.getLeagueId()));
            }
            //主队信息
            if (!ObjectUtil.isBlank(po.getHomeTeamId())) {
                updateTeamPos.add(new SportTeamInfoPO(po.getHomeTeamId(), ybf.getHomeTeamId(), ybf.getHomeOrder()));
            }
            //客队信息
            if (!ObjectUtil.isBlank(po.getVisitiTeamId())) {
                updateTeamPos.add(new SportTeamInfoPO(po.getVisitiTeamId(), ybf.getGuestTeamId(), ybf.getGuestOrder()));
            }

        }
        if (updatePos.size() > 0) {
            sportAgainstInfoDaoMapper.updateMatchInfoByOfficialId(updatePos);
        }
        if (updateMatchPos.size() > 0) {
            sportMatchInfoDaoMapper.updateBatch(updateMatchPos);
        }
        if (updateTeamPos.size() > 0) {
            sportTeamInfoDaoMapper.updateBatch(updateTeamPos);
        }
    }

	@Override
	public void crawlerYbfJclqMatch() {
        List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findSaleMatchList(Lottery.BB.getName());
        log.debug("同步一比分数据 list size ===== " + list.size());
        for (SportAgainstInfoPO po : list) {
            String result = webClientManager.getJcBbMatchByDate(DateUtil.convertDateToStr(po.getStartTime()));
            List<YbfJcBbMatch> ybfJclqMatchList = JsonUtil.jsonToArray(result, YbfJcBbMatch.class);
            //1.根据彩种301和official_id更新对阵信息
            SportAgainstInfoPO queryPo;
            SportAgainstInfoPO againstInfoPO;
            SportTeamInfoPO homeTeam;
            SportTeamInfoPO visitiTeam;
            List<SportTeamInfoPO> teamInfoPOList = new ArrayList<>();
            String issue;
            if (!ObjectUtil.isBlank(ybfJclqMatchList)) {
                for (YbfJcBbMatch ybfJclqMatch : ybfJclqMatchList) {
                    issue = DateUtil.convertStrToTarget(ybfJclqMatch.getLotteryDate(), DateUtil.DATE_FORMAT, DateUtil.FORMAT_YYMMDD);//170822
                    queryPo = new SportAgainstInfoPO();
                    queryPo.setLotteryCode(LotteryEnum.Lottery.BB.getName());
                    queryPo.setOfficialMatchCode(ybfJclqMatch.getLotteryNumber());
                    queryPo.setIssueCode(issue);
                    //根据彩期、官方赛事编码、彩种查询出唯一一条赛事
                    List<SportAgainstInfoPO> dataList = sportAgainstInfoDaoMapper.findCondition(queryPo);
                    if (!ObjectUtil.isBlank(dataList)) {
                        againstInfoPO = dataList.get(0);
                        againstInfoPO.setMatchAnalysisUrl(ybfJclqMatch.getAnalysisId().toString());
                        againstInfoPO.setMatchInfoUrl(ybfJclqMatch.getMatchAdvicesId());
                        sportAgainstInfoDaoMapper.update(againstInfoPO);

                        //更新赛事
                        SportMatchInfoPO matchInfoPO = new SportMatchInfoPO();
                        matchInfoPO.setId(againstInfoPO.getSportMatchInfoId());
                        matchInfoPO.setMatchDataUrl(ybfJclqMatch.getMatchDataId());
                        sportMatchInfoDaoMapper.update(matchInfoPO);

                        //更新球队
                        //主队
                        homeTeam = new SportTeamInfoPO();
                        homeTeam.setId(againstInfoPO.getHomeTeamId());
                        homeTeam.setTeamOrder(ybfJclqMatch.getHomeRank());
                        homeTeam.setTeamDataUrl(ybfJclqMatch.getHomeDataId());
                        teamInfoPOList.add(homeTeam);

                        //客队
                        visitiTeam = new SportTeamInfoPO();
                        visitiTeam.setId(againstInfoPO.getVisitiTeamId());
                        visitiTeam.setTeamOrder(ybfJclqMatch.getGuestRank());
                        visitiTeam.setTeamDataUrl(ybfJclqMatch.getGuestDataId());
                        teamInfoPOList.add(visitiTeam);
                    }
                }
            }


            if (!ObjectUtil.isBlank(teamInfoPOList)) {
                sportTeamInfoDaoMapper.updateBatch(teamInfoPOList);
            }

        }
		
	}

}
