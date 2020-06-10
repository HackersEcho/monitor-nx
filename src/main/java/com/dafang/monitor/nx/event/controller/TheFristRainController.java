package com.dafang.monitor.nx.event.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.event.entity.po.TheFirstRainParam;
import com.dafang.monitor.nx.event.entity.vo.TheFirstRain;
import com.dafang.monitor.nx.event.service.TheFristRainService;
import com.dafang.monitor.nx.event.service.VariableTempService;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
import com.dafang.monitor.nx.statistics.entity.vo.ContinuousDays;
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
 * @author: RenXin
 * @createDate: 2020/06/08
 * @version: 1.0
 */
@RestController
@Api(value = "首场透雨Controller",tags = {"首场透雨"})
@RequestMapping(value = "fristRain/")
@Slf4j
public class TheFristRainController {

    @Autowired
    private TheFristRainService theFristRainService;

    @PostMapping(value = "rainfall")
    @ApiOperation(value = "首场透雨雨量查询",notes = "透雨雨量")
    public ResuleDto<List<TheFirstRain>> getSoakingDateStatistics(
            @Apicp("regions,startYear,endYear,climateScale,rankStartYear,rankEndYear")
            @RequestBody TheFirstRainParam theFirstRainParam){
        ResuleDto<List<TheFirstRain>> resuleDto = new ResuleDto<>();
        theFirstRainParam.setCondition(CommonUtils.getCondition(theFirstRainParam.getRegions()));
        List<TheFirstRain> list = theFristRainService.getSoakingDateStatistics(theFirstRainParam);
        resuleDto.setRespData(list);
        if (list.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;

    }
}
