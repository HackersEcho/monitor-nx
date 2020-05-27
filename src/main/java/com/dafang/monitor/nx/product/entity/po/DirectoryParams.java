package com.dafang.monitor.nx.product.entity.po;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
public class DirectoryParams implements Serializable  {

    private String displayName;//文档名称
    private String filePath;//文档路径
    private String createTime;//创建时间
    private String directoryId;//文档编号
    private String fileType;//文件类型

    public DirectoryParams(String displayName) {
        this.displayName = displayName;
    }

    public DirectoryParams(String displayName, String filePath, String createTime, String directoryId) {
        this.displayName = displayName;
        this.filePath = filePath;
        this.createTime = createTime;
        this.directoryId = directoryId;
    }
}
