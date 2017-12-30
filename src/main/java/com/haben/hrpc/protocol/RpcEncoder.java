package com.haben.hrpc.protocol;

import com.haben.hrpc.entity.RpcRequest;
import com.haben.hrpc.entity.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 14:36
 * @Version: 1.0
 **/
public class RpcEncoder extends MessageToByteEncoder {
	private static final Logger log = LoggerFactory.getLogger(RpcDecoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		log.debug("begin encode ...:Message Type:{}", msg.getClass());
		byte[] data = null;
		// 心跳编码
		if (msg instanceof String) {
			String str = (String) msg;
			data = SerializationUtils.serialize(str);
			out.writeByte(MessageHeaderType.HEART_BEAT);
		}

		// rpc请求编码
		if (msg instanceof RpcRequest) {
			data = SerializationUtils.serialize(msg);
			out.writeByte(MessageHeaderType.RPC_REQUEST_MSG);
		}

		// rpc回应编码
		if (msg instanceof RpcResponse) {
			data = SerializationUtils.serialize(msg);
			out.writeByte(MessageHeaderType.RPC_RESPONSE_MSG);
		}

		if (data != null) {
			out.writeBytes(data);
		}

	}
}
