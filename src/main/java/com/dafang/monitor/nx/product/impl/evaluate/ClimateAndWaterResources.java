package com.dafang.monitor.nx.product.impl.evaluate;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.accessment.entity.po.WaterResourceparam;
import com.dafang.monitor.nx.accessment.service.impl.WaterResourceServiceImpl;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.utils.jfreechart.JFreeChartUtil;
import org.apache.commons.lang3.StringUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClimateAndWaterResources extends TemplateAbstract {

    @Autowired
    WaterResourceServiceImpl waterResourceService;
    String[] regions = {"全区","银川市","石嘴山市","吴忠市","中卫市","固原市"};

    @Override
    protected void init(ProductParams params) {
        year = params.getYear();
        templateName = "气候与水资源.ftl";
        fileName = year + "年" + ProductEmun.getFileName(11);
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        return contentData(hanleData(params));
    }
    //数据处理
    public List<Map<String,Object>> hanleData(ProductParams params){
        String region = String.join(",", regions);
        WaterResourceparam water = new WaterResourceparam(region, "1961", year, "1981-2010");
        List<Map<String,Object>> waterList = waterResourceService.dataList(water);
        return waterList;
    }
    //内容拼接
    public Map<String,Object> contentData(List<Map<String,Object>> waterList){
        Map<String,Object> result = new HashMap<>();
        List<Map<String, Object>> currentList = waterList.stream().filter(x -> StringUtils.equals(x.get("year").toString(), this.year)).collect(Collectors.toList());
        int index = 0;
        for (String region : regions) {
            //===============文字==================
            index++;
            String str = "%s降水资源总量%s亿立方米，较常年偏%s亿立方米，属于%s。";
            Map<String, Object> map = currentList.stream().filter(x -> StringUtils.equals(x.get("regionName").toString(), region)).collect(Collectors.toList()).get(0);
            String annomalyVal = (Convert.toDouble(map.get("annomalyVal")) > 0 ? "高" : "低") + Math.abs(Convert.toDouble(map.get("annomalyVal")));
            str = String.format(str,region,map.get("liveVal").toString(),annomalyVal,map.get("level").toString());
            result.put("water"+index,str);
            //================柱状图================
            List<Map<String, Object>> sineleList = waterList.stream().filter(x -> StringUtils.equals(x.get("regionName").toString(), region)).collect(Collectors.toList());
            DefaultCategoryDataset linedataset = new DefaultCategoryDataset();
            String series = "降水资源总量";
            for (Map<String, Object> single : sineleList) {
                linedataset.addValue(Convert.toDouble(single.get("liveVal")),series,single.get("year").toString());
            }
            String his = JFreeChartUtil.createChart(region+series, "", "", "降水资源总量", linedataset);
            result.put(region,region);
            result.put("his"+index,his);
        }
        result.put("year",year+"年");
        return result;
    }
}
