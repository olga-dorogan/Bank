package com.custom.app.service;

import com.custom.app.dto.Account;
import com.custom.app.dto.Client;
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

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;

/**
 * Created by olga on 27.09.15.
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
        "/datasets/client-service-test/one-account.xml"})
public class ClientServiceIT {


    @Autowired
    private ClientService clientService;

    @Test
    public void testClientServiceIsInjected() {
        assertNotNull(clientService);
    }

    @Test
    public void testFindAll() {
        List<Client> clients = clientService.findAll();
        assertThat(clients.size(), is(1));
        assertThat(clients.get(0), allOf(
                hasProperty("id", is(TestData.PREDEFINED_CLIENT.getId())),
                hasProperty("firstName", is(TestData.PREDEFINED_CLIENT.getFirstName())),
                hasProperty("lastName", is(TestData.PREDEFINED_CLIENT.getLastName())),
                hasProperty("passport", is(TestData.PREDEFINED_CLIENT.getPassport()))));
    }

    @Test
    @ExpectedDatabase(value = Datasets.AFTER_ADDED_CLIENT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreate() {
        clientService.create(TestData.ADDED_CLIENT);
    }

    @Test(expected = PersistenceException.class)
    @ExpectedDatabase(value = Datasets.ONE_CLIENT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateClientWithDuplicatePassportShouldThrowException() {
        Client client = new Client("someName", "someSurname", TestData.PREDEFINED_CLIENT.getPassport());
        clientService.create(client);
    }

    @Test(expected = PersistenceException.class)
    @ExpectedDatabase(value = Datasets.ONE_CLIENT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateClientWithoutFirstNamePassportShouldThrowException() {
        Client client = new Client();
        client.setLastName(TestData.ADDED_CLIENT.getLastName());
        client.setPassport(TestData.ADDED_CLIENT.getPassport());
        clientService.create(client);
    }

    @Test(expected = PersistenceException.class)
    @ExpectedDatabase(value = Datasets.ONE_CLIENT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateClientWithoutLastNamePassportShouldThrowException() {
        Client client = new Client();
        client.setFirstName(TestData.ADDED_CLIENT.getFirstName());
        client.setPassport(TestData.ADDED_CLIENT.getPassport());
        clientService.create(client);
    }

    @Test
    public void testFindAllAccountsByClientId() {
        List<Account> accounts = clientService.findAllAccountsByClientId(TestData.PREDEFINED_CLIENT.getId());
        assertThat(accounts.size(), is(1));
        assertThat(accounts.get(0), allOf(
                hasProperty("id", is(TestData.PREDEFINED_ACCOUNT.getId())),
                hasProperty("title", is(TestData.PREDEFINED_ACCOUNT.getTitle())),
                hasProperty("amount", comparesEqualTo(TestData.PREDEFINED_ACCOUNT.getAmount()))));
    }

    @Test
    @ExpectedDatabase(value = Datasets.AFTER_ADDED_ACCOUNT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateAccount() {
        clientService.createAccount(TestData.ADDED_ACCOUNT);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @ExpectedDatabase(value = Datasets.ONE_ACCOUNT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateAccountWithDuplicateTitleShouldThrowException() {
        Account account = new Account(TestData.PREDEFINED_ACCOUNT.getTitle(), new BigDecimal(3000), TestData.PREDEFINED_CLIENT);
        clientService.createAccount(account);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @ExpectedDatabase(value = Datasets.ONE_ACCOUNT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateAccountWithNegativeAmountShouldThrowException() {
        Account account = new Account("bad account", new BigDecimal(-100), TestData.PREDEFINED_CLIENT);
        clientService.createAccount(account);
    }

    @Test(expected = DataIntegrityViolationException.class)
    @ExpectedDatabase(value = Datasets.ONE_ACCOUNT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateAccountWithoutTitleShouldThrowException() {
        Account account = new Account();
        account.setAmount(new BigDecimal(1000));
        account.setClient(TestData.PREDEFINED_CLIENT);
        clientService.createAccount(account);
    }

    private static final class Datasets {
        private static final String ONE_CLIENT = "/datasets/client-service-test/one-client.xml";
        private static final String ONE_ACCOUNT = "/datasets/client-service-test/one-account.xml";
        private static final String AFTER_ADDED_CLIENT = "/datasets/client-service-test/added-client.xml";
        private static final String AFTER_ADDED_ACCOUNT = "/datasets/client-service-test/added-account.xml";
    }
}