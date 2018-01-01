package com.haben.hrpc.loadbalance;

import com.haben.hrpc.client.ClientHandler;

import java.util.Map;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2018-01-01 23:57
 * @Version: 1.0
 **/
public interface ILoadBalance {
	/**
	* @Author: Haben
	* @Description: 获取handler
	* @Date: 2018/1/1 23:58
	* @param:
	* @Return:
	**/
	public ClientHandler getClientHandler(Map<ClientHandler, Integer> handlerMap);
}
