package com.dafang.monitor.nx.event.service;

import com.dafang.monitor.nx.event.entity.po.SnowfallParam;
import com.dafang.monitor.nx.event.entity.vo.SnowFall;

import java.util.List;

public interface SnowfallService {
    //降雪初终日
    List<SnowFall> getSnowFallSDateStatisticsData(SnowfallParam snowfallParam);
}
