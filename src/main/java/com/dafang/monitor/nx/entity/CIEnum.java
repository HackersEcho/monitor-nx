package com.dafang.monitor.nx.entity;

public enum  CIEnum {

    LEVEL1("无旱","-0.6_999"),
    LEVEL2("轻旱","-1.2_-0.6"),
    LEVEL3("中旱","-1.8_-1.2"),
    LEVEL4("重旱","-2.4_-1.8"),
    LEVEL5("特旱","-999_-2.4");

    private String level;
    private String range;

    CIEnum(String level, String range) {
        this.level = level;
        this.range = range;
    }

    public String getLevel() {
        return level;
    }

    public String getRange() {
        return range;
    }

    @Override
    public String toString() {
        return "CIEnum{" +
                "level='" + level + '\'' +
                ", range='" + range + '\'' +
                '}';
    }
}
