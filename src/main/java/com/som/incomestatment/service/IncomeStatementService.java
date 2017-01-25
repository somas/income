package com.som.incomestatment.service;

import com.som.incomestatment.bean.*;
import com.som.incomestatment.client.TransactionsApi;
import com.som.incomestatment.utils.DailyTransactionSummaryCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Component
public class IncomeStatementService {

    @Autowired
    TransactionsApi transactionsApi;

    @Autowired
    FilterProperties filterProperties;

    public Map<String, TransactionSummary> getIncomeResponse(String apiToken, String token, Integer uid, String filterKeys, boolean isIgnorePayments) {
        Args args = Args.builder().apiToken(apiToken)
            .token(token).uid(uid).build();

        Transactions transactions = transactionsApi.getAllTransactions(RequestArgs.builder().args(args).build());

        Map<LocalDate, TransactionSummary> dailyTransactionSummaryMap =
            getDailyTransactionSummaryMap(getTransactionFilters(filterKeys), transactions, isIgnorePayments);

        return getMonthlyTransactionSummary(dailyTransactionSummaryMap);
    }

    private List<String> getTransactionFilters(String filterKeys) {
        String[] transactionFilters = filterProperties.getTransaction().get(filterKeys);
        return transactionFilters != null? Arrays.asList(transactionFilters) : null;
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
                monthlyTransactionSummary.get(key).merge(v);
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
        BigDecimal avgExpense = averageTransactionSummary.getExpense().divide(numberOfMonths,2, RoundingMode.HALF_UP);
        BigDecimal avgIncome = averageTransactionSummary.getIncome().divide(numberOfMonths, 2, RoundingMode.HALF_UP);
        averageTransactionSummary.setExpense(avgExpense);
        averageTransactionSummary.setIncome(avgIncome);
    }

    private void addToAverage(Map<String, TransactionSummary> monthlyTransactionSummary, TransactionSummary v) {
        monthlyTransactionSummary.get("average").merge(v, false);
    }


}
