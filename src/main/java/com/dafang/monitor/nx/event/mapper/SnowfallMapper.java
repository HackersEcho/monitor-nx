package com.dafang.monitor.nx.event.mapper;

import com.dafang.monitor.nx.event.entity.po.SnowfallParam;
import com.dafang.monitor.nx.event.entity.po.TheFirstRainParam;
import com.dafang.monitor.nx.event.entity.vo.SnowFall;
import com.dafang.monitor.nx.event.entity.vo.SnowFallDaily;
import com.dafang.monitor.nx.event.entity.vo.TheFirstRain;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface SnowfallMapper extends MyMapper<SnowFall> {

    //查询降雪初终日数据
    List<SnowFallDaily> selectSnowFallSDateStatisticsData(SnowfallParam snowfallParam);
}
