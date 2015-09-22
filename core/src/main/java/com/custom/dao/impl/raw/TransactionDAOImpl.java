package com.custom.dao.impl.raw;

import com.custom.dao.TransactionDAO;
import com.custom.dao.util.Query;
import com.custom.entity.Account;
import com.custom.entity.Client;
import com.custom.entity.Transaction;
import com.custom.entity.composite.TransactionFull;
import com.custom.exception.BankDAOException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 18.09.15.
 */
public class TransactionDAOImpl implements TransactionDAO {
    private enum TYPE {TRANSACTION, CREDIT, DEBIT}

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Transaction> findAll() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(Query.Transaction.FIND_ALL)) {
            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt(Query.Transaction.FIND_BY_ID_ID),
                        rs.getInt(Query.Transaction.FIND_BY_ID_ACCOUNT_FROM),
                        rs.getInt(Query.Transaction.FIND_BY_ID_ACCOUNT_TO),
                        rs.getTimestamp(Query.Transaction.FIND_BY_ID_DATE),
                        rs.getBigDecimal(Query.Transaction.FIND_BY_ID_AMOUNT)));
            }
            return transactions;
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }

    @Override
    public List<TransactionFull> findAllFull() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(Query.Transaction.FIND_ALL_FULL)) {
            List<TransactionFull> transactions = new ArrayList<>();
            Transaction transaction;
            Client clientFrom, clientTo;
            Account accountFrom, accountTo;
            String accountTitle;
            Integer clientId;
            while (rs.next()) {
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
                TransactionFull transactionFull = new TransactionFull(transaction, clientFrom, clientTo, accountFrom, accountTo);
                transactions.add(transactionFull);
            }
            return transactions;
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }

    @Override
    public void createTransaction(Connection conn, Transaction transaction) {
        create(conn, transaction, TYPE.TRANSACTION);
    }

    @Override
    public void createCredit(Connection conn, Transaction transaction) {
        create(conn, transaction, TYPE.CREDIT);
    }

    @Override
    public void createDebit(Connection conn, Transaction transaction) {
        create(conn, transaction, TYPE.DEBIT);
    }

    @Override
    public void createCredit(Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createDebit(Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createTransaction(Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    private void create(Connection conn, Transaction transaction, TYPE type) {
        try (PreparedStatement prStmt = conn.prepareStatement(Query.Transaction.INSERT, Statement.RETURN_GENERATED_KEYS)) {
            switch (type) {
                case CREDIT:
                    prStmt.setNull(1, Types.INTEGER);
                    prStmt.setInt(2, transaction.getIdAccountTo());
                    break;
                case DEBIT:
                    prStmt.setInt(1, transaction.getIdAccountFrom());
                    prStmt.setNull(2, Types.INTEGER);
                    break;
                case TRANSACTION:
                    prStmt.setInt(1, transaction.getIdAccountFrom());
                    prStmt.setInt(2, transaction.getIdAccountTo());
                    break;
            }
            prStmt.setTimestamp(3, new Timestamp(transaction.getDate().getTime()));
            prStmt.setBigDecimal(4, transaction.getAmount());
            if (prStmt.executeUpdate() != 1) {
                throw new BankDAOException("Error with creation " + transaction);
            }
            try (ResultSet rsGeneratedKeys = prStmt.getGeneratedKeys()) {
                if (rsGeneratedKeys.next()) {
                    transaction.setId(rsGeneratedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }
}
