package com.dafang.monitor.nx.entity;

import cn.hutool.core.convert.Convert;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:区域站点
 * @author: echo
 * @createDate: 2020/4/1
 * @version: 1.0
 */
public enum  RegionStaEnum {
    QUANQU("1","53519,53610,53611,53612,53614,53615,53617,53618,53619,53704,53705,53707,53723,53727,53806,53810,53817,53903,53914,53916","全区"),
    ZBB("2","53517,53518,53519,53610,53611,53612,53614,53615,53617,53618,53619,53704,53705,53707,53723,53727,53806,53810,53881","中北部"),
    ZNB("3","53707,53723,53727,53806,53810,53881,53817,53903,53914,53916,53910","中南部"),
    YHGQ("4","53519,53610,53611,53612,53614,53615,53617,53618,53619,53704,53705","引黄灌区"),
    ZBGHD("5","53707,53723,53727,53806,53810","中部干旱带"),
    NBSQ("6","53817,53903,53914,53916","南部山区"),
    YC("7","53614,53618,53610,53619","银川市"),
    SZS("8","53517,53518,53519,53611,53615","石嘴山市"),
    WZ("9","53612,53617,53723,53810,53881,53727","吴忠市"),
    ZW("10","53704,53705,53806,53707","中卫市"),
    GY("11","53817,53903,53914,53916,53910","固原市");

    // 成员变量
    private String regionId;
    private String stas;
    private String desc;

    private RegionStaEnum(String regionId, String stas, String desc) {
        this.regionId = regionId;
        this.stas = stas;
        this.desc = desc;
    }
// 通过regionId得到对应的站点
    public static List<String> getStas(String regionId){
        List<String> res = new ArrayList<>();
        for (RegionStaEnum r : RegionStaEnum.values()) {
            if (StringUtils.equals(regionId,r.regionId)){
                res = Convert.toList(String.class,r.stas);
            }
        }
        return res;
    }
    @Override
    public String toString() {
        return "RegionStaEnum{" +
                "regionId='" + regionId + '\'' +
                ", stas='" + stas + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

}
