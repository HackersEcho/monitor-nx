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

    /*
    查询ci指数
     */
    List<Product> ciList(ProductParams params);

    /*
    查询同期AVG/SUM数据数据
     */
    List<Product> periodsList(ProductParams params);

    /*
    同期日数统计
     */
    List<Product> periodDays(ProductParams params);
}
