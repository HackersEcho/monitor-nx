package com.dafang.monitor.nx.accessment.service;

import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;

import java.util.List;

public interface HumanComfortService{

    /*
    连续舒适度指数
     */
    List<Comfort> queryContinueComfortValue(ComfortParam comfortParam);
    /*
    同期舒适度指数
     */
    List<Comfort> queryPeriodComfortValue(ComfortParam comfortParam);
}
