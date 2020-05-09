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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 重要气候信息
 */
@Service
public class ClimateInfo extends TemplateAbstract {

    @Autowired
    private ProductMapper mapper;
    //该产品用到的区域和要素
    String[] regionids = {"1","4","5","6"};
    String[] elements = {"TEM_Avg","PRE_Time_2020"};

    @Override
    protected void init(ProductParams params) {
        startData = params.getStartDate().substring(4,6)+"月"+params.getStartDate().substring(6)+"日";
        endData = params.getEndDate().substring(4,6)+"月"+params.getEndDate().substring(6)+"日";
        year = params.getStartDate().substring(0,4);
        month = params.getStartDate().substring(4,6);
        day = params.getStartDate().substring(6,8);
        fileName = year+"年"+startData+"-"+year+"年"+endData+ProductEmun.getFileName(3);
        templateName = "重要气候信息.ftl";
        //所有要素数据
        baseData = mapper.periodList(params);
        //当年数据
        currentList = baseData.stream().filter(x-> StringUtils.equals(x.getYear().toString(),year)).collect(Collectors.toList());
        //常年数据
        perenList = baseData.stream().filter(x->x.getYear() >= 1981 && x.getYear() <= 2010).collect(Collectors.toList());
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        Map<String,Object> result = new HashMap<>();
        //得到区域站的实况常年距平
        Map<String, Object> handle = new HashMap<>();
        for (String regionid : regionids) {
            for (String element : elements) {
                handle = handleRegionData(element,regionid,handle);
            }
        }

        //得到国家站的实况常年距平
        List<Map<String,Object>> staDataList = handleStationData();

        //产品内容拼接
        String str = "%s～%s以来，我区气温总体偏%s，降水偏%s。"+
                "全区平均气温为%s℃，较常年同期偏%s%s℃；其中引黄灌区、中部干旱带、南部山区平均气温分别为%s℃、%s℃、%s℃，偏%s %s℃、%s℃、%s℃。" +
                "全区平均降水量为%smm，较常年同期偏%s%s%%；其中引黄灌区、中部干旱带、南部山区分别为%smm、%smm、%smm，偏%s %s%%、%s%%、%s%%";
        String s1 = startData;
        String s2 = endData;
        String s3 = Convert.toDouble(handle.get("1-TEM_Avg-annomlyVal")) > 0 ? "高" : "低";
        String s4 = Convert.toDouble(handle.get("1-PRE_Time_2020-annomlyVal")) > 0 ? "多" : "少";
        String s5 = handle.get("1-TEM_Avg-liveVal")+"";
        String s6 = Convert.toDouble(handle.get("1-TEM_Avg-annomlyVal")) > 0 ? "高" : "低";
        String s7 = Math.abs(Convert.toDouble(handle.get("1-TEM_Avg-annomlyVal")))+"";
        String s8 = handle.get("4-TEM_Avg-liveVal")+"";
        String s9 = handle.get("5-TEM_Avg-liveVal")+"";
        String s10 = handle.get("6-TEM_Avg-liveVal")+"";
        String s11 = Convert.toDouble(handle.get("4-TEM_Avg-annomlyVal")) > 0 ? "高" : "低";
        String s12 = Math.abs(Convert.toDouble(handle.get("4-TEM_Avg-annomlyVal")))+"";
        String s13 = Math.abs(Convert.toDouble(handle.get("5-TEM_Avg-annomlyVal")))+"";
        String s14 = Math.abs(Convert.toDouble(handle.get("6-TEM_Avg-annomlyVal")))+"";
        String s15 = handle.get("1-PRE_Time_2020-liveVal")+"";
        String s16 = Convert.toDouble(handle.get("1-PRE_Time_2020-annomlyVal")) > 0 ? "多" : "少";
        String s17 = Math.abs(Convert.toDouble(handle.get("1-PRE_Time_2020-annomlyVal")))+"";
        String s18 = handle.get("4-PRE_Time_2020-liveVal")+"";
        String s19 = handle.get("5-PRE_Time_2020-liveVal")+"";
        String s20 = handle.get("6-PRE_Time_2020-liveVal")+"";
        String s21 = Convert.toDouble(handle.get("4-PRE_Time_2020-annomlyVal")) > 0 ? "多" : "少";
        String s22 = Math.abs(Convert.toDouble(handle.get("4-PRE_Time_2020-annomlyVal")))+"";
        String s23 = Math.abs(Convert.toDouble(handle.get("5-PRE_Time_2020-annomlyVal")))+"";
        String s24 = Math.abs(Convert.toDouble(handle.get("6-PRE_Time_2020-annomlyVal")))+"";

        str = String.format(str,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19,s20,s21,s22,s23,s24);

        //产品分布图绘制
        String temPic1 = DrawUtils.drawImg(staDataList, "TEM_Avg-liveVal", startData + "~" + endData + "平均气温");
        String temPic2 = DrawUtils.drawImg(staDataList, "TEM_Avg-annomlyVal", startData + "~" + endData + "平均气温距平");
        String prePic1 = DrawUtils.drawImg(staDataList, "PRE_Time_2020-liveVal", startData + "~" + endData + "降水量");
        String prePic2 = DrawUtils.drawImg(staDataList, "PRE_Time_2020-annomlyVal", startData + "~" + endData + "降水距平百分率");

        result.put("startData",startData);
        result.put("endData",endData);
        result.put("year",year);
        result.put("month",month);
        result.put("day",day);
        result.put("productDate",year+"年"+startData);
        result.put("climateDesc",str);
        result.put("pic1",temPic1);
        result.put("pic2",temPic2);
        result.put("pic3",prePic1);
        result.put("pic4",prePic2);
        return result;
    }

