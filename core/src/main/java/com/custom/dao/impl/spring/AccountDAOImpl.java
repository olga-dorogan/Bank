package com.custom.dao.impl.spring;

import com.custom.dao.AccountDAO;
import com.custom.dao.util.Query;
import com.custom.entity.Account;
import com.custom.entity.Client;
import com.custom.exception.BankDAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by olga on 22.09.15.
 */
public class AccountDAOImpl implements AccountDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public List<Account> findAllForClient(Client client) {
        return jdbcTemplate.query(
                Query.Account.FIND_ACCOUNTS_FOR_CLIENT,
                new Object[]{client.getId()},
                new AccountMapper());
    }

    @Override
    public Account findById(int id) {
        return jdbcTemplate.queryForObject(
                Query.Account.FIND_BY_ID,
                new Object[]{id},
                (rs, i) -> {
                    return new Account(
                            rs.getInt(Query.Account.FIND_BY_ID_ID),
                            rs.getString(Query.Account.FIND_BY_ID_TITLE),
                            rs.getBigDecimal(Query.Account.FIND_BY_ID_AMOUNT));
                });
    }

    @Override
    public Account findById(Connection conn, int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void create(Account account, Client client) {
        int updatedCnt = jdbcTemplate.update(
                Query.Account.INSERT_ACCOUNT,
                client.getId(), account.getTitle(), account.getAmount());
        if (updatedCnt != 1) {
            throw new BankDAOException(account.toString() + "is not saved");
        }
    }

    @Override
    public void update(Connection conn, Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Account account) {
        int updatedCnt = jdbcTemplate.update(
                Query.Account.UPDATE_ACCOUNT,
                account.getAmount(), account.getId());
        if (updatedCnt != 1) {
            throw new BankDAOException(account.toString() + "is not updated");
        }
    }

    private static class AccountMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int i) throws SQLException {
            return new Account(
                    rs.getInt(Query.Account.FIND_ALL_ID),
                    rs.getString(Query.Account.FIND_ALL_TITLE),
                    rs.getBigDecimal(Query.Account.FIND_ALL_AMOUNT));
        }
    }
}
