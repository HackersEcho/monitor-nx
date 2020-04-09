package com.dafang.monitor.nx.accessment.controller;

import com.dafang.monitor.nx.accessment.entity.po.ComfortParam;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
class HumanComfortControllerTest {
    @Autowired
    private HumanComfortController controller;
    //同期舒适度指数
//    @Test
//    void periodList(){
//        ComfortParam build = ComfortParam.builder().startDate("20190501").regions("53517").endDate("20190601").climateScale("1981-2010").comfortType("1").build();
//        controller.periodList(build);
//    }
    //连续舒适度指数
    @Test
    void continueList(){
        ComfortParam build = ComfortParam.builder().startDate("20200301").regions("53517").endDate("20200401").climateScale("1981-2010").comfortType("1").build();
        controller.continueList(build);
    }

}