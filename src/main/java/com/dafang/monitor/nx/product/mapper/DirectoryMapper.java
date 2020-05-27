package com.dafang.monitor.nx.product.mapper;

import com.dafang.monitor.nx.product.entity.po.Directory;
import com.dafang.monitor.nx.product.entity.po.DirectoryParams;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

@Mapper
@Repository
public interface DirectoryMapper extends MyMapper<Directory> {

    /*
    删除
     */
    void deleteDirectories(DirectoryParams params);

    /*
    插入
     */
    void insertDirectories(DirectoryParams params);
}
