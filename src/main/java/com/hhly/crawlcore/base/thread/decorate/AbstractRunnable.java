package com.hhly.crawlcore.base.thread.decorate;

/**
 * @desc 抽象类对runable接口进行装饰
 * @author jiangwei
 * @date 2017年2月27日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public abstract class AbstractRunnable implements Runnable {
	//装饰接口
    private final Runnable runnable;

	public AbstractRunnable( Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void run() {
		runnable.run();
	}

	
}
