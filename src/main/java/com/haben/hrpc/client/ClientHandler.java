package com.haben.hrpc.client;

import com.haben.hrpc.entity.RpcRequest;
import com.haben.hrpc.entity.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 11:55
 * @Version: 1.0
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private Channel channel;

	private Map<String, Object> rpcRes = new ConcurrentHashMap<>();
	private Map<String, CountDownLatch> rpcCountDown = new ConcurrentHashMap<>();

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

	Executor executor = Executors.newFixedThreadPool(20);
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				if (msg instanceof RpcResponse) {
					RpcResponse response = (RpcResponse) msg;
					String requestId = response.getRequestId();
					Object result = response.getResult();
					if (result == null) {
						result = response.getMsg();
					}
					CountDownLatch countDownLatch = rpcCountDown.get(requestId);
					// 如果获取到了证明调用端还在等待
					if (countDownLatch != null) {
						System.out.println("response.toString():"+response.toString()  +"  thread:"+Thread.currentThread().getName());
						rpcRes.put(requestId, result);
						countDownLatch.countDown();
					} else {
						System.out.println("客户端没接受到...");
					}
//			System.out.println("channelRead - thread:"+Thread.currentThread().getName());
//			System.out.println(msg1);
				}
			}
		});
	}

	public void send(Object o) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		if (o instanceof RpcRequest) {
			RpcRequest request = (RpcRequest) o;
			String requestId = request.getRequestId();
			rpcCountDown.put(requestId, countDownLatch);
			System.out.println("发送 send:"+Thread.currentThread().getName());
//			System.out.println("发送请求123 内容为:" + request.getMethodName());
			channel.writeAndFlush(o);

		}
	}

	//	Executor executor = Executors.newFixedThreadPool(10);
	public Object getRpcRes(RpcRequest request) {
//		System.out.println("getRpcRes - thread:"+Thread.currentThread().getName());
		String requestId = request.getRequestId();
		CountDownLatch countDownLatch = rpcCountDown.get(requestId);
		try {
			countDownLatch.await(3, TimeUnit.SECONDS);
			rpcCountDown.remove(requestId);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Object res = rpcRes.get(requestId);
		rpcRes.remove(requestId);

		return res;
	}

}
