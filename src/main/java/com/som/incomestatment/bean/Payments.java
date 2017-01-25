package com.som.incomestatment.bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Payments {
  private LocalDateTime datePaid;
  private BigDecimal paymentAmount = BigDecimal.ZERO.movePointLeft(2);
}
