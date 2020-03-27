package com.dafang.monitor.nx.statistics.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: echo
 * @createDate: 2020/3/23
 * @version: 1.0
 */
@Data
@Builder
@ApiModel(value = "连续日数")
public class ContinuousDays {
    private String stationNo;
    private String stationName;
    private String longitude;
    private String latitude;
    private Double val;
}
