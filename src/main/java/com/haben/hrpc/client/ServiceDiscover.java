package com.haben.hrpc.client;

import com.haben.hrpc.config.SysConstant;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2018-01-01 22:40
 * @Version: 1.0
 **/
public class ServiceDiscover {

	private String zkUrl;

	private Map<String, Integer> ipMap = new HashMap<>();

	//	private static ServiceDiscover serviceDiscover = null;
	private CuratorFramework client = null;

	public ServiceDiscover(String zkUrl) {
		System.out.println("ServiceDiscover init ");
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 1000);
		client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
		client.start();
		try {
			initIpMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		startWatcher();
	}

	public void initIpMap() throws Exception {
		List<String> nodes = client.getChildren().forPath(SysConstant.ZK_PATH);
		for (String node : nodes) {
			byte[] ipData = client.getData().forPath(SysConstant.ZK_PATH + "/" + node);
			//数据格式为ip:port/权重
			String ip = new String(ipData);
			//获取权重
			String[] split = ip.split("/");
			int weight = split.length == 1 ? 1 : Integer.parseInt(split[1]);
			ipMap.put(split[0], weight);
		}
		System.out.println("zk init ipMap:" + ipMap.toString());
	}

	public Map<String, Integer> getIpMap() {
		return ipMap;
	}

	public void startWatcher() {
		System.out.println("startWatcher启动了");
		PathChildrenCache childrenCache = new PathChildrenCache(client, SysConstant.ZK_PATH, true);
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
						System.out.println("zk init");
						break;
					default:
						System.out.println("为监听的事件:" + pathChildrenCacheEvent.getType());
				}
			}
		};
	}
}
