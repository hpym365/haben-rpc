package com.haben.hrpc.loadbalance;

import com.haben.hrpc.client.ClientHandler;

import java.util.*;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-25 上午11:12
 * @Version: 1.0
 **/
public class RandomLoadBalnce implements ILoadBalance {

	@Override
	public ClientHandler getClientHandler(Map<ClientHandler, Integer> map) {
		//重建一个Map，避免服务器的上下线导致的并发问题
		Map<ClientHandler, Integer> handlerMap = new HashMap<>();
		handlerMap.putAll(map);
		System.out.println("put之后==equals判断handlerMap.equals(map):"+handlerMap.equals(map));
		//获取Ip地址List
		Set<ClientHandler> keySet = handlerMap.keySet();
		List<ClientHandler> keyList = new ArrayList<>();
		keyList.addAll(keySet);
		Random random = new Random();
		int pos = random.nextInt(keyList.size());
		return keyList.get(pos);
	}
}
