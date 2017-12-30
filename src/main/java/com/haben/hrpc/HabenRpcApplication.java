package com.haben.hrpc;

import com.haben.hrpc.server.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ThreadFactory;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017/12/30 14:29
 * @param:
 * @Return:
 **/
@SpringBootApplication
public class HabenRpcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HabenRpcApplication.class, args);
	}


	@Override
	public void run(String... args) {
		RpcServer rpcServer = new RpcServer(1234);
		rpcServer.start();
		System.out.println("rpc server is running");
	}
}
