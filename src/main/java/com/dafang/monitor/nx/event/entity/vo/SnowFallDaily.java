package com.dafang.monitor.nx.event.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @description:降雪初终日返回字段
 * @author: renxin
 * @createDate: 2020/07/06
 * @version: 1.0
 */
@Entity
@Data
public class SnowFallDaily implements Serializable {
    private String stationNo;
    private String stationName;
    private Integer year;
    private String longitude;
    private String latitude;
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String type;//类型

}