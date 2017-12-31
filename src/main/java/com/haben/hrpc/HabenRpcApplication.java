package com.haben.hrpc;

import com.haben.hrpc.server.RpcServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Haben
 * @Description: 一个简单的Netty+SpringBoot(IOC)+Protostuff的Rpc框架
 * 可以使用spring注解来管理容器,也可以同时提供web接口调用
 * @Date: 2017/12/30 14:29
 * @param:
 * @Return:
 **/
@SpringBootApplication
public class HabenRpcApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HabenRpcApplication.class, args);
	}

	@Value("${rpc.server.port}")
	int port;

	@Override
	public void run(String... args) {
		RpcServer rpcServer = new RpcServer(port);
		System.out.println("rpc server is running");

	}
}
