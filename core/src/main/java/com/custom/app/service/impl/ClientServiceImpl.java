package com.custom.app.service.impl;

import com.custom.app.dao.AccountRepository;
import com.custom.app.dao.ClientRepository;
import com.custom.app.entity.Client;
import com.custom.app.dto.Account;
import com.custom.app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olga on 23.09.15.
 */
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<com.custom.app.dto.Client> findAll() {
        List<Client> clientEntities = clientRepository.findAll();
        return clientEntities
                .stream()
                .map(entity -> new com.custom.app.dto.Client(entity.getId(), entity.getName(), entity.getSurname()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Account> findAllAccountsByClientId(int clientId) {
        Client clientEntity = clientRepository.findOne(clientId);
        List<com.custom.app.entity.Account> accountEntities = clientEntity.getAccounts();
        return accountEntities
                .stream()
                .map(entity -> new Account(entity.getId(), entity.getTitle(), entity.getAmount()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public com.custom.app.dto.Client findClientWithAccounts(int clientId) {
        Client clientEntity = clientRepository.findOne(clientId);
        com.custom.app.dto.Client client = new com.custom.app.dto.Client(clientId, clientEntity.getName(), clientEntity.getSurname());
        List<com.custom.app.entity.Account> accountEntities = clientEntity.getAccounts();
        List<Account> accounts = accountEntities
                .stream()
                .map(entity -> new Account(entity.getId(), entity.getTitle(), entity.getAmount()))
                .collect(Collectors.toList());
        client.setAccounts(accounts);
        return client;
    }

    @Override
    public void create(com.custom.app.dto.Client client) {
        Client savedClient = clientRepository.save(new Client(client.getName(), client.getSurname()));
        client.setId(savedClient.getId());
    }

    @Override
    public void createAccount(Account account) {
        com.custom.app.entity.Account accountEntity = new com.custom.app.entity.Account(account.getTitle());
        accountEntity.setAmount(account.getAmount());
        accountEntity.setClient(clientRepository.findOne(account.getClient().getId()));
        accountRepository.save(accountEntity);
    }
}
