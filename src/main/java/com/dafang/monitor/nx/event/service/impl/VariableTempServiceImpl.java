package com.dafang.monitor.nx.event.service.impl;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.event.entity.po.VariableTempParam;
import com.dafang.monitor.nx.event.entity.vo.VariableTempVO;
import com.dafang.monitor.nx.event.mapper.VariableTempMapper;
import com.dafang.monitor.nx.event.service.VariableTempService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    if (params.getUpOrDown().equals("up")) {//升温
                        index2 = afterValue >= beforeValue;
                    } else if (params.getUpOrDown().equals("down")) {//降温
                        index2 = afterValue <= beforeValue;
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
        List<VariableTempVO> hisList = new ArrayList<>();
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
            List<String> statList = liveValue.stream().map(x -> x.get("stationNO").toString()).distinct().collect(Collectors.toList());
            for (int i = startYear; i <= endYear; i++) {
                String year = i + "";
                for (String stationNo : statList) {
                    List<Map<String, Object>> singleList = liveValue.stream().filter(
                            x -> StringUtils.equals(stationNo, x.get("stationNO").toString())).collect(Collectors.toList());
                    List<Map<String, Object>> currentList = singleList.stream().filter(x -> x.get("Years").toString().equals(year)).collect(Collectors.toList());
                    if (params.getStatisticType().equals("rang")) {
                        for (int m = day; m < currentList.size(); m++) {
                            double beforeValue = Convert.toDouble(currentList.get(m - day).get(params.getTempType()));
                            double afterValue = Convert.toDouble(currentList.get(m).get(params.getTempType()));
                            String beforeTime = currentList.get(m - day).get("time").toString();//变温前时间
                            String afterTime = currentList.get(m).get("time").toString();//变温后时间
                            Double rangeValue = afterValue - beforeValue;//变温幅度
                            if (Math.abs(rangeValue) >= Math.abs(params.getThreshold())) {
                                //历史同期最大
                                String md = beforeTime.replace("-","").substring(4,8);
                                Map<String, Object> historyMap = getHistory(day, singleList, params, hisList, md, rangeValue);
                                VariableTempVO build = VariableTempVO.builder()
                                        .stationNo(currentList.get(m).get("stationNO").toString())
                                        .stationName(currentList.get(m).get("stationName").toString())
                                        .beforeTime(beforeTime)
                                        .beforeValue(beforeValue)
                                        .afterTime(afterTime)
                                        .afterValue(afterValue)
                                        .ObserverTime(year)
                                        .historyMax(Convert.toDouble(historyMap.get("historyMax")))
                                        .historyMaxTime(historyMap.get("historyMaxTime").toString())
                                        .historicalRanking(historyMap.get("historicalRanking").toString())
                                        .rangeValue(Double.valueOf(dft.format(rangeValue))).build();
                                tempVOList.add(build);
                            }
                        }
                    }
                    if (params.getStatisticType().equals("count")) {
                        tempVOList = getCountVariableTemp(singleList, tempVOList, params,
                                startYear, endYear, day, sdf, dft, isCDY);
                    }
                }
            }
        }
        return tempVOList;
    }

    private Map<String, Object> getHistory(int day, List<Map<String, Object>> singleList, VariableTempParam params,
                                           List<VariableTempVO> hisList, String md, Double rangeValue) {
        Map<String, Object> historyMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat dft = new DecimalFormat("0.00");
        for (int m = day; m < singleList.size(); m++) {
            double beforeValue = Convert.toDouble(singleList.get(m - day).get(params.getTempType()));
            double afterValue = Convert.toDouble(singleList.get(m).get(params.getTempType()));
            String beforeTime = singleList.get(m - day).get("time").toString();//变温前时间
            String monDay = beforeTime.replace("-","").substring(4,8);
            Double hrangeValue = afterValue - beforeValue;//变温幅度
            if (monDay.equals(md)) {
                VariableTempVO build = VariableTempVO.builder()
                        .beforeTime(beforeTime)
                        .rangeValue(Double.valueOf(dft.format(hrangeValue))).build();
                hisList.add(build);
            }
        }
        if (params.getUpOrDown().equals("up")) {//升温
            hisList = hisList.stream().
                    sorted(Comparator.comparing(VariableTempVO::getRangeValue).reversed()).collect(Collectors.toList());
        } else if (params.getUpOrDown().equals("down")) {//降温
            hisList = hisList.stream().
                    sorted(Comparator.comparing(VariableTempVO::getRangeValue)).collect(Collectors.toList());
        }
        historyMap.put("historyMax", Math.abs(hisList.get(0).getRangeValue()));
        historyMap.put("historyMaxTime", hisList.get(0).getBeforeTime());
        historyMap.put("historicalRanking",historicalRankingTotalDays(hisList, rangeValue, params.getUpOrDown()));
        return historyMap;
    }

    /*
     * 变温同期次数统计
     */
    private List<VariableTempVO> getCountVariableTemp(List<Map<String, Object>> singleList, List<VariableTempVO> tempVOList, VariableTempParam params, Integer startYear, Integer endYear,int day, SimpleDateFormat sdf, DecimalFormat dft, Boolean isCDY) {
        List<VariableTempVO> listHis = new ArrayList<>();
        LocalDate date = LocalDate.now();
        Integer yearNow = Integer.valueOf(date.format(DateTimeFormatter.ofPattern("y")));
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
            perennialValue = dft.format(sum / (index == 0 ? 1 : index));
            String historicalRanking = historicalRankingTimes(listSort, times);
            VariableTempVO build = VariableTempVO.builder()
                    .stationNo(singleList.get(0).get("stationNO").toString())
                    .stationName(singleList.get(0).get("stationName").toString())
                    .ObserverTime(y + "")
                    .counts(times)
                    .perennialValue(perennialValue)
                    .anomalyValue(dft.format(times - Double.valueOf(perennialValue)))
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
