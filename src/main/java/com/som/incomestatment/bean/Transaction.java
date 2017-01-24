package com.som.incomestatment.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class Transaction {

    private Long amount;
    private boolean isPending;
    private Date aggregationTime;
    private String accountId;
    private Date clearDate;
    private String transactionId;
    private String rawMerchant;
    private String categorization;
    private String merchant;
    private LocalDate transactionTime;

    public String transactionAsString() {
        return null;
    }

}
