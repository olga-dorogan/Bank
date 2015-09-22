package com.custom.service.impl;

import com.custom.dao.AccountDAO;
import com.custom.dao.ClientDAO;
import com.custom.model.Account;
import com.custom.model.Client;
import com.custom.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olga on 17.09.15.
 */
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientDAO clientDAO;
    @Autowired
    private AccountDAO accountDAO;

    public ClientServiceImpl() {
    }

    @Override
    public List<Client> findAll() {
        List<com.custom.entity.Client> clientEntities = clientDAO.findAll();
        List<Client> clientModels = clientEntities
                .stream()
                .map(clientEntity -> new Client(clientEntity.getId(), clientEntity.getName(), clientEntity.getSurname()))
                .collect(Collectors.toList());
        return clientModels;
    }

    @Override
    public List<Account> findAllAccountsByClientId(int clientId) {
        com.custom.entity.Client client = new com.custom.entity.Client();
        client.setId(clientId);
        List<com.custom.entity.Account> clientAccountEntities = accountDAO.findAllForClient(client);
        List<Account> clientAccounts = clientAccountEntities
                .stream()
                .map(accountEntity -> new Account(accountEntity.getId(), accountEntity.getTitle(), accountEntity.getAmount()))
                .collect(Collectors.toList());
        return clientAccounts;
    }

    @Override
    public Client findClientWithAccounts(int clientId) {
        com.custom.entity.Client entityClient = clientDAO.findById(clientId);
        Client client = new Client(clientId, entityClient.getName(), entityClient.getSurname());
        List<Account> clientAccounts = findAllAccountsByClientId(clientId);
        client.setAccounts(clientAccounts);
        return client;
    }

    @Override
    public void create(Client client) {
        com.custom.entity.Client entityClient = new com.custom.entity.Client(client.getName(), client.getSurname());
        clientDAO.create(entityClient);
    }

    @Override
    public void createAccount(Account account) {
        com.custom.entity.Account entityAccount = new com.custom.entity.Account(account.getId(), account.getTitle(), account.getAmount());
        com.custom.entity.Client entityClient = new com.custom.entity.Client();
        entityClient.setId(account.getClient().getId());
        accountDAO.create(entityAccount, entityClient);
    }
}
