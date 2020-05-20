package com.dafang.monitor.nx.product.impl.evaluate;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.entity.SeasonEnum;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.DrawUtils;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import com.dafang.monitor.nx.utils.jfreechart.JFreeChartUtil;
import org.apache.commons.lang3.StringUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeasonClimateImpact extends TemplateAbstract {

    @Autowired
    ProductMapper mapper;
    String[] elements = {"TEM_Avg","PRE_Time_2020","SSH"};


    @Override
    protected void init(ProductParams params) {
        year = params.getYear();
        season = params.getSeason();
        templateName = "季气候影响评价.ftl";
        fileName = year + "年" + season + ProductEmun.getFileName(8);
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        //获得季节内月份时间段
        List<String> slots = SeasonEnum.getSlotBySeason(season);
        return contentData(handleDate(params,slots),slots);
    }

    //数据处理
    public Map<String,Object> handleDate(ProductParams params,List<String> slots){
        List<Map<String,Object>> RegionList = new ArrayList<>();
        List<Map<String,Object>> SingleList = new ArrayList<>();
        List<Map<String,Object>> HistogramList = new ArrayList<>();//柱状图
        Map<String,Object> result = new HashMap<>();
        //全区站点
        List<String> stas = RegionStaEnum.getStas("1");
        String stationNos = String.join(",", stas);
        for (String element : elements) {
            params.setElement(element);
            params.setCal("AVG");
            if(element.contains("PRE") && element.contains("SSH")){
                params.setCal("SUM");
            }
            for (String slot : slots) {//时间段（春季/3月/4月/5月）
                Map<String,Object> QXMap = new HashMap<>();
                String dateName = slot.split("_")[2];
                params.setST(slot.split("_")[0]);
                params.setET(slot.split("_")[1]);
                baseData = mapper.periodsList(params);
                currentList = baseData.stream().filter(x-> StringUtils.equals(x.getYear().toString(),year)).collect(Collectors.toList());
                perenList = baseData.stream().filter(x-> Convert.toDouble(x.getYear()) >= 1981 && Convert.toDouble(x.getYear()) <= 2020).collect(Collectors.toList());
                //全区数据处理
                List<Product> QXCurrentList = currentList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo())).collect(Collectors.toList());
                double QXliveVal = ReflectHandleUtils.getValByOp(QXCurrentList, "val", "avg");
                List<Product> QXPerenList = perenList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo())).collect(Collectors.toList());
                double QXperenVal = ReflectHandleUtils.getValByOp(QXPerenList, "val", "avg");
                double QXannomlyVal = QXliveVal - QXperenVal;
                if (element.contains("PRE"))QXannomlyVal = QXannomlyVal/QXperenVal*100;
                QXMap.put("stationName","全区");
                QXMap.put("dateName", dateName);
                QXMap.put(element+"QXliveVal", NumberUtil.round(QXliveVal,1));
                QXMap.put(element+"QXperenVal",NumberUtil.round(QXperenVal,1));
                QXMap.put(element+"QXannomlyVal",NumberUtil.round(QXannomlyVal,1));
                RegionList.add(QXMap);
                //柱状图数据
                if(StringUtils.contains(dateName,"季") && (element.contains("PRE") || element.contains("TEM"))){
                    for (int i = 1961; i <= Convert.toDouble(year); i++){
                        Map<String,Object> hisMap = new HashMap<>();
                        String years = i+"";
                        List<Product> yearList = baseData.stream().filter(x -> StringUtils.equals(x.getYear().toString(), years)).collect(Collectors.toList());
                        List<Product> QXList = yearList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo())).collect(Collectors.toList());
                        QXliveVal = ReflectHandleUtils.getValByOp(QXList, "val", "avg");
                        QXannomlyVal = QXliveVal - QXperenVal;
                        if (element.contains("PRE"))QXannomlyVal = QXannomlyVal/QXperenVal*100;
                        hisMap.put("year", years);
                        hisMap.put(element+"QXannomlyVal",NumberUtil.round(QXannomlyVal,1));
                        HistogramList.add(hisMap);
                    }
                }
                //单站数据处理
                for (String sta : stas) {
                    Map<String,Object> SingleMap = new HashMap<>();
                    Product product = currentList.stream().filter(x -> StringUtils.equals(sta, x.getStationNo())).collect(Collectors.toList()).get(0);
                    List<Product> SingleCurrentList = currentList.stream().filter(x -> StringUtils.equals(sta, x.getStationNo())).collect(Collectors.toList());
                    double liveVal = ReflectHandleUtils.getValByOp(SingleCurrentList, "val", "avg");
                    List<Product> SinglePerenList = perenList.stream().filter(x -> StringUtils.equals(sta, x.getStationNo())).collect(Collectors.toList());
                    double perenVal = ReflectHandleUtils.getValByOp(SinglePerenList, "val", "avg");
                    double annomlyVal = liveVal - perenVal;
                    if (element.contains("PRE"))annomlyVal = annomlyVal/perenVal*100;
                    SingleMap.put("stationNo",sta);
                    SingleMap.put("dateName", dateName);
                    SingleMap.put("stationName",product.getStationName());
                    SingleMap.put("longitude",product.getLongitude());
                    SingleMap.put("latitude",product.getLatitude());
                    SingleMap.put(element+"liveVal",NumberUtil.round(liveVal,1));
                    SingleMap.put(element+"perenVal",NumberUtil.round(perenVal,1));
                    SingleMap.put(element+"annomlyVal",NumberUtil.round(annomlyVal,1));
                    SingleList.add(SingleMap);
                }
            }
        }
        List<Map<String,Object>> Regions = new ArrayList<>();
        List<Map<String,Object>> Singles = new ArrayList<>();
        List<Map<String,Object>> Histogram = new ArrayList<>();
        //区域数据整合
        for (Map<String, Object> map1 : RegionList) {
            for (Map<String, Object> map2 : RegionList) {
                if (StringUtils.equals(map1.get("stationName").toString(),map2.get("stationName").toString()) && StringUtils.equals(map1.get("dateName").toString(),map2.get("dateName").toString())){
                    map1.putAll(map2);
                    Regions.add(map1);
                }
            }
        }
        //站点数据整合
        for (Map<String, Object> map1 : SingleList) {
            for (Map<String, Object> map2 : SingleList) {
                if (StringUtils.equals(map1.get("stationName").toString(),map2.get("stationName").toString()) && StringUtils.equals(map1.get("dateName").toString(),map2.get("dateName").toString())){
                    map1.putAll(map2);
                    Singles.add(map1);
                }
            }
        }
        //柱状图
        for (Map<String, Object> his1 : HistogramList) {
            for (Map<String, Object> his2 : HistogramList) {
                if(StringUtils.equals(his1.get("year").toString(),his2.get("year").toString())){
                    his1.putAll(his2);
                    Histogram.add(his1);
                }
            }
        }
        Regions = Regions.stream().distinct().collect(Collectors.toList());
        Singles = Singles.stream().distinct().collect(Collectors.toList());
        Histogram = Histogram.stream().distinct().collect(Collectors.toList());
        result.put("region",Regions);//区域数据
        result.put("single",Singles);//站点数据，绘图数据
        result.put("histogram",Histogram);//柱状图数据
        return result;
    }

    //内容拼接
    public Map<String,Object> contentData(Map<String,Object> map,List<String> slots){
        Map<String,Object> result = new HashMap<>();
        List<Map<String,Object>> regionList = (List<Map<String,Object>>)map.get("region");
        List<Map<String,Object>> singleList = (List<Map<String,Object>>)map.get("single");
        List<Map<String,Object>> histogram = (List<Map<String,Object>>)map.get("histogram");
        //====================文字=====================
        String str1 = "%s全区平均气温%s℃，较常年同期偏%s℃，全区平均降水量%smm，较常年同期偏%s%%，全区平均日照时数%s小时，较常年同期偏%s小时。";
        String str2 = "%s全区平均气温%s℃，较常年同期偏%s℃，各地气温%s℃（图1），偏%s℃～偏%s℃。";
        String str3 = "%s全区平均气温%s℃，较常年同期偏%s℃，各地气温%s℃，偏%s℃～偏%s℃。" +
                "%s全区平均气温%s℃，较常年同期偏%s℃，各地气温%s℃，偏%s℃～偏%s℃。" +
                "%s全区平均气温%s℃，较常年同期偏%s℃，各地气温%s℃，偏%s℃～偏%s℃。";
        String str4 = "全区平均降水量%smm，较常年同期偏%s%%，各地降水量%smm，偏%s%%～偏%s%%（图4、图5）。";
        String str5 = "%s全区平均降水量%smm，较常年同期偏%s%%，各地降水量%smm，偏%s%%～偏%s%%。" +
                "%s全区平均降水量%smm，较常年同期偏%s%%，各地降水量%smm，偏%s%%～偏%s%%。" +
                "%s全区平均降水量%smm，较常年同期偏%s%%，各地降水量%smm，偏%s%%～偏%s%%。";
        String str6 = "全区平均日照时数%s小时，较常年同期偏%s小时。各地日照时数%s小时（图7），偏%s小时～偏%s小时（图8）。";
        String str7 = "%s全区平均日照时数%s小时，较常年同期偏%s小时，各地日照时数%s小时，偏%s小时～偏%s小时。" +
                "%s全区平均日照时数%s小时，较常年同期偏%s小时，各地日照时数%s小时，偏%s小时～偏%s小时。" +
                "%s全区平均日照时数%s小时，较常年同期偏%s小时，各地日照时数%s小时，偏%s小时～偏%s小时。";
        String s1 = year + "年" + season;
        String s2 = "",s3 = "",s4 = "",s5 = "",s6 = "",s7 = "";
        String t2 = "",t3 = "",t4 = "",t5 = "",t6 = "",t7 = "",t8 = "",t9 = "",t10 = "";
        String dateName = "";//时间名
        Map<String,Object> dataMap = new HashMap<>();
        //全区
        for (Map<String, Object> region : regionList) {
            for (String date : slots) {
                dateName = date.split("_")[2];
                if (StringUtils.equals(region.get("dateName").toString(),dateName)){
                    s2 = region.get("TEM_AvgQXliveVal").toString();
                    s3 = (Convert.toDouble(region.get("TEM_AvgQXannomlyVal").toString()) > 0 ? "高" : "低") + Math.abs(Convert.toDouble(region.get("TEM_AvgQXannomlyVal").toString()));
                    s4 = region.get("PRE_Time_2020QXliveVal").toString();
                    s5 = (Convert.toDouble(region.get("PRE_Time_2020QXannomlyVal").toString()) > 0 ? "多" : "少") + Math.abs(Convert.toDouble(region.get("PRE_Time_2020QXannomlyVal").toString()));
                    s6 = region.get("SSHQXliveVal").toString();
                    s7 = (Convert.toDouble(region.get("SSHQXannomlyVal").toString()) > 0 ? "多" : "少") + Math.abs(Convert.toDouble(region.get("SSHQXannomlyVal").toString()));
                    dataMap.put(dateName+"s2",s2);//气温实况
                    dataMap.put(dateName+"s3",s3);//气温距平
                    dataMap.put(dateName+"s4",s4);//降水实况
                    dataMap.put(dateName+"s5",s5);//降水距平
                    dataMap.put(dateName+"s6",s6);//日照实况
                    dataMap.put(dateName+"s7",s7);//日照距平
                }
            }
        }
        //各地
        for (String date : slots) {
            String dateName2 = date.split("_")[2];
            DoubleSummaryStatistics summary1 = singleList.stream().filter(x -> StringUtils.equals(x.get("dateName").toString(), dateName2)).
                    mapToDouble(x -> Convert.toDouble(x.get("TEM_AvgliveVal").toString())).summaryStatistics();
            DoubleSummaryStatistics summary2 = singleList.stream().filter(x -> StringUtils.equals(x.get("dateName").toString(), dateName2)).
                    mapToDouble(x -> Convert.toDouble(x.get("TEM_AvgannomlyVal").toString())).summaryStatistics();
            DoubleSummaryStatistics summary3 = singleList.stream().filter(x -> StringUtils.equals(x.get("dateName").toString(), dateName2)).
                    mapToDouble(x -> Convert.toDouble(x.get("PRE_Time_2020liveVal").toString())).summaryStatistics();
            DoubleSummaryStatistics summary4 = singleList.stream().filter(x -> StringUtils.equals(x.get("dateName").toString(), dateName2)).
                    mapToDouble(x -> Convert.toDouble(x.get("PRE_Time_2020annomlyVal").toString())).summaryStatistics();
            DoubleSummaryStatistics summary5 = singleList.stream().filter(x -> StringUtils.equals(x.get("dateName").toString(), dateName2)).
                    mapToDouble(x -> Convert.toDouble(x.get("SSHliveVal").toString())).summaryStatistics();
            DoubleSummaryStatistics summary6 = singleList.stream().filter(x -> StringUtils.equals(x.get("dateName").toString(), dateName2)).
                    mapToDouble(x -> Convert.toDouble(x.get("SSHannomlyVal").toString())).summaryStatistics();
            t2 = summary1.getMin() + "～" + summary1.getMax();
            t3 = (summary2.getMin() >= 0 ? "高" : "低") +Math.abs(summary2.getMin());
            t4 = (summary2.getMax() > 0 ? "高" : "低") +Math.abs(summary2.getMax());
            t5 = summary3.getMin() + "～" + summary3.getMax();
            t6 = (summary4.getMin() >= 0 ? "多" : "少") +Math.abs(summary4.getMin());
            t7 = (summary4.getMax() > 0 ? "多" : "少") +Math.abs(summary4.getMax());
            t8 = summary5.getMin() + "～" + summary5.getMax();
            t9 = (summary6.getMin() >= 0 ? "多" : "少") +Math.abs(summary6.getMin());
            t10 = (summary6.getMax() > 0 ? "多" : "少") +Math.abs(summary6.getMax());
            dataMap.put(dateName2+"t2",t2);//各地气温xx~xx
            dataMap.put(dateName2+"t3",t3);//气温距平min
            dataMap.put(dateName2+"t4",t4);//气温距平max
            dataMap.put(dateName2+"t5",t5);//各地降水xx~xx
            dataMap.put(dateName2+"t6",t6);//降水距平min
            dataMap.put(dateName2+"t7",t7);//降水距平max
            dataMap.put(dateName2+"t8",t8);//各地日照xx~xx
            dataMap.put(dateName2+"t9",t9);//日照距平min
            dataMap.put(dateName2+"t10",t10);//日照距平max
        }
        List<String> s = SeasonEnum.getMonthBySeason(season);
        String month1 = s.get(0), month2 = s.get(1), month3 = s.get(2);
        str1 = String.format(str1,s1,dataMap.get(season+"s2"),dataMap.get(season+"s3"),dataMap.get(season+"s4"),dataMap.get(season+"s5"),dataMap.get(season+"s6"),dataMap.get(season+"s7"));
        str2 = String.format(str2,s1,dataMap.get(season+"s2"),dataMap.get(season+"s3"),dataMap.get(season+"t2"),dataMap.get(season+"t3"),dataMap.get(season+"t4"));
        str3 = String.format(str3,month1,dataMap.get(month1+"s2"),dataMap.get(month1+"s3"),dataMap.get(month1+"t2"),dataMap.get(month1+"t3"),dataMap.get(month1+"t4"),month2,dataMap.get(month2+"s2"),dataMap.get(month2+"s3"),
                dataMap.get(month2+"t2"),dataMap.get(month2+"t3"),dataMap.get(month2+"t4"),month3,dataMap.get(month3+"s2"),dataMap.get(month3+"s3"),dataMap.get(month3+"t2"),dataMap.get(month3+"t3"),dataMap.get(month3+"t4"));
        str4 = String.format(str4,dataMap.get(season+"s4"),dataMap.get(season+"s5"),dataMap.get(season+"t5"),dataMap.get(season+"t6"),dataMap.get(season+"t7"));
        str5 = String.format(str5,month1,dataMap.get(month1+"s4"),dataMap.get(month1+"s5"),dataMap.get(month1+"t5"),dataMap.get(month1+"t6"),dataMap.get(month1+"t7"),month2,dataMap.get(month2+"s4"),dataMap.get(month2+"s5"),
                dataMap.get(month2+"t5"),dataMap.get(month2+"t6"),dataMap.get(month2+"t7"),month3,dataMap.get(month3+"s4"),dataMap.get(month3+"s5"),dataMap.get(month3+"t5"),dataMap.get(month3+"t6"),dataMap.get(month3+"t7"));
        str6 = String.format(str6,dataMap.get(season+"s6"),dataMap.get(season+"s7"),dataMap.get(season+"t8"),dataMap.get(season+"t9"),dataMap.get(season+"t10"));
        str7 = String.format(str7,month1,dataMap.get(month1+"s6"),dataMap.get(month1+"s7"),dataMap.get(month1+"t8"),dataMap.get(month1+"t9"),dataMap.get(month1+"t10"),month2,dataMap.get(month2+"s6"),dataMap.get(month2+"s7"),
                dataMap.get(month2+"t8"),dataMap.get(month2+"t9"),dataMap.get(month2+"t10"),month3,dataMap.get(month3+"s6"),dataMap.get(month3+"s7"),dataMap.get(month3+"t8"),dataMap.get(month3+"t9"),dataMap.get(month3+"t10"));
        result.put("climateDesc",str1);
        result.put("tem1",str2);
        result.put("tem2",str3);
        result.put("pre1",str4);
        result.put("pre2",str5);
        result.put("ssh1",str6);
        result.put("ssh2",str7);
        //=================分布图===============
        String temPic1 = DrawUtils.drawImg(singleList,"TEM_AvgliveVal",startData+"平均气温分布图");
        String temPic2 = DrawUtils.drawImg(singleList,"TEM_AvgannomlyVal",startData+"平均气温距平分布图");
        String prePic1 = DrawUtils.drawImg(singleList,"PRE_Time_2020liveVal",startData+"降水分布图");
        String prePic2 = DrawUtils.drawImg(singleList,"PRE_Time_2020annomlyVal",startData+"降水距平百分率分布图");
        String sshPic1 = DrawUtils.drawImg(singleList,"PRE_Time_2020annomlyVal",startData+"日照时数分布图");
        String sshPic2 = DrawUtils.drawImg(singleList,"PRE_Time_2020annomlyVal",startData+"日照时数距平分布图");
        result.put("temPic1",temPic1);
        result.put("temPic2",temPic2);
        result.put("prePic1",prePic1);
        result.put("prePic2",prePic2);
        result.put("sshPic1",sshPic1);
        result.put("sshPic2",sshPic2);
        //================柱状图================
        DefaultCategoryDataset temLinedataset = new DefaultCategoryDataset();
        DefaultCategoryDataset preLinedataset = new DefaultCategoryDataset();
        String series1 = "气温距平";
        String series2 = "降水距平百分率";
        for (Map<String, Object> his : histogram) {
            temLinedataset.addValue(Convert.toDouble(his.get("TEM_AvgQXannomlyVal")),series1,his.get("year").toString());
            preLinedataset.addValue(Convert.toDouble(his.get("PRE_Time_2020QXannomlyVal")),series2,his.get("year").toString());
        }
        String temHis = JFreeChartUtil.createChart(year+"年"+season+series1, "", "", "气温距平", temLinedataset);
        String preHis = JFreeChartUtil.createChart(year+"年"+season+series2,"","","降水距平百分率",preLinedataset);
        result.put("temHis",temHis);
        result.put("preHis",preHis);
        String y = year;
        if (season.equals("冬季")){
            y = year + "/" + (Convert.toInt(year) + 1);
        }
        result.put("year",y);
        result.put("season",season);
        return result;
    }

}
