package com.dafang.monitor.nx.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.CommonVal;
import com.dafang.monitor.nx.statistics.entity.vo.ComprehensiveExtrem;
import com.dafang.monitor.nx.statistics.mapper.NumericalMapper;
import com.dafang.monitor.nx.statistics.service.NumericalService;
import com.dafang.monitor.nx.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/4/2
 * @version: 1.0
 */
@Service
public class NumericalServiceImpl implements NumericalService {
    @Autowired
    private NumericalMapper mapper;

    @Override
    public List<Map<String, Object>> comprehensiveStatistic(DailyParam params) {
        List<Map<String, Object>> resList = new ArrayList<>();
        // 获取数据
        List<Map<String, Object>> datas = mapper.continuousByElement(params);
        List<String> staList = datas.stream().map(x -> Convert.toStr(x.get("stationNo"))).distinct().collect(Collectors.toList());
        List<String> elements = Convert.toList(String.class, params.getElement());
        for (String stationNo : staList) {
            Map<String, Object> map = new HashMap<>();
            List<Map<String, Object>> singleList = datas.stream().filter(x -> StringUtils.equals(
                    stationNo, x.get("stationNo").toString())).collect(Collectors.toList());
            map.put("stationNo", stationNo);
            map.put("stationName", singleList.get(0).get("stationName"));
            for (String element : elements) {// 得到每个要素对应处理得到的值
                double val = CommonUtils.getValByOp(singleList, element, params.getOpType());
                map.put(element, val);
            }
            resList.add(map);
        }
        return resList;
    }

    @Override
    public List<ComprehensiveExtrem> extrem(DailyParam params) {
        List<ComprehensiveExtrem> resultList = new ArrayList<>();
        int startYear = Convert.toInt(params.getStartDate().substring(0, 4));
        int endYear = Convert.toInt(params.getEndDate().substring(0, 4));
        String[] scales = params.getClimateScale().split("-");// 常年值区间段
        // 获取数据
        List<Daily> periodList = mapper.periodList(params);
        List<String> staList = periodList.parallelStream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : staList) {
            ComprehensiveExtrem.ComprehensiveExtremBuilder comprehensiveBuilder = ComprehensiveExtrem.builder();
            List<Map<String, CommonVal>> middleList = new ArrayList<>();
            // 得到单个站点的所有数据
            List<Daily> singleList = periodList.parallelStream().filter(
                    x -> StringUtils.equals(stationNo, x.getStationNo())).collect(Collectors.toList());
            comprehensiveBuilder.stationNo(stationNo).stationName(singleList.get(0).getStationName());
            // 得到每一年的数据
            List<Map<String , Double>> yearDatas = new ArrayList<>();
            List<Integer> yearList = singleList.stream().map(x -> x.getYear()).distinct().collect(Collectors.toList());
            for (Integer year : yearList) {
                Map<String, Double> yearMap = new HashMap<>();
                List<Daily> collect = singleList.stream().filter(x -> x.getYear() == year).collect(Collectors.toList());
                if (collect.size() == 0){// 如果该年份没有数据不做处理
                    continue;
                }
                DoubleSummaryStatistics summaryStatistics = collect.stream().mapToDouble(x -> x.getElement()).summaryStatistics();
                double val = summaryStatistics.getAverage();
                if (StringUtils.containsIgnoreCase(params.getElement(),"pre") || StringUtils.containsIgnoreCase(params.getElement(),"pre")){
                    val = summaryStatistics.getSum();
                }
                yearMap.put("year", year*1.0);
                yearMap.put("val", val);
                yearDatas.add(yearMap);
            }
            // 得到查询时间同期内的最大值和最小值
            List<Map<String, Double>> selList = yearDatas.stream().filter(x -> x.get("year") >= startYear
                    && x.get("year") <= endYear).collect(Collectors.toList());
            if (selList.size()>0){
                DoubleSummaryStatistics statistics = selList.stream().mapToDouble(x -> x.get("val")).summaryStatistics();
                comprehensiveBuilder.min(statistics.getMin()).max(statistics.getMax());
            }
            // 得到极大值和极小值以及对应发生的年份
            DoubleSummaryStatistics statistics = yearDatas.stream().mapToDouble(x -> x.get("val")).summaryStatistics();
                double minExtrem = statistics.getMin();
                double maxExtrem = statistics.getMax();
                String minExtremTime = yearDatas.stream().filter(x -> x.get("val") == minExtrem).map(x -> x.get("year").toString()).collect(Collectors.joining(","));
                String maxExtremTime = yearDatas.stream().filter(x -> x.get("val") == maxExtrem).map(x -> x.get("year").toString()).collect(Collectors.joining(","));
                comprehensiveBuilder.maxExtrem(maxExtrem).maxExtremTime(maxExtremTime).minExtrem(minExtrem).minExtremTime(minExtremTime);
            // 常年值数据处理
            Double perenVal = null;
            List<Map<String, Double>> perenList = yearDatas.stream().filter(x -> x.get("year") >= Convert.toInt(scales[0])
                    && x.get("year") <= Convert.toInt(scales[1])).collect(Collectors.toList());
            if (perenList.size() > 0){
                 perenVal = perenList.stream().mapToDouble(x -> x.get("val")).average().getAsDouble();
            }
            // 查询年份数据统计
            for (int i = startYear;i <= endYear;i++){
                Map<String, CommonVal> resMap = new HashMap<>();
                int year = i;
                String key = year + "";
                CommonVal.CommonValBuilder builder = CommonVal.builder();
                Optional<Map<String, Double>> currOp = yearDatas.stream().filter(x -> x.get("year") == year).findFirst();
                if (currOp.isPresent()){
                    Map<String, Double> map = currOp.get();
                    double liveVal = map.get("val");
                    builder.liveVal(liveVal);
                    if (perenVal != null){
                        double anomalyVal = liveVal - perenVal;
                        builder.perenVal(perenVal).anomalyVal(anomalyVal);
                    }
                   // 计算排位
                    List<Double> valList = yearDatas.stream().map(x -> x.get("val")).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                    if (StringUtils.containsIgnoreCase(params.getElement(),"min")){// 当查询如低温的情况  排序应该按照越小排位越高的策略
                        valList.sort(Comparator.naturalOrder());
                    }
                    int rank = valList.indexOf(liveVal) + 1;
                    builder.rank(rank);
                    // 如果款年key应该为2018-2019这样的格式
                    if (StringUtils.compare(params.getST(),params.getET())>0){
                        key = year + "-" + (i + 1);
                    }
                }
                resMap.put(key, builder.build());
                middleList.add(resMap);
            }
            comprehensiveBuilder.data(middleList);
            resultList.add(comprehensiveBuilder.build());
        }
        return resultList;
    }

}
