package com.dafang.monitor.nx.product.impl.evaluate.climateAccessment;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.product.impl.evaluate.climateAccessment.util.JFreeChartUtils;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClimateChangeReportHistogramService {
	
	private static String HistogramX = "";//柱状图X轴
	private static double HistogramY = 0.0d;//柱状图Y轴
	private static double HistogramSlope = 0.0d;//柱状图斜度
	
	//日照折线图（常年值、年平均日照时数、年平均日照时数斜度）
	public static Map<String, String> sshHistogram(List<Map<String, Object>> Datas, String startYear, String endYear){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear);
		double perennialValue = Convert.toDouble(NumberUtil.round(Datas.parallelStream().filter(x->Double.valueOf(x.get("year").toString()) >= 1981 &&
				Double.valueOf(x.get("year").toString()) <= 2010).mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics().getAverage(),1));
		double liveValue = 0.0d;
		double slope = 0.0d;
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc.addValue(liveValue, "年平均日照时数", HistogramX);
					dc.addValue(perennialValue, "常年值", HistogramX);
					dc.addValue(slope, "线性（年平均日照时数）", HistogramX);
				}
			}
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年平均日照时数变化";
		String char1 = JFreeChartUtils.createLineChar(fileName, "", "", "日照时数(小时)", dc);
		result.put("sshDraw", char1);
		return result;
	}
	//相对湿度折线图（常年值、年平均相对湿度、年平均相对湿度斜度）
	public static Map<String, String> rhuHistogram(List<Map<String, Object>> Datas, String startYear, String endYear){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear);
		double perennialValue = Convert.toDouble(NumberUtil.round(Datas.parallelStream().filter(x->Double.valueOf(x.get("year").toString()) >= 1981 &&
				Double.valueOf(x.get("year").toString()) <= 2010).mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics().getAverage(),1));
		double liveValue = 0.0d;
		double slope = 0.0d;
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc.addValue(liveValue, "年平均相对湿度", HistogramX);
					dc.addValue(perennialValue, "常年值", HistogramX);
					dc.addValue(slope, "线性（年平均相对湿度）", HistogramX);
				}
			}
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年平均相对湿度变化";
		String char1 = JFreeChartUtils.createLineChar(fileName, "", "", "相对湿度", dc);
		result.put("rhuDraw", char1);
		return result;
	}
	
	//气温折线图
	public static Map<String, String> temHistogram(List<Map<String, Object>> Datas, String startYear, String endYear, int index, String fieldName){
		Map<String, String> result = new HashMap<String, String>();
		//柱状图
		DefaultCategoryDataset dc1 = new DefaultCategoryDataset();
		DefaultCategoryDataset dc2 = new DefaultCategoryDataset();
		for(Map<String, Object> maps : Datas) {
			HistogramSlope = ClimateHandleDate.calSlopeDataY(Datas, "anomalyValue", Integer.valueOf(maps.get("id").toString()));
			HistogramY = Double.valueOf(maps.get("anomalyValue").toString());
			HistogramX = maps.get("year").toString();
			dc1.addValue(HistogramY, "气温距平", HistogramX);//柱子（距平）
			dc2.addValue(HistogramSlope, "线性（气温距平）", HistogramX);//折线（斜度）
		}
		String fileName = ""+startYear+"-"+startYear+fieldName+"距平变化";
		String chart1 = JFreeChartUtils.createChart1(fileName, "", "", "气温距平(℃)", dc1, dc2);
		result.put("temDraw"+index, chart1);
		return result;
	}
	//降水折线图
	public static Map<String, String> preHistogram(List<Map<String, Object>> Datas, String startYear, String endYear, String fieldName, int index){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc1 = new DefaultCategoryDataset();
		DefaultCategoryDataset dc2 = new DefaultCategoryDataset();
		for(Map<String, Object> maps : Datas) {
			HistogramSlope = ClimateHandleDate.calSlopeDataY(Datas, "anomalyValuePercentage", Integer.valueOf(maps.get("id").toString()));
			HistogramY = Double.valueOf(maps.get("anomalyValuePercentage").toString());
			HistogramX = maps.get("year").toString();
			dc1.addValue(HistogramY, "降水距平百分率", HistogramX);//柱子（距平）
			dc2.addValue(HistogramSlope, "线性（降水距平百分率）", HistogramX);//折线（斜度）
		}
		String fileName = ""+startYear+"-"+startYear+fieldName+"降水距平百分率变化";
		String chart1 = JFreeChartUtils.createChart1(fileName, "", "", "降水距平百分率(%)", dc1, dc2);
		result.put("preDraw"+index, chart1);
		return result;
	}
	//降水日数折线图
	public static Map<String, String> preDaysHistogram(List<Map<String, Object>> Datas, String startYear, String endYear){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc1 = new DefaultCategoryDataset();//绘图数据
		DefaultCategoryDataset dc2 = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear);
		double perennialValue = Convert.toDouble(NumberUtil.round(Datas.parallelStream().filter(x->Double.valueOf(x.get("year").toString()) >= 1981 &&
				Double.valueOf(x.get("year").toString()) <= 2010).mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics().getAverage(),1));
		double liveValue = 0.0d;
		double slope = 0.0d;
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc1.addValue(liveValue, "年平均降水日数", HistogramX);
					dc2.addValue(perennialValue, "常年值", HistogramX);
					dc2.addValue(slope, "线性（年降水日数）", HistogramX);
				}
			}
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年平均降水日数变化";
		String char1 = JFreeChartUtils.createChart1(fileName, "", "", "年降水日数", dc1, dc2);
		result.put("preDaysDraw", char1);
		return result;
	}
	//干旱日数柱状图
	public static Map<String, String> droughtDaysHistogram(List<Map<String, Object>> Datas, String startYear, String endYear){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc1 = new DefaultCategoryDataset();//绘图数据
		DefaultCategoryDataset dc2 = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear);
		double liveValue = 0.0d;//年干旱日数
		double slope = 0.0d;//线性（年干旱日数）
		double averageValue = 0.0d;
		double sumLiveValue = 0.0d;
		int s = 0;
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc1.addValue(liveValue, "年干旱日数", HistogramX);
					dc2.addValue(slope, "线性（年干旱日数）", HistogramX);
					sumLiveValue += liveValue;
					s++;
				}
			}	
			if(s!=0) {				
				averageValue = NumberUtil.div(sumLiveValue, s, 1);//多年平均干旱日数
			}
		}
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			dc2.addValue(averageValue, "多年平均", HistogramX);
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年平均干旱日数变化";
		String char1 = JFreeChartUtils.createChart1(fileName, "", "", "年平均干旱日数", dc1, dc2);
		result.put("droDaysDraw", char1);
		return result;
	}
	
	//冰雹柱状图绘制
	public static Map<String, String> hailHistogram(List<Map<String, Object>> Datas, String startYear, String endYear){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear); 
		double liveValue = 0.0d;
		double slope = 0.0d;
		double averageValue = 0.0d;
		double sumLiveValue = 0.0d;
		int s = 0;
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc.addValue(liveValue, "年日数", HistogramX);
					dc.addValue(slope, "线性（年日数）", HistogramX);
					sumLiveValue += liveValue;
					s++;
				}
			}
			if(s!=0) {				
				averageValue = NumberUtil.div(sumLiveValue, s, 1);//多年平均干旱日数
			}
		}
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			dc.addValue(averageValue, "多年平均", HistogramX);
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年冰雹日数变化";
		String char1 = JFreeChartUtils.createLineChar(fileName, "", "", "冰雹日数", dc);
		result.put("hailDraw", char1);
		return result;
	}
	//沙尘柱状图绘制
	public static Map<String, String> sandDustHistogram(List<Map<String, Object>> Datas, String startYear, String endYear, String fieldName, int index){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear); 
		double liveValue = 0.0d;
		double slope = 0.0d;
		double perennialValue = Convert.toDouble(NumberUtil.round(Datas.parallelStream().filter(x->Double.valueOf(x.get("year").toString()) >= 1981 &&
				Double.valueOf(x.get("year").toString()) <= 2010).mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics().getAverage(),1));
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc.addValue(liveValue, "年日数", HistogramX);
					dc.addValue(slope, "线性（年日数）", HistogramX);
					dc.addValue(perennialValue, "常年值", HistogramX);
				}
			}
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年"+fieldName+"日数变化";
		String char1 = JFreeChartUtils.createLineChar(fileName, "", "", "" + fieldName + "日数", dc);
		result.put("sandDustDraw"+index, char1);
		return result;
	}
	//大风柱状图绘制
	public static Map<String, String> galeHistogram(List<Map<String, Object>> Datas, String startYear, String endYear){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear); 
		double liveValue = 0.0d;
		double slope = 0.0d;
		double perennialValue = Convert.toDouble(NumberUtil.round(Datas.parallelStream().filter(x->Double.valueOf(x.get("year").toString()) >= 1981 &&
				Double.valueOf(x.get("year").toString()) <= 2010).mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics().getAverage(),1));
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc.addValue(liveValue, "平均年大风日数", HistogramX);
					dc.addValue(slope, "线性（年日数）", HistogramX);
					dc.addValue(perennialValue, "常年值", HistogramX);
				}
			}
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年大风日数变化";
		String char1 = JFreeChartUtils.createLineChar(fileName, "", "", "大风日数", dc);
		result.put("galeDraw", char1);
		return result;
	}
	//高低温强降水日数柱状图
	public static Map<String, String> highTemHistogram(List<Map<String, Object>> Datas, String startYear, String endYear, String fieldName, String key){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc1 = new DefaultCategoryDataset();//绘图数据
		DefaultCategoryDataset dc2 = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear);
		double liveValue = 0.0d;
		double slope = 0.0d;
		double averageValue = 0.0d;
		double sumLiveValue = 0.0d;
		int s = 0;
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc1.addValue(liveValue, "年日数", HistogramX);
					dc2.addValue(slope, "线性（年日数）", HistogramX);
					sumLiveValue += liveValue;
					s++;
				}
			}	
			if(s!=0) {				
				averageValue = NumberUtil.div(sumLiveValue, s, 1);
			}
		}
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			dc2.addValue(averageValue, "多年平均", HistogramX);
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年"+fieldName+"日数变化";
		String chart1 = JFreeChartUtils.createChart1(fileName, "", "", "年平均" + fieldName + "日数", dc1, dc2);
		result.put(key, chart1);
		return result;
	}
	//暴雨站日柱状图
	public static Map<String, String> rainStormHistogram(List<Map<String, Object>> Datas, String startYear, String endYear){
		Map<String, String> result = new HashMap<String, String>();
		DefaultCategoryDataset dc1 = new DefaultCategoryDataset();//绘图数据
		DefaultCategoryDataset dc2 = new DefaultCategoryDataset();//绘图数据
		int st = Integer.valueOf(startYear);
		int ed = Integer.valueOf(endYear);
		double liveValue = 0.0d;
		double slope = 0.0d;
		double perennialValue = Convert.toDouble(NumberUtil.round(Datas.parallelStream().filter(x->Double.valueOf(x.get("year").toString()) >= 1981 &&
				Double.valueOf(x.get("year").toString()) <= 2010).mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics().getAverage(),1));
		for(int i = st; i <= ed; i++) {
			HistogramX = i+"";
			for (int j = 0; j < Datas.size(); j++) {
				if(Double.valueOf(Datas.get(j).get("year").toString()) <= i ) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(Datas.get(j).get("liveVal").toString()),1));
					slope = ClimateHandleDate.calSlopeDataY(Datas, "liveVal", Integer.valueOf(Datas.get(j).get("id").toString()));
					dc1.addValue(liveValue, "年日数", HistogramX);
					dc2.addValue(slope, "线性（年日数）", HistogramX);
					dc2.addValue(perennialValue, "常年值", HistogramX);
				}
			}	
		}
		String fileName = ""+startYear+"-"+endYear+"年宁夏年暴雨站日变化";
		String char1 = JFreeChartUtils.createChart1(fileName, "", "", "年平均暴雨站日", dc1, dc2);
		result.put("rainStormDraw", char1);
		return result;
	}
}
