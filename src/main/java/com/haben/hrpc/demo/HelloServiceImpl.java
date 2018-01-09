package com.haben.hrpc.demo;

import com.haben.hrpc.annotation.RpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 04:46
 * @Version: 1.0
 **/
@RpcService(service = HelloService.class)
public class HelloServiceImpl implements HelloService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public String say(String str) {
		return "okok";
	}

	@Override
	public String say500(String str) {

		long begin = System.currentTimeMillis();
		try {
			Lock lock = new ReentrantLock();
			Condition condition = lock.newCondition();
			lock.lock();
			condition.await(500, TimeUnit.MILLISECONDS);
			lock.unlock();
			return "okokok";
		} catch (Exception e) {
			e.printStackTrace();
			return "nonono";
		}
	}

	@Override
	public String say200(String str) {

		long begin = System.currentTimeMillis();
		try {
			Lock lock = new ReentrantLock();
			Condition condition = lock.newCondition();
			lock.lock();
			condition.await(200, TimeUnit.MILLISECONDS);
			lock.unlock();
			return "okokok";
		} catch (Exception e) {
			e.printStackTrace();
			return "nonono";
		}
	}

	@Override
	public String say1(String str) {

		long begin = System.currentTimeMillis();
		try {
			Lock lock = new ReentrantLock();
			Condition condition = lock.newCondition();
			lock.lock();
			condition.await(50, TimeUnit.MILLISECONDS);
			lock.unlock();
			return "okokok";
		} catch (Exception e) {
			e.printStackTrace();
			return "nonono";
		}
	}
}
