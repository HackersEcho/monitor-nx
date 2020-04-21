package com.dafang.monitor.nx.statistics.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import javax.persistence.Entity;
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
@Entity
@ApiModel(value = "同期日数")
public class PeriodStas implements Serializable {
    @ApiModelProperty(name = "区域名称")
    private String regionName;
    @ApiModelProperty(name = "历史最多站数")
    private Double mostHistoryStas;
    @ApiModelProperty(name = "历史最多站数出现时间")
    private String mostHistoryDate;
    @ApiModelProperty(name = "常年值")
    private Double perenVal;
    @ApiModelProperty(name = "查询年份中的数据",notes = "key = 年份  val = 对应的数据")
    private List<Map<String,CommonVal>> data;
}
