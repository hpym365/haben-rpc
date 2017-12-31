package com.haben.hrpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 21:36
 * @Version: 1.0
 **/
public class ClientConnection {

	private static final Logger log = LoggerFactory.getLogger(RpcClient.class);

	public static Map<InetSocketAddress, ClientHandler> handlerMap = new ConcurrentHashMap<>();
	//	private Bootstrap bootstrap = new Bootstrap();
	private int retry = 0;

	private static volatile ClientConnection clientConnection = null;

	private InetSocketAddress inetSocketAddress = null;

	private final int MAX_RETRY = 100;

	private ClientConnection() {
	}

	public static ClientConnection getInstance() {
		if (clientConnection == null) {
			synchronized (ClientConnection.class) {
				if (clientConnection == null) {
					clientConnection = new ClientConnection();
				}
			}
		}
		return clientConnection;
	}

	public void setInetSocketAddress(String ip, int port) {
		this.inetSocketAddress = new InetSocketAddress(ip, port);
	}

	public ClientHandler getClientHandler() {
		if (handlerMap.size() == 0) {
			synchronized (this) {
				if (handlerMap.size() == 0) {
					try {
						this.connect();
						System.out.println("=====进行连接了进行连接了进行连接了=====");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		ClientHandler ch = null;
		// 要修改的 lb
		System.out.println("handlerMap:"+handlerMap.size());
		for (ClientHandler clientHandler : handlerMap.values()) {
			ch = clientHandler;
		}

		return ch;
	}

	public Channel connect() throws InterruptedException {
		Channel channel = null;
		NioEventLoopGroup clientWorker = new NioEventLoopGroup(2, new DefaultThreadFactory("clientWorkerccc"));
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(clientWorker);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ClientChannelInitializer());
			bootstrap.remoteAddress(inetSocketAddress);

			ChannelFuture channelFuture = bootstrap.connect().sync();
			if(handlerMap.size()==0){
				ClientHandler clientHandler = channelFuture.channel().pipeline().get(ClientHandler.class);
				InetSocketAddress remoteAddress = (InetSocketAddress) channelFuture.channel().remoteAddress();
				handlerMap.put(remoteAddress, clientHandler);
			}
//			channelFuture.addListener(new ChannelFutureListener() {
//				@Override
//				public void operationComplete(ChannelFuture future) throws Exception {
//					ClientHandler clientHandler = future.channel().pipeline().get(ClientHandler.class);
//					InetSocketAddress remoteAddress = (InetSocketAddress) future.channel().remoteAddress();
//					handlerMap.put(remoteAddress, clientHandler);
//					System.out.println("连接了:" + remoteAddress);
//				}
//			});

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
