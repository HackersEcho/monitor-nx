package com.dafang.monitor.nx;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.dafang.monitor.nx.*.mapper")
public class MonitorNxApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorNxApplication.class, args);
    }
    //解决sprongboot内置Tomcat允许url有特殊符号如_{}这类的
    @Bean
    public TomcatServletWebServerFactory webServerFactory() {

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {

            @Override

            public void customize(Connector connector) {

                connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");

                connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");

            }

        });

        return factory;

    }
}
