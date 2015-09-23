package com.custom.service.impl;

import com.custom.dao.AccountRepository;
import com.custom.dao.TransactionRepository;
import com.custom.model.Account;
import com.custom.model.Client;
import com.custom.model.Transaction;
import com.custom.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olga on 23.09.15.
 */
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public List<Transaction> findAll() {
        List<com.custom.entity.Transaction> transactionEntities = transactionRepository.findAll();
        return transactionEntities
                .stream()
                .map(entity -> {
                    Transaction transaction = new Transaction(entity.getId(), entity.getDate(), entity.getAmount());
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
                        transaction.setAccountFrom(accountFrom);
                        transaction.setClientFrom(accountFrom.getClient());
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
                        transaction.setAccountTo(accountTo);
                        transaction.setClientTo(accountTo.getClient());
                    }
                    return transaction;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createTransaction(Transaction transaction) {
        com.custom.entity.Account accountFrom = accountRepository.findOne(transaction.getAccountFrom().getId());
        accountFrom.setAmount(accountFrom.getAmount().subtract(transaction.getAmount()));
        accountRepository.save(accountFrom);
        com.custom.entity.Account accountTo = accountRepository.findOne(transaction.getAccountTo().getId());
        accountTo.setAmount(accountTo.getAmount().add(transaction.getAmount()));
        accountRepository.save(accountTo);
        transactionRepository.save(new com.custom.entity.Transaction(
                accountFrom,
                accountTo,
                transaction.getDate(),
                transaction.getAmount()
        ));
    }

    @Override
    @Transactional
    public void creditAccount(Transaction transaction) {
        com.custom.entity.Account accountTo = accountRepository.findOne(transaction.getAccountTo().getId());
        accountTo.setAmount(accountTo.getAmount().add(transaction.getAmount()));
        accountRepository.save(accountTo);
        transactionRepository.save(new com.custom.entity.Transaction(
                null,
                accountTo,
                transaction.getDate(),
                transaction.getAmount()
        ));
    }

    @Override
    @Transactional
    public void debitAccount(Transaction transaction) {
        com.custom.entity.Account accountFrom = accountRepository.findOne(transaction.getAccountFrom().getId());
        accountFrom.setAmount(accountFrom.getAmount().subtract(transaction.getAmount()));
        accountRepository.save(accountFrom);
        transactionRepository.save(new com.custom.entity.Transaction(
                accountFrom,
                null,
                transaction.getDate(),
                transaction.getAmount()
        ));
    }
}
