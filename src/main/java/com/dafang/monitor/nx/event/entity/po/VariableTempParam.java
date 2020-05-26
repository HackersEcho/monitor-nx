package com.dafang.monitor.nx.event.entity.po;

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
public class VariableTempParam implements Serializable {
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
    @ApiModelProperty(value = "变温时间", example = "24h or 48h or 72h")
    private String variableTime;
    @ApiModelProperty(value = "统计类型",example = "rang/count")
    private String statisticType;
    @ApiModelProperty(value = "要素选择",example = "TEM_Max")
    private String tempType;
    @ApiModelProperty(value = "升温或降温",example = "up/down")
    private String upOrDown;
    @ApiModelProperty(value = "阈值",example = "6")
    private Double threshold;
    @ApiModelProperty(value = "需要我们通过regions去得到要查询的的站点信息",hidden = true)
    @JsonIgnore
    private String condition;
    @ApiModelProperty(value = "通过startDate得到开始时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String sT;
    @ApiModelProperty(name = "得到结束时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String eT;
    @ApiModelProperty(value = "通过startDate得到新的开始时间",hidden = true)
    @JsonIgnore
    private String newStartDate;
    @JsonIgnore
    private String remark = "VariableTemp";

}
