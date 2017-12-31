package com.haben.hrpc.demo;

import com.haben.hrpc.annotation.RpcService;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 04:46
 * @Version: 1.0
 **/
@RpcService(service = HelloService.class)
public class HelloServiceImpl implements HelloService {
	@Override
	public String say(String str) {
		System.out.println("client say:" + str);
		return str;
	}
}
