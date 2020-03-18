package com.dafang.monitor.nx.event.entity.po;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
@ApiModel("用户实体类")
public class User implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private String sex;
    private Integer age;
    private String address;
    private String phone;

}
