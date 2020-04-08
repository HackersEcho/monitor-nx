package com.dafang.monitor.nx.accessment.service;

import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;

import java.util.List;
import java.util.Map;

public interface HumanComfortService{

    /*
    连续舒适度指数（风寒指数,穿衣指数，湿温指数 ,综合指数）
     */
    public List<Map<String,Object>> continueList(ComfortParam params);
    /*
    同期舒适度指数（风寒指数,穿衣指数，湿温指数 ，综合指数）
     */
    public List<Map<String,Object>> periodList(ComfortParam pParams);

    /*
    舒适度指数逐日连续数据（风寒指数,穿衣指数，湿温指数 ，综合指数）
     */
    public List<Comfort> dailyContinueList(ComfortParam params);
    /*
    舒适度指数逐日同期数据（风寒指数,穿衣指数，湿温指数 ，综合指数）
     */
    public List<Comfort> dailyPeriodList(ComfortParam params);
}
