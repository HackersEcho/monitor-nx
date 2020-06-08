package com.dafang.monitor.nx.doc.impl;

import com.dafang.monitor.nx.doc.entity.po.DocParams;
import com.dafang.monitor.nx.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@SpringBootTest
@RunWith(SpringRunner.class)
class DocServiceTest {
    @Autowired
    private DocService service;
    @Test
    void run() {
        DocParams params = new DocParams("20190101");
        params.setRegions("1");
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        service.init(params);
    }
}