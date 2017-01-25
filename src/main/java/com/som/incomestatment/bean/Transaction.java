package com.som.incomestatment.bean;

import java.time.LocalDate;
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
    public static final DateTimeFormatter YYYY_MM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private Long amount;
    private String transactionId;
    private String merchant;
    private LocalDateTime transactionTime;

    public String getTransactionTimeAsString() {
        return this.transactionTime.format(YYYY_MM_FORMATTER);
    }

    public LocalDate getTransactionTimeAsLocalDate() {
        return this.transactionTime.toLocalDate();
    }
}
