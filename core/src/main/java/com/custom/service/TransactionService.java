package com.custom.service;

import com.custom.model.Transaction;

import java.util.List;

/**
 * Created by olga on 18.09.15.
 */
public interface TransactionService {
    List<Transaction> findAll();

    void createTransaction(Transaction transaction);

    void creditAccount(Transaction transaction);

    void debitAccount(Transaction transaction);
}
