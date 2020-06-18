package com.dafang.monitor.nx.doc.impl;

import com.dafang.monitor.nx.doc.DocAbstrator;
import com.dafang.monitor.nx.doc.entity.emun.SurveySlotEnum;
import com.dafang.monitor.nx.doc.entity.po.DocParams;
import com.dafang.monitor.nx.doc.mapper.DocMapper;
import com.dafang.monitor.nx.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class DocSurveyService extends DocAbstrator {

    @Autowired
    DocMapper mapper;

    @Override
    public void init(DocParams params, String timeType) {
        endDate = params.getEndDate();

    }

    @Override
    public void process(DocParams params, String directoryName, String timeType) {
        //平均气温/距平
        for (SurveySlotEnum slot : SurveySlotEnum.values()) {
            Date ed = DateUtil.strToDate(endDate, "yyyyMMdd");
            Calendar c = Calendar.getInstance();
            c.setTime(ed);
            c.add(Calendar.DAY_OF_MONTH,-slot.getDays());
            startDate = DateUtil.dateToStr(c.getTime(),"yyyyMMdd");
            params.setST(startDate.substring(0,4));
            params.setET(endDate.substring(0,4));

        }
    }
}
