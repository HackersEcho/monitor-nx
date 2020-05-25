package com.dafang.monitor.nx.product.impl.evaluate.climateAccessment;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Repository
@Mapper
public class ClimateChangeReportDao {
	
	private static final Log log = LogFactory.getLog(ClimateChangeReportDao.class);

	private static String group1 = "'53519','53610','53611','53612','53614','53615','53617','53618','53619','53704',"
			+ "'53705','53707','53723','53727','53806','53810','53817','53903','53914','53916'";//全区
	
	//数据查询
	private List<Map<String, Object>> queryDate(String sql, String desc){
		List<Map<String, Object>> queryDate = new ArrayList<Map<String,Object>>();
		QueryRunner QueryRunner = new QueryRunner();
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://172.23.106.60:3333/nxcc?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai", "root","root");
			queryDate = QueryRunner.query(connection, sql, new MapListHandler());
			log.info(desc + "获取成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(desc + "获取失败");
		}
		return queryDate;
	}

	//全省基本要素数据平均值查询（每年）
	public List<Map<String, Object>> queryDateYearDate(String year, String condition1, String condition2){
		String sql = "SELECT\r\n" + 
				"	t.time year,\r\n" + 
				"	Avg(t.liveVal) liveVal\r\n" + 
				"FROM\r\n" + 
				"	(SELECT\r\n" + 
				"		stationNo,\r\n" + 
				"		year(ObserverTime) time,\r\n" + 
				"		"+condition1+"\r\n" + 
				"	FROM\r\n" + 
				"		t_mete_ns_day_data \r\n" + 
				"	WHERE\r\n" + 
				"		YEAR ( ObserverTime ) BETWEEN '1961' \r\n" + 
				"		AND '"+year+"' \r\n" + 
				"		AND "+condition2+" \r\n" +
				"		AND stationNo in ("+group1+")\r\n" + 
				"		GROUP BY stationNo,year(ObserverTime)) t\r\n" + 
				"	GROUP BY t.time";
		List<Map<String, Object>> queryDate = queryDate(sql,"全省基本要素数据平均值");
		int i = 1;
		for (Map<String, Object> map : queryDate) {
			map.put("id", i);
			i++;
		}
		return queryDate;
	}
	
	//全省基本要素数据平均值查询(每个站每年)
	public List<Map<String, Object>> queryStationDates(String startDate, String endDate, String condition1, String condition2){
		String sql = "SELECT\r\n" + 
				"	t1.stationNo stationNo,\r\n" + 
				"	t2.station_name stationName,\r\n" + 
				"	t2.longitude,t2.latitude,\r\n" +
				"	YEAR(t1.ObserverTime) year,\r\n" + 
				"	"+condition1+" " +
				"FROM\r\n" + 
				"	t_mete_ns_day_data t1,\r\n" + 
				"	t_mete_station t2\r\n" + 
				"WHERE\r\n" + 
				"	YEAR(t1.ObserverTime) BETWEEN '"+startDate+"' AND '"+endDate+"'\r\n" + 
				"	and t1.stationNo = t2.device_id " +
//				"	and t2.station_type = '1' " +
				"	and t1.stationNo in ("+group1+") " +
				"	and "+condition2+" " +
				"GROUP BY\r\n" + 
				"	t1.stationNo,YEAR(t1.ObserverTime)";
		return queryDate(sql,"全省基本要素数据平均值");
	}
	//全省基本要素数据平均值查询(每个区域每年)
	public List<Map<String, Object>> queryRegionDates(String startDate, String endDate, String condition1, String condition2, String groups){
		String sql = "SELECT \r\n" + 
				"	YEAR ( t1.ObserverTime ) YEAR,\r\n" + 
				"	"+condition1+" \r\n" + 
				"FROM\r\n" + 
				"	t_mete_ns_day_data t1 \r\n" + 
				"WHERE\r\n" + 
				"	YEAR ( t1.ObserverTime ) BETWEEN '"+startDate+"' AND '"+endDate+"' \r\n" + 
				"	AND "+condition2+"" +
				"	AND t1.stationNo in ("+groups+")\r\n" + 
				"GROUP BY\r\n" + 
				"	YEAR ( t1.ObserverTime )";
		return queryDate(sql,"区域年数据");
	}
	
	//根据站点查询年数据
	public List<Map<String, Object>> queryDates(String startDate, String endDate, String condition1, String condition2, String groups){
		String sql = "SELECT\r\n" + 
				"	t1.stationNo,\r\n" + 
				"	t2.station_name,\r\n" + 
				"	YEAR ( t1.ObserverTime ) YEAR,\r\n" + 
				"	"+condition1+" \r\n" + 
				"FROM\r\n" + 
				"	t_mete_ns_day_data t1,\r\n" + 
				"	t_mete_station t2 \r\n" + 
				"WHERE\r\n" + 
				"	t1.stationNo = t2.device_id \r\n" + 
				"	AND YEAR ( t1.ObserverTime ) BETWEEN '"+startDate+"' \r\n" + 
				"	AND '"+endDate+"'  \r\n" + 
				"	AND "+condition2+"\r\n" + 
				"	AND t1.stationNo IN ( "+groups+" ) \r\n" + 
				"GROUP BY\r\n" + 
				"	t1.stationNo,\r\n" + 
				"	YEAR ( t1.ObserverTime )\r\n" + 
				"ORDER BY\r\n" + 
				"	t1.stationNo,year(ObserverTime)";
		return queryDate(sql,"区域年数据");
	}
	
