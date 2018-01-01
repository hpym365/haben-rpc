package com.haben.hrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 14:05
 * @Version: 1.0
 **/
public class RpcServer {

	private ServerBootstrap bootstrap = new ServerBootstrap();
	private NioEventLoopGroup boss = new NioEventLoopGroup(2, new DefaultThreadFactory("boss"));
	private NioEventLoopGroup worker = new NioEventLoopGroup(2, new DefaultThreadFactory("workersssss"));

	public RpcServer(int port) {
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ServerChannelInitializer())
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);;
		ChannelFuture channelFuture = null;
		try {
			channelFuture = bootstrap.bind(port).sync();
		} catch (Exception e) {
			e.printStackTrace();
			boss.shutdownGracefully();
			worker.shutdownGracefully();
			if (channelFuture != null) {
				channelFuture.channel().closeFuture();
			}
		}
	}
}
