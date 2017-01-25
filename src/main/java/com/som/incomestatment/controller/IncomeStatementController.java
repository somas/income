package com.som.incomestatment.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.som.incomestatment.bean.TransactionSummary;
import com.som.incomestatment.service.IncomeStatementService;

@RestController
@RequestMapping("/income")
public class IncomeStatementController {

    public final static String TOKEN = "token";
    public final static String API_TOKEN = "api-token";
    public final static String FILTER_KEY = "filter";
    public final static String IGNORE_CC_PAYMENTS = "ignore-cc-payments";

    @Autowired
    private IncomeStatementService incomeStatementService;

    @RequestMapping(value = "/{uid}/statement", method = RequestMethod.POST)
    public Map<String, TransactionSummary> getStatement(@PathVariable String uid, @RequestBody Map<String, String> requestBody) {
        validate(requestBody, uid);
        return incomeStatementService.getIncomeResponse(requestBody.get(API_TOKEN), requestBody.get(TOKEN), Integer.valueOf(uid),
            getFilterKeys(requestBody), getIgnorePaymentsOption(requestBody));
    }

    private List<String> getFilterKeys(@RequestBody Map<String, String> requestBody) {
        String filterKey = requestBody.get(FILTER_KEY);
        return StringUtils.isNotBlank(filterKey)? Arrays.asList(requestBody.get(FILTER_KEY)) : null;
    }

    private Boolean getIgnorePaymentsOption(@RequestBody Map<String, String> requestBody) {
        if(StringUtils.isNotBlank(requestBody.get(IGNORE_CC_PAYMENTS))) {
            try {
                return Boolean.valueOf(requestBody.get(IGNORE_CC_PAYMENTS));
            } catch(Exception e) {
                throw new RuntimeException("Expected boolean value for ignore-cc-paymenst");
            }
        }
        return false;
    }

    private void validate(Map<String, String> requestBody, String uid) {
        checkForValidUID(uid);

        checkForRequiredAttributes(requestBody);

        checkForValidValues(requestBody);
    }

    private void checkForValidUID(String uid) {
        try {
            Integer.valueOf(uid);
        } catch(Exception e) {
            throw new RuntimeException("UID must be integer");
        }
    }

    private void checkForValidValues(Map<String, String> requestBody) {
        if(requestBody == null) throw new RuntimeException("Empty request body");
        if(StringUtils.isBlank(requestBody.get(TOKEN))) throw new RuntimeException("Bad token data");
        if(StringUtils.isBlank(requestBody.get(API_TOKEN))) throw new RuntimeException("Bad api token");
    }

    private void checkForRequiredAttributes(Map<String, String> body) {
        if(body.size() < 2) throw new RuntimeException("Missing one of the required attributes: token, api-token");
    }

}
