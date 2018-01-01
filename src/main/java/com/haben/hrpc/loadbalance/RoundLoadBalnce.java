package com.haben.hrpc.loadbalance;

import com.haben.hrpc.client.ClientHandler;

import java.util.*;

/**
 * @Author: Haben
 * @Description:轮询
 * @Date: 2017-12-25 上午11:12
 * @Version: 1.0
 **/
public class RoundLoadBalnce implements ILoadBalance{

	private static Integer pos = 0;

	@Override
	public ClientHandler getClientHandler(Map<ClientHandler, Integer> map) {
		//重建一个Map，避免服务器的上下线导致的并发问题
		Map<ClientHandler, Integer> handlerMap = new HashMap<>();
		handlerMap.putAll(map);

		Set<ClientHandler> keySet = handlerMap.keySet();
		List<ClientHandler> keyList = new ArrayList<>();
		keyList.addAll(keySet);
		ClientHandler clientHandler;
		synchronized (this) {
			if (pos >= keySet.size()) {
				System.out.println("keySet.size() -pos- keyList.size():"+keySet.size()+"-"+pos+"-"+keyList.size());
				pos = 0;
			}
			clientHandler = keyList.get(pos);
			pos++;
		}
		return clientHandler;
	}
}
