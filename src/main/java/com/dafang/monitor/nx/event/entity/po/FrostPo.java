package com.dafang.monitor.nx.event.entity.po;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @description:日表和信息表中的字段
 * @author: echo
 * @createDate: 2020/3/11
 * @version: 1.0
 */
@Entity
@Data
public class FrostPo implements Serializable {
    private String stationNo;
    private String stationName;
    private Integer year;
    private String md;
    private String longitude;
    private String latitude;
    private Integer timeType;// 1表示结束 2表示开始
    private Double val;// 对于要素的统计值,比如日数  平均值等等
}