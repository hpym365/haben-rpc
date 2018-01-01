package com.haben.hrpc;

import com.haben.hrpc.config.SysConstant;
import com.haben.hrpc.server.RpcServer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: Haben
 * @Description: 一个简单的Netty+SpringBoot(IOC)+Protostuff的Rpc框架
 * 可以使用spring注解来管理容器,也可以同时提供web接口调用
 * @Date: 2017/12/30 14:29
 * @param:
 * @Return:
 **/
@SpringBootApplication
@Configuration
public class HabenRpcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HabenRpcApplication.class, args);
	}

	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory(httpClient());
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(httpRequestFactory());
	}

	@Bean
	public HttpClient httpClient() {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory())
				.build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(SysConstant.MAX_THREAD);
		connectionManager.setDefaultMaxPerRoute(SysConstant.MAX_THREAD);

		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(2000)
				.setConnectTimeout(2000)
				.setConnectionRequestTimeout(2000)
				.build();

		return HttpClientBuilder.create()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(connectionManager)
				.build();
	}

	@Value("${rpc.server.port}")
	int port;

	@Override
	public void run(String... args) {
		RpcServer rpcServer = new RpcServer(port);
		System.out.println("rpc server is running");

	}
}
