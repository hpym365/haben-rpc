package com.haben.hrpc.banner;

import java.io.*;

/**
 * @Author: Haben
 * @Description:
 * @Date: 2017-12-31 02:04
 * @Version: 1.0
 **/
public class BannerUtils {

	public static void printBanner() {
		InputStream in = null;
		BufferedReader bufferedReader = null;
		try {
			in = new FileInputStream("src/main/resources/banner.txt");
			bufferedReader = new BufferedReader(new InputStreamReader(in));
			String s = null;
			while ((s = bufferedReader.readLine()) != null) {
				System.out.println(s);
			}
		} catch (Exception e) {

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}