package com.hhly.crawlcore.database.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.crawlcore.database.service.FbScheduleService;
import com.hhly.crawlcore.database.service.YbfFootBallOddsService;
import com.hhly.crawlcore.persistence.database.dao.FbSchedulePOMapper;
import com.hhly.crawlcore.persistence.database.po.FbSchedulePO;
import com.hhly.skeleton.base.util.JsonUtil;
import com.hhly.skeleton.base.util.ObjectUtil;

/**
 * @author lgs on
 * @version 1.0
 * @desc 足球赛事对阵实现类
 * @date 2017/11/18.
 * @company 益彩网络科技有限公司
 */
@Service
public class FbScheduleServiceImpl implements FbScheduleService {

    @Autowired
    private FbSchedulePOMapper fbSchedulePOMapper;

    @Autowired
    private YbfFootBallOddsService ybfFootBallOddsService;

    private static Logger logger = LoggerFactory.getLogger(FbScheduleServiceImpl.class);

    /**
     * 初始化赛事
     *
     * @return
     */
    @Override
    public int initFbSchedule() throws IOException, URISyntaxException {
        List<Map<String, String>> map = fbSchedulePOMapper.findInitSchedule();

        for (Map<String, String> temp : map) {
            FbSchedulePO po = new FbSchedulePO();
            po.setSourceId(temp.get("matchInfoUrl"));
            po.setSourceType(1);
            saveFbSchedule(po);
        }

        for (Map<String, String> temp : map) {
            fbSchedulePOMapper.updateSportAgainstInfoByMatchInfoUrl(temp);
            ybfFootBallOddsService.saveOddsFbEuropeOddsPOs(temp.get("matchAnalysisUrl"));
        }

        return 1;
    }

    /**
     * 获取赛事
     *
     * @param po
     * @return
     */
    @Override
    public FbSchedulePO findFbSchedule(FbSchedulePO po) {
        FbSchedulePO result = fbSchedulePOMapper.findScheduleBySourceId(po);

        if (result == null) {
            fbSchedulePOMapper.insert(po);
            result = fbSchedulePOMapper.findScheduleBySourceId(po);
        }

        return result;
    }

    /**
     * 新增赛事
     *
     * @param po
     * @return
     */
    @Override
    public int saveFbSchedule(FbSchedulePO po) {
        FbSchedulePO temp = fbSchedulePOMapper.findScheduleBySourceId(po);
        int i = 0;
        if (ObjectUtil.isBlank(temp)) {
            try {
                i = fbSchedulePOMapper.insert(po);
            } catch (Exception e) {
                logger.info("init schedule error po" + JsonUtil.objectToJson(po) + " message=====" + e.getMessage());
            }
        }
        return i;
    }


}
