package com.dafang.monitor.nx.accessment.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Builder
@Entity
@ApiModel(value = "前端传入的环境参数实体类")
public class EnvironmentParam implements Serializable {

    @ApiModelProperty(value = "可以传入一个区域的名称 或者区域名称集合，用逗号分隔",example = "'银川','石嘴山','中卫','吴忠','固原'")
    private String area;
    @ApiModelProperty(value = "传入起始时间",example = "20190101")
    private String startDate;
    @ApiModelProperty(value = "传入结束时间",example = "20190201")
    private String endDate;
    @ApiModelProperty(value = "传入分页页数",example = "0")
    private String page;
    @ApiModelProperty(value = "传入分页每页行数",example = "30")
    private String rows;
    //分页索引开始行
    @JsonIgnore
    private String start;

    public EnvironmentParam(String area, String startDate, String endDate, String page, String rows, String start) {
        this.area = area;
        this.startDate = startDate;
        this.endDate = endDate;
        this.page = page;
        this.rows = rows;
        this.start = start;
    }

}
