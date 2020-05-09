package com.dafang.monitor.nx.event.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:事件中数据库对应的字段
 * @author: echo
 * @createDate: 2020/4/23
 * @version: 1.0
 */
@Data
public class EventDaily {
    private String stationNo;
    private String stationName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "四季入季表 我会将年份和月日拼接。得到发生入季或者出季的时间")
    private String observerTime;
    @ApiModelProperty(value = "月日")
    private String md;
    @ApiModelProperty(value = "1-入季开始  2-入季结束")
    private Integer timeType;
    @ApiModelProperty(value = "入季时间对应的天数")
    private Integer days;
    private Integer year;
}
