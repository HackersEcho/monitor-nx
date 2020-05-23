package com.dafang.monitor.nx.event.mapper;

import com.dafang.monitor.nx.event.entity.po.FrostParam;
import com.dafang.monitor.nx.event.entity.po.FrostPo;
import com.dafang.monitor.nx.event.entity.po.FrostStaParam;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.MyMapper;

import java.util.List;

/**
 * @description:霜冻
 * @author: echo
 * @createDate: 2020/5/15
 * @version: 1.0
 */
@Mapper
public interface FrostMapper extends MyMapper<Daily> {
    List<FrostPo> periodDatas(FrostParam params);// 霜冻数据查询
    List<FrostPo> periodStas(FrostStaParam params);// 霜冻站数数据查询
}
