package com.custom.service.impl;

import com.custom.dao.AccountRepository;
import com.custom.dao.ClientRepository;
import com.custom.entity.Client;
import com.custom.model.Account;
import com.custom.service.ClientService;
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
    public List<com.custom.model.Client> findAll() {
        List<Client> clientEntities = clientRepository.findAll();
        return clientEntities
                .stream()
                .map(entity -> new com.custom.model.Client(entity.getId(), entity.getName(), entity.getSurname()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> findAllAccountsByClientId(int clientId) {
        Client clientEntity = clientRepository.findOne(clientId);
        List<com.custom.entity.Account> accountEntities = clientEntity.getAccounts();
        return accountEntities
                .stream()
                .map(entity -> new Account(entity.getId(), entity.getTitle(), entity.getAmount()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public com.custom.model.Client findClientWithAccounts(int clientId) {
        Client clientEntity = clientRepository.findOne(clientId);
        com.custom.model.Client client = new com.custom.model.Client(clientId, clientEntity.getName(), clientEntity.getSurname());
        List<com.custom.entity.Account> accountEntities = clientEntity.getAccounts();
        List<Account> accounts = accountEntities
                .stream()
                .map(entity -> new Account(entity.getId(), entity.getTitle(), entity.getAmount()))
                .collect(Collectors.toList());
        client.setAccounts(accounts);
        return client;
    }

    @Override
    public void create(com.custom.model.Client client) {
        Client savedClient = clientRepository.save(new Client(client.getName(), client.getSurname()));
        client.setId(savedClient.getId());
    }

    @Override
    public void createAccount(Account account) {
        com.custom.entity.Account accountEntity = new com.custom.entity.Account(account.getTitle());
        accountEntity.setAmount(account.getAmount());
        accountEntity.setClient(new Client(account.getClient().getId()));
        accountRepository.save(accountEntity);
    }
}
