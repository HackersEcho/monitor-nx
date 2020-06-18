package com.dafang.monitor.nx.doc.entity.emun;

/*
气候概况
 */
public enum ClimateSurveyEnum {
    ;


    private String id;
    private String name;
    private String timeTYpe;

    ClimateSurveyEnum(String id, String name, String timeTYpe) {
        this.id = id;
        this.name = name;
        this.timeTYpe = timeTYpe;
    }
}
