package com.hhly.crawlcore.copyorder.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.copyorder.service.CopyOrderService;
import com.hhly.crawlcore.persistence.copyorder.dao.IssueContentInfoMapper;
import com.hhly.crawlcore.persistence.copyorder.dao.IssueContentOriginalMapper;
import com.hhly.crawlcore.persistence.copyorder.dao.MUserIssueInfoMapper;
import com.hhly.crawlcore.persistence.copyorder.dao.MUserIssueLevelMapper;
import com.hhly.crawlcore.persistence.copyorder.dao.OrderIssueInfoMapper;
import com.hhly.crawlcore.persistence.copyorder.po.IssueContentInfo;
import com.hhly.crawlcore.persistence.copyorder.po.IssueContentOriginal;
import com.hhly.crawlcore.persistence.copyorder.po.JczqDaoBO;
import com.hhly.crawlcore.persistence.copyorder.po.MUserIssueInfo;
import com.hhly.crawlcore.persistence.copyorder.po.MUserIssueLevel;
import com.hhly.crawlcore.persistence.copyorder.po.OrderIssueInfo;
import com.hhly.skeleton.base.bo.ResultBO;
import com.hhly.skeleton.base.constants.Constants;
import com.hhly.skeleton.base.constants.JCConstants;
import com.hhly.skeleton.base.constants.JCZQConstants;
import com.hhly.skeleton.base.constants.SymbolConstants;
import com.hhly.skeleton.base.util.DateUtil;
import com.hhly.skeleton.base.util.HttpUtil;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.NumberUtil;
import com.hhly.skeleton.base.util.ObjectUtil;
import com.hhly.skeleton.base.util.StringUtil;

/**
 * @author yuanshangbing
 * @version 1.0
 * @desc 抄单接一比分赛事定时任务业务类
 * @date 2018/1/3 14:48
 * @company 益彩网络科技公司
 */
@Service("copyOrderService")
public class CopyOrderServiceImpl implements CopyOrderService{

    private static Logger logger = LoggerFactory.getLogger(CopyOrderServiceImpl.class);

    @Value("${ybf_copy_match_url}")
    private String ybfMatchUrl;

    @Value("${ybf_copy_user_url}")
    private String ybfUserUrl;

    @Value("${lotto_url}")
    private String lottoUrl;

    @Autowired
    private IssueContentOriginalMapper issueContentOriginalMapper;

    @Autowired
    private MUserIssueInfoMapper mUserIssueInfoMapper;

    @Autowired
    private IssueContentInfoMapper issueContentInfoMapper;

    @Autowired
    private MUserIssueLevelMapper mUserIssueLevelMapper;

    @Autowired
    private OrderIssueInfoMapper orderIssueInfoMapper;




    public static final String GET_MATCHS = "/pc/v1.0/jc/getFootballDataByMatchCodes";
    public static final String GET_MATCH = "/pc/v1.0/jc/getFootballDataByMatchCode";

    public static final String GET_ADD_USER ="/h5/v1.0/member/addMemberInfo";
    public static final String GET_UPDATE_USER ="/h5/v1.0/member/updateMemberInfo";


