package com.hhly.crawlcore.sport.quartz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSONObject;
import com.hhly.crawlcore.base.thread.ThreadPoolManager;
import com.hhly.crawlcore.base.util.RedisUtil;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjGoalPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjHfWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjScorePO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjUdsdPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjWdfPO;
import com.hhly.crawlcore.persistence.sport.po.SportDataBjWfPO;
import com.hhly.crawlcore.sport.service.SportDataBjGoalService;
import com.hhly.crawlcore.sport.service.SportDataBjHfWdfService;
import com.hhly.crawlcore.sport.service.SportDataBjScoreService;
import com.hhly.crawlcore.sport.service.SportDataBjUdsService;
import com.hhly.crawlcore.sport.service.SportDataBjWdfService;
import com.hhly.crawlcore.sport.service.SportDataBjWfService;
import com.hhly.skeleton.base.util.StringUtil;

/**
 * @version 1.0
 * @auth chenkangning
 * @date 2017/7/10
 * @desc 北京单场SP值接口定时任务
 * @compay 益彩网络科技有限公司
 */
@Component
public class SportBjdcQuartz {

    private static final Logger logger = LoggerFactory.getLogger(SportBjdcQuartz.class);

    /**
     * 北单玩法redis常量key
     */
    public interface HowToPlay {
        /**
         * 胜负过关
         */
        String DC_SFGG = "aibo:spider:wf:list";
        /**
         * 胜平负
         */
        String DC_SFPF = "aibo:spider:wdf:list";
        /**
         * 单场比分
         */
        String DC_BFF = "aibo:spider:singlewdf:list";
        /**
         * 总进球数
         */
        String DC_ZJQF = "aibo:spider:totalscore:list";
        /**
         * 半全场胜平负
         */
        String DC_BQCF = "aibo:spider:hfwdf:list";
        /**
         * 上下盘单双数
         */
        String DC_SXDF = "aibo:spider:updownscore:list";
    }

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SportDataBjGoalService sportDataBjGoalService;

    @Resource
    private SportDataBjWfService sportDataBjWfService;

    @Resource
    private SportDataBjWdfService sportDataBjWdfService;

    @Resource
    private SportDataBjScoreService sportDataBjScoreService;

    @Resource
    private SportDataBjHfWdfService sportDataBjHfWdfService;

    @Resource
    private SportDataBjUdsService sportDataBjUdsService;


    /**
     * cron表达式：* * * * * *（共6位，使用空格隔开，具体如下）
     * cron表达式：*(秒0-59) *(分钟0-59) *(小时0-23) *(日期1-31) *(月份1-12或是JAN-DEC) *(星期1-7或是SUN-SAT)
     */
    //@Scheduled(cron = "*/30 * *  * * ? ")   //延迟10秒启动，然后每隔1分钟执行一次
    public void bjdcSpTask() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.debug(sdf.format(new Date()) + "*********北京单场Quartz任务进入" + "****");

