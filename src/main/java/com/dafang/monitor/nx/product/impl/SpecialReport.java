package com.dafang.monitor.nx.product.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpecialReport extends TemplateAbstract {

    @Resource
    ProductMapper mapper;
    String[] elements = {"TEM_Min","TEM_Max","TEM_Avg","PRE_Time_2020"};
    String single = "53614";//银川
    String singleName = "银川";//银川
    String startYear = "1961";//历史开始年限

    @Override
    protected void init(ProductParams params) {
        startData = params.getStartDate();
        endData = params.getEndDate();
        startMonthDay = startData.substring(4,6)+"月"+startData.substring(6)+"日";
        endMonthDay = endData.substring(4,6)+"月"+endData.substring(6)+"日";
        year = startData.substring(0, 4);
        fileName = year+"年"+ ProductEmun.getFileName(6);
        templateName = "专题报告.ftl";
        baseData = mapper.periodList(params);//基础要素日数据（气温，降水）
        currentList = baseData.stream().filter(x-> StringUtils.equals(x.getYear().toString(),year) &&
                StringUtils.equals(x.getStationNo(),single)).collect(Collectors.toList());//单站当年数据
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        Map<String,Object> result = new HashMap<>();

        Map<String, Object> map = handleData(params);
        String str1 = "宁夏是典型的大陆性气候。%s各地平均气温 (%s℃ )，平均最高气温(%s℃)，平均最低气温(%s℃)。日极端最高气温(%s℃)，日极端最低气温(%s℃)。平均降水量（%smm）";
        String str2 = "从历史资料（%s年）分析，（%s）(%s)多年平均气温（%s℃），平均最高气温（%s℃），平均最低气温（%s℃）。近3年来，（%s）日最高气温（%s℃）,最低气温（%s℃）。";
        String str3 = "（%s）(%s)多年平均日降水量（%smm），最大日降水量（%smm），出现日降水量>1mm 的概率（12.5～37.5%%）。近 3 年来，日最大降水量为（%smm）。";

        String s1 = startMonthDay+"～"+endMonthDay;
        String s2 = map.get("TEM_Avgmin").toString()+"～"+map.get("TEM_Avgmax").toString();
        String s3 = map.get("TEM_Maxmin").toString()+"～"+map.get("TEM_Maxmax").toString();
        String s4 = map.get("TEM_Minmin").toString()+"～"+map.get("TEM_Minmax").toString();
        String s5 = map.get("TEM_MaxextMin").toString()+"～"+map.get("TEM_MaxextMax").toString();
        String s6 = map.get("TEM_MinextMin").toString()+"～"+map.get("TEM_MinextMax").toString();
        String s7 = map.get("PRE_Time_2020min").toString()+"～"+map.get("PRE_Time_2020max").toString();
        str1 = String.format(str1,s1,s2,s3,s4,s5,s6,s7);
        String t1 = startYear+"～"+year;
        String t2 = single;
        String t3 = map.get("TEM_AvgsingleMin").toString()+"～"+map.get("TEM_AvgsingleMax").toString();
        String t4 = map.get("TEM_MaxsingleMin").toString()+"～"+map.get("TEM_MaxsingleMax").toString();
        String t5 = map.get("TEM_MinsingleMin").toString()+"～"+map.get("TEM_MinsingleMax").toString();
        String t6 = map.get("TEM_MaxdayMin").toString()+"～"+map.get("TEM_MaxdayMax").toString();
        String t7 = map.get("TEM_MindayMin").toString()+"～"+map.get("TEM_MindayMax").toString();
        str2 = String.format(str2,t1,s1,t2,t3,t4,t5,s1,t6,t7);
        String r1 = map.get("PRE_Time_2020singleMin").toString()+"～"+map.get("PRE_Time_2020singleMax").toString();
        String r2 = map.get("PRE_Time_2020preMin").toString()+"～"+map.get("PRE_Time_2020preMax").toString();
        String r3 = map.get("PRE_Time_2020dayMin").toString()+"～"+map.get("PRE_Time_2020dayMax").toString();
        str3 = String.format(str3,s1,t2,r1,r2,r3);

        result.put("year",year);
        result.put("stationName",singleName);
        result.put("fileName",ProductEmun.getFileName(6));
        result.put("monitorTime",s1);
        result.put("content",str1);
        result.put("temContent",str2);
        result.put("preContent",str3);

        return result;
    }

    /*
    处理数据
     */
    public Map<String,Object> handleData(ProductParams params){
        Map<String,Object> map = new HashMap<>();
        List<String> stationNos = baseData.stream().map(Product::getStationNo).distinct().collect(Collectors.toList());
        for (String element : elements) {
            params.setElement(element);
            params.setCal("AVG");
            if (element.contains("PRE")){
                params.setCal("SUM");
            }
            //时间段内平均，累计数据
            List<Product> periodList = mapper.periodsList(params);
            List<Product> cuList = periodList.stream().filter(x -> StringUtils.equals(x.getYear().toString(),year)).collect(Collectors.toList());
            double max = ReflectHandleUtils.getValByOp(cuList, "val", "max");
            double min = ReflectHandleUtils.getValByOp(cuList, "val", "min");
            map.put(element+"max",NumberUtil.round(max,1));//各地平均气温
            map.put(element+"min", NumberUtil.round(min,1));
            if (StringUtils.equals(element,"TEM_Max") || StringUtils.equals(element,"TEM_Min")){
                double extMax = ReflectHandleUtils.getValByOp(periodList, "val", "max");
                double extMin = ReflectHandleUtils.getValByOp(periodList, "val", "min");
                map.put(element+"extMax",NumberUtil.round(extMax,1));//日极端最高、最低气温
                map.put(element+"extMin", NumberUtil.round(extMin,1));
            }
            //=======================对单站数据做进一步处理=======================
            //历史数据
            List<Product> singleBaseData = periodList.stream().filter(x -> Convert.toDouble(x.getYear()) >= Convert.toDouble(startYear) &&
                    Convert.toDouble(x.getYear()) <= Convert.toDouble(year) && StringUtils.equals(x.getStationNo(),single)).collect(Collectors.toList());
            double singleMax = ReflectHandleUtils.getValByOp(singleBaseData, "val", "max");
            double singleMin = ReflectHandleUtils.getValByOp(singleBaseData, "val", "min");
            map.put(element+"singleMax",NumberUtil.round(singleMax,1));//多年日数据最大值、最小值
            map.put(element+"singleMin",NumberUtil.round(singleMin,1));
            if(element.equals("PRE_Time_2020")){
                double preMax = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(baseData, element), element, "max");
                double preMin = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(baseData, element), element, "min");
                map.put(element+"preMax",NumberUtil.round(preMax,1));//历史降水最大值、最小值
                map.put(element+"preMin",NumberUtil.round(preMin,1));
            }
            //近三年数据
            List<Product> thBaseData = periodList.stream().filter(x -> Convert.toDouble(x.getYear()) >= (Convert.toDouble(year)-3) &&
                    Convert.toDouble(x.getYear()) <= Convert.toDouble(year) && StringUtils.equals(x.getStationNo(),single)).collect(Collectors.toList());
            double dayMax = ReflectHandleUtils.getValByOp(singleBaseData, "val", "max");
            double dayMin = ReflectHandleUtils.getValByOp(singleBaseData, "val", "min");
            map.put(element+"dayMax",NumberUtil.round(dayMax,1));//近三年日最高、日最低
            map.put(element+"dayMin",NumberUtil.round(dayMin,1));
        }
        return map;
    }


}
