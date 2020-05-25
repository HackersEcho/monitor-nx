package com.dafang.monitor.nx.product.impl.evaluate.climateAccessment;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.utils.DrawUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
@Service
public class ClimateChangeReportService extends TemplateAbstract {
	private String stYear;
	private String edYear;
	private String fb = ""; //分别
	private String ds = "偏多";//偏多偏少
	private String zj = "增加";//增加减少
	private String sj = "上升";//上升下降
	private String gd = "偏高";//偏高偏低
	private String group1 = "'53519','53610','53611','53612','53614','53615','53617','53618','53619','53704',"
			+ "'53705','53707','53723','53727','53806','53810','53817','53903','53914','53916'";//全区
	private static String group2 = "'53517','53518','53519','53610','53611','53612','53614','53615','53617',"
	+ "'53618','53619','53704','53705','53707','53723','53727','53806','53810','53881'";//中北部
	private String group4 = "'53519','53610','53611','53612','53614','53615','53617','53618','53619','53704','53705'";//引黄灌区
	private String group5 = "'53707','53723','53727','53806','53810'";//中部干旱带
	private String group6 = "'53817','53903','53914','53916'";//南部山区
	private Map<String, String> groups = new HashMap<String, String>();
	{groups.put("全区", group1);
		groups.put("引黄灌区", group4);
		groups.put("中部干旱带", group5);
		groups.put("南部山区", group6);};

	@Resource
	ClimateChangeReportDao ClimateChangeReportDao;

	@Override
	protected void init(ProductParams params) {
		year = params.getYear();
		fileName = year + "年" + ProductEmun.getFileName(18);
		templateName = "宁夏变化评估报告.ftl";
	}

	@Override
	protected Map<String, Object> getDatas(ProductParams params) {
		stYear = "1961";
		edYear = year;
		return handleDate();
	}

	//数据处理
	public Map<String, Object> handleDate(){
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, String> mapPraResults = new HashMap<String, String>();
		Map<String, String> mapHisResults = new HashMap<String, String>();
		Map<String, String> mapPicResults = new HashMap<String, String>();
		List<Map<String, Object>> stationInfos = ClimateChangeReportDao.getStationNo();//站点信息
		
		//气温
		Map<String, Object> temMap = temPara(stationInfos);
		//降水
		Map<String, Object> preMap = prePara(stationInfos);
		//日照
		Map<String, Object> sshMap = sshPara(stationInfos);
		//相对湿润度
		Map<String, Object> rhuMap = rhuPara();
		//霜冻
		Map<String, Object> hailMap = hailPara();
		//沙尘
		Map<String, Object> sandDustMap = sandDustPara();
		//大风
		Map<String, Object> galeMap = galePara();
		//高温
		Map<String, Object> highTemMap = highTemPara();
		//低温
		Map<String, Object> lowTemMap = lowTemPara();
		//强降水
		Map<String, Object> heavyPreMap = heavyPre();
		//干旱
		Map<String, Object> droughtMap = droughtPara();

		results.putAll(temMap);
		results.putAll(preMap);
		results.putAll(sshMap);
		results.putAll(rhuMap);
		results.putAll(hailMap);
		results.putAll(sandDustMap);
		results.putAll(galeMap);
		results.putAll(highTemMap);
		results.putAll(lowTemMap);
		results.putAll(heavyPreMap);
		results.putAll(droughtMap);
		results.put("year",year);
		return results;
	}
	
