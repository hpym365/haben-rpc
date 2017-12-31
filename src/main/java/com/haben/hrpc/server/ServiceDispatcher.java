package com.haben.hrpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 16:00
 * @Version: 1.0
 **/
public class ServiceDispatcher {
	private static ServiceDispatcher serviceDispatcher = null;

	private Map<Class<?>, IHandler> handlerMap = new ConcurrentHashMap<>();

	private ServiceDispatcher() {
	}

	public static ServiceDispatcher getInstance() {
		if (serviceDispatcher == null) {
			synchronized (ServiceDispatcher.class) {
				if (serviceDispatcher == null) {
					serviceDispatcher = new ServiceDispatcher();
				}
			}
		}
		return serviceDispatcher;
	}

	public Object dispatch(Object msg) throws NoSuchMethodException {
		return invoke(msg);
	}

	public Object invoke(Object msg) {
		IHandler handler = getHandler(msg);
		Object res = handler.invoke(msg);
		return res;
	}

	public IHandler getHandler(Object msg) {
		IHandler handler = handlerMap.get(msg.getClass());
		if (handler == null) {
			handler = HandlerFactory.createHandler(msg);
			handlerMap.put(msg.getClass(), handler);
		}
		return handler;
	}
}
