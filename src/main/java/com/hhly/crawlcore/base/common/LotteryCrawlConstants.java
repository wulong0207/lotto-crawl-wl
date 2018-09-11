package com.hhly.crawlcore.base.common;

import java.util.ArrayList;
import java.util.List;

import com.hhly.skeleton.base.common.LotteryEnum.Lottery;

public class LotteryCrawlConstants {
	/**
	 * 需要排序的彩种编码集合
	 */
	public static final List<Integer> sortList = new ArrayList<>();

	/**
	 * 不需要处理开奖时间的高频彩
	 */
	public static final List<Integer> unMergeLotteryTimeList = new ArrayList<Integer>();

	/**
	 * 从抓取获取彩期日期，而不从彩期格式获取
	 */
	public static final List<Integer> useIssueDateFormSource = new ArrayList<Integer>();

	static {
		sortList.add(Lottery.GXK3.getName());
		sortList.add(Lottery.AHK3.getName());
		sortList.add(Lottery.JLK3.getName());
		sortList.add(Lottery.JSK3.getName());
		sortList.add(Lottery.JXK3.getName());
		sortList.add(Lottery.QHK3.getName());
		sortList.add(Lottery.HBK3.getName());
		sortList.add(Lottery.SHK3.getName());
		sortList.add(Lottery.BJK3.getName());
		sortList.add(Lottery.FJK3.getName());
		sortList.add(Lottery.HNK3.getName());
		sortList.add(Lottery.GSK3.getName());
		sortList.add(Lottery.HEBK3.getName());
		sortList.add(Lottery.GZK3.getName());
		sortList.add(Lottery.NMGK3.getName());
		sortList.add(Lottery.XZGK3.getName());
		sortList.add(Lottery.HEBHY2.getName());
		sortList.add(Lottery.HEBHY3.getName());
		sortList.add(Lottery.HEB20X5.getName());
		sortList.add(Lottery.SSQ.getName());
		sortList.add(Lottery.DLT.getName());
		sortList.add(Lottery.QLC.getName());
		//
		unMergeLotteryTimeList.add(Lottery.BJKL8.getName());
		unMergeLotteryTimeList.add(Lottery.PK10.getName());
		//
		useIssueDateFormSource.add(Lottery.GXKL10.getName());// 广西快乐10
	}

