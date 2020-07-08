package com.dafang.monitor.nx.drought.service;

import com.dafang.monitor.nx.drought.entity.po.DroughtParam;

import java.util.List;
import java.util.Map;

public interface SPIService {

    List<Map<String,Object>> getContinueSPI(DroughtParam params);
    List<Map<String,Object>> getPeriodSPI(DroughtParam params);

}
