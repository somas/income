package com.som.incomestatment.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.som.incomestatment.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.som.incomestatment.client.TransactionsApi;
import com.som.incomestatment.utils.DailyTransactionSummaryCollector;

@Component
public class IncomeStatementService {

    @Autowired
    TransactionsApi transactionsApi;

    public Map<String, TransactionSummary> getIncomeResponse(String apiToken, String token, Integer uid, Collection filterKeys, boolean isIgnorePayments) {
        Args args = Args.builder().apiToken(apiToken)
            .token(token).uid(uid).build();

        Transactions transactions = transactionsApi.getAllTransactions(RequestArgs.builder().args(args).build());

        Map<LocalDate, TransactionSummary> dailyTransactionSummaryMap =
            getDailyTransactionSummaryMap(filterKeys, transactions, isIgnorePayments);

        return getMonthlyTransactionSummary(dailyTransactionSummaryMap);
    }

    protected Map<LocalDate, TransactionSummary> getDailyTransactionSummaryMap(Collection filterKeys,
        Transactions transactions, boolean isIgnorePayments) {
        return transactions.getTransactions().stream()
            .filter(t -> filterKeys != null && t.getMerchant()!= null && filterKeys.contains(t.getMerchant())? false: true)
            .collect(new DailyTransactionSummaryCollector(isIgnorePayments));
    }

    protected Map<String, TransactionSummary> getMonthlyTransactionSummary(Map<LocalDate, TransactionSummary> dailyTransactionSummaryMap) {
        Map<String, TransactionSummary> monthlyTransactionSummary = new HashMap<>();
        monthlyTransactionSummary.put("average", new TransactionSummary());
        dailyTransactionSummaryMap.forEach((k,v) -> {
            String key = k.format(Transaction.YYYY_MM_FORMATTER);
            if(monthlyTransactionSummary.get(key) == null) {
                monthlyTransactionSummary.put(key, v);
            } else {
                monthlyTransactionSummary.get(key).merge(v, true);
            }
            addToAverage(monthlyTransactionSummary, v);
            v.clearTransactionMap();
        });
        calculateAverage(monthlyTransactionSummary);
        return monthlyTransactionSummary;
    }

    private void calculateAverage(Map<String, TransactionSummary> monthlyTransactionSummary) {
        BigDecimal numberOfMonths = BigDecimal.valueOf(monthlyTransactionSummary.size() - 1);
        TransactionSummary averageTransactionSummary = monthlyTransactionSummary.get("average");
        BigDecimal avgExpense = averageTransactionSummary.getExpense().divide(numberOfMonths);
        BigDecimal avgIncome = averageTransactionSummary.getIncome().divide(numberOfMonths);
        averageTransactionSummary.setExpense(avgExpense);
        averageTransactionSummary.setIncome(avgIncome);
    }

    private void addToAverage(Map<String, TransactionSummary> monthlyTransactionSummary, TransactionSummary v) {
        monthlyTransactionSummary.get("average").merge(v);
    }


}
