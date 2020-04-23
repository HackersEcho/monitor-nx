package com.dafang.monitor.nx.accessment.controller;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.accessment.entity.dto.AccessmentResuleDto;
import com.dafang.monitor.nx.accessment.entity.po.Environment;
import com.dafang.monitor.nx.accessment.entity.po.EnvironmentParam;
import com.dafang.monitor.nx.accessment.service.EnvironmentService;
import com.dafang.monitor.nx.config.ApiIgp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "气候评价",tags = {"气候与环境"})
@RestController
@RequestMapping(value = "accessment/Environment")
public class EnvironmentController {

    @Autowired
    private EnvironmentService service;

    /*
    气候与环境
     */
    @PostMapping(value = "dataList")
    @ApiOperation(value = "气候与环境数据",notes = "气候与环境")
    public AccessmentResuleDto<Map<String, Object>> dataList(@ApiIgp("start") @RequestBody EnvironmentParam params){
        AccessmentResuleDto<Map<String, Object>> resuleDto = new AccessmentResuleDto<>();
        String area = params.getArea();
        Double page = Convert.toDouble(params.getPage());
        Double rows = Convert.toDouble(params.getRows());
        //根据第几页和每页行数算出索引开始位置
        params.setStart((page-1)*rows+"");
        Map<String,Object> dataList = service.dataList(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }

}
