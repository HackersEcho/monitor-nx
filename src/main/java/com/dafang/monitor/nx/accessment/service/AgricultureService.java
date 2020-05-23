package com.dafang.monitor.nx.accessment.service;

import com.dafang.monitor.nx.accessment.entity.po.AccumuTemParam;
import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;

import java.util.List;
import java.util.Map;

public interface AgricultureService {

    /**
     * 农作物所需数据集合
     */
     public List<Map<String,Object>> dataList(AgricultureParam params);

     /*
     积温-同期
      */
     List<Map<String,Object>> periodList(AccumuTemParam params);

    /*
    积温-连续
     */
    List<Map<String,Object>> continueList(AccumuTemParam params);
}
