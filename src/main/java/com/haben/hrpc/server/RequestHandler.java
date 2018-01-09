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
	ThreadLocal<RpcResponse> threadLocalRpcResponse = new ThreadLocal<RpcResponse>(){
		@Override
		protected RpcResponse initialValue() {
			return   new RpcResponse();
		}
	};
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

		// new RpcResponse();10825  10341   50w 42948 42655  42171
		//request 修改了tl之后   50w    42438
		RpcResponse rpcResponse =threadLocalRpcResponse.get();

		//10136 10375     50w   43147  44816 44715
//		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.setRequestId(requestId);
		Object invoke = null;
		try {
			invoke = method.invoke(service, parameters);
		} catch (Throwable throwable) {
			rpcResponse.setMsg("throwable error:"+throwable.toString());
			System.out.println("===================");
			return rpcResponse;
		}
		rpcResponse.setResult(invoke);
		System.out.println("request handler request:"+request.getRequestId());
		return rpcResponse;
	}
}
