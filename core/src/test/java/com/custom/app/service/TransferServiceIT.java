package com.custom.app.service;

import com.custom.app.dto.Account;
import com.custom.app.dto.Transfer;
import com.custom.app.util.TestConfig;
import com.custom.config.BankConfig;
import com.custom.config.PersistenceConfig;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;

/**
 * Created by olga on 28.09.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("spring-test")
@ContextConfiguration(classes = {PersistenceConfig.class, BankConfig.class, TestConfig.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DbUnitConfiguration(databaseConnection = {"dataSource"})
@DatabaseSetup(value = {
        "/datasets/client-service-test/one-client.xml",
        "/datasets/client-service-test/two-accounts.xml",
        "/datasets/transfer-service-test/one-transfer.xml"
})
public class TransferServiceIT {
    @Autowired
    private TransferService transferService;

    @Test
    public void testInjectionShouldInject() {
        assertNotNull(transferService);
    }

    @Test
    public void testFindAll() {
        List<Transfer> transfers = transferService.findAll();
        assertThat(transfers.size(), is(1));
        assertThat(transfers.get(0), allOf(
                hasProperty("id", is(TestData.PREDEFINED_TRANSFER.getId())),
                hasProperty("accountFrom", is(TestData.PREDEFINED_TRANSFER.getAccountFrom())),
                hasProperty("accountTo", is(TestData.PREDEFINED_TRANSFER.getAccountTo())),
                hasProperty("amount", comparesEqualTo(TestData.PREDEFINED_TRANSFER.getAmount()))));
    }

    @Test
    @ExpectedDatabase(value = Datasets.AFTER_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateTransfer() {
        transferService.createTransfer(TestData.ADDED_TRANSFER);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @ExpectedDatabase(value = Datasets.ONE_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateTransferWithIllegalAmountShouldThrowException() {
        Transfer transfer = new Transfer(TestData.PREDEFINED_ACCOUNT_1, TestData.PREDEFINED_ACCOUNT_2,
                Date.from(Instant.parse("2015-09-01T00:00:00Z")),
                TestData.PREDEFINED_ACCOUNT_1.getAmount().multiply(new BigDecimal(2)));
        transferService.createTransfer(transfer);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @ExpectedDatabase(value = Datasets.ONE_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateTransferBetweenTheSameAccountShouldThrowException() {
        Transfer transfer = new Transfer(TestData.PREDEFINED_ACCOUNT_1, TestData.PREDEFINED_ACCOUNT_1,
                Date.from(Instant.parse("2015-09-01T00:00:00Z")),
                TestData.PREDEFINED_ACCOUNT_1.getAmount().divide(new BigDecimal(2)));
        transferService.createTransfer(transfer);
    }

    @Test(expected = IllegalArgumentException.class)
    @ExpectedDatabase(value = Datasets.ONE_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateTransferWithOnlyOneAccountShouldThrowException() {
        Transfer transfer = new Transfer(null, TestData.PREDEFINED_ACCOUNT_1,
                Date.from(Instant.parse("2015-09-01T00:00:00Z")),
                TestData.PREDEFINED_ACCOUNT_1.getAmount().divide(new BigDecimal(2)));
        transferService.createTransfer(transfer);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @ExpectedDatabase(value = Datasets.ONE_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateTransferDuplicateShouldThrowException() {
        Transfer transfer = new Transfer(
                TestData.PREDEFINED_TRANSFER.getAccountFrom(),
                TestData.PREDEFINED_TRANSFER.getAccountTo(),
                TestData.PREDEFINED_TRANSFER.getDate(),
                TestData.PREDEFINED_TRANSFER.getAmount());
        transferService.createTransfer(transfer);
    }

    @Test
    @ExpectedDatabase(value = Datasets.AFTER_CREDIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreditAccount() {
        transferService.creditAccount(TestData.ADDED_CREDIT);
    }

    @Test(expected = EntityNotFoundException.class)
    @ExpectedDatabase(value = Datasets.ONE_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreditAccountWithNotExistedAccountShouldThrowException() {
        Transfer transfer = new Transfer(null, new Account(Integer.MAX_VALUE),
                Date.from(Instant.parse("2015-09-01T00:00:00Z")), new BigDecimal(500));
        transferService.creditAccount(transfer);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @ExpectedDatabase(value = Datasets.AFTER_CREDIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreditAccountWithDuplicateCreditShouldThrowException() {
        transferService.creditAccount(TestData.ADDED_CREDIT);
        transferService.creditAccount(TestData.ADDED_CREDIT);
    }

    @Test(expected = IllegalArgumentException.class)
    @ExpectedDatabase(value = Datasets.ONE_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreditAccountWithSetSourceAccountShouldThrowException() {
        Transfer transfer = new Transfer(TestData.PREDEFINED_ACCOUNT_1, TestData.PREDEFINED_ACCOUNT_2,
                Date.from(Instant.parse("2015-09-01T00:00:00Z")), new BigDecimal(500));
        transferService.creditAccount(transfer);
    }

    @Test
    @ExpectedDatabase(value = Datasets.AFTER_DEBIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testDebitAccount() {
        transferService.debitAccount(TestData.ADDED_DEBIT);
    }

    @Test(expected = EntityNotFoundException.class)
    @ExpectedDatabase(value = Datasets.ONE_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testDebitAccountWithNotExistedAccountShouldThrowException() {
        Transfer transfer = new Transfer(new Account(Integer.MAX_VALUE), null,
                Date.from(Instant.parse("2015-09-01T00:00:00Z")), new BigDecimal(500));
        transferService.debitAccount(transfer);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @ExpectedDatabase(value = Datasets.AFTER_DEBIT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testDebitAccountWithDuplicateCreditShouldThrowException() {
        transferService.debitAccount(TestData.ADDED_DEBIT);
        transferService.debitAccount(TestData.ADDED_DEBIT);
    }
    @Test(expected = IllegalArgumentException.class)
    @ExpectedDatabase(value = Datasets.ONE_TRANSFER, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testDebitAccountWithSetTargetAccountShouldThrowException() {
        Transfer transfer = new Transfer(TestData.PREDEFINED_ACCOUNT_1, TestData.PREDEFINED_ACCOUNT_2,
                Date.from(Instant.parse("2015-09-01T00:00:00Z")), new BigDecimal(500));
        transferService.creditAccount(transfer);
    }


    private static final class Datasets {
        private static final String ONE_TRANSFER = "/datasets/transfer-service-test/one-transfer.xml";
        private static final String AFTER_TRANSFER = "/datasets/transfer-service-test/after-transfer.xml";
        private static final String AFTER_CREDIT = "/datasets/transfer-service-test/after-credit.xml";
        private static final String AFTER_DEBIT = "/datasets/transfer-service-test/after-debit.xml";
    }
}




















