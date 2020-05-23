package com.dafang.monitor.nx.accessment.mapper;

import com.dafang.monitor.nx.accessment.entity.po.WaterResource;
import com.dafang.monitor.nx.accessment.entity.po.WaterResourceparam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface WaterResourceMapper extends MyMapper<WaterResource> {

    /*查询年降水量*/
    List<WaterResource> preList();

}
