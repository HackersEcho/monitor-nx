package com.dafang.monitor.nx.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.CommonVal;
import com.dafang.monitor.nx.statistics.entity.vo.ContinuousDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;
import com.dafang.monitor.nx.statistics.mapper.DaysMapper;
import com.dafang.monitor.nx.statistics.service.DaysService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/12
 * @version: 1.0
 */
@Service
public class DaysServiceImpl implements DaysService {
    @Autowired
    private DaysMapper mapper;

    @Override
    public List<PeriodDays> periodDays(DailyParam params) {
        List<PeriodDays> resList = new ArrayList<>();
        int startYear = Convert.toInt(params.getStartDate().substring(0, 4));
        int endYear = Convert.toInt(params.getEndDate().substring(0, 4));
        String[] scales = params.getClimateScale().split("-");// 常年值区间段
        int scaleLen = Convert.toInt(scales[1]) -  Convert.toInt(scales[0]) +1;
        List<Daily> periodList = mapper.periodDays(params);
        List<String> staList = periodList.parallelStream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : staList) {
            // 得到单个站点的所有数据
            List<Daily> singleList = periodList.parallelStream().filter(
                    x -> StringUtils.equals(stationNo, x.getStationNo())).collect(Collectors.toList());
          // 得到该站每一个年份发生的天数
            if (singleList.size()>0){
                Daily daily = singleList.get(0);
                List<Double> daysList = singleList.stream().map(x -> x.getVal()).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                List<Double> rankList  = singleList.stream().filter(x -> x.getYear() >= params.getRankStartYear() && x.getYear() <= params.getRankEndYear())
                        .map(x -> x.getVal()).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                double maxDays = daysList.get(0);
                String maxDaysTime = singleList.stream().filter(x -> x.getVal() == maxDays).map(x -> x.getYear().toString()).collect(Collectors.joining("-"));
               // 得到常年天数并保留一位小数
                double perenVal =  singleList.stream().filter(x -> x.getYear() >= Convert.toInt(scales[0])
                       && x.getYear() <= Convert.toInt(scales[1])).mapToDouble(x -> x.getVal()).sum() / scaleLen;
                perenVal = NumberUtil.sub(perenVal, 0, 1).doubleValue();
                List<Map<String, CommonVal>> data = new ArrayList<>();
                for (int i = startYear;i<=endYear;i++){// 得到查询年份的
                    Map<String, CommonVal> map = new HashMap<>();
                    String key = i + "";
                    int year = i;
                    Optional<Daily> optional = singleList.stream().filter(x -> x.getYear() == year).findFirst();
                    CommonVal build = CommonVal.builder().build();
                    if (optional.isPresent()){
                        double liveVal = optional.get().getVal();
                        double anomalyVal = NumberUtil.sub(liveVal, perenVal, 1).doubleValue();
                        int rank = rankList.indexOf(liveVal) + 1;
                        build = CommonVal.builder().liveVal(liveVal).perenVal(perenVal).anomalyVal(anomalyVal).rank(rank).build();
                    }
                    // 如果款年key应该为2018-2019这样的格式
                    if (StringUtils.compare(params.getST(),params.getET())>0){
                        key = key + "-" + (i + 1);
                    }
                    map.put(key, build);
                    data.add(map);
                }
                PeriodDays periodDays = PeriodDays.builder().stationNo(stationNo).stationName(daily.getStationName()).latitude(daily.getLatitude())
                        .longitude(daily.getLongitude()).perenVal(perenVal).mostHistoryDay(maxDays).mostHistoryDate(maxDaysTime).data(data).build();
                resList.add(periodDays);
            }
        }
        return resList;
    }

    @Override
    public List<ContinuousDays> continuousDays(DailyParam params) {
        List<ContinuousDays> resList = new ArrayList<>();
        List<Daily> continuousDays = mapper.continuousDays(params);
        List<String> staList = continuousDays.parallelStream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        for (String stationNo : staList) {
            // 得到单个站点的所有数据
            Optional<Daily> first = continuousDays.parallelStream().filter(
                    x -> StringUtils.equals(stationNo, x.getStationNo())).findFirst();
            if (first.isPresent()){
                Daily daily = first.get();
                ContinuousDays build = ContinuousDays.builder().stationNo(stationNo).stationName(daily.getStationName()).latitude(daily.getLatitude())
                        .longitude(daily.getLongitude()).val(daily.getVal()).build();
                resList.add(build);
            }

        }
        return resList;
    }


}
