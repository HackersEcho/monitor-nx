package com.dafang.monitor.nx.accessment.controller;

import com.dafang.monitor.nx.accessment.entity.po.Comfort;
import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;
import com.dafang.monitor.nx.accessment.service.HumanComfortService;
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
 * @author: zyj
 * @createDate: 2020/3/31
 * @version: 1.0
 */
@Api(value = "zyj",tags = {"人体舒适度处理"})
@RestController
@RequestMapping(value = "accessment/humamComfort/")
public class HumanComfortController {

    @Autowired
    private HumanComfortService humanComfortService;

//    @PostMapping(value = "queryHumanComfort")
//    @ApiOperation(value = "舒适度指数",notes = "人体舒适度")
//    public List<Comfort> queryHumanComfort(@RequestBody ComfortParam comfortParam){
//        List<Comfort> results = humanComfortService.queryComfortValue(comfortParam);
//        return results;
//    }

}
