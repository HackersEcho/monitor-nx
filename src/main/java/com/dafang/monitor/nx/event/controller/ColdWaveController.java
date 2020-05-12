package com.dafang.monitor.nx.event.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.event.entity.po.ColdWaveParam;
import com.dafang.monitor.nx.event.entity.vo.CountiousSta;
import com.dafang.monitor.nx.event.service.ColdWaveService;
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
 * @author: echo
 * @createDate: 2020/5/11
 * @version: 1.0
 */
@RestController
@Api(value = "echo",tags = {"日数处理"})
@RequestMapping(value = "coldWave/")
@Slf4j
public class ColdWaveController {
    @Autowired
    private ColdWaveService service;
    @PostMapping(value = "period")
    @ApiOperation(value = "寒潮站次同期数据查询",notes = "同期站次")
    public ResuleDto<List<PeriodDays>> periodDays(@Apicp("regions,startDate,endDate,climateScale," +
            "clodWaveLeavel,rankStartYear,rankEndYear") @RequestBody ColdWaveParam params){
        ResuleDto<List<PeriodDays>> resuleDto = new ResuleDto<>();
        params.setST(params.getStartDate().substring(4));
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<PeriodDays> periodDays = service.periodDays(params);
        resuleDto.setRespData(periodDays);
        if (periodDays.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "continuous")
    @ApiOperation(value = "寒潮站次连续数据查询",notes = "连续站次")
    public ResuleDto<List<ContinuousDays>> continuousDays(@Apicp("regions,startDate,endDate," +
            "clodWaveLeavel") @RequestBody ColdWaveParam params){
        ResuleDto<List<ContinuousDays>> resuleDto = new ResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<ContinuousDays> continuousDays = service.continuousDays(params);
        resuleDto.setRespData(continuousDays);
        if (continuousDays.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "periodSta")
    @ApiOperation(value = "寒潮站数同期数据查询",notes = "同期站数")
    public ResuleDto<List<PeriodStas>> PeriodStas(@Apicp("regions,startDate,endDate,climateScale," +
            "clodWaveLeavel,rankStartYear,rankEndYear") @RequestBody ColdWaveParam params){
        ResuleDto<List<PeriodStas>> resuleDto = new ResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<PeriodStas> periodStas = service.periodSta(params);
        resuleDto.setRespData(periodStas);
        if (periodStas.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "continuousSta")
    @ApiOperation(value = "寒潮连续站数查询",notes = "连续站数")
    public ResuleDto<List<CountiousSta>> countiousSta(@Apicp("regions,startDate,endDate," +
            "clodWaveLeavel") @RequestBody ColdWaveParam params){
        ResuleDto<List<CountiousSta>> resuleDto = new ResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<CountiousSta> continuousSta = service.countiousSta(params);
        resuleDto.setRespData(continuousSta);
        if (continuousSta.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
