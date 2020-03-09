package com.dafang.monitor.nx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.dafang.monitor.nx.event.mapper")
public class MonitorNxApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorNxApplication.class, args);
    }

}
