package com.som.incomestatment.bean;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long amount;
    private String transactionId;
    private String merchant;
    private LocalDate transactionTime;
}
