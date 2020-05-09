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
public class SeasonParam implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 6522094017645298281L;
    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "6  or 53517,53518")
    private String regions;
    @ApiModelProperty(value = "开始年份")
    @JsonFormat(pattern = "yyyy")
    private String startYear;
    @ApiModelProperty(value = "结束时间年份")
    @JsonFormat(pattern = "yyyy")
    private String endYear;
    @ApiModelProperty(name = "climateScale",value = "常年值区间",example = "1991-2010")
    private String climateScale;
    @ApiModelProperty(value = "查询季节(1-春季 2-夏季 3-秋季 4-冬季)")
    private String seasonType;
    @ApiModelProperty(value = "季节类型(1-入季 2-结束)")
    private String timeType;
    @ApiModelProperty(value = "根据regions得到查询条件  默认不传")
    private String condition;
}
