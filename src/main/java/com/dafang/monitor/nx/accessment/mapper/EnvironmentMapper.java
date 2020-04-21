package com.dafang.monitor.nx.accessment.mapper;

import com.dafang.monitor.nx.accessment.entity.po.Environment;
import com.dafang.monitor.nx.accessment.entity.po.EnvironmentParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface EnvironmentMapper extends MyMapper<Environment> {

    /*
    查询环境数据
     */
    List<Environment> dataList(EnvironmentParam params);
    /*
    查询环境数据总条数
     */
    int dataCount(EnvironmentParam params);
}
