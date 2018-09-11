package com.hhly.crawlcore.base.thread;

import java.util.Queue;

/**
 * @desc 不规则定时任务处理类
 * @author jiangwei
 * @date 2017-2-12
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class RuleTimerHandler {
    /**
     * 比如：定时器执行240秒 0-60 10秒一次 60-180 5秒一次 180-240 10秒一次
     * 
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017-2-10 下午6:32:10
     * @param queue
     *            队列
     * @param taskInterval
     *            定时器真正执行周期（秒）
     * @param interval
     *            逻辑间隔时间（秒）
     * @param start
     *            开始时间 （秒）
     * @param end
     *            结束时间 （秒）
     */
    public static void calculateInterval(Queue<Boolean> queue,
            int taskInterval, int interval, int start, int end) {
        for (int i = start; i < end; i++) {
            if (i % taskInterval == 0) {
                if (i % interval == 0) {
                    queue.offer(Boolean.TRUE);
                } else {
                    queue.offer(Boolean.FALSE);
                }
            }
        }
    }
}
