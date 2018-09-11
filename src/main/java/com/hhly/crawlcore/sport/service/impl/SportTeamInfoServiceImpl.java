package com.hhly.crawlcore.sport.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.persistence.sport.dao.SportTeamInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.dao.SportTeamSourceInfoDaoMapper;
import com.hhly.crawlcore.persistence.sport.po.SportTeamInfoPO;
import com.hhly.crawlcore.persistence.sport.po.SportTeamSourceInfoPO;
import com.hhly.crawlcore.sport.service.SportTeamInfoService;
import com.hhly.crawlcore.sport.util.MQUtils;
import com.hhly.crawlcore.sport.util.SportLotteryUtil;
import com.hhly.skeleton.base.common.SportEnum.SportDataSource;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @auth lgs on
 * @date 2017/2/9.
 * @desc 球队信息Service实现类
 * @compay 益彩网络科技有限公司
 * @vsersion 1.0
 */
@Service
public class SportTeamInfoServiceImpl implements SportTeamInfoService {

    private static final Logger log = Logger.getLogger(SportTeamInfoServiceImpl.class);

    @Autowired
    private SportTeamInfoDaoMapper sportTeamInfoDaoMapper;
    
    @Autowired
    private SportTeamSourceInfoDaoMapper sportTeamSourceInfoDaoMapper;

    @Autowired
    private MQUtils mqUtils;

    
    //v2.0版本的方法  start -- 请不要覆盖
    /**
     * 获取球队名称
     *
     * @return 球队信息
     */
    //@Cacheable(cacheManager = "cacheManager", value = "teamInfo", key = "#po.md5Value")
    @Override
    public synchronized SportTeamInfoPO findTeam(SportTeamInfoPO po) {
        try {
            List<SportTeamInfoPO> list = sportTeamInfoDaoMapper.findTeam(po);
            //过滤北单的数据，不报警
            if (!ObjectUtil.isBlank(list) && list.size() > 1 && !po.getCreateBy().contains("aibo123")) {
            	teamNumWarn(list);
            } else if (!ObjectUtil.isBlank(list)) {
                return list.get(0);
            } else {
                //如果根据全称或简称查询不到球队就新增一条球队信息
                if (po.getCreateBy().contains("500.com")) {
                    po.setTeamFullName(po.getTeamShortName());
                }
                sportTeamInfoDaoMapper.insert(po);
                return po;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
		return po;
    }    
    
	@Override
	public SportTeamInfoPO getTeamInfo(SportTeamSourceInfoPO reqPO) {
		SportTeamInfoPO teamInfoPO = new SportTeamInfoPO();
		SportTeamSourceInfoPO paramPO = new SportTeamSourceInfoPO();
		try {
			String md5 = null;
			if(!ObjectUtil.isBlank(reqPO.getTeamName()) && !ObjectUtil.isBlank(reqPO.getTeamType()))
				md5 = SportLotteryUtil.getNameMd5(reqPO.getTeamName(), reqPO.getTeamType());
			SportDataSource sportDataSource = SportDataSource.getSportDataSource(reqPO.getSource());
			switch (sportDataSource) {
			case JC_OFFICIAL:
			case AIBO_OFFICIAL:
			case JIMI_OFFICIAL:
				teamInfoPO.setTeamFullName(reqPO.getTeamName());
				teamInfoPO.setTeamType(reqPO.getTeamType());
				teamInfoPO.setMd5Value(md5);
				paramPO.setTeamName(reqPO.getTeamName());
				paramPO.setTeamType(reqPO.getTeamType());
				paramPO.setSource(reqPO.getSource());
				break;
			case FIVEHUNDRED_OFFICIAL:
				teamInfoPO.setTeamShortName(reqPO.getTeamAbbrName());
				teamInfoPO.setTeamType(reqPO.getTeamType());
				paramPO.setTeamAbbrName(reqPO.getTeamAbbrName());
				paramPO.setTeamType(reqPO.getTeamType());
				paramPO.setSource(reqPO.getSource());				
				break;
			}
			SportTeamSourceInfoPO dataPO = sportTeamSourceInfoDaoMapper.find(paramPO);
			if(ObjectUtil.isBlank(dataPO)){
				List<SportTeamInfoPO> list = sportTeamInfoDaoMapper.findTeam(teamInfoPO);
			    if(ObjectUtil.isBlank(list)){
			    	if(sportDataSource.equals(SportDataSource.JC_OFFICIAL) || ((sportDataSource.equals(SportDataSource.AIBO_OFFICIAL) || 
			    			sportDataSource.equals(SportDataSource.JIMI_OFFICIAL))
			    			&& reqPO.getTeamType() != 1 && reqPO.getTeamType() != 2)){
				    	teamInfoPO.setTeamFullName(reqPO.getTeamName());
				    	teamInfoPO.setTeamShortName(reqPO.getTeamAbbrName());
				    	teamInfoPO.setTeamId(1l);
				    	teamInfoPO.setCreateBy("crawl");
				    	teamInfoPO.setModifyBy("crawl");
				    	sportTeamInfoDaoMapper.insert(teamInfoPO);
				    	reqPO.setTeamId(teamInfoPO.getId());
			    	}else{
				    	teamInfoPO = null;
//					    mqUtils.sendWarnMessage("渠道 :" + sportDataSource.getName() + "球队信息  : " + reqPO.getTeamName() == null ? reqPO.getTeamAbbrName() : reqPO.getTeamName()
//					    		+ "无法查询到益彩球队信息,  请绑定相关球队关系 !!!");			    		
			    	}
			    }else{
			    	if(list.size() > 1){
						teamNumWarn(list);
					}else{
						teamInfoPO = list.get(0);
						reqPO.setTeamId(teamInfoPO.getId());
					}
			    }
				sportTeamSourceInfoDaoMapper.insert(reqPO);				
			}else{
				teamInfoPO = sportTeamInfoDaoMapper.findById(dataPO.getTeamId());
			}
		} catch (Exception e) {
			log.error("get 益彩球队id 异常 , message : " + e.getMessage());
		}
		return teamInfoPO;
	}
	
	private void teamNumWarn(List<SportTeamInfoPO> list){
        //如果发现有多条球队数据，报警，手动关联球队信息
        StringBuilder content = new StringBuilder("球队信息出现多条重复数据：");
        for (SportTeamInfoPO po : list) {
            content.append(po.getTeamShortName()).append("【").append(po.getId()).append("】，");
        }
        mqUtils.sendWarnMessage(content.toString());
		log.info("球队信息查询两条, 请删除相关多余数据 !!!");		
	}
	
    //v2.0版本的方法  end -- 请不要覆盖
	
	public static void main(String[] args) {
		SportDataSource sportDataSource = SportDataSource.getSportDataSource((short) 1);
		System.out.println(sportDataSource.equals(SportDataSource.JC_OFFICIAL));
	}
}
