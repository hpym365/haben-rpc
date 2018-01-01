package com.haben.hrpc.client;

import com.haben.hrpc.config.SysConstant;
import com.haben.hrpc.loadbalance.ILoadBalance;
import com.haben.hrpc.loadbalance.LoadBalanceFactory;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 21:36
 * @Version: 1.0
 **/
public class ClientConnection {

	private static final Logger log = LoggerFactory.getLogger(RpcClient.class);

	/**
	 * @Author: Haben
	 * @Description: client channelread0 Threadpool
	 * @Date: 2018/1/1 23:45
	 * @param:
	 * @Return:
	 **/
	public static Executor channelReadThreadPool = new ThreadPoolExecutor(20, 20, 100,
			TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000), new DefaultThreadFactory("channelRead0"));

	private ILoadBalance loadBalance = null;

	public static Map<ClientHandler, Integer> handlerMap = new ConcurrentHashMap<>();

	public static List<ClientHandler> clientHandlerList = new CopyOnWriteArrayList<>();
	//	private Bootstrap bootstrap = new Bootstrap();
	private int retry = 0;

	private static volatile ClientConnection clientConnection = null;

	private InetSocketAddress inetSocketAddress = null;

	private final int MAX_RETRY = 100;

//	private ServiceDiscover serviceDiscover = null;

	private ClientConnection() {
		ServiceDiscover serviceDiscover = new ServiceDiscover(SysConstant.ZK_URL);
		initHanlderMap(serviceDiscover);
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

	//	public void setInetSocketAddress(String ip, int port) {
//		this.inetSocketAddress = new InetSocketAddress(ip, port);
//	}
	public ClientHandler getClientHandler() {
		if (handlerMap.size() == 0) {
			log.error("handlerMap size is 0,no provider...Start server first!");
			return null;
		}
		// load balance
		if (loadBalance == null) {
			loadBalance = LoadBalanceFactory.getLoadBalance(LoadBalanceFactory.LB_TYPE.ROUND);
		}

		return loadBalance.getClientHandler(handlerMap);
	}

	private void initHanlderMap(ServiceDiscover serviceDiscover) {
		// 如果没有可用的handler  那就从zk里找来连上吧
		Map<String, Integer> ipMap = serviceDiscover.getIpMap();
		for (String ip : ipMap.keySet()) {
			String[] split = ip.split(":");
			InetSocketAddress inetSocketAddress = new InetSocketAddress(split[0], Integer.parseInt(split[1]));
			try {
				Channel channel = this.connect(inetSocketAddress);
				ClientHandler clientHandler = channel.pipeline().get(ClientHandler.class);
				Integer integer = ipMap.get(ip);
				// 根据权重放几次 后面方便lb
				handlerMap.put(clientHandler, integer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Channel connect(InetSocketAddress inetSocketAddress) throws InterruptedException {
		Channel channel = null;
		NioEventLoopGroup clientWorker = new NioEventLoopGroup(4, new DefaultThreadFactory("clientWorker"));
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(clientWorker);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ClientChannelInitializer());
//			bootstrap.option(ChannelOption.SO_BACKLOG,1024);
			bootstrap.remoteAddress(inetSocketAddress);
			ChannelFuture channelFuture = bootstrap.connect().sync();
//			if (handlerMap.size() == 0) {
//				ClientHandler clientHandler = channelFuture.channel().pipeline().get(ClientHandler.class);
//				InetSocketAddress remoteAddress = (InetSocketAddress) channelFuture.channel().remoteAddress();
//				handlerMap.put(remoteAddress, clientHandler);
//			}
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
				channel = connect(inetSocketAddress);
			} else {
				throw e;
			}
		}
		return channel;
	}
}
