package com.som.incomestatment.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.som.incomestatment.bean.*;
import com.som.incomestatment.config.IncomeStatementApplication;
import com.som.incomestatment.utils.IncomeCollector;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= IncomeStatementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, value = {
        "logging.level.com.som.incomestatment.client=DEBUG", "capitalone.url=http://localhost:9998"})
@DirtiesContext
@AutoConfigureWireMock(port=9998)
public class TransactionsApiTest {

    @Autowired
    private TransactionsApi transactionsApi;

    @Test
    public void getAllTransactions() throws Exception {

        stubFor(post(urlEqualTo("/get-all-transactions"))
                        .willReturn( aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(response())));

        Args args = Args.builder().apiToken("AppTokenForInterview")
                .token("1E67BB01ED9A1D4DC67146404DAD2279").uid(1110590645).build();
        Transactions transactions = transactionsApi.getAllTransactions(RequestArgs.builder().args(args).build());
        Assert.assertNotNull(transactions);
        Assert.assertEquals(5, transactions.getTransactions().size());
        Map<LocalDate, IncomeResponse> transactionColl = transactions.getTransactions().stream().collect(new IncomeCollector());
       //TODO Assert.assertEquals(transactionColl.get("2014-10-08").getExpense());

    }

    private String response() {
        return "{\n" +
                "\t\"error\": \"no-error\",\n" +
                "\t\"transactions\": [{\n" +
                "\t\t\"amount\": 34300,\n" +
                "\t\t\"is-pending\": false,\n" +
                "\t\t\"aggregation-time\": 1412686740000,\n" +
                "\t\t\"account-id\": \"nonce:comfy-cc/hdhehe\",\n" +
                "\t\t\"clear-date\": 1412790480000,\n" +
                "\t\t\"transaction-id\": \"1412790480000\",\n" +
                "\t\t\"raw-merchant\": \"7-ELEVEN 23853\",\n" +
                "\t\t\"categorization\": \"Unknown\",\n" +
                "\t\t\"merchant\": \"7-Eleven 23853\",\n" +
                "\t\t\"transaction-time\": \"2014-10-07T12:59:00.000Z\"\n" +
                "\t}, {\n" +
                "\t\t\"amount\": -30200,\n" +
                "\t\t\"is-pending\": false,\n" +
                "\t\t\"aggregation-time\": 1412702940000,\n" +
                "\t\t\"account-id\": \"nonce:comfy-cc/hdhehe\",\n" +
                "\t\t\"clear-date\": 1412985120000,\n" +
                "\t\t\"transaction-id\": \"1412985120000\",\n" +
                "\t\t\"raw-merchant\": \"SUNOCO 0299792200\",\n" +
                "\t\t\"categorization\": \"Unknown\",\n" +
                "\t\t\"merchant\": \"Sunoco\",\n" +
                "\t\t\"transaction-time\": \"2014-10-07T17:29:00.000Z\"\n" +
                "\t}, {\n" +
                "\t\t\"amount\": 99000,\n" +
                "\t\t\"is-pending\": false,\n" +
                "\t\t\"aggregation-time\": 1412733360000,\n" +
                "\t\t\"account-id\": \"nonce:comfy-cc/hdhehe\",\n" +
                "\t\t\"clear-date\": 1412845980000,\n" +
                "\t\t\"transaction-id\": \"1412845980000\",\n" +
                "\t\t\"raw-merchant\": \"Krispy Kreme Donuts\",\n" +
                "\t\t\"categorization\": \"Unknown\",\n" +
                "\t\t\"merchant\": \"Krispy Kreme Donuts\",\n" +
                "\t\t\"transaction-time\": \"2014-10-08T01:56:00.000Z\"\n" +
                "\t}, {\n" +
                "\t\t\"amount\": -188800,\n" +
                "\t\t\"is-pending\": false,\n" +
                "\t\t\"aggregation-time\": 1412760240000,\n" +
                "\t\t\"account-id\": \"nonce:comfy-cc/hdhehe\",\n" +
                "\t\t\"clear-date\": 1412942220000,\n" +
                "\t\t\"transaction-id\": \"1412942220000\",\n" +
                "\t\t\"raw-merchant\": \"GIANT EAG 1201\",\n" +
                "\t\t\"categorization\": \"Unknown\",\n" +
                "\t\t\"merchant\": \"Giant Eag 1201\",\n" +
                "\t\t\"transaction-time\": \"2014-10-08T09:24:00.000Z\"\n" +
                "\t}, {\n" +
                "\t\t\"amount\": -860800,\n" +
                "\t\t\"is-pending\": false,\n" +
                "\t\t\"aggregation-time\": 1412764860000,\n" +
                "\t\t\"account-id\": \"nonce:comfy-checking/hdhehe\",\n" +
                "\t\t\"clear-date\": 1412948520000,\n" +
                "\t\t\"transaction-id\": \"1412948520000\",\n" +
                "\t\t\"raw-merchant\": \"AT&T BILL PAYMENT\",\n" +
                "\t\t\"categorization\": \"Unknown\",\n" +
                "\t\t\"merchant\": \"At&T Bill Payment\",\n" +
                "\t\t\"transaction-time\": \"2014-10-08T10:41:00.000Z\"\n" +
                "\t}]\n" +
                "}";
    }

}