    @Override
    public ResultBO addIssueMatchAndUserInfo() {
        //1.获取需要一比分赛事数据，分解成推荐list（推荐集合去重）
        Map<String,Object> issueContentOriginalListMap = getIssueContents();
        if(!ObjectUtil.isBlank(issueContentOriginalListMap)){
            //2.推荐赛事原始表数据
            IssueContentOriginal issueContentOriginal = (IssueContentOriginal)issueContentOriginalListMap.get("issueContentOriginal");
            Integer issueContentOriginalId = 0;
            List<Map<String,Object>> issueContentOriginalList = (List<Map<String,Object>>)issueContentOriginalListMap.get("matchList");
            //3.校验每场推荐是否合法（销售状态，截止时间，串关）
            //循环每场推荐，组建本站推荐相关的对象
            for(Map<String,Object> originalMap : issueContentOriginalList){
                //4.根据一比分推荐主键ID校验是否此推荐已存在
                int sourceIssueId = Integer.parseInt((String)originalMap.get("id"));
                int count = issueContentInfoMapper.queryIssueContentByOriginalIssueId(sourceIssueId);
                if(count==0){//推荐不存在，新增
                    //5.获取专家数据
                    Map<String,Object> userInfoMap = getIssueUserInfoMap((String)originalMap.get("userid"));
                    if(!ObjectUtil.isBlank(userInfoMap)){
                        //6.校验用户信息是否存在，不存在新增（更新操作在用户的定时任务里面）
                        Integer userIssueId = (Integer) userInfoMap.get("userIssueId");
                        //#######新增用户相关表
                        Integer userId=0;
                        if(ObjectUtil.isBlank(userIssueId)){//不存在，新增用户相关的表
                            //7.新增用户信息
                            userId = addUserInfo(userInfoMap);
                            if(!ObjectUtil.isBlank(userId)){
                                //8.新增专家表,专家级别表
                                userIssueId = addUserIssueInfo(userInfoMap,userId);
                            }
                        }
                        if(!ObjectUtil.isBlank(userIssueId)){
                            // ########新增推荐表，内容表
                            //9.组建推荐标准的赛事集合。按照销售截止时间升序
                            List<JczqDaoBO> jczqDaoBOs =  filterNoCanIssueMatch(originalMap);
                            if(!ObjectUtil.isBlank(jczqDaoBOs)){
                                if(ObjectUtil.isBlank(issueContentOriginalId)){//为空则新增  移到这个地方去保存原始内容是为了 保证插入原始内容时，推单信息也插入了
                                    issueContentOriginalMapper.insert(issueContentOriginal);
                                    issueContentOriginalId = issueContentOriginal.getId();
                                }
                                //10.组建推荐内容表对象
                                IssueContentInfo issueContentInfo = buildIssueContentInfo(originalMap,jczqDaoBOs,issueContentOriginalId);
                                //11.组建推单表对象
                                OrderIssueInfo orderIssueInfo = buildOrderIssueInfo(userInfoMap,originalMap);
                                issueContentInfoMapper.insert(issueContentInfo);
                                int issueContentInfoId = issueContentInfo.getId();
                                orderIssueInfo.setIssueContentInfoId(issueContentInfoId);
                                orderIssueInfo.setUserIssueId(userIssueId);
                                orderIssueInfoMapper.insert(orderIssueInfo);
                            }else{
                                logger.error("抄单推荐获取一比分推荐赛事不是可售状态！一比分推荐id："+ sourceIssueId);
                            }
                        }
                    }else{
                        logger.error("抄单推荐获取一比分专家接口失败！时间："+ DateUtil.getNow());
                    }
                }else{
                    logger.error("抄单推荐，一比分推荐赛事重复，sourceIssueId:"+sourceIssueId);
                }
            }
        }else{
            logger.error("抄单一比分推荐，内容重复，或者没有推荐内容，不入库。时间："+DateUtil.getNow());
        }
        return ResultBO.ok();
    }


    /**
     * 获取一比分赛事（去重）map key:原始内容表，value:需要增加的推荐
     * @return
     */
    private Map<String,Object> getIssueContents(){
        try {
            Map<String,Object> result = new HashMap<String,Object>();
            //1.获取赛事
            Map<String,String> param = new HashMap<String,String>();
            param.put("langId","2");
            String matchStr = HttpUtil.doPost(ybfMatchUrl,param);
            if(!ObjectUtil.isBlank(matchStr)){
                Set<Integer> set = new TreeSet<Integer>();
                List matchList = JsonUtil.jsonToArray(matchStr,Map.class);
                if(!ObjectUtil.isBlank(matchList)){
                    for(Object object : matchList){
                        Map<String,Object> matchMap = (Map<String,Object>)object;
                        set.add(Integer.valueOf((String)matchMap.get("id")));
                    }
                    StringBuffer buffer = new StringBuffer();
                    for(Integer id :set){
                        buffer.append(id).append(SymbolConstants.COMMA);
                    }
                    String ids = StringUtil.interceptEndSymbol(buffer.toString(),SymbolConstants.COMMA);
                    //2.校验当前赛事接口是否有更新，拿赛事原始表最近一条比较即可
                     String dbIds = issueContentOriginalMapper.queryNewestRecord();
                    if(ObjectUtil.isBlank(dbIds)){//没有记录
                        //新增原始内容
                        IssueContentOriginal issueContentOriginal =  new IssueContentOriginal();
                        issueContentOriginal.setMasterId(Constants.YI_BI_FEN_MASTER_ID);
                        issueContentOriginal.setOriginalIdList(ids);//多个以,好分割，升序
                        issueContentOriginal.setContent(matchStr);
                        issueContentOriginal.setStatus(Constants.NUM_2);
                        result.put("issueContentOriginal",issueContentOriginal);
                        result.put("matchList",matchList);
                        return result;
                    }else{
                        //有记录，判断是否需要更新
                        if(!ids.equals(dbIds)){//不一致，则需要把ids比dbIds多的ID拿出来，插入推荐内容表，并且新增一条原始内容
                            //拿出需要插入推单表的赛事
                            String idsArray[] = ids.split(SymbolConstants.COMMA);
                            String dbIdsArray[] = dbIds.split(SymbolConstants.COMMA);
                            List<String> addIdList = Arrays.asList(idsArray);
                            List<String> removeIDs = new ArrayList<String>();
                            List addMatchList = new ArrayList();
                            for(String id : idsArray){
                                for(String dbId : dbIdsArray){
                                    if(id.equals(dbId)){
                                        removeIDs.add(id);
                                    }
                                }
                            }
                            if(!ObjectUtil.isBlank(removeIDs)){
                                addIdList.removeAll(removeIDs);
                            }
                            for(Object object : matchList){
                                Map<String,Object> matchMap = (Map<String,Object>)object;
                                String id = String.valueOf(matchMap.get("id")) ;
                                if(addIdList.contains(id)){
                                    addMatchList.add(object);
                                }
                            }
                            //新增原始内容
                            IssueContentOriginal issueContentOriginal =  new IssueContentOriginal();
                            issueContentOriginal.setMasterId(Constants.YI_BI_FEN_MASTER_ID);
                            issueContentOriginal.setOriginalIdList(ids);//多个以,好分割，升序
                            issueContentOriginal.setContent(matchStr);
                            issueContentOriginal.setStatus(Constants.NUM_2);
                            result.put("issueContentOriginal",issueContentOriginal);
                            result.put("matchList",addMatchList);
                            return result;
                        }else{
                            logger.info("一比分推荐赛事，已存在，不处理！时间："+DateUtil.getNow());
                        }
                    }
                }
            }else{
                logger.error("抄单推荐获取一比分赛事接口数据为空！时间："+ DateUtil.getNow());
            }
        }catch (IOException e){
            logger.error("抄单推荐获取一比分赛事接口失败！时间："+ DateUtil.getNow()+e);
        }catch (URISyntaxException e1){
            logger.error("抄单推荐获取一比分赛事接口失败！时间："+ DateUtil.getNow()+e1);
        }catch (Exception e2){
            logger.error("抄单推荐内容处理失败！时间："+ DateUtil.getNow()+e2);
        }
        return null;
    }

