package com.haben.hrpc.server;

import com.haben.hrpc.client.ClientConnection;
import com.haben.hrpc.config.SysConstant;
import com.haben.hrpc.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 13:04
 * @Version: 1.0
 **/
public class ServerHandler extends SimpleChannelInboundHandler {

//	Executor executor = Executors.newFixedThreadPool(200);


	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg)  {
		ClientConnection.executor.execute(new Runnable() {
			@Override
			public void run() {
				ServiceDispatcher serviceDispatcher = ServiceDispatcher.getInstance();
				Object res = null;
				try {
					res = serviceDispatcher.dispatch(msg);
					RpcResponse response = (RpcResponse)res;
//					System.out.println("res:"+response.getResult()==null?response.getMsg():response.getResult());
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
//				System.out.println("ctx.writeAndFlush :"+Thread.currentThread().getName()+"  "+res);
		try {
			ctx.writeAndFlush(res).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
