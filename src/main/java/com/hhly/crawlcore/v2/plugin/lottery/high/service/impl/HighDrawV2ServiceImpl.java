package com.hhly.crawlcore.v2.plugin.lottery.high.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.hhly.crawlcore.base.common.LotteryCrawlConstants;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueSourceDaoMapper;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssueSourcePO;
import com.hhly.crawlcore.persistence.lottery.po.LotteryTypePO;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.v2.plugin.lottery.high.service.HighDrawV2Service;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.common.LotteryEnum;
import com.hhly.skeleton.base.common.LotterySourceEnum;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.exception.ServiceRuntimeException;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.LotUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;
import com.hhly.skeleton.lotto.base.dic.bo.DicDataDetailBO;
import com.hhly.skeleton.task.lottery.vo.LotteryVO;

/**
 * @author chengkangning
 * @version 1.0
 * @desc
 * @date 2017/11/16
 * @company 益彩网络科技公司
 */
@Service
public class HighDrawV2ServiceImpl implements HighDrawV2Service {

    private static final Logger logger = LoggerFactory.getLogger(HighDrawV2ServiceImpl.class);

    @Resource
    private LotteryIssueSourceDaoMapper lotteryIssueSourceDaoMapper;

    @Resource
    private LotteryTypeDaoMapper lotteryTypeDaoMapper;

    @Resource
    private LotteryIssueDaoMapper lotteryIssueMapper;

    @Resource
    private MQUtils mqUtils;

    @Override
    public void insert(LotteryIssueSourcePO lotteryIssueSourcePO) throws Exception {

        String finalIssueCode = processIssueCode(lotteryIssueSourcePO);
        if(ObjectUtil.isBlank(finalIssueCode)) {
        	return;
        }
        LotteryVO lotteryVO = LotteryVO.newVO(lotteryIssueSourcePO.getLotteryCode(), finalIssueCode, lotteryIssueSourcePO.getSource());
        List<LotteryIssueSourcePO> resultList = lotteryIssueSourceDaoMapper.selectCondition(lotteryVO);
        if (ObjectUtil.isBlank(resultList)) {
            //特殊参数处理
            setSpeccialParam(lotteryIssueSourcePO, finalIssueCode);
            lotteryIssueSourceDaoMapper.insertSelective(lotteryIssueSourcePO);
        }
    }

    /**
     * 特殊参数处理
     *
     * @param lotteryIssueSourcePO 对象
     * @throws Exception Exception
     */
    private void setSpeccialParam(LotteryIssueSourcePO lotteryIssueSourcePO, String issueCode) throws Exception {
        lotteryIssueSourcePO.setIssueStatus((short) Constants.NUM_3);

        //高频彩或是500万的全国彩
        lotteryIssueSourcePO.setIssueCode(issueCode);

        //设置drawDetail
        if (lotteryIssueSourcePO.getLotteryType().equals(Constants.NUM_1)) {
            String drawDetail = LotteryCrawlConstants.processDrawDetail(lotteryIssueSourcePO.getLotteryCode());
            lotteryIssueSourcePO.setDrawDetail(drawDetail);
        }
        if(LotterySourceEnum.Source.WUBAI.getValue().equals(lotteryIssueSourcePO.getSource())) {
        	//500w 天津快乐十分多一个号码，去掉
        	if(LotteryEnum.Lottery.TJKL10.getName() == lotteryIssueSourcePO.getLotteryCode() && !ObjectUtil.isBlank(lotteryIssueSourcePO.getDrawCode())) {
        		String[] drawCodeArr = LotUtil.split(lotteryIssueSourcePO.getDrawCode());
        		if(drawCodeArr.length > 8) {
        			drawCodeArr = Arrays.copyOf(drawCodeArr, 8);
        			lotteryIssueSourcePO.setDrawCode(StringUtil.join(drawCodeArr, SymbolConstants.COMMA));
        		}
        	}
        }

        //draw_code排序，升序
        if (LotteryCrawlConstants.sortList.contains(lotteryIssueSourcePO.getLotteryCode())) {
            logger.debug("彩果排序:" + lotteryIssueSourcePO.getLotteryCode() + "【" + lotteryIssueSourcePO.getDrawCode() + "】");
            specialDrawCode(lotteryIssueSourcePO);
        }

    }