    /**
     * 返回是否需要新增专家用户，和专家的状况，连红，命中率
     * @param yibifenUserId
     * @return
     */
    public Map<String,Object> getIssueUserInfoMap(String yibifenUserId){
        try {
            //1.获取专家信息
            Map<String,String> param = new HashMap<String,String>();
            param.put("userId",yibifenUserId);
            String matchStr = HttpUtil.doPost(ybfUserUrl,param);
            if(!ObjectUtil.isBlank(matchStr)) {
                Map<String,Object> userMap = JsonUtil.jsonToObject(matchStr, Map.class);
                if (!ObjectUtil.isBlank(userMap)) {
                    Integer code = (Integer)userMap.get("code");
                    if(code.equals(200)){
                        Map<String,Object> resultMap = new HashMap<String,Object>();
                        Map<String,Object> userInfoMap = (Map<String,Object>)userMap.get("data");
                        //连红
                        Integer continueRed =(Integer)userInfoMap.get("userWeekCurrentStreak");
                        //近几中几
                        Integer record = (Integer)userInfoMap.get("userWeekWinPoint");
                        StringBuffer recordStr = new StringBuffer();
                        recordStr.append(Constants.NUM_7).append(SymbolConstants.VERTICAL_BAR).append(record==null?0:record);
                        //发单总次数
                        Integer issueNum = (Integer)userInfoMap.get("userGrandTotalPoint");
                        //命中总次数
                        Integer hitNum = (Integer)userInfoMap.get("userWinTotalPoint");
                        //命中率
                        Double hitProfit = 0d;
                        if(!ObjectUtil.isBlank(issueNum) && !ObjectUtil.isBlank(hitNum)){
                            hitProfit = NumberUtil.div(Double.valueOf(hitNum),Double.valueOf(issueNum),2);
                        }
                        resultMap.put("continueRed",continueRed==null?0:continueRed);
                        resultMap.put("record",recordStr.toString());
                        resultMap.put("issueNum",issueNum==null?0:issueNum);
                        resultMap.put("hitNum",hitNum==null?0:hitNum);
                        resultMap.put("hitProfit",hitProfit);

                        resultMap.put("nickname",userInfoMap.get("userName"));
                        resultMap.put("headUrl",userInfoMap.get("userImg"));
                        //此专家是否存在
                        Integer mUserIssueId = mUserIssueInfoMapper.queryUserByYiBiFenUserId(yibifenUserId);
                        resultMap.put("userIssueId",mUserIssueId);
                        resultMap.put("yibifenUserId",yibifenUserId);
                        return resultMap;
                    }else{
                        logger.error("抄单推荐获取一比分专家接口失败！时间："+ DateUtil.getNow());
                    }
                }
            }
        }catch (IOException e){
            logger.error("抄单推荐获取一比分专家接口失败！时间："+ DateUtil.getNow()+e);
        }catch (URISyntaxException e1){
            logger.error("抄单推荐获取一比分专家接口失败！时间："+ DateUtil.getNow()+e1);
        }catch (Exception e2){
            logger.error("抄单推荐专家信息处理失败！时间："+ DateUtil.getNow()+e2);
        }
        return null;
    }

