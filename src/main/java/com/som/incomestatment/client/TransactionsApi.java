package com.som.incomestatment.client;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import feign.Body;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.som.incomestatment.bean.RequestArgs;
import com.som.incomestatment.bean.Transactions;
import com.som.incomestatment.config.FeignConfig;

@Component
@FeignClient(name="transaction-api", url="${capitalone.url}", configuration = FeignConfig.class)
public interface TransactionsApi {
    @RequestMapping(value = "/get-all-transactions", method = POST)
    @Headers("Content-Type: application/json")
    @Body("%7B \"args\" : { \"uid\" : 1110590645, \"token\" : \"1E67BB01ED9A1D4DC67146404DAD2279\","
        + "\"api-token\" : \"AppTokenForInterview\",\"json-strict-mode\" : false,\"json-verbose-response\" : false%7D")
    Transactions getAllTransactions(RequestArgs args);
}