    /**
     * 彩果排序
     *
     * @param lotteryIssueSourcePO 参数对象
     */
    private void specialDrawCode(LotteryIssueSourcePO lotteryIssueSourcePO) {
        StringBuffer redBuffer = new StringBuffer();
        StringBuffer blueBuffer = new StringBuffer();
        String[] strings = StringUtils.tokenizeToStringArray(lotteryIssueSourcePO.getDrawCode(), SymbolConstants.VERTICAL_BAR);//先按|分割
        String redStrings[] = StringUtils.tokenizeToStringArray(strings[0], SymbolConstants.COMMA);//红球再按,分割
        Arrays.sort(redStrings);
        String blueStrings[];
        String finalDrawCode;
        for (int i = 0; i < redStrings.length; i++) {
            redBuffer.append(redStrings[i]);
            if (i != redStrings.length - 1) {
                redBuffer.append(SymbolConstants.COMMA);
            }
        }
        if (strings.length > 1) {
            //如果蓝球不为空
            blueStrings = StringUtils.tokenizeToStringArray(strings[1], SymbolConstants.COMMA);//蓝球再按,分割
            Arrays.sort(blueStrings);
            for (int i = 0; i < blueStrings.length; i++) {
                if (i == 0) {
                    blueBuffer.append(SymbolConstants.VERTICAL_BAR);
                }
                blueBuffer.append(blueStrings[i]);
                if (i != blueStrings.length - 1) {
                    blueBuffer.append(SymbolConstants.COMMA);
                }
            }
        }
        finalDrawCode = redBuffer.append(blueBuffer).toString();
        lotteryIssueSourcePO.setDrawCode(finalDrawCode);
    }

