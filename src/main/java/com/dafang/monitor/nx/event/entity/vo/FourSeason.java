package com.dafang.monitor.nx.event.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/4/23
 * @version: 1.0
 */
@Data
@Builder
@ApiModel(value = "四季入季或出季的数据")
public class FourSeason {
    private String stationNo;
    private String stationName;
    private String perenVal;
    private Integer minDays;
    private Integer maxDays;
    @ApiModelProperty("同期最早入季时间")
    private String minDaysTime;
    @ApiModelProperty("同期最晚入季时间")
    private String maxDaysTime;
    @ApiModelProperty(value = "查询时间四季数据")
    private List<Map<String, EventVal>> data;
}
