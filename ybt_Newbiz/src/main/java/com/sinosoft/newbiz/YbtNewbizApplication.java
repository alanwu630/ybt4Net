package com.sinosoft.newbiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.sinosoft.newbiz","com.sinosoft.dao","com.sinosoft.common"})
@EnableFeignClients
public class YbtNewbizApplication {

    public static void main(String[] args) {
        SpringApplication.run(YbtNewbizApplication.class, args);
    }

}
