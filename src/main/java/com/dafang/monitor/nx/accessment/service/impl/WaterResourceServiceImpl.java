package com.dafang.monitor.nx.accessment.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.accessment.entity.emun.AreaEnum;
import com.dafang.monitor.nx.accessment.entity.emun.RegionEnum;
import com.dafang.monitor.nx.accessment.entity.po.WaterResource;
import com.dafang.monitor.nx.accessment.entity.po.WaterResourceparam;
import com.dafang.monitor.nx.accessment.mapper.WaterResourceMapper;
import com.dafang.monitor.nx.accessment.service.WaterResourceService;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WaterResourceServiceImpl implements WaterResourceService {

    @Autowired
    private WaterResourceMapper mapper;

    @Override
    public List<Map<String, Object>> dataList(WaterResourceparam params) {
        List<Map<String, Object>> results = new ArrayList<>();
        //年降水量集合
        List<WaterResource> baseData = mapper.preList();
        //起始年份
        Integer startYear = Convert.toInt(params.getStartYear());
        Integer endYear = Convert.toInt(params.getEndYear());
        //常年值区间
        String[] scale = params.getClimateScale().split("-");
        int scaleLen = Convert.toInt(scale[1])-Convert.toInt(scale[0])+1;
        //所选区域
        String[] regions = {params.getRegionName()};
        if(params.getRegionName().indexOf(",") != -1){
            regions = params.getRegionName().split(",");
        }
        List<Map<String, Object>> regionList = new ArrayList<>();
        for (AreaEnum areaInfo : AreaEnum.values()) {//自动站
            Double area = areaInfo.getArea();
            String regionId = areaInfo.getRegionId();
            String regionName = areaInfo.getRegionName();
            if(regionId.length() >= 5){
                List<WaterResource> singleList = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), regionId)).collect(Collectors.toList());
                List<WaterResource> perenList = singleList.stream().filter(x -> x.getYear() >= Convert.toInt(scale[0]) && x.getYear() <= Convert.toInt(scale[1])).collect(Collectors.toList());
                double perenVal = ReflectHandleUtils.getValByOp(perenList, "pre", "sum")/30;//降水量常年值
                double rePerenVal = perenVal*area/100000;//降水资源常年值
                double standardValue = perenList.stream().mapToDouble(x -> Convert.toDouble(NumberUtil.round(Convert.toDouble(x.getPre())-perenVal,2))).summaryStatistics().getSum()/scaleLen;//均方差
                for (int i = startYear; i<= endYear; i++){
                    Map<String, Object> map = new HashMap<>();
                    int year = i;
                    List<WaterResource> currentList = singleList.stream().filter(x -> x.getYear() == year).collect(Collectors.toList());
                    if(currentList.size()>0){
                        String liveVal = singleList.stream().filter(x -> x.getYear() == year).collect(Collectors.toList()).get(0).getPre();
                        double reLiveVal = Convert.toDouble(liveVal)*area/100000;//降水资源实况值
                        double reAnomalyVal =  reLiveVal - rePerenVal;//将水资源距平
                        map.put("year", year);
                        map.put("regionName", regionName);
                        map.put("liveVal", NumberUtil.round(reLiveVal,2));
                        map.put("perenVal", NumberUtil.round(rePerenVal,2));
                        map.put("annomalyVal", NumberUtil.round(reAnomalyVal,2));
                        map.put("standardValue", NumberUtil.round(standardValue,2));
                        map.put("level",getWaterResourcesLevel(reLiveVal,rePerenVal,standardValue));
                        regionList.add(map);
                    }
                }
            }
        }
        for (AreaEnum areaInfo : AreaEnum.values()) {//区域站
            String regionId = areaInfo.getRegionId();
            String regionName = areaInfo.getRegionName();
            if(regionId.length() <= 2 && regionList.size() > 0){
                List<String> stas = RegionEnum.getStas(regionId);//区域包含的所有站点
                for (int i = startYear; i<= endYear; i++){
                    Map<String, Object> map = new HashMap<>();
                    int year = i;
                    List<Map<String, Object>> yearList = regionList.stream().filter(x -> Convert.toInt(x.get("year")) == year).collect(Collectors.toList());
                    double reLiveVal = 0.0;
                    double reAnomalyVal = 0.0;
                    double standardValue = 0.0;
                    double rePerenVal = 0.0;
                    for (String sta : stas) {
                        List<Map<String, Object>> list = yearList.stream().filter(x -> StringUtils.equals(x.get("regionName").toString(), sta)).
                                collect(Collectors.toList());
                        if (list.size() > 0) {
                            Map<String, Object> singleMap =list.get(0);
                            reLiveVal += Convert.toDouble(singleMap.get("liveVal"));
                            rePerenVal += Convert.toDouble(singleMap.get("perenVal"));
                            reAnomalyVal += Convert.toDouble(singleMap.get("annomalyVal"));
                            standardValue += Convert.toDouble(singleMap.get("standardValue"));
                        }
                    }
                    map.put("year", year);
                    map.put("regionName", regionName);
                    map.put("liveVal", NumberUtil.round(reLiveVal,2));
                    map.put("perenVal", NumberUtil.round(rePerenVal,2));
                    map.put("annomalyVal", NumberUtil.round(reAnomalyVal,2));
                    map.put("standardValue", NumberUtil.round(standardValue,2));
                    map.put("level",getWaterResourcesLevel(reLiveVal,rePerenVal,standardValue));
                    regionList.add(map);
                }
            }
        }
        for (String region : regions) {
            List<Map<String, Object>> resultList = regionList.stream().filter(x -> StringUtils.equals(x.get("regionName").toString(), region)).collect(Collectors.toList());
            results.addAll(resultList);
        }
        return results;
    }
    /**
     * 降水资源等级
     *
     * @param preValue
     *            当年值
     * @param preAvgValue
     *            多年平均值（即1981-2010年平均值）
     * @param standardValue
     *            均方差
     * @return 降水资源等级
     */
    public static String getWaterResourcesLevel(double preValue, double preAvgValue, double standardValue) {
        String level = "";
        double value1 = preAvgValue + 1.5 * standardValue;
        double value2 = preAvgValue + 0.7 * standardValue;
        double value3 = preAvgValue - 1.5 * standardValue;
        double value4 = preAvgValue - 0.7 * standardValue;
        if (preValue > value1) {
            level = "异常丰水年";
        }
        if (preValue <= value1 && preValue >= value2) {
            level = "丰水年";
        }
        if (preValue < value1 && preValue > value4) {
            level = "正常年";
        }
        if (preValue <= value4 && preValue >= value3) {
            level = "枯水年";
        }
        if (preValue < value3) {
            level = "异常枯水年";
        }
        return level;
    }
}
