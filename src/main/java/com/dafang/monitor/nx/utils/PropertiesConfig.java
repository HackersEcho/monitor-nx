package com.dafang.monitor.nx.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig {

	/**
	 * 根据KEY，读取文件对应的值
	 * @param key 键
	 * @return key对应的值
	 */
	public static String readData(String key) {
		Properties props = new Properties();
		try {
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("application-zyj.yml");
			props.load(in);
			in.close();
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
