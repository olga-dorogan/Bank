package com.custom.service.impl;

import com.custom.dao.AccountDAO;
import com.custom.dao.TransactionDAO;
import com.custom.entity.Account;
import com.custom.entity.composite.TransactionFull;
import com.custom.exception.BankException;
import com.custom.model.Client;
import com.custom.model.Transaction;
import com.custom.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by olga on 18.09.15.
 */
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionDAO transactionDAO;
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private DataSource dataSource;

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
    public void createTransaction(Transaction transaction) {
        create(transaction);
    }

    @Override
    public void creditAccount(Transaction transaction) {
        create(transaction);
    }

    @Override
    public void debitAccount(Transaction transaction) {
        create(transaction);
    }

    private void create(Transaction transaction) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);

                if (transaction.getAccountFrom() != null) {
                    Account accountFrom = accountDAO.findById(conn, transaction.getAccountFrom().getId());
                    if (accountFrom.getAmount().compareTo(transaction.getAmount()) < 0) {
                        throw new BankException("Invalid transaction");
                    }
                    accountFrom.setAmount(accountFrom.getAmount().subtract(transaction.getAmount()));
                    accountDAO.update(conn, accountFrom);
                }
                if (transaction.getAccountTo() != null) {
                    Account accountTo = accountDAO.findById(conn, transaction.getAccountTo().getId());
                    accountTo.setAmount(accountTo.getAmount().add(transaction.getAmount()));
                    accountDAO.update(conn, accountTo);
                }
                com.custom.entity.Transaction entityTransaction = new com.custom.entity.Transaction();
                //      if accountFrom is null operation is credit
                // else if accountTo is null   operation is debit
                // else                        operation is transfer between accounts
                if (transaction.getAccountFrom() == null) {
                    entityTransaction.setIdAccountTo(transaction.getAccountTo().getId());
                    entityTransaction.setAmount(transaction.getAmount());
                    entityTransaction.setDate(transaction.getDate());
                    transactionDAO.createCredit(conn, entityTransaction);
                } else if (transaction.getAccountTo() == null) {
                    entityTransaction.setIdAccountFrom(transaction.getAccountFrom().getId());
                    entityTransaction.setAmount(transaction.getAmount());
                    entityTransaction.setDate(transaction.getDate());
                    transactionDAO.createDebit(conn, entityTransaction);
                } else {
                    if (transaction.getAccountFrom().equals(transaction.getAccountTo())) {
                        throw new BankException("Invalid transaction: accounts must be different");
                    }
                    entityTransaction.setIdAccountFrom(transaction.getAccountFrom().getId());
                    entityTransaction.setIdAccountTo(transaction.getAccountTo().getId());
                    entityTransaction.setDate(transaction.getDate());
                    entityTransaction.setAmount(transaction.getAmount());
                    transactionDAO.createTransaction(conn, entityTransaction);
                }
                transaction.setId(entityTransaction.getId());
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new BankException(e.getMessage(), e);
        }
    }
}
