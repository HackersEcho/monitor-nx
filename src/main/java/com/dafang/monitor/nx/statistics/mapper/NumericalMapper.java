package com.dafang.monitor.nx.statistics.mapper;

import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/4/2
 * @version: 1.0
 */
@Mapper
@Repository
public interface NumericalMapper{
    List<Map<String,Object>> continuousByElement(DailyParam params);
    //平均值、累计值同期
    List<Map<String,Object>> periodByElement(DailyParam params);
    // 单要素的同期数据查询
    List<Daily> periodList(DailyParam params);
}
