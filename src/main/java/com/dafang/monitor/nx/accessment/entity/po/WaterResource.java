package com.dafang.monitor.nx.accessment.entity.po;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * 气候与水资源实体类
 */
@Data
@Entity
public class WaterResource implements Serializable {

    private String stationNo;
    private String stationName;
    private Integer year;
    private String pre;//年降水量

}
