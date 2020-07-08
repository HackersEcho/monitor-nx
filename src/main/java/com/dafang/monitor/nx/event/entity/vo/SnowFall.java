package com.dafang.monitor.nx.event.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Renxin
 * @createDate: 2020/06/23
 * @version: 1.0
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnowFall implements Serializable{
    private static final long serialVersionUID = -8752056118824574544L;
    @ApiModelProperty(value = "站点编号")
    private String stationNo;
    @ApiModelProperty(value = "站点名称")
    private String stationName;
    @ApiModelProperty(value = "纬度")
    private Object latitude;
    @ApiModelProperty(value = "经度")
    private Object longitude;
    @ApiModelProperty(value = "历史最早出现时间")
    private Object maxDate;
    @ApiModelProperty(value = "历史最晚出现时间")
    private Object minDate;
    @ApiModelProperty(value = "查询年份中的数据",notes = "key = 年份  val = 对应的数据")
    private List<Map<String, EventVal>> data;


}
