package com.haben.hrpc.client;

import com.haben.hrpc.entity.RpcCountDown;
import com.haben.hrpc.entity.RpcRequest;
import com.haben.hrpc.entity.RpcResponse;
import io.netty.channel.*;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 11:55
 * @Version: 1.0
 **/
public class ClientHandler extends SimpleChannelInboundHandler {

	private Channel channel;

	private Map<String, Object> rpcRes = new ConcurrentHashMap<>();
	private Map<String, RpcCountDown> rpcCountDown = new ConcurrentHashMap<>();

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelRegistered();
		channel = ctx.channel();
	}

	;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client active");
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof RpcResponse) {
			RpcResponse response = (RpcResponse) msg;
			String requestId = response.getRequestId();
			Object result = response.getResult();
			if (result == null) {
				result = response.getMsg();
			}
			RpcCountDown res = rpcCountDown.get(requestId);
			// 如果获取到了证明调用端还在等待
			if (res != null) {
				res.setResponse(response);
			} else {
				System.out.println("客户端没接受到..." + requestId);
			}

		}
	}


	public RpcCountDown send(Object o) {
		RpcCountDown rpcRes = new RpcCountDown();
		if (o instanceof RpcRequest) {
			RpcRequest request = (RpcRequest) o;
			rpcRes.setRequest(request);
			String requestId = request.getRequestId();
			rpcCountDown.put(requestId, rpcRes);
			channel.writeAndFlush(request);
		}
		return rpcRes;
	}

//	//	Executor executor = Executors.newFixedThreadPool(10);
//	public Object getRpcRes(RpcRequest request) {
////		System.out.println("getRpcRes - thread:"+Thread.currentThread().getName());
//		String requestId = request.getRequestId();
//		CountDownLatch countDownLatch = rpcCountDown.get(requestId);
//		try {
//			countDownLatch.await(5, TimeUnit.SECONDS);
//			rpcCountDown.remove(requestId);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Object res = rpcRes.get(requestId);
//		rpcRes.remove(requestId);
//
//		return res;
//	}

}
