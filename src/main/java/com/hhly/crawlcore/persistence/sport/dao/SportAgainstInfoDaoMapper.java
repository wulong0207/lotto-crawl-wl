package com.hhly.crawlcore.persistence.sport.dao;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.hhly.crawlcore.persistence.sport.po.SportAgainstInfoPO;
import com.hhly.skeleton.lotto.base.sport.bo.JcAvgOddsBO;

public interface SportAgainstInfoDaoMapper {

    /**
     * 查询老足彩对阵
     * @return
     */
    List<SportAgainstInfoPO> findSfcMatch();

    /**
     * 根据条件查找
     *
     * @param sportAgainstInfoPO
     * @return
     */
    List<SportAgainstInfoPO> findCondition(SportAgainstInfoPO sportAgainstInfoPO);

    /**
     * 根据官方id批量修改赛事信息。
     *
     * @param po
     * @return
     */
    int updateMatchInfoByOfficialId(List<SportAgainstInfoPO> po);

    /**
     * 更新平均欧赔
     * @param list
     * @return
     */
    int updateAvgOdds(List<JcAvgOddsBO> list);

    
    //v2.0版本,数据抓取--------start
    
    int merge(SportAgainstInfoPO po);
    
    /**
     * 新增对阵数据
     * @param po
     * @return
     */
    int insert(SportAgainstInfoPO po);
    
    /**
     * 修改对阵数据
     * @param po
     * @return
     * @date 2017年11月29日上午10:42:22
     * @author cheng.chen
     */
    int update(SportAgainstInfoPO po);
    
    /**
     * 批量修改对阵数据
     * @param pos
     * @date 2017年12月8日下午4:32:23
     * @author cheng.chen
     */
    void updateBatch(List<SportAgainstInfoPO> pos);
    
    /**
     * 修改暂定赛程
     * @param po
     * @return
     */
    int updateInterimMatch(SportAgainstInfoPO po);
    
    /**
     * 同步竞足信息给老足彩
     * 
     * @date 2017年12月22日上午11:11:46
     * @author cheng.chen
     */
    void updateSynFbMatchInfo();
    
    /**
     * 根据彩种查询已开售对阵
     *
     * @param lotteryCode
     * @return
     */
    List<SportAgainstInfoPO> findSaleMatchList(Integer lotteryCode);
    
    /**
     * 根据彩种查询已开售对阵彩期集合
     * @param lotteryCode
     * @return
     * @date 2017年12月21日下午6:01:37
     * @author cheng.chen
     */
    List<String> findSaleMatchIssueList(@Param("lotteryCode") Integer lotteryCode);
    
    /**
     * 通过对阵对象查询对阵id
     * @param po
     * @return
     * @date 2017年11月22日上午9:57:56
     * @author cheng.chen
     */
    Long findId(SportAgainstInfoPO po);
    
    /**
     * 通过编码查询对阵id
     * @param lotteryCode
     * @param issueCode
     * @param systemCode
     * @return
     * @date 2017年11月22日上午9:57:45
     * @author cheng.chen
     */
    Long findIdByCode(@Param("lotteryCode") Integer lotteryCode, @Param("issueCode") String issueCode, @Param("systemCode") String systemCode);
    
    /**
     * 通过编码查询对阵详情
     * @param lotteryCode
     * @param issueCode
     * @param officialMatchCode
     * @return
     * @date 2017年12月7日下午3:21:13
     * @author cheng.chen
     */
    SportAgainstInfoPO findByCode(@Param("lotteryCode") Integer lotteryCode, @Param("issueCode") String issueCode, @Param("systemCode") String systemCode);
    
    
    /**
     * 通过彩种编码查询销售中未同步比赛年月日集合
     * @param lotteryCode
     * @return
     * @date 2017年12月8日下午3:08:15
     * @author cheng.chen
     */
    Set<String> findSynSaleMatchTimeList(@Param("lotteryCode") Integer lotteryCode);
    
    /**
     * 通过彩种编码查询销售中未同步比赛集合
     * @param lotteryCode
     * @return
     * @date 2017年12月8日下午3:08:15
     * @author cheng.chen
     */
    @MapKey("officialId")
    Map<String, SportAgainstInfoPO> findSynSaleMatch(@Param("lotteryCode") Integer lotteryCode);    
    //v2.0版本,数据抓取--------end
}