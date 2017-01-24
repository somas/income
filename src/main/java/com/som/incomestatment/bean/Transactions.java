package com.som.incomestatment.bean;

import lombok.Data;

import java.util.List;

@Data
public class Transactions {
    private List<Transaction> transactions;
}
