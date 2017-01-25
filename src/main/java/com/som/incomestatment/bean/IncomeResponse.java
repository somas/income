package com.som.incomestatment.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IncomeResponse {

    private BigDecimal income = BigDecimal.ZERO.movePointLeft(2);
    private BigDecimal expense = BigDecimal.ZERO.movePointLeft(2);

    public void addExpense(Long expense) {
        this.expense = this.expense.add(getBigDecimalFromLong(expense));
    }

    private BigDecimal getBigDecimalFromLong(Long value) {
        return value != null? BigDecimal.valueOf(value).movePointLeft(2) : BigDecimal.ZERO;
    }

    public void addIncome(Long income) {
        this.income = this.income.add(getBigDecimalFromLong(income));
    }
}
