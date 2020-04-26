package com.dafang.monitor.nx.product.entity.po;

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
@Entity
@ApiModel(value = "前端传入的参数实体类")
public class ProductParams implements Serializable {

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private String startDate;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private String endDate;
    @ApiModelProperty(value = "查询的数据库字段",example = "TEM_MAX")
    private String element;
    @ApiModelProperty(value = "最小值")
    @Builder.Default
    private Double min =-999d;
    @ApiModelProperty(value = "最大值")
    @Builder.Default
    private Double max=999d;
    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "6  or 63567,53765")
    private String regions;
    @ApiModelProperty(value = "通过startDate得到开始时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String sT;
    @ApiModelProperty(name = "得到结束时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String eT;
    @ApiModelProperty(value = "需要我们通过regions去得到要查询的的站点信息",hidden = true)
    @JsonIgnore
    private String condition;
    @ApiModelProperty(value = "天气现象码")
    private String code;
    @ApiModelProperty(value = "数据库查询是AVG或者SUM",hidden = true)
    @JsonIgnore
    private String cal;

    public ProductParams(String startDate, String endDate, String element, Double min, Double max, String regions, String sT, String eT, String condition, String code, String cal) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.element = element;
        this.min = min;
        this.max = max;
        this.regions = regions;
        this.sT = sT;
        this.eT = eT;
        this.condition = condition;
        this.code = code;
        this.cal = cal;
    }
}
