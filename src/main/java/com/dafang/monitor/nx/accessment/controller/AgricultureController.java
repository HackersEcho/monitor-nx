package com.dafang.monitor.nx.accessment.controller;

import com.dafang.monitor.nx.accessment.entity.po.AccumuTemParam;
import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;
import com.dafang.monitor.nx.accessment.service.AgricultureService;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "气候评价",tags = {"气候与农业"})
@RestController
@RequestMapping(value = "accessment/agriculture/")
public class AgricultureController {

    @Autowired
    private AgricultureService service;

    @PostMapping(value = "dataList")
    @ApiOperation(value = "农业数据",notes = "气候与农业")
    public List<Map<String,Object>> dataList(AgricultureParam params){
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        return service.dataList(params);
    }

    @PostMapping(value = "periodList")
    @ApiOperation(value = "同期积温",notes = "积温")
    public List<Map<String,Object>> periodList(AccumuTemParam params){
        params.setST(params.getStartDate().substring(4));
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setCode("1");
        Integer type = params.getType();
        if(type == 1){
            params.setElement("t.TEM_Avg");
            params.setAccCondition("AND t.TEM_Avg > 10");
        }else if(type == 2){
            params.setElement("t.TEM_Avg-10");
            params.setAccCondition("");
        }else if(type == 3){
            params.setElement("t.TEM_Avg");
            params.setAccCondition("AND t.TEM_Avg < 10");
        }
        return service.periodList(params);
    }

    @PostMapping(value = "continueList")
    @ApiOperation(value = "连续积温",notes = "积温")
    public List<Map<String,Object>> continueList(AccumuTemParam params){
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setCode("1");
        Integer type = params.getType();
        if(type == 1){
            params.setElement("t.TEM_Avg");
            params.setAccCondition("AND t.TEM_Avg > 10");
        }else if(type == 2){
            params.setElement("t.TEM_Avg-10");
            params.setAccCondition(" ");
        }else if(type == 3){
            params.setElement("t.TEM_Avg");
            params.setAccCondition("AND t.TEM_Avg < 10");
        }
        return service.continueList(params);
    }

}
