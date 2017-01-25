package com.som.incomestatment.bean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("filter")
@Data
public class FilterProperties {
    Map<String, String[]> transaction = new HashMap<>();
}
