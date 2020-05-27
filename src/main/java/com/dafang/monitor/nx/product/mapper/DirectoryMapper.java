package com.dafang.monitor.nx.product.mapper;

import com.dafang.monitor.nx.product.entity.po.Directory;
import com.dafang.monitor.nx.product.entity.po.DirectoryParams;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

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

    /*
    根据id查询产品
     */
    List<Directory> selectDirectoryById(DirectoryParams params);

    /*
    根据文件名查询产品路径
     */
    String selectFilePathByFileName(DirectoryParams params);

}
