package com.dafang.monitor.nx.product.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
@ApiModel(value = "前端传入的参数实体类")
public class DirectoryParams implements Serializable  {

    @ApiModelProperty(value = "文档名称")
    private String displayName;
    @ApiModelProperty(value = "文档路径")
    private String filePath;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "文档编号")
    private String directoryId;
    @ApiModelProperty(value = "文档类型")
    private String fileType;

    public DirectoryParams(String displayName) {
        this.displayName = displayName;
    }

    public DirectoryParams(String displayName, String filePath, String createTime, String directoryId, String fileType) {
        this.displayName = displayName;
        this.filePath = filePath;
        this.createTime = createTime;
        this.directoryId = directoryId;
        this.fileType = fileType;
    }
}
