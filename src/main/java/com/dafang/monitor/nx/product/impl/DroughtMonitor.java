package com.dafang.monitor.nx.product.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.entity.CIEnum;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.DrawUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.Class;
import java.lang.Object;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DroughtMonitor extends TemplateAbstract {

    @Autowired
    private ProductMapper mapper;

    @Override
    protected void init(ProductParams params) {
        startMonthDay = params.getStartDate().substring(4,6)+"月"+params.getStartDate().substring(6)+"日";
        year = params.getStartDate().substring(0,4);
        month = params.getStartDate().substring(4,6);
        startData = year+"年"+startMonthDay;
        baseData = mapper.ciList(params);
        fileName = startData+ ProductEmun.getFileName(2);
        templateName = "宁夏气象干旱监测报告.ftl";
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        Map<String,Object> result = new HashMap<>();
        //干旱数据
        Map<String, String> dataMap = handle();
        //拼接内容
        String str = "根据综合气象干旱指数CI监测，%s， %s。";
        String s1 = year+"年"+startMonthDay;
        String s2 = "";
        for (CIEnum value : CIEnum.values()) {
            String level = value.getLevel();
            String stationNames = dataMap.get(level);
            if (stationNames != null && !level.equals("无旱")){
                s2 += stationNames+"为"+level+",";
            }
        }
        if(!s2.equals("")){
            s2 = s2.substring(0,s2.length()-1);
        }else{
            s2 = "全区无旱";
        }
        str = String.format(str,s1,s2);
        //干旱绘图数据
        List<Map<String,Object>> drawList = drawList();
        //绘图
        String images = DrawUtils.drawImg(drawList, "CI", fileName);
        result.put("images",images);
        result.put("droughtDesc",str);
        result.put("year",year);
        result.put("month",month);
        result.put("startMonthDay",startMonthDay);
        result.put("productDate",startData);
        return result;

    }

    /*
    处理用于内容拼接的数据
     */
    public Map<String,String> handle(){
        Map<String,String> map = new HashMap<>();
        for (CIEnum value : CIEnum.values()) {
            String level = value.getLevel();
            String range = value.getRange();
            List<Product> collect = baseData.stream().filter(x -> Convert.toDouble(x.getCI()) >= Convert.toDouble(range.split("_")[0]) &&
                    Convert.toDouble(x.getCI()) <= Convert.toDouble(range.split("_")[1])).collect(Collectors.toList());
            if(collect.size()>0){
                String stationName = collect.stream().map(x -> x.getStationName()).distinct().collect(Collectors.joining("、"));
                map.put(level,stationName);
            }
        }
        return map;
    }
    /*
    处理绘图数据
     */
    public  List<Map<String,Object>> drawList(){
        List<Map<String,Object>> list = new ArrayList<>();
        for (Product baseDatum : baseData) {
            Map<String,Object> map = new HashMap<>();
            map.put("stationNo",baseDatum.getStationNo());
            map.put("longitude",baseDatum.getLongitude());
            map.put("latitude",baseDatum.getLatitude());
            map.put("CI",baseDatum.getCI());
            list.add(map);
        }
        return list;
    }

}