    /**
     *
     * 校验此推荐是否合法（销售状态，截止时间，串关），并返回标准的赛事对象
     * @param matchsMap
     * @return
     */
    private List<JczqDaoBO> filterNoCanIssueMatch(Map<String,Object> matchsMap){
        List<JczqDaoBO> filterMatchList = null;
        try{
            //1.过滤掉单场非单关的推荐
            List mutilDetailList = (List)matchsMap.get("mutilDetailList");
            List<String> officeMatchCodes = new ArrayList<String>();
            StringBuffer officeMatchCodeStr = new StringBuffer();
            for(Object object1 :mutilDetailList){
                Map<String,Object> matchMap = (Map<String,Object>)object1;
                String officeMatchCode = (String)matchMap.get("serNum");
                officeMatchCodes.add(officeMatchCode);
                officeMatchCodeStr.append(officeMatchCode).append(SymbolConstants.COMMA);
            }

            if(officeMatchCodes.size()==1){//只有单场，判断是否是单关
                Map<String,String> param = new HashMap<String,String>();
                param.put("matchCode",officeMatchCodes.get(0));
                String matchStr = HttpUtil.doGet(lottoUrl+GET_MATCH,param);
                if(!ObjectUtil.isBlank(matchStr)) {
                    Integer spfStatus = JsonUtil.jsonToObject(matchStr, Integer.class)==null?0:JsonUtil.jsonToObject(matchStr, Integer.class);
                    if(!spfStatus.equals(Constants.NUM_1)){//不是单关，过滤这个推荐
                        return null;
                    }
                }
            }
            //2.过滤掉非销售中和过了截止时间的赛事
            Map<String,String> param = new HashMap<String,String>();
            param.put("matchCodes",StringUtil.interceptEndSymbol(officeMatchCodeStr.toString(),SymbolConstants.COMMA));
            String matchStrs = HttpUtil.doGet(lottoUrl+GET_MATCHS,param);
            if(!ObjectUtil.isBlank(matchStrs)) {
                filterMatchList = JsonUtil.jsonToArray(matchStrs, JczqDaoBO.class);
                if(!ObjectUtil.isBlank(filterMatchList)){
                    if(filterMatchList.size()!= officeMatchCodes.size()){//入参的赛事大小和返回的赛事大小不匹配，
                        // 说明校验时过滤掉了不能购买的赛事，则此推荐不入库
                        return null;
                    }
                }else {
                    return null;
                }
            }
        }catch (IOException e){
            logger.error("抄单推荐获取本站赛事接口失败！时间："+ DateUtil.getNow()+e);
        }catch (URISyntaxException e1){
            logger.error("抄单推荐获取本站赛事接口失败！时间："+ DateUtil.getNow()+e1);
        }catch (Exception e2){
            logger.error("抄单推荐内容处理失败！时间："+ DateUtil.getNow()+e2);
        }
        return filterMatchList;
    }


    /**
     * 获取推荐内容表需要的参数包括 投注内容
     * @param matchsMap
     * @param matchList
     * @return
     */
    private Map<String,Object> getIssueContentInfo(Map<String,Object> matchsMap,List<JczqDaoBO> matchList){
        Map<String,Object> result = new HashMap<String,Object>();
        List mutilDetailList = (List)matchsMap.get("mutilDetailList");
        Integer bunch = (Integer)matchsMap.get("multiType");
        int sourceIssueId = Integer.parseInt((String)matchsMap.get("id"));
        int spfCount = 0;
        for(int i=0;i<mutilDetailList.size();i++){
            Map<String,Object> matchMap = (Map<String,Object>)mutilDetailList.get(i);
            String panKou = String.valueOf(matchMap.get("handicap"));
            if(panKou.equals("0")){//胜平负
                spfCount ++;
            }
        }
        Integer lotteryChildCode = 0;
        if(spfCount!=0 && spfCount!=mutilDetailList.size()){
            lotteryChildCode = JCZQConstants.ID_FHT;
        }
        StringBuffer buffer = new StringBuffer();
        StringBuffer buyScreen = new StringBuffer();

        for(int i=0;i<mutilDetailList.size();i++){
            Map<String,Object> matchMap = (Map<String,Object>)mutilDetailList.get(i);
            String officeMatchCode = String.valueOf(matchMap.get("serNum"));
            String panKou = String.valueOf(matchMap.get("handicap"));
            if(panKou.equals("0")){//单个赛事是胜平负
                if(lotteryChildCode.equals(JCZQConstants.ID_FHT)){//推荐是混合玩法
                    setHtSpf(matchList, mutilDetailList, buffer, buyScreen, i, matchMap, officeMatchCode);
                }else{////推荐是胜平负
                    lotteryChildCode = JCZQConstants.ID_JCZQ;
                    setSpf(matchList, mutilDetailList, lotteryChildCode, buffer, buyScreen, i, matchMap, officeMatchCode);

                }
            }else{//单个赛事是让球胜平负
                if(!ObjectUtil.isBlank(lotteryChildCode)){//推荐是混合玩法
                    //161128001_R[+1](3@1.57,0@2.27)_S(1@1.89,0@4.21)^2_1,3_1^868
                    setHtRqSpf(matchList, mutilDetailList, buffer, buyScreen, i, matchMap, officeMatchCode);
                }else{//推荐是让球胜平负
                    //161128001[+1](3@1.57)|161128002[+1](0@4.21)|161128003[+1](0@4.21)^3_1^1
                    lotteryChildCode = JCZQConstants.ID_RQS;
                    setRqSpf(matchList, mutilDetailList, lotteryChildCode, buffer, buyScreen, i, matchMap, officeMatchCode);
                }
            }

        }
        //2.串关，倍数
        buffer.append(SymbolConstants.UP_CAP).append(bunch).append(SymbolConstants.UNDERLINE).append("1").append(SymbolConstants.UP_CAP).append("1");
        result.put("lotteryChildCode",lotteryChildCode);
        result.put("planContent",buffer.toString());
        result.put("sourceIssueId",sourceIssueId);
        result.put("buyScreen",StringUtil.interceptEndSymbol(buyScreen.toString(),SymbolConstants.COMMA));
        return result;
    }

