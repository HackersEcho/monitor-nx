package com.dafang.monitor.nx.statistics.mapper;

import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface BaseMapper {
    List<Map<String, Object>> staInfoList(DailyParam params);
    // 基础数据查询(日 侯 旬 月 季 年)
    List<Map<String, Object>> infoList(String sql);

}
