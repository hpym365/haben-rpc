package com.haben.hrpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 14:36
 * @Version: 1.0
 **/
public class RpcEncoder extends MessageToByteEncoder {
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		if(msg instanceof String ){
			String str = (String) msg;
			out.writeBytes(str.getBytes());
		}
	}
}
