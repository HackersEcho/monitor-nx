package com.dafang.monitor.nx.doc.controller;

import com.dafang.monitor.nx.doc.entity.emun.MonitorEventsEnum;
import com.dafang.monitor.nx.doc.entity.po.DocParams;
import com.dafang.monitor.nx.doc.impl.DocEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "文档产品",tags = {"文案产品"})
@RestController
@RequestMapping(value = "doc/")
public class DocController {

    @Autowired
    DocEventService docService;

    @PostMapping(value = "EventsException")
    @ApiOperation(value = "监测评价气候事件异常",notes = "监测气候事件异常")
    public void EventsException(@RequestBody DocParams params){
//        params.setDirectoryId(1024);
        for (MonitorEventsEnum value : MonitorEventsEnum.values()) {
            int directoryId = value.getId();
            String directoryName = value.getName();
            String timeType = value.getTimeType();
            if (value.getId() == directoryId || directoryId == 0){
                docService.run(params,directoryName,timeType);
            }
        }
    }

    @PostMapping(value = "ClimateSurvey")
    @ApiOperation(value = "监测评价气候概况",notes = "监测气候事件异常")
    public void ClimateSurvey(@RequestBody DocParams params){
//        params.setDirectoryId(1024);
        for (MonitorEventsEnum value : MonitorEventsEnum.values()) {
            int directoryId = value.getId();
            String directoryName = value.getName();
            String timeType = value.getTimeType();
            if (value.getId() == directoryId || directoryId == 0){
                docService.run(params,directoryName,timeType);
            }
        }
    }



}
