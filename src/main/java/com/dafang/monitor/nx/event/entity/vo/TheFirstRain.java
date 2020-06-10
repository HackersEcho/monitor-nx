package com.dafang.monitor.nx.event.entity.vo;

import com.dafang.monitor.nx.statistics.entity.vo.CommonVal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Renxin
 * @createDate: 2020/06/08
 * @version: 1.0
 */
@Data
@Builder
public class TheFirstRain implements Serializable{
    private static final long serialVersionUID = -8752056118824574544L;
    @ApiModelProperty(value = "站点编号")
    private String stationNo;
    @ApiModelProperty(value = "站点名称")
    private String stationName;
    @ApiModelProperty(value = "纬度")
    private Object latitude;
    @ApiModelProperty(value = "经度")
    private Object longitude;
    @ApiModelProperty(value = "观测年份")
    private Integer statisticsYear;
    @ApiModelProperty(value = "观测月日")
    private Integer statisticsMonthDay;
    @ApiModelProperty(value = "历史最大值")
    private Double earlierValue;
    @ApiModelProperty(value = "历史最大值出现时间")
    private String earlierValueTime;
    @ApiModelProperty(value = "查询年份中的数据",notes = "key = 年份  val = 对应的数据")
    private List<Map<String, EventVal>> data;


}
