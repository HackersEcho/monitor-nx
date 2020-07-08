package com.dafang.monitor.nx.drought.service;

import com.dafang.monitor.nx.drought.entity.po.DroughtParam;

import java.util.List;
import java.util.Map;

public interface CIService {

    List<Map<String,Object>> getCI(DroughtParam params);

}
