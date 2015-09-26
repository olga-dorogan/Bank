package com.custom.app.service.impl;

import com.custom.app.dao.AccountRepository;
import com.custom.app.dao.TransferRepository;
import com.custom.app.dto.Account;
import com.custom.app.dto.Client;
import com.custom.app.dto.Transfer;
import com.custom.app.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olga on 23.09.15.
 */
public class TransferServiceImpl implements TransferService {
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public List<Transfer> findAll() {
        List<com.custom.app.model.Transfer> transferEntities = transferRepository.findAll();
        return transferEntities
                .stream()
                .map(entity -> {
                    Transfer transfer = new Transfer(entity.getId(), entity.getDate(), entity.getAmount());
                    if (entity.getAccountFrom() != null) {
                        Account accountFrom = new Account(
                                entity.getAccountFrom().getId(),
                                entity.getAccountFrom().getTitle(),
                                entity.getAccountFrom().getAmount());
                        accountFrom.setClient(new Client(
                                entity.getAccountFrom().getClient().getId(),
                                entity.getAccountFrom().getClient().getName(),
                                entity.getAccountFrom().getClient().getSurname()
                        ));
                        transfer.setAccountFrom(accountFrom);
                        transfer.setClientFrom(accountFrom.getClient());
                    }
                    if (entity.getAccountTo() != null) {
                        Account accountTo = new Account(
                                entity.getAccountTo().getId(),
                                entity.getAccountTo().getTitle(),
                                entity.getAccountTo().getAmount());
                        accountTo.setClient(new Client(
                                entity.getAccountTo().getClient().getId(),
                                entity.getAccountTo().getClient().getName(),
                                entity.getAccountTo().getClient().getSurname()
                        ));
                        transfer.setAccountTo(accountTo);
                        transfer.setClientTo(accountTo.getClient());
                    }
                    return transfer;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createTransaction(Transfer transfer) {
        com.custom.app.model.Account accountFrom = accountRepository.findOne(transfer.getAccountFrom().getId());
        accountFrom.setAmount(accountFrom.getAmount().subtract(transfer.getAmount()));
        accountRepository.save(accountFrom);
        com.custom.app.model.Account accountTo = accountRepository.findOne(transfer.getAccountTo().getId());
        accountTo.setAmount(accountTo.getAmount().add(transfer.getAmount()));
        accountRepository.save(accountTo);
        transferRepository.save(new com.custom.app.model.Transfer(
                accountFrom,
                accountTo,
                transfer.getDate(),
                transfer.getAmount()
        ));
    }

    @Override
    @Transactional
    public void creditAccount(Transfer transfer) {
        com.custom.app.model.Account accountTo = accountRepository.findOne(transfer.getAccountTo().getId());
        accountTo.setAmount(accountTo.getAmount().add(transfer.getAmount()));
        accountRepository.save(accountTo);
        transferRepository.save(new com.custom.app.model.Transfer(
                null,
                accountTo,
                transfer.getDate(),
                transfer.getAmount()
        ));
    }

    @Override
    @Transactional
    public void debitAccount(Transfer transfer) {
        com.custom.app.model.Account accountFrom = accountRepository.findOne(transfer.getAccountFrom().getId());
        accountFrom.setAmount(accountFrom.getAmount().subtract(transfer.getAmount()));
        accountRepository.save(accountFrom);
        transferRepository.save(new com.custom.app.model.Transfer(
                accountFrom,
                null,
                transfer.getDate(),
                transfer.getAmount()
        ));
    }
}
