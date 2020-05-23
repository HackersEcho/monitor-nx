package com.dafang.monitor.nx.event.entity.vo;

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
public class EventVal {
    @ApiModelProperty(value = "实况值")
    private Object liveVal;
    @ApiModelProperty(value = "常年值")
    private Object perenVal;
    @ApiModelProperty(value = "距平")
    private Double anomalyVal;
    @ApiModelProperty(value = "排位")
    private Integer rank;
}
