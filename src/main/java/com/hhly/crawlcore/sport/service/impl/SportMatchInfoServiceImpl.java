package com.hhly.crawlcore.sport.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.persistence.sport.dao.SportMatchInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportMatchSourceInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportMatchInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportMatchSourceInfoPO;
import com.hhly.crawlcore.sport.service.SportMatchInfoService;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @auth lgs on
 * @date 2017/2/9.
 * @desc 赛事信息Service实现类
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
@Service
public class SportMatchInfoServiceImpl implements SportMatchInfoService {
	
    private static final Logger log = LoggerFactory.getLogger(SportTeamInfoServiceImpl.class);
	
    @Autowired
    private SportMatchInfoDaoMapper sportMatchInfoDaoMapper;
    
    @Autowired
    private SportMatchSourceInfoDaoMapper sportMatchSourceInfoDaoMapper;
    
    @Autowired
    private MQUtils mqUtils;

    //v2.0版本的方法  start -- 请不要覆盖
    /**
     * 查询赛事
     *
     * @param po
     * @return
     */
    @Override
    public SportMatchInfoPO findMatch(SportMatchInfoPO po) {
        List<SportMatchInfoPO> resultList = sportMatchInfoDaoMapper.findMatch(po);
        if (ObjectUtil.isBlank(resultList)) {
            sportMatchInfoDaoMapper.insert(po);
            return po;
        }else{
        	if(resultList.size() > 1){
        		matchNumWarn(resultList);
        	}else{
        		po = resultList.get(0);
        	}
        }
        return po;
    }    
    
	@Override
	public SportMatchInfoPO getMatchInfo(SportMatchSourceInfoPO reqPO) {
		SportMatchInfoPO matchInfoPO = new SportMatchInfoPO();
		SportMatchSourceInfoPO paramPO = new SportMatchSourceInfoPO();
		try {
			String md5 = null;
			//获取赛事MD5值
			if(!ObjectUtil.isBlank(reqPO.getMatchName()) && !ObjectUtil.isBlank(reqPO.getMatchType()))
				md5 = SportLotteryUtil.getNameMd5(reqPO.getMatchName(), reqPO.getMatchType());
			SportDataSource sportDataSource = SportDataSource.getSportDataSource(reqPO.getSource());
			switch (sportDataSource) {
			case JC_OFFICIAL:
			case JIMI_OFFICIAL:
				//初始化需要查询对象
				if(!ObjectUtil.isBlank(reqPO.getMatchName())){
					matchInfoPO.setMatchFullName(reqPO.getMatchName());
					paramPO.setMatchName(reqPO.getMatchName());
				} else {
					matchInfoPO.setMatchShortName(reqPO.getMatchAbbrName());	
					paramPO.setMatchAbbrName(reqPO.getMatchAbbrName());
				}
				matchInfoPO.setMatchType(reqPO.getMatchType());
				matchInfoPO.setMd5Value(md5);
				paramPO.setMatchType(reqPO.getMatchType());
				paramPO.setSource(reqPO.getSource());
				break;
			case FIVEHUNDRED_OFFICIAL:
			case AIBO_OFFICIAL:
				matchInfoPO.setMatchShortName(reqPO.getMatchAbbrName());
				matchInfoPO.setMatchType(reqPO.getMatchType());
				paramPO.setMatchAbbrName(reqPO.getMatchAbbrName());
				paramPO.setMatchType(reqPO.getMatchType());
				paramPO.setSource(reqPO.getSource());				
				break;
			}
			SportMatchSourceInfoPO dataPO = sportMatchSourceInfoDaoMapper.find(paramPO);				
			if(ObjectUtil.isBlank(dataPO)){		
				List<SportMatchInfoPO> list = sportMatchInfoDaoMapper.findMatch(matchInfoPO);
			    if(ObjectUtil.isBlank(list)){
			    	if(sportDataSource.equals(SportDataSource.JC_OFFICIAL) || ((sportDataSource.equals(SportDataSource.AIBO_OFFICIAL) || 
			    			sportDataSource.equals(SportDataSource.JIMI_OFFICIAL))
			    			&& reqPO.getMatchType() != 1 && reqPO.getMatchType() != 2)){
				    	matchInfoPO.setMatchFullName(reqPO.getMatchName());
				    	matchInfoPO.setMatchShortName(reqPO.getMatchAbbrName());
				    	matchInfoPO.setMatchId(1l);
				    	matchInfoPO.setMatchColor("#000000");
				    	matchInfoPO.setModifyBy("crawl");
				        sportMatchInfoDaoMapper.insert(matchInfoPO);
				        reqPO.setMatchId(matchInfoPO.getId());
			    	}else{
				    	matchInfoPO = null;
//				    	mqUtils.sendWarnMessage("渠道 :" + sportDataSource.getName() + "赛事信息  : " + reqPO.getMatchName() == null ? reqPO.getMatchAbbrName() : reqPO.getMatchName()
//				    			+ "无法查询到益彩赛事信息,  请绑定相关赛事关系 !!!");
				    }
			    }else{
					if(list.size() > 1){
						matchNumWarn(list);
					}else{
						matchInfoPO = list.get(0);
						reqPO.setMatchId(matchInfoPO.getId());
					}			    	
			    }
				sportMatchSourceInfoDaoMapper.insert(reqPO);				
			}else{
				matchInfoPO = sportMatchInfoDaoMapper.findById(dataPO.getMatchId());
			}
		} catch (Exception e) {
			log.error("get 益彩赛事id 异常 , message : " + e.getMessage());
			//此处需要警告信息
		}
		return matchInfoPO;
	}
	
	private void matchNumWarn(List<SportMatchInfoPO> list){
        //如果发现有多条球队数据，报警，手动关联球队信息
        StringBuilder content = new StringBuilder("赛事信息出现多条重复数据：");
        for (SportMatchInfoPO po : list) {
            content.append(po.getMatchFullName()).append("【").append(po.getId()).append("】，");
        }
        mqUtils.sendWarnMessage(content.toString());
		log.info("赛事信息查询两条, 请删除相关多余数据 !!!");		
	}	
    //v2.0版本新增的方法  end -- 请不要覆盖
}
