package com.dafang.monitor.nx.product.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class Product implements Serializable {

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
    private Double CI;
    private String longitude;
    private String latitude;
    //命名其他数据
    private Double val;

}