    /**
     * 统一处理彩期格式
     *
     * @param lotteryIssueSourcePO lotteryIssueSourcePO
     * @return String
     * @throws Exception Exception
     */
    private String processIssueCode(LotteryIssueSourcePO lotteryIssueSourcePO) throws Exception {
        LotteryTypePO newTypeBO = lotteryTypeDaoMapper.findTypeUseAddIssue(lotteryIssueSourcePO.getLotteryCode());
        if(newTypeBO == null) {
			logger.debug("彩种不存在:" + lotteryIssueSourcePO.getLotteryCode());
        	return null;
        }
        if (!ObjectUtil.isBlank(newTypeBO) && !StringUtil.isBlank(newTypeBO.getFormat())) {
            //1.获取彩种表里面format格式后面xxx有几位
            int digit = StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "X");
            //2.根据彩种表里规定的彩期格式格式化抓到的数据
            String issuePrefixStyle = null;//彩期格式前缀
            if (!StringUtil.isBlank(newTypeBO.getFormat())) {
                issuePrefixStyle = newTypeBO.getFormat().substring(0, newTypeBO.getFormat().length() - digit);//彩期格式前缀
                issuePrefixStyle = issuePrefixStyle.replaceAll("m", "M");//把所有小写m替换成大写
            }
            assert issuePrefixStyle != null;
            String prefixIssue = "";//各平台抓到的彩期前缀
            if (lotteryIssueSourcePO.getIssueCode().length() < newTypeBO.getFormat().length()) {
                prefixIssue = lotteryIssueSourcePO.getIssueCode().substring(0, issuePrefixStyle.length());//各平台抓到的彩期前缀
            } else {
                //如果抓回来彩期长度大于彩期格式长度
                if (lotteryIssueSourcePO.getIssueCode().startsWith(DateUtil.getNow(DateUtil.DATE_FORMAT_NO_LINE).substring(0, 2))) {
                    //如果是当前年份前两位开头
                    if (StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "Y") == 4
                            && StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "M") == 2
                            && StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "D") == 2) {
                        //并且彩期样式y是4位,m有2位,d有2位，直接取传进来彩期前8位
                        //yyyymmdd
                        prefixIssue = lotteryIssueSourcePO.getIssueCode().substring(0, 8);
                    }
                    if (StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "Y") == 2
                            && StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "M") == 2
                            && StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "D") == 2) {
                        prefixIssue = lotteryIssueSourcePO.getIssueCode().substring(0, 8);
                    }
                    if (StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "Y") == 4
                            && StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "M") == 0
                            && StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "D") == 0) {
                        prefixIssue = lotteryIssueSourcePO.getIssueCode().substring(0, 4);
                    }
                    if (StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "Y") == 2
                            && StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "M") == 0
                            && StringUtils.countOccurrencesOf(newTypeBO.getFormat().toUpperCase(), "D") == 0) {
                        prefixIssue = lotteryIssueSourcePO.getIssueCode().substring(0, 4);
                    }
                }
                //prefixIssue = lotteryIssueSourcePO.getIssueCode().substring(0, lotteryIssueSourcePO.getIssueCode().length() - digit);//各平台抓到的彩期前缀
            }
            String formatIusse = unifiedIssue(prefixIssue, issuePrefixStyle);
            String suffix = lotteryIssueSourcePO.getIssueCode().substring(prefixIssue.length());//后缀
          /*  if (lotteryIssueSourcePO.getIssueCode().length() < newTypeBO.getFormat().length()) {
                suffix = lotteryIssueSourcePO.getIssueCode().substring(issuePrefixStyle.length());//后缀
            } else {
                suffix = lotteryIssueSourcePO.getIssueCode().substring(lotteryIssueSourcePO.getIssueCode().length() - digit);//后缀
            }*/
            Integer suffixInt = Integer.parseInt(suffix);
            Integer maxDigitIssue = (int) (Math.pow(10, digit) - 1);
            if(suffixInt > maxDigitIssue) {
                logger.error("彩种：【" + newTypeBO.getLotteryName() + "-" + newTypeBO.getLotteryCode() + "】彩期格式小于抓取彩期格式长度！");
                throw new ServiceRuntimeException("彩种：【" + newTypeBO.getLotteryName() + "-" + newTypeBO.getLotteryCode() + "】彩期格式小于抓取彩期格式长度！");
            }
            return formatIusse + String.format("%0" + digit + "d", suffixInt);
        } else {
            logger.error("彩种：【" + lotteryIssueSourcePO.getLotteryName() + "-" + lotteryIssueSourcePO.getLotteryCode() + "】未设置彩期格式！");
            throw new ServiceRuntimeException("彩种：【" + lotteryIssueSourcePO.getLotteryName() + "-" + lotteryIssueSourcePO.getLotteryCode() + "】未设置彩期格式！");
        }
    }

    /**
     * 统一彩期格式
     *
     * @param formatStr 各平台抓到的彩期
     * @param style     彩种表里规定的彩期格式
     * @return string
     * @throws Exception exception
     */
    private String unifiedIssue(String formatStr, String style) throws Exception {
        SimpleDateFormat simpleDateFormat;
        if (StringUtil.isBlank(style)) {
            return "";
        }
        if (formatStr.length() > 6
                && (StringUtils.countOccurrencesOf(style.toUpperCase(), "Y") == 4 || StringUtils.countOccurrencesOf(style.toUpperCase(), "Y") == 2)
                && StringUtils.countOccurrencesOf(style.toUpperCase(), "M") == 2
                && StringUtils.countOccurrencesOf(style.toUpperCase(), "D") == 2
                ) {
            simpleDateFormat = new SimpleDateFormat(DateUtil.DATE_FORMAT_NO_LINE);
        } else if (formatStr.length() == 4
                && StringUtils.countOccurrencesOf(style.toUpperCase(), "Y") == 4
                && StringUtils.countOccurrencesOf(style.toUpperCase(), "M") == 0
                && StringUtils.countOccurrencesOf(style.toUpperCase(), "D") == 0
                ) {
            simpleDateFormat = new SimpleDateFormat(DateUtil.FORMAT_YYYY);
        } else if (formatStr.length() == 4
                && StringUtils.countOccurrencesOf(style.toUpperCase(), "Y") == 2
                && StringUtils.countOccurrencesOf(style.toUpperCase(), "M") == 0
                && StringUtils.countOccurrencesOf(style.toUpperCase(), "D") == 0
                ) {
            simpleDateFormat = new SimpleDateFormat(DateUtil.FORMAT_YY);
        } else {
            simpleDateFormat = new SimpleDateFormat(DateUtil.FORMAT_YYMMDD);
        }

        Date date = simpleDateFormat.parse(formatStr);

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(style);
        return simpleDateFormat2.format(date);
    }


    /**
     * 报警
     * 检查当天报警信息里面是否包含此条报警内容，包含的话就不处理，不包含才发送报警mq
     *
     * @param alarmInfo 例：彩种、彩期
     * @throws Exception Exception
     */
    private void warning(String alarmInfo) throws Exception {
       /* int count = lotteryIssueSourceDaoMapper.queryAlarmInfo(Constants.NUM_39, alarmInfo);
        if (count <= 0) {

        }*/
        String content = alarmInfo + "彩果不一致！";
        mqUtils.sendWarnMessage(content, Constants.NUM_39);
    }

    /**
     * 根据彩种、彩期数据合并
     *
     * @throws Exception Exception
     */
    @Override
    public void mergeDraw() throws Exception {
        if (!processLotteryTime()) {
            return;
        }
        //1.查询出需要合并的彩种编码
        List<String> lotteryCodeList = selectLotteryCodes();
        List<Integer> localLotteryCods = new ArrayList<>();//地方彩彩种编码集合
        List<Integer> highLotteryCods = new ArrayList<>();//高频彩彩种编码集合
        for (String lotterCode : lotteryCodeList) {

            LotteryTypePO newTypeBO = lotteryTypeDaoMapper.findTypeUseAddIssue(Integer.valueOf(lotterCode));
            if (newTypeBO.getLotteryCategory() == Constants.NUM_2) {
                //高频彩逻辑
                highLotteryCods.add(Integer.valueOf(lotterCode));
            } else if (newTypeBO.getLotteryCategory() == Constants.NUM_4) {
                //地方彩
                localLotteryCods.add(Integer.valueOf(lotterCode));
            }
        }
        if (!ObjectUtil.isBlank(highLotteryCods)) {
            mergeHigh(highLotteryCods);
        }
        if (!ObjectUtil.isBlank(localLotteryCods)) {
            mergeLocal(localLotteryCods);
        }
    }

    private Boolean processLotteryTime() throws Exception {
        return processLotteryTimeFF() && processLotteryTimeCP2Y();
    }

    private Boolean processLotteryTimeCP2Y() throws Exception {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return processLotteryTime(Constants.NUM_4);
            }
        };
        Future<Boolean> future = ThreadPoolManager.submit("彩票2元高频彩开奖时间处理", callable);
        return future.get();
    }

    private Boolean processLotteryTimeFF() throws Exception {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return processLotteryTime(Constants.NUM_2);
            }
        };
        Future<Boolean> future = ThreadPoolManager.submit("非丹高频彩开奖时间处理", callable);
        return future.get();
    }

    /**
     * 地方彩数据合并
     *
     * @param localLotteryCods 地方彩彩种编码集合
     * @throws Exception Exception
     */
    private void mergeLocal(List<Integer> localLotteryCods) throws Exception {
        Map<Integer, List<String>> warningMap = new HashMap<>();
        //1.查询出彩果不同的数据
        List<LotteryIssueSourcePO> warningList = lotteryIssueSourceDaoMapper.selectWarningLocal(localLotteryCods);
        logger.debug("地方彩报警数据：" + JSONArray.toJSON(warningList));
        warning(warningMap, warningList);
        //2.合并正常数据
        for (Integer lotteryCode : localLotteryCods) {
            List<LotteryIssueSourcePO> synList = lotteryIssueSourceDaoMapper.synLocalData(lotteryCode, warningMap.get(lotteryCode));
            processSynData(synList);
            processStatus(lotteryCode);
        }
    }

    private void processSynData(List<LotteryIssueSourcePO> synList) {
        LotteryIssuePO queryPO;
        for (LotteryIssueSourcePO sourcePO : synList) {
            queryPO = new LotteryIssuePO();
            queryPO.setLotteryCode(sourcePO.getLotteryCode());
            queryPO.setIssueCode(sourcePO.getIssueCode());
            LotteryIssuePO issueBO = lotteryIssueMapper.findSingle(queryPO);
            if (ObjectUtil.isBlank(issueBO)) {
                LotteryIssuePO issuePO = insertLotteryIssue(sourcePO);
                updateCallBack(sourcePO, issuePO);
            }
        }
    }


    private void processStatus(Integer lotteryCode) throws Exception {
        //先更新其它数据为否
        lotteryIssueMapper.updateOtherData(lotteryCode);
        //写入完成后再把数据库最后一条设置为上一期，是最新开奖
        lotteryIssueMapper.updateLastStatus(lotteryCode);
    }

    /**
     * 高频彩逻辑
     *
     * @param highLotteryCods 高频彩彩种编码集合
     * @throws Exception Exception
     */
    private void mergeHigh(List<Integer> highLotteryCods) throws Exception {
        logger.debug("高频彩数据合并开始：-------" + DateUtil.getNow());
        Map<Integer, List<String>> warningMap = new HashMap<>();
        //2.查询出有不同彩果的数据
        List<LotteryIssueSourcePO> warningList = lotteryIssueSourceDaoMapper.selectWarningData(highLotteryCods);
        logger.debug("高频彩报警数据：" + JSONArray.toJSON(warningList));
        warning(warningMap, warningList);
        for (Integer lotteryCode : highLotteryCods) {
            List<LotteryIssueSourcePO> synList = lotteryIssueSourceDaoMapper.selectSynData(lotteryCode, warningMap.get(lotteryCode));
            processSynData(synList);
            processStatus(lotteryCode);
        }
        logger.error("高频彩数据合并结束：-------" + DateUtil.getNow());
    }

    private LotteryIssuePO insertLotteryIssue(LotteryIssueSourcePO sourcePO) {
        LotteryIssuePO issuePO = new LotteryIssuePO();
        issuePO.setLotteryCode(sourcePO.getLotteryCode());
        issuePO.setIssueCode(sourcePO.getIssueCode());
        issuePO.setLotteryName(sourcePO.getLotteryName());
        issuePO.setLotteryTime(sourcePO.getLotteryTime());
        issuePO.setCurrentIssue((short) Constants.NUM_3);
        issuePO.setSaleStatus((short) Constants.NUM_5);
        issuePO.setSalesAmount(sourcePO.getSalesAmount() == null ? 0L : sourcePO.getSalesAmount());
        issuePO.setJackpotAmount(sourcePO.getJackpotAmount());
        issuePO.setDrawCode(sourcePO.getDrawCode());
        issuePO.setDrawDetail(sourcePO.getDrawDetail());
        issuePO.setIssueLastest((short) Constants.NUM_0);
        issuePO.setRemark("非凡彩乐对接彩果入库");
        lotteryIssueMapper.insert(issuePO);
        return issuePO;
    }

    private void updateCallBack(LotteryIssueSourcePO sourcePO, LotteryIssuePO issuePO) {
        //回写lottery_issue_source表issue_id
        LotteryIssueSourcePO updatePO = new LotteryIssueSourcePO();
        updatePO.setLotteryCode(sourcePO.getLotteryCode());
        updatePO.setIssueCode(sourcePO.getIssueCode());
        updatePO.setIssueId(issuePO.getId());
        lotteryIssueSourceDaoMapper.updateByExampleSelective(updatePO);
    }

    /**
     * 报警，并把对应彩种彩期数据放到issueCodes里面
     *
     * @param warningMap  warningMap
     * @param warningList warningList
     * @throws Exception Exception
     */
    private void warning(Map<Integer, List<String>> warningMap, List<LotteryIssueSourcePO> warningList) throws Exception {
        if (!ObjectUtil.isBlank(warningList)) {
            //这里表示各来源有不一样的数据，报警！
            for (LotteryIssueSourcePO lotteryIssueSourcePO : warningList) {
                List<String> issueCodes = warningMap.get(lotteryIssueSourcePO.getLotteryCode());
                if (ObjectUtil.isBlank(issueCodes)) {
                    issueCodes = new ArrayList<>();
                    warningMap.put(lotteryIssueSourcePO.getLotteryCode(), issueCodes);
                }
                issueCodes.add(lotteryIssueSourcePO.getIssueCode());
                warning(lotteryIssueSourcePO.getLotteryCode() + "-" + lotteryIssueSourcePO.getIssueCode());
            }
        }
    }

    /**
     * 查询出需要合并的彩种编码
     *
     * @return 需要合并的彩种编码
     * @throws Exception Exception
     */
    private List<String> selectLotteryCodes() throws Exception {
        List<String> lotteryCodeList = new ArrayList<>();
        List<DicDataDetailBO> list = lotteryIssueSourceDaoMapper.selectLotteryCode();
        for (DicDataDetailBO dicDataDetailBO : list) {
            lotteryCodeList.add(dicDataDetailBO.getDicDataValue());
        }
        return lotteryCodeList;
    }


    /**
     * 处理开奖时间
     * @param source 数据来源,1:爱彩乐;2:非凡彩票;3:500网;4:彩票2元网
     * @throws Exception Exception
     */
    @Override
    public Boolean processLotteryTime(Integer source) throws Exception {
        LotteryVO lotteryVO;
        List<LotteryIssueSourcePO> lotteryIssueSourcePOS;
        List<String> lotteryCodeList = selectLotteryCodes();
        int week = DateUtil.dayForWeek();
        for (String lotterCode : lotteryCodeList) {
            LotteryTypePO newTypeBO = lotteryTypeDaoMapper.findTypeUseAddIssue(Integer.valueOf(lotterCode));//彩种信息
            if (newTypeBO.getLotteryCategory() == Constants.NUM_2) {

                //只处理高频彩的，地方彩不需要处理时间
                //[1, 09:00, 2, 09:00, 3, 09:00, 4, 09:00, 5, 09:00, 6, 09:00, 7, 09:00]
                String[] startSaleTime = StringUtils.tokenizeToStringArray(newTypeBO.getStartSailTime(), SymbolConstants.VERTICAL_BAR + SymbolConstants.COMMA);
                if (startSaleTime == null) {
                    continue;
                }
                Map<Integer, String> startSaleTimeMap = new HashMap<>();//{1=09:00, 2=09:00, 3=09:00, 4=09:00, 5=09:00, 6=09:00, 7=09:00}
                for (int i = 0; i < startSaleTime.length; i++) {
                    startSaleTimeMap.put(Integer.valueOf(startSaleTime[i]), startSaleTime[i + 1]);
                    i++;
                }

                if (NumberUtils.parseNumber(lotterCode, Integer.class) == LotteryEnum.Lottery.CQKL10.getName()) {
                    //重庆幸运农场开奖时间特殊特殊处理
                    processLotteryTimeSpecialLotto(newTypeBO, source, startSaleTimeMap.get(week));
                } else {
                    String sailDayCycle;
                    if (!StringUtil.isBlank(newTypeBO.getStartSailTime())) {
                        sailDayCycle = StringUtils.tokenizeToStringArray(newTypeBO.getSailDayCycle(), SymbolConstants.VERTICAL_BAR)[1];
                    } else {
                        logger.error("彩种：【" + newTypeBO.getLotteryName() + "-" + newTypeBO.getLotteryCode() + "】销售日销售周期为空！");
                        continue;
                        //throw new ServiceRuntimeException("彩种：【" + newTypeBO.getLotteryName() + "-" + newTypeBO.getLotteryCode() + "】销售日销售周期为空！");
                    }
                    //1.找出今天的开奖数据
                    lotteryVO = LotteryVO.newVO(Integer.valueOf(lotterCode), "", source);
                    lotteryVO.setLotteryTime(new Date());//随便设个值，直接用数据库时间查询
                    lotteryVO.setOrderSql("issue_code ASC");
                    lotteryIssueSourcePOS = lotteryIssueSourceDaoMapper.selectForLotteryTime(lotteryVO);
                    for (int i = 0; i < lotteryIssueSourcePOS.size(); i++) {
                        LotteryIssueSourcePO lotteryIssueSourcePO = lotteryIssueSourcePOS.get(i);
                        String time = startSaleTimeMap.get(week);
                        String formTime = DateUtil.convertDateToStr(lotteryIssueSourcePO.getLotteryTime(), DateUtil.DATE_FORMAT) + " " + time;
                        Date date = DateUtil.convertStrToDate(formTime, DateUtil.DATETIME_FORMAT_NO_SEC);
                        Date date2 = DateUtil.addSecond(date, Integer.valueOf(sailDayCycle) * (i + 1));

                        lotteryIssueSourcePO.setLotteryTime(DateUtil.addSecond(date2, 60));
                        lotteryIssueSourceDaoMapper.updateByExampleSelective(lotteryIssueSourcePO);
                    }
                }
            }
        }
        return Boolean.TRUE;
    }

    private void processLotteryTimeSpecialLotto(LotteryTypePO newTypeBO, int source, String suffixTime) throws Exception {
        String str[] = StringUtils.tokenizeToStringArray(newTypeBO.getSailDayCycle(), SymbolConstants.COMMA);//,分割
        int[][] cycle = new int[str.length][3];
        //[[1,13,600],[14,14,28800],[15,97,600]]
        for (int i = 0; i < str.length; i++) {
            String range[] = StringUtils.tokenizeToStringArray(str[i], SymbolConstants.TRAVERSE_SLASH + SymbolConstants.VERTICAL_BAR);//-|分割
            int[] cyc = new int[3];
            cyc[0] = NumberUtils.parseNumber(range[0], Integer.class);
            cyc[1] = NumberUtils.parseNumber(range[1], Integer.class);
            cyc[2] = NumberUtils.parseNumber(range[2], Integer.class);
            cycle[i] = cyc;
        }
        //1.找出今天的开奖数据
        LotteryVO lotteryVO = LotteryVO.newVO(newTypeBO.getLotteryCode(), "", source);
        //lotteryVO.setLotteryTime(new Date());//随便设个值，直接用数据库时间查询
        lotteryVO.setOrderSql("issue_code ASC");
        List<LotteryIssueSourcePO> lotteryIssueSourcePOS = lotteryIssueSourceDaoMapper.selectForLotteryTime(lotteryVO);
        for (int i = 0; i < lotteryIssueSourcePOS.size(); i++) {
            LotteryIssueSourcePO lotteryIssueSourcePO = lotteryIssueSourcePOS.get(i);
            String formTime = DateUtil.convertDateToStr(DateUtil.getDateDit(lotteryIssueSourcePO.getLotteryTime(), -1), DateUtil.DATE_FORMAT) + " " + suffixTime;
            Date date = DateUtil.convertStrToDate(formTime, DateUtil.DATETIME_FORMAT_NO_SEC);
            Date date2 = null;
            int row = i + 1;
            for (int[] aCycle : cycle) {
                if (aCycle[1] >= row && row >= aCycle[0]) {
                    if (row >= 1 && row < 14) {
                        date2 = DateUtil.addSecond(date, aCycle[2] * (i + 1));//1-13
                    } else {
                        //14期以后
                        date2 = DateUtil.addSecond(DateUtil.addSecond(lotteryIssueSourcePOS.get(i - 1).getLotteryTime(), -60), aCycle[2]);
                    }
                    break;
                }
            }
            if (!ObjectUtil.isBlank(date2)) {
                lotteryIssueSourcePO.setLotteryTime(DateUtil.addSecond(date2, 60));
                lotteryIssueSourceDaoMapper.updateByExampleSelective(lotteryIssueSourcePO);
            }

        }
    }

    /**
     * lotto-task 彩期开奖号码抓取入库修改
     *
     * @param lotteryCode 彩种
     * @param issueCode   彩期
     * @return String 彩果
     */
    @Override
    public ResultBO<?> querySingleDrawCode(Integer lotteryCode, String issueCode) throws Exception {
        if (ObjectUtil.isBlank(lotteryCode)) {

            return ResultBO.err("彩种编码为空！");
        } else {
            LotteryEnum.Lottery lottery = LotteryEnum.Lottery.getLottery(lotteryCode);
            if (ObjectUtil.isBlank(lottery)) {
                return ResultBO.err("彩种编码错误！");
            }
        }
        if (StringUtil.isBlank(issueCode)) {
            return ResultBO.err("彩期为空！");
        }

        LotteryIssueSourcePO lotteryIssueSourcePO = lotteryIssueSourceDaoMapper.querySingleDrawCode(lotteryCode, issueCode);
        if (!ObjectUtil.isBlank(lotteryIssueSourcePO)) {
            return ResultBO.ok(lotteryIssueSourcePO.getDrawCode());
        }
        return ResultBO.ok();
    }
}
