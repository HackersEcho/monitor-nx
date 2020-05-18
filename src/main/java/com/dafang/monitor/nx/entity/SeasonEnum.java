package com.dafang.monitor.nx.entity;

import cn.hutool.core.convert.Convert;

import java.util.ArrayList;
import java.util.List;

public enum SeasonEnum {

    SPRING("春季","3月,4月,5月","0301_0531_春季,0301_0331_3月,0401_0431_4月,0501_0531_5月"),
    SUMMER("夏季","6月,7月,8月","0601_0831_夏季,0601_0631_6月,0701_0731_7月,0801_0831_8月"),
    AUTUMN("秋季","9月,10月,11月","0901_1131_秋季,0901_0931_9月,1001_1031_10月,1101_1131_11月"),
    WINTER("冬季","12月,01月,02月","1201_0231_冬季,1201_1231_12月,0101_0131_1月,0201_0231_2月");

    private String season;
    private String month;
    private String slot;

    /*
    根据季节得到该季节内月份
     */
    public static List<String> getMonthBySeason(String season){
        List<String> list = new ArrayList<>();
        for (SeasonEnum value : SeasonEnum.values()) {
            if (season.equals(value.season)){
                list = Convert.toList(String.class,value.month);
            }
        }
        return list;
    }
    /*
    根据季节得到该季节内每月的时间段
     */
    public static List<String> getSlotBySeason(String season){
        List<String> list = new ArrayList<>();
        for (SeasonEnum value : SeasonEnum.values()) {
            if (season.equals(value.season)){
                list = Convert.toList(String.class,value.slot);
            }
        }
        return list;
    }

    SeasonEnum(String season, String month, String slot) {
        this.season = season;
        this.month = month;
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "SeasonEnum{" +
                "season='" + season + '\'' +
                ", month='" + month + '\'' +
                ", slot='" + slot + '\'' +
                '}';
    }
}
