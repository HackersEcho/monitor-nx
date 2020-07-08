package com.dafang.monitor.nx.drought.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
@Data
@Builder
@Entity
public class DroughtParam implements Serializable {

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private String startDate;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private String endDate;
    @ApiModelProperty(value = "年份",example = "1981")
    @JsonFormat(pattern = "yyyy")
    private String year;
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
    @ApiModelProperty(value = "字段")
    @JsonIgnore
    private String cal;
    @ApiModelProperty(value = "区别表名")
    @JsonIgnore
    private String code;
}
