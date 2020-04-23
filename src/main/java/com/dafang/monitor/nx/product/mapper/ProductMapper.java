package com.dafang.monitor.nx.product.mapper;

import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface ProductMapper extends MyMapper<Product> {

    /*
    查询基础数据
     */
    List<Product> periodList(ProductParams params);

}
