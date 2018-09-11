package com.hhly.crawlcore.sport.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSONArray;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.service.ClimbTeamInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.constants.CacheConstants;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;

/**
 * Created by chenkangning on 2017/9/1.
 */
@Service
public class ClimbTeamInfoServiceImpl implements ClimbTeamInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ClimbTeamInfoServiceImpl.class);

    @Value("${wubai_team_info_url}")
    String url;

    @Value("${teamSize}")
    int teamSize;

   /* @Resource
    private TeamTmpMapper teamTmpMapper;*/

    @Resource
    private RedisUtil redisUtil;
//    private List<Integer> fbErrorList;
//    private List<Integer> bbErrorList = new ArrayList<>();

    /**
     * redis足球错误ID KEY
     */
    private static final String FB_ERROR_LIST_KEY = "FB_ERROR_LIST_KEY";

    @Resource
    private SportTeamInfoService sportTeamInfoService;


    @Override
    public void climbTeamInfo() throws Exception {
        final List<Integer> fbErrorList = redisUtil.getObj(FB_ERROR_LIST_KEY, new ArrayList<Integer>());
        ThreadPoolManager.execute("500足球球队信息抓取", new Runnable() {
            @Override
            public void run() {

            	Short matchType = SportEnum.getMatchType("足球");
                String createBy = "500.com";
                for (int i = 1; i <= teamSize; i++) {
                    logger.debug("500足球球队信息抓取正常:>>>>>>>>" + i);
                    if (processFB(matchType, createBy, i)) {
                        continue;
                    }
                }
                if (!ObjectUtil.isBlank(fbErrorList)) {
                    processErrorList(matchType, createBy, fbErrorList);
                }
            }
        });


    }

    private void processErrorList(short matchType, String createBy, List<Integer> fbErrorList) {
        Iterator<Integer> iterator = fbErrorList.iterator();//183
        while (iterator.hasNext()) {
            int i = iterator.next();
            if (!processFB(matchType, createBy, i)) {
                //返回false，表示执行成功，没有出错，删除errorList里面对应的值，再重新迭代
                iterator.remove();
                if (!fbErrorList.contains(i)) {
                    redisUtil.addString(FB_ERROR_LIST_KEY, JSONArray.toJSONString(fbErrorList), null);
                    processErrorList(matchType, createBy, fbErrorList);
                    logger.debug("500足球球队信息抓取异常:<<<<<<<<<<<<" + i);
                }
            }
        }
    }

    private boolean processFB(Short matchType, String createBy, int i) {
        List<String> ipStrs = redisUtil.getObj(CacheConstants.C_CRAWL_PROXY_IP, new ArrayList<String>());
        List<Integer> fbErrorList = redisUtil.getObj(FB_ERROR_LIST_KEY, new ArrayList<Integer>());
        String string;
        if (ipStrs != null) {
            Document doc;
            try {
                doc = ParseHtmlUtil.getDocumentGBK(MessageFormat.format(url, i));
            } catch (Exception e) {
                logger.error("500网球队信息抓取异常:" + e.getMessage());
//                string = redisUtil.getString(FB_ERROR_LIST_KEY);
                if (ObjectUtil.isBlank(fbErrorList)) {
                    fbErrorList = new ArrayList<>();
                }
                if (!fbErrorList.contains(i)) {
                    fbErrorList.add(i);//记录出错ID
                    redisUtil.addObj(FB_ERROR_LIST_KEY, fbErrorList, null);
                }
                return true;
            }
            //执行正常逻辑
            try {
                climbTeam(matchType, createBy, i, doc);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void climbTeam(Short matchType, String createBy, int i, Document doc) throws UnsupportedEncodingException {
        String fullName = ParseHtmlUtil.getSelect(doc, ".lsnav_qdnav_name").text();
        if (!StringUtil.isBlank(fullName)) {
            String shortName = ParseHtmlUtil.getSelect(doc, ".lcur_chart_zj p b").text();
            String homeTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(fullName + matchType).getBytes("UTF-8"));
            SportTeamSourceInfoPO sourceInfoPO = new SportTeamSourceInfoPO(fullName, shortName, matchType, Long.valueOf(i), SportDataSource.FIVEHUNDRED_OFFICIAL.getValue());
            sportTeamInfoService.getTeamInfo(sourceInfoPO);
            logger.debug("500网球队信息抓取,全称【" + fullName + "】，简称【" + shortName + "】》>>>>>>>>>" + i);
        }
    }


    @Override
    public void climbTeamInfoBB() throws Exception {
       /* String url = "http://liansai.500.com/lq/404/team/{0,number,#}/";
        for (int i = 1; i < 1567; i++) {
            if (processBB(url, i)) continue;
        }

        if (!ObjectUtil.isBlank(bbErrorList)) {
            System.out.println("fberrorList.size>>>>>>>>>>>>>>>>>>>>>>>>" + bbErrorList.size());
            Iterator<Integer> iterator = bbErrorList.iterator();
            while (iterator.hasNext()) {
                int i = iterator.next();
                processBB(url, i);
                iterator.remove();
            }
        }*/

    }

    private boolean processBB(String url, int i) {
       /* Document doc;
        try {
            doc = ParseHtmlUtil.getDocument(MessageFormat.format(url, i));
        } catch (Exception e) {
            bbErrorList.add(i);
            return true;
        }
        System.out.println("*******************" + i);
        processBBTeam(doc, i);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return false;
    }

    private void processBBTeam(Document doc, int i) {
        /*TeamTmp teamTmp;
        String fullNameCN = ParseHtmlUtil.getSelect(doc, ".d-list li").eq(0).text();
        String fullNameEN = ParseHtmlUtil.getSelect(doc, ".d-list li").eq(4).text();
        if (!StringUtil.isBlank(fullNameCN)) {
            teamTmp = new TeamTmp();
            teamTmp.setFullNameCn(fullNameCN.split("：")[1]);
            if (fullNameEN.split("：").length >= 2) {
                teamTmp.setFullNameEn(fullNameEN.split("：")[1]);
            }
            teamTmp.setNum(i);
            teamTmpMapper.insert(teamTmp);
        }*/

    }
}
