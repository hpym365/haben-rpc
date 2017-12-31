package com.haben.hrpc.server;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 16:14
 * @Version: 1.0
 **/
public interface IHandler {
	public Object invoke(Object msg);
}
