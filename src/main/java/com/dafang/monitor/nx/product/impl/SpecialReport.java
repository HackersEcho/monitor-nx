package com.dafang.monitor.nx.product.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.resolver.JPATraversableResolver;

import javax.annotation.Resource;
import java.sql.Ref;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpecialReport extends TemplateAbstract {

    @Resource
    ProductMapper mapper;
    String[] elements = {"TEM_Min","TEM_Max","TEM_Avg","PRE_Time_2020"};

    @Override
    protected void init(ProductParams params) {
        startData = params.getStartDate();
        endData = params.getEndDate();
        startMonthDay = startData.substring(4,6)+"月"+startData.substring(6)+"日";
        endMonthDay = endData.substring(4,6)+"月"+endData.substring(6)+"日";
        year = startData.substring(0, 4);
        baseData = mapper.periodList(params);//基础要素日数据（气温，降水）
        currentList = baseData.stream().filter(x -> StringUtils.equals(x.getYear().toString(), year)).collect(Collectors.toList());
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        return null;
    }

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
            List<Product> cuList = periodList.stream().filter(x -> x.getYear().equals(year)).collect(Collectors.toList());
            double max = ReflectHandleUtils.getValByOp(cuList, "val", "max");
            double min = ReflectHandleUtils.getValByOp(cuList, "val", "min");
            map.put(element+"max",max);
            map.put(element+"min",min);
            if (StringUtils.equals(element,"TEM_Max") || StringUtils.equals(element,"TEM_Min")){
                double extMax = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(currentList, element), element, "max");
                double extMin = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(currentList, element), element, "min");
                map.put(element+"extMax",extMax);
                map.put(element+"extMin",extMin);
            }

            List<Product> collect = periodList.stream().filter(x -> Convert.toDouble(x.getYear()) >= 1961 && Convert.toDouble(x.getYear()) <= Convert.toDouble(year)).collect(Collectors.toList());

        }

        return null;
    }


}
