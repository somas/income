package com.som.incomestatment.bean;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payments {
  private ZonedDateTime datePaid;
  private BigDecimal paymentAmount = BigDecimal.ZERO.movePointLeft(2);
}
