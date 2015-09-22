package com.custom.service.impl.spring;

import com.custom.dao.AccountDAO;
import com.custom.dao.TransactionDAO;
import com.custom.entity.Account;
import com.custom.entity.composite.TransactionFull;
import com.custom.exception.BankException;
import com.custom.model.Client;
import com.custom.model.Transaction;
import com.custom.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olga on 22.09.15.
 */
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionDAO transactionDAO;
    @Autowired
    private AccountDAO accountDAO;

    @Override
    public List<Transaction> findAll() {
        List<TransactionFull> transactionEntities = transactionDAO.findAllFull();
        List<Transaction> transactions = transactionEntities
                .stream()
                .map(entity -> {
                    Transaction transaction = new Transaction(
                            entity.transaction.getId(),
                            entity.transaction.getDate(),
                            entity.transaction.getAmount());
                    if (entity.clientFrom == null) {
                        transaction.setClientFrom(null);
                    } else {
                        transaction.setClientFrom(new Client(
                                entity.clientFrom.getId(),
                                entity.clientFrom.getName(),
                                entity.clientFrom.getSurname()
                        ));
                    }

                    if (entity.clientTo == null) {
                        transaction.setClientTo(null);
                    } else {
                        transaction.setClientTo(new Client(
                                entity.clientTo.getId(),
                                entity.clientTo.getName(),
                                entity.clientTo.getSurname()
                        ));
                    }
                    if (entity.accountFrom == null) {
                        transaction.setAccountFrom(null);
                    } else {
                        transaction.setAccountFrom(new com.custom.model.Account(
                                entity.accountFrom.getTitle()
                        ));
                    }
                    if (entity.accountTo == null) {
                        transaction.setAccountTo(null);
                    } else {
                        transaction.setAccountTo(new com.custom.model.Account(
                                entity.accountTo.getTitle()
                        ));
                    }
                    return transaction;
                })
                .collect(Collectors.toList());
        return transactions;
    }

    @Override
    @Transactional
    public void createTransaction(Transaction transaction) {
        create(transaction);
    }

    @Override
    @Transactional
    public void creditAccount(Transaction transaction) {
        create(transaction);
    }

    @Override
    @Transactional
    public void debitAccount(Transaction transaction) {
        create(transaction);
    }

    private void create(Transaction transaction) {
        if (transaction.getAccountFrom() != null) {
            Account accountFrom = accountDAO.findById(transaction.getAccountFrom().getId());
            if (accountFrom.getAmount().compareTo(transaction.getAmount()) < 0) {
                throw new BankException("Invalid transaction");
            }
            accountFrom.setAmount(accountFrom.getAmount().subtract(transaction.getAmount()));
            accountDAO.update(accountFrom);
        }
        if (transaction.getAccountTo() != null) {
            Account accountTo = accountDAO.findById(transaction.getAccountTo().getId());
            accountTo.setAmount(accountTo.getAmount().add(transaction.getAmount()));
            accountDAO.update(accountTo);
        }
        com.custom.entity.Transaction entityTransaction = new com.custom.entity.Transaction();
        //      if accountFrom is null operation is credit
        // else if accountTo is null   operation is debit
        // else                        operation is transfer between accounts
        if (transaction.getAccountFrom() == null) {
            entityTransaction.setIdAccountTo(transaction.getAccountTo().getId());
            entityTransaction.setAmount(transaction.getAmount());
            entityTransaction.setDate(transaction.getDate());
            transactionDAO.createCredit(entityTransaction);
        } else if (transaction.getAccountTo() == null) {
            entityTransaction.setIdAccountFrom(transaction.getAccountFrom().getId());
            entityTransaction.setAmount(transaction.getAmount());
            entityTransaction.setDate(transaction.getDate());
            transactionDAO.createDebit(entityTransaction);
        } else {
            if (transaction.getAccountFrom().equals(transaction.getAccountTo())) {
                throw new BankException("Invalid transaction: accounts must be different");
            }
            entityTransaction.setIdAccountFrom(transaction.getAccountFrom().getId());
            entityTransaction.setIdAccountTo(transaction.getAccountTo().getId());
            entityTransaction.setDate(transaction.getDate());
            entityTransaction.setAmount(transaction.getAmount());
            transactionDAO.createTransaction(entityTransaction);
        }
    }
}
