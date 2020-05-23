package com.dafang.monitor.nx.event.service;

import com.dafang.monitor.nx.event.entity.po.ColdWaveParam;
import com.dafang.monitor.nx.event.entity.po.VariableTempParam;
import com.dafang.monitor.nx.event.entity.vo.VariableTempVO;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;

import java.util.List;

/**
 * @ClassName VariableTempService
 * @Description TODO
 * @Author my
 * @Date 2020/5/20
 **/
public interface VariableTempService {
    List<VariableTempVO> variableTempContinuous(VariableTempParam params);

    List<VariableTempVO> periodDays(VariableTempParam params);
}