        sfgg();
        sfpf();
        bff();
        zjqf();
        bqcf();
        sxdf();

    }


    /**
     * 上下盘单双数
     */
    public void sxdf() {
        ThreadPoolManager.execute("北京单场上下盘单双数SP及对阵信息", new Runnable() {
            @Override
            public void run() {
                try {
                    String redisKey = HowToPlay.DC_SXDF;
                    Long size = redisUtil.length(redisKey);
                    List<?> list = redisUtil.range(redisKey, 0, size.intValue());
                    redisUtil.delObj(redisKey);
                    String value;
                    for (int j = 0; j < size; j++) {
                        value = list.get(j).toString();
                        if (!StringUtil.isBlank(value)) {
                            SportDataBjUdsdPO sportDataBjUdsdPO = JSONObject.parseObject(value, SportDataBjUdsdPO.class);
                            String union = DigestUtils.md5DigestAsHex(sportDataBjUdsdPO.toString().getBytes("UTF-8"));
                            sportDataBjUdsdPO.setMd5Value(union);
                            sportDataBjUdsService.save(sportDataBjUdsdPO);
                            //删除redis对应数据
                            //redisUtil.remove(redisKey, THREAD_SIZE, value);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }


    /**
     * 半全场胜平负
     */
    private void bqcf() {
        ThreadPoolManager.execute("北京单场半全场胜平负SP及对阵信息", new Runnable() {
            @Override
            public void run() {
                try {
                    //String string = redisUtil.pop(HowToPlay.DC_BQCF);
                    String redisKey = HowToPlay.DC_BQCF;
                    Long size = redisUtil.length(redisKey);
                    List<?> list = redisUtil.range(redisKey, 0, size.intValue());
                    redisUtil.delObj(redisKey);
                    String value;
                    for (int i = 0; i < size; i++) {
                        value = list.get(i).toString();
                        if (!StringUtil.isBlank(value)) {
                            SportDataBjHfWdfPO sportDataBjHfWdfPO = JSONObject.parseObject(value, SportDataBjHfWdfPO.class);
                            String union = DigestUtils.md5DigestAsHex(sportDataBjHfWdfPO.toString().getBytes("UTF-8"));
                            sportDataBjHfWdfPO.setMd5Value(union);
                            sportDataBjHfWdfService.save(sportDataBjHfWdfPO);
                            //删除redis对应数据
                            //redisUtil.remove(redisKey, THREAD_SIZE, value);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

    /**
     * 总进球数
     */
    private void zjqf() {
        ThreadPoolManager.execute("北京单场总进球数SP及对阵信息", new Runnable() {
            @Override
            public void run() {
                try {
                    //String string = redisUtil.pop(HowToPlay.DC_ZJQF);
                    String redisKey = HowToPlay.DC_ZJQF;
                    Long size = redisUtil.length(redisKey);
                    List<?> list = redisUtil.range(redisKey, 0, size.intValue());
                    redisUtil.delObj(redisKey);
                    String value;
                    for (int i = 0; i < size; i++) {
                        value = list.get(i).toString();
                        if (!StringUtil.isBlank(value)) {
                            SportDataBjGoalPO sportDataBjGoalPO = JSONObject.parseObject(value, SportDataBjGoalPO.class);
                            String union = DigestUtils.md5DigestAsHex(sportDataBjGoalPO.toString().getBytes("UTF-8"));
                            sportDataBjGoalPO.setMd5Value(union);
                            sportDataBjGoalService.save(sportDataBjGoalPO);
                            //删除redis对应数据
                            //redisUtil.remove(redisKey, THREAD_SIZE, value);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);

    }

    /**
     * 单场比分
     */
    private void bff() {
        ThreadPoolManager.execute("北京单场单场比分SP及对阵信息", new Runnable() {
            @Override
            public void run() {
                try {
                    //String string = redisUtil.pop(HowToPlay.DC_BFF);
                    String redisKey = HowToPlay.DC_BFF;
                    Long size = redisUtil.length(redisKey);
                    List<?> list = redisUtil.range(redisKey, 0, size.intValue());
                    redisUtil.delObj(redisKey);
                    String value;
                    for (int i = 0; i < size; i++) {
                        value = list.get(i).toString();
                        if (!StringUtil.isBlank(value)) {
                            SportDataBjScorePO sportDataBjScorePO = JSONObject.parseObject(value, SportDataBjScorePO.class);
                            String union = DigestUtils.md5DigestAsHex(sportDataBjScorePO.toString().getBytes("UTF-8"));
                            sportDataBjScorePO.setMd5Value(union);
                            sportDataBjScoreService.save(sportDataBjScorePO);
                            //删除redis对应数据
                            //redisUtil.remove(redisKey, THREAD_SIZE, value);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }


    /**
     * 胜平负
     */
    private void sfpf() {
        ThreadPoolManager.execute("北京单场胜平负SP及对阵信息", new Runnable() {
            @Override
            public void run() {
                try {
                    //String string = redisUtil.pop(HowToPlay.DC_SFPF);
                    String redisKey = HowToPlay.DC_SFPF;
                    Long size = redisUtil.length(redisKey);
                    List<?> list = redisUtil.range(redisKey, 0, size.intValue());
                    redisUtil.delObj(redisKey);
                    String value;
                    for (int i = 0; i < size; i++) {
                        value = list.get(i).toString();
                        if (!StringUtil.isBlank(value)) {
                            SportDataBjWdfPO sportDataBjWdfPO = JSONObject.parseObject(value, SportDataBjWdfPO.class);
                            String union = DigestUtils.md5DigestAsHex(sportDataBjWdfPO.toString().getBytes("UTF-8"));
                            sportDataBjWdfPO.setMd5Value(union);
                            sportDataBjWdfService.save(sportDataBjWdfPO);
                            //删除redis对应数据
                            //redisUtil.remove(redisKey, THREAD_SIZE, value);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

    /**
     * 胜负过关
     */
    private void sfgg() {
        ThreadPoolManager.execute("北京单场胜负过关SP及对阵信息", new Runnable() {
            @Override
            public void run() {
                try {
                    //String string = redisUtil.pop(HowToPlay.DC_SFGG);
                    String redisKey = HowToPlay.DC_SFGG;
                    Long size = redisUtil.length(redisKey);
                    List<?> list = redisUtil.range(redisKey, 0, size.intValue());
                    redisUtil.delObj(redisKey);
                    String value;
                    for (int i = 0; i < size; i++) {
                        value = list.get(i).toString();
                        if (!StringUtil.isBlank(value)) {
                            SportDataBjWfPO sportDataBjBasePO = JSONObject.parseObject(value, SportDataBjWfPO.class);
                            String union = DigestUtils.md5DigestAsHex(sportDataBjBasePO.toString().getBytes("UTF-8"));
                            sportDataBjBasePO.setMd5Value(union);
                            sportDataBjWfService.save(sportDataBjBasePO);
                            //删除redis对应数据
                            //redisUtil.remove(redisKey, THREAD_SIZE, value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }


}
