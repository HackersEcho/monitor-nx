package com.dafang.monitor.nx.product.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.accessment.entity.emun.RegionEnum;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExtremWeatherEvent extends TemplateAbstract {

    @Autowired
    private ProductMapper mapper;
    String[] elements = {"TEM_Avg-avg","PRE_Time_2020-sum"};
    List<String> QXstas = RegionStaEnum.getStas("1");//全区站点
    int QXstasLen = QXstas.size();
    @Override
    protected void init(ProductParams params) {

        startData = params.getStartDate();
        year = params.getStartDate().substring(0,4);
        //同期月数据
        baseData = mapper.periodList(params);
        //当年数据
        currentList = baseData.stream().filter(x -> StringUtils.equals(x.getYear().toString(), year)).collect(Collectors.toList());
        //常年值数据
        perenList = baseData.stream().filter(x->Convert.toDouble(x.getYear()) >= 1981 && Convert.toDouble(x.getYear()) <= 2010).collect(Collectors.toList());

    }

    @Override
    protected Map<String, Object> getDatas() {
        return null;
    }
    /*
    处理内容拼接数据
     */
    public Map<String,Object> handleData(){
        Map<String,Object> map = new HashMap<>();
        String stationNos = QXstas.stream().collect(Collectors.joining("、"));
        List<Product> currentQXList = currentList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo())).collect(Collectors.toList());
        List<Product> perenQXList = perenList.stream().filter(x -> StringUtils.contains(stationNos, x.getStationNo())).collect(Collectors.toList());
        for (String element : elements) {
            String ele = element.split("-")[0];
            String op = element.split("-")[1];
            double QXLiveVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(currentQXList, ele), ele, op);//全区实况值
            double QXPerenVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(perenQXList, ele), ele, op);//全区常年值
            double QXannomlyVal = QXLiveVal - QXPerenVal;//全区距平
            if(StringUtils.contains(element,"PRE")){
                QXLiveVal = QXLiveVal/QXstasLen;
                QXPerenVal = QXPerenVal/QXstasLen/30;
                QXannomlyVal = (QXLiveVal - QXPerenVal)/QXPerenVal*100;
            }
            for (String station : QXstas) {
                List<Product> singleList = ReflectHandleUtils.filterData(baseData, ele).stream().filter(x -> StringUtils.equals(x.getStationNo(), station)).collect(Collectors.toList());


            }
        }


        return map;
    }

}
