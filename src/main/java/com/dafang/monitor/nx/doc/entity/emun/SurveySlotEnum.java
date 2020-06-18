package com.dafang.monitor.nx.doc.entity.emun;

public enum SurveySlotEnum {

    Slot1("5天以来",10,"DAY"),
    Slot2("7天以来",30,"DAY"),
    Slot3("10天以来",60,"DAY"),
    Slot4("当月以来",90,"DAY"),
    Slot5("当季以来",150,"DAY"),
    Slot6("今年以来",300,"DAY");

    private String name;
    private Integer days;
    private String timeType;

    SurveySlotEnum(String name, Integer days, String timeType) {
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
