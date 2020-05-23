package com.dafang.monitor.nx.event.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.event.entity.po.ColdWaveParam;
import com.dafang.monitor.nx.event.entity.vo.CountiousSta;
import com.dafang.monitor.nx.event.mapper.ColdWaveMapper;
import com.dafang.monitor.nx.event.service.ColdWaveService;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.statistics.entity.vo.CommonVal;
import com.dafang.monitor.nx.statistics.entity.vo.ContinuousDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodStas;
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
public class ColdWaveServiceImpl implements ColdWaveService {
    @Autowired
    private ColdWaveMapper mapper;

    @Override
    public List<PeriodDays> periodDays(ColdWaveParam params) {
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
    public List<ContinuousDays> continuousDays(ColdWaveParam params) {
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
    @Override
    public List<PeriodStas> periodSta(ColdWaveParam params) {
        List<PeriodStas> resList = new ArrayList<>();// 返回值
        int startYear = Convert.toInt(params.getStartDate().substring(0, 4));
        int endYear = Convert.toInt(params.getEndDate().substring(0, 4));
        String[] scales = params.getClimateScale().split("-");// 常年值区间段
        int scaleLen = Convert.toInt(scales[1]) -  Convert.toInt(scales[0]) + 1;
        List<Daily> periodList = mapper.periodList(params);
        // 得到要统计的所有区域
        String[] regions = params.getRegions().split(",");
        RegionStaEnum[] enums = RegionStaEnum.values();
        for (String region : regions) {
            for (RegionStaEnum anEnum : enums) {
                if (StringUtils.equals(anEnum.getRegionId(),region)){
                    PeriodStas.PeriodStasBuilder stasBuilder = PeriodStas.builder();
                    stasBuilder.regionName(anEnum.getDesc());
                    // 得到该区域下所有的站点
                    String stas = anEnum.getStas();
                    // 得到该区域下所有的数据
                    List<Daily> singleList = periodList.parallelStream().filter(x -> StringUtils.contains(stas, x.getStationNo())).collect(Collectors.toList());
                    // 得到每一年的该区域发生的站数
                    List<Map<String , Integer>> yearDatas = new ArrayList<>();
                    List<Integer> yearList = singleList.stream().map(x -> x.getYear()).distinct().collect(Collectors.toList());
                    for (Integer year : yearList) {
                        int temYear = year;
                        Map<String, Integer> yearMap = new HashMap<>();
                        List<Daily> collect = singleList.stream().filter(x -> x.getYear() == temYear).collect(Collectors.toList());
                        int val = 0;
                        if (collect.size()>0){
                            val = (int) collect.stream().map(x -> x.getStationNo()).distinct().count();
                        }
                        yearMap.put("year", year);
                        yearMap.put("val", val);
                        yearDatas.add(yearMap);
                    }
                    // 得到历史最大值和出现的年份
                    DoubleSummaryStatistics statistics = yearDatas.stream().mapToDouble(x -> x.get("val")).summaryStatistics();
                    double maxExtrem = statistics.getMax();
                    String maxExtremTime = yearDatas.stream().filter(x -> x.get("val") == maxExtrem).map(x -> x.get("year").toString()).collect(Collectors.joining(","));
                    stasBuilder.mostHistoryStas(maxExtrem).mostHistoryDate(maxExtremTime);
                    // 常年值数据处理
                    Double perenVal = null;
                    List<Map<String, Integer>> perenList = yearDatas.stream().filter(x -> x.get("year") >= Convert.toInt(scales[0])
                            && x.get("year") <= Convert.toInt(scales[1])).collect(Collectors.toList());
                    if (perenList.size() > 0){
                        perenVal = perenList.stream().mapToDouble(x -> x.get("val")).sum() / scaleLen;
                    }
                    stasBuilder.perenVal(perenVal);
                    // 查询年份数据统计
                    List<Map<String, CommonVal>> middleList = new ArrayList<>();
                    for (int i = startYear;i <= endYear;i++){
                        Map<String, CommonVal> resMap = new HashMap<>();
                        int year = i;
                        String key = year + "";
                        CommonVal.CommonValBuilder builder = CommonVal.builder();
                        Optional<Map<String, Integer>> currOp = yearDatas.stream().filter(x -> x.get("year") == year).findFirst();
                        if (currOp.isPresent()){
                            Map<String, Integer> map = currOp.get();
                            int liveVal = map.get("val");
                            builder.liveVal(liveVal*1.0);
                            if (perenVal != null){
                                double anomalyVal = liveVal - perenVal;
                                builder.perenVal(perenVal).anomalyVal(anomalyVal);
                            }
                            // 计算排位
                            List<Integer> valList = yearDatas.stream().filter(x->x.get("year")>=params.getRankStartYear()
                                    && x.get("year")<=params.getRankEndYear()).map(x -> x.get("val")).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
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
                    stasBuilder.data(middleList);
                    resList.add(stasBuilder.build());
                }
            }
        }
        return resList;
    }
    @Override
    public List<CountiousSta> countiousSta(ColdWaveParam params) {
        List<CountiousSta> resList = new ArrayList<>();
        List<Daily> continuouList = mapper.continuouStas(params);
        // 得到要统计的所有区域
        String[] regions = params.getRegions().split(",");
        RegionStaEnum[] enums = RegionStaEnum.values();
        for (String region : regions) {
            for (RegionStaEnum anEnum : enums) {
                if (StringUtils.equals(anEnum.getRegionId(),region)){
                    CountiousSta bean = new CountiousSta();
                    bean.setRegionName(anEnum.getDesc());
                    // 得到该区域下所有的站点
                    String stas = anEnum.getStas();
                    // 统计发生站数
                   Long val = 0L;
                    Optional<Daily> op = continuouList.stream().filter(x -> StringUtils.contains(stas, x.getStationNo())).findFirst();
                    if (op.isPresent()){
                        val = continuouList.stream().filter(x -> StringUtils.contains(stas, x.getStationNo())).map(x -> x.getStationNo()).distinct().count();
                    }
                    bean.setVal(val+"");
                    resList.add(bean);
                }
            }
        }
        return resList;
    }
}
