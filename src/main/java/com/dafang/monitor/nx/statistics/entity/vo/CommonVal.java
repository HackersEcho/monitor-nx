package com.dafang.monitor.nx.statistics.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/11
 * @version: 1.0
 */
@ApiModel("数据的基本信息情况")
@Entity
@Builder
public class CommonVal {
    @ApiModelProperty(name = "实况值")
    private Double liveVal;
    @ApiModelProperty(name = "常年值")
    private Double perenVal;
    @ApiModelProperty(name = "距平")
    private Double anomalyVal;
    @ApiModelProperty(name = "排位")
    private Integer rank;
}
