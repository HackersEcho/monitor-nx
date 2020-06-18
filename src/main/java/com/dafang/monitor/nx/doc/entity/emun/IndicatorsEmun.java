package com.dafang.monitor.nx.doc.entity.emun;
/*
气候事件异常监测指标
 */
public enum IndicatorsEmun {

    Tem1("气温","平均气温","-999_999","TEM_Avg","AVG","TEM_AvgliveVal",1961,"站号,站名,监测指标,时间尺度,开始日期,结束日期,值,起始年限,历史排位,排名顺序"),
    Tem2("气温","高温日数","35_999","TEM_Max","","HighTemDays",1961,"站号,站名,监测指标,时间尺度,开始日期,结束日期,值,起始年限,历史排位,排名顺序"),
    Pre1("降水","累计降水","0.1_999","PRE_Time_2020","SUM","PRE_Time_2020liveVal",1961,"站号,站名,监测指标,时间尺度,开始日期,结束日期,值,起始年限,历史排位,排名顺序"),
    Pre2("降水","降水日数","0.1_999","PRE_Time_2020","","PreDays",1961,"站号,站名,监测指标,时间尺度,开始日期,结束日期,值,起始年限,历史排位,排名顺序"),
    FirstSoaker1("首场透雨","首场透雨时间","","event_time","","",1981,"站号,站名,监测指标,开始日期,值,起始年限,历史排位,排名顺序"),
    FirstSoaker2("首场透雨","首场透雨量","","pre","","",1981,"站号,站名,监测指标,日期,值,起始年限,历史排位,排名顺序"),
    season1("四季入季","春季入季","","","1","",1961,"站号,站名,监测指标,日期,值,起始年限,历史排位,排名顺序"),
    season2("四季入季","夏季入季","","","2","",1961,"站号,站名,监测指标,日期,值,起始年限,历史排位,排名顺序"),
    season3("四季入季","秋季入季","","","3","",1961,"站号,站名,监测指标,日期,值,起始年限,历史排位,排名顺序"),
    season4("四季入季","冬季入季","","","4","",1961,"站号,站名,监测指标,日期,值,起始年限,历史排位,排名顺序"),
    frost1("霜冻","轻霜日数","0_2","TEM_Min","","FrostDays",1961,"站号,站名,监测指标,时间尺度,开始日期,结束日期,值,起始年限,历史排位,排名顺序"),
    frost2("霜冻","重霜日数","-999_0","TEM_Min","","FrostDays",1961,"站号,站名,监测指标,时间尺度,开始日期,结束日期,值,起始年限,历史排位,排名顺序");

    private String indicators;
    private String name;
    private String slot;
    private String element;
    private String cal;
    private String drawValue;
    private Integer sortYear;
    private String sheetHead = "站号,站名,监测指标,时间尺度,开始日期,结束日期,值,起始年限,历史排位,排名顺序";

    IndicatorsEmun(String indicators, String name, String slot, String element, String cal, String drawValue, Integer sortYear, String sheetHead) {
        this.indicators = indicators;
        this.name = name;
        this.slot = slot;
        this.element = element;
        this.cal = cal;
        this.drawValue = drawValue;
        this.sortYear = sortYear;
        this.sheetHead = sheetHead;
    }

    public String getIndicators() {
        return indicators;
    }

    public String getName() {
        return name;
    }

    public String getSlot() {
        return slot;
    }

    public String getElement() {
        return element;
    }

    public String getCal() {
        return cal;
    }

    public String getDrawValue() {
        return drawValue;
    }

    public Integer getSortYear() {
        return sortYear;
    }

    public String getSheetHead() {
        return sheetHead;
    }
}
