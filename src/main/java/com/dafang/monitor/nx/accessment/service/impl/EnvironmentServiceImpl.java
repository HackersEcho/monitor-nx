package com.dafang.monitor.nx.accessment.service.impl;

import com.dafang.monitor.nx.accessment.entity.po.Environment;
import com.dafang.monitor.nx.accessment.entity.po.EnvironmentParam;
import com.dafang.monitor.nx.accessment.mapper.EnvironmentMapper;
import com.dafang.monitor.nx.accessment.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {

    @Autowired
    private EnvironmentMapper mapper;

    @Override
    public Map<String,Object> dataList(EnvironmentParam params) {
        Map<String, Object> results = new HashMap<>();
        results.put("data",mapper.dataList(params));
        results.put("total",mapper.dataCount(params));
        return results;
    }
}
