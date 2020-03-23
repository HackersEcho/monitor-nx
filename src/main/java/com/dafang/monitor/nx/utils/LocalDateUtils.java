package com.dafang.monitor.nx.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateUtils {
	public static void main(String[] args) {
		Long differDays = getDifferDays("20180101","20180111");
		System.out.println(differDays);
	}
	//LocalDateTime与String的相互转化
	public static LocalDateTime stringToDateTime(String date){
		DateTimeFormatter df2 =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//将String转化为LocalDate第一种方式
		LocalDateTime localString1 = LocalDateTime.parse(date, df2);
		//将时间中的T去掉
		return localString1;
	}
	public static String DateTimeToString(LocalDateTime date){
		DateTimeFormatter df2 =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//将时间中的T去掉
		return date.format(df2);
	}
	/**
	 *
	 * @param date
	 * @return
	 */
	public static LocalDate stringToDate(String date){
		//日期格式化
		DateTimeFormatter df =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			LocalDate.parse(date, df);
		}catch (DateTimeParseException e){
			df =  DateTimeFormatter.ofPattern("yyyyMMdd");
		}
		return LocalDate.parse(date, df);
	}

	/**
	 * 得到两个日期相差多少天
	 * @Title: getDifferDays
	 * @param start开始时间
	 * @param end结束时间
	 * @return   参数类型
	 * @return Long
	 * @date: 2018年11月6日 下午6:32:12
	 */
	public static Long getDifferDays(LocalDate start,LocalDate end){
		return end.toEpochDay()-start.toEpochDay();
	}
	public static Long getDifferDays(String start,String end){
		//日期格式化
		DateTimeFormatter df =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			LocalDate.parse(start, df);
		}catch (DateTimeParseException e){
			df =  DateTimeFormatter.ofPattern("yyyyMMdd");
		}
		return getDifferDays(LocalDate.parse(start, df),LocalDate.parse(end, df));
	}
	/**
	 * 判断当前日期在这一年有多少天
	 * @Title: getNumberOfDays
	 * @return   参数类型
	 * @return Long
	 * @date: 2018年11月6日 下午7:02:01
	 */
	public static int getNumberOfDays(LocalDate date){
		return date.getDayOfYear();
	}
	public static int getNumberOfDays(String date){
		//日期格式化
		DateTimeFormatter df =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			LocalDate.parse(date, df);
		}catch (DateTimeParseException e){
			df =  DateTimeFormatter.ofPattern("yyyyMMdd");
		}
		return getNumberOfDays(LocalDate.parse(date,df));
	}
	/**
	 * 得到月日格式   例如12月31日
	 * @param date
	 * @return
	 */
	public static String getMDDesc(LocalDate date){
		return date.getMonthValue()+"月"+date.getDayOfMonth()+"日";
	}
	public static String getMDDesc(String dateStr){
		LocalDate date = stringToDate(dateStr);
		return date.getMonthValue()+"月"+date.getDayOfMonth()+"日";
	}
}
