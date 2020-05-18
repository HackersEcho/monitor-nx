package com.dafang.monitor.nx.product.impl.evaluate;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.entity.SeasonEnum;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
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
public class SeasonClimateImpact extends TemplateAbstract {

    @Autowired
    ProductMapper mapper;
    String[] elements = {"TEM_Avg","PRE_Time_2020","SSH"};

    @Override
    protected void init(ProductParams params) {
        year = params.getYear();
        season = params.getSeason();

    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        return null;
    }

    //数据处理
    public Map<String,Object> handleDate(ProductParams params){
        //获得季节内月份时间段
        List<String> Slots = SeasonEnum.getSlotBySeason(season);

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
            for (String slot : Slots) {//时间段（春季/3月/4月/5月）
                Map<String,Object> QXMap = new HashMap<>();
                String dateName = slot.split("_")[2];
                params.setST(month+slot.split("_")[0]);
                params.setET(month+slot.split("_")[1]);
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


}
