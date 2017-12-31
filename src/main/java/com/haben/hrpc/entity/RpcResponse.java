package com.haben.hrpc.entity;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-30 17:00
 * @Version: 1.0
 **/
public class RpcResponse {
	private String requestId;
	private String msg;
	private Object result;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
