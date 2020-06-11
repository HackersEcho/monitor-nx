package com.dafang.monitor.nx.event.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.event.entity.po.TheFirstRainParam;
import com.dafang.monitor.nx.event.entity.vo.EventVal;
import com.dafang.monitor.nx.event.entity.vo.TheFirstRain;
import com.dafang.monitor.nx.event.mapper.TheFristRainMapper;
import com.dafang.monitor.nx.event.service.TheFristRainService;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.utils.DoubleMathUtil;
import com.dafang.monitor.nx.utils.LocalDateUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TheFristRainServiceImpl implements TheFristRainService {

    @Autowired
    private TheFristRainMapper theFristRainMapper;

    @Override
    public List<TheFirstRain> getSoakingPreStatistics(TheFirstRainParam theFirstRainParam) {
        List<TheFirstRain> resList = new ArrayList<>();// 返回值
        int startYear = Convert.toInt(theFirstRainParam.getStartYear());
        int endYear = Convert.toInt(theFirstRainParam.getEndYear());
        String[] scales = theFirstRainParam.getClimateScale().split("-");
        //查询数据
        List<Daily> firstRainList = theFristRainMapper.selectTheFirstRainRainfall(theFirstRainParam);
        //过滤站点编号集合
        List<String> stationNoList = firstRainList.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        //循环每一个站点编号
        for (String stationNo : stationNoList) {
            TheFirstRain.TheFirstRainBuilder firstRainBuilder = TheFirstRain.builder();

            //获取当前站点下的所有数据
            List<Daily> singleList = firstRainList.stream().filter(x ->
                    x.getStationNo().equals(stationNo)).collect(Collectors.toList());
            //获取常年值数据集合
            List<Daily> perennialValueList = singleList.stream().filter(x ->
                    StringUtils.compare(x.getYear().toString(), scales[0]) >= 0
                            && StringUtils.compare(x.getYear().toString(), scales[1]) <= 0).collect(Collectors.toList());
            //常年值
            double perennialValue = perennialValueList.stream().mapToDouble(x -> x.getVal()).average().getAsDouble();
            perennialValue = new BigDecimal(perennialValue).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            //取出集合中所有的实况值并且进行排序
            List<Double> liveValueList = singleList.stream().map(x -> x.getVal()).collect(Collectors.toList());
            liveValueList.sort(Comparator.reverseOrder());
            //取出历史最大值
            Double earlierValue = Collections.max(liveValueList);
            //取出历史最大值对应的时间
            String earlierValueTime = singleList.stream().filter(x -> x.getVal() == earlierValue).map(
                    x -> x.getYear().toString().replace("-", "")).collect(Collectors.joining(","));
            firstRainBuilder.stationNo(stationNo).stationName(singleList.get(0).getStationName()).
                    latitude(singleList.get(0).getLatitude()).longitude(singleList.get(0).
                    getLongitude()).earlierValue(earlierValue).earlierValueTime(earlierValueTime).
                    statisticsYear(singleList.get(0).getYear()).statisticsMonthDay(singleList.get(0).getMonthDay());
            //获取当前年份下所选的数据
            List<Map<String, EventVal>> middleList = new ArrayList<>();
            for (int i = startYear; i <= endYear; i++) {
                Map<String, EventVal> resMap = new HashMap<>();
                Double liveValue = 0.0;// 实况值
                Double anmolyValue = 0.0;//距平
                Integer historyQualifying = 0;//排位
                int year = i;
                String key = year + "";
                EventVal.EventValBuilder builder = EventVal.builder();
                Optional<Daily> firstList = singleList.stream().filter(x -> x.getYear() == year).findFirst();
                if (firstList.isPresent()) {
                    Daily daily = firstList.get();
                    //获取实况值
                    liveValue = daily.getVal();
                    historyQualifying = liveValueList.indexOf(liveValue) + 1;
                    anmolyValue = DoubleMathUtil.sub(liveValue, DoubleMathUtil.div(perennialValue, 1, 1), 1);
                    anmolyValue = DoubleMathUtil.div(anmolyValue, 1, 1);
                    builder.liveVal(liveValue).anomalyVal(anmolyValue).rank(historyQualifying).perenVal(perennialValue);

                    resMap.put(key, builder.build());
                    middleList.add(resMap);
                }


            }
            firstRainBuilder.data(middleList);
            resList.add(firstRainBuilder.build());
        }

        return resList;
    }

    @Override
    public List<TheFirstRain> getSoakingDateStatistics(TheFirstRainParam theFirstRainParam) {
        List<TheFirstRain> resList = new ArrayList<>();// 返回值
        int startYear = Convert.toInt(theFirstRainParam.getStartYear());
        int endYear = Convert.toInt(theFirstRainParam.getEndYear());
        String[] scales = theFirstRainParam.getClimateScale().split("-");
        //查询数据
        List<Daily> firstRainDateList = theFristRainMapper.selectTheFirstRainRainDate(theFirstRainParam);
        //站点编号去重
        List<String> stationNoList = firstRainDateList.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        //循环站点编号
        for (String stationNo : stationNoList) {
            TheFirstRain.TheFirstRainBuilder firstRainBuilder = TheFirstRain.builder();
            //拿到当前站点下的所有数据
            List<Daily> singleList = firstRainDateList.stream().
                    filter(x -> x.getStationNo().equals(stationNo)).collect(Collectors.toList());
            //获取常年值数据集合
            List<Daily> perennialValueList = singleList.stream().filter(x ->
                    StringUtils.compare(x.getYear().toString(), scales[0]) >= 0
                            && StringUtils.compare(x.getYear().toString(), scales[1]) <= 0).collect(Collectors.toList());
            // 常年值
            double num = perennialValueList.stream()
                    .mapToDouble(
                            x -> LocalDateUtils.stringToDate(x.getYear() + x.getMonthDay().toString()).getDayOfYear())
                    .average().getAsDouble();
            // 将常年值转化为月日
            int Days = (int) Math.round(num);
            String startDatePerennial = LocalDate.now().withDayOfYear(Days).toString().replace("-", "")
                    .substring(4);
            //历史最早出现时间
            List<Integer> allValues = singleList.stream().map(x -> Integer.parseInt(x.getMonthDay().toString())).collect(Collectors.toList());
            allValues.sort(Comparator.reverseOrder());
            int mxaMonthDay = Collections.max(allValues);
            List<Daily> maxCollect = singleList.stream().filter(x -> Integer.parseInt(x.getMonthDay().toString()) == mxaMonthDay).collect(Collectors.toList());
            //历史最晚出现时间
            int minMonthDay = Collections.min(allValues);
            List<Daily> minCollect = singleList.stream().filter(x -> Integer.parseInt(x.getMonthDay().toString()) == minMonthDay).collect(Collectors.toList());
            firstRainBuilder.stationNo(stationNo).stationName(singleList.get(0).getStationName()).
                    longitude(singleList.get(0).getLongitude()).latitude(singleList.get(0).getLatitude())
                    .minDate(minCollect.get(0).getYear().toString() + minCollect.get(0).getMonthDay())
                    .maxDate(maxCollect.get(0).getYear().toString() + maxCollect.get(0).getMonthDay());
            //获取当前年份下所选的数据
            List<Map<String, EventVal>> middleList = new ArrayList<>();
            for (int i = startYear; i <= endYear; i++) {
                Map<String, EventVal> resMap = new HashMap<>();
                int year = i;
                String key = year + "";
                EventVal.EventValBuilder builder = EventVal.builder();
                Optional<Daily> firstList = singleList.stream().filter(x -> x.getYear() == year).findFirst();
                if (firstList.isPresent()) {
                    Daily daily = firstList.get();
                    //获取实况值
                    Integer liveValue = Integer.parseInt(daily.getMonthDay().toString());
                    int historyQualifying = allValues.indexOf(Integer.parseInt(daily.getMonthDay().toString())) + 1;
                    Long anomaly = LocalDateUtils.getDifferDays(key + startDatePerennial, key + daily.getMonthDay().toString());

                    builder.liveVal(liveValue).anomalyVal(Convert.toDouble(anomaly)).rank(historyQualifying).perenVal(startDatePerennial);

                    resMap.put(key, builder.build());
                    middleList.add(resMap);
                }


            }
            firstRainBuilder.data(middleList);
            resList.add(firstRainBuilder.build());
        }
        return resList;
    }

    @Override
    public List<TheFirstRain> getSoakingStationsStatistics(TheFirstRainParam theFirstRainParam) {
        List<TheFirstRain> resList = new ArrayList<>();// 返回值
        //获取参数
        String[] stations = theFirstRainParam.getRegions().split(",");
        String startMonthDay = theFirstRainParam.getStartDate().replaceAll("-", "").substring(4, 8);
        String endMonthDay = theFirstRainParam.getEndDate().replaceAll("-", "").substring(4, 8);
        String[] scales = theFirstRainParam.getClimateScale().split("-");
        int startYear = Integer.valueOf(theFirstRainParam.getStartDate().replaceAll("-", "").substring(0, 4));
        int endYear = Integer.valueOf(theFirstRainParam.getEndDate().replaceAll("-", "").substring(0, 4));
        //查询站点区域信息
        List<Daily> stationGroupList = theFristRainMapper.selectStationGroup(stations);
        for (int i = 0; i < stations.length; i++) {
            String[] stationNo = stationGroupList.get(i).getStationIdArray().replaceAll("'","").split(",");
            TheFirstRain.TheFirstRainBuilder firstRainBuilder = TheFirstRain.builder();
            //查询改区域下的首场透雨数据集合
            List<Daily> firstRainDateList = theFristRainMapper.selectTheFirstRainStations(stationNo, startMonthDay, endMonthDay);
            //获取常年值数据
            List<Daily> perennialValueList = firstRainDateList.stream().filter(x ->
                    StringUtils.compare(x.getYear().toString(), scales[0]) >= 0
                            && StringUtils.compare(x.getYear().toString(), scales[1]) <= 0).collect(Collectors.toList());
            //常年值
            double perennialValue = perennialValueList.stream()
                    .mapToDouble(x -> x.getVal()).average().getAsDouble();
            perennialValue = new BigDecimal(perennialValue).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            //历史最大值
            List<Double> liveValueList = firstRainDateList.stream().map(x -> x.getVal()).collect(Collectors.toList());
            liveValueList.sort(Comparator.reverseOrder());
            //取出历史最大值
            Double earlierValue = Collections.max(liveValueList);
            //取出历史最大值对应的时间
            String earlierValueTime = firstRainDateList.stream().filter(x -> x.getVal() == earlierValue).map(
                    x -> x.getYear().toString().replace("-", "")).collect(Collectors.joining(","));
            firstRainBuilder.stationName(stationGroupList.get(i).getStationName()).earlierValue(earlierValue).earlierValueTime(earlierValueTime);
            //获取当前年份下所选的数据
            List<Map<String, EventVal>> middleList = new ArrayList<>();
            for (int k = startYear; k <= endYear; k++) {
                Map<String, EventVal> resMap = new HashMap<>();
                Double liveValue = 0.0;// 实况值
                Double anmolyValue = 0.0;//距平
                Integer historyQualifying = 0;//排位
                int year = k;
                String key = year + "";
                EventVal.EventValBuilder builder = EventVal.builder();
                Optional<Daily> firstList = firstRainDateList.stream().filter(x -> x.getYear() == year).findFirst();
                if (firstList.isPresent()) {
                    Daily daily = firstList.get();
                    //获取实况值
                    liveValue = daily.getVal();
                    historyQualifying = liveValueList.indexOf(liveValue) + 1;
                    anmolyValue = DoubleMathUtil.sub(liveValue, DoubleMathUtil.div(perennialValue, 1, 1), 1);
                    anmolyValue = DoubleMathUtil.div(anmolyValue, 1, 1);
                    builder.liveVal(liveValue).anomalyVal(anmolyValue).rank(historyQualifying).perenVal(perennialValue);

                    resMap.put(key, builder.build());
                    middleList.add(resMap);
                }
            }
            firstRainBuilder.data(middleList);
            resList.add(firstRainBuilder.build());
        }
        return resList;
    }


}
