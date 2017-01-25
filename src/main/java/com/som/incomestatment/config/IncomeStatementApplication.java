package com.som.incomestatment.config;

import com.som.incomestatment.bean.FilterProperties;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.som.incomestatment.client"})
@ComponentScan("com.som")
@EnableConfigurationProperties(FilterProperties.class)
public class IncomeStatementApplication {
    public static void main(String[] args) {
        SpringApplication.run(IncomeStatementApplication.class, args);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
