package com.haben.hrpc.client;

import io.netty.bootstrap.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 03:04
 * @Version: 1.0
 **/
public class RpcClient {
	private static final Logger log = LoggerFactory.getLogger(RpcClient.class);
	private Bootstrap bootstrap = new Bootstrap();
	private int retry = 0;
	private final int MAX_RETRY = 100;


//	public static Map<String,ClientHandler> handlerMap = new ConcurrentHashMap<>();

	public RpcClient() {
//		ClientConnection instance = ClientConnection.getInstance();
////		instance.setInetSocketAddress(ip,port);
	}

}
