package com.haben.hrpc.demo;

import com.haben.hrpc.annotation.RpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 04:46
 * @Version: 1.0
 **/
@RpcService(service = HelloService.class)
public class HelloServiceImpl implements HelloService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public String say(String str)  {
//		System.out.println("client say:" + str);
		String targetUrl = "http://www.cnblogs.com/";

//		// 1.建立HttpClient对象
//		CloseableHttpClient client = HttpClients.createDefault();
//		// 2.建立Get请求
//		HttpGet get = new HttpGet(targetUrl);
//		// 3.发送Get请求
//		System.out.println("执行了 impl:"+Thread.currentThread().getName());
//		URI uri = new URI(targetUrl);
//
//		try {
//			CloseableHttpResponse res = client.e
//			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//				return "okokok";
//			}else {
//				return "nonono";
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		long begin = System.currentTimeMillis();
		try {
			String res = restTemplate.getForObject(new URI(targetUrl), String.class);
			System.out.println("restTemplate.getForObject time :"+(System.currentTimeMillis()-begin));

			return "okokok";
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return "nonono";
		}


//		return "buzhidao";
	}
}
