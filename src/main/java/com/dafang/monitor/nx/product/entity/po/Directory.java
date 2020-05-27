package com.dafang.monitor.nx.product.entity.po;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;
/*
产品文档
 */
@Entity
@Data
public class Directory implements Serializable {

    private String displayName;//文档名称
    private String filePath;//文档路径
    private String createTime;//创建时间
    private String directoryId;//文档编号
    private String fileType;//文档类型

}
