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

import java.util.*;
import java.util.stream.Collectors;

public class ExtremWeatherEvent extends TemplateAbstract {

    @Autowired
    private ProductMapper mapper;
    String[] elements = {"TEM_Avg","PRE_Time_2020"};
    List<String> QXstas = RegionStaEnum.getStas("1");//全区站点
    int QXstasLen = QXstas.size();//全区站点数

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

    public Map<String,String> handleData(ProductParams params){
        Map<String,Object> map = new HashMap<>();
        params.setElement("t.TEM_Avg");
        for (String element : elements) {
            baseData = mapper.periodsList(params);
            //常年值数据
            List<Product> perenList = baseData.stream().filter(x -> Convert.toDouble(x.getYear()) >= 1981 && Convert.toDouble(x.getYear()) <= 2010).collect(Collectors.toList());
            //当年数据
            List<Product> currentList = baseData.stream().filter(x -> StringUtils.equals(x.getYear().toString(), year)).collect(Collectors.toList());

            //全区数据
            String QXstationNos = QXstas.stream().collect(Collectors.joining("、"));
            List<Product> QXCurrentList = currentList.stream().filter(x -> StringUtils.contains(QXstationNos, x.getStationNo())).collect(Collectors.toList());
            List<Product> QXPerenList = perenList.stream().filter(x -> StringUtils.contains(QXstationNos, x.getStationNo())).collect(Collectors.toList());
            double QXLiveVal = ReflectHandleUtils.getValByOp(QXCurrentList, "val", "avg");
            double QXPerenVal = ReflectHandleUtils.getValByOp(QXPerenList, "val", "avg");
            double QXAnnomlyVal = QXLiveVal - QXPerenVal;

            //全省各站点数据
            DoubleSummaryStatistics summary = currentList.stream().mapToDouble(x -> Convert.toDouble(x.getVal())).summaryStatistics();
            double max = summary.getMax();
            double min = summary.getMin();
            List<String> stationNos = currentList.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
            String rankStr = "";
            for (String stationNo : stationNos) {
                Product product = currentList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList()).get(0);
                Double liveVal = product.getVal();
                String stationName = product.getStationName();
                List<Product> singlePerenList = perenList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
                double perenVal = ReflectHandleUtils.getValByOp(singlePerenList, "val", "avg");
                double annomlyVal = liveVal - perenVal;
                String s1 = "";
                double staAnnMax = 0.0d;
                double staAnnMin = 0.0d;
                if(annomlyVal > 0){
                    s1 += stationName+"、";

                }
                int rank = 0;
                List<Double> liveList = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).map(x -> x.getVal()).collect(Collectors.toList());
                //历史排位
                for (Double liveValue : liveList) {
                    if(liveVal < liveValue){
                        rank++;
                    }
                }
                if(rank != 0 && rank <= 5){
                    rankStr += stationName+"为历史同期第"+rank+"位，";
                }

            }
        }


        return null;
    }

    /*
    处理内容拼接数据
     */
//    public Map<String,Object> handleData(){
//        Map<String,Object> map = new HashMap<>();
//        String QXstationNos = QXstas.stream().collect(Collectors.joining("、"));
//        List<Product> currentQXList = currentList.stream().filter(x -> StringUtils.contains(QXstationNos, x.getStationNo())).collect(Collectors.toList());
//        List<Product> perenQXList = perenList.stream().filter(x -> StringUtils.contains(QXstationNos, x.getStationNo())).collect(Collectors.toList());
//        //区域数据处理（全区）
//        for (String element : elements) {
//            String ele = element.split("-")[0];
//            String op = element.split("-")[1];
//            double QXLiveVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(currentQXList, ele), ele, op);//全区实况值
//            double QXPerenVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(perenQXList, ele), ele, op);//全区常年值
//            double QXannomlyVal = QXLiveVal - QXPerenVal;//全区距平
//            if(StringUtils.contains(element,"PRE")){
//                QXLiveVal = QXLiveVal/QXstasLen;
//                QXPerenVal = QXPerenVal/QXstasLen/30;
//                QXannomlyVal = (QXLiveVal - QXPerenVal)/QXPerenVal*100;
//            }
//            map.put(ele+"QXLiveVal",QXLiveVal);
//            map.put(ele+"QXannomlyVal",QXannomlyVal);
//        }
//        //全省各站数据处理
//        List<String> stationNos = baseData.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
//        for (String station : stationNos) {
//            List<Product> singleCurrentList = currentList.stream().filter(x -> StringUtils.equals(x.getStationNo(), station)).collect(Collectors.toList());
//            List<Product> singlePerenList = perenList.stream().filter(x -> StringUtils.equals(x.getStationNo(), station)).collect(Collectors.toList());
//            List<Product> singleList = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), station)).collect(Collectors.toList());
//            for (String element : elements) {
//                String ele = element.split("-")[0];
//                String op = element.split("-")[1];
//                double liveVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(singleCurrentList, ele), ele, op);
//                double perenVal = ReflectHandleUtils.getValByOp(ReflectHandleUtils.filterData(singlePerenList, ele), ele, op);
//                double annomlyVal = liveVal - perenVal;
//                if(StringUtils.contains(element,"PRE")){
//                    perenVal = perenVal/30;
//                    annomlyVal = (liveVal - perenVal)/perenVal*100;
//                }
//
//
//            }
//        }
//
//
//        return map;
//    }

}
