package com.dafang.monitor.nx.accessment.controller;

import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;
import com.dafang.monitor.nx.accessment.service.HumanComfortService;
import com.dafang.monitor.nx.config.ApiIgp;
import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.entity.vo.PeriodDays;
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
    public List<Map<String,Object>> continueList(@RequestBody ComfortParam params){
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<Map<String,Object>> results = humanComfortService.continueList(params);
        return results;
    }
    @PostMapping(value = "periodList")
    @ApiOperation(value = "同期舒适度指数",notes = "人体舒适度")
    public List<Map<String,Object>> periodList(@RequestBody ComfortParam params){
        params.setST(params.getStartDate().substring(4));
        params.setET(params.getEndDate().substring(4));
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        List<Map<String,Object>> results = humanComfortService.periodList(params);
        return results;
    }
    @PostMapping(value = "dailyContinueList")
    @ApiOperation(value = "连续舒适度指数逐日数据",notes = "人体舒适度")
    public List<Comfort> dailyContinueList(@RequestBody ComfortParam params){
        return humanComfortService.dailyContinueList(params);
    }
    @PostMapping(value = "dailyPeriodList")
    @ApiOperation(value = "同期舒适度逐日指数数据",notes = "人体舒适度")
    public List<Comfort> dailyPeriodList(@RequestBody ComfortParam params){
        return humanComfortService.dailyPeriodList(params);
    }
}
