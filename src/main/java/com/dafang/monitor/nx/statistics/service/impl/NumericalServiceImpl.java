package com.dafang.monitor.nx.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.*;
import com.dafang.monitor.nx.statistics.mapper.NumericalMapper;
import com.dafang.monitor.nx.statistics.service.NumericalService;
import com.dafang.monitor.nx.utils.CommonUtils;
import com.dafang.monitor.nx.utils.LocalDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                    List<Double> valList = yearDatas.stream().filter(x->x.get("year")>=params.getRankStartYear()
                             && x.get("year")<=params.getRankEndYear()).map(x -> x.get("val")).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
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

    @Override
    public List<FirstDays>  firstDays(DailyParam params) {
        List<FirstDays> resList = new ArrayList<>();// 返回值
        // 默认常年值对应的天数转成月日以当前年份为准
        LocalDate now = LocalDate.now();
        int startYear = Convert.toInt(params.getStartDate().substring(0, 4));
        int endYear = Convert.toInt(params.getEndDate().substring(0, 4));
        String[] scales = params.getClimateScale().split("-");// 常年值区间段
        int scaleLen = Convert.toInt(scales[1]) -  Convert.toInt(scales[0]) + 1;
        // 获取数据
        List<Daily> periodList = mapper.periodList(params);
        List<String> staList = periodList.parallelStream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : staList) {
            FirstDays.FirstDaysBuilder firBuilder = FirstDays.builder();
            // 得到单个站点的所有数据
            List<Daily> singleList = periodList.parallelStream().filter(
                    x -> StringUtils.equals(stationNo, x.getStationNo())).collect(Collectors.toList());
            Daily daily = singleList.get(0);
            firBuilder.stationNo(stationNo).stationName(daily.getStationName());
            // 得到单个站点历年所有的初日终日时间以及对应的天数
            List<Map<String, String>> yearDatas = firstDaysHandle(singleList);
            // 得到初日和终日常年值
            List<Map<String, String>> perenList = yearDatas.stream().filter(x -> StringUtils.compare(x.get("year"), scales[0]) >= 0
                    && StringUtils.compare(x.get("year"), scales[0]) <= 1).collect(Collectors.toList());
            int firstPerenDays = Convert.toInt(perenList.stream().mapToDouble(x -> Convert.toDouble(x.get("firstDays"))).average().getAsDouble()+0.5);// 初日对应的天数
            int endPerenDays = Convert.toInt(perenList.stream().mapToDouble(x -> Convert.toDouble(x.get("endDays"))).average().getAsDouble()+0.5);
            // 因为hutool的Convert.toInt()是向下取整,所以加上0.5相当于四舍五入的功能
            String firstPerenDay = LocalDateUtils.getMDDesc(now.withDayOfYear(firstPerenDays));
            String endPerenDay = LocalDateUtils.getMDDesc(now.withDayOfYear(endPerenDays));
            firBuilder.firstPerenDay(firstPerenDay).endPerenDay(endPerenDay);
            // 查询年份数据统计
            List<Map<String, FirstDayCommonVal>> middleList = new ArrayList<>();
            for (int i = startYear;i <= endYear;i++){
                Map<String, FirstDayCommonVal> resMap = new HashMap<>();
                int year = i;
                String key = year + "";
                FirstDayCommonVal.FirstDayCommonValBuilder builder = FirstDayCommonVal.builder();
                Optional<Map<String, String>> currOp = yearDatas.stream().filter(x -> Convert.toInt(x.get("year")) == year).findFirst();
                if (currOp.isPresent()){
                    Map<String, String> map = currOp.get();
                    // 得到初日和终日实况值
                    Integer firstLiveDays = Convert.toInt(map.get("firstDays"), null);
                    Integer endLiveDays = Convert.toInt(map.get("endDays"), null);

                    int firstAnomalyVal = firstPerenDays - firstLiveDays;
                    int endAnomalyVal = endPerenDays - endLiveDays;
                    builder.firstLiveVal(map.get("firstDate")).endLiveVal(map.get("endDate")).firstPerenVal(firstPerenDay)
                            .endPerenVal(endPerenDay).firstAnomalyVal(firstAnomalyVal).endAnomalyVal(endAnomalyVal);
                    // 计算排位
                    List<Integer> firstList = yearDatas.stream().filter(x->Convert.toInt(x.get("year"))>=params.getRankStartYear()
                            && Convert.toInt(x.get("year"))<=params.getRankEndYear()).map(x -> Convert.toInt(x.get("firstDays"))).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                    List<Integer> endList = yearDatas.stream().filter(x->Convert.toInt(x.get("year"))>=params.getRankStartYear()
                            && Convert.toInt(x.get("year"))<=params.getRankEndYear()).map(x -> Convert.toInt(x.get("endDays"))).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                    int firstRank = firstList.indexOf(firstLiveDays) + 1;
                    int endRank = endList.indexOf(endLiveDays) + 1;
                    builder.firstRank(firstRank).endRank(endRank);
                    // 如果款年key应该为2018-2019这样的格式
                    if (StringUtils.compare(params.getST(),params.getET())>0){
                        key = year + "-" + (i + 1);
                    }
                }
                resMap.put(key, builder.build());
                middleList.add(resMap);
            }
            firBuilder.data(middleList);
            resList.add(firBuilder.build());
        }

        return resList;
    }
    // 得到单个站点历年所有的初日终日时间以及对应的天数
    public List<Map<String,String>> firstDaysHandle(List<Daily> singleList){
        List<Map<String,String>> resList = new ArrayList<>();
        Map<Integer, List<Daily>> collect = singleList.stream().collect(Collectors.groupingBy(Daily::getYear));
        for (Integer key : collect.keySet()) {
            Map<String,String> map = new HashMap<>();
            List<Daily> singleYearList = collect.get(key);
            IntSummaryStatistics summaryStatistics = singleYearList.stream().mapToInt(x -> Convert.toInt(x.getObserverTime().toString().replace("-",""))).summaryStatistics();
            String firstDate = summaryStatistics.getMin()+"";
            String endDate = summaryStatistics.getMax()+"";
            // 得到初终日对应的天数
            int firstDays = LocalDateUtils.stringToDate(firstDate).getDayOfYear();
            int endDays = LocalDateUtils.stringToDate(endDate).getDayOfYear();
            map.put("year", key+"");
            map.put("firstDate", LocalDateUtils.getMDDesc(firstDate));// 存储月日
            map.put("endDate",  LocalDateUtils.getMDDesc(endDate));
            map.put("firstDays", firstDays+"");
            map.put("endDays", endDays+"");
            resList.add(map);
        }
        return resList;
    }
}
