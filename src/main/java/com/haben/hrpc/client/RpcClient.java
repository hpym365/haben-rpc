package com.haben.hrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 03:04
 * @Version: 1.0
 **/
public class RpcClient {
	private static final Logger log = LoggerFactory.getLogger(RpcClient.class);

	private static Bootstrap bootstrap = new Bootstrap();
	private int retry = 0;
	public RpcClient(String ip, int port) {
		NioEventLoopGroup clientWorker = new NioEventLoopGroup(2, new DefaultThreadFactory("clientWorker"));

		bootstrap = new Bootstrap();
		bootstrap.group(clientWorker);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ClientHandler());
		bootstrap.remoteAddress(ip, port);
	}

	public Channel connect(){
		Channel channel = null;
		try {
			ChannelFuture channelFuture = bootstrap.connect().sync();
			retry=0;
			channel = channelFuture.channel();
			System.out.println("channel:"+channel);
		} catch (Exception e) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			retry++;
			log.error("Connect Rpc Server failed..{} .retry...{}",e,retry);
			channel = connect();
		}
		return channel;
	}
}
