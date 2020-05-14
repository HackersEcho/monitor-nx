package com.dafang.monitor.nx.statistics.controller;

import com.dafang.monitor.nx.statistics.entity.po.InfoParam;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@SpringBootTest
@RunWith(SpringRunner.class)
class BaseControllerTest {
    @Autowired
    private BaseController controller;

    @Test
    void infoSelect() {
        InfoParam build = InfoParam.builder().regions("1").startDate("20180801").endDate("20180831")
                .tableName("base_day_data").element("TEM_Max").min(35d).max(99d).pageNo(1).pageSize(20).build();
        controller.infoSelect(build);
    }
}