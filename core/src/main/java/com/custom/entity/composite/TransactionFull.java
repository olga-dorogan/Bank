package com.custom.entity.composite;

import com.custom.entity.Account;
import com.custom.entity.Client;
import com.custom.entity.Transaction;

/**
 * Created by olga on 19.09.15.
 */
public class TransactionFull {
    public final Transaction transaction;
    public final Client clientFrom;
    public final Client clientTo;
    public final Account accountFrom;
    public final Account accountTo;


    public TransactionFull(Transaction transaction, Client clientFrom, Client clientTo, Account accountFrom, Account accountTo) {
        this.transaction = transaction;
        this.clientFrom = clientFrom;
        this.clientTo = clientTo;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    @Override
    public String toString() {
        return "TransactionFull{" +
                "transaction=" + transaction +
                ", clientFrom=" + clientFrom +
                ", clientTo=" + clientTo +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                '}';
    }
}
