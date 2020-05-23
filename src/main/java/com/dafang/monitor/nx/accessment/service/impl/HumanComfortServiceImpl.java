package com.dafang.monitor.nx.accessment.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.accessment.entity.emun.ComfortLevelEnum;
import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;
import com.dafang.monitor.nx.accessment.mapper.HumanComfortMapper;
import com.dafang.monitor.nx.accessment.service.HumanComfortService;
import com.dafang.monitor.nx.utils.CommonUtils;
import com.dafang.monitor.nx.utils.LocalDateUtils;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HumanComfortServiceImpl implements HumanComfortService {

    @Autowired
    private HumanComfortMapper humanComfortMapper;

    @Override
    public List<Map<String,Object>> continueList(ComfortParam params) {
        List<Map<String,Object>> resList = new ArrayList<>();
        //获取数据
        List<Comfort> baseData = humanComfortMapper.continueList(params);
        List<String> staList = baseData.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
        //根据指数类型去筛选对应数据
        for (ComfortLevelEnum comfortLevel : ComfortLevelEnum.values()) {
            if(StringUtils.equals(comfortLevel.getIndex(),params.getComfortType())){//判断指数类别
                Map<String, Object> resMap = new HashMap<>();
                String scales = comfortLevel.getDesc();
                resMap.put("index",comfortLevel.getDesc());
                resMap.put("level",comfortLevel.getLevel());
                resMap.put("feel",comfortLevel.getFell());
                // 得到一个等级的所有数据
                List<Comfort> singleList = baseData.stream().filter(x -> x.getIndexValue() >= Convert.toInt(scales.split("_")[0])
                        && x.getIndexValue() < Convert.toInt(scales.split("_")[1])).collect(Collectors.toList());
//                List<String> staList = singleList.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
                Map<String, Object> staMap = new HashMap<>();
                for (String stationNo : staList) {
                    List<Comfort> collect = singleList.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList());
                    if (collect.size() > 0){
                        Comfort comfort = collect.get(0);
                        staMap.put("stationName",comfort.getStationName());
                        staMap.put("liveVal",collect.size());
                    }else {
                        String stationName = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).
                                map(x -> x.getStationName()).collect(Collectors.toList()).get(0);
                        staMap.put("stationName",stationName);
                        staMap.put("liveVal","0");
                    }
                }
                resMap.put("data",staMap);
                resList.add(resMap);
            }
        }
        return resList;
    }

    @Override
    public List<Map<String,Object>> periodList(ComfortParam params) {
        List<Map<String, Object>> resList = new ArrayList<>();
        // 得到开始和结束年份以及月日
        int startYear = Convert.toInt(params.getStartDate().substring(0, 4));
        int endtYear = Convert.toInt(params.getEndDate().substring(0, 4));
        String sMD = params.getStartDate().substring(4);
        String eMD = params.getStartDate().substring(4);
        // 判断是否跨年(true 表跨年)
        boolean isKua = StringUtils.compare(sMD,eMD)>0?true:false;
        String[] climateScales = params.getClimateScale().split("-");
        // 得到常年值区间长度
        int perenLen = Convert.toInt(climateScales[1]) - Convert.toInt(climateScales[0]) + 1;
        // 获取数据
        List<Comfort> baseData = humanComfortMapper.periodList(params);

        for (ComfortLevelEnum comfortLevel : ComfortLevelEnum.values()) {
            if ( StringUtils.equals(comfortLevel.getIndex(),params.getComfortType())){// 根据我们的配置和前端传来的指数类型去操作
                Map<String, Object> resMap = new HashMap<>();
                String scales = comfortLevel.getDesc();// 得到自定义指数范围
                resMap.put("index", scales);
                resMap.put("level", comfortLevel.getLevel());
                resMap.put("fell", comfortLevel.getFell());
                // 得到一个等级的所有数据
                List<Comfort> singleList = baseData.stream().filter(x -> x.getIndexValue() >= Convert.toInt(scales.split("_")[0])
                        && x.getIndexValue() < Convert.toInt(scales.split("_")[1])).collect(Collectors.toList());
//                List<String> staList = singleList.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
                List<String> staList = baseData.stream().map(x -> x.getStationNo()).distinct().collect(Collectors.toList());
                Map<String, Object> staMap = new HashMap<>();
                for (String stationNo : staList) {
                    String stationName = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).
                            map(x -> x.getStationName()).collect(Collectors.toList()).get(0);
                    List<Comfort> collect = singleList.stream().filter(x -> StringUtils.equals(stationNo, x.getStationNo())).collect(Collectors.toList());
                    long perenVal = 0; long liveVal = 0;
                    // 得到常年值
                    perenVal = collect.stream().filter(x -> x.getYear() >= Convert.toInt(climateScales[0]) && x.getYear() <= Convert.toInt(climateScales[1])).count() / perenLen;
                    // 遍历年份
                    Map<String, Object> yearMap = new HashMap<>();
                    for (int i=startYear;i<=endtYear;i++){
                        Map<String, Object> valMap = new HashMap<>();
                        int year = i;
                        liveVal = collect.stream().filter(x -> x.getYear() == year).count();
                        long anomalyVal = liveVal - perenVal;
                        valMap.put("perenVal", perenVal);
                        valMap.put("liveVal", liveVal);
                        valMap.put("anomalyVal", anomalyVal);
                        String key = year+"";
                        if (isKua){
                            key = year + "-" + (year + 1);
                        }
                        yearMap.put(key, valMap);
                    }
//                    staMap.put(comfort.getStationName(),yearMap);
                    staMap.put(stationName,yearMap);
                }
                resMap.put("data",staMap);
                resList.add(resMap);
            }
        }
        return resList;
    }

    @Override
    public List<Comfort> dailyContinueList(ComfortParam params) {
        LocalDate startDate = LocalDateUtils.stringToDate(params.getStartDate());
        LocalDate endDate = LocalDateUtils.stringToDate(params.getEndDate());
        List<Comfort> baseData = humanComfortMapper.continueList(params);
        List<Comfort> results = baseData.stream().
                filter(x -> x.getObserverTime().isAfter(startDate) && x.getObserverTime().isBefore(endDate)).collect(Collectors.toList());
        return results;
    }

    @Override
    public List<Comfort> dailyPeriodList(ComfortParam params) {
        LocalDate startDate = LocalDateUtils.stringToDate(params.getStartDate());
        LocalDate endDate = LocalDateUtils.stringToDate(params.getEndDate());
        List<Comfort> baseData = humanComfortMapper.periodList(params);
        List<Comfort> results = baseData.stream().
                filter(x -> x.getObserverTime().isAfter(startDate) && x.getObserverTime().isBefore(endDate)).collect(Collectors.toList());
        return results;
    }
}
