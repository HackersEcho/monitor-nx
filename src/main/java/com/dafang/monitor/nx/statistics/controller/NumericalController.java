package com.dafang.monitor.nx.statistics.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.ComprehensiveExtrem;
import com.dafang.monitor.nx.statistics.entity.vo.FirstDays;
import com.dafang.monitor.nx.statistics.service.NumericalService;
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

/**
 * @description:数值型数据统计
 * @author: echo
 * @createDate: 2020/4/2
 * @version: 1.0
 */
@Api(value = "echo",tags = {"综合统计处理处理"})
@RestController
@RequestMapping(value = "numerical/")
public class NumericalController {
    @Autowired
    private NumericalService service;
    @PostMapping(value = "comprehensive")
    @ApiOperation(value = "综合统计",notes = "平均值 累计值 最大值 最小值")
    public ResuleDto<Object> comprehensive(@Apicp("regions,startDate,endDate,opType,element,min,max") @RequestBody DailyParam params){
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        ResuleDto<Object> resuleDto = new ResuleDto<>();
        List<Map<String, Object>> mapList = service.comprehensiveStatistic(params);
        resuleDto.setRespData(mapList);
        if (mapList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "extrem")
    @ApiOperation(value = "极值统计",notes = "统计最大值 最小值 极大值 极小值...")
    public ResuleDto<List<ComprehensiveExtrem>> extrem(@Apicp("regions,startDate,endDate,climateScale,element,rankStartYear,rankEndYear") @RequestBody DailyParam params){
        ResuleDto<List<ComprehensiveExtrem>> resuleDto = new ResuleDto<>();
        params.setMin(-999d);
        params.setMax(999d);
        params.setST(params.getStartDate().substring(4));
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<ComprehensiveExtrem> extremList = service.extrem(params);
        resuleDto.setRespData(extremList);
        if (extremList.size()==0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "firstDays")
    @ApiOperation(value = "初终日",notes = "高温、低温...")
    public ResuleDto<List<FirstDays>> firstDays(@Apicp("regions,startDate,endDate,climateScale,element,min,max,rankStartYear,rankEndYear,code") @RequestBody DailyParam params){
        ResuleDto<List<FirstDays>> resuleDto = new ResuleDto<>();
        params.setST(params.getStartDate().substring(4));
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<FirstDays> extremList = service.firstDays(params);
        resuleDto.setRespData(extremList);
        if (extremList.size()==0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
