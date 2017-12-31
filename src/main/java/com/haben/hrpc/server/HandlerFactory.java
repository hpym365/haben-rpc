package com.haben.hrpc.server;

import com.haben.hrpc.entity.RpcRequest;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 16:25
 * @Version: 1.0
 **/
public class HandlerFactory {
	public static IHandler createHandler(Object msg){
		IHandler handler = null;
		if(msg instanceof RpcRequest){
			handler = new RequestHandler();
		}


		return handler;
	}
}
