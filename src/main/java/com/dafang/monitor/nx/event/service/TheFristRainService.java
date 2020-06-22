package com.dafang.monitor.nx.event.service;

import com.dafang.monitor.nx.event.entity.po.TheFirstRainParam;
import com.dafang.monitor.nx.event.entity.vo.TheFirstRain;

import java.util.List;

public interface TheFristRainService {
    //首场透雨雨量
    List<TheFirstRain> getSoakingPreStatistics(TheFirstRainParam theFirstRainParam);
    //首场透雨日期
    List<TheFirstRain> getSoakingDateStatistics(TheFirstRainParam theFirstRainParam);
    //首场透雨站数
    List<TheFirstRain> getSoakingStationsStatistics(TheFirstRainParam theFirstRainParam);
}
