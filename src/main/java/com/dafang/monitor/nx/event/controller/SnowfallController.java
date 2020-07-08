package com.dafang.monitor.nx.event.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.event.entity.po.SnowfallParam;
import com.dafang.monitor.nx.event.entity.po.TheFirstRainParam;
import com.dafang.monitor.nx.event.entity.vo.SnowFall;
import com.dafang.monitor.nx.event.entity.vo.TheFirstRain;
import com.dafang.monitor.nx.event.service.SnowfallService;
import com.dafang.monitor.nx.event.service.TheFristRainService;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
import com.dafang.monitor.nx.statistics.entity.po.Daily;
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
 * @createDate: 2020/06/22
 * @version: 1.0
 */
@RestController
@Api(value = "降雪Controller",tags = {"降雪"})
@RequestMapping(value = "snowfall/")
@Slf4j
public class SnowfallController {

    @Autowired
    private SnowfallService snowfallService;

    @PostMapping(value = "seDay")
    @ApiOperation(value = "降雪初终日",notes = "降雪初终日")
    public ResuleDto<List<SnowFall>> getSnowFallSDateStatisticsData(
            @Apicp("regions,startYear,endYear,climateScale,type")
            @RequestBody SnowfallParam snowfallParam){
        ResuleDto<List<SnowFall>> resuleDto = new ResuleDto<>();
        snowfallParam.setCondition(CommonUtils.getCondition(snowfallParam.getRegions()));
        List<SnowFall> list = snowfallService.getSnowFallSDateStatisticsData(snowfallParam);
        resuleDto.setRespData(list);
        if (list.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;

    }

}
