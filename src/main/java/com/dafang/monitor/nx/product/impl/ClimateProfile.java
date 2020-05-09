package com.dafang.monitor.nx.product.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.DrawUtils;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ClientInfoStatus;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 气候概况
 */
@Service
public class ClimateProfile extends TemplateAbstract {

    @Autowired
    private ProductMapper mapper;
    String[] elements = {"TEM_Avg","PRE_Time_2020"};

    @Override
    protected void init(ProductParams params) {
        startData = params.getStartDate().substring(0,4)+"年"+params.getStartDate().substring(4,6)+"月"+params.getStartDate().substring(6,8)+"日";
        endData = params.getEndDate().substring(0,4)+"年"+params.getEndDate().substring(4,6)+"月"+params.getEndDate().substring(6,8)+"日";
        year = params.getStartDate().substring(0,4);
        fileName = startData+"-"+endData+ ProductEmun.getFileName(1);
        templateName = "气候概况.ftl";
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        //得到区域和单站的数据集合
        Map<String, Object> map = handleData(params);
        //处理得到内容和表格的集合
        Map<String, Object> result = contentData(map);
        return result;
    }
    //得到区域数据集合和站点数据集合
    public Map<String,Object> handleData(ProductParams params){
        Map<String, Object> result = new HashMap<>();
        List<Map<String,Object>> regionList = new ArrayList<>();
        List<Map<String,Object>> singleList = new ArrayList<>();
        List<Map<String,Object>> regionLists = new ArrayList<>();
        List<Map<String,Object>> singleLists = new ArrayList<>();
        for (String element : elements) {
            params.setElement(element);
            if(StringUtils.contains(element,"PRE")){
                params.setCal("SUM");
            }else{
                params.setCal("AVG");
            }
            //基础数据
            baseData = mapper.periodsList(params);
            //当年数据
            currentList = baseData.stream().filter(x-> StringUtils.equals(x.getYear().toString(),year)).collect(Collectors.toList());
            //常年数据
            perenList = baseData.stream().filter(x->x.getYear() >= 1981 && x.getYear() <= 2010).collect(Collectors.toList());
            //站数
            List<String> stationNos = baseData.stream().map(Product::getStationNo).distinct().collect(Collectors.toList());
            //=========================区域=====================
            for (RegionStaEnum value : RegionStaEnum.values()) {
                Map<String,Object> map1 = new HashMap<>();
                String stas = value.getStas();
                String desc = value.getDesc();
                String regionId = value.getRegionId();
                //获得某区域的当年数据
                List<Product> collect = currentList.stream().filter(x -> StringUtils.contains(stas, x.getStationNo())).collect(Collectors.toList());
                double liveVal = ReflectHandleUtils.getValByOp(collect, "val", "avg");//区域实况值
                //获得某区域的常年值数据
                List<Product> collect1 = perenList.stream().filter(x -> StringUtils.contains(stas, x.getStationNo())).collect(Collectors.toList());
                double perenVal = ReflectHandleUtils.getValByOp(collect1, "val", "avg");//区域常年值
                double annomlyVal = liveVal - perenVal;
                if (StringUtils.contains(element,"PRE")){
                    annomlyVal = annomlyVal/perenVal*100;
                }
                map1.put("stationNo",regionId);
                map1.put("stationName",desc);
                map1.put(element+"liveVal",NumberUtil.round(liveVal,1));
                map1.put(element+"perenVal",NumberUtil.round(perenVal,1));
                map1.put(element+"annomlyVal",NumberUtil.round(annomlyVal,1));
                regionList.add(map1);
            }
            //=========================单站=====================
            for (String stationNo : stationNos) {
                Map<String,Object> map2 = new HashMap<>();
                //单站当年数据
                Product product = currentList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList()).get(0);
                Double singleLiveVal = product.getVal();//实况值
                //单站常年值数据
                List<Product> collect2 = perenList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
                double singlePerenVal = ReflectHandleUtils.getValByOp(collect2, "val", "avg");//常年值
                double singleAnnomlyVal = singleLiveVal - singlePerenVal;//距平
                if (StringUtils.contains(element,"PRE")){
                    singleAnnomlyVal = singleAnnomlyVal/singlePerenVal*100;//降水距平百分率
                }
                map2.put("stationNo",stationNo);
                map2.put("stationName",product.getStationName());
                map2.put("longitude",product.getLongitude());
                map2.put("latitude",product.getLatitude());
                map2.put(element+"liveVal", NumberUtil.round(singleLiveVal,1));
                map2.put(element+"perenVal",NumberUtil.round(singlePerenVal,1));
                map2.put(element+"annomlyVal",NumberUtil.round(singleAnnomlyVal,1));
                singleList.add(map2);
            }
        }
        //处理一下，方便后面取值和绘图
        for (Map<String, Object> single1 : singleList) {
            for (Map<String, Object> single2 : singleList) {
                if (StringUtils.equals(single1.get("stationNo").toString(),single2.get("stationNo").toString())){
                    single1.putAll(single2);
                }
                singleLists.add(single1);
            }
        }
        singleLists = singleLists.stream().distinct().collect(Collectors.toList());
        for (Map<String, Object> region1 : regionList) {
            for (Map<String, Object> region2 : regionList) {
                if (StringUtils.equals(region1.get("stationNo").toString(),region2.get("stationNo").toString())){
                    region1.putAll(region2);
                }
                regionLists.add(region1);
            }
        }
        regionLists = regionLists.stream().distinct().collect(Collectors.toList());
        result.put("region",regionLists);
        result.put("single",singleLists);
        return result;
    }
    //内容拼接
    public Map<String,Object> contentData(Map<String, Object> map){
        Map<String,Object> result = new HashMap<>();
        //段落
        String str1 = "%s，全区平均气温 %s℃，较常年同期偏%s℃。其中%s。";
        String str2 = "全区平均降水量 %smm ，较常年同期偏%s%%。其中%s。";
        List<Map<String,Object>> regions = (List<Map<String,Object>>)map.get("region");
        List<Map<String,Object>> singles = (List<Map<String,Object>>)map.get("single");
        String s1 = startData+"～"+endData;
        result.put("time", s1);
        String s2 = "",s3 = "",t2 = "",t3 = "";
        StringBuilder s4 = new StringBuilder();
        StringBuilder t4 = new StringBuilder();
        for (Map<String, Object> region : regions) {
            if (StringUtils.equals(region.get("stationNo").toString(),"1")){//全区
                s2 = region.get("TEM_AvgliveVal").toString();
                s3 = Convert.toDouble(region.get("TEM_AvgannomlyVal").toString()) > 0 ? "高" : "低" +
                        Math.abs(Convert.toDouble(region.get("TEM_AvgannomlyVal").toString()));
                t2 = region.get("PRE_Time_2020liveVal").toString();
                t3 = Convert.toDouble(region.get("PRE_Time_2020annomlyVal").toString()) > 0 ? "多" : "少" +
                        Math.abs(Convert.toDouble(region.get("PRE_Time_2020annomlyVal").toString()));
            }
        }
        String Areas1 = "4,5,6";
        for (RegionStaEnum value : RegionStaEnum.values()) {
            if(StringUtils.contains(Areas1,value.getRegionId())){
                String desc = value.getDesc();
                String stas = value.getStas();
                //该区域所有站点数据
                List<Map<String, Object>> stationNosList = singles.stream().filter(x -> StringUtils.contains(stas, x.get("stationNo").toString())).collect(Collectors.toList());
                //==============气温==============
                DoubleSummaryStatistics temSummary = stationNosList.stream().mapToDouble(x -> Convert.toDouble(x.get("TEM_AvgliveVal"))).summaryStatistics();
                s4.append(desc).append(temSummary.getMin()).append("℃").append("～").append(temSummary.getMax()).append("℃，");
                List<Map<String, Object>> annoList1 = stationNosList.stream().filter(x -> Convert.toDouble(x.get("TEM_AvgannomlyVal").toString()) > 0).collect(Collectors.toList());
                if (annoList1.size() > 0){
                    List<String> stationName = annoList1.stream().map(x -> x.get("stationName").toString()).distinct().collect(Collectors.toList());
                    String stationNames = String.join("、", stationName);
                    DoubleSummaryStatistics annoSummary = annoList1.stream().mapToDouble(x -> Convert.toDouble(x.get("TEM_AvgannomlyVal").toString())).summaryStatistics();
                    s4.append(stationNames).append("偏高").append(annoSummary.getMin()).append("℃").append("～").append(annoSummary.getMax()).append("℃，");
                }
                List<Map<String, Object>> annoList2 = stationNosList.stream().filter(x -> Convert.toDouble(x.get("TEM_AvgannomlyVal").toString()) < 0).collect(Collectors.toList());
                if (annoList2.size() > 0){
                    List<String> stationName = annoList2.stream().map(x -> x.get("stationName").toString()).distinct().collect(Collectors.toList());
                    String stationNames = String.join("、", stationName);
                    DoubleSummaryStatistics annoSummary = annoList2.stream().mapToDouble(x -> Convert.toDouble(x.get("TEM_AvgannomlyVal").toString())).summaryStatistics();
                    s4.append(stationNames).append("偏低").append(annoSummary.getMin()).append("℃").append("～").append(annoSummary.getMax()).append("℃，");
                }
                //===============降水==============
                DoubleSummaryStatistics preSummary = stationNosList.stream().mapToDouble(x -> Convert.toDouble(x.get("PRE_Time_2020liveVal"))).summaryStatistics();
                t4.append(desc).append(preSummary.getMin()).append("mm").append("～").append(preSummary.getMax()).append("mm，");
                List<Map<String, Object>> annoList3 = stationNosList.stream().filter(x -> Convert.toDouble(x.get("PRE_Time_2020annomlyVal").toString()) > 0).collect(Collectors.toList());
                if (annoList3.size() > 0){
                    List<String> stationName = annoList3.stream().map(x -> x.get("stationName").toString()).distinct().collect(Collectors.toList());
                    String stationNames = String.join("、", stationName);
                    DoubleSummaryStatistics annoSummary = annoList3.stream().mapToDouble(x -> Convert.toDouble(x.get("PRE_Time_2020annomlyVal").toString())).summaryStatistics();
                    t4.append(stationNames).append("偏多").append(annoSummary.getMin()).append("%").append("～").append(annoSummary.getMax()).append("%，");
                }
                List<Map<String, Object>> annoList4 = stationNosList.stream().filter(x -> Convert.toDouble(x.get("PRE_Time_2020annomlyVal").toString()) < 0).collect(Collectors.toList());
                if (annoList4.size() > 0){
                    List<String> stationName = annoList4.stream().map(x -> x.get("stationName").toString()).distinct().collect(Collectors.toList());
                    String stationNames = String.join("、", stationName);
                    DoubleSummaryStatistics annoSummary = annoList4.stream().mapToDouble(x -> Convert.toDouble(x.get("PRE_Time_2020annomlyVal").toString())).summaryStatistics();
                    t4.append(stationNames).append("偏少").append(annoSummary.getMin()).append("%").append("～").append(annoSummary.getMax()).append("%，");
                }
            }
        }
        str1 = String.format(str1,s1,s2,s3,s4.substring(0, s4.length()-1));
        str2 = String.format(str2,t2,t3,t4.substring(0, t4.length()-1));
        result.put("temContent",str1);
        result.put("preContent",str2);
        //表格
        List<Map<String,Object>> table = new ArrayList<>();
        String Areas2 = "7,8,9,10,11";
        for (RegionStaEnum value : RegionStaEnum.values()) {
            String regionId = value.getRegionId();
            String stas = value.getStas();
            if(StringUtils.contains(Areas2,regionId) && !StringUtils.equals(regionId,"1")){
                for (Map<String, Object> region : regions) {
                    if(StringUtils.equals(region.get("stationNo").toString(),regionId)){
                        table.add(region);
                    }
                }
                for (Map<String, Object> single : singles) {
                    if(StringUtils.contains(stas,single.get("stationNo").toString())){
                        table.add(single);
                    }
                }
            }
        }
        result.put("table",table);
        //绘图
        //气温
        String temPic1 = DrawUtils.drawImg(singles,"TEM_AvgliveVal",s1+"平均气温分布图");
        //气温距平
        String temPic2 = DrawUtils.drawImg(singles,"TEM_AvgannomlyVal",s1+"平均气温距平分布图");
        //降水
        String prePic1 = DrawUtils.drawImg(singles,"PRE_Time_2020liveVal",s1+"降水量分布图");
        //降水距平百分率
        String prePic2 = DrawUtils.drawImg(singles, "PRE_Time_2020annomlyVal", s1 + "降水距平百分率分布图");
        result.put("temPic1",temPic1);
        result.put("temPic2",temPic2);
        result.put("prePic1",prePic1);
        result.put("prePic2",prePic2);
        return result;
    }
}
