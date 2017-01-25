package com.som.incomestatment.service;

import com.som.incomestatment.bean.IncomeResponse;
import com.som.incomestatment.bean.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.som.incomestatment.bean.RequestArgs;
import com.som.incomestatment.bean.Transactions;
import com.som.incomestatment.client.TransactionsApi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

public class IncomeStatementServiceTest {

    @Mock
    TransactionsApi mockTransactionsApi;

    @InjectMocks IncomeStatementService incomeStatementService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test public void getIncomeResponse() throws Exception {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate currentMonth = LocalDate.now();
        Transactions transactions = buildTransactions(lastMonth, currentMonth, null);

        Mockito.when(mockTransactionsApi.getAllTransactions(Mockito.any(RequestArgs.class))).thenReturn(transactions);

        Map<LocalDate, IncomeResponse> balanceSheet = incomeStatementService
            .getIncomeResponse("apiToken", "token", 1, null);

        Assert.assertEquals(BigDecimal.valueOf(-2550).movePointLeft(2),  balanceSheet.get(lastMonth).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1000).movePointLeft(2),  balanceSheet.get(lastMonth).getIncome());

        Assert.assertEquals(BigDecimal.valueOf(0).movePointLeft(2), balanceSheet.get(currentMonth).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1100).movePointLeft(2), balanceSheet.get(currentMonth).getIncome());
    }

    @Test public void getIncomeResponse_withFilter() throws Exception {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate currentMonth = LocalDate.now();
        Transactions transactions = buildTransactions(lastMonth, currentMonth, "Krispy Kreme Donuts");

        Mockito.when(mockTransactionsApi.getAllTransactions(Mockito.any(RequestArgs.class))).thenReturn(transactions);

        Map<LocalDate, IncomeResponse> balanceSheet = incomeStatementService
            .getIncomeResponse("apiToken", "token", 1, Arrays.asList("Krispy Kreme Donuts"));

        Assert.assertEquals(BigDecimal.valueOf(-2050).movePointLeft(2),  balanceSheet.get(lastMonth).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1000).movePointLeft(2),  balanceSheet.get(lastMonth).getIncome());

        Assert.assertEquals(BigDecimal.valueOf(0).movePointLeft(2), balanceSheet.get(currentMonth).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1100).movePointLeft(2), balanceSheet.get(currentMonth).getIncome());
    }

    private Transactions buildTransactions(LocalDate lastMonth, LocalDate currentMonth, String merchantId) {
        Transaction transaction = Transaction.builder().transactionId("1").transactionTime(lastMonth).amount(1000L).build();
        Transaction transaction2 = Transaction.builder().transactionId("2").transactionTime(lastMonth).amount(-500L).merchant(merchantId).build();
        Transaction transaction3 = Transaction.builder().transactionId("3").transactionTime(lastMonth).amount(-2050L).build();
        Transaction transaction4 = Transaction.builder().transactionId("4").transactionTime(currentMonth).amount(100L).build();
        Transaction transaction5 = Transaction.builder().transactionId("5").transactionTime(currentMonth).amount(1000L).build();
        Transactions transactions = new Transactions();

        transactions.setTransactions(Arrays.asList(transaction, transaction2, transaction3, transaction4, transaction5));
        return transactions;
    }
}
