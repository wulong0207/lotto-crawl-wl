package com.hhly.crawlcore.persistence.sport.dao;

import com.hhly.crawlcore.persistence.sport.po.SportMatchOddPO;
import com.hhly.skeleton.lotto.base.sport.bo.JcAvgOddsBO;

import java.util.List;

/**
 * 竞彩平均欧赔
 */
public interface SportMatchOddDaoMapper {
    int insert(List<SportMatchOddPO> list);

    List<JcAvgOddsBO> selectAvgOddsByMatchId(List<String> list);
}