package com.haben.hrpc.protocol;

import com.haben.hrpc.entity.RpcRequest;
import com.haben.hrpc.entity.RpcRequestJava;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 14:57
 * @Version: 1.0
 **/
public class SerializationUtils {

	private static Map<Class<?>,Schema<?>> classSchemaMap = new ConcurrentHashMap<>();

	public static <T> Schema<T> getSchema(Class<T> tClass) {
		Schema<T> schema = (Schema<T>) classSchemaMap.get(tClass);
		if (schema==null){
			schema = RuntimeSchema.getSchema(tClass);
			classSchemaMap.put(tClass,schema);
		}
		return schema;
	}

	public static <T> byte[] serialize(T object) {
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		Class<T> aClass = (Class<T>) object.getClass();
		Schema<T> schema = getSchema(aClass);
		byte[] data = ProtostuffIOUtil.toByteArray(object, schema, buffer);
		return data;
	}

	public static <T> T deserialize(byte[] bytes, Class<T> tClass) {
		Schema<T> schema = getSchema(tClass);
		T message = null;
		try {
			message = tClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProtostuffIOUtil.mergeFrom(bytes, message, schema);
		return message;
	}

	public static void main(String[] args) throws Exception {

		String msg = "123123123";
		System.out.println(msg.getBytes().length);

		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		ObjectOutputStream oo =
				new ObjectOutputStream(byteBuffer);
		oo.write(msg.getBytes());
		oo.flush();
		byte[] bytes = byteBuffer.toByteArray();
		System.out.println("java seril 大小:"+bytes.length);

		byte[] serialize = serialize(msg);
		System.out.println("protostuff:"+serialize.length);
		String deserialize1 = deserialize(serialize, String.class);

		System.out.println(deserialize1);



		RpcRequestJava rpcRequest = new RpcRequestJava();
		rpcRequest.setClassName("123123123");
		ObjectOutputStream ooa = new ObjectOutputStream (new FileOutputStream("kakaFile"));
		ooa.writeObject(rpcRequest);
		ooa.close();

		RpcRequest request = new RpcRequest();
		request.setClassName("123123123");
		byte[] serialize1 = serialize(request);
		System.out.println("request dui xiang:"+serialize1.length);
		ObjectOutputStream oob = new ObjectOutputStream (new FileOutputStream("kakaFile1"));
		oob.write(serialize1);
		oob.close();

	}
}
