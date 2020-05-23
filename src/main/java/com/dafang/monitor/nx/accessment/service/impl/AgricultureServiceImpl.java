package com.dafang.monitor.nx.accessment.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.accessment.entity.po.AccumuTem;
import com.dafang.monitor.nx.accessment.entity.po.AccumuTemParam;
import com.dafang.monitor.nx.accessment.entity.po.Agriculture;
import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;
import com.dafang.monitor.nx.accessment.mapper.AgricultureMapper;
import com.dafang.monitor.nx.accessment.service.AgricultureService;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AgricultureServiceImpl implements AgricultureService {

    @Autowired
    AgricultureMapper mapper;

    @Override
    public List<Map<String, Object>> dataList(AgricultureParam params) {
        List<Map<String,Object>> results = new ArrayList<>();
        List<Agriculture> baseData = mapper.baseDataList(params);
        Integer startYear = Convert.toInt(params.getStartYear());
        Integer endYear = Convert.toInt(params.getEndYear());
        String[] scale = params.getClimateScale().split("-");
        int scaleLen = Convert.toInt(scale[1])-Convert.toInt(scale[0])+1;
        List<String> stationNos = baseData.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            List<Agriculture> singleList = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
            String stationName = singleList.get(0).getStationName();
            // 得到常年值数据
            List<Agriculture> perenList = singleList.stream().filter(x -> x.getYear() >= Convert.toInt(scale[0])
                    && x.getYear() <= Convert.toInt(scale[1])).collect(Collectors.toList());
            for(int i = startYear; i <= endYear; i++){
                Map<String,Object> map = new HashMap<>();
                int year = i;
                // 得到当前年份的数据
                List<Agriculture> currentList = singleList.stream().filter(x -> x.getYear() ==year).collect(Collectors.toList());
                String[] elements = {"temAvg_avg","temMax_avg","temMin_avg","ssh_sum","pre_sum"};
                if(currentList.size()>0){
                    for (String element : elements) {
                        currentList = ReflectHandleUtils.filterData(currentList, element.split("_")[0]);
                        perenList = ReflectHandleUtils.filterData(perenList, element.split("_")[0]);
                        handle(currentList,perenList,element.split("_")[0],element.split("_")[1],scaleLen,map);
                    }
                    //高温日数
                    long highTemDays = currentList.stream().filter(x -> Convert.toDouble(x.getTemMax()) > 35).count();
                    //霜冻日数
                    long frostDays = currentList.stream().filter(x -> Convert.toDouble(x.getTemMin()) < 0).count();
                    map.put("highTemDays",highTemDays);
                    map.put("frostDays",frostDays);
                    map.put("year",year);
                    map.put("stationNo",stationNo);
                    map.put("stationName",stationName);
                    results.add(map);
                }
            }
        }
        return results;
    }

    /**
     *气候与农业-数据处理
     * @param currentList 当前年数据集合
     * @param perenList 常年值数据集合
     * @param element 要素
     * @param op 计算类型
     * @param scaleLen 常年值长度
     * @param map
     */
    public void handle(List<Agriculture> currentList,List<Agriculture> perenList,String element,String op, int scaleLen,Map<String,Object> map){
        // 得到实况值
        double liveVal = ReflectHandleUtils.getValByOp(currentList, element, op);
        double perenVal =  ReflectHandleUtils.getValByOp(perenList, element, op);
        // 求常年值
        if (StringUtils.equals(op,"sum")){
            perenVal /= scaleLen;
        }
        double anomalyVal = liveVal - perenVal;
        map.put(element + "_liveVal", NumberUtil.round(liveVal,1));
        map.put(element + "_perenVal", NumberUtil.round(perenVal,1));
        map.put(element + "_anomalyVal", NumberUtil.round(liveVal,1));
    }

    /**
     * 同期积温
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> periodList(AccumuTemParam params) {
        List<Map<String, Object>> results = new ArrayList<>();
        List<AccumuTem> baseData = mapper.periodList(params);
        int startYear = Convert.toInt(params.getStartDate().substring(0, 4));
        int endtYear = Convert.toInt(params.getEndDate().substring(0, 4));
        String[] scale = params.getClimateScale().split("-");
        int scaleLen = Convert.toInt(scale[1]) - Convert.toInt(scale[0]) + 1;
        List<String> stationNos = baseData.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            //获取单站数据
            List<AccumuTem> singleList = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
            String stationName = singleList.get(0).getStationName();
            //常年值
            double perenVal = singleList.stream().filter(x -> x.getYear() >= Convert.toInt(scale[0]) && x.getYear() <= Convert.toInt(scale[1])).
                    mapToDouble(x -> Convert.toDouble(x.getTemAvg())).summaryStatistics().getAverage();
            //历史同期最大值
            double historyExtrem = singleList.stream().mapToDouble(x -> Convert.toDouble(x.getTemAvg())).summaryStatistics().getMax();
            //历史同期最大值对应年份
            String historyExtremYear = singleList.stream().filter(x -> Convert.toDouble(x.getTemAvg()) == historyExtrem).
                    map(x -> x.getYear().toString()).collect(Collectors.joining(","));
            List<Double> collect = singleList.stream().map(x -> Convert.toDouble(x.getTemAvg())).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            for (int i = startYear; i <= endtYear; i++){
                Map<String,Object> map = new HashMap<>();
                int year = i;
                //实况值
                Double liveVal = singleList.stream().filter(x -> x.getYear() == year).
                        map(x -> Convert.toDouble(x.getTemAvg())).collect(Collectors.toList()).get(0);
                double annolyVal = liveVal - perenVal;
                //排位
                int sort = collect.indexOf(liveVal)+1;
                map.put("stationNo", stationNo);
                map.put("stationName", stationName);
                map.put("liveVal", NumberUtil.round(liveVal,1));
                map.put("perenVal", NumberUtil.round(perenVal,1));
                map.put("annolyVal", NumberUtil.round(annolyVal,1));
                map.put("historyExtrem", historyExtrem);
                map.put("historyExtremYear", historyExtremYear);
                map.put("sort", sort);
                results.add(map);
            }
        }
        return results;
    }

    /**
     * 连续积温
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> continueList(AccumuTemParam params) {
        List<Map<String,Object>> results = new ArrayList<>();
        List<AccumuTem> baseData = mapper.continueList(params);
        List<String> stationNos = baseData.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            Map<String,Object> map = new HashMap<>();
            List<AccumuTem> singleList = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
            AccumuTem accumuTem = singleList.get(0);
            map.put("stationNo",stationNo);
            map.put("stationName",accumuTem.getStationName());
            map.put("liveVal",NumberUtil.round(accumuTem.getTemAvg(),1));
            results.add(map);
        }
        return results;
    }
}
