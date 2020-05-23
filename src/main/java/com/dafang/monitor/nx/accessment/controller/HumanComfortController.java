package com.dafang.monitor.nx.accessment.controller;

import com.dafang.monitor.nx.accessment.entity.dto.AccessmentResuleDto;
import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;
import com.dafang.monitor.nx.accessment.service.HumanComfortService;
import com.dafang.monitor.nx.config.ApiIgp;
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
 * @description:
 * @author: zyj
 * @createDate: 2020/3/31
 * @version: 1.0
 */
@Api(value = "气候评价",tags = {"人体舒适度"})
@RestController
@RequestMapping(value = "accessment/humamComfort/")
public class HumanComfortController {

    @Autowired
    private HumanComfortService humanComfortService;
    @PostMapping(value = "continueList")
    @ApiOperation(value = "连续舒适度指数",notes = "人体舒适度")
    public AccessmentResuleDto<List<Map<String,Object>>> continueList(@ApiIgp("sT,eT,condition,code") @RequestBody ComfortParam params){
        AccessmentResuleDto<List<Map<String,Object>>> resuleDto = new AccessmentResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<Map<String,Object>> continueList = humanComfortService.continueList(params);
        resuleDto.setRespData(continueList);
        if (continueList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "periodList")
    @ApiOperation(value = "同期舒适度指数",notes = "人体舒适度")
    public AccessmentResuleDto<List<Map<String,Object>>> periodList(@ApiIgp("sT,eT,condition,code") @RequestBody ComfortParam params){
        AccessmentResuleDto<List<Map<String,Object>>> resuleDto = new AccessmentResuleDto<>();
        params.setST(params.getStartDate().substring(4));
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<Map<String,Object>> periodList = humanComfortService.periodList(params);
        resuleDto.setRespData(periodList);
        if (periodList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "dailyContinueList")
    @ApiOperation(value = "连续舒适度指数逐日数据",notes = "人体舒适度")
    public AccessmentResuleDto<List<Comfort>> dailyContinueList(@ApiIgp("sT,eT,condition,code,climateScale") @RequestBody ComfortParam params){
        AccessmentResuleDto<List<Comfort>> resuleDto = new AccessmentResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<Comfort> dailyContinueList = humanComfortService.dailyContinueList(params);
        resuleDto.setRespData(dailyContinueList);
        if (dailyContinueList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
    @PostMapping(value = "dailyPeriodList")
    @ApiOperation(value = "同期舒适度逐日指数数据",notes = "人体舒适度")
    public AccessmentResuleDto<List<Comfort>> dailyPeriodList(@ApiIgp("sT,eT,condition,code,climateScale") @RequestBody ComfortParam params){
        AccessmentResuleDto<List<Comfort>> resuleDto = new AccessmentResuleDto<>();
        params.setST(params.getStartDate().substring(4));
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<Comfort> dailyPeriodList = humanComfortService.dailyPeriodList(params);
        resuleDto.setRespData(dailyPeriodList);
        if (dailyPeriodList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
