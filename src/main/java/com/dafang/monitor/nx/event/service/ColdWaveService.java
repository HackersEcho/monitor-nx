package com.dafang.monitor.nx.event.service;

import com.dafang.monitor.nx.event.entity.po.ColdWaveParam;
import com.dafang.monitor.nx.event.entity.vo.CountiousSta;
import com.dafang.monitor.nx.statistics.entity.vo.ContinuousDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodStas;

import java.util.List;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/5/12
 * @version: 1.0
 */
public interface ColdWaveService {
        List<PeriodDays> periodDays(ColdWaveParam params);
        List<ContinuousDays> continuousDays(ColdWaveParam params);
        // 同期站数
        List<PeriodStas> periodSta(ColdWaveParam param);
        List<CountiousSta> countiousSta(ColdWaveParam param);
}
