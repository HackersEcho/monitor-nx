package com.dafang.monitor.nx.accessment.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
@Data
@Builder
@Entity
@ApiModel(value = "前端传入的水资源参数实体类")
public class WaterResourceparam implements Serializable {

    @ApiModelProperty(value = "可以传入一个区域的名称 或者区域名称集合，用逗号分隔",example = "全区,银川市,石嘴山市,吴忠市,中卫市,固原市,兴庆区,西夏区,金凤区,永宁县,贺兰县,灵武市,大武口区,惠农区,平罗县,利通区,红寺堡区,盐池县,同心县,青铜峡市,原州区,西吉县,隆德县,泾源县,彭阳县,沙坡头区,中宁县,海原县")
    private String regionName;
    @ApiModelProperty(value = "传入起始年份",example = "2019")
    private String startYear;
    @ApiModelProperty(value = "传入结束年份",example = "2019")
    private String endYear;
    @ApiModelProperty(name = "climateScale",value = "常年值区间",example = "1981-2010")
    private String climateScale;

    public WaterResourceparam(String regionName, String startYear, String endYear, String climateScale) {
        this.regionName = regionName;
        this.startYear = startYear;
        this.endYear = endYear;
        this.climateScale = climateScale;
    }
}
