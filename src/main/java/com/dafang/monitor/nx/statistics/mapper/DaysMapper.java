package com.dafang.monitor.nx.statistics.mapper;

import com.dafang.monitor.nx.statistics.entity.po.Daily;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/12
 * @version: 1.0
 */
@Mapper
@Repository
public interface DaysMapper extends MyMapper<Daily> {
    List<Daily> periodDays(DailyParam params);
    List<Daily> continuousDays(DailyParam params);
}
