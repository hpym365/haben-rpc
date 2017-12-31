package com.haben.hrpc.server;

import com.haben.hrpc.protocol.LengthField;
import com.haben.hrpc.protocol.RpcDecoder;
import com.haben.hrpc.protocol.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 04:31
 * @Version: 1.0
 **/
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
	@Override
	protected void initChannel(NioSocketChannel ch) throws Exception {
		ch.pipeline().addLast("lengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(LengthField.MAX_FRAME_LENGTH, LengthField.LENGTH_FIELD_OFFSET, LengthField.LENGTH_FIELD_LENGTH, LengthField.LENGTH_ADJUSTMENT, LengthField.INITIAL_BYTES_TO_STRIP));
		ch.pipeline().addLast("lengthFieldPrepender", new LengthFieldPrepender(LengthField.LENGTH_FIELD_LENGTH));
		ch.pipeline().addLast("rpcDecoder", new RpcDecoder());
		ch.pipeline().addLast("rpcEncoder", new RpcEncoder());
		ch.pipeline().addLast("serverHandler", new ServerHandler());
	}
}

