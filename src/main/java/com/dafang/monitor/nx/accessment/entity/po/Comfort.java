package com.dafang.monitor.nx.accessment.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 舒适度指数实体类/积温
 */
@Entity
@Data
public class Comfort implements Serializable {
    //站号
    private String stationNo;
    //站名
    @Column
    private String stationName;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    //时间（JsonFormat保证前后端数据格式一致）
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate observerTime;
    //年份
    private Integer year;
    //指数值
    private Double indexValue;
    //指数类型（1：温湿指数  2：风寒指数 3：着衣指数 4：综合舒适指数）
    private Integer comfortType;
}
