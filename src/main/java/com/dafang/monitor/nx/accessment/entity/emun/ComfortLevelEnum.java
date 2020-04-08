package com.dafang.monitor.nx.accessment.entity.emun;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum  ComfortLevelEnum {
    LEVEL_SHIWEN_9("1","4级","极热,极不舒适","80_999","湿温指数"),
    LEVEL_SHIWEN_8("1","3级","闷热,不舒适", "75_80","湿温指数"),
    LEVEL_SHIWEN_7("1","2级","偏热,较不舒适", "70_75","湿温指数"),
    LEVEL_SHIWEN_6("1","1级","凉爽,舒适", "65_70","湿温指数"),
    LEVEL_SHIWEN_5("1","0级","非常凉爽,非常舒适", "60_65","湿温指数"),
    LEVEL_SHIWEN_4("1","-1级","较凉爽,舒适", "55_60","湿温指数"),
    LEVEL_SHIWEN_3("1","-2级","较冷,较不舒适", "45_55","湿温指数"),
    LEVEL_SHIWEN_2("1","-3级","寒冷,不舒适", "40_45","湿温指数"),
    LEVEL_SHIWEN_1("1","-4级","极冷,极不舒适", "-999_40","湿温指数"),

    LEVEL_FENGHAN_9("2","4级","很冷风","80_999","湿温指数"),
    LEVEL_FENGHAN_8("2","3级","冷风", "75_80","湿温指数"),
    LEVEL_FENGHAN_7("2","2级","稍冷风", "70_75","湿温指数"),
    LEVEL_FENGHAN_6("2","1级","凉风", "65_70","湿温指数"),
    LEVEL_FENGHAN_5("2","0级","舒适风", "60_65","湿温指数"),
    LEVEL_FENGHAN_4("2","-1级","暖风", "55_60","湿温指数"),
    LEVEL_FENGHAN_3("2","-2级","皮感不明显风", "45_55","湿温指数"),
    LEVEL_FENGHAN_2("2","-3级","皮肤感热风", "40_45","湿温指数"),
    LEVEL_FENGHAN_1("2","-4级","皮肤不适风", "-999_40","湿温指数"),

    LEVEL_ZHUOYI_9("3","4级","羽绒或毛皮衣","80_999","湿温指数"),
    LEVEL_ZHUOYI_8("3","3级","便服加坚实外套", "75_80","湿温指数"),
    LEVEL_ZHUOYI_7("3","2级","冬季常用服装", "70_75","湿温指数"),
    LEVEL_ZHUOYI_6("3","1级","春秋常用便服", "65_70","湿温指数"),
    LEVEL_ZHUOYI_5("3","0级","衬衫和常用便服", "60_65","湿温指数"),
    LEVEL_ZHUOYI_4("3","-1级","轻便的夏装", "55_60","湿温指数"),
    LEVEL_ZHUOYI_3("3","-2级","短袖开领衫", "45_55","湿温指数"),
    LEVEL_ZHUOYI_2("3","-3级","热带单衣", "40_45","湿温指数"),
    LEVEL_ZHUOYI_1("3","-4级","超短裙", "-999_40","湿温指数"),

    LEVEL_ZONGHE_1("4","1级","", "7_9","综合指数"),
    LEVEL_ZONGHE_2("4","2级","", "5_7","综合指数"),
    LEVEL_ZONGHE_3("4","3级","", "3_5","综合指数"),
    LEVEL_ZONGHE_4("4","4级","", "1_3","综合指数"),

    LEVEL_RENTISHUSHIDU_9("5","4级","极热,极不舒适","86_999","人体舒适度指数"),
    LEVEL_RENTISHUSHIDU_8("5","3级","闷热,不舒适", "80_85","人体舒适度指数"),
    LEVEL_RENTISHUSHIDU_7("5","2级","偏热,较不舒适", "76_79","人体舒适度指数"),
    LEVEL_RENTISHUSHIDU_6("5","1级","凉爽,舒适", "71_75","人体舒适度指数"),
    LEVEL_RENTISHUSHIDU_5("5","0级","非常凉爽,非常舒适", "59_70","人体舒适度指数"),
    LEVEL_RENTISHUSHIDU_4("5","-1级","较凉爽,舒适", "51_58","人体舒适度指数"),
    LEVEL_RENTISHUSHIDU_3("5","-2级","较冷,较不舒适", "39_50","人体舒适度指数"),
    LEVEL_RENTISHUSHIDU_2("5","-3级","寒冷,不舒适", "26_38","人体舒适度指数"),
    LEVEL_RENTISHUSHIDU_1("5","-4级","极冷,极不舒适", "-999_25","人体舒适度指数");

    // 成员变量
    private String index;
    private String level;
    private String fell;
    private String desc;
    private String label;


    private ComfortLevelEnum(String index, String level, String fell,String desc,String label) {
        this.index = index;
        this.level = level;
        this.fell = fell;
        this.desc = desc;
        this.label = label;
    }

    public String getIndex() {
        return index;
    }

    public String getLevel() {
        return level;
    }

    public String getFell() {
        return fell;
    }

    public String getDesc() {
        return desc;
    }

    public String getLabel() {
        return label;
    }
    @Override
    public String toString() {
        return "ComfortLevelEnum{" +
                "index='" + index + '\'' +
                ", level='" + level + '\'' +
                ", fell='" + fell + '\'' +
                '}';
    }
}
