package com.haben.hrpc.client;

import com.haben.hrpc.protocol.LengthField;
import com.haben.hrpc.protocol.RpcDecoder;
import com.haben.hrpc.protocol.RpcEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 03:06
 * @Version: 1.0
 **/
public class ClientHandler extends ChannelInitializer<NioSocketChannel> {

	@Override
	protected void initChannel(NioSocketChannel ch) throws Exception {
		ch.pipeline().addLast("lengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(LengthField.MAX_FRAME_LENGTH, LengthField.LENGTH_FIELD_OFFSET, LengthField.LENGTH_FIELD_LENGTH, LengthField.LENGTH_ADJUSTMENT,LengthField.INITIAL_BYTES_TO_STRIP));
		ch.pipeline().addLast("lengthFieldPrepender", new LengthFieldPrepender(LengthField.LENGTH_FIELD_LENGTH));
		ch.pipeline().addLast("rpcEncoder", new RpcEncoder());
		ch.pipeline().addLast("rpcDecoder", new RpcDecoder());
		ch.pipeline().addLast("handler", new ChannelInboundHandlerAdapter() {
			@Override
			public void channelActive(ChannelHandlerContext ctx) throws Exception {
				System.out.println("client active");
			}

			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				System.out.println(msg);
			}
		});
	}
}
