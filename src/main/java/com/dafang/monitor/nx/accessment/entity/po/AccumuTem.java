package com.dafang.monitor.nx.accessment.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

/*
积温
 */
@Data
@Entity
public class AccumuTem implements Serializable {
    //站号
    private String stationNo;
    //站名
    private String stationName;
    //时间
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate observerTime;
    //年份
    private Integer year;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    //平均气温
    private String temAvg;
}
