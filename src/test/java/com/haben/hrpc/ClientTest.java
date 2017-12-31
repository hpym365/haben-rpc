package com.haben.hrpc;

import com.haben.hrpc.client.RpcClient;
import com.haben.hrpc.demo.HelloService;
import com.haben.hrpc.proxy.ServiceProxy;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 11:45
 * @Version: 1.0
 **/
public class ClientTest {
	public static void main(String[] args) throws InterruptedException {
		RpcClient rpcClient = new RpcClient("127.0.0.1", 1234);

		rpcClient.connect();
		HelloService helloService = ServiceProxy.create(HelloService.class);
//		System.out.println("hello:"+helloService);
		String aPublic = helloService.say("public");
		System.out.println("调用后服务端返回的信息:"+aPublic);
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
