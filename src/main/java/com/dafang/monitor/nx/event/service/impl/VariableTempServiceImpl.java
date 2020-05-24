package com.dafang.monitor.nx.event.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.event.entity.po.VariableTempParam;
import com.dafang.monitor.nx.event.entity.vo.VariableTempVO;
import com.dafang.monitor.nx.event.mapper.VariableTempMapper;
import com.dafang.monitor.nx.event.service.VariableTempService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName VariableTempServiceImpl
 * @Description TODO
 * @Author my
 * @Date 2020/5/20
 **/
@Service
public class VariableTempServiceImpl implements VariableTempService {

    @Autowired
    private VariableTempMapper mapper;

    /**
     * @param
     * @return
     * @descriptions 变温连续幅度/次数统计
     * @author
     * @date 2020/5/21 10:56
     */
    @Override
    public List<VariableTempVO> variableTempContinuous(VariableTempParam params) {
        List<VariableTempVO> tempVOList = new ArrayList<>();
        DecimalFormat dft = new DecimalFormat("0.00");
        //时间处理
        Map<String, Object> timeMap = hanldTime(params.getStartDate(), params.getVariableTime());
        int day = (int) timeMap.get("day");
        params.setStartDate(timeMap.get("newStartDate").toString());
        List<Map<String, Object>> liveValue = mapper.getVariableTempContinuousData(params);
        if (liveValue.size() > 0) {
            List<String> statList = liveValue.stream().map(x -> x.get("stationNO").toString()).distinct().collect(Collectors.toList());
            for (String stationNo : statList) {
                int counts = 0;
                List<Map<String, Object>> singleList = liveValue.stream().filter(
                        x -> StringUtils.equals(stationNo, x.get("stationNO").toString())).collect(Collectors.toList());
                for (int j = day; j < singleList.size(); j++) {
                    double beforeValue = Convert.toDouble(singleList.get(j - day).get(params.getTempType()));
                    double afterValue = Convert.toDouble(singleList.get(j).get(params.getTempType()));
                    Double rangeValue = afterValue - beforeValue;//变温幅度
                    boolean index = false;
                    boolean index2 = true;
                    index = Math.abs(rangeValue) >= Math.abs(params.getThreshold());
                    if(params.getUpOrDown().equals("up")) {//升温
                        index2 = afterValue>=beforeValue;
                    }else if(params.getUpOrDown().equals("down")) {//降温
                        index2 = afterValue<=beforeValue;
                    }
                    if (index && index2) {
                        counts++;
                        if (params.getStatisticType().equals("rang")) {//幅度统计
                            VariableTempVO build = VariableTempVO.builder()
                                    .stationNo(singleList.get(j).get("stationNO").toString())
                                    .stationName(singleList.get(j).get("stationName").toString())
                                    .beforeTime(singleList.get(j - day).get("time").toString())
                                    .beforeValue(beforeValue)
                                    .afterTime(singleList.get(j).get("time").toString())
                                    .afterValue(afterValue)
                                    .rangeValue(Double.valueOf(dft.format(rangeValue))).build();
                            tempVOList.add(build);
                            tempVOList.stream().map(x -> x.getBeforeTime()).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                        }
                    }
                }
                if (params.getStatisticType().equals("count")) {//次数统计
                    String stationName = singleList.stream().map(x -> x.get("stationName").toString()).distinct().collect(Collectors.joining());
                    VariableTempVO build = VariableTempVO.builder().stationNo(stationNo)
                            .stationName(stationName).counts(counts).build();
                    tempVOList.add(build);
                }
            }
        }
        return tempVOList;
    }
    /**
     * @param
     * @return
     * @descriptions 变温同期幅度/次数统计
     * @author
     * @date 2020/5/21 10:56
     */
    @Override
    public List<VariableTempVO> periodDays(VariableTempParam params) {
        List<VariableTempVO> tempVOList = new ArrayList<>();
        DecimalFormat dft = new DecimalFormat("0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //时间处理
        Map<String, Object> timeMap = hanldTime(params.getStartDate(), params.getVariableTime());
        int day = (int) timeMap.get("day");
        Boolean isCDY = Double.valueOf(params.getStartDate().substring(4)) > Double.valueOf(params.getET());
        //同期开始日期0101时，需要查询前三天的数据，涉及到跨年，用跨年方式处理
        if (Double.valueOf(params.getStartDate().substring(4)) >= 101 && Double.valueOf(params.getStartDate().substring(4)) <= 103) {
            isCDY = true;
        }
        params.setStartDate(timeMap.get("newStartDate").toString());
        params.setST(timeMap.get("newStartDate").toString().substring(4));
        List<Map<String, Object>> liveValue = mapper.getVariableTempPeriodData(params);
        if (liveValue.size() > 0) {
            Integer startYear = Integer.valueOf(params.getStartDate().substring(0, 4));
            Integer endYear = Integer.valueOf(params.getEndDate().substring(0, 4));
            LocalDate date = LocalDate.now();
            Integer yearNow = Integer.valueOf(date.format(DateTimeFormatter.ofPattern("y")));
            List<String> statList = liveValue.stream().map(x -> x.get("stationNO").toString()).distinct().collect(Collectors.toList());
            for (String stationNo : statList) {
                List<VariableTempVO> listHis = new ArrayList<>();
                List<Map<String, Object>> singleList = liveValue.stream().filter(
                        x -> StringUtils.equals(stationNo, x.get("stationNO").toString())).collect(Collectors.toList());
                if (params.getStatisticType().equals("rang")) {
                    tempVOList = getRangVariableTemp(singleList, listHis, tempVOList, params,
                            startYear, endYear, yearNow, day, dft, isCDY);
                }
                if (params.getStatisticType().equals("count")) {
                    tempVOList = getCountVariableTemp(singleList, listHis, tempVOList, params,
                            startYear, endYear, yearNow, day, sdf, dft, isCDY);
                }
            }
        }
        return tempVOList;
    }
    /*
     * 变温同期次数统计
     */
    private List<VariableTempVO> getCountVariableTemp(List<Map<String, Object>> singleList, List<VariableTempVO> listHis, List<VariableTempVO> tempVOList, VariableTempParam params, Integer startYear, Integer endYear, Integer yearNow, int day, SimpleDateFormat sdf, DecimalFormat dft, Boolean isCDY) {
        for (int m = 1951; m <= yearNow; m++) {
            int counts = 0;
            for (int n = day; n < singleList.size(); n++) {
                if (singleList.get(n).get("Years").toString().equals(m + "")) {
                    double beforeValue = Convert.toDouble(singleList.get(n - day).get(params.getTempType()));
                    double afterValue = Convert.toDouble(singleList.get(n).get(params.getTempType()));
                    String beforeTime = singleList.get(n - day).get("time").toString();//变温前时间
                    String afterTime = singleList.get(n).get("time").toString();//变温后时间
                    Double rangeValue = afterValue - beforeValue;//变温幅度
                    long d = 0;
                    try {
                        d = (sdf.parse(afterTime).getTime() - sdf.parse(beforeTime).getTime()) / (1000 * 60 * 60 * 24);//变温前后时间差
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    boolean index = false;
                    index = Math.abs(rangeValue) >= Math.abs(params.getThreshold());
                    if (index && d >= 1 && d <= 3) {
                        counts++;
                    }
                }
            }
            VariableTempVO build = VariableTempVO.builder()
                    .stationNo(singleList.get(0).get("stationNO").toString())
                    .stationName(singleList.get(0).get("stationName").toString())
                    .ObserverTime(m + "")
                    .counts(counts).build();
            listHis.add(build);
        }
        //排序方便取历史同期最大值和历史排位
        List<VariableTempVO> listSort = new ArrayList<>();
        if (params.getUpOrDown().equals("up")) {//升温
            listSort = listHis.stream().
                    sorted(Comparator.comparing(VariableTempVO::getCounts).reversed()).collect(Collectors.toList());
        } else if (params.getUpOrDown().equals("down")) {//降温
            listSort = listHis.stream().
                    sorted(Comparator.comparing(VariableTempVO::getCounts)).collect(Collectors.toList());
        }
        //实际需求数据
        if (isCDY) {//跨年有特殊性
            startYear = startYear + 1;
        }
        //常年值计算
        String perennialValue = "";
        Double sum = 0.0;
        int index = 0;
        for (int p = 1981; p <= 2010; p++) {
            for (int o = 0; o < listSort.size(); o++) {
                if (listSort.get(o).getObserverTime().equals(p + "")) {
                    sum += listSort.get(o).getCounts();
                    index++;
                }
            }
        }
        for (int y = startYear; y <= endYear; y++) {
            int times = 0;//次数
            for (int z = 0; z < listSort.size(); z++) {
                if (listSort.get(z).getObserverTime().equals(y + "")) {
                    times = listSort.get(z).getCounts();
                }
            }
            perennialValue = dft.format(sum/(index==0?1:index));
            String historicalRanking = historicalRankingTimes(listSort, times);
            VariableTempVO build = VariableTempVO.builder()
                    .stationNo(singleList.get(0).get("stationNO").toString())
                    .stationName(singleList.get(0).get("stationName").toString())
                    .ObserverTime(y + "")
                    .counts(times)
                    .perennialValue(perennialValue)
                    .anomalyValue(dft.format(times-Double.valueOf(perennialValue)))
                    .historyMaxCount(listSort.get(0).getCounts())
                    .historicalRanking(historicalRanking).build();
            tempVOList.add(build);
        }
        return tempVOList;
    }

    /*
     * 变温次数历史排位
     */
    public static String historicalRankingTimes(List<VariableTempVO> listSort, int times) {
        int historicalRanking = -9999;
        for (int s = 0; s < listSort.size(); s++) {
            if (times >= Double.valueOf(listSort.get(s).getCounts())) {
                historicalRanking = s + 1;
                break;
            }
            historicalRanking = s + 1;
        }
        return historicalRanking + "";
    }

    /*
     * 同期变温幅度
     */
    private List<VariableTempVO> getRangVariableTemp(List<Map<String, Object>> singleList, List<VariableTempVO> listHis, List<VariableTempVO> tempVOList, VariableTempParam params,
                                                     Integer startYear, Integer endYear, int yearNow, int day, DecimalFormat dft, Boolean isCDY) {
        for (int m = 1951; m <= yearNow; m++) {
            for (int n = day; n < singleList.size(); n++) {
                if (singleList.get(n).get("Years").toString().equals(m + "")) {
                    double beforeValue = Convert.toDouble(singleList.get(n - day).get(params.getTempType()));
                    double afterValue = Convert.toDouble(singleList.get(n).get(params.getTempType()));
                    String beforeTime = singleList.get(n - day).get("time").toString();//变温前时间
                    String afterTime = singleList.get(n).get("time").toString();//变温后时间
                    Double rangeValue = afterValue - beforeValue;//变温幅度
                    if (Math.abs(rangeValue) >= Math.abs(params.getThreshold())) {
                        VariableTempVO build = VariableTempVO.builder()
                                .stationNo(singleList.get(n).get("stationNO").toString())
                                .stationName(singleList.get(n).get("stationName").toString())
                                .beforeTime(beforeTime)
                                .beforeValue(beforeValue)
                                .afterTime(afterTime)
                                .afterValue(afterValue)
                                .ObserverTime(m + "")
                                .rangeValue(Double.valueOf(dft.format(rangeValue))).build();
                        listHis.add(build);
                    }
                }
            }
        }
        //排序方便取历史同期最大值和历史排位
        List<VariableTempVO> listSort = new ArrayList<>();
        if (params.getUpOrDown().equals("up")) {//升温
            listSort = listHis.stream().
                    sorted(Comparator.comparing(VariableTempVO::getRangeValue).reversed()).collect(Collectors.toList());
        } else if (params.getUpOrDown().equals("down")) {//降温
            listSort = listHis.stream().
                    sorted(Comparator.comparing(VariableTempVO::getRangeValue)).collect(Collectors.toList());
        }
        //组装返回数据
        if (isCDY) {//跨年
            startYear = startYear + 1;
        }
        for (int y = startYear; y <= endYear; y++) {
            for (int z = 0; z < listSort.size(); z++) {
                if (listSort.get(z).getObserverTime().equals(y + "")) {
                    Double rangeValue = listSort.get(z).getRangeValue();
                    String historicalRanking = historicalRankingTotalDays(listSort, rangeValue, params.getUpOrDown());
                    VariableTempVO build = VariableTempVO.builder()
                            .stationNo(listSort.get(z).getStationNo())
                            .stationName(listSort.get(z).getStationName())
                            .beforeTime(listSort.get(z).getBeforeTime())
                            .beforeValue(listSort.get(z).getBeforeValue())
                            .afterTime(listSort.get(z).getAfterTime())
                            .afterValue(listSort.get(z).getAfterValue())
                            .ObserverTime(listSort.get(z).getObserverTime())
                            .historyMax(listSort.get(0).getRangeValue())
                            .historyMaxTime(listSort.get(0).getAfterTime())
                            .historicalRanking(historicalRanking)
                            .rangeValue(rangeValue).build();
                    tempVOList.add(build);
                }
            }
        }
        return tempVOList;
    }

    private Map<String, Object> hanldTime(String startDate, String variableTime) {
        Map<String, Object> timeMap = new HashMap<>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            c.setTime(df.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = 0;
        if (variableTime.equals("24h")) {
            c.add(Calendar.DAY_OF_YEAR, -1);
            day = 1;
        } else if (variableTime.equals("48h")) {
            c.add(Calendar.DAY_OF_YEAR, -2);
            day = 2;
        } else if (variableTime.equals("72h")) {
            c.add(Calendar.DAY_OF_YEAR, -3);
            day = 3;
        }
        String newStartDate = df.format(c.getTime());
        timeMap.put("day", day);
        timeMap.put("newStartDate", newStartDate);
        return timeMap;
    }

    /*
     * 变温历史排位
     */
    public static String historicalRankingTotalDays(List<VariableTempVO> listSort, Double rangeValue, String upOrDown) {
        int historicalRanking = -9999;
        for (int s = 0; s < listSort.size(); s++) {
            if (upOrDown.equals("up")) {//升温
                if (rangeValue >= Double.valueOf(listSort.get(s).getRangeValue())) {
                    historicalRanking = s + 1;
                    break;
                }
            } else if (upOrDown.equals("down")) {//降温
                if (rangeValue <= Double.valueOf(listSort.get(s).getRangeValue())) {
                    historicalRanking = s + 1;
                    break;
                }
            }
            historicalRanking = s + 1;
        }
        return historicalRanking + "";
    }
}
