package com.haben.hrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 14:05
 * @Version: 1.0
 **/
public class RpcServer {

	private ServerBootstrap bootstrap = new ServerBootstrap();
	private NioEventLoopGroup boss = new NioEventLoopGroup(2, new DefaultThreadFactory("boss"));
	private NioEventLoopGroup worker = new NioEventLoopGroup(2, new DefaultThreadFactory("worker"));

	public RpcServer(int port) {
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ServerChannelInitializer())
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
		;
		ChannelFuture channelFuture = null;
		try {
			channelFuture = bootstrap.bind(port).sync();
		} catch (Exception e) {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
			if (channelFuture != null) {
				channelFuture.channel().closeFuture();
			}
			e.printStackTrace();
		}
		providerRegister(port);
	}

	public void providerRegister(int port) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 1000);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
		client.start();
		try {
			InetAddress address = InetAddress.getLocalHost();
			String ip = address.getHostAddress();
			client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/hrpc/data", (ip + ":" + port).getBytes());
			System.out.println("register ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
