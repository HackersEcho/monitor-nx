package com.dafang.monitor.nx.event.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/12
 * @version: 1.0
 */
@Builder
@Data
@ApiModel(value = "日期统计")
public class DateStatistic implements Serializable {
    private static final long serialVersionUID = -8752056118824574544L;
    private String stationNo;
    private String stationName;
    private String longitude;
    private String latitude;
    @ApiModelProperty(value = "历史最早")
    private String mostHistoryEarliest;
    @ApiModelProperty(value = "历史最晚")
    private String mostHistoryLaste;
    @ApiModelProperty(value = "常年值")
    private Object perenVal;
    @ApiModelProperty(value = "查询年份中的数据",notes = "key = 年份  val = 对应的数据")
    private List<Map<String,EventVal>> data;
}
