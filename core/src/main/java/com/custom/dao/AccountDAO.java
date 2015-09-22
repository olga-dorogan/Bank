package com.custom.dao;

import com.custom.entity.Account;
import com.custom.entity.Client;

import java.sql.Connection;
import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
public interface AccountDAO {
    List<Account> findAllForClient(Client client);

    Account findById(int id);

    Account findById(Connection conn, int id);

    void create(Account account, Client client);

    void update(Connection conn, Account account);

    void update(Account account);
}
