package com.haben.hrpc.server;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 15:39
 * @Version: 1.0
 **/
public class RpcServiceList {
	public static Map<String,Object> serviceMap = new HashMap<>();

	public static Object getService(String interfaceName){
		return  serviceMap.get(interfaceName);
	}

}
