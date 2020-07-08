package com.dafang.monitor.nx.drought.service;

import com.dafang.monitor.nx.drought.entity.po.DroughtParam;

import java.util.List;
import java.util.Map;

public interface KForecastService {

    public List<Map<String,Object>> getKForecast(DroughtParam params);

}
