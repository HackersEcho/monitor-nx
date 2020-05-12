package com.dafang.monitor.nx.statistics.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/14
 * @version: 1.0
 */
@Data
@Builder
@ApiModel(value = "前端传入的参数实体类")
public class DailyParam implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 6522094017645298281L;
    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "6  or 63567,53765")
    private String regions;
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private String startDate;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private String endDate;
    @ApiModelProperty(name = "climateScale",value = "常年值区间",example = "1991-2010")
    private String climateScale;
    @ApiModelProperty(value = "查询的数据库字段",example = "TEM_MAX")
    private String element;
    @ApiModelProperty(value = "最小值")
    @Builder.Default
    private Double min = -999d;
    @ApiModelProperty(value = "最大值")
    @Builder.Default
    private Double max=999d;
    @ApiModelProperty(value = "天气现象码")
    private String code;
    @ApiModelProperty(value = "操作类型",example = "sum || avg || min || max")
    private String opType;
    @ApiModelProperty(value = "需要我们通过regions去得到要查询的的站点信息",hidden = true)
    @JsonIgnore
    private String condition;
    @ApiModelProperty(value = "通过startDate得到开始时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String sT;
    @ApiModelProperty(name = "得到结束时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String eT;
    @ApiModelProperty(value = "排位年限开始年份")
    @Builder.Default
    private Integer rankStartYear=1961;
    @ApiModelProperty(value = "排位年限结束年份")
    @Builder.Default
    private Integer rankEndYear=2020;
    @ApiModelProperty(name = "客户端拼接的条件  带and",hidden = true)
    @JsonIgnore
    private String clientCondition;
}
