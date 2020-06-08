package com.dafang.monitor.nx.doc.entity.emun;

public enum SlotEmun {

    Slot10("10日以来",10),
    Slot30("30日以来",30),
    Slot60("60日以来",60),
    Slot90("90日以来",90),
    Slot150("150日以来",150),
    Slot300("300日以来",300);

    private String name;
    private Integer days;

    SlotEmun(String name, Integer days) {
        this.name = name;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public Integer getDays() {
        return days;
    }
}
