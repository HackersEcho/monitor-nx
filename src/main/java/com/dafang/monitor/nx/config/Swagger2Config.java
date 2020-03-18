package com.dafang.monitor.nx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author echo
 * @create 2020-03-02
 */
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("气候事件")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dafang.monitor.nx.event.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket createRestApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("数据统计")
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.dafang.monitor.nx.statistics.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("宁夏API文档")//接口文档主题
                .description("API网关接口：http://www.baidu.com")//地址
                .termsOfServiceUrl("www.taobao.com")//路径
                .version("1.0.0")//版本号
                .build();
    }
}
