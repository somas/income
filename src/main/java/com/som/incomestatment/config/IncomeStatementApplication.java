package com.som.incomestatment.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.som.incomestatment.bean.FilterProperties;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

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

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        b.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        b.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return b;
    }
}
