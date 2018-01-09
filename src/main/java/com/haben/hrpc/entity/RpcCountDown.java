package com.haben.hrpc.entity;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2018-01-04 上午10:26
 * @Version: 1.0
 **/
public class RpcCountDown {
	private RpcRequest request;
	private RpcResponse response;
	private CountDownLatch countDownLatch;

	public RpcCountDown() {
		this.countDownLatch = new CountDownLatch(1);
	}

	public void countDown(){
		countDownLatch.countDown();
	}

	public RpcRequest getRequest() {
		return request;
	}

	public void setRequest(RpcRequest request) {
		this.request = request;
	}

	public Object getResponse() throws InterruptedException {
		countDownLatch.await();
		Object result = response.getResult();
		if (result == null) {
			result = response.getMsg();
		}
		return result;
	}

	public void setResponse(RpcResponse response) {

		this.response = response;
		countDownLatch.countDown();
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

//	public void setCountDownLatch(CountDownLatch countDownLatch) {
//		this.countDownLatch = countDownLatch;
//	}
}
