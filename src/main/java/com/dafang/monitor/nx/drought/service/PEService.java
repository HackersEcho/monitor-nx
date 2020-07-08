package com.dafang.monitor.nx.drought.service;

import com.dafang.monitor.nx.drought.entity.po.DroughtParam;

import java.util.List;
import java.util.Map;

public interface PEService {

    /*
    连续蒸散量
     */
    List<Map<String,Object>> getContinue(DroughtParam params);
    /*
    同期蒸散量
     */
    List<Map<String,Object>> getPeriod(DroughtParam params);

}
