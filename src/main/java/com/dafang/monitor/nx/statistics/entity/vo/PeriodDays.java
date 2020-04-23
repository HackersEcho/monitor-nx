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
public class PeriodDays implements Serializable {
    private static final long serialVersionUID = -8752056118824574544L;
    private String stationNo;
    private String stationName;
    private String longitude;
    private String latitude;
    @ApiModelProperty(value = "历史最多日数")
    private Double mostHistoryDay;
    @ApiModelProperty(value = "历史最多日数出现时间")
    private String mostHistoryDate;
    @ApiModelProperty(value = "常年值")
    private Double perenVal;
    @ApiModelProperty(value = "查询年份中的数据",notes = "key = 年份  val = 对应的数据")
    private List<Map<String,CommonVal>> data;
}
