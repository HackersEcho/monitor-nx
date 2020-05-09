package com.dafang.monitor.nx.event.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.event.entity.po.SeasonParam;
import com.dafang.monitor.nx.event.entity.vo.EventDaily;
import com.dafang.monitor.nx.event.entity.vo.EventVal;
import com.dafang.monitor.nx.event.entity.vo.FourSeason;
import com.dafang.monitor.nx.event.mapper.SeasonMapper;
import com.dafang.monitor.nx.event.service.SeasonService;
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
 * @createDate: 2020/4/23
 * @version: 1.0
 */
@Service
public class SeasonServiceImpl implements SeasonService {
    @Autowired
    private SeasonMapper mapper;
    @Override
    public List<FourSeason> inOutSeason(SeasonParam params) {
        List<FourSeason> resList = new ArrayList<>();// 返回值
        int startYear = Convert.toInt(params.getStartYear());
        int endYear = Convert.toInt(params.getEndYear());
        String[] scales = params.getClimateScale().split("-");// 常年值区间段
        int scaleLen = Convert.toInt(scales[1]) -  Convert.toInt(scales[0]) + 1;
        // 获取数据
        List<EventDaily> seasonDatas = mapper.seasonData(params);
        List<String> staList = seasonDatas.parallelStream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : staList) {
            FourSeason.FourSeasonBuilder seasonBuilder = FourSeason.builder();
            // 得到单个站点的所有数据
            List<EventDaily> singleList = seasonDatas.parallelStream().filter(
                    x -> StringUtils.equals(stationNo, x.getStationNo())).collect(Collectors.toList());
            seasonBuilder.stationNo(stationNo).stationName(singleList.get(0).getStationName());
            // 对每条数据增加一个日期对应的天数
            for (EventDaily eventDaily : singleList) {
                int days = LocalDateUtils.getNumberOfDays(eventDaily.getObserverTime());
                eventDaily.setDays(days);
            }
            // 常年值数据处理
            List<EventDaily> perenList = singleList.stream().filter(x -> x.getYear() >= Convert.toInt(scales[0])
                    && x.getYear() <= Convert.toInt(scales[1])).collect(Collectors.toList());
            double asDouble = perenList.stream().mapToDouble(x -> x.getDays()).average().getAsDouble();
            int perenVal = Convert.toInt(asDouble);
            // 得到常年值月日
            String perenMd = LocalDate.now().withDayOfYear(perenVal).toString().replace("-", "").substring(4);
            // 得到当前站点历年对应天数集合并按从小到大的情况进行
            List<Integer> daysList = singleList.stream().map(x -> x.getDays()).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            // 得到最早和最晚发生时间
            IntSummaryStatistics intSummaryStatistics = daysList.stream().mapToInt(x -> x).summaryStatistics();
            int minDays = intSummaryStatistics.getMin();
            int maxDays = intSummaryStatistics.getMax();
            String minDaysTime = singleList.stream().filter(x -> x.getDays() == minDays).map(
                    x -> x.getObserverTime().toString().replace("-","")).collect(Collectors.joining(","));
            String maxDaysTime = singleList.stream().filter(x -> x.getDays() == maxDays).map(
                    x -> x.getObserverTime().toString().replace("-","")).collect(Collectors.joining(","));
            seasonBuilder.perenVal(perenMd).minDays(minDays).maxDays(maxDays).maxDaysTime(maxDaysTime).minDaysTime(minDaysTime);
            // 查询年份数据
            List<Map<String, EventVal>> middleList = new ArrayList<>();
            for (int i=startYear;i<=endYear;i++){
                Map<String, EventVal> resMap = new HashMap<>();
                int year = i;
                String key = year + "";
                EventVal.EventValBuilder builder = EventVal.builder();
                Optional<EventDaily> currOp = singleList.stream().filter(x -> x.getYear() == year).findFirst();
                if (currOp.isPresent()){
                    EventDaily eventDaily = currOp.get();
                    int days = eventDaily.getDays();
                    String liveVal = eventDaily.getMd();
                    double anomalyVal = LocalDateUtils.getNumberOfDays(eventDaily.getObserverTime()) - perenVal;
                    int rank = daysList.indexOf(days) + 1;
                    builder.liveVal(liveVal).perenVal(perenMd).anomalyVal(anomalyVal).rank(rank);
                }
                resMap.put(key, builder.build());
                middleList.add(resMap);
            }
            seasonBuilder.data(middleList);
            resList.add(seasonBuilder.build());
        }
        return resList;
    }

    @Override
    public List<FourSeason> seasonLen(SeasonParam params) {
        List<FourSeason> resList = new ArrayList<>();// 返回值
        int startYear = Convert.toInt(params.getStartYear());
        int endYear = Convert.toInt(params.getEndYear());
        String[] scales = params.getClimateScale().split("-");// 常年值区间段
        int scaleLen = Convert.toInt(scales[1]) -  Convert.toInt(scales[0]) + 1;
        // 获取数据
        List<EventDaily> seasonDatas = mapper.seasonLen(params);
        List<String> staList = seasonDatas.parallelStream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : staList) {
            FourSeason.FourSeasonBuilder seasonBuilder = FourSeason.builder();
            // 得到单个站点的所有数据
            List<EventDaily> singleList = seasonDatas.parallelStream().filter(
                    x -> StringUtils.equals(stationNo, x.getStationNo())).collect(Collectors.toList());
            seasonBuilder.stationNo(stationNo).stationName(singleList.get(0).getStationName());
            // 得到所有年份
            List<Integer> yearList = singleList.stream().map(x -> x.getYear()).distinct().collect(Collectors.toList());
            // 对每条数据增加一个日期入季的长度
            List<EventDaily> lenList = new ArrayList<>();
            for (Integer year : yearList) {
                int years = year;
                List<EventDaily> singleYearList = singleList.stream().filter(x -> x.getYear() == years).collect(Collectors.toList());
                Optional<EventDaily> inOp = singleYearList.stream().filter(x -> x.getTimeType() == 1).findFirst();
                Optional<EventDaily> outOp = singleYearList.stream().filter(x -> x.getTimeType() == 2).findFirst();
                if (inOp.isPresent() && outOp.isPresent()){
                    EventDaily eventDaily1 = inOp.get();
                    EventDaily eventDaily2 = outOp.get();
                    long len = LocalDateUtils.getDifferDays(eventDaily1.getObserverTime(), eventDaily2.getObserverTime());
                    eventDaily1.setDays(Convert.toInt(len));
                    lenList.add(eventDaily1);
                }
            }
            // 常年值数据处理
            List<EventDaily> perenList = lenList.stream().filter(x -> x.getYear() >= Convert.toInt(scales[0])
                    && x.getYear() <= Convert.toInt(scales[1])).collect(Collectors.toList());
            double perenVal = perenList.stream().mapToDouble(x -> x.getDays()).average().getAsDouble();
            // 得到当前站点历年对应天数集合并按从小到大的情况进行
            List<Integer> daysList = lenList.stream().map(x -> x.getDays()).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            // 得到最早和最晚发生时间
            IntSummaryStatistics intSummaryStatistics = daysList.stream().mapToInt(x -> x).summaryStatistics();
            int minDays = intSummaryStatistics.getMin();
            int maxDays = intSummaryStatistics.getMax();
            String minDaysTime = lenList.stream().filter(x -> x.getDays() == minDays).map(
                    x -> x.getYear().toString()).collect(Collectors.joining(","));
            String maxDaysTime = lenList.stream().filter(x -> x.getDays() == maxDays).map(
                    x -> x.getYear().toString()).collect(Collectors.joining(","));
            seasonBuilder.perenVal(perenVal+"").minDays(minDays).maxDays(maxDays).maxDaysTime(maxDaysTime).minDaysTime(minDaysTime);
            // 查询年份数据
            List<Map<String, EventVal>> middleList = new ArrayList<>();
            for (int i=startYear;i<=endYear;i++){
                Map<String, EventVal> resMap = new HashMap<>();
                int year = i;
                String key = year + "";
                EventVal.EventValBuilder builder = EventVal.builder();
                Optional<EventDaily> currOp = lenList.stream().filter(x -> x.getYear() == year).findFirst();
                if (currOp.isPresent()){
                    EventDaily eventDaily = currOp.get();
                    int liveVal = eventDaily.getDays();
                    double anomalyVal = liveVal - perenVal;
                    int rank = daysList.indexOf(liveVal) + 1;
                    builder.liveVal(liveVal).perenVal(perenVal+"").anomalyVal(anomalyVal).rank(rank);
                }
                resMap.put(key, builder.build());
                middleList.add(resMap);
            }
            seasonBuilder.data(middleList);
            resList.add(seasonBuilder.build());
        }
        return resList;
    }
}
