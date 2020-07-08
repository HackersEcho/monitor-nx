package com.dafang.monitor.nx.drought.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.drought.entity.dto.DroughtDto;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.service.Impl.PEServiceImpl;
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

@Api(value = "干旱",tags = {"蒸散量"})
@RestController
@RequestMapping(value = "drought/PE/")
public class PEController {

    @Autowired
    PEServiceImpl PEService;

    @PostMapping(value = "getContinumPE")
    @ApiOperation(value = "连续蒸散量(PE)",notes = "连续蒸散量(PE)")
    public DroughtDto<List<Map<String,Object>>> getContinumPE(@Apicp("startDate,endDate,regions") @RequestBody DroughtParam params){
        DroughtDto<List<Map<String,Object>>> resuleDto = new DroughtDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setElement("PE_VAL");
        params.setCode("0");
        params.setMax(999.0);
        params.setMin(-999.0);
        List<Map<String,Object>> dataList = PEService.getContinue(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "getPeriodPE")
    @ApiOperation(value = "同期蒸散量(PE)",notes = "同期蒸散量(PE)")
    public DroughtDto<List<Map<String,Object>>> getPeriodPE(@Apicp("startDate,endDate,regions") @RequestBody DroughtParam params){
        DroughtDto<List<Map<String,Object>>> resuleDto = new DroughtDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setElement("PE_VAL");
        params.setCode("0");
        params.setMax(999.0);
        params.setMin(-999.0);
        List<Map<String,Object>> dataList = PEService.getPeriod(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
