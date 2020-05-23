package com.dafang.monitor.nx.product.impl.evaluate;

import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.po.ProductParams;

import java.util.Map;

public class ClimateAndVegetation extends TemplateAbstract {

    @Override
    protected void init(ProductParams params) {
        year = params.getYear();
    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        return null;
    }
}
