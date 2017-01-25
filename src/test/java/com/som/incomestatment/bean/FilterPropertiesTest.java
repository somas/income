package com.som.incomestatment.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.som.incomestatment.config.IncomeStatementApplication;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= IncomeStatementApplication.class)
public class FilterPropertiesTest {

    @Autowired FilterProperties filterProperties;

    @Test public void getFilterTransactionMap() throws Exception {
        Assert.notNull(filterProperties.getTransaction());
        Assert.isTrue(filterProperties.getTransaction().size() == 1);
    }

}
