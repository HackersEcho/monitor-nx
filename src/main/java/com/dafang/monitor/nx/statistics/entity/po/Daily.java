package com.dafang.monitor.nx.statistics.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @description:日表和信息表中的字段
 * @author: echo
 * @createDate: 2020/3/11
 * @version: 1.0
 */
@Entity
@Data
public class Daily implements Serializable {
    private String stationNo;
    private String stationName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate observerTime;
    private Integer year;
    private Double PRE_Time_2008;
    private Double PRE_Time_0820;
    private Double PRE_Time_0808;
    private Double PRE_Time_2020;
    private Double TEM_Avg;
    private Double TEM_Max;
    private Double TEM_Min;
    private Double SSH;
    private Double FRS_Depth_AVG;//冻土
    private Double Snow_Depth;//积雪
    private String longitude;
    private String latitude;
    private String region_id;
    private String region_id_two;

    private Double val;// 对于要素的统计值,比如日数  平均值等等
    private Double element;// 前段传来的单要素

    private Object monthDay;//月日
    private String stationIdArray;//某个下区域站点

}