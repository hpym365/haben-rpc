package com.haben.hrpc;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2018-01-01 20:12
 * @Version: 1.0
 **/
public class TestZk {
	static List<String> serverList = new ArrayList();

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 1000);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
		client.start();



		PathChildrenCache childrenCache = new PathChildrenCache(client, "/hrpc", true);
		PathChildrenCacheListener listener = new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
				ChildData data = pathChildrenCacheEvent.getData();
				switch (pathChildrenCacheEvent.getType()) {
					case CHILD_ADDED:
						System.out.println("CHILD_ADDED  path:" + data.getPath() + "  data:" + data.getData());
						byte[] data1 = data.getData();
						String s = new String(data1);
						System.out.println(s);
						// serverList.add()
						break;
					case CHILD_REMOVED:
						System.out.println("CHILD_REMOVED  path:" + data.getPath() + "  data:" + new String(data.getData()));
						break;
					case CHILD_UPDATED:
						System.out.println("CHILD_UPDATED  path:" + data.getPath() + "  data:" + data.getData());
						break;
					case INITIALIZED:
//						List<String> nodes = client.getChildren().forPath("/hrpc");
//						serverList=nodes;
					default:
						System.out.println("为监听的事件:" + pathChildrenCacheEvent.getType());
				}
			}
		};
		childrenCache.getListenable().addListener(listener);
		System.out.println("Register zk watcher successfully!");
		childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);


		while (true) {
			Thread.sleep(5000);
			List<String> strings = client.getChildren().forPath("/hrpc");
			for (String string : strings) {
				System.out.println("节点:" + string);
			}
		}

	}
}
