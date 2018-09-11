package com.hhly.crawlcore.sport.util;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.NumberUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgs on 2017/1/5.
 * 解析html Util类
 */
public class ParseHtmlUtil {

    public static Document getDocument(String url) throws Exception {
        return Jsoup.parse(HTTPUtil.transRequest(url));
    }

    public static Document getDocumentGBK(String url) throws Exception {
        return Jsoup.parse(HTTPUtil.transRequest(url, "GBK"));
    }

    public static Elements getElementsByClass(Document doc, String className) {
        return doc.getElementsByClass(className);
    }

    public static Elements getElementsByClass(Element ele, String className) {
        return ele.getElementsByClass(className);
    }

    public static Element getElementById(Document doc, String id) {
        return doc.getElementById(id);
    }

    public static Elements getElementsByTag(Element e, String tagName) {
        return e.getElementsByTag(tagName);
    }

    public static Elements getElementsByTag(Document e, String tagName) {
        return e.getElementsByTag(tagName);
    }

    public static Elements getSelect(Document doc, String cssQuery) {
        return doc.select(cssQuery);
    }

    public static Elements getSelect(Elements el, String selector) {
        return el.select(selector);
    }

    public static Elements getSelect(Element el, String selector) {
        return el.select(selector);
    }


    /**
     * 获取Elements.eq(index) 的值
     * @param el Elements
     * @param index 索引
     * @return 返回值
     */
    public static String getElementsEqIndexText(Elements el, int index) {
        return el.eq(index).text();
    }

    /**
     * 获取Elements eq(index) 的值 并且转换成功数字类型
     * @param el 获取Elements
     * @param index index
     * @param clazz 数值类型的class 例如Double.class
     * @param <T> Double.class
     * @return 返回数值类型
     */
    public static <T extends Number> T getElementsEqIndexText(Elements el, int index,Class<T> clazz) {
        String text = el.eq(index).text();
        if(StringUtils.isBlank(text)){
            return null;
        }

        return NumberUtils.parseNumber(text,clazz);
    }

    /**
     * 获取JavaScript变量的值
     * @param doc doc文档名称
     * @param index 第几个JavaScript索引
     * @return 变量值
     */
    public static Map<String,String> getJavaScriptData(Document doc,int index){
        Map<String,String> resultMap = new HashMap<>();
         /*取得script下面的JS变量*/
        Elements e = doc.getElementsByTag("script").eq(index);

        /*循环遍历script下面的JS变量*/
        for (Element element : e) {

            /*取得JS变量数组*/
            String[] data = element.data().toString().split("var");

            /*取得单个JS变量*/
            for(String variable : data){

                /*过滤variable为空的数据*/
                if(variable.contains("=")){

                    /*取到满足条件的JS变量*/
                    if(variable.contains("obj_match_info")){

                        String[]  kvp = variable.split("=");

                        /*取得JS变量存入map*/
                        if(!resultMap.containsKey(kvp[0].trim())){
                            resultMap.put(kvp[0].trim(), kvp[1].trim().substring(0, kvp[1].trim().indexOf(";")).toString());
                        }
                    }
                }
            }
        }
        return resultMap;
    }
}
