package com.hhly.crawlcore.base.thread;

/**
 * @desc 定时任务，执行具体任务接口
 * @author jiangwei
 * @date 2017-2-10
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface ITask{
    
    /**
     * 用定时任务执行
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017-2-10 下午2:22:14
     * @return true 继续下次定时任务 false 马上结束任务
     */
    boolean execute();
    /**
     * 当定时任务关闭，将执行该方法
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年2月24日 下午2:46:53
     */
    void destroy();
}
