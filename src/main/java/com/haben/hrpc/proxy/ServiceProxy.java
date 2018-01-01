package com.haben.hrpc.proxy;


import com.haben.hrpc.client.ClientConnection;
import com.haben.hrpc.client.ClientHandler;
import com.haben.hrpc.entity.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 04:49
 * @Version: 1.0
 **/
public class ServiceProxy<T> implements InvocationHandler {

	private Class<T> tClass;

	public ServiceProxy(Class<T> tClass) {
		this.tClass = tClass;
	}

	public static <T> T create(Class<T> tClass) {
		Class<T>[] interfaces = new Class[]{tClass};
		ServiceProxy serviceProxy = new ServiceProxy(tClass);
		Object o = Proxy.newProxyInstance(tClass.getClassLoader(), interfaces, serviceProxy);
		return (T) o;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		System.out.println("调用invoke 啦啦啦");
//		ClientHandler clientHandler = new ClientHandler();
		RpcRequest request = new RpcRequest();
		request.setRequestId(UUID.randomUUID().toString());
		request.setClassName(method.getDeclaringClass().getName());
		request.setMethodName(method.getName());
		request.setParameterTypes(method.getParameterTypes());
		request.setParameters(args);
//		System.out.println("request id :"+request.getRequestId());

		ClientHandler clientHandler = ClientConnection.getInstance().getClientHandler();
		//没有可用的服务
		if (clientHandler == null) {
			return null;
		}
		clientHandler.send(request);
		Object rpcRes = clientHandler.getRpcRes(request);
		return rpcRes;
//		return "123";
	}
}