	public static String processDrawDetail(int lotteryCode) {
		Lottery lottery = Lottery.getLottery(lotteryCode);
		String str;
		switch (lottery) {
		case SSQ:
			str = "一等奖|||0,二等奖|||0,三等奖||3000|0,四等奖||200|0,五等奖||10|0,六等奖||5|0";
			break;
		case QLC:
			str = "一等奖|||0,二等奖|||0,三等奖|||0,四等奖||200|0,五等奖||50|0,六等奖||10|0,七等奖||5|0,特别奖||5|0";
			break;
		case DLT:
			str = "一等奖||||,二等奖||||,三等奖||||,四等奖||200||100,五等奖||10||5,六等奖||5|0|0";
			break;
		case PL5:
			str = "直选||100000|0";
			break;
		case PL3:
		case F3D:
			str = "直选||1040|0,组三||346|0,组六||173|0";
			break;
		case QXC:
			str = "一等奖|||0,二等奖|||0,三等奖||1800|0,四等奖||300|0,五等奖||20|0,六等奖||5|0";
			break;
		case CQSSC:
		case JXSSC:
		case XJSSC:
		case YNSSC:
		case TJSSC:
			// 时时彩
			str = "五星直选|100000|0,五星通选|20440|0,三星直选|1000|0,三星组三|320|0,三星组六|160|0,二星直选|100|0,二星组选|50|0,一星|10|0,大小单双|4|0";
			break;
		case D11X5:
		case HB11X5:
		case JS11X5:
		case JX11X5:
		case LN11X5:
		case SD11X5:
		case XJ11X5:
		case GZ11X5:
		case SH11X5:
		case YN11X5:
		case NMG11X5:
		case BJ11X5:
		case JL11X5:
		case SC11X5:
		case TJ11X5:
		case NX11X5:
		case AH11X5:
		case SX11X5:
		case GX11X5:
		case HL11X5:
		case ZJ11X5:
		case GS11X5:
		case FJ11X5:
		case QH11X5:
		case HLJ11X5:
		case HEB11X5:
		case SHX11X5:
			// 11选5
			str = "任二|6|0,任三|19|0,任四|78|0,任五|540|0,任六|90|0,任七|26|0,任八|9|0,前一|13|0,前二直选|130|0,前二组选|65|0,前三直选|1170|0,前三组选|195|0,乐二一等|201|0,乐二二等|71|0,乐二三等|6|0,乐三一等|1384|0,乐三二等|214|0,乐三三等|19|0,乐四一等|154|0,乐四二等|19|0,乐五一等|1080|0,乐五二等|90|0";
			break;
		case DKL10:
		case CQKL10:
		case TJKL10:
		case HNKL10:
		case HLJKL10:
		case GXKL10:
		case YNKL10:
		case SXKL10:
		case SHXKL10:
			// 快乐10分
			str = "选一数投|25|0,选一红投|5|0,任选二|8|0,选二连组|31|0,选二连直|62|0,任选三|24|0,选三前组|1300|0,选三前直|8000|0,任选四|80|0,任选五|320|0";
			break;
		case GXK3:
		case AHK3:
		case JLK3:
		case JSK3:
		case JXK3:
		case SHK3:
		case HEBK3:
		case HBK3:
		case GSK3:
		case FJK3:
		case GZK3:
		case BJK3:
		case NMGK3:
		case QHK3:
		case HNK3:
			// 快3
			str = "和值3|240|0,和值4|80|0,和值5|40|0,和值6|25|0,和值7|16|0,和值8|12|0,和值9|10|0,和值10|9|0,和值11|9|0,和值12|10|0,和值13|12|0,和值14|16|0,和值15|25|0,和值16|40|0,和值17|80|0,和值18|240|0,三同号通选|40|0,三同号单选|240|0,三不同号|40|0,三连号通选|10|0,二同号复选|15|0,二同号单选|80|0,二不同号|8|0";
			break;
		case ZC6:
		case JQ4:
		case ZC_NINE:
			str = "一等奖|||0";
			break;
		case SFC:
			str = "一等奖|||0,二等奖|||0";
			break;
		case SDPOKER:
			str = "同花包选|22|0,同花单选|90|0,同花顺包选|535|0,同花顺单选|2150|0,顺子包选|33|0,顺子单选|400|0,豹子包选|500|0,豹子单选|6400|0,对子包选|7|0,对子单选|88|0,任选一|5|0,任选二|33|0,任选三|116|0,任选四|46|0,任选五|22|0,任选六|12|0";
			break;
		case SHSSL:
			// 时时乐
			str = "直选|980|0,组三|320|0,组六|160|0,前二|98|0,后二|98|0,前一|10|0,后一|10|0";
			break;
		case BJKL8:
			// 北京快乐8
			str = "选8|20000|0,选7|8000|0,选6|600|0,选5|250|0,选4|40|0,选3|30|0,选2|16|0,选1|4|0";
			break;
		case SCHKL12:
		case LNKL12:
		case ZHJKL12:
			str = "选前一|14|0,任选二|7|0,选前二组选|77|0,选前二直选|155|0,任选三|25|0,选前三组选|255|0,选前三直选|1550|0,任选四|115|0,任选五|930|0,任选六|155|0,任选七|44|0,任选八|16|0";
			break;
		case SXYTDJ:
			// 山西泳坛夺金
			str = "任选一|8|0,任选二|64|0,任选三|512|0,任选四|4096|0,选四组选24|170|0,选四组选12|341|0,选四组选6|682|0,选四组选4|1024|0";
			break;
		case SDQYH:
			// 山东群英会
			str = "任选十|71|0,任选九|142|0,任选八|320|0,任选七|855|0,任选六|3000|0,任选五中五|10000|0,任选五中四|50|0,任选五中三|4|0,任选四中四|820|0,任选四中三|10|0,任选三中三|57|0,任选三中二|5|0,任选二|22|0,任选一|4|0,围选五中五|10000|0,围选五中四|300|0,围选五中三|28|0,围选四中四|5000|0,围选四中三|38|0,围选三中三|1000|0,围选三中二|18|0,围选二中二|130|0,围选二中一|5|0,顺选三中三|7700|0,顺选三中二|12|0,顺选二中二|350|0,顺选二中一|5|0,顺选一|23|0";
			break;
		case CQBBWP:
			// 重庆百变王牌
			str = "任选二|5|0,任选三|15|0,任选四|48|0,任选五|222|0,同花任二|21|0,同花任三|58|0,同花任四|195|0,同花任五|888|0,前一|14|0,前二直选|160|0,前二组选|80|0,前三直选|1780|0,前三组选|296|0,同花前一|54|0,同花前二直选|640|0,同花前二组选|320|0,同花前三直选|7120|0,同花前三组选|1184|0,同花任五通选(同花)|700|0,同花任五通选(不同花)|60|0,前二组通选(前二)|52|0,前二组通选(任二)|2|0,前三直通选(前三)|1480|0,前三直通选(任二)|10|0,前三组通选(前三)|200|0,前三组通选(任三)|50|0";
			break;
		case HNXYSC:
			str = "前一|0|0,前二|0|0,前三|0|0,组二|0|0,组三|0|0,大小奇偶|0|0,颜一|0|0,颜二|0|0,颜三|0|0";
			break;
		case BJKZC:
			str = "一等奖||0,二等奖|5000|0,三等奖|500|0,四等奖|100|0,五等奖|50|0,六等奖|10|0,七等奖|5|0,八等奖|2|0";
			break;
		case HENKY481:
			str = "任选一|9|0,任选二|74|0,任选三|593|0,豹子|163|0,直选|4751|0,通选中4|3350|0,通选中3|50|0,组选4|1187|0,组选6|791|0,组选12|395|0,组选24|197|0,组4形态|21|0,组6形态|28|0";
			break;
		default:
			str = "";
			break;
		}
		return str;
	}

	/**
	 * 判断是否需要合并开奖时间
	 * 
	 * @param lotteryCode
	 * @return
	 */
	public static boolean isUnMergeLotteryTime(Integer lotteryCode) {
		return unMergeLotteryTimeList.contains(lotteryCode);
	}

	/**
	 * 判断是否从抓取获取彩期日期
	 * 
	 * @param lotteryCode
	 * @return
	 */
	public static boolean isUseIssueDateFormSource(Integer lotteryCode) {
		return useIssueDateFormSource.contains(lotteryCode);
	}
}
