package com.dafang.monitor.nx.statistics.controller;

import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class StasControllerTest {
    @Autowired
    private StasController controller;
    @Test

    void periodDays() {
        DailyParam bean = DailyParam.builder().climateScale("1981-2010").startDate("20180801").endDate("20200831").regions("5,6")
                .element("TEM_Max").min(30d).max(99d).build();
        controller.periodStas(bean);
    }
}