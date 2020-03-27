package com.dafang.monitor.nx.statistics.controller;

import com.dafang.monitor.nx.config.ApiIgp;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.ContinuousDays;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;
import com.dafang.monitor.nx.statistics.service.DaysService;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/14
 * @version: 1.0
 */
@Api(value = "echo",tags = {"日数处理"})
@RestController
@RequestMapping(value = "days/")
public class DaysController {
    @Autowired
    private DaysService service;
    @PostMapping(value = "period")
    @ApiOperation(value = "同期日数数据查询",notes = "同期日数")
    public ResuleDto<List<PeriodDays>> periodDays(@ApiIgp("sT,eT,condition,serialVersionUID") @RequestBody  DailyParam params){
        ResuleDto<List<PeriodDays>> resuleDto = new ResuleDto<>();
        params.setST(params.getStartDate().substring(4));
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<PeriodDays> periodDays = service.periodDays(params);
        if (periodDays.size()>0){
            resuleDto.setRespData(periodDays);
        }else {
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "continuous")
    @ApiOperation(value = "查询时间段日数数据查询",notes = "连续日数")
    public ResuleDto<List<ContinuousDays>> continuousDays(@ApiIgp("sT,eT,condition,climateScale,serialVersionUID") @RequestBody  DailyParam params){
        ResuleDto<List<ContinuousDays>> resuleDto = new ResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<ContinuousDays> continuousDays = service.continuousDays(params);
        if (continuousDays.size()>0){
            resuleDto.setRespData(continuousDays);
        }else {
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
