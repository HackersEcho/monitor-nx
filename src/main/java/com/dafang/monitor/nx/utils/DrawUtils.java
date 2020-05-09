package com.dafang.monitor.nx.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DrawUtils {
	private static final Log log = LogFactory.getLog(DrawUtils.class);

	public static String drawImg(List<Map<String, Object>> list,String type,String fileName) {
		String lonStr = "";
		String latStr = "";
		String valStr = "";
		for (Map<String, Object> map : list) {
			lonStr += map.get("longitude") + ",";
			latStr += map.get("latitude") + ",";
			valStr += map.get(type) + ",";
		}
		StringBuffer stringBuffer1 = new StringBuffer();
		stringBuffer1.append("lonStr=" + lonStr.substring(0, lonStr.length() - 1) + "&");
		stringBuffer1.append("latStr=" + latStr.substring(0, latStr.length() - 1) + "&");
		stringBuffer1.append("valStr=" + valStr.substring(0, valStr.length() - 1) + "&");
		// stringBuffer1.append("drawType=contourfmplus&");
		stringBuffer1.append("drawType=png&");
		stringBuffer1.append("graticules=true&");
		stringBuffer1.append("siteValue=false&");
		stringBuffer1.append("outFunction=nxquanqu_sharp&");
		stringBuffer1.append("siteName=true&");
		stringBuffer1.append("isShowPoint=true&");
		stringBuffer1.append("mapType=dzm&");
		
		setParmsData(stringBuffer1, type);
		String res = "";
		try {
			res = imageChangeBase64(draw(stringBuffer1, fileName ));
			log.info("图片转换 成功");
		} catch (Exception e) {
			log.info("图片转换 出错");
		}
		return res;
	}
	/***
	 * 绘图访问接口
	 * 
	 * @param buffer
	 */
	public static String draw(StringBuffer buffer, String fileName) {

		String lineStr = "";
		try {
//			String str = "http://10.178.12.167:7779/GisDrawServiceNew/gis/drawMapPng";
			String str = "http://172.23.106.60:7779/GisDrawServiceNew/gis/drawMapPng";

			URL url = new URL(str);
			// 得到connection对象。
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // 需要输出
			connection.setDoInput(true); // 需要输入
			connection.setUseCaches(false); // 不允许缓存

			// 设置请求方式
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Charsert", "UTF-8");
			// 连接
			connection.connect();
			// 建立输入流，向指向的URL传入参数
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

			dos.writeBytes(buffer.toString() + "&titleName=" + URLEncoder.encode(fileName, "UTF-8"));
			dos.flush();
			dos.close();
			// 得到响应码
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 得到响应流
				InputStream inputStream = connection.getInputStream();
				// 获取响应
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				lineStr = reader.readLine();
				lineStr = new String(lineStr.getBytes(), "UTF-8");
				reader.close();
				// 该干的都干完了,记得把连接断了
				connection.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineStr;
	}

	/**
	 * 组织绘图接口格式数据
	 *
	 * @param stringBufferst
	 * @param type
	 * @return
	 */
	private static void setParmsData(StringBuffer stringBufferst, String type) {
		String leves = "";
		String colors = "";

		switch (type) {
			case "TEM_AvgliveVal":
				leves = "[3,6,9,12,15]";// 级别
				colors = "[[255,255,205],[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
				break;
			case "TEM_AvgannomlyVal":
				leves = "[-0.4,-0.3,-0.2,-0.1,-0.0,0.1,0.3,0.5,0.8,1.1,1.3]";// 级别
				colors = "[[58,58,255],[116,114,255],[0,247,247],[155,255,255],[0,255,0],[163,255,163],[255,255,205],"
						+ "[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
				break;
			case "PRE_Time_2020liveVal":
				leves = "[284.1,431.1,578.2,725.2,872.3,1019.3]";// 级别
				colors = "[[163,255,163],[0,255,0],[155,255,255],[0,247,247],[116,114,255],[58,58,255]]";// 颜色
				break;
			case "PRE_Time_2020annomlyVal":
				leves = "[-32.0,-25.6,-19.2,-12.8,-6.4,6.4,66.9,133.8,200.7,267.6,334.5]";// 级别
				colors = "[[58,58,255],[116,114,255],[0,247,247],[155,255,255],[0,255,0],[163,255,163],[255,255,205],"
						+ "[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
				break;
			case "sshYearAnomaly1":
				leves = "[-523.7,-437.9,-352.1,-266.3,-180.6,-94.8]";// 级别
				colors = "[[255,255,205],[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
				break;
			case "ciDayAnomaly":
				leves = "[-25,-15,-5,10]";// 级别
				colors = "[[255,255, 255],[166,251,214],[88,238,243],[87,211,252],[37,166,239]]";// 颜色
				break;
		}
		stringBufferst.append("leves=" + leves).append("&colors=" + colors);
	}

//	/**
//	 * 组织绘图接口格式数据
//	 *
//	 * @param stringBufferst
//	 * @param type
//	 * @return
//	 */
//	private static void setParmsData(StringBuffer stringBufferst, String type) {
//		String leves = "";
//		String colors = "";
//
//		switch (type) {
//		case "temp":
//			leves = "[-20,-10,-5,0,5,7.5,10,12.5,15,17.5,20,22.5,25,27.5,30,32.5,35,37.5,40]";// 级别
//			colors = "[[255,255, 255],[61,151,179],[80,170,173],[98,190,166],[156,215,164],[223,242,153],"
//					+ "[239,244,165],[255,246,176],[252,232,118],[248,218,60],[249,198,49],[249,178,37],[251,162,33],"
//					+ "[252,145,29],[253,129,25],[254,112,20],[254,99,11],[253,85,1],[245,44,2],[236,2,2]]";// 颜色
//			break;
//		case "tempAnomaly":
//			leves = "[-6,-4,-2,-1,0,1,2,4,6]";// 级别
//			colors = "[[255,255, 255],[0,0,255],[2,92,226],[75,171,245],[134,232,250],[253,227,125],[250,166,68],[251,103,14]"
//					+ ",[255,0,0],[147,0,0]]";// 颜色
//			break;
//		case "TEM_Avg-liveVal":
//			leves = "[3,6,9,12,15]";// 级别
//			colors = "[[255,255,205],[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
//			break;
//		case "TEM_Avg-annomlyVal":
//			leves = "[-0.4,-0.3,-0.2,-0.1,-0.0,0.1,0.3,0.5,0.8,1.1,1.3]";// 级别
//			colors = "[[58,58,255],[116,114,255],[0,247,247],[155,255,255],[0,255,0],[163,255,163],[255,255,205],"
//					+ "[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
//			break;
//		case "pre":
//			leves = "[0,10,15,20,50,100,150,200]";// 级别
//			colors = "[[255,255, 255],[166,251,214],[88,238,243],[87,211,252],[37,166,239],[14,236,118],"
//					+ "[54,80,240],[65,36,226],[59,25,187],[59,25,153]]";// 颜色
//			break;
//		case "preAnomaly":
//			leves = "[0,10,15,20,50,100,150,200]";// 级别
//			colors = "[[255,255, 255],[166,251,214],[88,238,243],[87,211,252],[37,166,239],[14,236,118],"
//					+ "[54,80,240],[65,36,226],[59,25,187],[59,25,153]]";// 颜色
//			break;
//		case "PRE_Time_2020-liveVal":
//			leves = "[284.1,431.1,578.2,725.2,872.3,1019.3]";// 级别
//			colors = "[[163,255,163],[0,255,0],[155,255,255],[0,247,247],[116,114,255],[58,58,255]]";// 颜色
//			break;
//		case "PRE_Time_2020-annomlyVal":
//			leves = "[-32.0,-25.6,-19.2,-12.8,-6.4,6.4,66.9,133.8,200.7,267.6,334.5]";// 级别
//			colors = "[[58,58,255],[116,114,255],[0,247,247],[155,255,255],[0,255,0],[163,255,163],[255,255,205],"
//					+ "[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
//			break;
//		case "sshYear":
//			leves = "[0,200,400,600,800,1000,1200,1400,1600,1800,2000,2200,2400]";// 级别
//			colors = "[[255,255, 255],[177,243,239],[204,251,219],[249,249,213],[252,242,171],[252,227,126],"
//					+ "[252,200,101],[250,166,68],[250,146,0],[255,121,26],[241,93,5],[248,79,20],[252,38,2],"
//					+ "[230,0,0]]";// 颜色
//			break;
//		case "sshYearAnomaly":
//			leves = "[-30,0,8,16,24,32,40,48,56,64,72,80,88,96]";// 级别
//			colors = "[[255,255, 255],[166,251,214],[177,243,239],[204,251,219],[249,249,213],[252,242,171],[252,227,126],"
//					+ "[252,200,101],[250,166,68],[250,146,0],[255,121,26],[241,93,5],[248,79,20],[252,38,2]"
//					+ ",[230,0,0]]";// 颜色
//			break;
//		case "sshYear1":
//			leves = "[2168.2,2343.4,2518.5,2693.7,2868.9,3044.1]";// 级别
//			colors = "[[255,255,205],[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
//			break;
//		case "sshYearAnomaly1":
//			leves = "[-523.7,-437.9,-352.1,-266.3,-180.6,-94.8]";// 级别
//			colors = "[[255,255,205],[247,247,0],[255,207,157],[255,181,104],[255,116,116],[255,26,24]]";// 颜色
//			break;
//		case "CI":
//			leves = "[-2.4,-1.8,-1.2,-0.6]";// 级别
//			colors = "[[255,255, 255],[166,251,214],[88,238,243],[87,211,252],[37,166,239]]";// 颜色
//			break;
//		case "ciDayAnomaly":
//			leves = "[-25,-15,-5,10]";// 级别
//			colors = "[[255,255, 255],[166,251,214],[88,238,243],[87,211,252],[37,166,239]]";// 颜色
//			break;
//		}
//		stringBufferst.append("leves=" + leves).append("&colors=" + colors);
//	}

	/**
	 * 图片转BASE64
	 * 
	 * @param imagePath 路径
	 * @return
	 */
	public static String imageChangeBase64(String imagePath) {
		InputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(java.net.URLDecoder.decode(imagePath, "utf-8"));
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 加密
		Base64 encoder = new Base64();
		return new String(encoder.encode(data));
	}

}
