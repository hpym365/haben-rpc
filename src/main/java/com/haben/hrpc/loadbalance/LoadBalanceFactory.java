package com.haben.hrpc.loadbalance;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2018-01-02 00:20
 * @Version: 1.0
 **/
public class LoadBalanceFactory {
	public enum LB_TYPE {
		RANDOM, ROUND
	}

	private ILoadBalance loadBalance = null;

	public static ILoadBalance getLoadBalance(LB_TYPE lbType) {
		switch (lbType) {
			case ROUND:
				return new RoundLoadBalnce();
			default:
				return new RandomLoadBalnce();
		}
	}
}
