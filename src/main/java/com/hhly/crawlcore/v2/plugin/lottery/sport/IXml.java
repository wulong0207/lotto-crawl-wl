package com.hhly.crawlcore.v2.plugin.lottery.sport;
/**
 * @desc xml与对象相互转换借口
 * @author jiangwei
 * @date 2017年12月19日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface IXml {
   /**
    * 转换为xml
    * @author jiangwei
    * @Version 1.0
    * @CreatDate 2017年5月11日 下午3:03:48
    * @return
    */
   String toXml();
   /**
    * 转换对象
    * @author jiangwei
    * @Version 1.0
    * @CreatDate 2017年5月18日 下午8:33:49
    * @param strXml
    * @return
    */
   <T> T fromXML(String xml);
}