    private void setRqSpf(List<JczqDaoBO> matchList, List mutilDetailList, Integer lotteryChildCode, StringBuffer buffer, StringBuffer buyScreen, int i, Map<String, Object> matchMap, String officeMatchCode) {
        for(int j=0;j<matchList.size();j++){
            if(officeMatchCode.equals(matchList.get(j).getOfficialMatchCode())) {
                buyScreen.append(matchList.get(j).getSystemCode()).append(SymbolConstants.COMMA);
                String letNum = "";
                if(NumberUtil.isNumeric(String.valueOf(matchList.get(j).getNewestLetNum()))){
                    letNum = SymbolConstants.ADD+letNum;
                }else{
                    letNum = String.valueOf(matchList.get(j).getNewestLetNum());
                }
                String choose = (String) matchMap.get("choose");
                String choose1 = (String) matchMap.get("choose1");
                StringBuffer betContent = new StringBuffer();
                betContent.append(getStandChoose(lotteryChildCode, choose, matchList.get(j)));
                if (!ObjectUtil.isNull(choose1)) {
                    betContent.append(SymbolConstants.COMMA).append(getStandChoose(lotteryChildCode, choose1, matchList.get(j)));
                }
                if (i < mutilDetailList.size()) {
                    buffer.append(matchList.get(j).getSystemCode()).append(SymbolConstants.MIDDLE_PARENTHESES_LEFT).append(letNum).append(SymbolConstants.MIDDLE_PARENTHESES_RIGHT).append(SymbolConstants.PARENTHESES_LEFT).append(betContent.toString()).append(SymbolConstants.PARENTHESES_RIGHT).append(SymbolConstants.VERTICAL_BAR);
                } else {
                    buffer.append(matchList.get(j).getSystemCode()).append(SymbolConstants.MIDDLE_PARENTHESES_LEFT).append(letNum).append(SymbolConstants.MIDDLE_PARENTHESES_RIGHT).append(SymbolConstants.PARENTHESES_LEFT).append(betContent.toString()).append(SymbolConstants.PARENTHESES_RIGHT);
                }
                break;
            }
        }
    }

    private void setHtRqSpf(List<JczqDaoBO> matchList, List mutilDetailList, StringBuffer buffer, StringBuffer buyScreen, int i, Map<String, Object> matchMap, String officeMatchCode) {
        for(int j=0;j<matchList.size();j++){
            if(officeMatchCode.equals(matchList.get(j).getOfficialMatchCode())) {
                buyScreen.append(matchList.get(j).getSystemCode()).append(SymbolConstants.COMMA);
                String letNum = "";
                if(NumberUtil.isNumeric(String.valueOf(matchList.get(j).getNewestLetNum()))){
                    letNum = SymbolConstants.ADD+letNum;
                }else{
                    letNum = String.valueOf(matchList.get(j).getNewestLetNum());
                }
                String choose = (String) matchMap.get("choose");
                String choose1 = (String) matchMap.get("choose1");
                StringBuffer betContent = new StringBuffer();
                betContent.append(getStandChoose(JCZQConstants.ID_RQS, choose, matchList.get(j)));
                if (!ObjectUtil.isNull(choose1)) {
                    betContent.append(SymbolConstants.COMMA).append(getStandChoose(JCZQConstants.ID_RQS, choose1, matchList.get(j)));
                }
                if (i < mutilDetailList.size()) {
                    buffer.append(matchList.get(j).getSystemCode()).append(SymbolConstants.UNDERLINE).append(JCConstants.R).append(SymbolConstants.MIDDLE_PARENTHESES_LEFT).append(letNum).append(SymbolConstants.MIDDLE_PARENTHESES_RIGHT).append(SymbolConstants.PARENTHESES_LEFT).append(betContent.toString()).append(SymbolConstants.PARENTHESES_RIGHT).append(SymbolConstants.VERTICAL_BAR);
                } else {
                    buffer.append(matchList.get(j).getSystemCode()).append(SymbolConstants.UNDERLINE).append(JCConstants.R).append(SymbolConstants.MIDDLE_PARENTHESES_LEFT).append(letNum).append(SymbolConstants.MIDDLE_PARENTHESES_RIGHT).append(SymbolConstants.PARENTHESES_LEFT).append(betContent.toString()).append(SymbolConstants.PARENTHESES_RIGHT);
                }
                break;
            }

        }
    }