	//气温
	//1.
	//1961～X年，宁夏年平均气温呈显著X趋势，平均每10年升高X℃（见图1.1.1a）。X年宁夏平均气温为X℃，较常年偏高X℃。
	//宁夏平均最高气温每10年升高X℃（见图1.1.1b）。X年宁夏平均最高气温为X℃，较常年偏高X℃。
	//宁夏平均最低气温也呈明显的X趋势（见图1.1.1c），平均每10年上升X℃。X年宁夏平均最低气温为X℃，较常年偏高X℃。
	//2.
	//X年，宁夏各地平均气温为X～X℃（图1.1.2a），与常年相比，偏高X～X℃（图1.1.2b）；
	//各地平均最高气温为X～X℃（图1.1.3a），偏高X～X℃（图1.1.3b）；
	//各地平均最低气温为X～X℃（图1.1.4a），除X偏低X℃外，其他大部偏高X～X℃（图1.1.4b）。
	public Map<String, Object> temPara(List<Map<String, Object>> stationInfos){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		Map<String, String> mapHis = new HashMap<String, String>();//柱状图
		Map<String, String> mapPra = new HashMap<String, String>();//语句
		Map<String, String> mapDraw = new HashMap<String, String>();//分布图
		List<Map<String, Object>> drawData = new ArrayList<Map<String,Object>>();//柱状图数据
		List<Map<String, Object>> picData = new ArrayList<Map<String,Object>>();//分布图数据
		StringBuilder presb1 = new StringBuilder();//气温第一句
		StringBuilder presb2 = new StringBuilder();//气温第一句
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("气温", "TEM_Avg");
		fieldMap.put("最高气温", "TEM_Max");
		fieldMap.put("最低气温", "TEM_Min");
		presb1.append(stYear+"～"+edYear+"年，");
		int index = 0;
		for(Entry<String, String> entry : fieldMap.entrySet()) {
			String tu1 = "",tu2 = "",tu3 = "";
			String fieldName = entry.getKey();
			String field = entry.getValue();
			switch (field) {
				case "TEM_Avg" -> {
					tu1 = "（见图1.1.1a）";
					tu2 = "（图1.1.2a）";
					tu3 = "（图1.1.2b）";
				}
				case "TEM_Max" -> {
					tu1 = "（见图1.1.1b）";
					tu2 = "（图1.1.3a）";
					tu3 = "（图1.1.3b）";
				}
				case "TEM_Min" -> {
					tu1 = "（见图1.1.1c）";
					tu2 = "（图1.1.4a）";
					tu3 = "（图1.1.4b）";
				}
			}
			String condition1 = "AVG("+field+") liveVal";//查询条件
			String condition2 = ""+field+" between '-999' and '999'";
			//全省年数据
			List<Map<String, Object>> allYearList = ClimateChangeReportDao.queryDateYearDate(edYear, condition1, condition2);
			double perennialValue = Convert.toDouble(NumberUtil.round(allYearList.stream().filter(x->Integer.parseInt(x.get("year").toString())>=1981
					&& Integer.parseInt(x.get("year").toString())<=2010).mapToDouble(x->Double.parseDouble(x.get("liveVal").toString())).
					summaryStatistics().getAverage(),1));
			double changeValue = ClimateHandleDate.calSlopeData(allYearList, "liveVal");//平均每10年变化 
			double liveValue = 0.0d;
			double anomalyValue = 0.0d;
			for (int j = 0; j < allYearList.size(); j++) {
				Map<String, Object> drawMap = new HashMap<String, Object>();
				drawMap.put("id", j+1);//id
				drawMap.put("year", allYearList.get(j).get("year"));//年份
				drawMap.put("anomalyValue", NumberUtil.sub(NumberUtil.round(Double.parseDouble(allYearList.get(j).get("liveVal").toString()),1), perennialValue, 1));//距平
				drawData.add(drawMap);
				if(allYearList.get(j).get("year").toString().equals(edYear)) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.parseDouble(allYearList.get(j).get("liveVal").toString()),1));
					anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
				}
			}
			if(changeValue<0) {
				sj = "下降";
				changeValue = Math.abs(changeValue);
			}
			presb1.append("宁夏年平均"+fieldName+"呈显著"+sj+"趋势，平均每10年"+sj+changeValue+"℃"+tu1+"。");
			if(anomalyValue<0) {
				gd = "偏低";
				anomalyValue = Math.abs(anomalyValue);
			}
			presb1.append(""+edYear+"年宁夏平均"+fieldName+"为"+liveValue+"℃，较常年"+gd+anomalyValue+"℃。");
			//站点年数据
			presb2.append(""+edYear+"年，");
			List<Map<String, Object>> stationYearList = ClimateChangeReportDao.queryStationDates(stYear, edYear, condition1, condition2);
			List<Map<String, Object>> stationLocalYearList = stationYearList.stream().
					filter(x->x.get("year").toString().equals(edYear)).collect(Collectors.toList());
			String stations1 = "", stations2 = "";
			double anomalyValueMax1 = -999d,anomalyValueMin1 = 999d,
					anomalyValueMax2 = -999d,anomalyValueMin2 = 999d;
			int s1 = 0, s2 = 0;
			for (Map<String, Object> stationInfo : stationInfos) {
				String stationNo = stationInfo.get("stationNo").toString();
				String stationName = stationInfo.get("stationName").toString();
				liveValue = 0.0d;
				anomalyValue = 0.0d;
				perennialValue = Convert.toDouble(NumberUtil.round(stationYearList.stream().filter(x -> Integer.valueOf(x.get("year").toString()) >= 1981
						&& Integer.valueOf(x.get("year").toString()) <= 2010 && StringUtils.contains(x.get("stationNo").toString(), stationNo)).
						mapToDouble(x -> Double.parseDouble(x.get("liveval").toString())).summaryStatistics().getAverage(), 1));
				for (Map<String, Object> map : stationLocalYearList) {
					if (map.get("stationNo").toString().equals(stationNo)) {
						liveValue = Convert.toDouble(NumberUtil.round(Double.parseDouble(map.get("liveVal").toString()), 1));
						anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
						Map<String, Object> mapPic = new HashMap<String, Object>();
						mapPic.put("stationNo", stationNo);
						mapPic.put("longitude", map.get("longitude").toString());
						mapPic.put("latitude", map.get("latitude").toString());
						mapPic.put("temp1", liveValue);
						mapPic.put("tempAnomaly1", anomalyValue);
						picData.add(mapPic);
					}
				}
				if (anomalyValue > 0) {//偏多
					stations1 += stationName + "、";
					if (anomalyValue > anomalyValueMax1) {
						anomalyValueMax1 = anomalyValue;
					}
					if (anomalyValue < anomalyValueMin1) {
						anomalyValueMin1 = anomalyValue;
					}
					s1++;
				} else if (anomalyValue < 0) {//偏少
					stations2 += stationName + "、";
					if (anomalyValue > anomalyValueMax2) {
						anomalyValueMax2 = Math.abs(anomalyValue);
					}
					if (anomalyValue < anomalyValueMin2) {
						anomalyValueMin2 = Math.abs(anomalyValue);
					}
					s2++;
				}
			}
			DoubleSummaryStatistics summaryStatistics = stationLocalYearList.stream().
					mapToDouble(x->Double.parseDouble(x.get("liveval").toString())).summaryStatistics();
			double max = Convert.toDouble(NumberUtil.round(summaryStatistics.getMax(),1));
			double min = Convert.toDouble(NumberUtil.round(summaryStatistics.getMin(),1));
			presb2.append("宁夏各地平均"+fieldName+"为"+min+"～"+max+"℃"+tu2+"，与常年相比");
			if(s1>0) {	
				gd = "偏高";
				if(s1>1) {
					presb2.append("，"+stations1.substring(0,stations1.length()-1)+gd+anomalyValueMin1+"~"+anomalyValueMax1+"℃");
				}else if(s1==1) {
					presb2.append("，"+stations1.substring(0,stations1.length()-1)+gd+anomalyValueMin1+"℃");
				}
			}
			if(s2>0) {
				gd = "偏低";
				if(s2>1) {
					presb2.append("，"+stations2.substring(0,stations2.length()-1)+gd+anomalyValueMin2+"~"+anomalyValueMax2+"℃");
				}else if(s2==1) {
					presb2.append("，"+stations2.substring(0,stations2.length()-1)+gd+anomalyValueMin2+"℃");
				}
			}
			presb2.append(""+tu3+"。");

			//柱状图
			index++;
			Map<String, String> temHistogram = ClimateChangeReportHistogramService.temHistogram(drawData, stYear, edYear, index, fieldName);
			mapHis.putAll(temHistogram);
			
			//分布图
			String res1 = DrawUtils.drawImg(picData, "temp1","气温实况分布");
			String res2 = DrawUtils.drawImg(picData, "tempAnomaly1","气温距平分布");
			mapDraw.put("temPicLiv"+index, res1);
			mapDraw.put("temPicAno"+index, res2);
		}
		mapPra.put("temFirstPara1", presb1.toString());
		mapPra.put("temFirstPara2", presb2.toString());
		mapResult.putAll(mapPra);
		mapResult.putAll(mapHis);
		mapResult.putAll(mapDraw);
		return mapResult;
	}	
	
	//日照
	public Map<String, Object> sshPara(List<Map<String, Object>> stationInfos){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> mapPra = new HashMap<String, String>();
		Map<String, String> mapHis = new HashMap<String, String>();
		Map<String, String> mapPic = new HashMap<String, String>();
		List<Map<String, Object>> picData = new ArrayList<Map<String,Object>>();//分布图数据
		StringBuilder sshsb1 = new StringBuilder();//日照第一句
		String condition1 = "SUM(SSH) liveval";//查询条件
		String condition2 = "SSH between -999 and 999";
		//全省年数据
		List<Map<String, Object>> yearDatas = ClimateChangeReportDao.queryDateYearDate(edYear, condition1, condition2);
		double changeValue = ClimateHandleDate.calSlopeData(yearDatas, "liveval");//平均每10年变化
		if(changeValue>0) {//变化趋势
			zj = "增加";
		}else {
			zj = "减少";
		}
		//站点年数据
		List<Map<String, Object>> stationDatas = ClimateChangeReportDao.queryStationDates("1961", edYear, condition1, condition2);
		List<Map<String, Object>> LocalDateList = stationDatas.parallelStream().
				filter(x->StringUtils.contains(x.get("year").toString(), edYear)).collect(Collectors.toList());//当年数据
		DoubleSummaryStatistics summaryStatistics = LocalDateList.stream().
				mapToDouble(x->Double.valueOf(x.get("liveval").toString())).summaryStatistics();
		double max = Convert.toDouble(NumberUtil.round(summaryStatistics.getMax(),1));
		double min = Convert.toDouble(NumberUtil.round(summaryStatistics.getMin(),1));
		String sta1 = "";
		String sta2 = "";
		double anoValmax1 = -999d, anoValmin1 = 999d, anoValmax2 = -999d, anoValmin2 = 999d;
		int s1 = 0;
		int s2 = 0;
		for (int i = 0; i < stationInfos.size(); i++) {
			String stationNo = stationInfos.get(i).get("stationNo").toString();
			double perennialValue = Convert.toDouble(NumberUtil.round(stationDatas.parallelStream().filter(x->Integer.valueOf(x.get("year").toString()) >= 1981
					&& Integer.valueOf(x.get("year").toString()) <= 2010 && StringUtils.contains(x.get("stationNo").toString(), stationNo)).
					mapToDouble(x->Double.parseDouble(x.get("liveval").toString())).summaryStatistics().getAverage(),1));
			for (int j = 0; j < LocalDateList.size(); j++) {
				String stationName = LocalDateList.get(j).get("stationName").toString();
				if(LocalDateList.get(j).get("stationNo").toString().equals(stationNo)) {
					double liveValue = Double.valueOf(LocalDateList.get(i).get("liveval").toString());
					double anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
					Map<String, Object> mapDraw = new HashMap<String, Object>();
					mapDraw.put("stationNo", stationNo);
					mapDraw.put("longitude", LocalDateList.get(j).get("longitude").toString());
					mapDraw.put("latitude", LocalDateList.get(j).get("latitude").toString());
					mapDraw.put("sshYear1", liveValue);
					mapDraw.put("sshYearAnomaly1", anomalyValue);
					picData.add(mapDraw);
					if(anomalyValue > 0) {
						s1++;
						sta1 += stationName+"、";
						if(anomalyValue > anoValmax1) {
							anoValmax1 = anomalyValue;
						}
						if(anomalyValue < anoValmin1) {
							anoValmin1 = anomalyValue;
						}
					}else if(anomalyValue < 0) {
						s2++;
						sta2 += stationName+"、";
						if(anomalyValue > anoValmax2) {
							anoValmax2 = Math.abs(anomalyValue);
						}
						if(anomalyValue < anoValmin2) {
							anoValmin2 = Math.abs(anomalyValue);
						}
					}
				}
			}
		}
		sshsb1.append("1961～"+edYear+"年，宁夏年日照时数呈"+zj+"变化趋势（图1.3.1）。"+edYear+"年各地日照时数为"+min+"～"+max+"小时（图1.3.2），与常年相比");
		if(s1>0) {	
			ds = "偏多";
			if(s1>1) {
				sshsb1.append("，"+sta1.substring(0,sta1.length()-1)+ds+anoValmin1+"~"+anoValmax1+"小时");
			}else if(s1==1) {
				sshsb1.append("，"+sta1.substring(0,sta1.length()-1)+ds+anoValmin1+"小时");
			}
		}
		if(s2>0) {
			ds = "偏少";
			if(s2>1) {
				sshsb1.append("，"+sta2.substring(0,sta2.length()-1)+ds+anoValmax2+"~"+anoValmin2+"小时。");
			}else if(s2==1) {
				sshsb1.append("，"+sta2.substring(0,sta2.length()-1)+ds+anoValmin2+"小时。");
			}
		}
		//柱状图
		mapHis.putAll(ClimateChangeReportHistogramService.sshHistogram(yearDatas, stYear, edYear));
		//分布图
		String res1 = DrawUtils.drawImg(picData, "sshYear1","日照时数实况分布");
		String res2 = DrawUtils.drawImg(picData, "sshYearAnomaly1","日照时数距平分布");
		mapPic.put("sshPicLiv", res1);
		mapPic.put("sshPicAno", res2);
		mapPra.put("sshsb1", sshsb1.toString());
		result.putAll(mapHis);
		result.putAll(mapPra);
		result.putAll(mapPic);
		return result;
	};
	
	//相对湿度
	public Map<String, Object> rhuPara(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> rhuPra = new HashMap<String, String>();
		Map<String, String> rhuHis = new HashMap<String, String>();
		StringBuilder rhusb1 = new StringBuilder();//相对湿润度第一段
		String condition1 = "AVG(RHU_Avg) liveval";//查询条件
		String condition2 = "RHU_Avg between -999 and 999";
		//全省年数据
		List<Map<String, Object>> allYearList = ClimateChangeReportDao.queryDateYearDate(edYear, condition1, condition2);	
		double perennialValue = Convert.toDouble(NumberUtil.round(allYearList.stream().filter(x->Integer.valueOf(x.get("year").toString())>=1981
				&& Integer.valueOf(x.get("year").toString())<=2010).mapToDouble(x->Double.parseDouble(x.get("liveval").toString())).
				summaryStatistics().getAverage(),1));
		double liveValue = 0.0d;
		double anomalyValue = 0.0d;
		for (int i = 0; i < allYearList.size(); i++) {
			if(allYearList.get(i).get("year").toString().equals(edYear)) {
				liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(allYearList.get(i).get("liveval").toString()),1));
				anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
			}
		}
		//1961～X年，宁夏年平均相对湿度呈X趋势，平均每10年降低X%（图1.4.1）。2018年宁夏平均相对湿度为X%，偏高X。
		double changeValue = ClimateHandleDate.calSlopeData(allYearList, "liveval");//年趋势	
		if(changeValue < 0) {
			sj = "下降";
			
			changeValue = Math.abs(changeValue);
		}
		rhusb1.append("1961～"+edYear+"年，宁夏年平均相对湿度呈"+sj+"趋势，平均每10年降低"+changeValue+"%（图1.4.1）。");
		if(anomalyValue < 0) {
			gd = "偏低";
			anomalyValue = Math.abs(anomalyValue);
		}
		rhusb1.append(""+edYear+"年宁夏平均相对湿度为"+liveValue+"%，较常年"+gd+anomalyValue+"%。");
		
		//柱状图===========================
		rhuHis.putAll(ClimateChangeReportHistogramService.rhuHistogram(allYearList, stYear, edYear));
		//=================================	
		rhuPra.put("rhusb1", rhusb1.toString());
		result.putAll(rhuHis);
		result.putAll(rhuPra);
		return result;
	}
	
	//降水
	//1.1961～X年，宁夏平均年降水量呈X趋势，具有明显的阶段性变化特征。X年，宁夏平均年降水量为X毫米，较常年同期偏多X%，引黄灌区、中部干旱带、南部山区分别为X毫米、X毫米、X毫米，分别偏多X%、X%、X%。
	//2.X年，各地降水量在X～X毫米之间（图1.2.2-3），与常年同期相比，X地区偏多X%以上，其他大部偏多X%以下。
	//其中X/X-1年冬季宁夏平均降水量为X毫米，较常年同期偏多X%；X年春季平均降水量为X毫米，偏多X%；夏季为X毫米，偏多X%；秋季为X毫米，偏多X%。
	//3.1961～X年，宁夏平均年降水日数呈X趋势，每10年减少X天。X年降水日数为X天，为1961年以来最多，X年最少，仅为X天。X年，平均年降水日数为X天，较常年偏多X天（图1.2.4）。
	//初始化全省年数据，常年值、实况值、距平、斜度、斜度参数b(乘10即是每10年变化)
	public Map<String, Object> prePara(List<Map<String, Object>> stationInfos){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> prePra = new HashMap<String, String>();
		Map<String, String> preHis = new HashMap<String, String>();
		Map<String, String> prePic = new HashMap<String, String>();
		List<Map<String, Object>> drawData = new ArrayList<Map<String,Object>>();//降水量柱状图数据
		List<Map<String, Object>> picData = new ArrayList<Map<String,Object>>();//降水量分布图数据
		StringBuilder presb1 = new StringBuilder();//降水第一段
		StringBuilder presb2 = new StringBuilder();//降水第二段
		StringBuilder presb3 = new StringBuilder();//降水第三段
		String condition1 = "SUM(PRE_Time_2020) liveval";//查询条件
		String condition2 = "PRE_Time_2020 between 0.1 and 999";
		//全省年数据
		List<Map<String, Object>> allYearList = ClimateChangeReportDao.queryDateYearDate(edYear, condition1, condition2);	
		double changeValue = ClimateHandleDate.calSlopeData(allYearList, "liveval");
		double perennialValue = Convert.toDouble(NumberUtil.round(allYearList.stream().filter(x->Integer.valueOf(x.get("year").toString())>=1981
				&& Integer.valueOf(x.get("year").toString())<=2010).mapToDouble(x->Double.parseDouble(x.get("liveval").toString())).
				summaryStatistics().getAverage(),1));
		double liveValue = 0.0d;
		double anomalyValue = 0.0d;
		double anomalyValuePercentage = 0.0d;
		for (int i = 0; i < allYearList.size(); i++) {
			if(allYearList.get(i).get("year").toString().equals(edYear)) {
				liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(allYearList.get(i).get("liveval").toString()),1));
				anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
				anomalyValuePercentage = Convert.toDouble(NumberUtil.div(anomalyValue, perennialValue, 1));
			}
		}
		if(changeValue < 0) {
			sj = "下降";
		}
		presb1.append("1961～"+edYear+"年，宁夏平均年降水量呈"+sj+"趋势，具有明显的阶段性变化特征。");
		if(anomalyValuePercentage>0) {
			ds = "偏少";
			anomalyValuePercentage = Math.abs(anomalyValuePercentage);
		}
		presb1.append(""+edYear+"年，宁夏平均年降水量为"+liveValue+"毫米，较常年"+ds+anomalyValuePercentage+"%");
		
		//区域年数据
		String groups1 = "", groups2 = "",groupLiveVal1 = "",groupLiveVal2 = "",
				groupAnomalyValuePercentage1 = "",groupAnomalyValuePercentage2 = "";
		int ss1 = 0, ss2 = 0;
		int index = 0;
		for (Entry<String, String> entry : groups.entrySet()) {
			String groupId = entry.getValue();
			String groupName = entry.getKey();
			List<Map<String, Object>> groupYearList = ClimateChangeReportDao.queryRegionDates(stYear, edYear, condition1, condition2, groupId);
			perennialValue = Convert.toDouble(NumberUtil.round(allYearList.stream().filter(x->Integer.valueOf(x.get("year").toString())>=1981
					&& Integer.valueOf(x.get("year").toString())<=2010).mapToDouble(x->Double.parseDouble(x.get("liveval").toString())).
					summaryStatistics().getAverage(),1));
			liveValue = 0.0d;
			anomalyValue = 0.0d;
			anomalyValuePercentage = 0.0d;
			for (int i = 0; i < groupYearList.size(); i++) {
				Map<String, Object> drawMap = new HashMap<String, Object>();
				drawMap.put("id", i+1);//id
				drawMap.put("year", groupYearList.get(i).get("year"));//年份
				drawMap.put("anomalyValuePercentage", 100*Convert.toDouble(NumberUtil.div(NumberUtil.sub(
						NumberUtil.round(Double.valueOf(allYearList.get(i).get("liveVal").toString()),1), perennialValue, 1), perennialValue, 2)));//距平百分率
				drawData.add(drawMap);
				if(groupYearList.get(i).get("year").toString().equals(edYear)) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.valueOf(groupYearList.get(i).get("liveval").toString()),1));
					anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
					anomalyValuePercentage = Convert.toDouble(NumberUtil.div(anomalyValue, perennialValue, 1));
				}
			}
			if(anomalyValuePercentage > 0) {//偏多
				groups1 += groupName+"、";
				groupLiveVal1 += liveValue+"毫米、";
				groupAnomalyValuePercentage1 += anomalyValuePercentage+"%、";
				ss1++;
			}else if(anomalyValuePercentage < 0){//偏少
				groups2 += groupName+"、";
				groupLiveVal2 += liveValue+"毫米、";
				groupAnomalyValuePercentage2 += Math.abs(anomalyValuePercentage)+"%、";
				ss2++;
			}
			
			//降水量柱状图============================================================
			index++;
			preHis.putAll(ClimateChangeReportHistogramService.preHistogram(drawData, stYear, edYear, groupName, index));
			//=====================================================================
		}
		if(ss1>0) {	
			ds = "偏多";
			if(ss1>1) {
				fb = "分别"; 
			}else if(ss1==1) {
				fb = "";
			}
			presb1.append("，"+groups1.substring(0,groups1.length()-1)+fb+"为"+groupLiveVal1.substring(0, groupLiveVal1.length()-1)+"，"+fb+ds+
					groupAnomalyValuePercentage1.substring(0,groupAnomalyValuePercentage1.length()-1)+"");
		}
		if(ss2>0) {
			ds = "偏少";
			if(ss2>1) {
				fb = "分别"; 
			}else if(ss2==1) {
				fb = "";
			}
			presb1.append("，"+groups2.substring(0,groups2.length()-1)+fb+"为"+groupLiveVal2.substring(0, groupLiveVal2.length()-1)+"，"+fb+ds+
					groupAnomalyValuePercentage2.substring(0,groupAnomalyValuePercentage2.length()-1)+"");
		}
		presb1.append("。");
		
		//各站年数据
		condition1 = "SUM(t1.PRE_Time_2020) liveval";//查询条件
		condition2 = "PRE_Time_2020 between 0.1 and 999";
		List<Map<String, Object>> stationYearList = ClimateChangeReportDao.queryStationDates(stYear, edYear, condition1, condition2);
		List<Map<String, Object>> stationLocalYearList = stationYearList.stream().
				filter(x->x.get("year").toString().equals(edYear)).collect(Collectors.toList());
		String stations1 = "", stations2 = "";
		double anomalyValuePercentageMax1 = -999d,anomalyValuePercentageMin1 = 999d,
				anomalyValuePercentageMax2 = -999d,anomalyValuePercentageMin2 = 999d;
		int s1 = 0, s2 = 0;
		for(int i=0; i<stationInfos.size(); i++) {
			String stationNo = stationInfos.get(i).get("stationNo").toString();
			String stationName = stationInfos.get(i).get("stationName").toString();
			liveValue = 0.0d;
			anomalyValue = 0.0d;
			anomalyValuePercentage = 0.0d;
			perennialValue = Convert.toDouble(NumberUtil.round(stationYearList.stream().filter(x->Integer.valueOf(x.get("year").toString())>=1981
					&& Integer.valueOf(x.get("year").toString())<=2010 && StringUtils.contains(x.get("stationNo").toString(),stationNo)).
					mapToDouble(x->Double.parseDouble(x.get("liveval").toString())).summaryStatistics().getAverage(),1));
			for(int j=0; j<stationLocalYearList.size(); j++) {
				if(stationLocalYearList.get(j).get("stationNo").toString().equals(stationNo)) {
					liveValue = Convert.toDouble(NumberUtil.round(Double.parseDouble(stationLocalYearList.get(j).get("liveVal").toString()),1));
					anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
					anomalyValuePercentage = Convert.toDouble(NumberUtil.div(anomalyValue, perennialValue, 1));
					Map<String, Object> mapPic = new HashMap<String, Object>();
					mapPic.put("stationNo", stationNo);
					mapPic.put("longitude", stationLocalYearList.get(j).get("longitude").toString());
					mapPic.put("latitude", stationLocalYearList.get(j).get("latitude").toString());
					mapPic.put("pre1", liveValue);
					mapPic.put("preAnomaly1", anomalyValuePercentage);
					picData.add(mapPic);
				}
			}
			if(anomalyValuePercentage > 0) {//偏多
				stations1 += stationName+"、";
				if(anomalyValuePercentage > anomalyValuePercentageMax1) {
					anomalyValuePercentageMax1 = anomalyValuePercentage;
				}
				if(anomalyValuePercentage < anomalyValuePercentageMin1) {
					anomalyValuePercentageMin1 = anomalyValuePercentage;
				}
				s1++;
			}else if(anomalyValuePercentage < 0){//偏少
				stations2 += stationName+"、";
				if(anomalyValuePercentage > anomalyValuePercentageMax2) {
					anomalyValuePercentageMax2 = Math.abs(anomalyValuePercentage);
				}
				if(anomalyValuePercentage < anomalyValuePercentageMin2) {
					anomalyValuePercentageMin2 = Math.abs(anomalyValuePercentage);
				}
				s2++;
			}
		}
		DoubleSummaryStatistics summaryStatistics = stationLocalYearList.stream().
				mapToDouble(x->Double.parseDouble(x.get("liveval").toString())).summaryStatistics();
		double max = Convert.toDouble(NumberUtil.round(summaryStatistics.getMax(),1));
		double min = Convert.toDouble(NumberUtil.round(summaryStatistics.getMin(),1));
		presb2.append(""+edYear+"年，各地降水量在"+min+"～"+max+"mm之间（图1.2.2-3），与常年相比");
		if(s1>0) {	
			ds = "偏多";
			if(s1>1) {
				presb2.append("，"+stations1.substring(0,stations1.length()-1)+ds+anomalyValuePercentageMin1+"~"+anomalyValuePercentageMax1+"%");
			}else if(s1==1) {
				presb2.append("，"+stations1.substring(0,stations1.length()-1)+ds+anomalyValuePercentageMin1+"%");
			}
		}
		if(s2>0) {
			ds = "偏少";
			if(s2>1) {
				presb2.append("，"+stations2.substring(0,stations2.length()-1)+ds+anomalyValuePercentageMax2+"~"+anomalyValuePercentageMin2+"%");
			}else if(s2==1) {
				presb2.append("，"+stations2.substring(0,stations2.length()-1)+ds+anomalyValuePercentageMin2+"%");
			}
		}
		presb2.substring(0, presb2.length()-1);
		presb2.append("。");
		//全省年降水日数数据
		double preDays = 0.0d;
		condition1 = "count(DISTINCT ObserverTime) liveval";//查询条件
		condition2 = "PRE_Time_2020 between 0.1 and 999";
		List<Map<String, Object>> preDaysList = ClimateChangeReportDao.queryDateYearDate(edYear, condition1, condition2);
		double preDaysMax = 0.0d;
		double preDaysMin = 9999d;
		String preDaysMaxYear = "";
		String preDaysMinYear = "";
		for(int i=0; i<preDaysList.size(); i++){
			if(preDaysList.get(i).get("year").toString().equals(edYear)) {
				preDays = Double.parseDouble(preDaysList.get(i).get("liveVal").toString());
			}
			double preday = Double.parseDouble(preDaysList.get(i).get("liveVal").toString());
			if(preday != 0) {				
				if(preday >= preDaysMax) {				
					preDaysMax = Double.parseDouble(preDaysList.get(i).get("liveVal").toString());
					preDaysMaxYear = preDaysList.get(i).get("year").toString();
				}
				if(preday <= preDaysMin) {				
					preDaysMin = Double.parseDouble(preDaysList.get(i).get("liveVal").toString());
					preDaysMinYear = preDaysList.get(i).get("year").toString();
				}
			}
		}
		changeValue = ClimateHandleDate.calSlopeData(preDaysList, "liveVal");
		if(changeValue<0) {
			ds = "偏少";
			zj = "减少";
			changeValue = Math.abs(changeValue);
		}
		double preDaysPerennialValue = preDaysList.stream().filter(x->Integer.parseInt(x.get("year").toString())>=1981 && 
				Integer.parseInt(x.get("year").toString())<=2010).mapToDouble(x->Double.parseDouble(x.get("liveVal").toString())).average().getAsDouble();
		double preDaysAnomalyValue = preDays - preDaysPerennialValue;
		presb3.append("1961～"+edYear+"年，宁夏平均年降水日数呈"+ds+"趋势，每10年"+zj+changeValue+"天。");
		presb3.append(""+preDaysMaxYear+"年降水日数为"+preDaysMax+"天，为1961年以来最多，"+preDaysMinYear+"年最少，仅为"+preDaysMin+"天。");
		presb3.append(""+edYear+"年，平均年降水日数为"+preDays+"天，较常年偏多"+Convert.toDouble(NumberUtil.round(preDaysAnomalyValue,1))+"天（图1.2.4）。");
		
		//降水日数柱状图============================================
		preHis.putAll(ClimateChangeReportHistogramService.preDaysHistogram(preDaysList, stYear, edYear));
		//==========================================================
		
		//分布图
		String res1 = DrawUtils.drawImg(picData, "pre1","降水实况分布");
		String res2 = DrawUtils.drawImg(picData, "preAnomaly1","降水距平分布");
		prePic.put("prePicLiv", res1);
		prePic.put("prePicAno", res2);
		
		//语句
		prePra.put("presb1", presb1.toString());
		prePra.put("presb2", presb2.toString());
		prePra.put("presb3", presb3.toString());
		
		result.putAll(prePra);
		result.putAll(preHis);
		result.putAll(prePic);
		
		return result;
	}
	
	//处理天气现象全省年数据
	public Map<String, Object> queryStationYearData(List<Map<String ,Object>> stationYearList,String statistical){
		Map<String, Object> mapResults = new HashMap<String, Object>();
		List<Map<String ,Object>> allYearList = new ArrayList<Map<String,Object>>();//全省年数据
		int startYear = Integer.valueOf(stYear);
		int endYear = Integer.valueOf(edYear);
		int id = 0;
		if(statistical.equals("累计")) {//累计
			for(int i=startYear; i<=endYear; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				id++;
				int year = i;
				double average = Convert.toDouble(NumberUtil.round(filterData(stationYearList, "liveVal").parallelStream().
						filter(x->Integer.valueOf(x.get("year").toString()) == year).
						mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).
						summaryStatistics().getSum(),1));
				map.put("id", id);
				map.put("year", year);
				map.put("liveVal", average);
				allYearList.add(map);
			}
		}else {//平均
			for(int i=startYear; i<=endYear; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				id++;
				int year = i;
				double average = Convert.toDouble(NumberUtil.round(filterData(stationYearList, "liveVal").parallelStream().
						filter(x->Integer.valueOf(x.get("year").toString()) == year).
						mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).
						summaryStatistics().getAverage(),1));
				map.put("id", id);
				map.put("year", year);
				map.put("liveVal", average);
				allYearList.add(map);
			}
		}
		//从大到小排序
		allYearList = allYearList.stream().sorted((a,b)->Double.valueOf(b.get("liveVal").toString()).
				compareTo(Double.valueOf(a.get("liveVal").toString()))).collect(Collectors.toList());
		double changeValue = ClimateHandleDate.calSlopeData(allYearList, "liveVal");
		double perennialValue = Convert.toDouble(NumberUtil.round(allYearList.stream().filter(x->Integer.valueOf(x.get("year").toString())>=1981
				&& Integer.valueOf(x.get("year").toString())<=2010).mapToDouble(x->Double.parseDouble(x.get("liveVal").toString())).
				summaryStatistics().getAverage(),1));
		double liveValue = 0.0d;
		double anomalyValue = 0.0d;
		double historicalMax1 = Convert.toDouble(NumberUtil.round(Double.valueOf(allYearList.get(0).get("liveVal").toString()),1));
		String historicalMaxYear1 = allYearList.get(0).get("year").toString();
		double historicalMax2 = Convert.toDouble(NumberUtil.round(Double.valueOf(allYearList.get(1).get("liveVal").toString()),1));
		String historicalMaxYear2 = allYearList.get(1).get("year").toString();
		double historicalMin = Convert.toDouble(NumberUtil.round(Double.valueOf(allYearList.get(allYearList.size()-1).get("liveVal").toString()),1));
		String historicalMinYear = allYearList.get(allYearList.size()-1).get("year").toString();
		for (int i = 0; i < allYearList.size(); i++) {
			double liveVal = Convert.toDouble(NumberUtil.round(Double.valueOf(allYearList.get(i).get("liveVal").toString()),1));
			if(allYearList.get(i).get("year").toString().equals(edYear)){
				liveValue = liveVal;
				anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
			}
		}
		mapResults.put("liveValue", liveValue);//实况值
		mapResults.put("perennialValue", perennialValue);//常年值
		mapResults.put("anomalyValue", anomalyValue);//距平
		mapResults.put("changeValue", changeValue);//每10年变化
		mapResults.put("historicalMax1", historicalMax1);//历史最大值
		mapResults.put("historicalMaxYear1", historicalMaxYear1);//历史最大值对应时间
		mapResults.put("historicalMax2", historicalMax2);//历史第二大
		mapResults.put("historicalMaxYear2", historicalMaxYear2);//历史第二大对应时间
		mapResults.put("historicalMin", historicalMin);//历史最小值
		mapResults.put("historicalMinYear", historicalMinYear);//历史最小值对应时间
		return mapResults;
	}
	
	//处理站点年数据
	public List<Map<String, Object>> queryStationData(List<Map<String, Object>> yearData, List<Map<String, Object>> stationInfo) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < stationInfo.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String stationNo = stationInfo.get(i).get("stationNo").toString();
			String stationName = stationInfo.get(i).get("stationName").toString();
			String longitude = stationInfo.get(i).get("longitude").toString();
			String latitude = stationInfo.get(i).get("latitude").toString();
			double perennialValue = Convert.toDouble(NumberUtil.round(yearData.parallelStream().filter(x->StringUtils.contains(x.get("stationNo").toString(),stationNo)
					&& Integer.valueOf(x.get("year").toString()) >= 1981 && Integer.valueOf(x.get("year").toString()) <= 2010).
					mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics().getAverage(),1));
			double liveValue = 0d;
			double anomalyValue = 0d;
			for(int j=0; j<yearData.size(); j++) {
				if(StringUtils.contains(yearData.get(j).get("stationNo").toString(),stationNo) && StringUtils.contains(yearData.get(j).get("year").toString(),edYear)) {					
					liveValue =  Convert.toDouble(NumberUtil.round(Double.valueOf(yearData.get(j).get("liveVal").toString()),1));
					anomalyValue = Convert.toDouble(NumberUtil.sub(liveValue, perennialValue, 1));
				}
			}
			map.put("stationNo", stationNo);
			map.put("stationName", stationName);
			map.put("longitude", longitude);
			map.put("latitude", latitude);
			map.put("perennialValue", perennialValue);
			map.put("liveValue", liveValue);
			map.put("ciDayAnomaly", anomalyValue);
			list.add(map);
		}
		return list;
	}
	
	//干旱
	//CI<=-0.6为干旱
	//1.
	//1961～X年，宁夏平均年干旱日数X变化趋势，但年代际特征明显。X年平均年干旱日数最多，为X天，X年最少，仅有X天。X年宁夏平均干旱日数X天，较常年偏少X天（图2.1.1）。
	//2.
	//X年宁夏各地干旱日数较常年偏少X～X天，其中X、X、X等偏少X天以上（图2.1.2）。
	public Map<String, Object> droughtPara(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> droughtHis = new HashMap<String, String>();
		Map<String, String> droughtPra = new HashMap<String, String>();
		Map<String, String> droughtPic = new HashMap<String, String>();
		StringBuilder droughtPara1 = new StringBuilder();
		StringBuilder droughtPara2 = new StringBuilder();
		//数据查询
		List<Map<String, Object>> stationInfo = ClimateChangeReportDao.getStationNo();
		List<Map<String, Object>> droughtDays = ClimateChangeReportDao.getDroughtData(stYear, edYear);
		List<Map<String ,Object>> yearDatas = queryWeaDatas(droughtDays,"干旱");
		//全省年数据
		Map<String, Object> valueMap = queryStationYearData(droughtDays, "平均");
		double liveValue = Double.valueOf(valueMap.get("liveValue").toString());//年平均干旱日数
		double anomalyValue = Double.valueOf(valueMap.get("anomalyValue").toString());//距平
		double changeValue = Double.valueOf(valueMap.get("changeValue").toString());//每10年变化
		double historicalMax1 = Double.valueOf(valueMap.get("historicalMax1").toString());//年最大值
		String historicalMaxYear1 = valueMap.get("historicalMaxYear1").toString();//年最大值对应时间
		double historicalMin = Double.valueOf(valueMap.get("historicalMin").toString());//年最小值
		String historicalMinYear = valueMap.get("historicalMinYear").toString();//年最小值对应时间
		if(changeValue<0) {
			sj = "下降";
		}else {
			sj = "上升";
		}
		if(anomalyValue < 0) {
			ds = "偏少";
			anomalyValue = Math.abs(anomalyValue);
		}else {
			ds = "偏多";
		}
		droughtPara1.append("1961～"+edYear+"年，宁夏平均年干旱日数呈"+sj+"变化趋势，但年代际特征明显。");
		droughtPara1.append(""+historicalMaxYear1+"年平均年干旱日数最多，为"+historicalMax1+"天，"+historicalMinYear+"年最少，仅有"+historicalMin+"天。");
		droughtPara1.append(""+edYear+"年宁夏平均干旱日数"+liveValue+"天，较常年"+ds+anomalyValue+"天（图2.1.1）。");
		
		//站点年数据
		String stationNos1 = ""; 
		double anoMax1 = -999d,anoMin1 = 999d;
		List<Map<String, Object>> stationList = queryStationData(droughtDays, stationInfo);
		DoubleSummaryStatistics summ = stationList.stream().mapToDouble(x->Double.valueOf(x.get("liveValue").toString())).summaryStatistics();
		for(int i=0; i<stationList.size(); i++) {
			double anomalyVal = Double.valueOf(stationList.get(i).get("ciDayAnomaly").toString());
			if(anomalyVal < 0) {
				stationNos1 += stationList.get(i).get("stationName").toString()+"、";
				if(anomalyVal > anoMax1) {
					anoMax1 = Math.abs(anomalyVal);
				}
				if(anomalyVal < anoMin1) {
					anoMin1 = Math.abs(anomalyVal);
				}
			}
		}
		droughtPara2.append(""+edYear+"年宁夏各地干旱日数为"+summ.getMin()+"～"+summ.getMax()+"，");
		droughtPara2.append("其中"+stationNos1.substring(0, stationNos1.length()-1)+"较常年偏少"+anoMin1+"～"+anoMax1+"天（图2.1.2）。");
		droughtPra.put("droughtPara1", droughtPara1.toString());
		droughtPra.put("droughtPara2", droughtPara2.toString());
		
		//干旱日数柱状图==================================
		droughtHis.putAll(ClimateChangeReportHistogramService.droughtDaysHistogram(yearDatas, stYear, edYear));
		//================================================ 
		
		//分布图
		String res = DrawUtils.drawImg(stationList, "ciDayAnomaly","干旱日数距平分布");
		droughtPic.put("droughtPic", res);
		
		result.putAll(droughtHis);
		result.putAll(droughtPra);
		result.putAll(droughtPic);
		
		return result;
	}
	//冰雹沙尘大风年数据
	public List<Map<String, Object>> queryWeaDatas(List<Map<String, Object>> Datas, String type) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		int st = Integer.valueOf(stYear);
		int ed = Integer.valueOf(edYear); 
		double sum = 0.0d;
		int s = 0;
		int id = 0;
		double liveVal = 0.0d;
		for(int i = st; i <= ed; i++) {
			Map<String, Object> mapData = new HashMap<String, Object>();
			id++;
			for (Map<String, Object> map : Datas) {
				if(Integer.valueOf(map.get("year").toString()) == i) {
					sum += Double.valueOf(map.get("liveVal").toString());
					s++;
				}
			}
			if(s>0) {				
				liveVal = NumberUtil.div(sum, s, 1);
			}
			mapData.put("id", id);
			mapData.put("year", i);
			if(type.equals("强降水")) {
				mapData.put("liveVal", sum);
			}else {
				mapData.put("liveVal", liveVal);				
			}
			result.add(mapData);
		}
		return result;
	}
	
	//冰雹
	//1961～X年，宁夏平均冰雹日数呈X趋势，平均每10年减少X天。X年，宁夏平均冰雹日数为X天，较常年偏少X天（图2.2.1）。
	public Map<String, Object> hailPara(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> hailPra = new HashMap<String, String>();
		Map<String, String> hailHis = new HashMap<String, String>();
		StringBuilder hailPara = new StringBuilder();
		//全省各站年数据
		List<Map<String ,Object>> stationYearList = ClimateChangeReportDao.queryStationWeaDatas(stYear, edYear, "冰雹");
		//全省年数据
		List<Map<String ,Object>> yearDatas = queryWeaDatas(stationYearList,"冰雹");
		//结果集
		Map<String, Object> valueMap = queryStationYearData(stationYearList,"平均");
		double liveValue = Double.valueOf(valueMap.get("liveValue").toString());
		double anomalyValue = Double.valueOf(valueMap.get("anomalyValue").toString());
		double changeValue = Double.valueOf(valueMap.get("changeValue").toString());
		if(changeValue < 0) {
			zj = "减少";
			changeValue = Math.abs(changeValue);
		}else{
			zj = "增加";
		}
		if(anomalyValue < 0) {
			ds = "偏少";
			anomalyValue = Math.abs(anomalyValue);
		}else {
			ds = "偏多";
		}
		hailPara.append("1961～"+edYear+"年，宁夏平均冰雹日数呈"+zj+"趋势，平均每10年"+zj+changeValue+"天。");
		hailPara.append(""+edYear+"年，宁夏平均冰雹日数为"+liveValue+"天，较常年偏少"+anomalyValue+"天（图2.2.1）。");
		hailPra.put("hailPara", hailPara.toString());
		
		//冰雹柱状图==========================================
		hailHis.putAll(ClimateChangeReportHistogramService.hailHistogram(yearDatas, stYear, edYear));
		//==================================================
		
		result.putAll(hailHis);
		result.putAll(hailPra);
		
		return result;
	}
	//沙尘
	//1961～X年，宁夏平均年沙尘暴、扬沙、浮尘日数均呈X趋势，每10年分别减少X天、X天、X天。X年，宁夏平均年沙尘暴、扬沙、浮尘日数分别为X天、X天、X天，较常年偏少X天、1X天、X天（图2.3.1 a-c）。
	public Map<String, Object> sandDustPara(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> sandDustPra = new HashMap<String, String>();
		Map<String, String> sandDustHis = new HashMap<String, String>();
		StringBuilder sandDustPara = new StringBuilder();
		String fields[] = {"沙尘暴","扬沙","浮尘"};
		sandDustPara.append("1961～"+edYear+"年");
		int index = 0;
		for(int i=0; i<fields.length; i++) {
			//全省各站年数据
			List<Map<String ,Object>> stationYearList = ClimateChangeReportDao.queryStationWeaDatas(stYear, edYear, fields[i]);
			//全省年数据
			List<Map<String ,Object>> yearDatas = queryWeaDatas(stationYearList,"沙尘");
			//结果集
			Map<String, Object> valueMap = queryStationYearData(stationYearList, "平均");
			double liveValue = Double.valueOf(valueMap.get("liveValue").toString());
			double anomalyValue = Double.valueOf(valueMap.get("anomalyValue").toString());
			double changeValue = Double.valueOf(valueMap.get("changeValue").toString());
			if(changeValue < 0) {
				zj = "减少";
				changeValue = Math.abs(changeValue);
			}else{
				zj = "增加";
			}
			if(anomalyValue < 0) {
				ds = "偏少";
				anomalyValue = Math.abs(anomalyValue);
			}else {
				ds = "偏多";
			}
			sandDustPara.append("，宁夏平均"+fields[i]+"日数呈"+zj+"趋势，每10年"+zj+changeValue+"天。");
			sandDustPara.append(""+edYear+"年，宁夏平均年"+fields[i]+"日数为"+liveValue+"天，较常年"+ds+anomalyValue+"天");
			
			//沙尘柱状图==================
			index++;
			sandDustHis.putAll(ClimateChangeReportHistogramService.sandDustHistogram(yearDatas, stYear, edYear, fields[i], index));
			//============================
			
		}
		sandDustPara.append("（图2.3.1 a-c）。");
		sandDustPra.put("sandDustPara", sandDustPara.toString());
		
		result.putAll(sandDustHis);
		result.putAll(sandDustPra);
		
		return result;
	}
	//大风
	//1961～X年，宁夏平均年大风日数呈X少趋势，平均每10年减少X天。2018年，宁夏平均年大风日数为X天，较常年偏少X天（图2.4.1）。
	public Map<String, Object> galePara(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> galePra = new HashMap<String, String>();
		Map<String, String> galeHis = new HashMap<String, String>();
		StringBuilder galePara = new StringBuilder();
		//全省各站年数据
		List<Map<String ,Object>> stationYearList = ClimateChangeReportDao.queryStationWeaDatas(stYear, edYear, "大风");
		//全省年数据
		List<Map<String ,Object>> yearDatas = queryWeaDatas(stationYearList,"大风");
		//结果集
		Map<String, Object> valueMap = queryStationYearData(stationYearList, "平均");
		double liveValue = Double.valueOf(valueMap.get("liveValue").toString());
		double anomalyValue = Double.valueOf(valueMap.get("anomalyValue").toString());
		double changeValue = Double.valueOf(valueMap.get("changeValue").toString());
		if(changeValue < 0) {
			zj = "减少";
			changeValue = Math.abs(changeValue);
		}else{
			zj = "增加";
		}
		if(anomalyValue < 0) {
			ds = "偏少";
			anomalyValue = Math.abs(anomalyValue);
		}else {
			ds = "偏多";
		}
		galePara.append("1961～"+edYear+"年，宁夏平均年大风日数呈"+zj+"趋势，平均每10年"+zj+changeValue+"天。");
		galePara.append(""+edYear+"年，宁夏平均年大风日数为"+liveValue+"天，较常年偏少"+anomalyValue+"天（图2.4.1）。");
		galePra.put("galePara", galePara.toString());
		
		//沙尘柱状图==================
		galeHis.putAll(ClimateChangeReportHistogramService.galeHistogram(yearDatas, stYear, edYear));
		//============================
		
		result.putAll(galeHis);
		result.putAll(galePra);
		
		return result;
	}
	
	//高温日数
	//1.
	//宁夏高温天气主要出现在中北部地区。1961～X年，中北部年高温日数以每10年X天的速率呈X趋势（图2.5.1）。
	//年代际特征明显，X年以后，高温日数明显增多，X年和X年为1961年以来高温日数最多的两年，分别为X天和X天。
	//2.
	//X年中北部平均高温日数为X天，较常年偏多X天。大部地区偏多X天以上，其中X偏多X天。
	public Map<String, Object> highTemPara(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> highTemPra = new HashMap<String, String>();
		Map<String, String> highTemHis = new HashMap<String, String>();
		StringBuilder highTemPara1 = new StringBuilder();
		StringBuilder highTemPara2 = new StringBuilder();
		String condition1 = "count( 1 ) liveVal";//查询条件
		String condition2 = "TEM_Max >= '35' and TEM_Max <= '999'";
		//查询区域各站数据（中北部地区）
		List<Map<String, Object>> groupList = ClimateChangeReportDao.queryDates(stYear, edYear, condition1, condition2, group2);
		//全省年数据
		List<Map<String ,Object>> yearDatas = queryWeaDatas(groupList,"高温");
		//转换成年数据
		Map<String, Object> valueMap = queryStationYearData(groupList, "平均");
		double liveValue = Double.valueOf(valueMap.get("liveValue").toString());
		double anomalyValue = Double.valueOf(valueMap.get("anomalyValue").toString());
		double changeValue = Double.valueOf(valueMap.get("changeValue").toString());
		double historicalMax1 = Double.valueOf(valueMap.get("historicalMax1").toString());
		String historicalMaxYear1 = valueMap.get("historicalMaxYear1").toString();
		double historicalMax2 = Double.valueOf(valueMap.get("historicalMax2").toString());
		String historicalMaxYear2 = valueMap.get("historicalMaxYear2").toString();
		if(changeValue < 0) {
			zj = "减少";
			changeValue = Math.abs(changeValue);
		}else{
			zj = "增加";
		}
		if(anomalyValue < 0) {
			ds = "偏少";
			anomalyValue = Math.abs(anomalyValue);
		}else {
			ds = "偏多";
		}
		highTemPara1.append("宁夏高温天气主要出现在中北部地区。1961～"+edYear+"年，中北部年高温日数以每10年"+changeValue+"天的速率呈"+zj+"趋势（图2.5.1）。");
		highTemPara1.append("年代际特征明显，1997年以后，高温日数明显增多，");
		highTemPara1.append(""+historicalMaxYear1+"年和"+historicalMaxYear2+"年为1961年以来高温日数最多的两年，分别为"+historicalMax1+"天和"+historicalMax2+"天。");
		highTemPara2.append(""+edYear+"年中北部平均高温日数为"+liveValue+"天，较常年偏多"+anomalyValue+"天。");
		highTemPra.put("highTemPara1", highTemPara1.toString());
		highTemPra.put("highTemPara2", highTemPara2.toString());
		//高温柱状图==================
		highTemHis.putAll(ClimateChangeReportHistogramService.highTemHistogram(yearDatas, stYear, edYear, "高温", "highTemDraw"));
		//============================
		
		result.putAll(highTemHis);
		result.putAll(highTemPra);
		return result;
	}
	
	//低温
	//1961～X年，宁夏低温日数以每10年X天的速率呈显著减少趋势（图2.6.1）。X年以后，低温日数明显减少。X年，宁夏平均低温日数X天，为X年以来的最高值。
	//X年宁夏平均低温日数为X天，较常年偏多近X天；X出现低温日数最多，为X天，X出现低温日数最少，为X天。
	public Map<String, Object> lowTemPara(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> lowTemPra = new HashMap<String, String>();
		Map<String, String> lowTemHis = new HashMap<String, String>();
		StringBuilder lowTemPara = new StringBuilder();
		String condition1 = "count( 1 ) liveVal";//查询条件
		String condition2 = "TEM_Max <= '-15' and TEM_Max >= '-999'";
		//站点数据查询
		List<Map<String, Object>> stationYearList = new ClimateChangeReportDao().queryStationDates(stYear, edYear, condition1, condition2);
		//全省年数据
		List<Map<String ,Object>> yearDatas = queryWeaDatas(stationYearList,"低温");
		//处理数据
		Map<String, Object> valueMap = queryStationYearData(stationYearList, "平均");
		double liveValue = Double.valueOf(valueMap.get("liveValue").toString());
		double anomalyValue = Double.valueOf(valueMap.get("anomalyValue").toString());
		double changeValue = Double.valueOf(valueMap.get("changeValue").toString());
		double historicalMax1 = Double.valueOf(valueMap.get("historicalMax1").toString());
		String historicalMaxYear1 = valueMap.get("historicalMaxYear1").toString();
		double historicalMin = Double.valueOf(valueMap.get("historicalMin").toString());
		String historicalMinYear = valueMap.get("historicalMinYear").toString();
		if(changeValue < 0) {
			zj = "减少";
			changeValue = Math.abs(changeValue);
		}else{
			zj = "增加";
		}
		if(anomalyValue < 0) {
			ds = "偏少";
			anomalyValue = Math.abs(anomalyValue);
		}else {
			ds = "偏多";
		}
		lowTemPara.append("1961～"+edYear+"年，宁夏低温日数以每10年"+changeValue+"天的速率呈"+zj+"趋势（图2.6.1）。");
		lowTemPara.append(""+edYear+"年宁夏平均低温日数为"+liveValue+"天，较常年"+ds+anomalyValue+"天；");
		lowTemPara.append(""+historicalMaxYear1+"出现低温日数最多，为"+historicalMax1+"天，"+historicalMinYear+"出现低温日数最少，为"+historicalMin+"天。");
		lowTemPra.put("lowTemPara", lowTemPara.toString());
		//低温柱状图==================
		lowTemHis.putAll(ClimateChangeReportHistogramService.highTemHistogram(yearDatas, stYear, edYear, "低温", "lowTemDraw"));
		//============================
		result.putAll(lowTemHis);
		result.putAll(lowTemPra);
		return result;
	}
	
	//强降水
	//1.
	//1961～X年宁夏强降水日数呈X变化趋势（图2.7.1）。X年各地均出现了X～X天强降水天气，累计强降水日数X站日，为1961年以来最多；而X年仅X个站出现X-X天强降水天气，累计强降水日数X站日，为近X年最少。
	//X年X个站出现了X～X天强降水天气，累计强降水为X站日，为1961年以来的第X多值，较常年偏多X站日。
	//2.
	//1961～X年，宁夏年累计暴雨站日也呈下降趋势，每10年减少X站日。X年，宁夏暴雨日数X站日，较常年偏多X站日。
	public Map<String, Object> heavyPre(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> heavyPra = new HashMap<String, String>();
		Map<String, String> heavyHis = new HashMap<String, String>();
		StringBuilder heavyPrePara = new StringBuilder();
		StringBuilder rainStormPara = new StringBuilder();
		String condition1 = "count( 1 ) liveVal";//查询条件
		String condition2 = "PRE_Time_2020  >= '25' and PRE_Time_2020 <='999'";//强降水
		String condition3 = "PRE_Time_2020  >= '50' and PRE_Time_2020 <='999'";//暴雨
		//强降水数据查询
		List<Map<String, Object>> heavyPreStationYearList = ClimateChangeReportDao.queryStationDates(stYear, edYear, condition1, condition2);
		//全省年数据
		List<Map<String ,Object>> heavyPreYearDatas = queryWeaDatas(heavyPreStationYearList,"强降水");
		Map<String, Object> hPMap = queryStationYearData(heavyPreStationYearList, "累计");
		double hPliveValue = Double.valueOf(hPMap.get("liveValue").toString());
		double hPchangeValue = Double.valueOf(hPMap.get("changeValue").toString());
		double historicalMax1 = Double.valueOf(hPMap.get("historicalMax1").toString());
		String historicalMaxYear1 = hPMap.get("historicalMaxYear1").toString();
		double historicalMin = Double.valueOf(hPMap.get("historicalMin").toString());
		String historicalMinYear = hPMap.get("historicalMinYear").toString();
		if(hPchangeValue < 0) {
			zj = "减少";
		}else{
			zj = "增加";
		}
		DoubleSummaryStatistics summary1 = heavyPreStationYearList.parallelStream().filter(x->x.get("year").toString().
				equals(historicalMaxYear1)).mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics();
		DoubleSummaryStatistics summary2 = heavyPreStationYearList.parallelStream().filter(x->x.get("year").toString().
				equals(historicalMinYear)).mapToDouble(x->Double.valueOf(x.get("liveVal").toString())).summaryStatistics();
		heavyPrePara.append("1961～"+edYear+"年宁夏强降水日数呈"+zj+"变化趋势（图2.7.1）。");
		heavyPrePara.append(""+historicalMaxYear1+"年各地均出现了"+summary1.getMin()+"～"+summary1.getMax()+"天强降水天气，累计强降水日数"+historicalMax1+"站日，为1961年以来最多；");
		heavyPrePara.append("而"+historicalMinYear+"年各地均出现了"+summary2.getMin()+"～"+summary2.getMax()+"天强降水天气，累计强降水日数"+historicalMin+"站日，为1961年以来最少。");
		//暴雨数据查询
		List<Map<String, Object>> rainStormStationYearList = ClimateChangeReportDao.queryStationDates(stYear, edYear, condition1, condition3);
		//全省年数据
		List<Map<String ,Object>> rainStormYearDatas = queryWeaDatas(rainStormStationYearList,"强降水");
		Map<String, Object> rSMap = queryStationYearData(rainStormStationYearList, "累计");
		double rSliveValue = Double.valueOf(rSMap.get("liveValue").toString());
		double rSanomalyValue = Double.valueOf(rSMap.get("anomalyValue").toString());
		double rSchangeValue = Double.valueOf(rSMap.get("changeValue").toString());
		if(rSchangeValue < 0) {
			zj = "减少";
			rSchangeValue = Math.abs(rSchangeValue);
		}else{
			zj = "增加";
		}
		if(rSanomalyValue < 0) {
			ds = "偏少";
			rSanomalyValue = Math.abs(rSanomalyValue);
		}else {
			ds = "偏多";
		}
		rainStormPara.append("1961～"+edYear+"年，宁夏年累计暴雨站日也呈"+zj+"趋势，每10年"+zj+rSchangeValue+"站日。");
		rainStormPara.append(""+edYear+"年，宁夏暴雨日数"+rSliveValue+"站日，较常年"+ds+rSanomalyValue+"站日。");
		heavyPra.put("heavyPrePara", heavyPrePara.toString());
		heavyPra.put("rainStormPara", rainStormPara.toString());
		//强降水柱状图==================
		heavyHis.putAll(ClimateChangeReportHistogramService.highTemHistogram(heavyPreYearDatas, stYear, edYear, "强降水", "heavyPreDraw"));
		//暴雨柱状图==================
		heavyHis.putAll(ClimateChangeReportHistogramService.rainStormHistogram(rainStormYearDatas, stYear, edYear));
		//============================
		result.putAll(heavyPra);
		result.putAll(heavyHis);
		return result;
	}
	/**
	 * 过滤掉异常数据
	 */
	public List<Map<String, Object>> filterData(List<Map<String, Object>> data,String filed) {
		return data.stream().filter(x->!Objects.isNull(x.get(filed))
				&& Double.parseDouble(x.get(filed).toString())> -999
				&& Double.parseDouble(x.get(filed).toString())< 999).collect(Collectors.toList());
	}
}
