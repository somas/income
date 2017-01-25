package com.som.incomestatment.utils;


import com.som.incomestatment.bean.TransactionSummary;
import com.som.incomestatment.bean.Payments;
import com.som.incomestatment.bean.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class DailyTransactionSummaryCollector implements Collector<Transaction, Map<LocalDate, TransactionSummary>, Map<LocalDate, TransactionSummary>> {
    private boolean isIgnorePayments = false;

    public DailyTransactionSummaryCollector(boolean isIgnorePayments) {
        this.isIgnorePayments = isIgnorePayments;
    }

    @Override
    public Supplier<Map<LocalDate, TransactionSummary>> supplier() {
        return ConcurrentHashMap::new;
    }

    @Override
    public BiConsumer<Map<LocalDate, TransactionSummary>, Transaction> accumulator() {
        return (map, transaction) -> {
            TransactionSummary transactionSummary = map.get(transaction.getTransactionTimeAsLocalDate());
            if(transactionSummary == null) {
                transactionSummary = new TransactionSummary();
                map.put(transaction.getTransactionTimeAsLocalDate(), transactionSummary);
            }
            if(isIgnorePayments || !isPaymentTransaction(map, transaction)) {
                if(transaction.getAmount() < 0) {
                    transactionSummary.addExpense(transaction.getAmount());
                } else {
                    transactionSummary.addIncome(transaction.getAmount());
                }
            }
        };
    }

    private boolean isPaymentTransaction(Map<LocalDate, TransactionSummary> map, Transaction transaction) {
        TransactionSummary transactionSummary = map.get(transaction.getTransactionTimeAsLocalDate());
        TransactionSummary dayMinus1TransactionSummary = map.get(transaction.getTransactionTimeAsLocalDate().minusDays(1));
        Map<Long, List<ZonedDateTime>> transactionMap = transactionSummary.getTransactionsMap();
        Map<Long, List<ZonedDateTime>> dayMinus1TransactionMap = dayMinus1TransactionSummary != null? dayMinus1TransactionSummary.getTransactionsMap() : null;

        return filterAndUpdatePaymentTransaction(transaction, transactionMap, transactionSummary) ||
            filterAndUpdatePaymentTransaction(transaction, dayMinus1TransactionMap, dayMinus1TransactionSummary);
    }

    private boolean filterAndUpdatePaymentTransaction(Transaction transaction,
        Map<Long, List<ZonedDateTime>> transactionMap, TransactionSummary transactionSummary) {
        if(transactionMap == null) {
            return false;
        }

        if(transactionMap.get(transaction.getAmount() * -1) == null) {
            updateTransactionMap(transaction, transactionMap);
            return false;
        } else {
            Optional<ZonedDateTime> transTime = transactionMap.get(transaction.getAmount() * -1).stream().filter(localDateTime -> ChronoUnit.HOURS.between(localDateTime, transaction.getTransactionTime()) <= 24).findFirst();

            if(!transTime.isPresent()) {
                updateTransactionMap(transaction, transactionMap);
                return false;
            }
            adjustForPaymentTransaction(transaction, transactionMap, transactionSummary, transTime);
            return true;
        }
    }

    private void adjustForPaymentTransaction(Transaction transaction, Map<Long, List<ZonedDateTime>> transactionMap,
        TransactionSummary transactionSummary, Optional<ZonedDateTime> transTime) {
        Payments payments = null;
        if(transaction.getAmount() < 0) {
            transactionSummary.removeIncome(transaction.getAmount());
            payments = Payments.builder().datePaid(transTime.get()).paymentAmount(
                BigDecimal.valueOf(transaction.getAmount() * -1).movePointLeft(2)).build();
        } else {
            transactionSummary.removeExpense(transaction.getAmount());
            payments = Payments.builder().datePaid(transaction.getTransactionTime()).paymentAmount(BigDecimal.valueOf(transaction.getAmount()).movePointLeft(2)).build();
        }
        transactionMap.get(transaction.getAmount() * -1).remove(transTime.get());
        transactionSummary.getPaymentDetailsList().add(payments);
    }

    private void updateTransactionMap(Transaction transaction, Map<Long, List<ZonedDateTime>> transactionMap) {
        if(transactionMap.get(transaction.getAmount()) == null) {
            List<ZonedDateTime> tempList = new ArrayList<>();
            tempList.add(transaction.getTransactionTime());
            transactionMap.put(transaction.getAmount(), tempList);
        } else {
            transactionMap.get(transaction.getAmount()).add(transaction.getTransactionTime());
        }
    }

    @Override
    public BinaryOperator<Map<LocalDate, TransactionSummary>> combiner() {
        return (map1, map2) -> {
            map1.forEach((k, v) -> {
                TransactionSummary transactionSummary = map2.get(k);
                transactionSummary.getExpense().add(v.getExpense());
                transactionSummary.getIncome().add(v.getIncome());
            }
            );
            return map2;
        };
    }

    @Override
    public Function<Map<LocalDate, TransactionSummary>, Map<LocalDate, TransactionSummary>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
