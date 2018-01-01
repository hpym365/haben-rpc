package com.haben.hrpc.server;

import com.haben.hrpc.entity.RpcRequest;
import com.haben.hrpc.entity.RpcResponse;

import java.lang.reflect.Method;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 16:01
 * @Version: 1.0
 **/
public class RequestHandler implements IHandler {
	@Override
	public Object invoke(Object msg) {
		RpcRequest request = (RpcRequest) msg;
		String requestId = request.getRequestId();

		String className = request.getClassName();
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();

		Object service = RpcServiceList.getService(className);

		Method method = null;
		try {
			method = service.getClass().getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		System.out.println("method:" + method);

		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.setRequestId(requestId);
		Object invoke = null;
		try {
			invoke = method.invoke(service, parameters);
		} catch (Throwable throwable) {
			rpcResponse.setMsg("throwable error:"+throwable.toString());
			return rpcResponse;
		}
		rpcResponse.setResult(invoke);
		return rpcResponse;
	}
}
