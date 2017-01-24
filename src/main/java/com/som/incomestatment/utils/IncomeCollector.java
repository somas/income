package com.som.incomestatment.utils;


import com.som.incomestatment.bean.IncomeResponse;
import com.som.incomestatment.bean.Transaction;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class IncomeCollector implements Collector<Transaction, Map<LocalDate, IncomeResponse>, Map<LocalDate, IncomeResponse>> {

    @Override
    public Supplier<Map<LocalDate, IncomeResponse>> supplier() {
        return ConcurrentHashMap::new;
    }

    @Override
    public BiConsumer<Map<LocalDate, IncomeResponse>, Transaction> accumulator() {
        return (map, transaction) -> {
            IncomeResponse incomeResponse = map.get(transaction.getTransactionTime());
            if(incomeResponse == null) {
                incomeResponse = new IncomeResponse();
                map.put(transaction.getTransactionTime(), incomeResponse);

            }
            if(transaction.getAmount() < 0) {
                incomeResponse.addExpense(transaction.getAmount());
            } else {
                incomeResponse.addIncome(transaction.getAmount());
            }
        };
    }

    @Override
    public BinaryOperator<Map<LocalDate, IncomeResponse>> combiner() {
        return (map1, map2) -> {
            map1.forEach((k, v) -> {
                IncomeResponse incomeResponse = map2.get(k);
                incomeResponse.addExpense(v.getExpense());
                incomeResponse.addIncome(v.getIncome());
            }
            );
            return map2;
        };
    }

    @Override
    public Function<Map<LocalDate, IncomeResponse>, Map<LocalDate, IncomeResponse>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
