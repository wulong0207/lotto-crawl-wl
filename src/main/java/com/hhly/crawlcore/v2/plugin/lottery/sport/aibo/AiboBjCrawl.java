
package com.hhly.crawlcore.v2.plugin.lottery.sport.aibo;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.sport.aiboInterface.constant.AiboConstant.SellingEnum;
import com.hhly.crawlcore.sport.util.ParseHtmlUtil;
import com.hhly.crawlcore.v2.plugin.AbstractCrawl;
import com.hhly.crawlcore.v2.sport.entity.bj.AbBjMatchInfo;
import com.hhly.skeleton.base.util.DateUtil;

import us.codecraft.xsoup.Xsoup;

/**
 * @desc
 * @author cheng chen
 * @date 2017年12月13日
 * @company 益彩网络科技公司
 * @version 1.0
 */
@Service
public class AiboBjCrawl extends AbstractCrawl {
	
	private static final Logger log = LoggerFactory.getLogger(AiboBjCrawl.class);

	@Value("${ab_bj_get_sale_play_list}")
	String ab_bj_get_sale_play_list;

	@Value("${ab_bj_get_all_match}")
	String ab_bj_get_all_match;

	@Override
	public void crawlHandle() throws Exception {

		Document document = ParseHtmlUtil.getDocumentGBK(ab_bj_get_sale_play_list);
		List<String> list = Xsoup.select(document, "//selling/w").list();
		for (String palyInfo : list) {
			String palyId = Xsoup.select(palyInfo, "//w/@li").get();
			String issueCode = Xsoup.select(palyInfo, "//w/@is").get();
			SellingEnum sellingEnum = SellingEnum.getValueByKey(palyId);
			if (null != sellingEnum) {
				dataHandle(issueCode, sellingEnum);
			}
		}
	}

	@Override
	public void crawlMatch() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void dataHandle(String... params) throws Exception {
		// TODO Auto-generated method stub

	}

	private void dataHandle(String issueCode, SellingEnum sellingEnum) throws Exception {
		Document document = ParseHtmlUtil.getDocumentGBK(MessageFormat.format(ab_bj_get_all_match, sellingEnum.getValue() + issueCode));
		List<String> list = Xsoup.select(document, "//w/*").list().subList(0, 1);
		Map<Integer, AbBjMatchInfo> map = new HashMap<>();
		switch (sellingEnum) {
        case WDF:
            log.info("北单爱波对接处理【胜平负】数据start>>>>>>>>>>>>>" + list.size());
            wdf(list, map);
            break;
        case GOAL:
            log.info("北单爱波对接处理【总进球数】数据start>>>>>>>>>>>>>" + list.size());
//            goal(document, list, issueCode);
            break;
        case SINGLE_DOUBLE:
            log.info("北单爱波对接处理【上下盘单双数】数据start>>>>>>>>>>>>>" + list.size());
//            sxdf(document, list, issueCode);
            break;
        case SCORE:
            log.info("北单爱波对接处理【单场比分】数据start>>>>>>>>>>>>>" + list.size());
//            dcbf(document, list, issueCode);
            break;
        case HF_WDF:
            log.info("北单爱波对接处理【半全场胜平负】数据start>>>>>>>>>>>>>" + list.size());
//            bqcf(document, list, issueCode);
            break;
        case DOWN_SCORE:
            log.info("北单爱波对接处理【下半场比分】数据start>>>>>>>>>>>>>" + list.size());
            break;
        case WF:
            log.info("北单爱波对接处理【胜负过关】数据start>>>>>>>>>>>>>" + list.size());
//            sfgg(document, list, issueCode);
            break;
		}
	}
	
	private void wdf(List<String> list, Map<Integer, AbBjMatchInfo> map){
		Integer lotteryCode = 306;
		
		int num = 1;
		for (String matchStr : list) {
			AbBjMatchInfo matchInfo = MatchHandle(map, matchStr, lotteryCode, num);	
			
			num++;
		}
	}
	
	private AbBjMatchInfo MatchHandle(Map<Integer, AbBjMatchInfo> map, String matchStr, Integer lotteryCode, int num){
		
		if(map.containsKey(num))
			return map.get(num);
		
		AbBjMatchInfo matchInfo = new AbBjMatchInfo();
		
		matchInfo.setLotteryCode(lotteryCode);
		//主队全称
		matchInfo.setHomeName(Xsoup.select(matchStr, "//w" + num + "/@h").get());
		//主队简称
		matchInfo.setHomeAbbrName(Xsoup.select(matchStr, "//w" + num + "/@hf").get());
		//客队全称
		matchInfo.setAwayName(Xsoup.select(matchStr, "//w" + num + "/@a").get());
		//客队简称
		matchInfo.setAwayAbbrName(Xsoup.select(matchStr, "//w" + num + "/@af").get());
		//赛事简称
		matchInfo.setLeagueAbbrName(Xsoup.select(matchStr, "//w" + num + "/@gn").get());
		//比赛时间
		matchInfo.setStartTime(DateUtil.convertStrToDate(Xsoup.select(matchStr, "//w" + num + "/@gt").get(),
				DateUtil.DATETIME_FORMAT_NO_SEC));
		//让分 暂无
		map.put(num, matchInfo);
		return matchInfo;
	}
	
	public static void main(String[] args) {
		String str = "<w1 id=\"13003506\" c1=\"2.321519\" c2=\"0\" h=\"日本U23\" hf=\"日本\" a=\"朝鲜U23\" af=\"朝鲜\" htn=\"\" atn=\"\" r=\"-0.5\" gt=\"2018-01-16 16:00\" gn=\"足球\" gno=\"2430\" gnt=\"b\" gn1=\"\" gn2=\"小组赛\" gn_=\"U23亚锦赛\" st=\"4\" sr=\"3:1\" rt=\"胜\" sp=\"2.321519\" pc=\"\" ub=\"\" pb=\"\" hc=\"\" ac=\"\" hr=\"\" ar=\"\" hl=\"\" al=\"\" gl=\"\" cr=\"\" cl=\"\" ct=\"\" et=\"2018-01-16 15:55\" fet=\"2018-01-16 15:55\" cc=\"\" mud=\"2018-01-18 11:39\" spud=\"2018-01-16 15:55\" dt=\"0001-01-01 00:00\" rev=\"0\" />";
		String id = Xsoup.select(str, "//body//*").get();
		System.out.println(id);
	}

}
