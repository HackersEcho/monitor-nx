package com.dafang.monitor.nx.doc.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
public class Doc implements Serializable {

    private String stationNo;
    private String stationName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate observerTime;
    private String year;
    private String longitude;
    private String latitude;
    private String val;

}
