package com.haben.hrpc.util;

import java.io.*;
import java.util.Properties;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2018-01-01 22:20
 * @Version: 1.0
 **/
public class PropertiesUtils {

	private static Properties properties = null;

	private static Properties getProperties(){
		InputStream in= null;
		try {
			in = new BufferedInputStream(new FileInputStream("src/main/resources/application.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties p=new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	public static String getProperty(String key){
		if(properties ==null){
			properties = getProperties();
		}
		return properties.getProperty(key);
	}

	public static void main(String[] args) {
		Properties properties = getProperties();
		String property = properties.getProperty("rpc.server.port");
		System.out.println(property);
	}
}
