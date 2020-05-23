package com.dafang.monitor.nx.statistics.entity.vo;

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
@ApiModel(value = "同期日数")
public class PeriodStas implements Serializable {
    private static final long serialVersionUID = -8247214502050038730L;
    @ApiModelProperty(value = "区域名称")
    private String regionName;
    @ApiModelProperty(value = "历史最多站数")
    private Double mostHistoryStas;
    @ApiModelProperty(value = "历史最多站数出现时间")
    private String mostHistoryDate;
    @ApiModelProperty(value = "常年值")
    private Double perenVal;
    @ApiModelProperty(value = "查询年份中的数据",notes = "key = 年份  val = 对应的数据")
    private List<Map<String,CommonVal>> data;
}
