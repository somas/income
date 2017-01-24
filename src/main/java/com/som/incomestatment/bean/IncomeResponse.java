package com.som.incomestatment.bean;

import lombok.Data;

@Data
public class IncomeResponse {

    private Long income = Long.valueOf(0);
    private Long expense = Long.valueOf(0);

    public void addExpense(Long expense) {
        this.expense = this.expense + expense;
    }

    public void addIncome(Long income) {
        this.income = this.income + income;
    }
}
