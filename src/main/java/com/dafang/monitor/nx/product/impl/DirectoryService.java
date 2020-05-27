package com.dafang.monitor.nx.product.impl;

import com.dafang.monitor.nx.product.entity.po.Directory;
import com.dafang.monitor.nx.product.entity.po.DirectoryParams;
import com.dafang.monitor.nx.product.mapper.DirectoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryService {

    @Autowired
    DirectoryMapper mapper;

    //文档展示
    public List<Directory> documentDisplay(DirectoryParams params){
        return mapper.selectDirectoryById(params);
    }

    //文档路径
    public String getFilePath(DirectoryParams params){
        return mapper.selectFilePathByFileName(params);
    }

}