	//天气现象数据查询（各站每年）
	public List<Map<String, Object>> queryStationWeaDatas(String startDate, String endDate, String field){
		String sql = "SELECT \r\n" + 
				"	t1.stationNo stationNo,\r\n" + 
				"	t2.station_name stationName,\r\n" + 
				"	YEAR( ObserverTime ) year,\r\n" + 
				"	COUNT( DISTINCT ObserverTime ) liveVal\r\n" + 
				"FROM\r\n" + 
				"	t_mete_ns_day_wea t1,\r\n" + 
				"	t_mete_station t2\r\n" + 
				"WHERE\r\n" + 
				"	t1.stationNo = t2.device_id\r\n" + 
				"	AND t2.station_type = '1'\r\n" + 
				"	AND t1.WEP_Name = '"+field+"' \r\n" + 
				"	AND YEAR ( t1.ObserverTime ) BETWEEN '"+startDate+"' \r\n" + 
				"	AND '"+endDate+"' \r\n" + 
				"GROUP BY\r\n" + 
				"	stationNo, year( t1.ObserverTime )\r\n" + 
				"ORDER BY\r\n" + 
				"	YEAR ( t1.ObserverTime )";
		List<Map<String, Object>> queryDate = queryDate(sql,"全省各站天气现象年数据");
		int i = 1;
		for (Map<String, Object> map : queryDate) {
			map.put("id", i);
			i++;
		}
		return queryDate;
	}
	
	//干旱日数年数据
	public List<Map<String, Object>> getDroughtData(String startDate, String endDate){
		String sql = "SELECT\r\n" + 
				"	t1.stationNo stationNo,\r\n" + 
				"	t2.station_name staionName,\r\n" + 
				"	year(t1.ObserverTime) year,\r\n" + 
				"	count(1) liveVal \r\n" + 
				"FROM\r\n" + 
				"	t_mete_climate_ci_index t1,\r\n" + 
				"	t_mete_station t2 \r\n" + 
				"WHERE\r\n" + 
				"	t1.stationNo = t2.device_id \r\n" + 
				"	AND t2.station_type = '1' \r\n" + 
				"	AND t1.CI_VAL > '-999' \r\n" + 
				"	AND t1.CI_VAL <= '-0.6'\r\n" + 
				"	AND year(t1.ObserverTime) >= '"+startDate+"'\r\n" + 
				"	AND year(t1.ObserverTime) <= '"+endDate+"'\r\n" + 
				"GROUP BY\r\n" + 
				"	stationNo,year(t1.ObserverTime)";
		List<Map<String, Object>> queryDate = queryDate(sql,"干旱日数年数据");
		int i = 1;
		for (Map<String, Object> map : queryDate) {
			map.put("id", i);
			i++;
		}
		return queryDate;
	}
	
	//站点
	public List<Map<String, Object>> getStationNo(){
		String sql = "SELECT device_id stationNo,station_name stationName,longitude,latitude"
				+ " FROM t_mete_station WHERE device_id in ("+group1+")";
		return queryDate(sql,"全省基本要素数据平均值");
	}
	//区域
	public List<Map<String, Object>> getGroup(){
		String sql = "SELECT id groupId,group_name groupName,station_id_array FROM t_mete_station_group WHERE id in ('1','4','5','6')";
		return queryDate(sql,"全省基本要素数据平均值");
	}

	//气温月平均值
	public List<Map<String, Object>> queryMonthDates(String month, String condition1, String condition2){
		String sql = "SELECT\r\n" + 
				"	t1.stationNo stationNo,\r\n" + 
				"	t2.station_name stationName,\r\n" + 
				"	DATE_FORMAT(T1.ObserverTime,'%Y%m') monthDates,\r\n" + 
				"	"+condition1+" val\r\n" + 
				"FROM\r\n" + 
				"	t_mete_ns_day_data t1,\r\n" + 
				"	t_mete_station t2 \r\n" + 
				"WHERE\r\n" + 
				"	DATE_FORMAT(T1.ObserverTime,'%m') = '"+month+"'\r\n" + 
				"	AND t1.stationNo = t2.device_id \r\n" + 
				"	AND t2.station_type = '1' \r\n" + 
				"	AND "+condition2+"\r\n" + 
				"GROUP BY\r\n" + 
				"t1.stationNo,DATE_FORMAT(t1.ObserverTime,'%Y%m')";
		return queryDate(sql,"气温月平均值");
	}
}