    private void setSpf(List<JczqDaoBO> matchList, List mutilDetailList, Integer lotteryChildCode, StringBuffer buffer, StringBuffer buyScreen, int i, Map<String, Object> matchMap, String officeMatchCode) {
        // 1710124303(3@1.75,0@1.75)|1710124302(3@1.75,0@1.75)^2_1^1
        for(int j=0;j<matchList.size();j++){
            if(officeMatchCode.equals(matchList.get(j).getOfficialMatchCode())) {
                buyScreen.append(matchList.get(j).getSystemCode()).append(SymbolConstants.COMMA);
                String choose = (String) matchMap.get("choose");
                String choose1 = (String) matchMap.get("choose1");
                StringBuffer betContent = new StringBuffer();
                betContent.append(getStandChoose(lotteryChildCode, choose, matchList.get(j)));
                if (!ObjectUtil.isNull(choose1)) {
                    betContent.append(SymbolConstants.COMMA).append(getStandChoose(lotteryChildCode, choose1, matchList.get(j)));
                }
                if (i < mutilDetailList.size()) {
                    buffer.append(matchList.get(j).getSystemCode()).append(SymbolConstants.PARENTHESES_LEFT).append(betContent.toString()).append(SymbolConstants.PARENTHESES_RIGHT).append(SymbolConstants.VERTICAL_BAR);
                } else {
                    buffer.append(matchList.get(j).getSystemCode()).append(SymbolConstants.PARENTHESES_LEFT).append(betContent.toString()).append(SymbolConstants.PARENTHESES_RIGHT);
                }
                break;
            }

        }
    }

    private void setHtSpf(List<JczqDaoBO> matchList, List mutilDetailList, StringBuffer buffer, StringBuffer buyScreen, int i, Map<String, Object> matchMap, String officeMatchCode) {
        //161128001_R[+1](3@1.57,0@2.27)_S(1@1.89,0@4.21)^2_1,3_1^868
        for(int j=0;j<matchList.size();j++){
            if(officeMatchCode.equals(matchList.get(j).getOfficialMatchCode())) {
                buyScreen.append(matchList.get(j).getSystemCode()).append(SymbolConstants.COMMA);
                String choose = (String) matchMap.get("choose");
                String choose1 = (String) matchMap.get("choose1");
                StringBuffer betContent = new StringBuffer();
                betContent.append(getStandChoose(JCZQConstants.ID_JCZQ, choose, matchList.get(j)));
                if (!ObjectUtil.isNull(choose1)) {
                    betContent.append(SymbolConstants.COMMA).append(getStandChoose(JCZQConstants.ID_JCZQ, choose1, matchList.get(j)));
                }
                if (i < mutilDetailList.size()) {
                    buffer.append(matchList.get(j).getSystemCode()).append(SymbolConstants.UNDERLINE).append(JCConstants.S).append(SymbolConstants.PARENTHESES_LEFT).append(betContent.toString()).append(SymbolConstants.PARENTHESES_RIGHT).append(SymbolConstants.VERTICAL_BAR);
                } else {
                    buffer.append(matchList.get(j).getSystemCode()).append(SymbolConstants.UNDERLINE).append(JCConstants.S).append(SymbolConstants.PARENTHESES_LEFT).append(betContent.toString()).append(SymbolConstants.PARENTHESES_RIGHT);
                }
                break;
            }
        }
    }

    /**
     * 1客胜 0平 -1主胜 转换成 310
     *
     * @param choose
     * @return
     */
    private String getStandChoose(Integer lotteryChildCode,String choose,JczqDaoBO jczqDaoBO){
        if(lotteryChildCode.equals(JCZQConstants.ID_JCZQ)){//胜平负
            if(choose.equals("-1")){
                return "3"+SymbolConstants.AT+jczqDaoBO.getNewestSpWin();
            }else if(choose.equals("0")){
                return "1"+SymbolConstants.AT+jczqDaoBO.getNewestSpDraw();
            }else if(choose.equals("1")){
                return "0"+SymbolConstants.AT+jczqDaoBO.getNewestSpFail();
            }
        }else if(lotteryChildCode.equals(JCZQConstants.ID_RQS)){
            if(choose.equals("-1")){
                return "3"+SymbolConstants.AT+jczqDaoBO.getNewestLetSpWin();
            }else if(choose.equals("0")){
                return "1"+SymbolConstants.AT+jczqDaoBO.getNewestLetSpDraw();
            }else if(choose.equals("1")){
                return "0"+SymbolConstants.AT+jczqDaoBO.getNewestLetSpFail();
            }
        }
        return "";
    }

