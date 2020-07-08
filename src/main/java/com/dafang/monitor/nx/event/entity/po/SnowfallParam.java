package com.dafang.monitor.nx.event.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@ApiModel(value = "前端传入的降雪参数实体类")
public class SnowfallParam implements Serializable {
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

    @ApiModelProperty(value = "需要我们通过regions去得到要查询的的站点信息",hidden = true)
    @JsonIgnore
    private String condition;

    @ApiModelProperty(value = "类型",example = "1（初日）  or 2（终日）")
    private String type;

}
