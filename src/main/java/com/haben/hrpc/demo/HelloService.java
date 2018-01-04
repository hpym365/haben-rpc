package com.haben.hrpc.demo;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 04:46
 * @Version: 1.0
 **/
public interface HelloService {

	public String say(String str);
	public String say500(String str);
	public String say200(String str);
	public String say1(String str);
}
