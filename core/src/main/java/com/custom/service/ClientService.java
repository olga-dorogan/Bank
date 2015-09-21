package com.custom.service;

import com.custom.model.Account;
import com.custom.model.Client;

import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
public interface ClientService {
    List<Client> findAll();

    List<Account> findAllAccountsByClientId(int clientId);

    Client findClientWithAccounts(int clientId);

    void create(Client client);

    void createAccount(Account account);
}