    /**
     * 组建推荐内容表
     * @return
     */
    private IssueContentInfo buildIssueContentInfo(Map<String,Object> matchsMap,List<JczqDaoBO> jczqDaoBOs,int issueContentOriginalId){
        Map<String,Object> result = getIssueContentInfo(matchsMap,jczqDaoBOs);
        IssueContentInfo issueContentInfo = new IssueContentInfo();
        issueContentInfo.setIssueCode(jczqDaoBOs.get(0).getIssueCode());
        issueContentInfo.setAmount(Double.valueOf((Integer)matchsMap.get("price")));
        issueContentInfo.setIssueContentOriginalId(issueContentOriginalId);
        issueContentInfo.setLotteryCode(JCZQConstants.ID_JCZQ_B);
        issueContentInfo.setLotteryChildCode((Integer) result.get("lotteryChildCode"));
        issueContentInfo.setOrderStatus(Constants.NUM_1);
        issueContentInfo.setPlanContent((String) result.get("planContent"));
        issueContentInfo.setBuyScreen((String)result.get("buyScreen"));
        issueContentInfo.setSourceIssueId((Integer) result.get("sourceIssueId"));
        String shortHomeName = ObjectUtil.isBlank(jczqDaoBOs.get(0).getHomeShortName())?jczqDaoBOs.get(0).getHomeFullName():jczqDaoBOs.get(0).getHomeShortName();
        String shortGuestName = ObjectUtil.isBlank(jczqDaoBOs.get(0).getGuestShortName())?jczqDaoBOs.get(0).getGuestFullName():jczqDaoBOs.get(0).getGuestShortName();
        issueContentInfo.setBeginTeamName(shortHomeName+SymbolConstants.VERTICAL_BAR+shortGuestName);
        issueContentInfo.setSaleEndTime(jczqDaoBOs.get(0).getSaleEndTime());
        return issueContentInfo;
    }

    /**
     * 组建推单对象表
     * @param userInfoMap
     * @return
     */
    private OrderIssueInfo buildOrderIssueInfo(Map<String,Object> userInfoMap,Map<String,Object> originalMap){
        OrderIssueInfo orderIssueInfo = new OrderIssueInfo();
        orderIssueInfo.setRecentRecord((String)userInfoMap.get("record"));
        orderIssueInfo.setContinueHit((Integer)userInfoMap.get("continueRed"));
        orderIssueInfo.setHitRate((Double)userInfoMap.get("hitProfit"));
        orderIssueInfo.setIssueType(Constants.NUM_2);

        Integer price = (Integer) originalMap.get("price");
        Integer chargeType = 0;
        if(price.equals(0)){
            chargeType = Constants.NUM_1;
        }else{
            chargeType = Constants.NUM_2;
        }
        orderIssueInfo.setChargeType(chargeType);
        orderIssueInfo.setIsShow(Constants.NUM_1);
        orderIssueInfo.setMasterId(Constants.YI_BI_FEN_MASTER_ID);
        return orderIssueInfo;
    }

    /**
     * 新增用户、钱包信息
     * @param userInfoMap
     * @return
     */
    private Integer addUserInfo(Map<String,Object> userInfoMap){
        try {
            Map<String,String> param = new HashMap<String,String>();
            param.put("channelId",Constants.YI_BI_FEN_CHANNEL);
            param.put("headUrl",(String)userInfoMap.get("headUrl"));
            param.put("nickname",(String)userInfoMap.get("nickname"));
            String userStr = HttpUtil.doPost(lottoUrl+GET_ADD_USER,param);
            Map<String,Object> userMap = JsonUtil.json2Map(userStr);
            Integer errorCode = (Integer)userMap.get("success");
            if(errorCode.equals(1)){
               Map<String,Object> data = (Map<String,Object>)userMap.get("data");
               Integer userId = (Integer)data.get("u_id");
               return userId;
            }else{
                logger.error("抄单推荐调用新增用户信息接口失败！时间："+ DateUtil.getNow());
            }
        }catch (IOException e){
            logger.error("抄单推荐调用新增用户信息接口失败！时间："+ DateUtil.getNow()+e);
        }catch (URISyntaxException e1){
            logger.error("抄单推荐调用新增用户信息接口失败！时间："+ DateUtil.getNow()+e1);
        }catch (Exception e2){
            logger.error("抄单推荐新增用户信息失败！时间："+ DateUtil.getNow()+e2);
        }
        return null;
    }


