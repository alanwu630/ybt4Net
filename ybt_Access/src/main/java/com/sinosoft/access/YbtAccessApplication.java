package com.sinosoft.access;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.sinosoft.api")
public class YbtAccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(YbtAccessApplication.class, args);
    }

}
