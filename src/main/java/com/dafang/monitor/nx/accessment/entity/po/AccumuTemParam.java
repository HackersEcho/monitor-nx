package com.dafang.monitor.nx.accessment.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

/*
积温
 */
@Data
@Builder
@Entity/*实体类注解，不写可能读取不到*/
@ApiModel(value = "前端传入的参数")
public class AccumuTemParam implements Serializable {
    //积温
    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "53517")
    private String regions;
    @ApiModelProperty(value = "开始时间",example = "20190501")
    @JsonFormat(pattern = "yyyyMMdd")
    private String startDate;
    @ApiModelProperty(value = "结束时间",example = "20190601")
    @JsonFormat(pattern = "yyyyMMdd")
    private String endDate;
    @ApiModelProperty(value = "通过startDate得到开始时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String sT;
    @ApiModelProperty(value = "通过endDate得到开始时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String eT;
    @ApiModelProperty(name = "climateScale",value = "常年值区间",example = "1981-2010")
    private String climateScale;
    @ApiModelProperty(value = "积温类型（1：有效积温  2：活动积温 3：负积温 ）",example = "1")
    private Integer type;
    @JsonIgnore
    private String condition;
    @JsonIgnore
    private String accCondition;//积温条件
    @JsonIgnore
    private String element;//积温要素（temAvg）
    @JsonIgnore
    private String code;//判断表名的依据

    public AccumuTemParam(String regions, String startDate, String endDate, String sT, String eT, String climateScale, Integer type, String condition, String accCondition, String element, String code) {
        this.regions = regions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sT = sT;
        this.eT = eT;
        this.climateScale = climateScale;
        this.type = type;
        this.condition = condition;
        this.accCondition = accCondition;
        this.element = element;
        this.code = code;
    }
}
