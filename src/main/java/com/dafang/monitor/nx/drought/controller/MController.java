package com.dafang.monitor.nx.drought.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.drought.entity.dto.DroughtDto;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.service.Impl.MServiceImpl;
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

@Api(value = "干旱",tags = {"相对湿润度"})
@RestController
@RequestMapping(value = "drought/M/")
public class MController {

    @Autowired
    MServiceImpl MService;

    @PostMapping(value = "getContinumM")
    @ApiOperation(value = "连续相对湿润度(M)",notes = "连续相对湿润度(M)")
    public DroughtDto<List<Map<String,Object>>> getContinumM(@Apicp("startDate,endDate,regions") @RequestBody DroughtParam params){
        DroughtDto<List<Map<String,Object>>> resuleDto = new DroughtDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setMax(999.0);
        params.setMin(-999.0);
        List<Map<String,Object>> dataList = MService.getContiuneM(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "getPeriodM")
    @ApiOperation(value = "同期相对湿润度(M)",notes = "同期相对湿润度(M)")
    public DroughtDto<List<Map<String,Object>>> getPeriodM(@Apicp("startDate,endDate,regions") @RequestBody DroughtParam params){
        DroughtDto<List<Map<String,Object>>> resuleDto = new DroughtDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setMax(999.0);
        params.setMin(-999.0);
        List<Map<String,Object>> dataList = MService.getPeriodM(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
