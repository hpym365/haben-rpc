package com.haben.hrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 03:04
 * @Version: 1.0
 **/
public class RpcClient {
	private static final Logger log = LoggerFactory.getLogger(RpcClient.class);
	private NioEventLoopGroup clientWorker = new NioEventLoopGroup(2, new DefaultThreadFactory("clientWorker"));
	private Bootstrap bootstrap = new Bootstrap();
	private int retry = 0;
	private final int MAX_RETRY = 100;

	public static Map<String,ClientHandler> handlerMap = new ConcurrentHashMap<>();

	public RpcClient(String ip, int port) {

		bootstrap = new Bootstrap();
		bootstrap.group(clientWorker);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ClientChannelInitializer());
		bootstrap.remoteAddress(ip, port);
	}

	public Channel connect() throws InterruptedException {
		Channel channel = null;
		try {
			ChannelFuture channelFuture = bootstrap.connect().sync();
			channelFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					ClientHandler clientHandler = future.channel().pipeline().get(ClientHandler.class);
					handlerMap.put("clientHandler",clientHandler);
					System.out.println("连接了");
				}
			});

			retry = 0;
			channel = channelFuture.channel();
			System.out.println("channel:" + channel);
		} catch (Exception e) {
			if (e instanceof ConnectException) {
				Thread.sleep(5000);
				retry++;
				if (retry > MAX_RETRY) {
					Thread.currentThread().interrupt();
					clientWorker.shutdownGracefully();
					return null;
				}
				log.error("Connect Rpc Server failed..{} .retry...{}", e, retry);
				channel = connect();
			} else {
				throw e;
			}
		}
		return channel;
	}





}
