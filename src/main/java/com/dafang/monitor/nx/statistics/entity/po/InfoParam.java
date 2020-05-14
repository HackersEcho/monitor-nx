package com.dafang.monitor.nx.statistics.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:数据分页查询
 * @author: echo
 * @createDate: 2020/5/13
 * @version: 1.0
 */
@Data
@Builder
@ApiModel(value = "站点信息前段传入实体类")
public class InfoParam implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 6522094017645298281L;
    @ApiModelProperty(value = "表名")
    private String tableName;
    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "6  or 63567,53765")
    private String regions;
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private String startDate;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private String endDate;
    @ApiModelProperty(value = "查询的数据库字段",notes = "多个要素用,连接。如果是天气现象的查询，传进来的要素为天气现象码",example = "TEM_MAX")
    private String element;
    @ApiModelProperty(value = "最小值")
    @Builder.Default
    private Double min = -999d;
    @ApiModelProperty(value = "最大值")
    @Builder.Default
    private Double max=999d;
    @ApiModelProperty(value="页码",example = "1")
    private Integer pageNo;
    @ApiModelProperty(value="每页显示条数",example = "20")
    private Integer pageSize;
}
