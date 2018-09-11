
package com.hhly.crawlcore.base.enums;

/**
 * @desc
 * @author cheng chen
 * @date 2017年12月21日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class FbLotteryEnum {

	/**
	 * FhFbLotteryEnum
	 * @desc  500网竞彩足球子玩法
	 * @author  cheng chen
	 * @date    2018年3月15日
	 * @company 益彩网络科技公司
	 * @version 1.0
	 */
	public enum FhFbLotteryEnum {
	
		WDF("nspf", "胜平负"), LET_WDF("spf", "让球胜平负"), GOAL("jqs", "总进球"), HF_WDF("bqc", "半全场胜平负"), SCORE("bf", "比分");

		private String code;

		private String name;

		private FhFbLotteryEnum(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
	}
	
	public enum StFbWcEnum {
		
		CHP(104895, 467130, "chp", "冠军", 308), FNL(104895, 467131, "fnl", "冠亚军", 309);
		
		/**
		 * 彩种id
		 */
		private Integer id;
		/**
		 * 玩法id
		 */
		private Integer pId;
		/**
		 * 玩法类型
		 */
		private String type;
		/**
		 * 玩法名称
		 */
		private String name;
		
		/**
		 * 彩种编码
		 */
		private Integer lotteryCode;
		
		private StFbWcEnum(Integer id, Integer pId, String type, String name, Integer lotteryCode) {
			this.id = id;
			this.pId = pId;
			this.type = type;
			this.name = name;
			this.lotteryCode = lotteryCode;
		}
		
		public Integer getId() {
			return id;
		}


		public Integer getpId() {
			return pId;
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public Integer getLotteryCode() {
			return lotteryCode;
		}

		public void setLotteryCode(Integer lotteryCode) {
			this.lotteryCode = lotteryCode;
		}
	}
}
