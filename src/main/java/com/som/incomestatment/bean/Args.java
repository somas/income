package com.som.incomestatment.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Args {
    private Integer uid;
    private String token;
    private String apiToken;
    private boolean jsonStrictMode = false;
    private boolean jsonVerboseResponse = false;
}
