package com.dafang.monitor.nx.statistics.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/4/9
 * @version: 1.0
 */
@ApiModel(value = "综合统计 统计分析极值统计")
@Data
@Builder
public class ComprehensiveExtrem {
    private String stationNo;
    private String stationName;
    @ApiModelProperty(value = "最小值")
    private Double min;
    private Double max;
    @ApiModelProperty(value = "极大值")
    private Double maxExtrem;
    @ApiModelProperty(value = "极大值出现时间")
    private String maxExtremTime;
    private Double minExtrem;
    private String minExtremTime;
    @ApiModelProperty(value = "查询年份中的数据",notes = "key = 年份  val = 对应的数据")
    private List<Map<String,CommonVal>> data;
}