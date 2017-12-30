package com.haben.hrpc.server;

import com.haben.hrpc.protocol.RpcDecoder;
import com.haben.hrpc.protocol.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 14:05
 * @Version: 1.0
 **/
public class RpcServer {

	private static ServerBootstrap bootstrap = new ServerBootstrap();

	public RpcServer(int port) {
		NioEventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
		NioEventLoopGroup worker = new NioEventLoopGroup(2, new DefaultThreadFactory("worker"));
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel channel) throws Exception {
				channel.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));
				channel.pipeline().addLast("LengthFieldPrepender", new LengthFieldPrepender(4));
				channel.pipeline().addLast("RpcDecoder", new RpcDecoder());
				channel.pipeline().addLast("RpcEncoder", new RpcEncoder());
				channel.pipeline().addLast("handler", new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						System.out.println(msg);
						ctx.writeAndFlush("服务器收到了");
					}

					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						System.out.println("active");
					}
				});
			}
		});
		bootstrap.bind(port);
//		try {
//
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	public void start() {
		try {
			bootstrap.register().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
