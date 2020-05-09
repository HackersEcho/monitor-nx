package com.dafang.monitor.nx.product.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.product.Products;
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
public class DecisionService extends TemplateAbstract implements Products {

    @Autowired
    ProductMapper mapper;
    String RegionIds = "1,4,5,6";

    @Override
    protected void init(ProductParams params) {
        startData = params.getStartDate();
        endData = params.getEndDate();
        year = startData.substring(0,4);
        fileName = year+"年"+ ProductEmun.getFileName(5);
        templateName = "决策服务材料.ftl";
        //各站降水量集合
        baseData = mapper.periodsList(params);
        currentList = baseData.stream().filter(x-> StringUtils.equals(x.getYear().toString(),year)).collect(Collectors.toList());
        perenList = baseData.stream().filter(x-> Convert.toDouble(x.getYear()) >= 1981 && Convert.toDouble(x.getYear()) <= 2010).collect(Collectors.toList());
        //降水日数
        params.setMin(0.1);
        baseDayData = mapper.periodDays(params);
        currentDayList = baseDayData.stream().filter(x-> StringUtils.equals(x.getYear().toString(),year)).collect(Collectors.toList());
        perenDayList = baseDayData.stream().filter(x-> Convert.toDouble(x.getYear()) >= 1981 && Convert.toDouble(x.getYear()) <= 2010).collect(Collectors.toList());
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        Map<String, Object> result = new HashMap<>();
        //数据获取
        Map<String, Object> map = handleData();
        String str = "%s以来，全区平均降水量为%s毫米，较常年同期偏%s%%；引黄灌区、中部干旱带、南部山区降水量分别为%s，偏%s%%、偏%s%%、偏%s%%。" +
                "全区各地降水量在%s～%s毫米之间，较常年同期偏%s%%～偏%s%%；各地降水日数为%s～%s天，偏%s～偏%s天。";
        String s1 = startData+"～"+endData;
        String s2 = map.get("全区liveVal").toString();
        String s3 = Convert.toDouble(map.get("全区annomlyVal")) > 0 ? "多" : "少" + Math.abs(Convert.toDouble(map.get("全区annomlyVal")));
        String s4 = map.get("引黄灌区liveVal").toString()+"mm、"+map.get("中部干旱带liveVal").toString()+"mm、"+map.get("南部山区liveVal").toString()+"mm";
        String s5 = Convert.toDouble(map.get("引黄灌区annomlyVal")) > 0 ? "多" : "少" + Math.abs(Convert.toDouble(map.get("引黄灌区annomlyVal")));
        String s6 = Convert.toDouble(map.get("中部干旱带annomlyVal")) > 0 ? "多" : "少" + Math.abs(Convert.toDouble(map.get("中部干旱带annomlyVal")));
        String s7 = Convert.toDouble(map.get("南部山区annomlyVal")) > 0 ? "多" : "少" + Math.abs(Convert.toDouble(map.get("南部山区annomlyVal")));
        String s8 = map.get("staLiveValeMin").toString();
        String s9 = map.get("staLiveValeMax").toString();
        String s10 = Convert.toDouble(map.get("staAnnomlyValMin")) > 0 ? "多" : "少" + Math.abs(Convert.toDouble(map.get("staAnnomlyValMin")));
        String s11 = Convert.toDouble(map.get("staAnnomlyValMax")) > 0 ? "多" : "少" + Math.abs(Convert.toDouble(map.get("staAnnomlyValMax")));
        String s12 = map.get("staDayLiveValeMin").toString();
        String s13 = map.get("staDayLiveValeMax").toString();
        String s14 = Convert.toDouble(map.get("staDayAnnomlyValMin")) > 0 ? "多" : "少" + Math.abs(Convert.toDouble(map.get("staDayAnnomlyValMin")));
        String s15 = Convert.toDouble(map.get("staDayAnnomlyValMax")) > 0 ? "多" : "少" + Math.abs(Convert.toDouble(map.get("staDayAnnomlyValMax")));
        str = String.format(str,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15);
        result.put("preDesc",str);
        return result;
    }

