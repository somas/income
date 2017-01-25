package com.som.incomestatment.bean;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class Payments {
  private ZonedDateTime datePaid;
  private BigDecimal paymentAmount = BigDecimal.ZERO.movePointLeft(2);
  private String transactionId;
  private String merchant;
}
