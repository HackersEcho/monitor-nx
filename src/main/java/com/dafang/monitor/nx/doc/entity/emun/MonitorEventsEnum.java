package com.dafang.monitor.nx.doc.entity.emun;
/*
气候事件异常
 */
public enum MonitorEventsEnum {

    MONEVENT1(1024,"气温","DAY"),
//    MONEVENT2(1025,"气温分布"),
    MONEVENT3(1026,"降水","DAY"),
//    MONEVENT4(1027,"降水分布"),
    MONEVENT5(1028,"首场透雨","DAY"),
    MONEVENT6(1029,"四季入季","DAY"),
    MONEVENT7(1030,"霜冻","DAY"),
    MONEVENT8(1031,"天气现象","DAY"),
    MONEVENT9(1032,"其他","DAY"),

    EVAEVENT1(1051,"气温","MONTH"),
    //    EVAEVENT2(1052,"气温分布"),
    EVAEVENT3(1053,"降水","MONTH"),
    //    EVAEVENT4(1054,"降水分布"),
    EVAEVENT5(1055,"首场透雨","MONTH"),
    EVAEVENT6(1056,"四季入季","MONTH"),
    EVAEVENT7(1057,"霜冻","MONTH"),
    EVAEVENT8(1058,"天气现象","MONTH"),
    EVAEVENT9(1060,"其他","MONTH");

    private Integer id;
    private String name;
    private String timeType;

    MonitorEventsEnum(Integer id, String name, String timeType) {
        this.id = id;
        this.name = name;
        this.timeType = timeType;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTimeType() {
        return timeType;
    }
}
