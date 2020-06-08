package com.dafang.monitor.nx.doc.mapper;

import com.dafang.monitor.nx.doc.entity.po.Doc;
import com.dafang.monitor.nx.doc.entity.po.DocParams;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.MyMapper;

import java.util.List;

@Mapper
@Repository
public interface DocMapper extends MyMapper<Doc> {
    List<Doc> periodsList(DocParams params);
    List<Doc> periodDays(DocParams params);
}
