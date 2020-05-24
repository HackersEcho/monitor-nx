package com.dafang.monitor.nx.event.mapper;

import com.dafang.monitor.nx.event.entity.po.VariableTempParam;
import com.dafang.monitor.nx.event.entity.vo.VariableTempVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName VariableTempMapper
 * @Description TODO
 * @Author my
 * @Date 2020/5/20
 **/
@Mapper
public interface VariableTempMapper {
    List<Map<String, Object>> getVariableTempContinuousData(VariableTempParam params);

    List<Map<String, Object>> getVariableTempPeriodData(VariableTempParam params);
}
