package com.dafang.monitor.nx.product.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExtremWeatherEvent extends TemplateAbstract {

    @Autowired
    private ProductMapper mapper;
    String[] elements = {"TEM_Avg","PRE_Time_2020"};
    List<String> QXstas = RegionStaEnum.getStas("1");//全区站点

    @Override
    protected void init(ProductParams params) {
        startData = params.getStartDate();
        year = startData.substring(0,4);
        month = startData.substring(4,6);
        fileName = year+"年"+month+"月"+ ProductEmun.getFileName(4);
        templateName = "极端天气气候事件监测报告.ftl";
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        Map<String, Object> result = new HashMap<>();
        //获取数据
        Map<String, Object> map = handleData(params);
        //拼接内容
        String str1 = "%s,全区平均气温为%s℃,较常年同期偏%s %s℃;各地平均气温为%s°C %s%s；高温日数为 %s 天。";
        String str2 = "%s,全区平均降水量为 %smm,较常年同期偏%s %s%%，各地平均降水为%smm %s%s；暴雨日数为 %s 天。";
        String s1 = year+"年"+month+"月";
        String s2 = map.get("TEM_AvgQXLiveVal").toString();
        String s3 = Convert.toDouble(map.get("TEM_AvgQXAnnomlyVal")) > 0 ? "高" : "低";
        String s4 = Math.abs(Convert.toDouble(map.get("TEM_AvgQXAnnomlyVal").toString()))+"";
        String s5 = map.get("TEM_Avgmin").toString()+"～"+map.get("TEM_Avgmax").toString();
        String s6 = "";
        if (!map.get("TEM_Avgstas").equals("")){
            s6 = "，"+map.get("TEM_Avgstas").toString()+"较常年同期偏高"+map.get("TEM_AvgstaAnnMin").toString()+"～"+map.get("TEM_AvgstaAnnMax").toString()+"°C，";
            if(!map.get("TEM_Avgstas").toString().contains("、")){
                s6 = ", "+map.get("TEM_Avgstas").toString()+"较常年同期偏高"+map.get("TEM_AvgstaAnnMin").toString()+"°C,";
            }
        }
        String s7 = map.get("TEM_AvgrankStr").toString();
        String s8 = map.get("TEM_Avgcount").toString();
        String t2 = map.get("PRE_Time_2020QXLiveVal").toString();
        String t3 = Convert.toDouble(map.get("PRE_Time_2020QXAnnomlyVal")) > 0 ? "高" : "低";
        String t4 = map.get("PRE_Time_2020QXAnnomlyVal").toString();
        String t5 = map.get("PRE_Time_2020min").toString()+"～"+map.get("PRE_Time_2020max").toString();
        String t6 = "";
        if (!map.get("PRE_Time_2020stas").equals("")){
            t6 = map.get("PRE_Time_2020stas").toString()+"较常年同期偏多"+map.get("PRE_Time_2020staAnnMin").toString()+"～"+map.get("PRE_Time_2020staAnnMax").toString()+"mm，";
            if(!map.get("PRE_Time_2020stas").toString().contains("、")){
                t6 = map.get("PRE_Time_2020stas").toString()+"较常年同期偏多"+map.get("PRE_Time_2020staAnnMin").toString()+"mm,";
            }
        }
        String t7 = map.get("PRE_Time_2020rankStr").toString();
        String t8 = map.get("PRE_Time_2020count").toString();
        str1 = String.format(str1,s1,s2,s3,s4,s5,s6,s7,s8);
        str2 = String.format(str2,s1,t2,t3,t4,t5,t6,t7,t8);
        result.put("year",year);
        result.put("month",month);
        result.put("TemDesc",str1);
        result.put("preDesc",str2);
        return result;
    }

    /*
    处理内容拼接数据
     */
    public Map<String,Object> handleData(ProductParams params){
        Map<String,Object> result = new HashMap<>();
        for (String element : elements) {
            params.setElement(element);//查询指定要素
            params.setCal("AVG");
            if(element.contains("PRE")){
                params.setCal("SUM");
            }
            //基础数据
            baseData = mapper.periodsList(params);
            //当年数据
            currentList = baseData.stream().filter(x -> StringUtils.equals(x.getYear().toString(), year)).collect(Collectors.toList());
            //常年值数据
            perenList = baseData.stream().filter(x->Convert.toDouble(x.getYear()) >= 1981 && Convert.toDouble(x.getYear()) <= 2010).collect(Collectors.toList());
            //===================全区数据=========================
            String QXstationNos = String.join("、", QXstas);
            List<Product> QXCurrentList = currentList.stream().filter(x -> StringUtils.contains(QXstationNos, x.getStationNo())).collect(Collectors.toList());
            List<Product> QXPerenList = perenList.stream().filter(x -> StringUtils.contains(QXstationNos, x.getStationNo())).collect(Collectors.toList());
            double QXLiveVal = ReflectHandleUtils.getValByOp(QXCurrentList, "val", "avg");
            double QXPerenVal = ReflectHandleUtils.getValByOp(QXPerenList, "val", "avg");
            double QXAnnomlyVal = QXLiveVal - QXPerenVal;
            //===================全省各站点数据====================
            DoubleSummaryStatistics summary = currentList.stream().mapToDouble(x -> Convert.toDouble(x.getVal())).summaryStatistics();
            double max = summary.getMax();
            double min = summary.getMin();
            List<String> stationNos = currentList.stream().map(Product::getStationNo).distinct().collect(Collectors.toList());
            StringBuilder rankStr = new StringBuilder();//各站排位
            double staAnnMax = -999;//各站最大值
            double staAnnMin = 999;//各站最小值
            StringBuilder stas = new StringBuilder();//较常年偏高的站点
            for (String stationNo : stationNos) {
                Product product = currentList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList()).get(0);
                Double liveVal = product.getVal();
                String stationName = product.getStationName();
                List<Product> singlePerenList = perenList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
                double perenVal = ReflectHandleUtils.getValByOp(singlePerenList, "val", "avg");
                double annomlyVal = liveVal - perenVal;
                if(annomlyVal > 0){
                    stas.append(stationName).append("、");
                    if(staAnnMax < annomlyVal){
                        staAnnMax = annomlyVal;
                    }
                    if(staAnnMin > annomlyVal){
                        staAnnMin = annomlyVal;
                    }
                }
                int rank = 0;
                List<Double> liveList = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).map(Product::getVal).collect(Collectors.toList());
                //历史排位
                for (Double liveValue : liveList) {
                    if(liveVal < liveValue){
                        rank++;
                    }
                }
                if(rank != 0 && rank <= 5){
                    rankStr.append(stationName).append("为历史同期第").append(rank).append("位，");
                }
            }
            long count;
            if (StringUtils.contains(element,"TEM")){
                //高温日数
                count = currentList.stream().filter(x -> Convert.toDouble(x.getVal()) > 35).count();
            }else{
                //暴雨日数
                count = currentList.stream().filter(x -> Convert.toDouble(x.getVal()) > 50).count();
            }
            result.put(element+"QXLiveVal", NumberUtil.round(QXLiveVal,1));//全区实况值
            result.put(element+"QXAnnomlyVal",NumberUtil.round(QXAnnomlyVal,1));//全区常年值
            result.put(element+"max",NumberUtil.round(max,1));//各地最大值
            result.put(element+"min",NumberUtil.round(min,1));//各地最小值
            result.put(element+"stas", stas.substring(0,stas.length()-1));//较常年偏高的站点
            result.put(element+"staAnnMax",NumberUtil.round(staAnnMax,1));//各地距平最大值
            result.put(element+"staAnnMin",NumberUtil.round(staAnnMin,1));//各地距平最小值
            String rank = rankStr.toString();
            if(!rank.equals("")){
                rank = rank.substring(0,rank.length()-1);
            }
            result.put(element+"rankStr", rank);//历史排位前5的站
            result.put(element+"count",count);//暴雨、高温日数
        }
        return result;
    }

}
