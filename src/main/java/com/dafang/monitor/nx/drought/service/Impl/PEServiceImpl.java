package com.dafang.monitor.nx.drought.service.Impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.drought.entity.po.Drought;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.mapper.DroughtMapper;
import com.dafang.monitor.nx.drought.service.PEService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PEServiceImpl implements PEService {

    @Autowired
    DroughtMapper mapper;

    @Override
    public List<Map<String, Object>> getContinue(DroughtParam params) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Drought> baseData = mapper.getContinueData(params);
        List<String> stationNos = baseData.stream().map(Drought::getStationNo).distinct().collect(Collectors.toList());
        for (String stationNo : stationNos) {
            Map<String,Object> map = new HashMap<>();
            Drought drought = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList()).get(0);
            DoubleSummaryStatistics summary = baseData.stream().filter(x -> StringUtils.equals(stationNo, x.getStationNo())
                    && Convert.toDouble(x.getVal()) > 0).mapToDouble(Drought::getVal).summaryStatistics();
            long days = summary.getCount();
            double value = summary.getSum();
            map.put("stationNo",stationNo);
            map.put("stationName",drought.getStationName());
            map.put("longitude",drought.getLongitude());
            map.put("latitude",drought.getLatitude());
            map.put("days",days);
            map.put("value",value);
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getPeriod(DroughtParam params) {
        List<Map<String, Object>> result = new ArrayList<>();
        params.setST(params.getStartDate().substring(4,8));
        params.setET(params.getEndDate().substring(4,8));
        int sYear = Integer.valueOf(params.getStartDate().substring(0,4));
        int eYear = Integer.valueOf(params.getEndDate().substring(0,4));
        List<Drought> baseData = mapper.getPeriodData(params);
        List<String> stationNos = baseData.stream().map(Drought::getStationNo).distinct().collect(Collectors.toList());
        List<Integer> years = baseData.stream().map(Drought::getYear).distinct().collect(Collectors.toList());
        for (int i = sYear; i <= eYear; i++){
            int year = i;
            List<Drought> currentList = baseData.stream().filter(x -> x.getYear().equals(year)).collect(Collectors.toList());
            for (String stationNo : stationNos) {
                Map<String,Object> map = new HashMap<>();
                Drought drought = baseData.stream().filter(x -> StringUtils.equals(x.getStationNo(), stationNo)).collect(Collectors.toList()).get(0);
                DoubleSummaryStatistics summary = currentList.stream().filter(x -> StringUtils.equals(stationNo, x.getStationNo())
                        && Convert.toDouble(x.getVal()) > 0).mapToDouble(Drought::getVal).summaryStatistics();
                long days = summary.getCount();
                double value = summary.getSum();
                map.put("stationNo",stationNo);
                map.put("stationName",drought.getStationName());
                map.put("year",year);
                map.put("longitude",drought.getLongitude());
                map.put("latitude",drought.getLatitude());
                map.put("days",days);
                map.put("value",value);
                result.add(map);
            }
        }
        return result;
    }
}
