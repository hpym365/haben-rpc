package com.haben.hrpc;

import com.haben.hrpc.client.RpcClient;
import com.haben.hrpc.demo.HelloService;
import com.haben.hrpc.proxy.ServiceProxy;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 11:45
 * @Version: 1.0
 **/
public class ClientTest {
	static Logger log = LoggerFactory.getLogger(ClientTest.class);

	static class Tt extends Thread {
		@Override
		public void run() {
			RpcClient rpcClient = new RpcClient("127.0.0.1", 1234);

			HelloService helloService = ServiceProxy.create(HelloService.class);
//		System.out.println("hello:"+helloService);

			for (int i = 0; i < 10; i++) {
				String a = helloService.say("p1");
				String b = helloService.say("p2");
				String c = helloService.say("p3");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		LogManager.resetConfiguration();
		PropertyConfigurator.configure("/Users/hpym365/IdeaProjects/haben-rpc/src/main/resources/log4j.properties");
		RpcClient rpcClient = new RpcClient("127.0.0.1", 1234);

		Thread.sleep(1000);
		Executor executor = Executors.newFixedThreadPool(10);
		long begin = System.currentTimeMillis();

		for (int i = 0; i < 100; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					HelloService helloService = ServiceProxy.create(HelloService.class);
					String c = helloService.say("p3");
					System.out.println("helloService.say:"+c);
				}
			});
		}
		System.out.println(System.currentTimeMillis() - begin);


		log.debug("测试一下");
//		Thread t1 = new Tt();
//		Thread t2 = new Tt();
//		Thread t3 = new Tt();
//		t1.start();
//		t2.start();
//		t3.start();
//		t1.join();
//		t2.join();
//		t3.join();
//		helloService.say("wakaka");
//		rpcClient.sendRequest();


//		Channel channel = null;
//		try {
//			channel = rpcClient.connect();
//			while (true) {
//				while (channel == null || channel.isActive() == false) {
//					try {
//						channel = rpcClient.connect();
//					} catch (Exception e) {
//						System.out.println("重新连接失败" + e);
//					}
//					sleep();
//					System.out.println("继续重新连接");
//				}
//				String str = "wakaka:" + new Date();
//				channel.writeAndFlush(str);
//				HelloService helloService = ServiceProxy.create(HelloService.class);
//				System.out.println("HelloService:"+helloService);
//
//				sleep();
//			}
//		} catch (InterruptedException e) {
//			Thread.currentThread().interrupt();
//		}

	}


	public static void sleep() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
//		System.out.println("发送了123123");
//		bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
//			@Override
//			protected void initChannel(NioSocketChannel channel) throws Exception {
//				channel.pipeline().addLast("LengthFieldBasedFrameDecoder",new LengthFieldBasedFrameDecoder(MAX_MSG_LENGTH,0,4,0,4));
//				channel.pipeline().addLast("LengthFieldPrepender",new LengthFieldPrepender(4));
//				channel.pipeline().addLast("decoder",new StringDecoder());
//				channel.pipeline().addLast("encode",new StringEncoder());
//				channel.pipeline().addLast("handler", new ChannelInboundHandlerAdapter() {
//					@Override
//					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//						System.out.println(msg);
//					}
//				});
//			}
//		});
//
//		bootstrap.bind(1234);
//		bootstrap.register().sync();

}
