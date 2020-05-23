package com.dafang.monitor.nx.accessment.controller;


import com.dafang.monitor.nx.accessment.entity.dto.AccessmentResuleDto;
import com.dafang.monitor.nx.accessment.entity.po.WaterResourceparam;
import com.dafang.monitor.nx.accessment.service.WaterResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "气候评价",tags = {"气候与水资源"})
@RestController
@RequestMapping(value = "accessment/waterResource")
public class WaterResourceController {

    @Autowired
    private WaterResourceService service;

    @PostMapping(value = "dataList")
    @ApiOperation(value = "气候与水资源",notes = "气候与水资源")
    public AccessmentResuleDto<List<Map<String,Object>>> dataList(@RequestBody WaterResourceparam params){
        AccessmentResuleDto<List<Map<String,Object>>> resuleDto = new AccessmentResuleDto<>();
        List<Map<String,Object>> dataList = service.dataList(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
