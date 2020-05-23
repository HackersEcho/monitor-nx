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
@Entity
@ApiModel(value = "前端传入的参数实体类")
public class ComfortParam implements Serializable {

    @ApiModelProperty(value = "可以传入一个区域的编号 或者站点集合，用逗号分隔",example = "53517")
    private String regions;
    @ApiModelProperty(value = "开始时间",example = "20190501")
    @JsonFormat(pattern = "yyyyMMdd")
    private String startDate;
    @ApiModelProperty(value = "结束时间",example = "20190601")
    @JsonFormat(pattern = "yyyyMMdd")
    private String endDate;
    @ApiModelProperty(name = "climateScale",value = "常年值区间",example = "1981-2010")
    private String climateScale;
    @ApiModelProperty(value = "指数类型（1：温湿指数  2：风寒指数 3：着衣指数 4：综合舒适指数 5：人体舒适度指数）",example = "1")
    private String comfortType;
    @ApiModelProperty(value = "通过startDate得到开始时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String sT;
    @ApiModelProperty(value = "通过endDate得到开始时间的月日  用于查询同期时需要用到",hidden = true)
    @JsonIgnore
    private String eT;
    @JsonIgnore
    private String condition;
    @JsonIgnore
    private String code;//判断表名的依据
}
