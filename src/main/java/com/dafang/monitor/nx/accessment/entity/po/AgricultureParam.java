package com.dafang.monitor.nx.accessment.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Builder
@Entity/*实体类注解，不写可能读取不到*/
@ApiModel(value = "前端传入的参数")
public class AgricultureParam implements Serializable {
    //气候与农业
    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "53517")
    private String regions;
    @ApiModelProperty(value = "传入起始年份",example = "2019")
    private String startYear;
    @ApiModelProperty(value = "传入结束年份",example = "2019")
    private String endYear;
    @ApiModelProperty(value = "传入植物生长期对应的开始时间",example = "0301")
    private String startMonthDay;
    @ApiModelProperty(value = "传入植物生长期对应的结束时间",example = "0401")
    private String endMonthDay;
    @ApiModelProperty(name = "climateScale",value = "常年值区间",example = "1981-2010")
    private String climateScale;
    @JsonIgnore
    private String condition;

    public AgricultureParam() {
    }

    public AgricultureParam(String regions, String startYear, String endYear, String startMonthDay, String endMonthDay, String climateScale, String condition) {
        this.regions = regions;
        this.startYear = startYear;
        this.endYear = endYear;
        this.startMonthDay = startMonthDay;
        this.endMonthDay = endMonthDay;
        this.climateScale = climateScale;
        this.condition = condition;
    }



}
