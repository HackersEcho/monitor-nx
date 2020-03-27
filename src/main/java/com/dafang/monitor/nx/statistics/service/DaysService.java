package com.dafang.monitor.nx.statistics.service;

import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.ContinuousDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;

import java.util.List;

/**
 * @description:日数的所有接口
 * @author: echo
 * @createDate: 2020/3/12
 * @version: 1.0
 */
public interface DaysService {
        List<PeriodDays> periodDays(DailyParam params);
        List<ContinuousDays> continuousDays(DailyParam params);
}
