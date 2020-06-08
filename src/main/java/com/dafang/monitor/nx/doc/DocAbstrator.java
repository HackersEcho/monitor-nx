package com.dafang.monitor.nx.doc;

import com.dafang.monitor.nx.doc.entity.po.DocParams;

public abstract class DocAbstrator {

    protected String startDate;
    protected String endDate;
    protected String year;

    public abstract void init(DocParams params);

    public void run(DocParams params){
        init(params);
    }

}
