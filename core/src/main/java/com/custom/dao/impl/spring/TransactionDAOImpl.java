package com.custom.dao.impl.spring;

import com.custom.dao.TransactionDAO;
import com.custom.dao.util.Query;
import com.custom.entity.Account;
import com.custom.entity.Client;
import com.custom.entity.Transaction;
import com.custom.entity.composite.TransactionFull;
import com.custom.exception.BankDAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 22.09.15.
 */
public class TransactionDAOImpl implements TransactionDAO {
    private enum TYPE {TRANSACTION, CREDIT, DEBIT}

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Transaction> findAll() {
        return jdbcTemplate.query(Query.Transaction.FIND_ALL, (rs, i) -> {
            return new Transaction(
                    rs.getInt(Query.Transaction.FIND_BY_ID_ID),
                    rs.getInt(Query.Transaction.FIND_BY_ID_ACCOUNT_FROM),
                    rs.getInt(Query.Transaction.FIND_BY_ID_ACCOUNT_TO),
                    rs.getTimestamp(Query.Transaction.FIND_BY_ID_DATE),
                    rs.getBigDecimal(Query.Transaction.FIND_BY_ID_AMOUNT));
        });
    }

    @Override
    public List<TransactionFull> findAllFull() {
        return jdbcTemplate.query(Query.Transaction.FIND_ALL_FULL, new TransactionMapper());
    }


    @Override
    public void createTransaction(Connection conn, Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createCredit(Connection conn, Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createDebit(Connection conn, Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createCredit(Transaction transaction) {
        create(transaction, TYPE.CREDIT);
    }

    @Override
    public void createDebit(Transaction transaction) {
        create(transaction, TYPE.DEBIT);
    }

    @Override
    public void createTransaction(Transaction transaction) {
        create(transaction, TYPE.TRANSACTION);
    }

    private void create(Transaction transaction, TYPE type) {
        List<Object> args = new ArrayList<>(4);
        switch (type) {
            case CREDIT:
                args.add(null);
                args.add(transaction.getIdAccountTo());
                break;
            case DEBIT:
                args.add(transaction.getIdAccountFrom());
                args.add(null);
                break;
            case TRANSACTION:
                args.add(transaction.getIdAccountFrom());
                args.add(transaction.getIdAccountTo());
                break;
        }
        args.add(new Timestamp(transaction.getDate().getTime()));
        args.add(transaction.getAmount());
        int insertedCnt = jdbcTemplate.update(Query.Transaction.INSERT, args.toArray());
        if (insertedCnt != 1) {
            throw new BankDAOException(transaction.toString() + "was not saved");
        }
    }

    private static final class TransactionMapper implements RowMapper<TransactionFull> {
        @Override
        public TransactionFull mapRow(ResultSet rs, int i) throws SQLException {
            Transaction transaction;
            Client clientFrom, clientTo;
            Account accountFrom, accountTo;
            String accountTitle;
            Integer clientId;
            transaction = new Transaction(
                    rs.getInt(Query.Transaction.FIND_ALL_FULL_ID),
                    rs.getTimestamp(Query.Transaction.FIND_ALL_FULL_DATE),
                    rs.getBigDecimal(Query.Transaction.FIND_ALL_FULL_AMOUNT));
            accountTitle = rs.getString(Query.Transaction.FIND_ALL_FULL_AC_FROM_TITLE);
            if (accountTitle == null) {
                accountFrom = null;
            } else {
                accountFrom = new Account(accountTitle);
            }

            clientId = rs.getInt(Query.Transaction.FIND_ALL_FULL_CLIENT_FROM_ID);
            if (rs.wasNull()) {
                clientFrom = null;
            } else {
                clientFrom = new Client(
                        clientId,
                        rs.getString(Query.Transaction.FIND_ALL_FULL_CLIENT_FROM_NAME),
                        rs.getString(Query.Transaction.FIND_ALL_FULL_CLIENT_FROM_SURNAME));
            }
            accountTitle = rs.getString(Query.Transaction.FIND_ALL_FULL_AC_TO_TITLE);
            if (accountTitle == null) {
                accountTo = null;
            } else {
                accountTo = new Account(accountTitle);
            }
            clientId = rs.getInt(Query.Transaction.FIND_ALL_FULL_CLIENT_TO_ID);
            if (rs.wasNull()) {
                clientTo = null;
            } else {
                clientTo = new Client(
                        clientId,
                        rs.getString(Query.Transaction.FIND_ALL_FULL_CLIENT_TO_NAME),
                        rs.getString(Query.Transaction.FIND_ALL_FULL_CLIENT_TO_SURNAME));
            }
            return new TransactionFull(transaction, clientFrom, clientTo, accountFrom, accountTo);
        }
    }
}
