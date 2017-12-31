package com.haben.hrpc.server;

import com.haben.hrpc.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 13:04
 * @Version: 1.0
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {

	Executor executor = Executors.newFixedThreadPool(16);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)  {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				ServiceDispatcher serviceDispatcher = ServiceDispatcher.getInstance();
				Object res = null;
				try {
					res = serviceDispatcher.dispatch(msg);
					RpcResponse response = (RpcResponse)res;
					System.out.println("res:"+response.getResult());
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				System.out.println("ctx.writeAndFlush :"+Thread.currentThread().getName());
				ctx.writeAndFlush(res);
			}
		});


//		System.out.println(msg);
////		ctx.writeAndFlush("服务器收到了");
//		RpcResponse rpcResponse = new RpcResponse();
//		rpcResponse.setMsg(new HelloServiceImpl().say("wakaka"));
//		ctx.writeAndFlush(rpcResponse);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("active");
	}
}
