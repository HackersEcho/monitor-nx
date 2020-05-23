package com.dafang.monitor.nx.event.mapper;

import com.dafang.monitor.nx.event.entity.po.SeasonParam;
import com.dafang.monitor.nx.event.entity.vo.EventDaily;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.MyMapper;

import java.util.List;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/4/23
 * @version: 1.0
 */
@Mapper
public interface SeasonMapper extends MyMapper<EventDaily> {
    List<EventDaily> seasonData(SeasonParam params);
    List<EventDaily> seasonLen(SeasonParam params);
}
