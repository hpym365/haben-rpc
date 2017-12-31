package com.haben.hrpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 13:04
 * @Version: 1.0
 **/
public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ServiceDispatcher serviceDispatcher = ServiceDispatcher.getInstance();
		Object res = serviceDispatcher.dispatch(msg);
		ctx.writeAndFlush(res);

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
