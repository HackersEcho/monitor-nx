package com.dafang.monitor.nx.event.service;

import com.dafang.monitor.nx.event.entity.po.FrostParam;
import com.dafang.monitor.nx.event.entity.po.FrostStaParam;
import com.dafang.monitor.nx.event.entity.vo.DateStatistic;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodStas;

import java.util.List;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/5/15
 * @version: 1.0
 */
public interface FrostService {
        List<PeriodDays> periodDays(FrostParam params);
        // 日期
        List<DateStatistic> periodDates(FrostParam params);
        // 同期站数
        List<PeriodStas> periodSta(FrostStaParam param);

}
