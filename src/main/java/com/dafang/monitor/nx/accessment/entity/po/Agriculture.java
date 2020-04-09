package com.dafang.monitor.nx.accessment.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

/*
 * 农业实体类
 */
@Data
@Entity
public class Agriculture implements Serializable {
    //站号
    private String stationNo;
    //站名
    private String stationName;
    //时间
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate observerTime;
    //年份
    private Integer year;
    //平均气温
    private String temAvg;
    //最高气温
    private String temMax;
    //最低气温
    private String temMin;
    //降水
    private String pre2020;
    //日照
    private String ssh;
}
