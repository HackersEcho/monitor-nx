package com.dafang.monitor.nx.product.controller;

import cn.hutool.core.util.ReflectUtil;
import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.impl.ClimateInfo;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Api(value = "产品",tags = {"产品"})
@RestController
@RequestMapping(value = "product/")
public class ProductController {

    @Autowired
    ClimateInfo method;

    @PostMapping(value = "creatProduct")
    @ApiOperation(value = "产品生成",notes = "产品")
    public void creatProduct(ProductParams params) throws Exception {
        params.setStartDate("20190101");
        params.setEndDate("20190201");
        params.setST("0101");
        params.setET("0201");
        params.setRegions("1");
        params.setElement("t.TEM_Avg,t.PRE_Time_2020");
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        method.entrance(params);
    }

}
