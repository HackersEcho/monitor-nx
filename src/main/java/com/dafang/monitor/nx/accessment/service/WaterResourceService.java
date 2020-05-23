package com.dafang.monitor.nx.accessment.service;

import com.dafang.monitor.nx.accessment.entity.po.WaterResourceparam;

import java.util.List;
import java.util.Map;

public interface WaterResourceService {

    /*
    水资源数据
     */
    List<Map<String,Object>> dataList(WaterResourceparam params);

}
