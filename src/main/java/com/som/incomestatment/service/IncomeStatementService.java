package com.som.incomestatment.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.som.incomestatment.bean.Args;
import com.som.incomestatment.bean.IncomeResponse;
import com.som.incomestatment.bean.RequestArgs;
import com.som.incomestatment.bean.Transactions;
import com.som.incomestatment.client.TransactionsApi;
import com.som.incomestatment.utils.IncomeCollector;

@Component
public class IncomeStatementService {

    @Autowired
    TransactionsApi transactionsApi;

    public Map<String, IncomeResponse> getIncomeResponse(String apiToken, String token, Integer uid, Collection filterKeys) {
        Args args = Args.builder().apiToken(apiToken)
            .token(token).uid(uid).build();
        Transactions transactions = transactionsApi.getAllTransactions(RequestArgs.builder().args(args).build());
        Map<String, IncomeResponse>
            transactionColl = transactions.getTransactions().stream()
            .filter(t -> filterKeys != null && t.getMerchant()!= null && filterKeys.contains(t.getMerchant())? false: true)
            .collect(new IncomeCollector());

        return transactionColl;
    }
}
