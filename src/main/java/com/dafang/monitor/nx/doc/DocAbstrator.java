package com.dafang.monitor.nx.doc;

import com.dafang.monitor.nx.doc.entity.po.DocParams;

public abstract class DocAbstrator {

    protected String date;//日期
    protected String startDate;//开始时间
    protected String endDate;//结束时间
    protected String year;//年份
    protected String excelName;//列表名称
    protected String drawName;//分布图名称
    protected String filePath;//文件地址
    protected String slotName;//时间尺度
    protected String indicatorName;//监测指标
    protected String indicator;//监测指标
    protected String drawValue;//绘图值
    protected Integer sortYear;//排位年限
    protected String sheetHead;//列表名称

    public abstract void init(DocParams params,String timeType);

    public abstract void process(DocParams params,String directoryName,String timeType);

    public void run(DocParams params,String directoryName,String timeType){
        init(params,timeType);
        process(params,directoryName,timeType);
    }

}
