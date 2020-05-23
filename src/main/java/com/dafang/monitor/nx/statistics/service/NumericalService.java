package com.dafang.monitor.nx.statistics.service;

import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.ComprehensiveExtrem;
import com.dafang.monitor.nx.statistics.entity.vo.FirstDays;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/4/2
 * @version: 1.0
 */
public interface NumericalService {
    /*
    *
     * 综合数据统计
     * @param params
     * @return void
     * @author echo
     * @date 2020/4/2
     */
    List<Map<String, Object>> comprehensiveStatistic(DailyParam params);
    // 极值统计
    List<ComprehensiveExtrem> extrem(DailyParam params);

    // 初终日
    List<FirstDays> firstDays(DailyParam params);


}
