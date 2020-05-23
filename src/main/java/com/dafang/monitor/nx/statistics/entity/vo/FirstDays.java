package com.dafang.monitor.nx.statistics.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:初终日
 * @author: echo
 * @createDate: 2020/4/20
 * @version: 1.0
 */
@ApiModel(value = "初终日数据")
@Data
@Builder
public class FirstDays {
    private String stationNo;
    private String stationName;
    @ApiModelProperty(value = "初日时间")
    private String firstPerenDay;
    @ApiModelProperty(value = "终日时间")
    private String endPerenDay;
    @ApiModelProperty(value = "查询时间段内初终日信息")
    private List<Map<String,FirstDayCommonVal>> data;
}
