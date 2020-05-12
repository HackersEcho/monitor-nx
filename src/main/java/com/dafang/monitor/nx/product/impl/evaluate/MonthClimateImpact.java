package com.dafang.monitor.nx.product.impl.evaluate;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.accessment.entity.emun.RegionEnum;
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
public class MonthClimateImpact extends TemplateAbstract {

    @Autowired
    ProductMapper mapper;
    String[] elements = {"TEM_Avg","PRE_Time_2020","SSH"};
    String[] dates = {"01_31_当月","01_10_上旬","11_20_中旬","21_31_下旬"};

    @Override
    protected void init(ProductParams params) {
        year = params.getStartDate().substring(0,4);
        month = params.getStartDate().substring(4,6);
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        return null;
    }
    //数据处理
    public Map<String,Object> handleDate(ProductParams params){
        List<Map<String,Object>> RegionList = new ArrayList<>();
        List<Map<String,Object>> SingleList = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        //全区站点
        List<String> stas = RegionEnum.getStas("1");
        String stationNos = String.join(",", stas);
        for (String element : elements) {
            params.setElement(element);
            params.setCal("AVG");
            if(element.contains("PRE") && element.contains("SSH")){
                params.setCal("SUM");
            }
            for (String date : dates) {//时间段（全月/上旬/中旬/下旬）
                Map<String,Object> QXMap = new HashMap<>();
                String dateName = date.split("_")[2];
                params.setStartDate(month+date.split("_")[0]);
                params.setEndDate(month+date.split("_")[1]);
                baseData = mapper.periodsList(params);
                currentList = baseData.stream().filter(x->x.getYear().equals(year)).collect(Collectors.toList());
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
                QXMap.put(element+"QXliveVal",QXliveVal);
                QXMap.put(element+"QXperenVal",QXperenVal);
                QXMap.put(element+"QXannomlyVal",QXannomlyVal);
                RegionList.add(QXMap);

                //单站数据处理
                for (String sta : stas) {
                    Map<String,Object> SingleMap = new HashMap<>();
                    Product product = currentList.stream().filter(x -> StringUtils.equals(sta, x.getStationNo())).collect(Collectors.toList()).get(0);
                    List<Product> SingleCurrentList = currentList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo())).collect(Collectors.toList());
                    double liveVal = ReflectHandleUtils.getValByOp(SingleCurrentList, "val", "avg");
                    List<Product> SinglePerenList = perenList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo())).collect(Collectors.toList());
                    double perenVal = ReflectHandleUtils.getValByOp(SinglePerenList, "val", "avg");
                    double annomlyVal = liveVal - perenVal;
                    if (element.contains("PRE"))annomlyVal = annomlyVal/perenVal*100;
                    SingleMap.put("stationNo",sta);
                    SingleMap.put("dateName", dateName);
                    SingleMap.put("stationName",product.getStationName());
                    SingleMap.put("longitude",product.getLongitude());
                    SingleMap.put("latitude",product.getLatitude());
                    SingleMap.put(element+"liveVal",liveVal);
                    SingleMap.put(element+"perenVal",perenVal);
                    SingleMap.put(element+"annomlyVal",annomlyVal);
                    SingleList.add(SingleMap);
                }
            }
        }
        List<Map<String,Object>> Regions = new ArrayList<>();
        List<Map<String,Object>> Singles = new ArrayList<>();
        for (Map<String, Object> map1 : RegionList) {
            for (Map<String, Object> map2 : RegionList) {
                if (StringUtils.equals(map1.get("stationName").toString(),map2.get("stationName").toString()) && StringUtils.equals(map1.get("dateName").toString(),map2.get("dateName").toString())){
                    map1.putAll(map2);
                    Regions.add(map1);
                }
            }
        }
        for (Map<String, Object> map1 : SingleList) {
            for (Map<String, Object> map2 : SingleList) {
                if (StringUtils.equals(map1.get("stationName").toString(),map2.get("stationName").toString()) && StringUtils.equals(map1.get("dateName").toString(),map2.get("dateName").toString())){
                    map1.putAll(map2);
                    Singles.add(map1);
                }
            }
        }
        Regions = Regions.stream().distinct().collect(Collectors.toList());
        Singles = Singles.stream().distinct().collect(Collectors.toList());
        result.put("region",Regions);
        result.put("single",Singles);
        return result;
    }

    public Map<String,Object> contentData(Map<String,Object> map){


        return null;
    }

}
