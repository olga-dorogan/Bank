package com.custom.dao.impl.raw;

import com.custom.dao.AccountDAO;
import com.custom.dao.util.Query;
import com.custom.entity.Account;
import com.custom.entity.Client;
import com.custom.exception.BankDAOException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
public class AccountDAOImpl implements AccountDAO {
    @Autowired
    private DataSource dataSource;

    @Override
    public List<Account> findAllForClient(Client client) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prStmt = conn.prepareStatement(Query.Account.FIND_ACCOUNTS_FOR_CLIENT)) {
            prStmt.setInt(1, client.getId());
            try (ResultSet rs = prStmt.executeQuery()) {
                List<Account> accounts = new ArrayList<>();
                while (rs.next()) {
                    accounts.add(new Account(
                            rs.getInt(Query.Account.FIND_ALL_ID),
                            rs.getString(Query.Account.FIND_ALL_TITLE),
                            rs.getBigDecimal(Query.Account.FIND_ALL_AMOUNT)));
                }
                return accounts;
            }
        } catch (SQLException e) {
            throw new BankDAOException(e.getMessage(), e);
        }
    }

    @Override
    public Account findById(int id) {
        try (Connection conn = dataSource.getConnection()) {
            return findById(conn, id);
        } catch (SQLException e) {
            throw new BankDAOException(e.getMessage(), e);
        }
    }

    @Override
    public Account findById(Connection conn, int id) {
        try (PreparedStatement prStmt = conn.prepareStatement(Query.Account.FIND_BY_ID)) {
            prStmt.setInt(1, id);
            try (ResultSet rs = prStmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt(1), rs.getString(2), rs.getBigDecimal(3));
                }
                throw new BankDAOException(String.format("Account with id == %d doesn't exist", id));
            }
        } catch (SQLException e) {
            throw new BankDAOException(e.getMessage(), e);
        }
    }

    @Override
    public void create(Account account, Client client) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prStmt = conn.prepareStatement(Query.Account.INSERT_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {
            prStmt.setInt(1, client.getId());
            prStmt.setString(2, account.getTitle());
            prStmt.setBigDecimal(3, account.getAmount());
            if (prStmt.executeUpdate() != 1) {
                throw new BankDAOException("Error with creation " + account);
            }
            try (ResultSet rsGeneratedKeys = prStmt.getGeneratedKeys()) {
                if (rsGeneratedKeys.next()) {
                    account.setId(rsGeneratedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new BankDAOException(e.getMessage(), e);
        }
    }

    @Override
    public void update(Connection conn, Account account) {
        try (PreparedStatement prStmt = conn.prepareStatement(Query.Account.UPDATE_ACCOUNT)) {
            prStmt.setBigDecimal(1, account.getAmount());
            prStmt.setInt(2, account.getId());
            if (prStmt.executeUpdate() != 1) {
                throw new BankDAOException("Error with updating " + account);
            }
        } catch (SQLException e) {
            throw new BankDAOException(e.getMessage(), e);
        }
    }

    @Override
    public void update(Account account) {
        throw new UnsupportedOperationException();
    }
}
