package com.dafang.monitor.nx.statistics.controller;

import com.dafang.monitor.nx.statistics.entity.po.DailyParam;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@SpringBootTest
@RunWith(SpringRunner.class)
class NumericalControllerTest {
    @Autowired
    private NumericalController controller;
    @Test
    void comprehensive() {
        DailyParam bean = DailyParam.builder().startDate("20180801").endDate("20180831").regions("6").opType("max")
                .element("TEM_Max,TEM_Min").min(35d).max(99d).build();
        controller.comprehensive(bean);
    }
    @Test
    void extrem(){
        DailyParam bean = DailyParam.builder().startDate("20120801").endDate("20120831").regions("53517").climateScale("1981-2010")
                .element("TEM_Max").min(35d).max(99d).build();
//        DailyParam bean = DailyParam.builder().startDate("20110801").endDate("20110831").regions("53517").climateScale("1981-2010")
//                .element("TEM_Max").build();
        controller.extrem(bean);
    }
    @Test
    void first(){
        DailyParam bean = DailyParam.builder().startDate("20120801").endDate("20120831").regions("53517").climateScale("1981-2010")
                .element("TEM_Max").min(30d).max(99d).rankStartYear(1961).rankEndYear(2020).build();
        controller.firstDays(bean);
    }

}