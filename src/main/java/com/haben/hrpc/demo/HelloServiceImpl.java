package com.haben.hrpc.demo;

import com.haben.hrpc.annotation.RpcService;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 04:46
 * @Version: 1.0
 **/
@RpcService(service = HelloService.class)
public class HelloServiceImpl implements HelloService {
	@Override
	public String say(String str) {
//		System.out.println("client say:" + str);
		String targetUrl = "http://www.cnblogs.com/";

		// 1.建立HttpClient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 2.建立Get请求
		HttpGet get = new HttpGet(targetUrl);
		// 3.发送Get请求
		System.out.println("执行了 impl");
		try {
			CloseableHttpResponse res = client.execute(get);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				return "okokok";
			}else {
				return "nonono";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		return "buzhidao";
	}
}
