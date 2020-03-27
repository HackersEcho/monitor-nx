package com.dafang.monitor.nx.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @description:展示某些字段
 * @author: echo
 * @createDate: 2020/3/14
 * @version: 1.0
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Apicp {
    String value(); //对象属性值

}