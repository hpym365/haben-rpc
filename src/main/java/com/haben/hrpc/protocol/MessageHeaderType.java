package com.haben.hrpc.protocol;

/**
 * @Author: Haben
 * @Description: 消息体结构  2byte = 16位  1111111111111111=65535byte
 * length-2+headerType-1+body-65535   --最大可允许 65538 前面限制了65535
 * @Date: 2017-12-31 00:34
 * @Version: 1.0
 **/
public class MessageHeaderType {
	public static final byte HEART_BEAT = 1;
	public static final byte RPC_REQUEST_MSG = 2;
	public static final byte RPC_RESPONSE_MSG = 3;
}

// 长度2byte  = 16bit     消息最大长度 65535byte   right
// 整个消息长度 2byte+1byte(header)+65535byte =2+1+65535=65538 byte  大半夜应该没算错 2017-12-31 01:39:02