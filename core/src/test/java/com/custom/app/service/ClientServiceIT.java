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
    private static final Client PREDEFINED_CLIENT = new Client(-1, "Ivan", "Ivanov", "ЕР123456");
    private static final Client ADDED_CLIENT = new Client(1, "firstName", "lastName", "ЕР000000");
    private static final Account PREDEFINED_ACCOUNT = new Account(-1, "test_account", new BigDecimal(1000.00));

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
                hasProperty("id", is(PREDEFINED_CLIENT.getId())),
                hasProperty("firstName", is(PREDEFINED_CLIENT.getFirstName())),
                hasProperty("lastName", is(PREDEFINED_CLIENT.getLastName())),
                hasProperty("passport", is(PREDEFINED_CLIENT.getPassport()))));
    }

    @Test
    @ExpectedDatabase(value = Datasets.AFTER_ADDED_CLIENT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreate() {
        clientService.create(ADDED_CLIENT);
    }

    @Test(expected = PersistenceException.class)
    @ExpectedDatabase(value = Datasets.ONE_CLIENT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateClientWithDuplicatePassportShouldThrowException() {
        Client client = new Client("someName", "someSurname", PREDEFINED_CLIENT.getPassport());
        clientService.create(client);
    }

    @Test(expected = PersistenceException.class)
    @ExpectedDatabase(value = Datasets.ONE_CLIENT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateClientWithoutFirstNamePassportShouldThrowException() {
        Client client = new Client();
        client.setLastName(ADDED_CLIENT.getLastName());
        client.setPassport(ADDED_CLIENT.getPassport());
        clientService.create(client);
    }

    @Test(expected = PersistenceException.class)
    @ExpectedDatabase(value = Datasets.ONE_CLIENT, assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void testCreateClientWithoutLastNamePassportShouldThrowException() {
        Client client = new Client();
        client.setFirstName(ADDED_CLIENT.getFirstName());
        client.setPassport(ADDED_CLIENT.getPassport());
        clientService.create(client);
    }

    @Test
    public void testFindAllAccountsByClientId() {
        List<Account> accounts = clientService.findAllAccountsByClientId(PREDEFINED_CLIENT.getId());
        assertThat(accounts.size(), is(1));
        assertThat(accounts.get(0), allOf(
                hasProperty("id", is(PREDEFINED_ACCOUNT.getId())),
                hasProperty("title", is(PREDEFINED_ACCOUNT.getTitle())),
                hasProperty("amount", comparesEqualTo(PREDEFINED_ACCOUNT.getAmount()))));
    }

    private static final class Datasets {
        private static final String ONE_CLIENT = "/datasets/client-service-test/one-client.xml";
        private static final String AFTER_ADDED_CLIENT = "/datasets/client-service-test/added-client.xml";
    }
}