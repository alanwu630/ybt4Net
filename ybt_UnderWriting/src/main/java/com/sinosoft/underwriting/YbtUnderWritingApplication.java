package com.sinosoft.underwriting;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.sinosoft.underwriting","com.sinosoft.common","com.sinosoft.dao"})
@EnableFeignClients
public class YbtUnderWritingApplication {

    public static void main(String[] args) {
        SpringApplication.run(YbtUnderWritingApplication.class, args);
    }

}