    /**
     * 更新用户信息
     * @param headUrl
     * @param nickname
     * @return
     */
    private Integer updateUserInfo(String headUrl,String nickname,Integer userId){
        try {
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("headUrl",headUrl);
            param.put("nickname",nickname);
            param.put("id",String.valueOf(userId));
            String userStr = HttpUtil.doPost(lottoUrl+GET_UPDATE_USER,param);
            Map<String,Object> userMap = JsonUtil.json2Map(userStr);
            Integer errorCode = (Integer)userMap.get("success");
            if(errorCode.equals(1)){
               return 1;
            }else{
                logger.error("抄单推荐调用更新用户信息接口失败！时间："+ DateUtil.getNow());
            }
        }catch (IOException e){
            logger.error("抄单推荐调用更新用户信息接口失败！时间："+ DateUtil.getNow()+e);
        }catch (URISyntaxException e1){
            logger.error("抄单推荐调用更新用户信息接口失败！时间："+ DateUtil.getNow()+e1);
        }catch (Exception e2){
            logger.error("抄单推荐更新用户信息失败！时间："+ DateUtil.getNow()+e2);
        }
        return null;
    }

    /**
     * 增加用户专家表和级别表
     * @param userInfoMap
     */
    private Integer addUserIssueInfo(Map<String,Object> userInfoMap,Integer userId){
        MUserIssueInfo mUserIssueInfo = new MUserIssueInfo();
        mUserIssueInfo.setUserId(userId);
        mUserIssueInfo.setStatus(Constants.NUM_1);
        mUserIssueInfo.setRecentRecord((String)userInfoMap.get("record"));
        mUserIssueInfo.setContinueHit((Integer)userInfoMap.get("continueRed"));
        mUserIssueInfo.setHitRate((Double)userInfoMap.get("hitProfit"));
        mUserIssueInfo.setIssueNum((Integer)userInfoMap.get("issueNum"));
        mUserIssueInfo.setHitNum((Integer)userInfoMap.get("hitNum"));
        mUserIssueInfo.setYibifenUserId((String)userInfoMap.get("yibifenUserId"));
        mUserIssueInfoMapper.insert(mUserIssueInfo);
        Integer id = mUserIssueInfo.getId();

        MUserIssueLevel mUserIssueLevel = new MUserIssueLevel();
        mUserIssueLevel.setUserIssueInfoId(id);
        mUserIssueLevel.setLotteryCode(JCZQConstants.ID_JCZQ_B);
        mUserIssueLevel.setLevel(Constants.NUM_0);
        mUserIssueLevel.setIsAutomatic(Constants.NUM_0);
        mUserIssueLevelMapper.insert(mUserIssueLevel);
        return id;
    }

    /**
     * 更新专家信息
     * @param userInfoMap
     */
    private void updateUserIssueInfo(Map<String,Object> userInfoMap,String yibifenUserId){
        MUserIssueInfo mUserIssueInfo = new MUserIssueInfo();
        mUserIssueInfo.setYibifenUserId(yibifenUserId);
        mUserIssueInfo.setRecentRecord((String)userInfoMap.get("record"));
        mUserIssueInfo.setContinueHit((Integer)userInfoMap.get("continueRed"));
        mUserIssueInfo.setHitRate((Double)userInfoMap.get("hitProfit"));
        mUserIssueInfo.setIssueNum((Integer)userInfoMap.get("issueNum"));
        mUserIssueInfo.setHitNum((Integer)userInfoMap.get("hitNum"));
        mUserIssueInfoMapper.updateByPrimaryKey(mUserIssueInfo);
    }

    @Override
    public ResultBO updateUserIssueInfo() {
        //一比分专家暂时不多，先做For循环处理。后续量大，考虑多线程处理
        List<MUserIssueInfo> mUserIssueInfos = mUserIssueInfoMapper.queryAllYiBiFenUserIds();
        if(!ObjectUtil.isBlank(mUserIssueInfos)){
            for(MUserIssueInfo mUserIssueInfo : mUserIssueInfos){
                Map<String,Object> userInfoMap = getIssueUserInfoMap(mUserIssueInfo.getYibifenUserId());
                if(!ObjectUtil.isBlank(userInfoMap)) {
                    //更新用户信息
                    String headUrl = (String)userInfoMap.get("headUrl");
                    String nickname = (String)userInfoMap.get("nickname");
                    if(!ObjectUtil.isBlank(nickname)){
                        updateUserInfo(headUrl,nickname,mUserIssueInfo.getUserId());
                        //更新专家信息
                        updateUserIssueInfo(userInfoMap,mUserIssueInfo.getYibifenUserId());
                    }
                }
            }
        }
        return ResultBO.ok();
    }




}
