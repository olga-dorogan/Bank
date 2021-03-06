package com.custom.app.service.impl;

import com.custom.app.dao.AccountRepository;
import com.custom.app.dao.ClientRepository;
import com.custom.app.dto.Account;
import com.custom.app.dto.Client;
import com.custom.app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olga on 23.09.15.
 */
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<com.custom.app.dto.Client> findAll() {
        List<com.custom.app.entity.Client> clientEntities = clientRepository.findAll();
        return clientEntities
                .stream()
                .map(entity -> new Client(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getPassport()))
                .collect(Collectors.toList());
    }

    @Override
    public Client findById(int clientId) {
        com.custom.app.entity.Client clientEntity = clientRepository.findOne(clientId);
        return new Client(clientEntity.getId(), clientEntity.getFirstName(), clientEntity.getLastName(), clientEntity.getPassport());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAllAccountsByClientId(int clientId) {
        com.custom.app.entity.Client clientEntity = clientRepository.findOne(clientId);
        List<com.custom.app.entity.Account> accountEntities = clientEntity.getAccounts();
        return accountEntities
                .stream()
                .map(entity -> new Account(entity.getId(), entity.getTitle(), entity.getAmount()))
                .collect(Collectors.toList());
    }

    @Override
    public void create(Client client) {
        com.custom.app.entity.Client savedClient = clientRepository.saveAndFlush(
                new com.custom.app.entity.Client(client.getFirstName(), client.getLastName(), client.getPassport()));
        client.setId(savedClient.getId());
    }

    @Override
    @Transactional
    public void createAccount(Account account) {
        com.custom.app.entity.Account accountEntity = new com.custom.app.entity.Account(account.getTitle());
        accountEntity.setAmount(account.getAmount());
        accountEntity.setClient(clientRepository.findOne(account.getClient().getId()));
        accountRepository.save(accountEntity);
        account.setId(accountEntity.getId());
    }
}
