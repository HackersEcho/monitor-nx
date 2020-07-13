package com.dafang.monitor.nx.statistics.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExportDataMapper {
    //查询数据
    List<Map<String, Object>> getBasicsDataQueryDaoByRegion(String table, String col,String colStr, String startTime, String endTime, String condition, String min, String max, int startIndex, int endIndex);
    //查询总条数
    int selectCount(String table,String col, String startTime, String endTime, String condition,String min, String max);

}
