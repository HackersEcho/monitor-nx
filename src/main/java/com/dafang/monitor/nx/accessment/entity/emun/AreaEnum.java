package com.dafang.monitor.nx.accessment.entity.emun;

public enum AreaEnum {

    YCS("银川市", 8874.36,"7"),
    XQQ("兴庆区", 828.37,"53614"),
    XXQ("西夏区", 1129.72,"53614"),
    JFQ("金凤区", 345.46,"53614"),
    YNX("永宁县", 1193.91,"53618"),
    HLX("贺兰县", 1530.73,"53610"),
    LWS("灵武市", 3846.16,"53619"),

    SZSS("石嘴山市", 5207.98,"8"),
    DWKQ("大武口区", 1214.15,"53518"),
    HNQ("惠农区", 1361.01,"53519"),
    PLX("平罗县", 2632.82,"53611"),

    WZS("吴忠市", 21419.6,"9"),
    LTQ("利通区", 1414.54,"53612"),
    HSBQ("红寺堡区", 3522.99,"53810"),
    YCQ("盐池县", 8377.06,"53723"),
    TXX("同心县", 5666.7,"53810"),
    QTXS("青铜峡市", 2438.26,"53617"),

    GYS("固原市", 13450.2,"11"),
    YZQ("原州区", 3501.01,"53817"),
    XJX("西吉县", 4000.0,"53903"),
    LDX("隆德县", 1268.24,"53914"),
    JYX("泾源县", 1442.67,"53916"),
    PYX("彭阳县", 3238.31,"53817"),

    ZWX("中卫市", 17447.6,"10"),
    SPTQ("沙坡头区", 6877.25,"53704"),
    ZNX("中宁县", 4192.72,"53705"),
    HYX("海原县", 6377.64,"53806"),

    QX("全区", 66399.7,"1");


    private String RegionName;
    private String RegionId;
    private Double Area;

    AreaEnum(String regionName, Double area, String regionId) {
        RegionName = regionName;
        Area = area;
        RegionId = regionId;
    }

    @Override
    public String toString() {
        return "AreaEnum{" +
                "RegionName='" + RegionName + '\'' +
                ", Area=" + Area +
                ", RegionId='" + RegionId + '\'' +
                '}';
    }

    public String getRegionName() {
        return RegionName;
    }

    public String getRegionId() {
        return RegionId;
    }

    public Double getArea() {
        return Area;
    }
}
