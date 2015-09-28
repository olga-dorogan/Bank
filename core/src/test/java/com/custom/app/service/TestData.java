package com.custom.app.service;

import com.custom.app.dto.Account;
import com.custom.app.dto.Transfer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

/**
 * Created by olga on 27.09.15.
 */
public class TestData {
    static final com.custom.app.dto.Client PREDEFINED_CLIENT = new com.custom.app.dto.Client(-1, "Ivan", "Ivanov", "ЕР123456");
    static final com.custom.app.dto.Client ADDED_CLIENT = new com.custom.app.dto.Client(1, "firstName", "lastName", "ЕР000000");
    static final Account PREDEFINED_ACCOUNT = new Account(-1, "test_account", new BigDecimal(1000.00));
    static final Account ADDED_ACCOUNT = new Account("added_account", new BigDecimal(2000.00), PREDEFINED_CLIENT);

    static final Account PREDEFINED_ACCOUNT_1 = new Account(-1, "1_account", new BigDecimal(1000.00), PREDEFINED_CLIENT);
    static final Account PREDEFINED_ACCOUNT_2 = new Account(-2, "2_account", new BigDecimal(1000.00), PREDEFINED_CLIENT);
    static final Transfer PREDEFINED_TRANSFER = new Transfer(-1, PREDEFINED_ACCOUNT_1, PREDEFINED_ACCOUNT_2,
            Date.from(Instant.parse("2015-09-01T01:00:00Z")), new BigDecimal(500));
    static final Transfer ADDED_TRANSFER = new Transfer(PREDEFINED_ACCOUNT_1, PREDEFINED_ACCOUNT_2,
            Date.from(Instant.parse("2015-09-01T00:00:00Z")), new BigDecimal(500));
    static final Transfer ADDED_CREDIT = new Transfer(null, PREDEFINED_ACCOUNT_1,
            Date.from(Instant.parse("2015-09-02T00:00:00Z")), new BigDecimal(200));
    static final Transfer ADDED_DEBIT = new Transfer(PREDEFINED_ACCOUNT_1, null,
            Date.from(Instant.parse("2015-09-02T00:00:00Z")), new BigDecimal(200));

    private TestData() {

    }
}
