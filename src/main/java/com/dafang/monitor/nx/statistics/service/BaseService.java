package com.dafang.monitor.nx.statistics.service;

import com.dafang.monitor.nx.statistics.entity.po.DailyParam;

import java.util.List;
import java.util.Map;

public interface BaseService {
    List<Map<String, Object>> staInfoList(DailyParam params);
}
