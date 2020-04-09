package com.dafang.monitor.nx.accessment.mapper;

import com.dafang.monitor.nx.accessment.entity.po.Agriculture;
import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface AgricultureMapper extends MyMapper<Agriculture> {

    /*
    查询农作物所需基础数据（气温，降水，日照）
     */
    List<Agriculture> baseDataList(AgricultureParam params);

}
