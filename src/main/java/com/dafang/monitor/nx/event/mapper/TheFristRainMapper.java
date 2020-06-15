package com.dafang.monitor.nx.event.mapper;

import com.dafang.monitor.nx.event.entity.po.TheFirstRainParam;
import com.dafang.monitor.nx.event.entity.vo.TheFirstRain;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface TheFristRainMapper extends MyMapper<TheFirstRain> {
    //首场透雨雨量
    List<Daily> selectTheFirstRainRainfall(TheFirstRainParam theFirstRainParam);
    //首场透雨日期
    List<Daily> selectTheFirstRainRainDate(TheFirstRainParam theFirstRainParam);
    //区域站点查询
    List<Daily> selectStationGroup(@Param("stations") String[] stations);
    //首场透雨站点查询
    List<Daily> selectTheFirstRainStations(String[] stationNo, String startMonthDay, String endMonthDay);
}
