package com.dafang.monitor.nx.doc.entity.emun;
/*
气候事件异常时间尺度
 */
public enum SlotEnum {

    Slot10("10日以来",10,"DAY"),
    Slot30("30日以来",30,"DAY"),
    Slot60("60日以来",60,"DAY"),
    Slot90("90日以来",90,"DAY"),
    Slot150("150日以来",150,"DAY"),
    Slot300("300日以来",300,"DAY"),
    Slot("月",0,"MONTH");

    private String name;
    private Integer days;
    private String timeType;

    SlotEnum(String name, Integer days, String timeType) {
        this.name = name;
        this.days = days;
        this.timeType = timeType;
    }

    public String getName() {
        return name;
    }

    public Integer getDays() {
        return days;
    }

    public String getTimeType() {
        return timeType;
    }
}
