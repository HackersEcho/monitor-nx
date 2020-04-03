package com.dafang.monitor.nx.accessment.service;

import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;

import java.util.List;
import java.util.Map;

public interface HumanComfortService{

    /*
    连续舒适度指数
     */
    public List<Map<String,Object>> continueList(ComfortParam comfortParam);
    /*
    同期舒适度指数
     */
    public List<Map<String,Object>> periodList(ComfortParam comfortParam);
}
