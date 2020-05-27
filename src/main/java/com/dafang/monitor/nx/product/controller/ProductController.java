package com.dafang.monitor.nx.product.controller;

import com.dafang.monitor.nx.config.ApiIgp;
import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.product.entity.po.Directory;
import com.dafang.monitor.nx.product.entity.po.DirectoryParams;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.impl.DirectoryService;
import com.dafang.monitor.nx.product.impl.evaluate.*;
import com.dafang.monitor.nx.product.impl.evaluate.climateAccessment.ClimateChangeReportService;
import com.dafang.monitor.nx.product.impl.monitor.*;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    @Autowired
    MonthClimateImpact evaMethod1;
    @Autowired
    SeasonClimateImpact evaMethod2;
    @Autowired
    YearClimateImpact evaMethod3;
    @Autowired
    ClimateAndHumanComfort evaMethod4;
    @Autowired
    ClimateAndWaterResources evaMethod5;
    @Autowired
    ClimateAndAgriculture evaMethod6;
    @Autowired
    ClimateAndVegetation evaMethod7;
    @Autowired
    ClimateAndElectricity evaMethod8;
    @Autowired
    ClimateAndAirQuality evaMethod9;
    @Autowired
    ClimateAndTransportation evaMethod10;
    @Autowired
    ClimateAndTourism evaMethod11;
    @Resource
    ClimateChangeReportService evaMethod12;

    @Autowired
    DirectoryService directoryService;


    @PostMapping(value = "creatProduct")
    @ApiOperation(value = "监测产品生成",notes = "产品")
    //生成气候监测产品
    public void creatProduct(@Apicp("productId,startDate,endDate") @RequestBody ProductParams params) {
        String productId = params.getProductId();
        if (StringUtils.equals(productId,"1")){//重要气候信息
//            params.setStartDate("20190101");
//            params.setEndDate("20190201");
            params.setST("0101");
            params.setET("0201");
            params.setRegions("1");
            params.setElement("t.TEM_Avg,t.PRE_Time_2020");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            method1.entrance(params);
        }else if(StringUtils.equals(productId,"2")){//干旱监测报告
//            params.setStartDate("20190101");
            method2.entrance(params);
        }else if (StringUtils.equals(productId,"3")){//极端天气气候事件报告
//            params.setStartDate("201901");
            params.setST(params.getStartDate().substring(4,6)+"01");
            params.setET(params.getStartDate().substring(4,6)+"31");
            params.setRegions("1");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            params.setMax(999d);
            params.setMin(-999d);
            method3.entrance(params);
        }else if(StringUtils.equals(productId,"4")){//决策服务
//            params.setStartDate("20190101");
//            params.setEndDate("20190110");
            params.setST("0101");
            params.setET("0110");
            params.setRegions("1");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            params.setMax(999d);
            params.setMin(-999d);
            params.setElement("PRE_Time_2020");
            method4.entrance(params);
        }else if (StringUtils.equals(productId,"5")){//专题报告
//            params.setStartDate("20190101");
//            params.setEndDate("20190201");
            params.setST("0101");
            params.setET("0201");
            params.setRegions("1");
            params.setElement("t.TEM_Avg,t.PRE_Time_2020");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            params.setMax(999d);
            params.setMin(-999d);
            method5.entrance(params);
        }else if(StringUtils.equals(productId,"6")){
//            params.setStartDate("20190101");
//            params.setEndDate("20190201");
            params.setST("0101");
            params.setET("0201");
            params.setRegions("1");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            params.setMax(999d);
            params.setMin(-999d);
            method6.entrance(params);
        }
    }

    @PostMapping(value = "createEvaProduct")
    @ApiOperation(value = "评价产品生成",notes = "产品")
    public void createEvaProduct(@RequestBody ProductParams params) {
        String productId = params.getProductId();
        if(StringUtils.equals(productId,"7")){//月评价
//            params.setStartDate("201901");
            params.setRegions("1");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            params.setMax(999d);
            params.setMin(-999d);
            evaMethod1.entrance(params);
        }else if(StringUtils.equals(productId,"8")){//季评价
//            params.setYear("2019");
//            params.setSeason("春季");
            params.setRegions("1");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            params.setMax(999d);
            params.setMin(-999d);
            evaMethod2.entrance(params);
        }else if (StringUtils.equals(productId,"9")){//年评价
//            params.setYear("2019");
            params.setRegions("1");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            params.setMax(999d);
            params.setMin(-999d);
            evaMethod3.entrance(params);
        }else if (StringUtils.equals(productId,"10")){//人体舒适度
//            params.setYear("2019");
            params.setRegions("1");
            params.setCondition(CommonUtils.getCondition(params.getRegions()));
            params.setMax(999d);
            params.setMin(-999d);
            evaMethod4.entrance(params);
        }else if (StringUtils.equals(productId,"11")){//水资源
//            params.setYear("2019");
            evaMethod5.entrance(params);
        }else if (StringUtils.equals(productId,"12")){//气候与农业
            evaMethod6.entrance(params);
        }else if (StringUtils.equals(productId,"13")){//气候与植被
            evaMethod7.entrance(params);
        }else if (StringUtils.equals(productId,"14")){//气候与电力
            evaMethod8.entrance(params);
        }else if (StringUtils.equals(productId,"15")){//气候与空气质量
            evaMethod9.entrance(params);
        }else if (StringUtils.equals(productId,"16")){//气候与交通
            evaMethod10.entrance(params);
        }else if (StringUtils.equals(productId,"17")){//气候与旅游
            evaMethod11.entrance(params);
        }else if (StringUtils.equals(productId,"18")){//宁夏变化评估报告
//          params.setYear("2019");
            evaMethod12.entrance(params);
        }else if (StringUtils.equals(productId,"19")){//气候对其他行业的影响

        }
    }

    @PostMapping(value = "DocumentDisplay")
    @ApiOperation(value = "文档展示",notes = "产品")
    //文档展示
    public List<Directory> documentDisplay(@ApiIgp("displayName,filePath,createTime") @RequestBody DirectoryParams params){
        params.setDirectoryId("1");
        params.setFileType("pdf");
        return directoryService.documentDisplay(params);
    }

    @PostMapping(value = "getFilePath")
    @ApiOperation(value = "文档路径",notes = "产品")
    //文档路径
    public String getFilePath(@ApiIgp("filePath,createTime,directoryId,fileType") @RequestBody DirectoryParams params){
        params.setDisplayName("2019年01月01日-2019年02月01日重要气候信息.pdf");
        String result = directoryService.getFilePath(params);
        return result;
    }

}
