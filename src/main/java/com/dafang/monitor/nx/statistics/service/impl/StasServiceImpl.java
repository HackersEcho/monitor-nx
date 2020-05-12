package com.dafang.monitor.nx.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.CommonVal;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodStas;
import com.dafang.monitor.nx.statistics.mapper.StasMapper;
import com.dafang.monitor.nx.statistics.service.StasService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/4/13
 * @version: 1.0
 */
@Service
public class StasServiceImpl implements StasService {
    @Autowired
    private StasMapper mapper;
    @Override
    public List<PeriodStas> periodSta(DailyParam params) {
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
                                 && x.get("year")>=params.getRankEndYear()).map(x -> x.get("val")).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
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
}
