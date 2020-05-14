package com.dafang.monitor.nx.event.mapper;

import com.dafang.monitor.nx.event.entity.po.ColdWaveParam;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.MyMapper;

import java.util.List;

/**
 * @description:寒潮
 * @author: echo
 * @createDate: 2020/5/11
 * @version: 1.0
 */
@Mapper
public interface ColdWaveMapper extends MyMapper<Daily> {
    List<Daily> periodDays(ColdWaveParam params);
    List<Daily> continuousDays(ColdWaveParam params);
    List<Daily> periodList(ColdWaveParam params);
    List<Daily> continuouStas(ColdWaveParam params);
}
