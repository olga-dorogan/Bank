package com.custom.config;

import com.custom.dao.AccountDAO;
import com.custom.dao.ClientDAO;
import com.custom.dao.TransactionDAO;
import com.custom.dao.impl.AccountDAOImpl;
import com.custom.dao.impl.ClientDAOImpl;
import com.custom.dao.impl.TransactionDAOImpl;
import com.custom.service.ClientService;
import com.custom.service.TransactionService;
import com.custom.service.impl.ClientServiceImpl;
import com.custom.service.impl.TransactionServiceImpl;

/**
 * Created by olga on 17.09.15.
 */
public class IoC {
    public ClientDAO getClientDAO() {
        return new ClientDAOImpl();
    }

    public AccountDAO getAccountDAO() {
        return new AccountDAOImpl();
    }

    public TransactionDAO getTransactionDAO() {
        return new TransactionDAOImpl();
    }

    public ClientService getClientService() {
        return new ClientServiceImpl();
    }
    public TransactionService getTransactionService() {
        return new TransactionServiceImpl();
    }

}
