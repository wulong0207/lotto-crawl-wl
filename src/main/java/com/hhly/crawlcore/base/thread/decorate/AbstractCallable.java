package com.hhly.crawlcore.base.thread.decorate;

import java.util.concurrent.Callable;

/**
 * @desc Callable 类型包装
 * @author jiangwei
 * @date 2017年3月10日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class AbstractCallable<T> implements Callable<T> {
	private Callable<T> callable;

	public AbstractCallable(Callable<T> callable) {
		this.callable = callable;
	}

	@Override
	public T call() throws Exception {
		return callable.call();
	}

}