    /*
    数据处理
     */
    public Map<String,Object> handleData(){
        Map<String,Object> map = new HashMap<>();
        //区域数据处理
        for (RegionStaEnum value : RegionStaEnum.values()) {
            String regionId = value.getRegionId();
            String RegionName = value.getDesc();
            if (StringUtils.contains(RegionIds,regionId)){
                List<String> stas = RegionStaEnum.getStas(regionId);
                String stationNos = String.join(",", stas);
                List<Product> RegionCurrentList = currentList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo().toString())).collect(Collectors.toList());
                List<Product> RegionPerenList = perenList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo().toString())).collect(Collectors.toList());
                double liveVal = ReflectHandleUtils.getValByOp(RegionCurrentList, "val", "avg");
                double perenVal = ReflectHandleUtils.getValByOp(RegionPerenList, "val", "avg");
                double annomlyVal = (liveVal - perenVal)/perenVal*100;
                map.put(RegionName+"liveVal", NumberUtil.round(liveVal,1));
                map.put(RegionName+"perenVal",NumberUtil.round(perenVal,1));
                map.put(RegionName+"annomlyVal",NumberUtil.round(annomlyVal,1));
                if(regionId.equals("1")){//全区各地数据处理
                    List<String> stationNo = baseData.stream().map(Product::getStationNo).distinct().collect(Collectors.toList());
                    double staLiveValeMax = -999,staLiveValeMin = 999,staAnnomlyValMax = -999,staAnnomlyValMin = 999;
                    double staDayLiveValeMax = -999,staDayLiveValeMin = 999,staDayAnnomlyValMax = -999,staDayAnnomlyValMin = 999;
                    for (String sta : stas) {
                        //降水量
                        List<Product> collect1 = currentList.stream().filter(x -> StringUtils.equals(x.getStationNo(), sta)).collect(Collectors.toList());
                        if (collect1.size() > 0){
                            Double staLiveVale = collect1.get(0).getVal();
                            List<Product> collect2 = perenList.stream().filter(x -> StringUtils.equals(x.getStationNo(), sta)).collect(Collectors.toList());
                            double staPerenVal = ReflectHandleUtils.getValByOp(collect2, "val", "avg");
                            double staAnnomlyVal = (staLiveVale - staPerenVal)/staPerenVal*100;
                            if(staLiveValeMax < staLiveVale){
                                staLiveValeMax = staLiveVale;
                            }
                            if(staLiveValeMin > staLiveVale){
                                staLiveValeMin = staLiveVale;
                            }
                            if(staAnnomlyValMax < staAnnomlyVal){
                                staAnnomlyValMax = staAnnomlyVal;
                            }
                            if(staAnnomlyValMin > staAnnomlyVal){
                                staAnnomlyValMin = staAnnomlyVal;
                            }
                        }
                        //降水日数
                        List<Product> collect3 = currentDayList.stream().filter(x -> StringUtils.equals(x.getStationNo(), sta)).collect(Collectors.toList());
                        if (collect3.size() > 0){
                            Double staDayLiveVale = collect3.get(0).getVal();
                            List<Product> collect4 = perenDayList.stream().filter(x -> StringUtils.equals(x.getStationNo(), sta)).collect(Collectors.toList());
                            double staDayPerenVal = ReflectHandleUtils.getValByOp(collect4, "val", "avg");
                            double staDayAnnomlyVal = staDayLiveVale - staDayPerenVal;
                            if(staDayLiveValeMax < staDayLiveVale){
                                staDayLiveValeMax = staDayLiveVale;
                            }
                            if(staDayLiveValeMin > staDayLiveVale){
                                staDayLiveValeMin = staDayLiveVale;
                            }
                            if(staDayAnnomlyValMax < staDayAnnomlyVal){
                                staDayAnnomlyValMax = staDayAnnomlyVal;
                            }
                            if(staDayAnnomlyValMin > staDayAnnomlyVal){
                                staDayAnnomlyValMin = staDayAnnomlyVal;
                            }
                        }
                    }
                    map.put("staLiveValeMax",NumberUtil.round(staLiveValeMax,1));
                    map.put("staLiveValeMin",NumberUtil.round(staLiveValeMin,1));
                    map.put("staAnnomlyValMax",NumberUtil.round(staAnnomlyValMax,1));
                    map.put("staAnnomlyValMin",NumberUtil.round(staAnnomlyValMin,1));
                    map.put("staDayLiveValeMax",staDayLiveValeMax);
                    map.put("staDayLiveValeMin",staDayLiveValeMin);
                    map.put("staDayAnnomlyValMax",NumberUtil.round(staDayAnnomlyValMax,1));
                    map.put("staDayAnnomlyValMin",NumberUtil.round(staDayAnnomlyValMin,1));
                }
            }
        }
        return map;
    }
}
