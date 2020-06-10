package com.dafang.monitor.nx.event.mapper;

import com.dafang.monitor.nx.event.entity.po.TheFirstRainParam;
import com.dafang.monitor.nx.event.entity.vo.TheFirstRain;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface TheFristRainMapper extends MyMapper<TheFirstRain> {

    List<Daily> selectTheFirstRainRainfall(TheFirstRainParam theFirstRainParam);
}
