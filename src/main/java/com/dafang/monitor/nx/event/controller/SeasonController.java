package com.dafang.monitor.nx.event.controller;

import com.dafang.monitor.nx.config.ApiIgp;
import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.event.entity.po.SeasonParam;
import com.dafang.monitor.nx.event.entity.vo.FourSeason;
import com.dafang.monitor.nx.event.service.SeasonService;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
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
 * @description:四季入季
 * @author: echo
 * @createDate: 2020/5/7
 * @version: 1.0
 */
@Api(value = "echo",tags = {"四季入季处理"})
@RestController
@Slf4j
@RequestMapping(value = "season/")
public class SeasonController {
    @Autowired
    private SeasonService service;

    @PostMapping(value = "inOut")
    @ApiOperation(value = "四季入季数据查询",notes = "季节开始或者季节结束")
    public ResuleDto<List<FourSeason>> inOutSeason(@Apicp("regions,startYear,endYear,climateScale,seasonType,timeType") @RequestBody SeasonParam params){
        log.info("四季入季数据查询");
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        ResuleDto<List<FourSeason>> resuleDto = new ResuleDto<>();
        List<FourSeason> fourSeasons = service.inOutSeason(params);
        resuleDto.setRespData(fourSeasons);
        if (fourSeasons.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("输入条件有误,该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "len")
    @ApiOperation(value = "四季入季长度统计",notes = "季节长度")
    public ResuleDto<List<FourSeason>> seasonLen(@Apicp("regions,startYear,endYear,climateScale,seasonType") @RequestBody SeasonParam params){
        log.info("四季入季长度数据查询");
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        ResuleDto<List<FourSeason>> resuleDto = new ResuleDto<>();
        List<FourSeason> fourSeasons = service.seasonLen(params);
        resuleDto.setRespData(fourSeasons);
        if (fourSeasons.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("输入条件有误,该条件下无数据");
        }
        return resuleDto;
    }
}
