package com.dafang.monitor.nx.statistics.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import com.dafang.monitor.nx.statistics.service.BaseService;
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
import java.util.Map;

@RestController
@Api(value = "",tags = {"站点查询"})
@RequestMapping(value = "base/")
@Slf4j
public class BaseController {
    @Autowired
    private BaseService service;
    @PostMapping(value = "sta")
    @ApiOperation(value = "站点信息查询",notes = "")

    public ResuleDto<Object> staInfoSelect(@Apicp("regions") @RequestBody DailyParam params){
        log.info("站点信息数据查询");
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        ResuleDto<Object> resuleDto = new ResuleDto<>();
        List<Map<String, Object>> mapList = service.staInfoList(params);
        resuleDto.setRespData(mapList);
        if (mapList.size()==0){
            resuleDto.setRespCode(0);
            resuleDto.setMessage("该条件下无数据");
        }
        return resuleDto;
    }


}
