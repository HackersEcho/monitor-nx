package com.dafang.monitor.nx.accessment.controller;

import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;
import com.dafang.monitor.nx.accessment.service.AgricultureService;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "气候评价",tags = {"气候与农业"})
@RestController
@RequestMapping(value = "accessment/Agriculture/")
public class AgricultureController {

    @Autowired
    AgricultureService service;

    @PostMapping(value = "dataList")
    @ApiOperation(value = "农业数据",notes = "气候与农业")
    public List<Map<String,Object>> dataList(AgricultureParam params){
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        return service.dataList(params);
    }
}
