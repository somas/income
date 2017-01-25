package com.som.incomestatment.bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    DateTimeFormatter yyyyMMFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    private Long amount;
    private String transactionId;
    private String merchant;
    private LocalDateTime transactionTime;

    public String getTransactionTimeAsString() {
        return this.transactionTime.format(yyyyMMFormatter);
    }
}
