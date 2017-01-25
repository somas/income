# Income Statement

Application that displays users monthly income and expenses, along with averages.

## Features

* Configurable transaction filter
  - e.g. ignore-donut expense from statement

* Configurable option to detect Credit Card payments and not include them in expense/income

## Walkthrough

### Prerequisite
* Java version 8 and above
* Optional - Postman

### Setup
1. git clone https://github.com/somas/income.git
2. cd income
3. ```./gradlew :bootRun```

when app is successfully running, you should see message similar to below

```
   --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
   --- [           main] c.s.i.config.IncomeStatementApplication  : Started IncomeStatementApplication in 2.645 seconds (JVM running for 2.913)
   > Building 80% > :bootRun
```

### Usage

Once App Server is running

#### Option 1: Postman REST client
1. Download postman ```https://www.getpostman.com/```
2. Open postman app
3. In the menu bar, click 'Import' button and select 'Choose Files'
4. cd income/documents and select 'capitalone.postman_collection'
5. Click on the 'capitalone' collection and select individual request to run.

#### Option 2: cURL
1. Open Terminal (for windows refer: https://help.zendesk.com/hc/en-us/articles/229136847-Installing-and-using-cURL#curl_win)
2. Paste the following cURL
- vanilla request

```
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{"token": "1E67BB01ED9A1D4DC67146404DAD2279","api-token": "AppTokenForInterview"}' "http://localhost:8080/income/1110590645/statement"
```
- with ignore-cc-payment false

```
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{"token": "1E67BB01ED9A1D4DC67146404DAD2279","api-token": "AppTokenForInterview","ignore-cc-payments":false}' "http://localhost:8080/income/1110590645/statement"
```

- with donut filter

```
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{ "token": "1E67BB01ED9A1D4DC67146404DAD2279", "api-token": "AppTokenForInterview", "filter":"ignore-donuts"}' "http://localhost:8080/income/1110590645/statement"
```

- with ignore-cc-payments

```
curl -X POST -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{"token": "1E67BB01ED9A1D4DC67146404DAD2279","api-token": "AppTokenForInterview","ignore-cc-payments": true}' "http://localhost:8080/income/1110590645/statement"
```

# Running tests

``` ./gradlew test ```