package com.dafang.monitor.nx.statistics.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/11
 * @version: 1.0
 */
@ApiModel("数据的基本信息情况")
@Data
@Builder
public class FirstDayCommonVal {
    @ApiModelProperty(value = "初日实况值")
    private String firstLiveVal;
    @ApiModelProperty(value = "初日常年值")
    private String firstPerenVal;
    @ApiModelProperty(value = "初日距平")
    private Integer firstAnomalyVal;
    @ApiModelProperty(value = "初日排位")
    private Integer firstRank;
    @ApiModelProperty(value = "终日实况值")
    private String endLiveVal;
    @ApiModelProperty(value = "终日常年值")
    private String endPerenVal;
    @ApiModelProperty(value = "终日距平")
    private Integer endAnomalyVal;
    @ApiModelProperty(value = "终日排位")
    private Integer endRank;
}
