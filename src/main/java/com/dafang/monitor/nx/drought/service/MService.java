package com.dafang.monitor.nx.drought.service;

import com.dafang.monitor.nx.drought.entity.po.DroughtParam;

import java.util.List;
import java.util.Map;

public interface MService {

    /**
     * 连续相对湿润度
     * @param params
     * @return
     */
    public List<Map<String,Object>> getContiuneM(DroughtParam params);
    /**
     * 同期相对湿润度
     * @param params
     * @return
     */
    public List<Map<String,Object>> getPeriodM(DroughtParam params);

}
