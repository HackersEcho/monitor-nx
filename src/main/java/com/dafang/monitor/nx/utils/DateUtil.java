package com.dafang.monitor.nx.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtil {
	public static void main(String[] args) {
	}
	
	/**
	 * 时间的加减运算
	 * @param dateStr 日期字符串
	 * @return
	 */
	public static Date dateArithmetic(String dateStr,String format,int sec)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToDate(dateStr,format));
		calendar.add(Calendar.SECOND, sec);
		return calendar.getTime();
	}
	
	/**
	 * 时间的加减运算
	 * @param date 日期
	 * @return
	 */
	public static Date dateArithmetic(Date date,int sec)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, sec);
		return calendar.getTime();
	}
	
	
	/**
	 * 日期类型转固指定格式的日期字符串
	 */
	public static String dateToStr(Date date,String format)
	{
		String str = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			str = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("DateUtil 中日期类型转字符串格式的日期时出现错误");
		}
		return str;
	}
	
	/**
	* 字符串转换成日期
	*/
	public static Date strToDate(String str,String format) {
	   Date date = null;
	   try {
		SimpleDateFormat  sdf = new SimpleDateFormat(format);
	    date = sdf.parse(str);
	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	   return date;
	}

	public static long dateSub(String beginDateStr, String endDateStr, String format, String timeSpan) {
	    long val = 0L;
	    Date beginDate = strToDate(beginDateStr, format);
	    Date endDate = strToDate(endDateStr, format);
	    if (timeSpan.equals("hour")) {
	      val = (endDate.getTime() - beginDate.getTime()) / 3600000L;
	    } else {
	      val = (endDate.getTime() - beginDate.getTime()) / 86400000L;
	    }
	    System.out.println(val);
	    return val+1;
	}
	 
	 public static boolean sameDate(Date d1, Date d2) {  
		    if(null == d1 || null == d2)  
		        return false;  
		    //return getOnlyDate(d1).equals(getOnlyDate(d2));  
		    Calendar cal1 = Calendar.getInstance();  
		    cal1.setTime(d1);  
		    Calendar cal2 = Calendar.getInstance();  
		    cal2.setTime(d2);  
		    return  cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);  
	 }  
	 public static String getLastDayOfMonth(int year, int month) {     
         Calendar cal = Calendar.getInstance();     
         cal.set(Calendar.YEAR, year);     
         cal.set(Calendar.MONTH, month-1);     
         cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
        return  new   SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());  
     }   
     public static String getFirstDayOfMonth(int year, int month) {     
         Calendar cal = Calendar.getInstance();     
         cal.set(Calendar.YEAR, year);     
         cal.set(Calendar.MONTH, month-1);  
         cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
        return   new   SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());  
     }
		/**
		 * 获取某日期所属时间段 同期非oracle 不考虑闰年及实际月终结日
		 *
		 * @param date
		 *            日期参数
		 * @return DATA0 日 1旬 2月 3季 4年 5-30 30天以来 文字描述，起始日期 ，终止日期 MMdd
		 */
		public static Map<String, String[]> getIntervalByDate(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int day = c.get(Calendar.DAY_OF_MONTH);
			int month = (c.get(Calendar.MONTH) + 1);
			int year = c.get(Calendar.YEAR);
			Map<String, String[]> result = new HashMap<String, String[]>();
			String mdStr = DateUtil.dateToStr(date, "MMdd");
			result.put("DATE0", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr ,year+""});
			String mStr = mdStr.substring(0, 2);
			if (day >= 1 && day <= 10) {
				result.put("DATE1", new String[] { (month) + "月上旬", mStr + "01", mStr + "10" ,year+""});
			}
			if (day >= 10 && day <= 20) {
				result.put("DATE1", new String[] { (month) + "月中旬", mStr + "11", mStr + "20" ,year+""});
			}
			if (day >= 20 && day <= 31) {
				result.put("DATE1", new String[] { (month) + "月下旬", mStr + "20", mStr + "31" ,year+""});
			}

			result.put("DATE2", new String[] { month + "月", mStr + "01", mStr + "31" ,year+""});
			if (month >= 3 && month <= 5)
				result.put("DATE3", new String[] { "春季", "0301", "0530" ,year+""});
			if (month >= 6 && month <= 8)
				result.put("DATE3", new String[] { "夏季", "0601", "0831",year+""});
			if (month >= 9 && month <= 11)
				result.put("DATE3", new String[] { "秋季", "0901", "1130" ,year+""});
			if (month >= 12 || month <= 2)
				result.put("DATE3", new String[] { "冬季", "1201", "0229" ,year+""});
			result.put("DATE4", new String[] { "年", "0101", "1231",year+""});
			int [] days = {5,7,10,15,20,30,45,60,90,120,150,300,360,365,366};
			for(int i=0;i<days.length;i++){
				Calendar cc = Calendar.getInstance();
				cc.setTime(date);
				String mdStr2 = DateUtil.dateToStr(cc.getTime(), "MMdd");
				cc.add(Calendar.DAY_OF_MONTH, 1-days[i]);
				String mdStr1 = DateUtil.dateToStr(cc.getTime(), "MMdd");
				result.put("DATE5-"+days[i], new String[]{days[i]+"天以来",mdStr1,mdStr2,year+""});
			}
			return result;
		}

		/**
		 * 获取某日期所同步时间段 同期非oracle 不考虑闰年及实际月终结日
		 * @param date
		 *            日期参数
		 * @return DATA0当日 1旬 2月 3季 4年 文字描述，起始日期 ，终止日期 MMdd 监测值所属年份
		 */
		public static Map<String, String[]> getLastIntervalByDate(Date date) {
			Map<String, String[]> result = new HashMap<String, String[]>();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			
			int day = c.get(Calendar.DAY_OF_MONTH);
			int month = (c.get(Calendar.MONTH) + 1);
			int year = c.get(Calendar.YEAR);
			int lyear = year - 1;
			boolean isONE = month==1;
			boolean isONEONE = isONE&&day==1;
			
			c.add(Calendar.DAY_OF_MONTH, -1);
			String lmdStr = DateUtil.dateToStr(c.getTime(), "MMdd");
			int lday = c.get(Calendar.DAY_OF_MONTH);
			int lmonth = (c.get(Calendar.MONTH) + 1);
			//前一日
			result.put("DATE-1", new String[] { (lmonth) + "月" + (lday) + "日", lmdStr, lmdStr,isONEONE?lyear+"":year+"" });
			c.add(Calendar.DAY_OF_MONTH, 1);
			
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MONTH, -1);
			lmonth = c.get(Calendar.MONTH) + 1;
		
			String mdStr = DateUtil.dateToStr(date, "MMdd");
			String lmStr = DateUtil.dateToStr(c.getTime(), "MM");
			
			result.put("DATE0", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr,year+"" });
			String mStr = mdStr.substring(0, 2);
			if (day >= 1 && day <= 10) {
				result.put("DATE1", new String[] { (lmonth) + "月下旬", lmStr + "20", lmStr + "31",isONE?lyear+"":year+"" });
			}
			if (day >= 10 && day <= 20) {
				result.put("DATE1", new String[] { (month) + "月上旬", mStr + "01", mStr + "10",year+"" });
			}
			if (day >= 20 && day <= 31) {
				result.put("DATE1", new String[] { (month) + "月中旬", mStr + "11", mStr + "20",year+"" });
			}

			result.put("DATE2", new String[] { lmonth + "月", lmStr + "01", lmStr + "31" ,isONE?lyear+"":year+""});
			if (month >= 3 && month <= 5)
				result.put("DATE3", new String[] { "冬季", "1201", "0229" ,year+""});
			if (month >= 6 && month <= 8)
				result.put("DATE3", new String[] { "春季", "0301", "0530" ,year+""});
			if (month >= 9 && month <= 11)
				result.put("DATE3", new String[] { "夏季", "0601", "0831" ,year+""});
			if (month >= 12 || month <= 2)
				result.put("DATE3", new String[] { "秋季", "0901", "1130" ,isONE?lyear+"":year+""});
			result.put("DATE4", new String[] { "年", "0101", "1231",isONE?lyear+"":year+"" });
			return result;
		}
		/**
		 * 获取某日期所同步时间段 同期非oracle 不考虑闰年及实际月终结日
		 * 20190920-首页地图超阈值事件只展示当日，需要注释部分代码，所以保留原方法以防其他地方调用，复制这份副本做修改用。
		 * @param date
		 *            日期参数
		 * @return DATA0当日 1旬 2月 3季 4年 文字描述，起始日期 ，终止日期 MMdd 监测值所属年份
		 * 20200305-DATA5 当前旬 DATA6当前月 DATA7 当前季 DATA8 当前年（为了日数据与当前所在旬月季数据比较）
		 */
		public static Map<String, String[]> getLastIntervalByDate1(Date date) {
			Map<String, String[]> result = new HashMap<String, String[]>();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			
			int day = c.get(Calendar.DAY_OF_MONTH);
			int month = (c.get(Calendar.MONTH) + 1);
			int year = c.get(Calendar.YEAR);
			int lyear = year - 1;
			boolean isONE = month==1;
			boolean isONEONE = isONE&&day==1;
			
			c.add(Calendar.DAY_OF_MONTH, -1);
			String lmdStr = DateUtil.dateToStr(c.getTime(), "MMdd");
			int lday = c.get(Calendar.DAY_OF_MONTH);
			int lmonth = (c.get(Calendar.MONTH) + 1);
			//前一日
			result.put("DATE-1", new String[] { (lmonth) + "月" + (lday) + "日", lmdStr, lmdStr,isONEONE?lyear+"":year+"" });
			c.add(Calendar.DAY_OF_MONTH, 1);
			
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MONTH, -1);
			lmonth = c.get(Calendar.MONTH) + 1;
		
			String mdStr = DateUtil.dateToStr(date, "MMdd");//当日：月日
			String lmStr = DateUtil.dateToStr(c.getTime(), "MM");
			
			result.put("DATE0", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr,year+"" });
			String mStr = mdStr.substring(0, 2);
			if (day >= 1 && day <= 10) {
				result.put("DATE1", new String[] { (lmonth) + "月下旬", lmStr + "20", lmStr + "31",isONE?lyear+"":year+"" });
				result.put("DATE5", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", (month) + "月上旬", mStr + "01", mStr + "10", "旬"});//日与旬
			}
			if (day >= 10 && day <= 20) {
				result.put("DATE1", new String[] { (month) + "月上旬", mStr + "01", mStr + "10",year+"" });
				result.put("DATE5", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", (month) + "月中旬", mStr + "11", mStr + "20", "旬"});//日与旬
			}
			if (day >= 20 && day <= 31) {
				result.put("DATE1", new String[] { (month) + "月中旬", mStr + "11", mStr + "20",year+"" });
				result.put("DATE5", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", (month) + "月下旬", mStr + "20", mStr + "31", "旬"});//日与旬
			}

			result.put("DATE2", new String[] { lmonth + "月", lmStr + "01", lmStr + "31" ,isONE?lyear+"":year+""});
			result.put("DATE6", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", (month) + "月", mStr + "01", mStr + "31", "月"});//日与月
			if (month >= 3 && month <= 5)
				result.put("DATE3", new String[] { "冬季", "1201", "0229" ,year+""});
				result.put("DATE7", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", "春季", "0301", "0530", "季"});//日与季
			if (month >= 6 && month <= 8)
				result.put("DATE3", new String[] { "春季", "0301", "0530" ,year+""});
				result.put("DATE7", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", "夏季", "0601", "0831", "季"});//日与季
			if (month >= 9 && month <= 11)
				result.put("DATE3", new String[] { "夏季", "0601", "0831" ,year+""});
				result.put("DATE7", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", "秋季", "0901", "1130", "季"});//日与季
			if (month >= 12 || month <= 2)
				result.put("DATE3", new String[] { "秋季", "0901", "1130" ,isONE?lyear+"":year+""});
				result.put("DATE7", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", "冬季", "1201", "1231", "季"});//日与季
			result.put("DATE4", new String[] { "年", "0101", "1231",isONE?lyear+"":year+"" });
			result.put("DATE8", new String[] { (month) + "月" + (day) + "日", mdStr, mdStr, year+"", year+"年", "0101", "1231", "年"});//日与年
			return result;
		}
		//本季度开始日期
		public static String getSeasonStartTime(Date date){
			String seasonStartTime = "";
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			SimpleDateFormat shortSdf = new SimpleDateFormat("yyyyMMdd");
			int currentMonth = c.get(Calendar.MONTH) + 1;
	        try {
	            if (currentMonth >= 1 && currentMonth <= 3)
	                c.set(Calendar.MONTH, 0);
	            else if (currentMonth >= 4 && currentMonth <= 6)
	                c.set(Calendar.MONTH, 3);
	            else if (currentMonth >= 7 && currentMonth <= 9)
	                c.set(Calendar.MONTH, 4);
	            else if (currentMonth >= 10 && currentMonth <= 12)
	                c.set(Calendar.MONTH, 9);
	            c.set(Calendar.DATE, 1);
	            seasonStartTime = shortSdf.format(c.getTime());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return seasonStartTime;
		}
		
	/**
     * 获取两个日期之间的所有日期（yyyy-MM-dd）
     * @Description TODO
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static List<String> getBetweenDates(String beginDateStr, String endDateStr) {
    	List<String> result = new ArrayList<String>();
    	try
    	{
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		Date beginDate = sdf.parse(beginDateStr);
    		Date endDate = sdf.parse(endDateStr);
    		Calendar tempStart = Calendar.getInstance();
    		tempStart.setTime(beginDate);
    		while(beginDate.getTime()<=endDate.getTime()){
    			Date timeDate = tempStart.getTime();
    			String timeStr=(new SimpleDateFormat("yyyy-MM-dd")).format(timeDate);
    			result.add(timeStr);
    			tempStart.add(Calendar.DAY_OF_YEAR, 1);
    			beginDate = tempStart.getTime();
    		}
    	}catch (ParseException e){
    		System.out.println(e.getMessage());
    	}
    	return result;
    }
	    /**
	     * 比较两个日期大小
	     * @param DATE1 开始日期
	     * @param DATE2 结束日期
	     * @return 0：开始日期等于结束日期  1：开始日期大于结束日期 -1：开始日期小于结束日期
	     */
	    public static int compareDate(String DATE1, String DATE2) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.getTime() > dt2.getTime()) {
	                //System.out.println("dt1 在dt2前");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	                //System.out.println("dt1在dt2后");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }
	    public static String getDay2(int days,int year){
			Calendar a = Calendar.getInstance();
			a.set(year, 0, 1);
			a.add(Calendar.DAY_OF_MONTH, days-1);
			return DateUtil.dateToStr(a.getTime(), "yyyyMMdd");
		}
	    
	    /**
		 * 日期的加减运算
		 * @param dateStr 日期字符串
		 * @return
		 */
		public static Date dayDateArithmetic(String dateStr,String format,int days)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(strToDate(dateStr,format));
			calendar.add(Calendar.DATE, days);
			return calendar.getTime();
		}
}
