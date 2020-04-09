package com.dafang.monitor.nx.accessment.service;

import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface AgricultureService {

    /**
     * 农作物所需数据集合
     */
     public List<Map<String,Object>> dataList(AgricultureParam params);
}
