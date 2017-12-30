package com.haben.hrpc.protocol;

import com.haben.hrpc.entity.RpcRequest;
import com.haben.hrpc.entity.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 14:27
 * @Version: 1.0
 **/
public class RpcDecoder extends ByteToMessageDecoder {

	private static final Logger log = LoggerFactory.getLogger(RpcDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		byte headerType = in.readByte();
		log.debug("begin decode ...:MessageHeaderType:{}",headerType);
		Object res = decodeByHeaderType(headerType, in);

		if (res != null) {
			out.add(res);
		}
	}

	private Object decodeByHeaderType(byte headerType, ByteBuf in) {
		int dataLength = in.readableBytes();
		byte[] data = new byte[dataLength];
		in.readBytes(data);

		switch (headerType) {
			case MessageHeaderType.HEART_BEAT: {
				return SerializationUtils.deserialize(data, String.class);
			}
			case MessageHeaderType.RPC_REQUEST_MSG: {
				return SerializationUtils.deserialize(data, RpcRequest.class);
			}
			case MessageHeaderType.RPC_RESPONSE_MSG: {
				return SerializationUtils.deserialize(data, RpcResponse.class);
			}
			default: {
				log.error("Decode error!Message must have a message header..please check your message header...See MessageHeaderType.class");
				return null;
			}
		}
	}
}
