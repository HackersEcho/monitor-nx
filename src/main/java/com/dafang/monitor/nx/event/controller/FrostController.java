package com.dafang.monitor.nx.event.controller;

import com.dafang.monitor.nx.event.entity.po.FrostParam;
import com.dafang.monitor.nx.event.entity.po.FrostStaParam;
import com.dafang.monitor.nx.event.entity.vo.DateStatistic;
import com.dafang.monitor.nx.event.service.FrostService;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
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
@Api(value = "echo",tags = {"霜冻"})
@RequestMapping(value = "frost/")
@Slf4j
public class FrostController {
    @Autowired
    private FrostService service;
    @PostMapping(value = "period")
    @ApiOperation(value = "霜冻日数数据查询",notes = "霜冻日数")
    public ResuleDto<List<PeriodDays>> periodDays(@RequestBody FrostParam params){
        ResuleDto<List<PeriodDays>> resuleDto = new ResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setTimeType(null);
        List<PeriodDays> periodDays = service.periodDays(params);
        resuleDto.setRespData(periodDays);
        if (periodDays.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "date")
    @ApiOperation(value = "霜冻日期数据查询",notes = "霜冻日期(低霜  重霜)")
    public ResuleDto<List<DateStatistic>> periodDate(@RequestBody FrostParam params){
        ResuleDto<List<DateStatistic>> resuleDto = new ResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<DateStatistic> periodDays = service.periodDates(params);
        resuleDto.setRespData(periodDays);
        if (periodDays.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "periodSta")
    @ApiOperation(value = "霜冻站数同期数据查询",notes = "同期站数")
    public ResuleDto<List<PeriodStas>> PeriodStas(@RequestBody FrostStaParam params){
        ResuleDto<List<PeriodStas>> resuleDto = new ResuleDto<>();
        // 默认直接查询全区的数据
        params.setCondition(CommonUtils.getCondition("1"));
        List<PeriodStas> periodStas = service.periodSta(params);
        resuleDto.setRespData(periodStas);
        if (periodStas.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
