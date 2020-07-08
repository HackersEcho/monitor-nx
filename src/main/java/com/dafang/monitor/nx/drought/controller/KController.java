package com.dafang.monitor.nx.drought.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.drought.entity.dto.DroughtDto;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.service.Impl.KServiceImpl;
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

@Api(value = "干旱",tags = {"干旱K指数"})
@RestController
@RequestMapping(value = "drought/K/")
public class KController {

    @Autowired
    KServiceImpl kService;

    @PostMapping(value = "getK")
    @ApiOperation(value = "干旱K指数",notes = "干旱K指数")
    public DroughtDto<List<Map<String,Object>>> getK(@Apicp("startDate,endDate,regions") @RequestBody DroughtParam params){
        DroughtDto<List<Map<String,Object>>> resuleDto = new DroughtDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setMax(999.0);
        params.setMin(-999.0);
//        params.setRegions("1");
//        params.setStartDate("");
//        params.setEndDate("");
        List<Map<String,Object>> dataList = kService.getK(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }

}
