package com.hhly.crawlcore.sport.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.hhly.crawlcore.base.zeroc.WebClientManager;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryIssueDaoMapper;
import com.hhly.crawlcore.persistence.lottery.dao.LotteryTypeDaoMapper;
import com.hhly.crawlcore.persistence.lottery.po.LotteryIssuePO;
import com.hhly.crawlcore.persistence.lottery.po.LotteryTypePO;
import com.hhly.crawlcore.persistence.sport.dao.SportAgainstInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjBasePO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.service.SportBjMatchService;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.SportEnum;
import com.hhly.skeleton.base.common.SportEnum.JcMatchStatusEnum;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @author lgs on
 * @version 1.0
 * @desc
 * @date 2017/6/13.
 * @company 益彩网络科技有限公司
 */
@Service
public class SportBjMatchServiceImpl implements SportBjMatchService {

    private static final Logger logger = LoggerFactory.getLogger(SportBjMatchServiceImpl.class);

    @Resource
    private SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

    @Resource
    private SportTeamInfoService sportTeamInfoService;

    @Resource
    private SportMatchInfoService sportMatchInfoService;

    @Resource
    private LotteryIssueDaoMapper lotteryIssueMapper;

    @Resource
    private LotteryTypeDaoMapper lotteryTypeDaoMapper;

    @Resource
    private WebClientManager webClientManager;


    @Resource
    private MQUtils mqUtils;


    /**
     * 保存北京单场对阵
     *
     * @param po
     */
    @Override
    public synchronized void save(SportDataBjBasePO bjPO) {
    	SportAgainstInfoPO po = new SportAgainstInfoPO(bjPO);
    	
    	Short matchType = SportEnum.getMatchType(po.getMatchTypeName());

//        String homeTeamUnion = null;
//        String guestTeamUnion = null;
//        String matchUnion = null;
//        try {
//            homeTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(po.getHomeName() + matchType).getBytes("UTF-8"));
//            guestTeamUnion = DigestUtils.md5DigestAsHex(String.valueOf(po.getVisitiName() + matchType).getBytes("UTF-8"));
//            matchUnion = DigestUtils.md5DigestAsHex(String.valueOf(po.getMatchName() + matchType).getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        SportTeamInfoPO homeTeam = new SportTeamInfoPO(matchType, po.getHomeName(), homeTeamUnion, po.getCreateBy(), po.getCreateBy());
//        SportTeamInfoPO guestTeam = new SportTeamInfoPO(matchType, po.getVisitiName(), guestTeamUnion, po.getCreateBy(), po.getCreateBy());
//        SportMatchInfoPO matchInfo = new SportMatchInfoPO(po.getMatchName(), (short) matchType, matchUnion, po.getCreateBy());
    	
    	SportTeamSourceInfoPO homeSourcePO = new SportTeamSourceInfoPO(bjPO.getHomeName(), bjPO.getHomeAbbrName(), matchType, null, SportDataSource.AIBO_OFFICIAL.getValue());
    	SportTeamSourceInfoPO guestSourcePO = new SportTeamSourceInfoPO(bjPO.getVisitiName(), bjPO.getVisitiAbbrName(), matchType, null, SportDataSource.AIBO_OFFICIAL.getValue());
    	SportMatchSourceInfoPO matchSourcePO = new SportMatchSourceInfoPO(null, bjPO.getMatchName(), matchType, null, SportDataSource.AIBO_OFFICIAL.getValue());

    	SportTeamInfoPO homeTeam = sportTeamInfoService.getTeamInfo(homeSourcePO);
    	SportTeamInfoPO guestTeam = sportTeamInfoService.getTeamInfo(guestSourcePO);
    	SportMatchInfoPO matchInfo = sportMatchInfoService.getMatchInfo(matchSourcePO);
        
        LotteryIssuePO issuePO;
        if (ObjectUtil.isBlank(po.getIssueCode())) {
            LotteryIssuePO paramPO = new LotteryIssuePO();
            paramPO.setLotteryCode(po.getLotteryCode());
            paramPO.setCurrentIssue((short) 1);
            issuePO = lotteryIssueMapper.findSingle(paramPO);
            if (ObjectUtil.isBlank(issuePO)) {
                throw new RuntimeException("北京单场获取彩种【" + po.getLotteryCode() + "】彩期异常");
            }
            po.setIssueCode(issuePO.getIssueCode());
        }

        LotteryTypePO lotTypePO = lotteryTypeDaoMapper.findTypeUseAddIssue(Integer.valueOf(po.getLotteryCode()));
        po.setSystemCode(SportLotteryUtil.getBjSystemCode(po.getBjNum(), po.getIssueCode()));
        if (!ObjectUtil.isBlank(homeTeam)) {
            po.setHomeTeamId(homeTeam.getId());
            po.setHomeName(homeTeam.getTeamFullName());
        }
        if (!ObjectUtil.isBlank(guestTeam)) {
            po.setVisitiTeamId(guestTeam.getId());
            po.setVisitiName(guestTeam.getTeamFullName());
        }
        if(!ObjectUtil.isBlank(matchInfo)){
            po.setSportMatchInfoId(matchInfo.getId());
            po.setMatchName(matchInfo.getMatchFullName());
        }
        po.setSaleEndTime(SportLotteryUtil.getBjSaleEndTime(po.getStartTime(), lotTypePO.getBuyEndTime()));
        //设置推荐赛事默认为0
        po.setIsRecommend((short)0);

        SportAgainstInfoPO resultPo = sportAgainstInfoDaoMapper.findByCode(po.getLotteryCode(), po.getIssueCode(), po.getSystemCode());

		if(!ObjectUtil.isBlank(resultPo)){
			if(JcMatchStatusEnum.isUpdate(resultPo.getMatchStatus()))
				return;
			po.setId(resultPo.getId());
		}
		
		sportAgainstInfoDaoMapper.merge(po);
//        if (ObjectUtil.isBlank(resultPo)) {
//            sportAgainstInfoDaoMapper.insert(po);
//        } else {
//            if (resultPo.getMatchStatus() == 7 || resultPo.getMatchStatus() == 9) {
//            	po.setId(resultPo.getId());
//                sportAgainstInfoDaoMapper.update(po);
//                //handlBjdcMq(po);
//            }
//        }

       /* int flag = sportAgainstInfoDaoMapper.updateBj(po);
        if (flag == 0) {
            sportAgainstInfoDaoMapper.insert(po);
            //调用MQ发消息
            mqUtils.sendMessage(po.getIssueCode(), po.getLotteryCode());
        } else {
            handlBjdcMq(po);
        }*/
    }

