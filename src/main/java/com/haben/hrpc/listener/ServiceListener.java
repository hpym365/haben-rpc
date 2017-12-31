package com.haben.hrpc.listener;

import com.haben.hrpc.annotation.RpcService;
import com.haben.hrpc.server.RpcServiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 15:31
 * @Version: 1.0
 **/
@Configuration
public class ServiceListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
		Map<String, Object> serviceMap = new HashMap<>();
		for (Object bean : beans.values()) {
			// 使用接口的全名作为参数
			String className = bean.getClass().getAnnotation(RpcService.class).service().getName();
			serviceMap.put(className, bean);
		}
		RpcServiceList.serviceMap = serviceMap;
	}
}
