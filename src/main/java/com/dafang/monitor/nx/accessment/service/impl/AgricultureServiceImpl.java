package com.dafang.monitor.nx.accessment.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.accessment.entity.po.Agriculture;
import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;
import com.dafang.monitor.nx.accessment.mapper.AgricultureMapper;
import com.dafang.monitor.nx.accessment.service.AgricultureService;
import com.dafang.monitor.nx.utils.CommonUtils;
import com.dafang.monitor.nx.utils.ReflectHandleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Ref;
import java.util.*;
import java.util.stream.Collectors;

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
            String stationame = singleList.get(0).getStationName();
            for(int i = startYear; i <= endYear; i++){
                Map<String,Object> map = new HashMap<>();
                int year = i;
                //平均气温
                Double temAvgLiveVal = Convert.toDouble(NumberUtil.round(ReflectHandleUtils.filterData(singleList, "temAvg").stream().
                        filter(x -> x.getYear() == year).mapToDouble(x -> Convert.toDouble(x.getTemAvg())).summaryStatistics().getAverage(), 1));
                //气温距平
                Double temAvgPerenVal = Convert.toDouble(NumberUtil.round(ReflectHandleUtils.filterData(singleList, "temAvg").stream().
                        filter(x -> x.getYear() >= Convert.toInt(scale[0]) && x.getYear() <= Convert.toInt(scale[1])).
                        mapToDouble(x -> Convert.toDouble(x.getTemAvg())).summaryStatistics().getAverage(),1));
                double temAvgAnomalyVal = NumberUtil.sub(temAvgLiveVal, temAvgPerenVal);
                //降水
                Double preLiveVal = Convert.toDouble(NumberUtil.round(ReflectHandleUtils.filterData(singleList, "pre2020").stream().
                        filter(x -> x.getYear() == year).mapToDouble(x -> Convert.toDouble(x.getPre2020())).summaryStatistics().getSum(), 1));
                //降水距平百分率
                Double prePerenVal = NumberUtil.div(ReflectHandleUtils.filterData(singleList, "pre2020").stream().
                        filter(x -> x.getYear() >= Convert.toInt(scale[0]) && x.getYear() <= Convert.toInt(scale[1])).
                        mapToDouble(x -> Convert.toDouble(x.getPre2020())).summaryStatistics().getSum(), scaleLen, 1);
                double preAnomalyVal = NumberUtil.div(Convert.toDouble((preLiveVal-prePerenVal)), prePerenVal, 1);
                //日照
                Double sshLiveVal = Convert.toDouble(NumberUtil.round(ReflectHandleUtils.filterData(singleList, "ssh").stream().
                        filter(x -> x.getYear() == year).mapToDouble(x -> Convert.toDouble(x.getSsh())).summaryStatistics().getSum(), 1));
                //日照距平
                Double sshPerenVal = NumberUtil.div(ReflectHandleUtils.filterData(singleList, "ssh").stream().
                        filter(x -> x.getYear() >= Convert.toInt(scale[0]) && x.getYear() <= Convert.toInt(scale[1])).
                        mapToDouble(x -> Convert.toDouble(x.getSsh())).summaryStatistics().getSum(), scaleLen, 1);
                double sshAnomalyVal = NumberUtil.sub(sshLiveVal, sshPerenVal);
                //最高气温
                Double temMaxLiveVal = Convert.toDouble(NumberUtil.round(ReflectHandleUtils.filterData(singleList, "temMax").stream().
                        filter(x -> x.getYear() == year).mapToDouble(x -> Convert.toDouble(x.getTemMax())).summaryStatistics().getAverage(), 1));
                //高温日数
                long highTemDays = ReflectHandleUtils.filterData(singleList, "temMax").stream().filter(x -> Convert.toDouble(x.getTemMax()) > 35).count();
                //最低气温
                Double temMinLiveVal = Convert.toDouble(NumberUtil.round(ReflectHandleUtils.filterData(singleList, "temMin").stream().
                        filter(x -> x.getYear() == year).mapToDouble(x -> Convert.toDouble(x.getTemMin())).summaryStatistics().getAverage(), 1));
                //霜冻日数
                long frostDays = ReflectHandleUtils.filterData(singleList, "temMin").stream().filter(x -> Convert.toDouble(x.getTemMin()) < 0).count();
                map.put("year",year);
                map.put("stationNo",stationNo);
                map.put("stationName",stationame);
                map.put("temAvgAnomalyVal",temAvgAnomalyVal);
                map.put("preLiveVal",preLiveVal);
                map.put("preAnomalyVal",preAnomalyVal);
                map.put("sshLiveVal",sshLiveVal);
                map.put("sshAnomalyVal",sshAnomalyVal);
                map.put("temMaxLiveVal",temMaxLiveVal);
                map.put("highTemDays",highTemDays);
                map.put("temMinLiveVal",temMinLiveVal);
                map.put("frostDays",frostDays);
                results.add(map);
            }
        }
        return results;
    }
}
