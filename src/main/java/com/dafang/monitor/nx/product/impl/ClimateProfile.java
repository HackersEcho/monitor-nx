package com.dafang.monitor.nx.product.impl;

import com.dafang.monitor.nx.product.TemplateAbstract;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 气候概况
 */
public class ClimateProfile extends TemplateAbstract {

    @Autowired
    private ProductMapper mapper;
    private String startData;
    private String endData;
    private List<Product> baseData;
    private List<Product> perenList;
    private List<Product> currentList;

    @Override
    protected void init(ProductParams params) {

    }

    @Override
    protected Map<String, Object> getDatas(ProductParams params) {
        return null;
    }
}
