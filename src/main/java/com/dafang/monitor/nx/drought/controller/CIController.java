package com.dafang.monitor.nx.drought.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.drought.entity.dto.DroughtDto;
import com.dafang.monitor.nx.drought.entity.po.DroughtParam;
import com.dafang.monitor.nx.drought.service.Impl.CIServiceImpl;
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

@Api(value = "干旱",tags = {"气候干旱指数"})
@RestController
@RequestMapping(value = "drought/CI/")
public class CIController {

    @Autowired
    CIServiceImpl CIService;

    @PostMapping(value = "getCI")
    @ApiOperation(value = "气候干旱指数(CI)",notes = "气候干旱指数(CI)")
    public DroughtDto<List<Map<String,Object>>> getCI(@Apicp("startDate,endDate,regions") @RequestBody DroughtParam params){
        DroughtDto<List<Map<String,Object>>> resuleDto = new DroughtDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        params.setElement("CI_VAL");
        params.setCode("2");
        params.setMax(999.0);
        params.setMin(-999.0);
        List<Map<String,Object>> dataList = CIService.getCI(params);
        resuleDto.setRespData(dataList);
        if (dataList.size() == 0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }
}
