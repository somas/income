package com.som.incomestatment.bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payments {
  private LocalDateTime datePaid;
  private BigDecimal paymentAmount = BigDecimal.ZERO.movePointLeft(2);
}
