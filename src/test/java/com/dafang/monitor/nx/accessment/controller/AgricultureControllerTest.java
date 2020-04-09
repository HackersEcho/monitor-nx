package com.dafang.monitor.nx.accessment.controller;


import com.dafang.monitor.nx.accessment.entity.po.AccumuTemParam;
import com.dafang.monitor.nx.accessment.entity.po.AgricultureParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AgricultureControllerTest {

    @Autowired
    private AgricultureController controller;

    @Test
    public void dataList(){
        AgricultureParam build = AgricultureParam.builder().regions("53517").startYear("2019").endYear("2019").startMonthDay("0301").endMonthDay("0401").climateScale("1981-2010").build();
        controller.dataList(build);
    }
    @Test
    public void periodList(){
        AccumuTemParam build = AccumuTemParam.builder().regions("53517").startDate("20190301").endDate("20190401").climateScale("1981-2010").build();
        controller.periodList(build);
    }

}
