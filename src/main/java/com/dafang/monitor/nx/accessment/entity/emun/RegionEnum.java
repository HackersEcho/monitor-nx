package com.dafang.monitor.nx.accessment.entity.emun;

import cn.hutool.core.convert.Convert;
import com.dafang.monitor.nx.entity.RegionStaEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public enum RegionEnum {

    QUANQU("1","兴庆区,西夏区,金凤区,永宁县,贺兰县,灵武市,大武口区,惠农区,平罗县,利通区,红寺堡区, 盐池县, 同心县,青铜峡市, 原州区,西吉县,隆德县,泾源县,彭阳县,沙坡头区,中宁县,海原县","全区"),
    YC("7","兴庆区,西夏区,金凤区,永宁县,贺兰县,灵武市","银川市"),
    SZS("8","大武口区,惠农区,平罗县","石嘴山市"),
    WZ("9","利通区,红寺堡区,盐池县,同心县,青铜峡市","吴忠市"),
    ZW("10","沙坡头区,中宁县,海原县","中卫市"),
    GY("11","原州区,西吉县,隆德县,泾源县,彭阳县","固原市");

    // 成员变量
    private String regionId;
    private String stas;
    private String desc;

    private RegionEnum(String regionId, String stas, String desc) {
        this.regionId = regionId;
        this.stas = stas;
        this.desc = desc;
    }
    // 通过regionId得到对应的站点
    public static List<String> getStas(String regionId){
        List<String> res = new ArrayList<>();
        for (RegionEnum r : RegionEnum.values()) {
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
