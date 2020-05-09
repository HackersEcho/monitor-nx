package com.dafang.monitor.nx.product.controller;

import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.impl.*;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "产品",tags = {"产品"})
@RestController
@RequestMapping(value = "product/")
public class ProductController {

    @Autowired
    ClimateInfo method1;
    @Autowired
    DroughtMonitor method2;
    @Autowired
    ExtremWeatherEvent method3;
    @Autowired
    DecisionService method4;
    @Autowired
    SpecialReport method5;
    @Autowired
    ClimateProfile method6;

    @PostMapping(value = "creatProduct")
    @ApiOperation(value = "产品生成",notes = "产品")
    //生成气候监测产品
    public void creatProduct(ProductParams params) throws Exception {
        //重要气候信息
//        params.setStartDate("20190101");
//        params.setEndDate("20190201");
//        params.setST("0101");
//        params.setET("0201");
//        params.setRegions("1");
//        params.setElement("t.TEM_Avg,t.PRE_Time_2020");
//        params.setCondition(CommonUtils.getCondition(params.getRegions()));
//        method1.entrance(params);
        //干旱监测报告
//        params.setStartDate("20190101");
//        method2.entrance(params);
        //极端天气气候事件报告
//        params.setStartDate("201901");
//        params.setST(params.getStartDate().substring(4,6)+"01");
//        params.setET(params.getStartDate().substring(4,6)+"31");
//        params.setRegions("1");
//        params.setCondition(CommonUtils.getCondition(params.getRegions()));
//        params.setMax(999d);
//        params.setMin(-999d);
//        method3.entrance(params);
        //决策服务
//        params.setStartDate("20190101");
//        params.setEndDate("20190110");
//        params.setST("0101");
//        params.setET("0110");
//        params.setRegions("1");
//        params.setCondition(CommonUtils.getCondition(params.getRegions()));
//        params.setMax(999d);
//        params.setMin(-999d);
//        params.setElement("PRE_Time_2020");
//        method4.entrance(params);
        //专题报告
        params.setStartDate("20190101");
        params.setEndDate("20190201");
        params.setST("0101");
        params.setET("0201");
        params.setRegions("1");
        params.setElement("t.TEM_Avg,t.PRE_Time_2020");
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setMax(999d);
        params.setMin(-999d);
        method5.entrance(params);
//        //气候概况
//        params.setStartDate("20190101");
//        params.setEndDate("20190201");
//        params.setST("0101");
//        params.setET("0201");
//        params.setRegions("1");
//        params.setCondition(CommonUtils.getCondition(params.getRegions()));
//        params.setMax(999d);
//        params.setMin(-999d);
//        method6.entrance(params);

    }

}
