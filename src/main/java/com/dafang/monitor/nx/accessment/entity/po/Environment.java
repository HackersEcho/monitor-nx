package com.dafang.monitor.nx.accessment.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
public class Environment implements Serializable {

    //地区
    private String area;
    //具体位置名字
    private String positionName;
    //时间
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate timePoint;
    //AQI
    private String aqi;
    //PM2.5细颗粒物
    private String pm25;
    //PM10可吸入颗粒物
    private String pm10;
    //一氧化碳
    private String co;
    //二氧化氮
    private String no2;
    //臭氧1小时平均
    private String o3;
    //臭氧8小时平均
    private String o38h;
    //二氧化硫
    private String so2;
    //空气质量指数类别
    private String quality;

}
