package com.dafang.monitor.nx.statistics.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @description:导出数据对应入参
 * @author: renxin
 * @createDate: 2020/06/16
 * @version: 1.0
 */
@Data
@Builder
@ApiModel(value = "前端传入的导出数据参数实体类")
public class ExportDataParam implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 6522094017645298281L;
    @ApiModelProperty(value = "表名",example = "如：base_day_data")
    private String table;
    @ApiModelProperty(value = "名称",example = "如：日数据")
    private String fileNames;
    @ApiModelProperty(value = "导出字段",example = "如：stationNo,station_name,observerTime,TEM_Avg")
    private String colStr;
    @ApiModelProperty(value = "要素",example = "如：TEM_Avg")
    private String col;
    @ApiModelProperty(value = "导出字段对应名称",example = "如：站点编号,站点名称,观测时间, 平均气温")
    private String colTextStr;
    @ApiModelProperty(value = "开始时间",example = "如：2020-06-01")
    private String startTime;
    @ApiModelProperty(value = "结束时间",example = "如：2020-06-30")
    private String endTime;
    @ApiModelProperty(value = "导出类型",example = "如：csv")
    private String fileType;
    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "1  or 63567,53765")
    private String regions;
    @ApiModelProperty(value = "站点类型",example = "如：1")
    private String stationType;
    @ApiModelProperty(value = "最小值",example = "如：-999")
    private String min;
    @ApiModelProperty(value = "最大值",example = "如：999")
    private String max;

    @ApiModelProperty(value = "需要我们通过regions去得到要查询的的站点信息",hidden = true)
    @JsonIgnore
    private String condition;

}