package com.dafang.monitor.nx.statistics.controller;

import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class DaysControllerTest {
    @Autowired
    private DaysController controller;
//    @Test
//    void periodDays() {
//        DailyParam bean = DailyParam.builder().climateScale("1981-2010").startDate("20180801").endDate("20200831").regions("6")
//                .element("TEM_Max").min(35d).max(99d).build();
//        // 测试天气现象
////        DailyParam bean = DailyParam.builder().climateScale("1981-2010").startDate("20191231").endDate("20200101").regions("6")
////                .code("42").build();
//        controller.periodDays(bean);
//    }
    @Test
    void continuousDays() {
        DailyParam bean = DailyParam.builder().climateScale("1981-2010").startDate("20180801").endDate("20200831").regions("6")
                .element("TEM_Max").min(35d).max(99d).build();
        controller.continuousDays(bean);
    }
}