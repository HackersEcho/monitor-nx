package com.dafang.monitor.nx.event.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: my
 * @createDate: 2020/5/20
 * @version: 1.0
 */
@Data
@Builder
@ApiModel(value = "变温事件")
public class VariableTempVO {
    private String stationNo;//站号
    private double value;//值
    private String stationName;//站名
    private String ObserverTime;//观测时间(year)
    private String beforeTime;//变温前时间
    private double beforeValue;//变温前气温
    private String afterTime;//变温后时间
    private double afterValue;//变温后气温
    private double rangeValue;//变温幅度
    private String anomalyValue;//距平
    private int counts;//变温次数
    private double historyMax;//历史最大值
    private int historyMaxCount;//历史最大次数
    private String historyMaxTime;//历史最大值出现时间
    private String historicalRanking;//历史排位
    private String perennialValue;//常年值


}
