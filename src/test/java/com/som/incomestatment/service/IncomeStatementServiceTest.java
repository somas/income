package com.som.incomestatment.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.som.incomestatment.bean.Transaction;
import com.som.incomestatment.bean.TransactionSummary;
import com.som.incomestatment.bean.Transactions;
import com.som.incomestatment.client.TransactionsApi;

public class IncomeStatementServiceTest {

    @Mock
    TransactionsApi mockTransactionsApi;

    @InjectMocks IncomeStatementService incomeStatementService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test public void getDailyTransactionSummaryMap_happyPath() throws Exception {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime currentMonth = LocalDateTime.now();
        Transactions transactions = buildTransactions(lastMonth, currentMonth, null);

        Map<LocalDate, TransactionSummary> balanceSheet = incomeStatementService
            .getDailyTransactionSummaryMap(null, transactions);

        Assert.assertEquals(BigDecimal.valueOf(-2550).movePointLeft(2),  balanceSheet.get(lastMonth.toLocalDate()).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1000).movePointLeft(2),  balanceSheet.get(lastMonth.toLocalDate()).getIncome());

        Assert.assertEquals(BigDecimal.valueOf(0).movePointLeft(2), balanceSheet.get(currentMonth.toLocalDate()).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1100).movePointLeft(2), balanceSheet.get(currentMonth.toLocalDate()).getIncome());
    }

    @Test public void getDailyTransactionSummaryMap_withFilter() throws Exception {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime currentMonth = LocalDateTime.now();
        Transactions transactions = buildTransactions(lastMonth, currentMonth, "Krispy Kreme Donuts");

        Map<LocalDate, TransactionSummary> balanceSheet = incomeStatementService
            .getDailyTransactionSummaryMap(Arrays.asList("Krispy Kreme Donuts"), transactions);

        Assert.assertEquals(BigDecimal.valueOf(-2050).movePointLeft(2),  balanceSheet.get(lastMonth.toLocalDate()).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1000).movePointLeft(2),  balanceSheet.get(lastMonth.toLocalDate()).getIncome());

        Assert.assertEquals(BigDecimal.valueOf(0).movePointLeft(2), balanceSheet.get(currentMonth.toLocalDate()).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1100).movePointLeft(2), balanceSheet.get(currentMonth.toLocalDate()).getIncome());
    }

    @Test public void getDailyTransactionSummaryMap_withPaymentTransaction() throws Exception {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime currentMonth = LocalDateTime.now();
        Transactions transactions = buildTransactionsWithPayments(lastMonth, currentMonth, null);

        Map<LocalDate, TransactionSummary> balanceSheet = incomeStatementService
            .getDailyTransactionSummaryMap(null, transactions);

        Assert.assertEquals(BigDecimal.valueOf(-500).movePointLeft(2), balanceSheet.get(lastMonth.toLocalDate()).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1000).movePointLeft(2), balanceSheet.get(lastMonth.toLocalDate()).getIncome());

        Assert.assertEquals(BigDecimal.valueOf(0).movePointLeft(2), balanceSheet.get(currentMonth.toLocalDate()).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(2100).movePointLeft(2), balanceSheet.get(currentMonth.toLocalDate()).getIncome());
    }

    private Transactions buildTransactions(LocalDateTime lastMonth, LocalDateTime currentMonth, String merchantId) {
        Transaction transaction = Transaction.builder().transactionId("1").transactionTime(lastMonth).amount(1000L).build();
        Transaction transaction2 = Transaction.builder().transactionId("2").transactionTime(lastMonth).amount(-500L).merchant(merchantId).build();
        Transaction transaction3 = Transaction.builder().transactionId("3").transactionTime(lastMonth).amount(-2050L).build();
        Transaction transaction4 = Transaction.builder().transactionId("4").transactionTime(currentMonth).amount(100L).build();
        Transaction transaction5 = Transaction.builder().transactionId("5").transactionTime(currentMonth).amount(1000L).build();
        Transactions transactions = new Transactions();

        transactions.setTransactions(Arrays.asList(transaction, transaction2, transaction3, transaction4, transaction5));
        return transactions;
    }

    private Transactions buildTransactionsWithPayments(LocalDateTime lastMonth, LocalDateTime currentMonth, String merchantId) {
        Transaction transaction = Transaction.builder().transactionId("1").transactionTime(lastMonth).amount(1000L).build();
        Transaction transaction2 = Transaction.builder().transactionId("2").transactionTime(lastMonth).amount(-500L).merchant(merchantId).build();
        Transaction transaction3 = Transaction.builder().transactionId("3").transactionTime(lastMonth).amount(-2050L).build();
        Transaction transaction4 = Transaction.builder().transactionId("4").transactionTime(currentMonth).amount(100L).build();
        Transaction transaction5 = Transaction.builder().transactionId("5").transactionTime(currentMonth).amount(1000L).build();
        Transaction transaction6 = Transaction.builder().transactionId("6").transactionTime(lastMonth.plusDays(1)).amount(2050L).build();
        Transaction transaction7 = Transaction.builder().transactionId("7").transactionTime(currentMonth.plusDays(1)).amount(-1000L).build();
        Transaction transaction8 = Transaction.builder().transactionId("8").transactionTime(currentMonth).amount(1000L).build();
        Transaction transaction9 = Transaction.builder().transactionId("9").transactionTime(currentMonth).amount(1000L).build();
        Transactions transactions = new Transactions();

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        transactionList.add(transaction2);
        transactionList.add(transaction3);
        transactionList.add(transaction4);
        transactionList.add(transaction5);
        transactionList.add(transaction6);
        transactionList.add(transaction7);
        transactionList.add(transaction8);
        transactionList.add(transaction9);

        transactions.setTransactions(transactionList);
        return transactions;
    }

    @Test public void getMonthlyTransactionSummaryMap_happyPath() throws Exception {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        LocalDateTime currentMonth = LocalDateTime.now();
        Transactions transactions = buildTransactionsWithPayments(lastMonth, currentMonth, null);

        Map<LocalDate, TransactionSummary> balanceSheet = incomeStatementService.getDailyTransactionSummaryMap(null, transactions);

        Map<String, TransactionSummary> monthlyTransactionSummary = incomeStatementService.getMonthlyTransactionSummary(balanceSheet);

        Assert.assertNotNull(monthlyTransactionSummary);
        Assert.assertTrue(monthlyTransactionSummary.size() > 0);

        Assert.assertEquals(BigDecimal.valueOf(-500).movePointLeft(2), monthlyTransactionSummary.get(lastMonth.toLocalDate().format(Transaction.YYYY_MM_FORMATTER)).getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1000).movePointLeft(2), monthlyTransactionSummary.get(lastMonth.toLocalDate().format(Transaction.YYYY_MM_FORMATTER)).getIncome());

        Assert.assertEquals(BigDecimal.valueOf(-250).movePointLeft(2), monthlyTransactionSummary.get("average").getExpense());
        Assert.assertEquals(BigDecimal.valueOf(1550).movePointLeft(2), monthlyTransactionSummary.get("average").getIncome());
    }
}
