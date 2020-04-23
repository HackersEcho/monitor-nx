package com.dafang.monitor.nx.accessment.service;

import com.dafang.monitor.nx.accessment.entity.po.Environment;
import com.dafang.monitor.nx.accessment.entity.po.EnvironmentParam;

import java.util.List;
import java.util.Map;

public interface EnvironmentService {

    /*
    气候与环境数据接口
     */
    Map<String,Object> dataList(EnvironmentParam params);

}
