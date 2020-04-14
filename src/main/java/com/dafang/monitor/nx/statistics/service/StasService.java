package com.dafang.monitor.nx.statistics.service;

import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodStas;

import java.util.List;

/**
 * @description:站数接口
 * @author: echo
 * @createDate: 2020/4/13
 * @version: 1.0
 */
public interface StasService {
    // 同期站数
    List<PeriodStas> periodSta(DailyParam param);
}
