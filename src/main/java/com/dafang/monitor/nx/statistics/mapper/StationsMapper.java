package com.dafang.monitor.nx.statistics.mapper;

import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import tk.mybatis.MyMapper;

import java.util.List;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/25
 * @version: 1.0
 */
public interface StationsMapper extends MyMapper<Daily> {
    List<Daily> periodStas(DailyParam params);
}
