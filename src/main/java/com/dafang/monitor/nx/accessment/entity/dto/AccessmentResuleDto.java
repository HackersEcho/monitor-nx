package com.dafang.monitor.nx.accessment.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "REST API接口统一相应接口实体类")
public class AccessmentResuleDto<T> implements Serializable {
    @ApiModelProperty(value = "respCode : 返回代码，1表示成功，其它的都有对应问题")
    private int respCode = 1;

    @ApiModelProperty(value = "message : 如果code!=1,错误信息")
    private String message="成功！";

    @SuppressWarnings("unchecked")
    @ApiModelProperty(value = "respCode为1时返回结果")
    private T respData = (T) new Object();
}
