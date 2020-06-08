package com.dafang.monitor.nx.utils;

/**
 * response.setContentType()的作用是使客户端浏览器，区分不同种类的数据，
 * 并根据不同的MIME调用浏览器内不同的程序嵌入模块来处理相应的数据。
 * 例如web浏览器就是通过MIME类型来判断文件是GIF图片。通过MIME类型来处理json字符串。 
 * @author WL
 *
 */
public class Constants {
	
	public static final String BMP = "image/bmp";
	public static final String GIF = "image/gif";
	public static final String PNG = "image/png";
	public static final String JPEG = "image/jpeg";
	public static final String TIFF = "image/tiff";
	public static final String HTML = "text/html";
	public static final String TXT = "text/plain";
	public static final String XML = "text/xml";
	public static final String PDF = "application/pdf";
	public static final String MSWORD = "pplication/msword";
	public static final String MSEXCEL = "application/vnd.ms-excel";
	public static final String MSPOWERPOINT = "application/vnd.ms-powerpoint";
	public static final String STREAM= "application/octet-stream";
	public static final String SWF = "application/x-shockwave-flash";
	public static final String CSS = "text/css";
}
