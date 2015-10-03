package com.custom.app.service.impl;

import com.custom.app.dao.AccountRepository;
import com.custom.app.dao.TransferRepository;
import com.custom.app.dto.Account;
import com.custom.app.dto.Client;
import com.custom.app.dto.Transfer;
import com.custom.app.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olga on 23.09.15.
 */
@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Transfer> findAll() {
        List<com.custom.app.entity.Transfer> transferEntities = transferRepository.findAll();
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
                                entity.getAccountFrom().getClient().getFirstName(),
                                entity.getAccountFrom().getClient().getLastName()
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
                                entity.getAccountTo().getClient().getFirstName(),
                                entity.getAccountTo().getClient().getLastName()
                        ));
                        transfer.setAccountTo(accountTo);
                        transfer.setClientTo(accountTo.getClient());
                    }
                    return transfer;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void createTransfer(Transfer transfer) {
        if (transfer.getAccountFrom() == null || transfer.getAccountTo() == null) {
            throw new IllegalArgumentException("Both accounts must be set");
        }
        com.custom.app.entity.Account accountFrom = accountRepository.findOne(transfer.getAccountFrom().getId());
        accountFrom.setAmount(accountFrom.getAmount().subtract(transfer.getAmount()));
        accountRepository.save(accountFrom);
        com.custom.app.entity.Account accountTo = accountRepository.findOne(transfer.getAccountTo().getId());
        accountTo.setAmount(accountTo.getAmount().add(transfer.getAmount()));
        accountRepository.save(accountTo);
        transferRepository.save(new com.custom.app.entity.Transfer(
                accountFrom,
                accountTo,
                transfer.getDate(),
                transfer.getAmount()
        ));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void creditAccount(Transfer transfer) {
        if (transfer.getAccountFrom() != null) {
            throw new IllegalArgumentException("Credit operation must not have source account");
        }
        com.custom.app.entity.Account accountTo = accountRepository.findOne(transfer.getAccountTo().getId());
        if (accountTo == null) {
            throw new EntityNotFoundException(String.format("Account with id = %d was not found", transfer.getAccountTo().getId()));
        }
        accountTo.setAmount(accountTo.getAmount().add(transfer.getAmount()));
        accountRepository.save(accountTo);
        transferRepository.save(new com.custom.app.entity.Transfer(
                null,
                accountTo,
                transfer.getDate(),
                transfer.getAmount()
        ));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void debitAccount(Transfer transfer) {
        if (transfer.getAccountTo() != null) {
            throw new IllegalArgumentException("Debit operation must not have target account");
        }
        com.custom.app.entity.Account accountFrom = accountRepository.findOne(transfer.getAccountFrom().getId());
        if (accountFrom == null) {
            throw new EntityNotFoundException(String.format("Account with id = %d was not found", transfer.getAccountFrom().getId()));
        }
        accountFrom.setAmount(accountFrom.getAmount().subtract(transfer.getAmount()));
        accountRepository.save(accountFrom);
        transferRepository.save(new com.custom.app.entity.Transfer(
                accountFrom,
                null,
                transfer.getDate(),
                transfer.getAmount()
        ));
    }
}
