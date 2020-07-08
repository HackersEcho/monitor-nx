package com.dafang.monitor.nx.drought.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.drought.entity.dto.DroughtDto;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.service.Impl.SPIServiceImpl;
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

@RestController
@Api(value = "干旱" ,tags = {"标准化降水"})
@RequestMapping(value = "drought/SPI")
public class SPIController {

    @Autowired
    SPIServiceImpl spiService;

    @PostMapping(value = "getContinueSPI")
    @ApiOperation(value = "连续标准化降水(SPI)",notes = "连续标准化降水(SPI)")
    public DroughtDto<List<Map<String,Object>>> getContinueSPI(@Apicp("startDate,endDate,regions") @RequestBody DroughtParam params){
        DroughtDto<List<Map<String,Object>>> resuleDto = new DroughtDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setMax(999.0);
        params.setMin(-999.0);
        params.setElement("PRE_Time_2020");
        params.setCode("1");
        List<Map<String,Object>> dataList = spiService.getContinueSPI(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }

    @PostMapping(value = "getPeriodSPI")
    @ApiOperation(value = "同期标准化降水(SPI)",notes = "同期标准化降水(SPI)")
    public DroughtDto<List<Map<String,Object>>> getCI(@Apicp("startDate,endDate,regions") @RequestBody DroughtParam params){
        DroughtDto<List<Map<String,Object>>> resuleDto = new DroughtDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setMax(999.0);
        params.setMin(-999.0);
        params.setST(params.getStartDate().substring(4,8));
        params.setET(params.getEndDate().substring(4,8));
        params.setElement("PRE_Time_2020");
        params.setCode("1");
        List<Map<String,Object>> dataList = spiService.getPeriodSPI(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }

}
