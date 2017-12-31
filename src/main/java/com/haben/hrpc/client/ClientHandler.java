package com.haben.hrpc.client;

import com.haben.hrpc.entity.RpcRequest;
import com.haben.hrpc.entity.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 11:55
 * @Version: 1.0
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private Channel channel;

	private Map<String,Object> rpcRes = new ConcurrentHashMap<>();
	private Map<String,CountDownLatch> rpcCountDown = new ConcurrentHashMap<>();

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelRegistered();
		channel = ctx.channel();
	};

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client active");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof RpcResponse){
			RpcResponse response = (RpcResponse) msg;
			String requestId = response.getRequestId();
			rpcRes.put(requestId,response.getResult());
			CountDownLatch countDownLatch = rpcCountDown.get(requestId);
			countDownLatch.countDown();
//			System.out.println(msg1);
		}


	}

	public void send(Object o){
		CountDownLatch countDownLatch = new CountDownLatch(1);
		if(o instanceof RpcRequest){
			RpcRequest request = (RpcRequest) o;
			String requestId = request.getRequestId();
			rpcCountDown.put(requestId,countDownLatch);
			System.out.println("发送请求 内容为:"+request.getMethodName());
			channel.writeAndFlush(o);
		}

	}

	public Object getRpcRes(RpcRequest request){
		String requestId = request.getRequestId();
		CountDownLatch countDownLatch = rpcCountDown.get(requestId);
		try {
			countDownLatch.await();
			rpcCountDown.remove(requestId);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rpcRes.get(requestId);
	}

}
