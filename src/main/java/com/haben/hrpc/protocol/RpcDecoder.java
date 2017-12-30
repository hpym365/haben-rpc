package com.haben.hrpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 14:27
 * @Version: 1.0
 **/
public class RpcDecoder extends ByteToMessageDecoder {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int dataLength = in.readableBytes();

		byte[] data = new byte[dataLength];

		in.readBytes(data);

		String res = new String(data,"UTF-8");
		out.add(res);
	}
}
