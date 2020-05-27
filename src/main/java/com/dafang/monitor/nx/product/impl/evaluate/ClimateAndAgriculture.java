package com.dafang.monitor.nx.product.impl.evaluate;

import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.emun.ProductEmun;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class ClimateAndAgriculture extends TemplateAbstract {

    @Override
    protected void init(ProductParams params) {
        year = params.getYear();
        fileName = year + "年" + ProductEmun.getFileName(12);
        templateName = "气候与农业.ftl";
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        return null;
    }
}