    /**
     * 处理竞篮比赛时间有变动的时候发送MQ消息
     * 当抓取的对阵赛事有新增 对阵赛事时,  数据库现有10场比赛, 当抓取有11场比赛时, 需要MQ通知
     *
     * @param po 参数
     */
    private void handlFBMq(SportAgainstInfoPO po) {
        SportAgainstInfoPO queryPO = new SportAgainstInfoPO();
        queryPO.setIssueCode(po.getIssueCode());
        queryPO.setLotteryCode(po.getLotteryCode());
        queryPO.setOfficialMatchCode(po.getOfficialMatchCode());
        List<SportAgainstInfoPO> list = sportAgainstInfoDaoMapper.findCondition(queryPO);
        SportAgainstInfoPO resultPO;
        if (!ObjectUtil.isBlank(list)) {
            resultPO = list.get(0);
            if (!po.getStartTime().equals(resultPO.getStartTime())) {
                logger.info("竞篮比赛时间，对接比赛信息【" + JSON.toJSONString(po) + "】,原比赛信息【" + JSON.toJSONString(resultPO) + "】");
                //调用MQ发消息
                mqUtils.sendMessage(po.getIssueCode(), po.getLotteryCode());
            }
        }
    }


    @Override
    public synchronized void saveForBasketBall(SportAgainstInfoPO po) {
    	Short matchType = SportEnum.getMatchType(po.getMatchTypeName());

        String matchUnion = null;
        try {
            matchUnion = DigestUtils.md5DigestAsHex(String.valueOf(po.getMatchName() + matchType).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SportTeamInfoPO homeTeam = new SportTeamInfoPO(matchType, po.getHomeName(), null, po.getCreateBy(), po.getCreateBy());
        SportTeamInfoPO guestTeam = new SportTeamInfoPO(matchType, po.getVisitiName(), null, po.getCreateBy(), po.getCreateBy());
        SportMatchInfoPO matchInfo = new SportMatchInfoPO(po.getMatchName(), (short) matchType, matchUnion, po.getCreateBy());
        if (po.getCreateBy().contains("500.com")) {
            homeTeam = new SportTeamInfoPO(matchType, null, po.getHomeName(), null, po.getCreateBy(), po.getCreateBy());
            guestTeam = new SportTeamInfoPO(matchType, null, po.getVisitiName(), null, po.getCreateBy(), po.getCreateBy());
            matchInfo = new SportMatchInfoPO(null, po.getMatchName(), (short) matchType, matchUnion, po.getCreateBy());
        }

        //获取彩期信息
        List<LotteryIssuePO> issuePOList = lotteryIssueMapper.getIssueInfo(Integer.valueOf(po.getLotteryCode()));

        homeTeam = sportTeamInfoService.findTeam(homeTeam);
        guestTeam = sportTeamInfoService.findTeam(guestTeam);
        matchInfo = sportMatchInfoService.findMatch(matchInfo);
        String systemCode = SportLotteryUtil.getSystemCode(po.getOfficialMatchCode(), po.getIssueCode());

        po.setSystemCode(systemCode);
        long matchId = matchInfo.getId();
        po.setSportMatchInfoId(matchId);
        if (!ObjectUtil.isBlank(homeTeam)) {
            po.setHomeTeamId(homeTeam.getId());
        }
        if (!ObjectUtil.isBlank(guestTeam)) {
            po.setVisitiTeamId(guestTeam.getId());
        }

        Date saleEndTime = SportLotteryUtil.getSaleEndTime(po.getStartTime(), po.getIssueCode(), issuePOList);
        po.setSaleEndTime(saleEndTime);

        if (po.getCreateBy().contains("500.com")) {
            po.setOfficialId(null);
        }
        
        SportAgainstInfoPO resultPo = sportAgainstInfoDaoMapper.findByCode(po.getLotteryCode(), po.getIssueCode(), po.getSystemCode());

		if(!ObjectUtil.isBlank(resultPo)){
			if(JcMatchStatusEnum.isUpdate(resultPo.getMatchStatus()))
				return;
			po.setId(resultPo.getId());
		    if (2 == matchType) {
               handlFBMq(po);
             }
		}
		
		sportAgainstInfoDaoMapper.merge(po);
//        if (ObjectUtil.isBlank(resultPo)) {
//            sportAgainstInfoDaoMapper.insert(po);
//        } else {
//            if (resultPo.getMatchStatus() == 7 || resultPo.getMatchStatus() == 9) {
//            	logger.info("修改竞技彩对阵明细sql 执行 update sql, 属性 : "+ JSONObject.toJSONString(po) );
//            	po.setId(resultPo.getId());
//                sportAgainstInfoDaoMapper.update(po);
//                if (2 == matchType) {
//                    handlFBMq(po);
//                }
//            }
//        }

//        updateYbfData(DateUtil.convertDateToStr(po.getStartTime(), DateUtil.DATE_FORMAT));
    }


//     add by cheng.chen 2017-10-28 竞足同步 调用竞篮一比分数据
//    /**
//     * 同步一步分数据
//     *
//     * @param start 比赛日期
//     */
//    private void updateYbfData(String start) {
//        String result = webClientManager.getJcBbMatchByDate(start);
//        List<YbfJclqMatch> ybfJclqMatch = JsonUtil.jsonToArray(result, YbfJclqMatch.class);
//        sportAgainstInfoService.updateBasketBallAnalysis(ybfJclqMatch);
//    }

}
