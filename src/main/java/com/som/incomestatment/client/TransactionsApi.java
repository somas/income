package com.som.incomestatment.client;

import com.som.incomestatment.bean.Args;
import com.som.incomestatment.bean.RequestArgs;
import com.som.incomestatment.bean.Transactions;
import com.som.incomestatment.config.FeignConfig;
import org.omg.CORBA.Request;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
@FeignClient(name="transaction-api", url="${capitalone.url}", configuration = FeignConfig.class)
public interface TransactionsApi {
    @RequestMapping(value = "/get-all-transactions", method = POST)
    Transactions getAllTransactions(RequestArgs args);
}
