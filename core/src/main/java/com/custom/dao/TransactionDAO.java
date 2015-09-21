package com.custom.dao;

import com.custom.entity.Transaction;
import com.custom.entity.composite.TransactionFull;

import java.sql.Connection;
import java.util.List;

/**
 * Created by olga on 18.09.15.
 */
public interface TransactionDAO {
    List<Transaction> findAll();

    List<TransactionFull> findAllFull();

    void createTransaction(Connection conn, Transaction transaction);

    void createCredit(Connection conn, Transaction transaction);

    void createDebit(Connection conn, Transaction transaction);
}
