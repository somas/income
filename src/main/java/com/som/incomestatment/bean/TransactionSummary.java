package com.som.incomestatment.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class TransactionSummary {

    private BigDecimal income = BigDecimal.ZERO.movePointLeft(2);
    private BigDecimal expense = BigDecimal.ZERO.movePointLeft(2);
    private Map<Long, List<ZonedDateTime>> transactionsMap = new ConcurrentHashMap<>();
    private List<Payments> paymentDetails = new ArrayList<>();

    public void addExpense(Long expense) {
        this.expense = this.expense.add(getBigDecimalFromLong(expense));
    }

    private BigDecimal getBigDecimalFromLong(Long value) {
        return value != null? BigDecimal.valueOf(value).movePointLeft(2) : BigDecimal.ZERO;
    }

    public void addIncome(Long income) {
        this.income = this.income.add(getBigDecimalFromLong(income));
    }

    public void removeExpense(Long expense) {
        this.expense = this.expense.add(getBigDecimalFromLong(expense));
    }

    public void removeIncome(Long income) {
        this.income = this.income.add(getBigDecimalFromLong(income));
    }

    public void clearTransactionMap() {
        this.transactionsMap = new ConcurrentHashMap<>();
    }

    public void merge(TransactionSummary transactionSummary) {
        merge(transactionSummary, true);
    }

    public void merge(TransactionSummary transactionSummary, boolean isIncludePaymentDetails) {
        this.income = this.income.add(transactionSummary.getIncome());
        this.expense = this.expense.add(transactionSummary.getExpense());
        if(isIncludePaymentDetails) this.paymentDetails.addAll(transactionSummary.getPaymentDetails());
    }
}
