package com.som.incomestatment.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.som.incomestatment.bean.IncomeResponse;
import com.som.incomestatment.service.IncomeStatementService;

public class IncomeStatementControllerTest {

    @Mock
    IncomeStatementService mockIncomeStatementService;

    @InjectMocks
    IncomeStatementController incomeStatementController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test public void getStatement_happyPath() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(IncomeStatementController.TOKEN, "12345");
        requestBody.put(IncomeStatementController.API_TOKEN, "ABCDE");

        Mockito.when(mockIncomeStatementService.getIncomeResponse(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyCollection()))
            .thenReturn(Mockito.mock(Map.class));

        Map<String, IncomeResponse> response = incomeStatementController.getStatement("123", requestBody);

        Assert.assertNotNull(response);
        Mockito.verify(mockIncomeStatementService, Mockito.times(1)).getIncomeResponse(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyCollection());
    }

    @Test(expected = RuntimeException.class) public void getStatement_badUid() throws Exception {
        Map<String, String> requestBody = new HashMap<>();

        incomeStatementController.getStatement("A123", requestBody);
    }

    @Test(expected = RuntimeException.class) public void getStatement_nullRequestBody() throws Exception {
        incomeStatementController.getStatement("A123", null);
    }

    @Test(expected = RuntimeException.class) public void getStatement_nullToken() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(IncomeStatementController.TOKEN, null);
        requestBody.put(IncomeStatementController.API_TOKEN, "ABCDE");

        incomeStatementController.getStatement("123", requestBody);
    }

    @Test(expected = RuntimeException.class) public void getStatement_nullApiToken() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(IncomeStatementController.TOKEN, "12345");
        requestBody.put(IncomeStatementController.API_TOKEN, null);

        incomeStatementController.getStatement("123", requestBody);
    }

}
