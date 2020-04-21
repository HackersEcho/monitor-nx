package com.dafang.monitor.nx.accessment.controller;

import com.dafang.monitor.nx.accessment.entity.dto.AccessmentResuleDto;
import com.dafang.monitor.nx.accessment.entity.po.AccumuTemParam;
import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;
import com.dafang.monitor.nx.accessment.service.AgricultureService;
import com.dafang.monitor.nx.config.ApiIgp;
import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @ApiOperation(value = "气候与农业数据",notes = "气候与农业")
    public AccessmentResuleDto<List<Map<String,Object>>> dataList(@ApiIgp("sT,eT,condition") @RequestBody AgricultureParam params){
        AccessmentResuleDto<List<Map<String,Object>>> resuleDto = new AccessmentResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<Map<String,Object>> dataList = service.dataList(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }

    @PostMapping(value = "periodList")
    @ApiOperation(value = "同期积温",notes = "积温")
    public AccessmentResuleDto<List<Map<String,Object>>> periodList(@Apicp ("regions,startDate,endDate,climateScale,type") @RequestBody AccumuTemParam params){
        AccessmentResuleDto<List<Map<String,Object>>> resuleDto = new AccessmentResuleDto<>();
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
        List<Map<String,Object>> periodList = service.periodList(params);
        resuleDto.setRespData(periodList);
        if (periodList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }

    @PostMapping(value = "continueList")
    @ApiOperation(value = "连续积温",notes = "积温")
    public AccessmentResuleDto<List<Map<String,Object>>>  continueList(@Apicp ("regions,startDate,endDate,type") @RequestBody AccumuTemParam params){
        AccessmentResuleDto<List<Map<String,Object>>> resuleDto = new AccessmentResuleDto<>();
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
        List<Map<String,Object>> continueList = service.continueList(params);
        resuleDto.setRespData(continueList);
        if (continueList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }

}