    /**
     * 处理区域数据，得到实况常年距平
     * @param element
     * @param regionId
     * @return
     */
    public Map<String,Object> handleRegionData(String element, String regionId, Map<String,Object> map){
        // 得到所有站点信息枚举
        RegionStaEnum[] staEnums = RegionStaEnum.values();
        for (RegionStaEnum staEnum : staEnums) {
            if(StringUtils.equals(staEnum.getRegionId(),regionId)){
                //获得区域内所有站点
                String stas = staEnum.getStas();
                //区域内站点个数
                int staLen = stas.split(",").length;
                List<Product> current = currentList.stream().filter(x -> StringUtils.contains(stas, x.getStationNo())).collect(Collectors.toList());
                List<Product> peren = perenList.stream().filter(x -> StringUtils.contains(stas, x.getStationNo())).collect(Collectors.toList());
                double liveVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(current, element), element, "avg");
                double perenVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(peren, element), element, "avg");
                double annomlyVal = liveVal - perenVal;
                if(StringUtils.contains(element,"PRE")){
                    liveVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(current, element), element, "sum")/staLen;
                    perenVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(peren, element), element, "sum")/staLen/30;
                    annomlyVal = (liveVal - perenVal)/perenVal*100;//距平百分率
                }
                map.put(regionId+"-"+element+"-"+"liveVal",NumberUtil.round(liveVal,1));
                map.put(regionId+"-"+element+"-"+"perenVal",NumberUtil.round(perenVal,1));
                map.put(regionId+"-"+element+"-"+"annomlyVal",NumberUtil.round(annomlyVal,1));
            }
        }
        return map;
    }

    /**
     * 处理站点数据，实况常年距平
     * @return
     */
    public List<Map<String,Object>> handleStationData(){
        List<Map<String,Object>> list = new ArrayList<>();
        List<String> stationNos = currentList.stream().map(Product::getStationNo).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            Map<String,Object> map = new HashMap<>();
            List<Product> singleCurrentList = currentList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
            Product product = singleCurrentList.get(0);
            List<Product> singlePerenList = perenList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
            for (String element : elements) {
                double liveVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(singleCurrentList, element), element, "avg");
                double perenVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(singlePerenList, element), element, "avg");
                double annomlyVal = liveVal - perenVal;
                if(element.contains("PRE")){
                    liveVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(singleCurrentList, element), element, "sum");
                    perenVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(singlePerenList, element), element, "sum")/30;
                    annomlyVal = (liveVal - perenVal)/perenVal*100;
                }
                map.put(element+"-"+"liveVal",liveVal);
                map.put(element+"-"+"perenVal",perenVal);
                map.put(element+"-"+"annomlyVal",annomlyVal);
            }
            map.put("stationNo",stationNo);
            map.put("stationName",product.getStationName());
            map.put("longitude",product.getLongitude());
            map.put("latitude",product.getLatitude());
            list.add(map);
        }
        return list;
    }

}
