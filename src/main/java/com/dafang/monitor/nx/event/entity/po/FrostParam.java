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
@ApiModel(value = "前端传入的霜冻参数实体类")
public class FrostParam implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 6522094017645298281L;
    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "1  or 63567,53765")
    private String regions;
    @ApiModelProperty(value = "开始年份")
    @JsonFormat(pattern = "yyyy")
    private String startYear;
    @ApiModelProperty(value = "结束年份")
    @JsonFormat(pattern = "yyyy")
    private String endYear;
    @ApiModelProperty(name = "climateScale",value = "常年值区间",example = "1981-2010")
    private String climateScale;
    @ApiModelProperty(value = "霜冻等级  1(轻霜) 2(重霜)",example = "1")
    private String frostType;
    @ApiModelProperty(value = "1表示结束 2表示开始",example = "1 天数的时候可以不填")
    private Integer timeType;
    @ApiModelProperty(value = "需要我们通过regions去得到要查询的的站点信息",hidden = true)
    @JsonIgnore
    private String condition;
    @ApiModelProperty(value = "排位年限开始年份",example = "1961")
    @Builder.Default
    private Integer rankStartYear=1961;
    @ApiModelProperty(value = "排位年限结束年份",example = "2020")
    @Builder.Default
    private Integer rankEndYear=2020;

}
