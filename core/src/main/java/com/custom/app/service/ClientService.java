package com.custom.app.service;

import com.custom.app.dto.Account;
import com.custom.app.dto.Client;

import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
public interface ClientService {
    List<Client> findAll();

    List<Account> findAllAccountsByClientId(int clientId);

    void create(Client client);

    void createAccount(Account account);
}
