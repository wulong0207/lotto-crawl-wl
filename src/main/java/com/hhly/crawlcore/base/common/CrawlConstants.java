package com.hhly.crawlcore.base.common;

/**
 * @desc 项目公用常量
 * @author jiangwei
 * @date 2017年4月1日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class CrawlConstants {
	/**
	 * 配置文件名
	 */
	public static final String APPLICATION = "application.properties";

	/**
	 * 开奖公告数据抓取彩果合并字典编号
	 */
	public static final String ISSUE_DRAW_CODE_MERGE_DIC = "9995";

	/**
	 * 开奖公告彩果合并报警
	 */
	public static final Integer WARN_DRAW_CODE_MERGE = 39;

	/**
	 * 开奖公告数据抓取彩果合并入库备注
	 */
	public static final String ISSUE_DRAW_CODE_MERGE_REMARK = "开奖公告数据抓取入库";

	/**
	 * 默认创建者
	 */
	public static final String DEFAULT_CREATE_BY = "lotto-crawl";
}
