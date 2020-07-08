package com.dafang.monitor.nx.event.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.event.entity.po.SnowfallParam;
import com.dafang.monitor.nx.event.entity.vo.EventVal;
import com.dafang.monitor.nx.event.entity.vo.SnowFall;
import com.dafang.monitor.nx.event.entity.vo.SnowFallDaily;
import com.dafang.monitor.nx.event.entity.vo.TheFirstRain;
import com.dafang.monitor.nx.event.mapper.SnowfallMapper;
import com.dafang.monitor.nx.event.service.SnowfallService;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.utils.LocalDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SnowfallServiceImpl implements SnowfallService {

    @Autowired
    private SnowfallMapper snowfallMapper;

    @Override
    public List<SnowFall> getSnowFallSDateStatisticsData(SnowfallParam snowfallParam) {
        List<SnowFall> resList = new ArrayList<>();// 返回值
        String[] scales = snowfallParam.getClimateScale().split("-");
        int startYear = Convert.toInt(snowfallParam.getStartYear());
        int endYear = Convert.toInt(snowfallParam.getEndYear());
        String type = snowfallParam.getType();
        //查询数据
        List<SnowFallDaily> snowFallList = snowfallMapper.selectSnowFallSDateStatisticsData(snowfallParam);
        //站点编号去重
        List<String> stationNoList = snowFallList.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        //循环站点编号
        for (String stationNo:stationNoList) {
            SnowFall.SnowFallBuilder snowFallBuilder = SnowFall.builder();
            //获取当前站点下的所有数据
            List<SnowFallDaily> singleList =null;
            //获取常年值区间数据
            List<SnowFallDaily> perennialValueList =null;
            String time = "";
            if(type.equals("1")){//降雪初日
                singleList =  snowFallList.stream()
                        .filter(x->x.getStationNo().equals(stationNo)
                                && x.getType().equals("1")).collect(Collectors.toList());
                perennialValueList = singleList.stream().filter(x ->
                        StringUtils.compare(x.getYear().toString(), scales[0]) >= 0
                                && StringUtils.compare(x.getYear().toString(), scales[1]) <= 0
                                && x.getType().equals("1")).collect(Collectors.toList());

            }else{//降雪终日
                singleList =  snowFallList.stream()
                        .filter(x->x.getStationNo().equals(stationNo)
                                && x.getType().equals("2")).collect(Collectors.toList());
                perennialValueList = singleList.stream().filter(x ->
                        StringUtils.compare(x.getYear().toString(), scales[0]) >= 0
                                && StringUtils.compare(x.getYear().toString(), scales[1]) <= 0
                                && x.getType().equals("2")).collect(Collectors.toList());
            }
            //常年值
            double num = perennialValueList.stream()
                    .mapToDouble(
                            x -> LocalDateUtils.stringToDate(type.equals("1")?x.getStartTime():x.getEndTime()).getDayOfYear())
                    .average().getAsDouble();
            // 将常年值转化为月日
            int Days = (int) Math.round(num);
            String perennialValue = LocalDate.now().withDayOfYear(Days).toString().replace("-", "")
                    .substring(4);
            //历史最早出现时间
            List<Integer> allValues = singleList.stream().map(x ->
                    Integer.parseInt(type.equals("1") ? x.getStartTime().substring(4,8) : x.getEndTime().substring(4,8)))
                    .collect(Collectors.toList());
            allValues.sort(Comparator.reverseOrder());
            int mxaMonthDay = Collections.max(allValues);
            List<SnowFallDaily> maxDay = singleList.stream()
                    .filter(x -> Integer.parseInt(type.equals("1") ? x.getStartTime().substring(4,8) : x.getEndTime().substring(4,8)) == mxaMonthDay)
                    .collect(Collectors.toList());
            //历史最晚出现时间
            int minMonthDay = Collections.min(allValues);
            List<SnowFallDaily> minDay = singleList.stream()
                    .filter(x -> Integer.parseInt(type.equals("1") ? x.getStartTime().substring(4,8) : x.getEndTime().substring(4,8)) == minMonthDay)
                    .collect(Collectors.toList());
            snowFallBuilder.stationNo(stationNo).stationName(singleList.get(0).getStationName())
                    .latitude(singleList.get(0).getLatitude()).longitude(singleList.get(0).getLongitude())
                    .maxDate(type.equals("1")?maxDay.get(0).getStartTime():maxDay.get(0).getEndTime())
                    .minDate(type.equals("1")?minDay.get(0).getStartTime():minDay.get(0).getEndTime());
            //获取当前年份下所选的数据
            List<Map<String, EventVal>> middleList = new ArrayList<>();
            for (int i = startYear; i <= endYear; i++) {
                Map<String, EventVal> resMap = new HashMap<>();
                int year = i;
                String key = year + "";
                EventVal.EventValBuilder builder = EventVal.builder();
                Optional<SnowFallDaily> firstList = singleList.stream().filter(x ->x.getYear()== year).findFirst();
                if (firstList.isPresent()) {
                    SnowFallDaily snowFallDaily = firstList.get();
                    //获取实况值
                    Integer liveValue = Integer.parseInt(type.equals("1")?snowFallDaily.getStartTime():snowFallDaily.getEndTime());
                    int historyQualifying = allValues.indexOf(Integer.parseInt(type.equals("1")?snowFallDaily.getStartTime().substring(4,8):snowFallDaily.getEndTime().substring(4,8))) + 1;
                    Long anomaly = LocalDateUtils.getDifferDays(type.equals("1")?(key+perennialValue):((year+1+"")+perennialValue), type.equals("1")?snowFallDaily.getStartTime():snowFallDaily.getEndTime());

                    builder.liveVal(liveValue).anomalyVal(Convert.toDouble(anomaly)).rank(historyQualifying).perenVal(perennialValue);

                    resMap.put(key, builder.build());
                    middleList.add(resMap);
                }


            }
            snowFallBuilder.data(middleList);
            resList.add(snowFallBuilder.build());
        }
        return resList;
    }
}
