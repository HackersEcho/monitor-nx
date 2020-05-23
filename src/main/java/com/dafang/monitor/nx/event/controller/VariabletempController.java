package com.dafang.monitor.nx.event.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.event.entity.po.ColdWaveParam;
import com.dafang.monitor.nx.event.entity.po.VariableTempParam;
import com.dafang.monitor.nx.event.entity.vo.CountiousSta;
import com.dafang.monitor.nx.event.entity.vo.VariableTempVO;
import com.dafang.monitor.nx.event.service.ColdWaveService;
import com.dafang.monitor.nx.event.service.VariableTempService;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
import com.dafang.monitor.nx.statistics.entity.vo.ContinuousDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodStas;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: my
 * @createDate: 2020/5/19
 * @version: 1.0
 */
@RestController
@Api(value = "echo",tags = {"变温"})
@RequestMapping(value = "variabletemp/")
@Slf4j
public class VariabletempController {
    @Autowired
    private VariableTempService service;
    @PostMapping(value = "continuous")
    @ApiOperation(value = "变温幅度/次数连续数据统计",notes = "连续变温幅度/次数")
    public ResuleDto<List<VariableTempVO>> VariableTempContinuous(@Apicp("regions,startDate,endDate,variableTime," +
            "temType,threshold") @RequestBody VariableTempParam params){
        ResuleDto<List<VariableTempVO>> resuleDto = new ResuleDto<>();
        params.setRemark("VariableTemp");
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<VariableTempVO> continuouDays = service.variableTempContinuous(params);
        resuleDto.setRespData(continuouDays);
        if (continuouDays.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }

    @PostMapping(value = "period")
    @ApiOperation(value = "变温幅度/次数同期数据统计",notes = "同期变温幅度/次数")
    public ResuleDto<List<VariableTempVO>> periodDays(@Apicp("regions,startDate,endDate,variableTime," +
            "temType,threshold") @RequestBody VariableTempParam params){
        ResuleDto<List<VariableTempVO>> resuleDto = new ResuleDto<>();
        params.setRemark("VariableTemp");
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<VariableTempVO> periodDays = service.periodDays(params);
        resuleDto.setRespData(periodDays);
        if (periodDays.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
